package me.liye.open.share.rpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 查詢單個資源的基類
 *
 * @author knight@momo.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSingleQuery extends BaseDTO {

    Long id;

}
