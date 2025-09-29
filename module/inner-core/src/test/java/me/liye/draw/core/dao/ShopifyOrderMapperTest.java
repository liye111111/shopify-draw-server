package me.liye.draw.core.dao;

import me.liye.draw.open.domain.ShopifyOrder;
import me.liye.framework.datasource.id.impl.CachedIDGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by liye on 2025-09-17.
 */
@SpringBootTest
class ShopifyOrderMapperTest {

    @Autowired
    ShopifyOrderMapper shopifyOrderMapper;

    @Autowired
    CachedIDGenerator idGenerator;

    @Test
    void test() {
        System.out.println(idGenerator);
        System.out.println(shopifyOrderMapper);
        ShopifyOrder row = Instancio.create(ShopifyOrder.class);
        row.setId(null);
        row.setIsDeleted(false);
        shopifyOrderMapper.insert(row);
        row = shopifyOrderMapper.selectById(row);
        System.out.println(row.dump());
    }

}