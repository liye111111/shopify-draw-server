package me.liye.draw.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.service.OrderService;
import me.liye.draw.open.domain.ShopifyOrder;
import me.liye.draw.open.domain.param.ListShopifyOrderParam;
import me.liye.open.share.rpc.RpcResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static me.liye.open.share.rpc.RpcResult.success;

/**
 * Created by liye on 2025-09-19.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    final OrderService orderService;

    @RequestMapping("/list")
    public RpcResult<List<ShopifyOrder>> listShopifyOrder(ListShopifyOrderParam param) {
        return success(orderService.list(param));
    }
}
