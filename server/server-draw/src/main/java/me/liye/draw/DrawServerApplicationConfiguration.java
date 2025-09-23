package me.liye.draw;

import me.liye.draw.domain.ActivityRule;
import me.liye.framework.datasource.mybatis.JsonTypeRegister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by liye on 2025-09-17.
 */
@Configuration
public class DrawServerApplicationConfiguration {

    @Bean
    public JsonTypeRegister jsonTypeRegister() {
        return () -> List.of(ActivityRule.class);
    }

}
