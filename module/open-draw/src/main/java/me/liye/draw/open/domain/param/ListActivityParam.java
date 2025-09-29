package me.liye.draw.open.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.rpc.BasePageQuery;

/**
 * Created by liye on 2025-09-22.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ListActivityParam extends BasePageQuery {
    String name;
}
