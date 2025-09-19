package me.liye.draw.dao;

import me.liye.draw.domain.Activity;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by liye on 2025-09-19.
 */
@Mapper
public interface ActivityMapper extends BaseMapperPgsql<Activity> {
    String TABLE = "activity";
    String COLUMNS = "id, gmt_create,gmt_modified, is_deleted, status, shop_domain, name, start_time, end_time, draw_rule, json_data";

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
                draw_rule JSONB,
                JSON_DATA JSONB NOT NULL
            );
            """;
}
