package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.dataobject.BaseDataObjectWithEmptyJsonColumn;

/**
 * 一条抽奖记录
 * Created by liye on 2025-09-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends BaseDataObjectWithEmptyJsonColumn {

    String name;

    /**
     * 抽奖票编号
     */
    String ticketSn;

    /**
     * 抽奖人邮箱
     */
    String email;

    /**
     * 抽奖人钱包地址
     */
    String walletAddress;

    /**
     * 抽奖活动ID
     */
    Long activityId;

    /**
     * 抽奖活动规则名称
     */
    String activityRuleName;

    Long orderId;
    String orderPrice;
    String orderCurrency;

    /**
     * 抽奖票对应的奖池ID
     */
//    String walletId;

    /**
     * 链上交易ID（用于验证支付/参与记录）
     */
    String txId;

    /**
     * 抽奖票状态: PENDING / WIN / LOSE
     */
    String status;

    /**
     * 抽奖时生成的随机数 (0 ~ 1 的小数，或 long 值)
     */
    String randomSeed;

    String amount;

    public enum TicketStatus {
        PENDING, WIN, LOSE
    }
}
