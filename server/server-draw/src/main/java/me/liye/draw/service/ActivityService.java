package me.liye.draw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.dao.ActivityMapper;
import me.liye.draw.domain.Activity;
import me.liye.draw.domain.param.CreateActivityParam;
import me.liye.draw.domain.param.ListActivityParam;
import me.liye.open.share.util.TypeConvertor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liye on 2025-09-19.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {
    final ActivityMapper activityMapper;

    public Activity create(CreateActivityParam param) {
        Activity row = TypeConvertor.convert(param, Activity.class);
        activityMapper.insert(row);
        return activityMapper.selectById(row);
    }

    public List<Activity> list(ListActivityParam param) {
        return activityMapper.list(param);
    }
}
