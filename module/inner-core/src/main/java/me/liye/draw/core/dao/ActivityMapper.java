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
            DROP TABLE IF EXISTS activity;
            
            CREATE TABLE activity (
                id BIGSERIAL PRIMARY KEY,                                   -- 自增主键
                gmt_create TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), -- 创建时间
                gmt_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),-- 修改时间
                is_deleted BOOLEAN NOT NULL DEFAULT FALSE,                  -- 逻辑删除
            
                shop_domain VARCHAR(512) NOT NULL,                          -- 店铺域名（Shopify 唯一标识）
                name VARCHAR(128) NOT NULL,                                 -- 活动名称
                description VARCHAR(2048),                                  -- 活动描述
                background_image VARCHAR(2048),                             -- 活动背景图
                preview_image VARCHAR(2048),                                -- 预览图
            
                status VARCHAR(128) NOT NULL,                               -- 活动状态（例如 DRAFT / ACTIVE / ENDED）
                start_time TIMESTAMP WITH TIME ZONE,                        -- 活动开始时间
                end_time TIMESTAMP WITH TIME ZONE,                          -- 活动结束时间
                draw_trigger_type VARCHAR(128),
            
                activity_target JSONB,                                      -- 活动目标配置（目标用户/人群条件等）
                draw_rule JSONB,                                            -- 抽奖规则定义（奖池金额、策略、上下限比例等）
            
                min_total_spend VARCHAR(128),                              -- 用户累计消费门槛
                min_order_spend VARCHAR(128),                              -- 单笔订单参与门槛
                wallet_address VARCHAR(256),                                -- 钱包地址（如果奖励为代币或链上资产）
            
                json_data JSONB NOT NULL                                    -- 预留扩展字段（活动自定义配置）
            );
            """;

    String TABLE = "activity";
    String COLUMNS = "id, gmt_create,gmt_modified, is_deleted, status, shop_domain, name,description," +
            " start_time, end_time,draw_trigger_type,background_image, preview_image," +
            "activity_target,draw_rule, min_total_spend,min_order_spend,wallet_address,json_data";


    @PageQuery
    @Select("SELECT " + COLUMNS + " FROM " + TABLE + " WHERE IS_DELETED=false ORDER BY GMT_CREATE DESC")
    List<Activity> list(ListActivityParam param);
}
