package me.liye.draw.open.domain.param;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.liye.draw.open.domain.Draw;
import me.liye.open.share.rpc.BaseDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by liye on 2025-09-23.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDrawParam extends BaseDTO {
    @NotNull
    String shopDomain;

    String name;
    @NotNull
    Long activityId;

    @Builder.Default
    String status = Draw.DrawStatus.START.name();
}
