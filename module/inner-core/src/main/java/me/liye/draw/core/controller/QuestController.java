package me.liye.draw.core.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.service.ActivityService;
import me.liye.draw.core.service.QuestEventService;
import me.liye.draw.core.service.TicketService;
import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.QuestEvent;
import me.liye.draw.open.domain.Ticket;
import me.liye.draw.open.domain.enums.ActivityStatus;
import me.liye.draw.open.domain.param.GetActivityParam;
import me.liye.draw.open.domain.param.ListActivityParam;
import me.liye.draw.open.domain.param.ListTicketParam;
import me.liye.open.share.page.PageQueryResult;
import me.liye.open.share.rpc.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 提供给web3的活动api
 * Created by liye on 2025-09-19.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quest")
public class QuestController {
    // 5% * 10e9
    public static final long DENOM = 1_000_000_000L;  // 精度扩大倍数
    public static final long SERVICE_FEE_RATE = 50_000_000L; // 5% = 0.05 * DENOM
    final ActivityService activityService;
    final TicketService ticketService;
    final QuestEventService questEventService;

    /**
     * 活动列表，含奖励
     */
    @GetMapping("/list")
    public Result<List<Quest>> list(
            @RequestParam("status") String status,
            @RequestParam(name = "start_at_gt", required = false) Date startAtGt,
            @RequestParam(name = "start_at_lt", required = false) Date startAtLt,
            @RequestParam(name = "page_index", defaultValue = "1") Integer pageIndex,
            @RequestParam(name = "page_size", defaultValue = "20") Integer pageSize
    ) {


        List<Activity> activities = activityService.list(ListActivityParam.builder()
                .status(status)
                .startAtGrateThen(startAtGt)
                .startAtLessThen(startAtLt)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build());

        List<Quest> quests = buildQuests(activities);

        return Result.success(quests);
    }

    /**
     * 构建quest属性
     */
    private List<Quest> buildQuests(List<Activity> activities) {
        if (activities.isEmpty()) {
            return Lists.newArrayList();
        }

        List<Long> activityIds = activities.stream().map(Activity::getId).toList();

        // 活动:tickets
        Map<Long, List<Ticket>> ticketMap = ticketService.list(ListTicketParam.builder()
                        .activityIds(activityIds)
                        .status(Ticket.TicketStatus.WIN)
                        .build())
                .stream()
                .collect(Collectors.groupingBy(Ticket::getActivityId));
        // 按buyer合并
        Map<Long, List<QuestItem>> questItemMap = Maps.newHashMap();

        for (Map.Entry<Long, List<Ticket>> et : ticketMap.entrySet()) {
            Long activityId = et.getKey();
            List<Ticket> tickets = et.getValue();
            // 每个Activity每个email对应一个item
            Map<String, QuestItem> userQuestItemMap = Maps.newHashMap();
            for (Ticket ticket : tickets) {
                QuestItem existingItem = userQuestItemMap.get(ticket.getEmail());
                QuestItem newItem = toQuestItem(ticket);

                if (existingItem == null) {
                    userQuestItemMap.put(ticket.getEmail(), newItem);
                }
                // email和address必须匹配
                else if (StringUtils.equals(newItem.getAddress(), existingItem.getAddress())) {
                    long amount = (existingItem.getAmount() == null ? 0 : existingItem.getAmount()) + (newItem.getAmount() == null ? 0 : newItem.getAmount());
                    existingItem.setAmount(amount);
                    existingItem.getTicketIds().addAll(newItem.getTicketIds());
                }

                // 忽略email和address不匹配的异常数据
                else {
                    log.warn("ticket email and walletAddress not match:expect={},ticket={}", existingItem.getAddress(), ticket.dump());
                }

            }

            questItemMap.put(activityId, userQuestItemMap.values().stream().toList()
                    .stream()
                    .sorted(Comparator.comparing(QuestItem::getEmail))
                    .toList()
            );
        }

//
//        Map<Long, List<QuestItem>> questItemMap = ticketService.list(ListTicketParam.builder()
//                        .activityIds(activityIds)
//                        .status(Ticket.TicketStatus.WIN)
//                        .build())
//                .stream()
//                .map(QuestController::toQuestItem)
//                .collect(Collectors.groupingBy(it -> it.getQuestId()));

        return activities.stream().map(it -> {
            Quest quest = toQuest(it);
            List<QuestItem> items = questItemMap.get(it.getId());
            quest.setItem(items);
            return quest;
        }).toList();
    }

