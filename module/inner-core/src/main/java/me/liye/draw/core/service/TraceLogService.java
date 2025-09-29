package me.liye.draw.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.TraceLogMapper;
import me.liye.draw.open.domain.TraceLog;
import org.springframework.stereotype.Service;

/**
 * Created by liye on 2025-09-29.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TraceLogService {
    final TraceLogMapper traceLogMapper;

    public TraceLog create(TraceLog traceLog) {
        traceLogMapper.insert(traceLog);
        return traceLog;
    }
}
