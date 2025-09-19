package me.liye.draw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.dao.ShopifyOrderMapper;
import me.liye.draw.domain.ShopifyOrder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * Created by liye on 2025-09-17.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    final ShopifyOrderMapper shopifyOrderMapper;

    public ShopifyOrder saveOrder(ShopifyOrder order) {
        try {
            shopifyOrderMapper.insert(order);
        } catch (DuplicateKeyException e) {
            log.warn("duplication order insert,skip it: {}", order.getOrderId(), e);
        }
        return order;

    }
}
