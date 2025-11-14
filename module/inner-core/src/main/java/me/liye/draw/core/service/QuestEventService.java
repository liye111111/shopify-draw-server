package me.liye.draw.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.QuestEventMapper;
import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.QuestEvent;
import me.liye.draw.open.domain.enums.ActivityStatus;
import me.liye.draw.open.domain.enums.QuestEventName;
import me.liye.draw.open.domain.param.GetActivityParam;
import org.springframework.stereotype.Service;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QuestEventService {
    final QuestEventMapper questEventMapper;
    final ActivityService activityService;

    public String receiveEvent(QuestEvent event) {
        String info = "";

        String dump = event.dump();
        log.info("receive event: {}", dump);

        Activity before = null;
        Activity after = null;
        try {
            before = activityService.get(GetActivityParam.builder()
                    .id(event.getQuestId())
                    .build());

            if (before == null) {
                log.warn("no matched activity for event: {}", dump);
                info = "failed:quest not found,%s".formatted(event.getQuestId());
                return info;
            }

            QuestEventName eventName = QuestEventName.valueOf(event.getEventName());

            ActivityStatus updateStatus = null;
            // 待注资->注资完成
            if (eventName == QuestEventName.merchantInit && before.getStatus() == ActivityStatus.AWAITING_FUNDING) {
                updateStatus = ActivityStatus.FUNDED_READY;
            }
            // 抽奖完成->领奖开始
            else if (eventName == QuestEventName.airdropOpen && before.getStatus() == ActivityStatus.DRAW_COMPLETED) {
                updateStatus = ActivityStatus.CLAIM_OPEN;
            }

            if (updateStatus == null) {
                log.warn("no matched status transition: current={},event={}", before.getStatus(), dump);
                info = "failed: no matched status transition:id=%s,status=%s,event=%s".formatted(event.getQuestId(), before.getStatus(), eventName);
            } else {
                after = activityService.updateStatus(before.getId(), updateStatus);
                info = "success:%s->%s".formatted(before.getStatus(), updateStatus);
            }


            return info;
        } finally {
            event.setInfo(info);
            event.setBefore(before);
            event.setAfter(after);
            questEventMapper.insert(event);
        }


    }
}
