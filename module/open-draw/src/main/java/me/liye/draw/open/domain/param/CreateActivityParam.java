package me.liye.draw.open.domain.param;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.ActivityRule;
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
    String shopDomain;
    String name;
    Date startTime;
    Date endTime;
    ActivityRule activityRule;
    @Builder.Default
    String status = Activity.ActivityStatus.START.name();

}
