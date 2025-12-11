package me.liye.draw.core.service;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.DrawMapper;
import me.liye.draw.core.util.DrawUtil;
import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.ActivityTargetEntry;
import me.liye.draw.open.domain.Draw;
import me.liye.draw.open.domain.Ticket;
import me.liye.draw.open.domain.enums.ActivityStatus;
import me.liye.draw.open.domain.enums.DrawStatus;
import me.liye.draw.open.domain.enums.TicketStatus;
import me.liye.draw.open.domain.param.CreateDrawParam;
import me.liye.draw.open.domain.param.GetActivityParam;
import me.liye.draw.open.domain.param.ListDrawParam;
import me.liye.draw.open.domain.param.ListTicketParam;
import me.liye.open.share.util.TypeConvertor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by liye on 2025-09-19.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DrawService {
    final ActivityService activityService;
    final DrawMapper drawMapper;
    final TicketService ticketService;

//    @Value("${applyRewardApi:http://localhost:8080/mock/applyReward}")
//    String applyRewardApi;

    @Value("${draw.serviceFeeRate:0.03}")
    Double serviceFeeRate;

    @Value("${draw.rewardRandomFactor:0.2}")
    Double rewardRandomFactor;

    final static ExecutorService executorService = new ThreadPoolExecutor(
            4,
            16,
            60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(256),
            new NamedThreadFactory("DrawService-", false),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public Draw create(CreateDrawParam param) {
        Draw row = TypeConvertor.convert(param, Draw.class);
        drawMapper.insert(row);
        DrawTask task = new DrawTask(
                row.getId(),
                param.isForce());

        if (param.isSyncRun()) {
            return task.syncRun();
        } else {
            executorService.execute(
                    task
            );
            return drawMapper.selectById(row);
        }


    }

    public List<Draw> list(ListDrawParam param) {
        return drawMapper.list(param);
    }


    class DrawTask implements Runnable {
        Long id;
        boolean force;

        public DrawTask(Long id, boolean force) {
            this.id = id;
            this.force = force;
        }

        @Override
        public void run() {
            log.info("start draw,id: {}", id);
            Draw draw = drawMapper.selectById(Draw.builder().id(id).build());
            Activity activity = activityService.activityMapper.selectById(
                    Activity.builder()
                            .id(draw.getActivityId())
                            .build());


            List<Ticket> tickets = ticketService.list(
                    ListTicketParam.builder()
                            .activityId(draw.getActivityId())
                            .includeOrder(true)
                            .build()
            );


            draw(tickets, activity, draw);

            for (Ticket ticket : tickets) {
                if (!ticket.getStatus().equals(TicketStatus.SKIP_BY_ORDER_PRICE.name())) {
                    ticketService.updateDrawResult(ticket.getId(), ticket.getStatus(), ticket.getAmount(), ticket.getRandomSeed());
                }
            }
            drawMapper.updateById(draw);
        }

        public Draw syncRun() {
            Draw draw = drawMapper.selectById(Draw.builder().id(id).build());
            draw.setStatus(DrawStatus.START.name());
            drawMapper.updateById(draw);
            //

            Activity activity = activityService.get(
                    GetActivityParam.builder().id(draw.getActivityId()).build());


            List<Ticket> tickets = ticketService.list(
                    ListTicketParam.builder()
                            .activityId(draw.getActivityId())
                            .includeOrder(true)
                            .build()
            );

            activityService.updateStatus(activity.getId(), ActivityStatus.DRAW_RUNNING);

            draw(tickets, activity, draw);

            for (Ticket ticket : tickets) {
                if (!ticket.getStatus().equals(TicketStatus.SKIP_BY_ORDER_PRICE.name())) {
                    ticketService.updateDrawResult(ticket.getId(), ticket.getStatus(), ticket.getAmount(), ticket.getRandomSeed());
                }
            }
            draw.setTickets(tickets);
            draw.setStatus(DrawStatus.END.name());
            drawMapper.updateById(draw);
            // 更新为抽奖完成
            activityService.updateStatus(activity.getId(), ActivityStatus.DRAW_COMPLETED);
            return draw;
        }

        private void draw(List<Ticket> tickets, Activity activity,
                          Draw draw) {
            // 达成的gmv,含不满足抽奖目标的订单
            double totalGmv = tickets.stream()
                    .mapToDouble(ticket -> Double.parseDouble(ticket.getOrderPrice()))
                    .sum();

            double serviceFee = totalGmv * serviceFeeRate;
            draw.setServiceFee(String.valueOf(serviceFee));

            List<ActivityTargetEntry> gmtTargets = activity.getActivityTarget().getEntries();
            // 计算梯度gmv目标
            for (ActivityTargetEntry gmtTarget : gmtTargets) {
                gmtTarget.setGmvAmount(
                        String.valueOf(
                                gmtTarget.getPercent() / 100.0 * Double.parseDouble(activity.getActivityTarget().getGmvTarget())));
            }
            gmtTargets.sort((o1, o2) -> o2.getGmvAmount().compareTo(o1.getGmvAmount()));
            // 满足gmv要求的最高梯度目标
            ActivityTargetEntry gmtTarget = gmtTargets.stream()
                    .filter(it -> totalGmv >= Double.parseDouble(it.getGmvAmount()))
                    .findFirst().orElse(null);

            if (gmtTarget == null) {
                log.warn("no gmt target complete，activityId={},totalGmv={}", activity.getId(), totalGmv);
                draw.setInfo("No matching GMV target found.");
                for (Ticket ticket : tickets) {
                    ticket.setStatus(TicketStatus.SKIP_BY_GMV_TARGET.name());
                }

                return;
            }

            // 奖池扣除服务费
            double reward = Double.parseDouble(gmtTarget.getRewardAmount());
            // 保存命中的梯度目标
            draw.setReward(gmtTarget.getRewardAmount());

            // 剩余奖池金额
            reward = reward - serviceFee;
            if (reward <= 0) {
                log.warn("no reward,activityId={},totalGmv={}", activity.getId(), totalGmv);
                draw.setInfo("Draw taget balance is zero after service fee deduction.");
                for (Ticket ticket : tickets) {
                    ticket.setStatus(TicketStatus.SKIP_BY_SERVICE_FEE.name());
                }
                return;
            }


            // 去掉不满足梯度目标的ticket
            tickets = tickets.stream().filter(it ->
                    !TicketStatus.SKIP_BY_ORDER_PRICE.name().equals(it.getStatus())

            ).toList();

            Map<String, Double> ticketWeightMap = tickets.stream()
                    .collect(Collectors.toMap(
                            Ticket::getTicketSn,
                            it -> Double.parseDouble(it.getOrderPrice())
                    ));

            Map<String, Double> drawResult = DrawUtil.draw(ticketWeightMap,
                    activity.getDrawRule().getUserLimitCount() == null || activity.getDrawRule().getUserLimitCount() <= 0 ? Integer.MAX_VALUE : activity.getDrawRule().getUserLimitCount(),
                    reward,
                    rewardRandomFactor
            );

            for (Ticket ticket : tickets) {
                Double ticketAmount = drawResult.getOrDefault(ticket.getTicketSn(), null);
                if (ticketAmount == null) {
                    log.info("ticket lose,ticketId={}", ticket.getId());
                    ticket.setStatus(TicketStatus.LOSE.name());
                } else {
                    log.info("ticket win,ticketId={},amount={}", ticket.getId(), ticketAmount);
                    ticket.setStatus(TicketStatus.WIN.name());
                    ticket.setAmount(String.valueOf(ticketAmount));
                }

            }
        }
    }

//        private void callWeb3Api(Ticket ticket) {
//            // call web3 contract api
//            ApplyRewardParam param = ApplyRewardParam.builder()
//                    .shopDomain(ticket.getShopDomain())
//                    .activityId(ticket.getActivityId())
//                    .rewardWalletAddress(ticket.getWalletAddress())
//                    .amount(ticket.getAmount())
//                    .build();
//            String body = "";
//            Exception ex = null;
//            try (HttpResponse resp = HttpUtil.createPost(applyRewardApi)
//                    .body(JSON.toJSONString(param), "application/json")
//                    .execute()) {
//                body = resp.body();
//                log.info("call web3 api, status: {}, body: {}", resp.getStatus(), body);
//                String txId = body;
//                ticket.setTxId(txId);
//                ticketMapper.updateTxId(ticket.getId(), txId);
//            } catch (Exception e) {
//                ex = e;
//                log.error("call web3 api error", e);
//            } finally {
//                List<String> stack = ExceptionUtils.getRootCauseStackTrace(ex, 10, "me.liye.");
//                traceLogService.create(TraceLog.builder()
//                        .shopDomain(ticket.getShopDomain())
//                        .name("callWeb3Api")
//                        .data(Map.of(
//                                        "url", applyRewardApi,
//                                        "requestBody", param,
//                                        "responseBody", body,
//                                        "error", stack
//                                )
//                        )
//                        .build());
//            }
//        }

}
