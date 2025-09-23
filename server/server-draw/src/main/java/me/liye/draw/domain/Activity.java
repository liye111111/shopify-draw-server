package me.liye.draw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.dataobject.BaseDataObjectWithJsonColumn;

import java.util.Date;

/**
 * 抽奖活动配置
 * Created by liye on 2025-09-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Activity extends BaseDataObjectWithJsonColumn {
    String shopDomain;
    String name;
    String status;
    Date startTime;
    Date endTime;
    ActivityRule activityRule;


    public enum ActivityStatus {
        //有效
        START,
        //无效
        END
    }
}
