package me.liye.draw.open.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.rpc.BaseDTO;

import java.util.Date;

/**
 * Created by liye on 2025-09-23.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CreateActivityParam extends BaseDTO {

    /**
     * 活动名称
     */
    String name;
    /**
     * 活动描述
     */
    String description;

    /**
     * 活动开始时间
     */
    Date startTime;
    /**
     * 活动结束时间
     */
    Date endTime;

    /**
     * 最小订单金额
     */
    String miniOrderSingleSpend;
    /**
     * 参与抽奖人数限制
     */
    String participantLimit;


}
