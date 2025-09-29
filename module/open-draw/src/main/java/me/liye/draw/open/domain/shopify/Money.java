package me.liye.draw.open.domain.shopify;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by liye on 2025-09-19.
 */
@Data
public class Money {
    @JSONField(name = "amount")
    String amount;
    @JSONField(name = "currency_code")
    String currencyCode;
}
