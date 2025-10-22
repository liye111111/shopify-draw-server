package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.rpc.BaseDTO;

import java.util.List;

/**
 * 活动目标
 * Created by liye on 2025-10-20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityTarget extends BaseDTO {
    /**
     * gmv目标
     */
    String gmvTarget;
    /**
     * 奖池目标
     */
    String rewardTarget;

    /**
     * 奖池比例
     */
    Double rewardPercent;
    /**
     * 梯度配置
     */
    List<ActivityTargetEntry> entries;
}
