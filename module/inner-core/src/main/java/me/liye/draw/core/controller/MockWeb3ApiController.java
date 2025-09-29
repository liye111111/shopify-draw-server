package me.liye.draw.core.controller;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.open.domain.ActivityWallet;
import me.liye.draw.open.domain.ShopWallet;
import me.liye.draw.open.domain.param.ApplyRewardParam;
import me.liye.draw.open.domain.param.BindActivityWalletParam;
import me.liye.draw.open.domain.param.BindShopWalletParam;
import me.liye.open.share.rpc.RpcResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liye on 2025-09-18.
 */
@Slf4j
@RestController
@RequestMapping("/mock")
public class MockWeb3ApiController {

    /**
     * 店铺绑定钱包
     */
    @RequestMapping("/bindShopWallet")
    public RpcResult<ShopWallet> bindShopWallet(@RequestBody BindShopWalletParam param) {
        return RpcResult.success();
    }

    /**
     * 活动绑定钱包
     */
    @RequestMapping("/bindActivityWallet")
    public RpcResult<ActivityWallet> bindActivityWaller(@RequestBody BindActivityWalletParam param) {
        return RpcResult.success();
    }

    /**
     * 领取奖金
     */
    @RequestMapping("/applyReward")
    public String applyReward(@RequestBody ApplyRewardParam param) {
        log.info("applyReward: {}", param.dump());
        // txHash
        return RandomUtil.randomString(32);
    }
}
