package me.liye.draw;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * Created by liye on 2025-09-17.
 */
@Configuration
public class DrawServerApplicationConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 禁止序列化为 ISO8601
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        // 注册 Date -> long 的序列化器
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new com.fasterxml.jackson.databind.JsonSerializer<Date>() {
            @Override
            public void serialize(Date value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers)
                    throws java.io.IOException {
                gen.writeNumber(value.getTime());
            }
        });
        mapper.registerModule(module);
        return mapper;
    }
}
