package me.liye.draw.dao;

import me.liye.draw.domain.Activity;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by liye on 2025-09-19.
 */
@SpringBootTest
class ActivityMapperTest {
    @Autowired
    ActivityMapper activityMapper;


    @Test
    void test(){
        Activity row = Instancio.create(Activity.class);
        activityMapper.insert(row);
    }
}