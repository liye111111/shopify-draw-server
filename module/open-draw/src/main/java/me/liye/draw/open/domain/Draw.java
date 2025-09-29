package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.dataobject.BaseDataObjectWithJsonColumn;

import java.util.Date;

/**
 * 描述一次抽奖过程
 * Created by liye on 2025-09-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Draw extends BaseDataObjectWithJsonColumn {

    String shopDomain;
    String name;
    String status;
    Date startTime;
    Date endTime;

    Long activityId;

    public enum DrawStatus {
        START,
        END
    }
}
