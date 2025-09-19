package me.liye.open.share.page;

/**
 * 聲明mapper select方法的入參支持分頁查詢參數，推薦DTO直接繼承BasePageQuery
 *
 * @author knight@momo.com
 */
public interface PageQuerySupport {

    /**
     * 頁碼,start with 1
     *
     * @return
     */
    int getPageIndex();

    /**
     * 每頁行數
     *
     * @return
     */
    int getPageSize();

    /**
     * 是否需要查詢總數
     *
     * @return
     */
    boolean isNeedTotal();


}
