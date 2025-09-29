package me.liye.framework.datasource;


import me.liye.framework.datasource.aop.InjectIdAspect;
import me.liye.framework.datasource.id.impl.CachedIDGenerator;
import me.liye.framework.datasource.mybatis.JsonTypeConfiguration;
import me.liye.framework.datasource.mybatis.JsonTypeRegister;
import me.liye.framework.datasource.page.PageQueryAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * UID 的自動配置
 *
 * @author knight@momo.com
 * @date 2019.02.20 10:57
 */
@ConditionalOnProperty(value = "server-framework.datasource.enabled", matchIfMissing = true)
@Configuration
public class DatasourceAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public PageQueryAspect pageQueryAspect() {
        return new PageQueryAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonTypeConfiguration jsonTypeConfiguration(Map<String, JsonTypeRegister> jsonTypeRegisters) {
        return new JsonTypeConfiguration(jsonTypeRegisters);
    }

    @Bean
    @ConditionalOnMissingBean
    InjectIdAspect injectIdAspect(CachedIDGenerator cachedIDGenerator) {
        return new InjectIdAspect(cachedIDGenerator);
    }

}
