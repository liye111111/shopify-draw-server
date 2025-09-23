package me.liye.draw.domain.param;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.liye.draw.domain.Activity;
import me.liye.open.share.rpc.BaseDTO;

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
    @Builder.Default
    String status = Activity.ActivityStatus.START.name();

}
