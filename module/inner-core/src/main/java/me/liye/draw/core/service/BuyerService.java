package me.liye.draw.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.core.dao.BuyerMapper;
import me.liye.draw.open.domain.Buyer;
import me.liye.draw.open.domain.enums.BuyerStatus;
import me.liye.draw.open.domain.param.GetBuyerParam;
import me.liye.draw.open.domain.param.ListBuyerParam;
import me.liye.draw.open.domain.param.UpdateBuyerParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liye on 2025-09-24.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BuyerService {
    final BuyerMapper buyerMapper;

    public Buyer upsert(Buyer buyer) {
        int updated = buyerMapper.update(UpdateBuyerParam.builder()
                .shopDomain(buyer.getShopDomain())
                .email(buyer.getEmail())
                .walletAddress(buyer.getWalletAddress())
                .build());
        if (updated == 0) {
            buyer.setStatus(BuyerStatus.ENABLED);
            buyerMapper.insert(buyer);
        }
        return buyerMapper.selectById(buyer);
    }

    public Buyer get(GetBuyerParam param) {
        return buyerMapper.get(param);
    }

    public List<Buyer> list(ListBuyerParam param) {
        return buyerMapper.list(param);
    }
}
