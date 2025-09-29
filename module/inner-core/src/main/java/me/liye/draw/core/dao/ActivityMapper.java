package me.liye.draw.core.dao;

import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.param.ListActivityParam;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import me.liye.open.share.page.PageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by liye on 2025-09-19.
 */
@Mapper
public interface ActivityMapper extends BaseMapperPgsql<Activity> {
    String DDL = """
            DROP TABLE if exists activity ;
            CREATE TABLE activity (
                ID BIGSERIAL PRIMARY KEY,                                   -- 自增主键
                GMT_CREATE TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), -- 创建时间
                GMT_MODIFIED TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),-- 修改时间
                IS_DELETED BOOLEAN NOT NULL DEFAULT FALSE,                  -- 逻辑删除
                SHOP_DOMAIN VARCHAR(512) NOT NULL,                          -- 店铺域名（Shopify 唯一标识）
                name VARCHAR(128) NOT NULL,
                status VARCHAR(128) NOT NULL,
                start_time TIMESTAMP,
                end_time TIMESTAMP,
                activity_rule JSONB,
                JSON_DATA JSONB NOT NULL
            );
            """;

    String TABLE = "activity";
    String COLUMNS = "id, gmt_create,gmt_modified, is_deleted, status, shop_domain, name, start_time, end_time, activity_rule, json_data";


    @PageQuery
    @Select("SELECT " + COLUMNS + " FROM " + TABLE + " WHERE IS_DELETED=false ORDER BY GMT_CREATE DESC")
    List<Activity> list(ListActivityParam param);
}
