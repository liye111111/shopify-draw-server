package me.liye.open.share.rpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分頁結果參數
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination implements Serializable {

    /**
     * 頁碼，從1開始
     */
    @Builder.Default
    Integer pageIndex = 1;
    /**
     * 頁面大小
     */
    @Builder.Default
    Integer pageSize = 20;
    /**
     * 起始行
     */
    long startRow;
    /**
     * 末行
     */
    long endRow;
    /**
     * 總數
     */
    long total;
    /**
     * 總頁數
     */
    int pages;

    /**
     * 是否爲第一頁
     */
    boolean isFirstPage;
    /**
     * 是否爲最後一頁
     */
    boolean isLastPage;
    /**
     * 是否有前一頁
     */
    boolean hasPreviousPage;
    /**
     * 是否有下一頁
     */
    boolean hasNextPage;
}

