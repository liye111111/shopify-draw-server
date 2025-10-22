package me.liye.draw.core.dao;

import me.liye.draw.open.domain.ActivityTarget;
import me.liye.draw.open.domain.DrawRule;
import me.liye.draw.open.domain.enums.DrawTriggerType;
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
        return () -> List.of(DrawRule.class,
                ActivityTarget.class,
                DrawTriggerType.class
        );
    }
}
