package me.liye.draw.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.ShopifyOrderMapper;
import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.ShopifyOrder;
import me.liye.draw.open.domain.Ticket;
import me.liye.draw.open.domain.param.CreateTicketParam;
import me.liye.draw.open.domain.param.ListShopifyOrderParam;
import me.liye.draw.open.domain.param.ListTicketParam;
import me.liye.open.share.util.UUIDUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liye on 2025-09-17.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    final ShopifyOrderMapper shopifyOrderMapper;
    final ActivityService activityService;
    final TicketService ticketService;

    public ShopifyOrder create(ShopifyOrder order) {
        try {
            // 保存order记录
            shopifyOrderMapper.insert(order);
            // 查询有效活动
            Activity activity = activityService.getMatchedActivity(order);
            if (activity == null) {
                log.info("no matched activity for order: {}", order.getOrderId());
            } else {
                // 符合条件，发放ticket
                Ticket ticket = ticketService.create(CreateTicketParam.builder()
                        .ticketSn(UUIDUtil.generateShortTicketSn("T-"))// 生成ticketSn
                        .shopDomain(order.getShopDomain())
                        .orderId(order.getId())
                        .activityId(activity.getId())
                        .activityRuleName(activity.getDrawRule().getName())
                        .email(order.getEmail())
                        .orderId(order.getId())
                        .orderPrice(order.getPrice())
                        .orderCurrency(order.getCurrency())
//                        .amount(activity.getDrawRule().getMinRewardAmount())
                        .build());
                log.info("matched activity: {}, create ticket {} for order: {}", activity.getId(), ticket.getId(), order.getOrderId());
            }
        } catch (DuplicateKeyException e) {
            log.warn("duplication order insert,skip it: {}", order.getOrderId(), e);
        }
        return order;

    }

    public List<ShopifyOrder> list(ListShopifyOrderParam param) {
        List<ShopifyOrder> rows = shopifyOrderMapper.list(param);
        if (rows.isEmpty()) {
            return rows;
        }

        Map<Long, Ticket> ticketMap = ticketService.list(ListTicketParam.builder()
                        .orderIds(rows.stream().map(ShopifyOrder::getId).toList())
                        .build())
                .stream().collect(Collectors.toMap(Ticket::getOrderId, it -> it));

        rows.forEach(it -> it.setTicket(ticketMap.get(it.getId())));

        return rows;
    }
}
