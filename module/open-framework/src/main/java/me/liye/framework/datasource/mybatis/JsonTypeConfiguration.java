package me.liye.framework.datasource.mybatis;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

import java.util.Map;

/**
 * @author knight@momo.com
 */
public class JsonTypeConfiguration implements ConfigurationCustomizer {

    final Map<String, JsonTypeRegister> registers;

    public JsonTypeConfiguration(Map<String, JsonTypeRegister> registers) {
        this.registers = registers;
    }


    @Override
    public void customize(Configuration configuration) {
        for (JsonTypeRegister register : registers.values()) {
            for (Class type : register.getRegisterTypes()) {
                configuration.getTypeHandlerRegistry().register(type, null, new JsonTypeHandler<>(type));
            }
        }
    }
}