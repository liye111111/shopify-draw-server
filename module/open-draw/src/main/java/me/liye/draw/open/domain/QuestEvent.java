package me.liye.draw.open.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.liye.open.share.dataobject.BaseDataObjectWithEmptyJsonColumn;

import java.util.Map;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class QuestEvent extends BaseDataObjectWithEmptyJsonColumn {
    @JsonProperty("event_id")
    String eventId;
    @JsonProperty("quest_id")
    Long questId;
    @JsonProperty("eventName")
    String eventName;
    @JsonProperty("body")
    Map<String, ?> body;

}
