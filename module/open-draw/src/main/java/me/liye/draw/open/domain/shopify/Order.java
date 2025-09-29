package me.liye.draw.open.domain.shopify;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by liye on 2025-09-19.
 */
@Data
public class Order {
    @JSONField(name = "customer")
    Customer customer;

    @JSONField(name = "current_total_price_set")
    private PriceSet currentTotalPriceSet;
}
