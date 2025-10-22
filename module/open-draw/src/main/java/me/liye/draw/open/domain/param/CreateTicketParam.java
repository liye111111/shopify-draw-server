package me.liye.draw.open.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.rpc.BaseDTO;

/**
 * Created by liye on 2025-09-23.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketParam extends BaseDTO {
    String shopDomain;
    Long activityId;
    String activityRuleName;
    Long orderId;
    String orderPrice;
    String orderCurrency;
    /**
     * 抽奖人邮箱
     */
    String email;
    /**
     * 抽奖人钱包地址
     */
    String walletAddress;

    /**
     * 抽奖金额
     */
    String amount;
}
