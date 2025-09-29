package me.liye.draw.core.dao;

import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.TraceLog;
import me.liye.draw.open.domain.param.ListTraceLogParam;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import me.liye.open.share.page.PageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by liye on 2025-09-19.
 */
@Mapper
public interface TraceLogMapper extends BaseMapperPgsql<TraceLog> {
    String DDL = """
            DROP TABLE if exists trace_log ;
            CREATE TABLE trace_log (
                ID BIGSERIAL PRIMARY KEY,
                GMT_CREATE TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                GMT_MODIFIED TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                SHOP_DOMAIN VARCHAR(512) NOT NULL,
                name VARCHAR(128) NOT NULL,
                JSON_DATA JSONB NOT NULL
            );
            """;

    String TABLE = "trace_log";
    String COLUMNS = "id, gmt_create,gmt_modified, shop_domain, name, json_data";


    @PageQuery
    @Select("SELECT " + COLUMNS + " FROM " + TABLE + " ORDER BY GMT_CREATE DESC")
    List<Activity> list(ListTraceLogParam param);
}
