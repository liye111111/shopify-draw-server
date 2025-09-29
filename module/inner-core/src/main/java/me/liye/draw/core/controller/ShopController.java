package me.liye.draw.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.service.ShopService;
import me.liye.draw.open.domain.Shop;
import me.liye.draw.open.domain.param.CreateShopParam;
import me.liye.draw.open.domain.param.DeleteShopParam;
import me.liye.draw.open.domain.param.ListShopParam;
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
@RequestMapping("/api/v1/shop")
public class ShopController {
    final ShopService shopService;

    @PostMapping("/create")
    public RpcResult<Shop> create(@RequestBody CreateShopParam param) {
        return success(shopService.create(param));
    }

    @GetMapping("/list")
    public RpcResult<List<Shop>> list(ListShopParam param) {
        return success(shopService.list(param));
    }

    @PostMapping("/delete")
    public RpcResult<Void> delete(@RequestBody DeleteShopParam param) {
        shopService.delete(param);
        return success();
    }
}
