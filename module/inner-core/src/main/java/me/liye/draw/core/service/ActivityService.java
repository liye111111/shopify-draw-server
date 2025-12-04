package me.liye.draw.core.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.ActivityMapper;
import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.ActivityTarget;
import me.liye.draw.open.domain.ActivityTargetEntry;
import me.liye.draw.open.domain.ShopifyOrder;
import me.liye.draw.open.domain.enums.ActivityStatus;
import me.liye.draw.open.domain.param.*;
import me.liye.open.share.util.TypeConvertor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liye on 2025-09-19.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {
    final ActivityMapper activityMapper;

    private static final String TEMPLATE_JSON = """
            {
              "1": [0.2, 0.5, 1],
              "2": [0.1, 0.3, 0.6, 0.8, 1],
              "3": [0.1, 0.2, 0.4, 0.6, 0.7, 0.9, 1]
            }
            """;
    private static final Map<String, List<Double>> TEMPLATE_MAP;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TEMPLATE_MAP = mapper.readValue(
                    TEMPLATE_JSON,
                    new TypeReference<>() {
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to load template config", e);
        }
    }

    public Activity create(CreateActivityParam param) {
        Activity row = TypeConvertor.convert(param, Activity.class);
        row.setStatus(ActivityStatus.DRAFT);
        activityMapper.insert(row);
        return activityMapper.selectById(row);
    }


    public Activity updateStatus(Long id, ActivityStatus status) {
        activityMapper.updateStatus(id, status);
        return activityMapper.selectById(Activity.builder()
                .id(id)
                .build());
    }

    public Activity update(UpdateActivityParam param) {
        Activity row = TypeConvertor.convert(param, Activity.class);
        Activity old = activityMapper.selectById(row);
        TypeConvertor.copyProperties(row, old, true, "jsonData");
        activityMapper.updateById(old);
        return activityMapper.selectById(old);
    }

    public List<Activity> list(ListActivityParam param) {
        return activityMapper.list(param);
    }

    public Activity getMatchedActivity(ShopifyOrder order) {
        //TODO: 返回符合抽奖规则的活动
        List<Activity> rows = list(ListActivityParam.builder().build());
        return rows.stream().filter(
                it -> {
                    BigDecimal threshold = new BigDecimal(it.getMinOrderSingleSpend());
                    BigDecimal orderPrice = new BigDecimal(order.getPrice());
                    return orderPrice.compareTo(threshold) >= 0;
                }
        ).findFirst().orElse(null);
    }

    public Activity get(GetActivityParam param) {
        Activity row = TypeConvertor.convert(param, Activity.class);
        return activityMapper.selectById(row);
    }

    public ActivityTarget getActivityTargetTemplate(GetActivityTargetTemplateParam param) {

        String templateId = param.getTemplateId();
        List<Double> percentList = TEMPLATE_MAP.get(templateId);

        if (percentList == null) {
            throw new IllegalArgumentException("Invalid templateId: " + templateId);
        }

        double gmvTarget = Double.parseDouble(param.getGmvTarget());
        double rewardTarget = Double.parseDouble(param.getRewardTarget());

        List<ActivityTargetEntry> entries = new ArrayList<>();

        for (Double percent : percentList) {
            ActivityTargetEntry entry = new ActivityTargetEntry();

            // 计算
            double gmvVal = gmvTarget * percent;
            double rewardVal = rewardTarget * percent;

            // 转成字符串
            entry.setGmtAmount(String.valueOf((long) gmvVal));
            entry.setRewardAmount(String.valueOf((long) rewardVal));
            entry.setPercent(Double.valueOf(percent * 100).intValue());

            entries.add(entry);
        }

        ActivityTarget target = new ActivityTarget();
        target.setGmvTarget(param.getGmvTarget());
        target.setRewardTarget(param.getRewardTarget());
        target.setEntries(entries);

        return target;
    }
}
