package me.liye.open.share.page;

import me.liye.open.share.rpc.Pagination;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

/**
 * 分頁查詢結果，繼承了List，用於增強mapper的select方法
 * @author knight@momo.com
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryResult<E> extends ArrayList<E> {
    /**
     * 分頁參數
     */
    Pagination page;
}
