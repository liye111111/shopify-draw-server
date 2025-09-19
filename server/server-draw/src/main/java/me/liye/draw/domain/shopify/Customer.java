package me.liye.draw.domain.shopify;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by liye on 2025-09-19.
 */
@Data
public class Customer {
    String id;
    String email;
    @JSONField(name = "first_name")
    String firstName;
    @JSONField(name = "last_name")
    String lastName;
}
