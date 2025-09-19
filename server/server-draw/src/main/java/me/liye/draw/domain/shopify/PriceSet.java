package me.liye.draw.domain.shopify;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by liye on 2025-09-19.
 */
@Data
public class PriceSet {
    @JSONField(name = "shop_money")
    Money shopMoney;
    @JSONField(name = "presentment_money")
    Money presentmentMoney;
}
