package me.liye.framework.datasource.mybatis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.*;

/**
 * @author knight@momo.com
 */
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {
    private final Class<T> javaType;

    public JsonTypeHandler(Class<T> javaType) {
        this.javaType = javaType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setObject(i, JsonUtil.getMapper().writeValueAsString(parameter), Types.VARCHAR);
        } catch (JsonProcessingException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {

        return toJavaTypeObject(rs.getObject(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        return toJavaTypeObject(rs.getObject(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

        return toJavaTypeObject(cs.getObject(columnIndex));
    }

    private T toJavaTypeObject(Object value) throws SQLException {

        if (value == null) {
            return null;
        }

        try {
            return JsonUtil.getMapper().readValue(value.toString(), javaType);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
