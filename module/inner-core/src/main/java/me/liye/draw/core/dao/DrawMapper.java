package me.liye.draw.core.dao;

import me.liye.draw.open.domain.Draw;
import me.liye.draw.open.domain.param.ListDrawParam;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import me.liye.open.share.page.PageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by liye on 2025-09-23.
 */
@Mapper
public interface DrawMapper extends BaseMapperPgsql<Draw> {
    String DDL = """
            DROP TABLE if exists draw ;
            CREATE TABLE draw (
                ID BIGSERIAL PRIMARY KEY,                                   -- 自增主键
                GMT_CREATE TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), -- 创建时间
                GMT_MODIFIED TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),-- 修改时间
                IS_DELETED BOOLEAN NOT NULL DEFAULT FALSE,                  -- 逻辑删除
                SHOP_ID VARCHAR(512) NOT NULL,                          -- 店铺域名（Shopify 唯一标识）
                activity_id bigint,
                name VARCHAR(128),
                status VARCHAR(128) NOT NULL,
                start_time TIMESTAMP,
                end_time TIMESTAMP,
                JSON_DATA JSONB NOT NULL
            );
            """;

    String TABLE = "draw";
    String COLUMNS = "id, gmt_create,gmt_modified, is_deleted, status, shop_id,activity_id, name, start_time, end_time, json_data";

    @PageQuery
    @SelectProvider(type = InnerSqlProvider.class, method = "list")
    List<Draw> list(ListDrawParam param);

    class InnerSqlProvider {
        public String list(ListDrawParam param) {
            return """
                    <script>
                    SELECT %s FROM %s WHERE IS_DELETED = FALSE
                    <if test="shopId != null">and shop_id=#{shopId}</if>
                    <if test="activityId != null">and activity_id=#{activityId}</if>                   
                    order by gmt_create desc
                    </script>
                    """.formatted(COLUMNS, TABLE);
        }
    }
}
