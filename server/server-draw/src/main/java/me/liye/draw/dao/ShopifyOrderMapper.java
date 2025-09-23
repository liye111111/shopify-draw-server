package me.liye.draw.dao;

import me.liye.draw.domain.ShopifyOrder;
import me.liye.draw.domain.param.ListShopifyOrderParam;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import me.liye.open.share.page.PageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by liye on 2025-09-17.
 */
@Mapper
public interface ShopifyOrderMapper extends BaseMapperPgsql<ShopifyOrder> {
    String DDL = """
            DROP TABLE if exists SHOPIFY_ORDER ;
            CREATE TABLE SHOPIFY_ORDER (
                ID BIGSERIAL PRIMARY KEY,                                   -- 自增主键
                GMT_CREATE TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), -- 创建时间
                GMT_MODIFIED TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),-- 修改时间
                IS_DELETED BOOLEAN NOT NULL DEFAULT FALSE,                  -- 逻辑删除
                SHOP_DOMAIN VARCHAR(512) NOT NULL,                          -- 店铺域名（Shopify 唯一标识）
                TOPIC VARCHAR(128) NOT NULL,                                -- Webhook 主题，比如 orders/paid
                ORDER_ID VARCHAR(128) NOT NULL,                             -- Shopify 订单 ID
                AMOUNT VARCHAR(128) NULL,
                CURRENCY VARCHAR(128) NULL,
                CUSTOMER_ID VARCHAR(256) NULL,
                EMAIL VARCHAR(256) NULL,
                FIRST_NAME VARCHAR(128) NULL,
                LAST_NAME VARCHAR(128) NULL,
                JSON_DATA JSONB NOT NULL,                                   -- 存储订单原始 JSON
                CONSTRAINT uk_shopify_order UNIQUE (SHOP_DOMAIN, TOPIC, ORDER_ID) -- 唯一约束
            );
            """;

    String TABLE = "shopify_order";
    String COLUMNS = "id, gmt_create, gmt_modified, is_deleted,shop_domain,topic,order_id,amount,currency,customer_id,email,first_name,last_name,json_data";


    @PageQuery
    @Select("select " + COLUMNS + " from " + TABLE + " where IS_DELETED=false")
    List<ShopifyOrder> list(ListShopifyOrderParam param);
}
