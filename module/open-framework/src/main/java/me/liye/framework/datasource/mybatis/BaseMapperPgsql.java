package me.liye.framework.datasource.mybatis;

import me.liye.framework.datasource.annotation.InjectId;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 *
 */
public interface BaseMapperPgsql<T> {

    @InjectId
    @InsertProvider(type = CommonSqlProviderPgsql.class, method = "insert")
    Integer insert(T row);

    @SelectProvider(type = CommonSqlProviderPgsql.class, method = "selectById")
    T selectById(T row);

    @UpdateProvider(type = CommonSqlProviderPgsql.class, method = "updateById")
    Integer updateById(T row);

    @DeleteProvider(type = CommonSqlProviderPgsql.class, method = "deleteById")
    Integer deleteById(T row);
}

