package me.liye.open.share.rpc;

import me.liye.open.share.page.PageQuerySupport;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 分頁查詢入參的基類，提供分頁參數
 * @author knight@momo.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasePageQuery extends BaseDTO implements PageQuerySupport {

    @Builder.Default
    int pageIndex = 1;
    @Builder.Default
    int pageSize = 20;
    @Builder.Default
    boolean needTotal = true;

}
