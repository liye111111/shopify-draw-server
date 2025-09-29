package me.liye.draw.open.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.dataobject.BaseDataObjectWithEmptyJsonColumn;
import me.liye.open.share.dataobject.JsonDataProperty;

/**
 * 一条抽奖记录
 * Created by liye on 2025-09-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TraceLog extends BaseDataObjectWithEmptyJsonColumn {
    String shopDomain;
    String name;
    @JsonDataProperty
    Object data;
}
