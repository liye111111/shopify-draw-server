package me.liye.draw.open.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.draw.open.domain.enums.TicketStatus;
import me.liye.open.share.rpc.BasePageQuery;

import java.util.List;

/**
 * Created by liye on 2025-09-22.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ListTicketParam extends BasePageQuery {
    String name;
    String email;
    Long activityId;

    List<Long> orderIds;
    List<Long> activityIds;

    TicketStatus status;

    boolean includeOrder;
}
