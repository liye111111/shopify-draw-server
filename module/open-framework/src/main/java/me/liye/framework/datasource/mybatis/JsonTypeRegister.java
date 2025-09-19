package me.liye.framework.datasource.mybatis;

import java.util.List;

/**
 * 將自定義類型註冊爲數據庫JSON字段
 *
 * @author knight@momo.com
 */
public interface JsonTypeRegister {


    /**
     * @return 需要保存爲json字段的java類型
     */
    List<Class> getRegisterTypes();

}
