package me.liye.draw.open.domain.enums;

/**
 *  抽奖策略
 * Created by liye on 2025-10-20.
 */
public enum DrawStrategy {

    /**
     * 等权重抽奖（所有订单中奖概率相同）
     * 适合：简单活动、人数较少的抽奖
     */
    EQUAL_WEIGHT,

    /**
     * 金额加权抽奖（订单金额越高，中奖金额越高）
     * 适合：消费越多中奖越多的活动
     */
    AMOUNT_WEIGHTED,

    /**
     * 等权重 + 金额浮动抽奖
     * 奖金围绕金额占比上下浮动（±p%）
     * 且自动归一化保证奖池金额用完
     */
    AMOUNT_WEIGHTED_WITH_FLUCTUATION,

    /**
     * 固定金额分配（非随机）
     * 平均或按比例瓜分奖池
     */
    FIXED_DISTRIBUTION,

    /**
     * 动态预算控制型抽奖
     * 奖池余额影响中奖概率或金额
     * 例如：奖池越少，中奖越难或金额越小
     */
    BUDGET_CONTROLLED,

    /**
     * 概率衰减型抽奖
     * 随参与次数或时间递减中奖概率
     */
    PROBABILITY_DECAY,

    /**
     * 排名型抽奖
     * 根据消费金额、积分等指标排名后抽取奖励
     */
    RANK_BASED;

}