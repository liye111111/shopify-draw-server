package me.liye.open.share.util;

import lombok.extern.slf4j.Slf4j;
import me.liye.open.share.page.PageQueryResult;
import me.liye.open.share.rpc.RpcResult;
import org.apache.commons.lang3.ArrayUtils;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 對象類型轉換工具類。也可以用於對象的深度複製
 * Created by cola on 2023/1/3.
 */
@Slf4j
public class TypeConvertor {
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <T, R> R convert(T src, Class<R> targetClz) {
        if (src == null) {
            return null;
        }
        return mapper.map(src, targetClz);
    }

    public static <T, R> List<R> convertList(List<T> rows, Class<R> targetClz) {
        if (rows == null) {
            return null;
        }
        List<R> ls = rows.stream()
                .map(it -> convert(it, targetClz))
                .toList();
        if (rows instanceof PageQueryResult pqr) {
            PageQueryResult<R> result = new PageQueryResult<>();
            result.setPage(pqr.getPage());
            result.addAll(ls);
            return result;

        } else {
            return ls;
        }
    }


    public static <T, R> PageQueryResult<R> convertPageQueryResult(PageQueryResult<T> rows, Class<R> targetClz) {
        if (rows == null) {
            return null;
        }
        List<R> ls = rows.stream()
                .map(it -> convert(it, targetClz))
                .toList();
        PageQueryResult<R> result = new PageQueryResult<>();
        result.setPage(rows.getPage());
        result.addAll(ls);
        return result;
    }

    public static <T, R> RpcResult<PageQueryResult<R>> convertAndCreateRpcResultForPage(PageQueryResult<T> rows, Class<R> targetClz) {
        return RpcResult.success(convertPageQueryResult(rows, targetClz));
    }


    public static <T, R> RpcResult<R> convertAndCreateRpcResult(T src, Class<R> targetClz) {
        return RpcResult.success(convert(src, targetClz));
    }

    public static <T, R> RpcResult<List<R>> convertAndCreateRpcResultForList(List<T> rows, Class<R> targetClz) {
        return RpcResult.success(convertList(rows, targetClz));
    }

    public static <T, R> RpcResult<R> convertAndCreateRpcResult(Supplier<T> src, Class<R> targetClz) {
        return convertAndCreateRpcResult(src.get(), targetClz);
    }

    public static <T, R> RpcResult<List<R>> convertAndCreateRpcResultForList(Supplier<List<T>> supplier, Class<R> targetClz) {
        return RpcResult.success(convertList(supplier.get(), targetClz));
    }

    @SuppressWarnings("unchecked")
    public static <T> T deepClone(Object src) {
        return convert(src, (Class<T>) src.getClass());
    }


    public static <T, R> void copyProperties(T src, R target, boolean ignoreNull, String... ignoreProperties) {
        String[] nullPropertyNames = getNullPropertyNames(ignoreNull ? src : null);
        BeanUtils.copyProperties(src, target, ArrayUtils.addAll(nullPropertyNames, ignoreProperties));
    }

    public static String[] getNullPropertyNames(Object source) {
        if (source == null) {
            return new String[0];
        }

        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }
}
