package me.liye.draw.core.dao;

import me.liye.draw.open.domain.Shop;
import me.liye.draw.open.domain.param.DeleteShopParam;
import me.liye.draw.open.domain.param.ListShopParam;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import me.liye.open.share.page.PageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by liye on 2025-09-19.
 */
@Mapper
public interface ShopMapper extends BaseMapperPgsql<Shop> {
    String DDL = """
            DROP TABLE if exists shop ;
            CREATE TABLE shop (
                ID BIGSERIAL PRIMARY KEY,
                GMT_CREATE TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                GMT_MODIFIED TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                IS_DELETED BOOLEAN NOT NULL DEFAULT FALSE,
                SHOP_DOMAIN VARCHAR(512) NOT NULL,
                TOKEN VARCHAR(512) NOT NULL,
                status VARCHAR(128),
                JSON_DATA JSONB
            );
            """;

    String TABLE = "shop";
    String COLUMNS = "id, gmt_create,gmt_modified, is_deleted, status, shop_domain, token, json_data";

    @PageQuery
    @Select("SELECT " + COLUMNS + " FROM " + TABLE + " WHERE IS_DELETED = false")
    List<Shop> list(ListShopParam param);

    @Update("UPDATE " + TABLE + " SET IS_DELETED = true WHERE SHOP_DOMAIN = #{shopDomain}")
    void delete(DeleteShopParam param);
}
