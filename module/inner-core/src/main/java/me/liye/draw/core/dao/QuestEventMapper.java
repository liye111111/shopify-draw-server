package me.liye.draw.core.dao;

import me.liye.draw.open.domain.QuestEvent;
import me.liye.framework.datasource.mybatis.BaseMapperPgsql;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by liye on 2025-09-19.
 */
@Mapper
public interface QuestEventMapper extends BaseMapperPgsql<QuestEvent> {
    String DDL = """
            DROP TABLE if exists quest_event ;
            CREATE TABLE quest_event (
                ID BIGSERIAL PRIMARY KEY,
                GMT_CREATE TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                GMT_MODIFIED TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                event_id VARCHAR(128) NOT NULL,
                quest_id VARCHAR(512) NOT NULL,
                event_name VARCHAR(128) NOT NULL,
                body JSONB NULL,
                JSON_DATA JSONB NOT NULL
            );
            """;

    String TABLE = "quest_event";
    String COLUMNS = "id, gmt_create,gmt_modified, quest_id, event_id,event_name,body, json_data";

}
