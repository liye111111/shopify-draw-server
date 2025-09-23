package me.liye.draw.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.domain.Draw;
import me.liye.draw.domain.param.CreateDrawParam;
import me.liye.draw.domain.param.ListDrawParam;
import me.liye.draw.service.DrawService;
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
@RequestMapping("/api/draw")
public class DrawController {

    final DrawService drawService;

    @PostMapping("/create")
    public RpcResult<Draw> create(@RequestBody CreateDrawParam param) {
        return success(drawService.create(param));
    }

    @GetMapping("/list")
    public RpcResult<List<Draw>> list(ListDrawParam param) {
        return success(drawService.list(param));
    }
}
