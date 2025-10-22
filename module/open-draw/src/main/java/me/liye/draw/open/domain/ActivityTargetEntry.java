package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.rpc.BaseDTO;

/**
 * 活动目标条目
 * Created by liye on 2025-10-20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityTargetEntry extends BaseDTO {
    /**
     * 完成百分比：0-100
     */
    int percent;
    /**
     * 奖池金额
     */
    String rewardAmount;


}
