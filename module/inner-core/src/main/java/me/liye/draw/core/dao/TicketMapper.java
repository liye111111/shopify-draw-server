package me.liye.draw.core.dao;

import me.liye.draw.open.domain.Ticket;
import me.liye.draw.open.domain.param.ListTicketParam;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by liye on 2025-09-19.
 */
@Mapper
public interface TicketMapper extends BaseMapperPgsql<Ticket> {
    String TABLE = "ticket";
    String COLUMNS = "id, gmt_create, gmt_modified, is_deleted,shop_id,name, ticket_sn,status,email, wallet_address, activity_id,activity_rule_name, order_id, order_price,order_currency, tx_id, amount, json_data";

    String DDL = """
            DROP TABLE if exists ticket ;
            CREATE TABLE ticket (
                ID BIGSERIAL PRIMARY KEY,
                GMT_CREATE TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                GMT_MODIFIED TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                IS_DELETED BOOLEAN NOT NULL DEFAULT FALSE,
                SHOP_ID VARCHAR(512) NOT NULL,
                name VARCHAR(128),
                ticket_sn VARCHAR(128),
                status VARCHAR(128),
                email VARCHAR(256),
                wallet_address VARCHAR(128),
                activity_id BIGINT,
                activity_rule_name VARCHAR(128),
                order_id BIGINT,
                order_price VARCHAR(128),
                order_currency VARCHAR(128),
                tx_id VARCHAR(128),
                random_seed VARCHAR(128),
                amount VARCHAR(128),
                JSON_DATA JSONB
            );
            """;


    @SelectProvider(type = InnerSqlProvider.class, method = "list")
    List<Ticket> list(ListTicketParam param);

    @Update("UPDATE " + TABLE + " SET status=#{status},amount=#{amount}, random_seed=#{randomSeed} WHERE ID = #{id}")
    int updateDrawResult(@Param("id") Long id, @Param("status") String status, @Param("amount") String amount, @Param("randomSeed") String randomSeed);

    @Update("UPDATE " + TABLE + " SET tx_id= #{txId} WHERE ID = #{id}")
    int updateTxId(@Param("id") Long id, @Param("txId") String txId);


    class InnerSqlProvider {
        public String list(ListTicketParam param) {
            return """
                    <script>
                    SELECT %s from %s WHERE IS_DELETED = false
                    <if test="shopId != null">and shop_id=#{shopId}</if>
                    <if test="email != null">and email=#{email}</if>
                    <if test="orderIds != null">
                    and order_id in (
                        <foreach item='orderId' collection='orderIds' separator=','>
                            #{orderId}
                        </foreach>
                        )
                    </if>
                    <if test="activityId != null">and activity_id=#{activityId}</if>
                    <if test="activityIds != null">
                    and activity_id in (
                        <foreach item='activityId' collection='activityIds' separator=','>
                            #{activityId}
                        </foreach>
                        )
                    </if>
                    <if test="status != null">
                    and status = #{status}
                    </if>
                    order by gmt_create desc
                    </script>
                    """.formatted(COLUMNS, TABLE);
        }
    }
}