    /**
     * 活动基本信息
     */
    @GetMapping("/get")
    public Result<Quest> get(@RequestParam("quest_id") Long questId,
                             @RequestParam(name = "include_items", defaultValue = "false") boolean includeItems) {

        Activity row = activityService.get(GetActivityParam.builder()
                .id(questId)
                .build());

        if (row == null) {
            return Result.of(404, "Not Found", null);
        }
        if (includeItems) {
            return Result.success(buildQuests(List.of(row)).get(0));
        } else {
            return Result.success(toQuest(row));
        }
    }

    /**
     * 接收web3的quest事件
     */
    @PostMapping("/event")
    public Result<Map<String, ?>> event(@RequestBody QuestEvent param) {
//        activityService.update(UpdateActivityParam.builder()
//                .id(param.getQuestId())
//                .status(param.getStatus())
//                .build());
        String info = questEventService.receiveEvent(param);
        return Result.success(Map.of("task-id", param.getId(), "info", info));
    }

    private static Quest toQuest(Activity row) {
        return Quest.builder()
                .questId(row.getId())
                .status(row.getStatus())
                .totalAmount(toLong(row.getActivityTarget().getRewardTarget()))
                .startAt(row.getStartTime())
                .endAt(row.getEndTime())
                .merchantAddress(row.getWalletAddress())
                .rewardToken(row.getRewardToken())
                // 服务费，mock
                .serviceFee(10 * SERVICE_FEE_RATE)
                .build();
    }

    private static QuestItem toQuestItem(Ticket ticket) {
        return QuestItem.builder()
                .ticketIds(Lists.newArrayList(ticket.getId()))
                .questId(ticket.getActivityId())
                .email(ticket.getEmail())
                .address(ticket.getWalletAddress())
                .amount(toLong(ticket.getAmount()))
                .build();
    }

    private static Long toLong(String value) {
        return new BigDecimal(value)
                .multiply(BigDecimal.valueOf(1_000_000_000L)) //10e9
                .setScale(0, RoundingMode.DOWN) // TODO：从活动配置取
                .longValue();
    }

    /**
     * quest/活动的基本信息
     * {
     * "code": 200,
     * "data": {
     * "quest_id": 1,
     * "total_amount": 100000000, //奖励数量
     * "start_at": 1760667157, // 空投开始时间
     * "end_at": 1760667157, // 空投结束时间，不是活动结束时间，如果活动结束时间无法预测，需要预留足够的时间给用户领取空投
     * "merchant_address":"", // 商户钱包地址
     * "reward_token": "", // 奖励的token 地址, 如usdc/usdt地址，dev用 DCDpBz2wzXpX4rD1F7o9jfxnzGEJ4AsP4TgDaaVi6ude，
     * },
     * "message": "Success"
     * }
     */

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Quest {
        @JsonProperty("status")
        ActivityStatus status;
        @JsonProperty("quest_id")
        Long questId;
        @JsonProperty("total_amount")
        Long totalAmount;
        @JsonProperty("start_at")
        Date startAt;
        @JsonProperty("end_at")
        Date endAt;
        @JsonProperty("merchant_address")
        String merchantAddress;
        @JsonProperty("reward_token")
        String rewardToken;
        @JsonProperty("item")
        List<QuestItem> item;

        @JsonProperty("service_fee")
        Long serviceFee;
    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestItem {
        @JsonProperty("quest_id")
        Long questId;
        @JsonProperty("email")
        String email;
        @JsonProperty("address")
        String address;
        @JsonProperty("amount")
        Long amount;

        @JsonProperty("ticket_ids")
        List<Long> ticketIds;
    }


    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateQuestStatusParam {
        @JsonProperty("quest_id")
        Long questId;
        @JsonProperty("status")
        ActivityStatus status;
    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result<T> {
        int code;
        String message;
        T data;
        Pagination page;

        public static <T> Result<T> success(T data) {
            Result<T> r = Result.<T>builder()
                    .code(200)
                    .message("Success")
                    .data(data)
                    .build();
            if (data instanceof PageQueryResult) {
                r.page = ((PageQueryResult<?>) data).getPage();
            }
            return r;
        }

        public static <T> Result<T> of(int code, String message, T data) {
            return Result.<T>builder()
                    .code(code)
                    .message(message)
                    .data(data)
                    .build();
        }
    }

}
