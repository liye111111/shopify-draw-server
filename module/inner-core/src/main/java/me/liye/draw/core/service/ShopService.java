package me.liye.draw.core.service;

import lombok.RequiredArgsConstructor;
import me.liye.draw.core.dao.ShopMapper;
import me.liye.draw.open.domain.Shop;
import me.liye.draw.open.domain.param.CreateShopParam;
import me.liye.draw.open.domain.param.DeleteShopParam;
import me.liye.draw.open.domain.param.ListShopParam;
import me.liye.open.share.util.TypeConvertor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liye on 2025-09-28.
 */

@Service
@RequiredArgsConstructor
public class ShopService {
    final ShopMapper shopMapper;

    public Shop create(CreateShopParam param) {
        Shop shop = TypeConvertor.convert(param, Shop.class);
        shopMapper.insert(shop);
        return shopMapper.selectById(shop);
    }

    public List<Shop> list(ListShopParam param) {
        return shopMapper.list(param);
    }

    public void delete(DeleteShopParam param) {
        shopMapper.delete(param);
    }
}
