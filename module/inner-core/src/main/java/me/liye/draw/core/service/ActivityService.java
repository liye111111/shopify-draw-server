package me.liye.draw.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.ActivityMapper;
import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.ShopifyOrder;
import me.liye.draw.open.domain.enums.ActivityStatus;
import me.liye.draw.open.domain.param.CreateActivityParam;
import me.liye.draw.open.domain.param.GetActivityParam;
import me.liye.draw.open.domain.param.ListActivityParam;
import me.liye.draw.open.domain.param.UpdateActivityParam;
import me.liye.open.share.util.TypeConvertor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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


    public int updateStatus(Long id, ActivityStatus status) {
        return activityMapper.updateStatus(id,status);
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
}
