package me.liye.draw.core.dao;

import me.liye.draw.open.domain.ActivityRule;
import me.liye.framework.datasource.mybatis.JsonTypeRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liye on 2025-09-17.
 */
@Component
@MapperScan
public class DrawMapperConfig {
    @Bean
    public JsonTypeRegister jsonTypeRegister() {
        return () -> List.of(ActivityRule.class);
    }
}
