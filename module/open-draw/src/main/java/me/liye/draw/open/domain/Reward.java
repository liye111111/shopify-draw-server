package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.rpc.BaseDTO;

/**
 * Created by liye on 2025-09-24.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Reward extends BaseDTO {
    /**
     * 活动ID
     */
    Long activityId;

    Long orderId;

    Long ticketId;

    /**
     * 中奖钱包地址
     */
    String rewardWalletAddress;
    /**
     * token数量
     */
    String amount;

    /**
     * 交易hash
     */
    String txHash;
}
