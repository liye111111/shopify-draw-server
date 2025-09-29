package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.rpc.BaseDTO;

/**
 * 抽奖规则
 * "walletAddress": "{{$string.alpha(length=32)}}",
 * "orderPriceThreshold": "{{$number.int(min=1,max=100)}}",
 * "rewardAmount" : "{{$number.int(min=1,max=10)}}",
 * "drawRate": "{{$number.float(min=0,max=1,fractionDigits=4)}}"
 * <p>
 * Created by liye on 2025-09-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRule extends BaseDTO {
    String name;
    String walletAddress;
    String orderPriceThreshold;
    int rewardAmount;
    /**
     * 抽奖概率,[0-1]
     */
    double winningProbability;
}
