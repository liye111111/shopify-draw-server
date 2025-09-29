package me.liye.draw.core.dao;

import me.liye.draw.open.domain.Shop;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by liye on 2025-09-28.
 */
@SpringBootTest
class ShopMapperTest {
    @Autowired
    ShopMapper shopMapperMapper;

    @Test
    void test() {
        Shop row = Instancio.create(Shop.class);
        shopMapperMapper.insert(row);
    }

}