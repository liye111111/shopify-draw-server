package me.liye.draw.core.controller;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.service.OrderService;
import me.liye.draw.core.util.HmacUtil;
import me.liye.draw.open.domain.ShopifyOrder;
import me.liye.draw.open.domain.param.ListShopifyOrderParam;
import me.liye.draw.open.domain.shopify.Order;
import me.liye.open.share.rpc.RpcResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liye on 2025-09-17.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/shopify/webhook")
public class ShopifyOrderWebhookController {

    final OrderService orderService;
    final HmacUtil hmacUtil;
    final HttpServletRequest request;

    /**
     * 通用订单 Webhook 接收接口
     */
    @PostMapping("/**")
    public ResponseEntity<String> handleOrderWebhook(
            @RequestBody String payload
    ) {
        try {
            String hmacHeader = request.getHeader("X-Shopify-Hmac-SHA256");
            String topic = request.getHeader("X-Shopify-Topic");
            String shopId = request.getHeader("X-Shopify-Shop-Domain");
            String orderId = request.getHeader("X-Shopify-Order-Id");


            // 1. 校验 HMAC
//            if (!hmacUtil.verifyHmac(payload, hmacHeader)) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("HMAC verification failed");
//            }

            // 2. 解析订单数据（这里只是示例，你可以用 Jackson 或 Gson）
//            ShopifyOrder order = ShopifyOrder.fromJson(payload);

            // 3. 幂等检查
            List<ShopifyOrder> rows = orderService.list(ListShopifyOrderParam.builder()
                    .shopId(shopId)
                    .topic(topic)
                    .orderId(orderId)
                    .includeTickets(false)
                    .build());
            if (!rows.isEmpty()) {
                return ResponseEntity.ok("Order already processed");
            }

            // 4. 调用区块链合约
//            String txHash = blockchainService.deposit(order.getId(), order.getTotalPrice());

            // 5. 存入数据库
            Order order = JSON.parseObject(payload, Order.class);

            ShopifyOrder row = ShopifyOrder.builder()
                    .shopId(shopId)
                    .orderId(orderId)
                    .topic(topic)
                    .herder(getHeaders(request))
                    .originPayload(payload)
                    .payload(JSON.parseObject(payload))
                    .customerId(order.getCustomer().getId())
                    .email(order.getCustomer().getEmail())
                    .firstName(order.getCustomer().getFirstName())
                    .lastName(order.getCustomer().getLastName())
                    .price(order.getCurrentTotalPriceSet().getShopMoney().getAmount())
                    .currency(order.getCurrentTotalPriceSet().getShopMoney().getCurrencyCode())
                    .build();

            orderService.create(row);

            return ResponseEntity.ok(
                    RpcResult.success(
                            "Webhook processed,rowId=%s, ticketId=%s,status=%s".formatted(
                                    row.getId(),
                                    row.getTicket().getId(), row.getTicket().getStatus())).dump()
            );

        } catch (Exception e) {
            log.error(e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }


    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            map.put(name, value);
        }
        return map;
    }

}