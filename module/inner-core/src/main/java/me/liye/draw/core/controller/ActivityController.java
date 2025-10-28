package me.liye.draw.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.service.ActivityService;
import me.liye.draw.open.domain.Activity;
import me.liye.draw.open.domain.param.CreateActivityParam;
import me.liye.draw.open.domain.param.GetActivityParam;
import me.liye.draw.open.domain.param.ListActivityParam;
import me.liye.draw.open.domain.param.UpdateActivityParam;
import me.liye.open.share.rpc.RpcResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static me.liye.open.share.rpc.RpcResult.success;

/**
 * 活动
 * Created by liye on 2025-09-19.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/activity")
public class ActivityController {
    final ActivityService activityService;

    @PostMapping("/create")
    public RpcResult<Activity> create(@RequestBody CreateActivityParam param) {
        return success(activityService.create(param));
    }

    @PostMapping("/update")
    public RpcResult<Activity> update(@RequestBody UpdateActivityParam param) {
        return success(activityService.update(param));
    }

    @GetMapping("/list")
    public RpcResult<List<Activity>> list(ListActivityParam param) {
        return success(activityService.list(param));
    }

    @GetMapping("/get")
    public RpcResult<Activity> get(GetActivityParam param) {
        return success(activityService.get(param));
    }
}
