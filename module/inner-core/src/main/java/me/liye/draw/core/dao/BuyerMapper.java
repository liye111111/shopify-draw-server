package me.liye.draw.core.dao;

import me.liye.draw.open.domain.Buyer;
import me.liye.draw.open.domain.param.GetBuyerParam;
import me.liye.draw.open.domain.param.ListBuyerParam;
import me.liye.draw.open.domain.param.UpdateBuyerParam;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import me.liye.open.share.page.PageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 *
 */
@Mapper
public interface BuyerMapper extends BaseMapperPgsql<Buyer> {
    String TABLE = "buyer";
    String COLUMNS = "id, gmt_create,gmt_modified, is_deleted, status, shop_domain,email,wallet_address,json_data";
    String DDL = """
            drop table if exists buyer;
            CREATE TABLE buyer
            (
                ID BIGSERIAL PRIMARY KEY,                                   -- 自增主键
                GMT_CREATE TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(), -- 创建时间
                GMT_MODIFIED TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),-- 修改时间
                IS_DELETED BOOLEAN NOT NULL DEFAULT FALSE,                  -- 逻辑删除
                SHOP_DOMAIN VARCHAR(512) NOT NULL,                          -- 店铺域名（Shopify 唯一标识）
                wallet_address   VARCHAR(255) NOT NULL,
                email        VARCHAR(255) NOT NULL,
                status VARCHAR(128) NOT NULL,
                JSON_DATA JSONB NOT NULL
            );
            """;

    @UpdateProvider(type = InnerSqlProvider.class, method = "update")
    int update(UpdateBuyerParam param);

    @SelectProvider(type = InnerSqlProvider.class, method = "get")
    Buyer get(GetBuyerParam param);

    @PageQuery
    @SelectProvider(type = InnerSqlProvider.class, method = "list")
    List<Buyer> list(ListBuyerParam param);

    class InnerSqlProvider {
        public String get() {
            return """
                    select %s from %s where is_deleted=false and shop_domain= #{shopDomain} and email = #{email}
                    """.formatted(COLUMNS, TABLE);
        }

        public String update() {
            return """
                    update %s set wallet_address = #{walletAddress} where shop_domain=#{shopDomain} and email = #{email}
                    """.formatted(TABLE);
        }

        public String list() {
            return """
                    <script>
                    select %s from %s where is_deleted=false and shop_domain= #{shopDomain}
                    <if test="emails != null">
                    and email in (
                        <foreach item='email' collection='emails' separator=','>
                            #{email}
                        </foreach>
                        )
                    </if>
                    </script>
                    """.formatted(COLUMNS, TABLE);
        }
    }
}
