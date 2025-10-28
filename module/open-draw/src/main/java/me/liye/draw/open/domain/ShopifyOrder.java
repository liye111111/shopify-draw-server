package me.liye.draw.open.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.dataobject.BaseDataObjectWithEmptyJsonColumn;
import me.liye.open.share.dataobject.JsonDataProperty;

import java.util.Map;

/**
 * Created by liye on 2025-09-17.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ShopifyOrder extends BaseDataObjectWithEmptyJsonColumn {

    String topic;
    String orderId;
    String shopDomain;

    @JsonDataProperty
    Object payload;

    @JsonDataProperty
    String originPayload;

    @JsonDataProperty
    Map<String, String> herder;


    String price;

    String currency;

    String customerId;
    String email;
    String firstName;
    String lastName;

    //
    Ticket ticket;
}
