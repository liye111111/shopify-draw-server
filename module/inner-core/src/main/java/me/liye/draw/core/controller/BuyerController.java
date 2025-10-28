package me.liye.draw.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.service.BuyerService;
import me.liye.draw.open.domain.Buyer;
import me.liye.draw.open.domain.param.GetBuyerParam;
import me.liye.open.share.rpc.RpcResult;
import org.springframework.web.bind.annotation.*;

import static me.liye.open.share.rpc.RpcResult.success;

/**
 * Created by liye on 2025-09-19.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/buyer")
public class BuyerController {
    final BuyerService buyerService;

    @PostMapping("/bindingWalletAddress")
    public RpcResult<Buyer> bindingWalletAddress(@RequestBody Buyer param) {
        return success(buyerService.upsert(param));
    }

    @GetMapping("/get")
    public RpcResult<Buyer> get(GetBuyerParam param) {
        return success(buyerService.get(param));
    }
}
