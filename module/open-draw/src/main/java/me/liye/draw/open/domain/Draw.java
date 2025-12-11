package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.dataobject.BaseDataObjectWithEmptyJsonColumn;
import me.liye.open.share.dataobject.JsonDataProperty;

import java.util.Date;
import java.util.List;

/**
 * 描述一次抽奖过程
 * Created by liye on 2025-09-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Draw extends BaseDataObjectWithEmptyJsonColumn {

    String name;
    String status;
    Date startTime;
    Date endTime;

    Long activityId;

    /**
     * 奖池金额
     */
    String reward;

    /**
     * 服务费
     */
    String serviceFee;

    @JsonDataProperty
    String info;

    List<Ticket> tickets;

}
