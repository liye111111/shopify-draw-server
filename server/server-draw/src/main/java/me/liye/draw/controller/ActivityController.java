package me.liye.draw.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.domain.Activity;
import me.liye.draw.domain.param.CreateActivityParam;
import me.liye.draw.domain.param.ListActivityParam;
import me.liye.draw.service.ActivityService;
import me.liye.open.share.rpc.RpcResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static me.liye.open.share.rpc.RpcResult.success;

/**
 * Created by liye on 2025-09-19.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    final ActivityService activityService;

    @PostMapping("/create")
    public RpcResult<Activity> create(@RequestBody CreateActivityParam param) {
        return success(activityService.create(param));
    }

    @GetMapping("/list")
    public RpcResult<List<Activity>> list(ListActivityParam param) {
        return success(activityService.list(param));
    }
}
