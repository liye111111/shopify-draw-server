package me.liye.draw.open.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.rpc.BaseDTO;

/**
 * 获取活动目标模板参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GetActivityTargetTemplateParam extends BaseDTO {
    /**
     * gmv目标
     */
    String gmvTarget;
    /**
     * 奖池目标
     */
    String rewardTarget;

    /**
     * 模板id
     */
    String templateId;
}
