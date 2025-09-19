package me.liye.draw.dao;

import me.liye.draw.domain.Ticket;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by liye on 2025-09-19.
 */
@Mapper
public interface TicketMapper extends BaseMapperPgsql<Ticket> {
    String TABLE = "ticket";
    String COLUMNS = "id, gmt_create, gmt_modified, is_deleted,shop_domain,name, status, wallet_id, activity_id, tx_id, amount, json_data";


    String DDL = """
            DROP TABLE if exists ticket ;
            CREATE TABLE ticket (
                ID BIGSERIAL PRIMARY KEY,                                   -- 自增主键
                GMT_CREATE TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), -- 创建时间
                GMT_MODIFIED TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),-- 修改时间
                IS_DELETED BOOLEAN NOT NULL DEFAULT FALSE,                  -- 逻辑删除
                SHOP_DOMAIN VARCHAR(512) NOT NULL,                          -- 店铺域名（Shopify 唯一标识）
                name VARCHAR(128) NOT NULL,
                status VARCHAR(128) NOT NULL,
                wallet_id VARCHAR(128),
                activity_id BIGINT NOT NULL,
                tx_id VARCHAR(128),
                amount VARCHAR(128),
                JSON_DATA JSONB NOT NULL
            );
            """;
}
