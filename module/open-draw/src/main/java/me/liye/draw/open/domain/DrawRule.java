package me.liye.draw.open.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.liye.draw.open.domain.enums.DrawStrategy;
import me.liye.open.share.rpc.BaseDTO;

/**
 * 抽奖规则
 * Created by liye on 2025-10-20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DrawRule extends BaseDTO {
    String name;
    /**
     * 抽奖策略
     */
    DrawStrategy strategy;
    /**
     * 是否限制用户参与
     */
    Boolean userLimit;
    /**
     * 用户限制人数
     */
    Integer userLimitCount;

    /**
     * 是否包含税
     */
    Boolean gmvIncludeTax;
    /**
     * 是否包含运费
     */
    Boolean gmvIncludeFreight;
    /**
     * 是否包含优惠
     */
    Boolean gmvIncludePromotion;

    /**
     * 最小中奖金额
     */
    String minRewardAmount;

    /**
     * 中奖概率
     */
    @Builder.Default
    double winRate = 0.5;

}
