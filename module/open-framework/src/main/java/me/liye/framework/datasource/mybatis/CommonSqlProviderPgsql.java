package me.liye.framework.datasource.mybatis;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
@Slf4j
public class CommonSqlProviderPgsql {


    private static final Map<Class<?>, Map<String, Class<?>>> cache = new ConcurrentHashMap<>();

    public String insert(ProviderContext providerContext) {
        Map<String, Class<?>> columnTypeMap = getMapperGerenicTypeMap(providerContext.getMapperType());

        return exec(providerContext, (table, columns) -> {
            String properties = toCloumnStream(columns).map(it -> {
                if ("gmt_create".equalsIgnoreCase(it) || "gmt_modified".equalsIgnoreCase(it)) {
                    return "now()";
                } else if ("json_data".equalsIgnoreCase(it)) {
                    return "#{jsonData,jdbcType=VARCHAR}::json";
                } else {
                    String property = toCamel(it);
                    Class<?> type = columnTypeMap.getOrDefault(property, Object.class);
                    JdbcType jdbcType = guessJdbcType(type);

                    // 适配 PostgresSQL
                    if (jdbcType == JdbcType.CLOB) {
                        jdbcType = JdbcType.VARCHAR;
                    }
                    if (jdbcType == JdbcType.BLOB) {
                        jdbcType = JdbcType.BINARY;
                    }

                    if (jdbcType == JdbcType.OTHER) {
                        return String.format("#{%s,jdbcType=VARCHAR}::json", property);
                    }

                    return String.format("#{%s,jdbcType=%s}", property, jdbcType.name());
                }
            }).collect(Collectors.joining(","));

            // 列名转大写并加引号
            String upperColumns = Arrays.stream(columns.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .collect(Collectors.joining(", "));

            // 表名转大写并加引号
            String upperTable = table.toUpperCase();

            String sql = """
                    INSERT INTO %s (%s) VALUES (%s)
                    """.formatted(upperTable, upperColumns, properties);
            if (log.isDebugEnabled()) {
                log.debug("insert sql: {}", sql);
            }
            return sql;
        });
    }


    public String updateById(ProviderContext providerContext) {
        return exec(providerContext, (table, columns) ->
                """
                        UPDATE %s 
                        SET GMT_MODIFIED = CURRENT_TIMESTAMP, %s 
                        WHERE IS_DELETED = false 
                          AND ID = #{id, jdbcType=BIGINT}                           
                        """.formatted(table.toUpperCase(), makeUpdateSnipe(columns, providerContext))
        );
    }

    public String updateByIdNoSafe(ProviderContext providerContext) {
        return exec(providerContext, (table, columns) ->
                """
                        UPDATE %s 
                        SET GMT_MODIFIED = CURRENT_TIMESTAMP, %s 
                        WHERE ID = #{id, jdbcType=BIGINT}
                        """.formatted(table.toUpperCase(), makeUpdateSnipe(columns, providerContext))
        );
    }

    public String deleteById(ProviderContext providerContext) {
        return exec(providerContext, (table, columns) ->
                """
                        UPDATE %s 
                        SET GMT_MODIFIED = CURRENT_TIMESTAMP, IS_DELETED = true 
                        WHERE IS_DELETED = false 
                          AND ID = #{id, jdbcType=BIGINT}                           
                        """.formatted(table.toUpperCase())
        );
    }

    public String deleteByIdNoSafe(ProviderContext providerContext) {
        return exec(providerContext, (table, columns) ->
                """
                        UPDATE %s 
                        SET GMT_MODIFIED = CURRENT_TIMESTAMP, IS_DELETED = true 
                        WHERE IS_DELETED = false 
                          AND ID = #{id, jdbcType=BIGINT}
                        """.formatted(table.toUpperCase())
        );
    }

    public String selectById(ProviderContext providerContext) {
        return exec(providerContext, (table, columns) ->
                """
                        SELECT %s 
                        FROM %s 
                        WHERE IS_DELETED = false 
                          AND ID = #{id, jdbcType=BIGINT}                           
                        """.formatted(columns.toUpperCase(), table.toUpperCase())
        );
    }

    public String selectByIdNoSafe(ProviderContext providerContext) {
        return exec(providerContext, (table, columns) ->
                """
                        SELECT %s 
                        FROM %s 
                        WHERE ID = #{id, jdbcType=BIGINT}
                        """.formatted(columns.toUpperCase(), table.toUpperCase())
        );
    }

    public String selectAll(ProviderContext providerContext) {
        return exec(providerContext, (table, columns) ->
                """
                        SELECT %s 
                        FROM %s 
                        WHERE IS_DELETED = false                           
                        ORDER BY ${orderBy}
                        """.formatted(columns.toUpperCase(), table.toUpperCase())
        );
    }

    @SneakyThrows
    private String getStringValue(Class clz, String name) {
        Field f = FieldUtils.getDeclaredField(clz, name);
        return (String) f.get(clz);
    }

    private String makeUpdateSnipe(String columns, ProviderContext providerContext) {
        Map<String, Class<?>> columnTypeMap = getMapperGerenicTypeMap(providerContext.getMapperType());
        Set<String> skips = ImmutableSet.of("ID", "GMT_CREATE", "GMT_MODIFIED");

        return toCloumnStream(columns)
                .map(String::toUpperCase) // 字段名转大写
                .filter(it -> !skips.contains(it))
                .map(it -> {
                            String property = toCamel(it); // 仍然按 Java 驼峰属性取
                            Class<?> type = columnTypeMap.getOrDefault(property, Object.class);
                            JdbcType jdbcType = guessJdbcType(type);
                            return "%s=#{%s,jdbcType=%s}".formatted(it, property, jdbcType.name());
                        }
                )
                .collect(Collectors.joining(","));
    }

    private Stream<String> toCloumnStream(String columns) {
        return Arrays.stream(columns.split(",")).map(it -> {
                    it = it.trim();
                    it = StringUtils.remove(it.trim(), "`");
                    return it;
                }
        );
    }

    private String exec(ProviderContext providerContext, BiFunction<String, String, String> fn) {
        Class<?> mapperType = providerContext.getMapperType();
        String table = getStringValue(mapperType, "TABLE");
        String columns = getStringValue(mapperType, "COLUMNS");
        return fn.apply(table, columns);
    }

    private static String toCamel(String it) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, it);
    }


    private static Map<String, Class<?>> getMapperGerenicTypeMap(Class<?> mapperClass) {
        return cache.computeIfAbsent(mapperClass, cls -> {
            Map<String, Class<?>> columnTypeMap = Maps.newHashMap();
            for (Type type : mapperClass.getGenericInterfaces()) {
                if (type instanceof ParameterizedType parameterizedType) {
                    Type rawType = parameterizedType.getRawType();
                    if (rawType instanceof Class<?>) {
                        Type actualType = parameterizedType.getActualTypeArguments()[0];
                        if (actualType instanceof Class<?> entityClass) {
                            Field[] fields = FieldUtils.getAllFields(entityClass);
                            for (Field f : fields) {
                                columnTypeMap.put(f.getName(), f.getType());
                            }
                        }
                    }
                }
            }
            return columnTypeMap;
        });
    }


    private static JdbcType guessJdbcType(Class<?> type) {
        if (type == null) {
            return JdbcType.OTHER;
        }
        if (String.class.isAssignableFrom(type)) {
            return JdbcType.VARCHAR;
        } else if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
            return JdbcType.INTEGER;
        } else if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
            return JdbcType.BIGINT;
        } else if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
            return JdbcType.BOOLEAN;
        } else if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
            return JdbcType.DOUBLE;
        } else if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
            return JdbcType.FLOAT;
        } else if (Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type)) {
            return JdbcType.SMALLINT;
        } else if (Byte.class.isAssignableFrom(type) || byte.class.isAssignableFrom(type)) {
            return JdbcType.TINYINT;
        } else if (Date.class.isAssignableFrom(type)
                || java.sql.Timestamp.class.isAssignableFrom(type)) {
            return JdbcType.TIMESTAMP;
        } else if (java.sql.Date.class.isAssignableFrom(type)) {
            return JdbcType.DATE;
        } else if (BigDecimal.class.isAssignableFrom(type)) {
            return JdbcType.DECIMAL;
        } else if (Clob.class.isAssignableFrom(type)
                || java.sql.NClob.class.isAssignableFrom(type)
                || StringBuffer.class.isAssignableFrom(type)
                || StringBuilder.class.isAssignableFrom(type)) {
            return JdbcType.VARCHAR;
        } else if (java.sql.Blob.class.isAssignableFrom(type) || byte[].class.isAssignableFrom(type)) {
            return JdbcType.BINARY;
        } else {
            return JdbcType.OTHER;
        }
    }
}
