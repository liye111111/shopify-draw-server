package me.liye.draw.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.QuestEventMapper;
import me.liye.draw.open.domain.QuestEvent;
import org.springframework.stereotype.Service;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {
    final QuestEventMapper questEventMapper;
    public void receiveEvent(QuestEvent event) {
        log.info("receive event: {}", event.dump());
        questEventMapper.insert(event);
    }
}
