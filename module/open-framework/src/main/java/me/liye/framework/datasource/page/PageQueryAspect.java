package me.liye.framework.datasource.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import me.liye.open.share.page.PageQuery;
import me.liye.open.share.page.PageQueryResult;
import me.liye.open.share.page.PageQuerySupport;
import me.liye.open.share.rpc.Pagination;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 對聲明瞭@PageQuery的方法，將返回結果的List包裝爲PageQueryResult，PageQueryResult繼承了List。
 * @author knight@momo.com
 */
@Slf4j
@Aspect
@Component
public class PageQueryAspect {
    @Around("@annotation(me.liye.open.share.page.PageQuery)")
    public Object inject(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class returnType = signature.getReturnType();
        Object[] args = joinPoint.getArgs();
        Object arg0 = (args == null || args.length == 0) ? null : args[0];
        Object result = null;
        PageQuery annotation = method.getAnnotation(PageQuery.class);

        if (annotation != null
                && args != null && args.length > 0
                && args[0] instanceof PageQuerySupport
                && List.class.isAssignableFrom(returnType)
        ) {
            PageQuerySupport pqs = (PageQuerySupport) args[0];
            int startPage = pqs.getPageIndex();
            if (startPage < 1) {
                throw new RuntimeException("Page number start with 1, current is " + startPage);
            }

            int pageSize = pqs.getPageSize();
            boolean needTotal = pqs.isNeedTotal();
            PageHelper.startPage(startPage, pageSize, needTotal);
            //
            result = joinPoint.proceed();
            //
            if (result != null) {
                PageInfo pageInfo = new PageInfo((Page) result);
                PageQueryResult pageQueryResult = new PageQueryResult();
                pageQueryResult.addAll(pageInfo.getList());
                Pagination pagination = Pagination.builder()
                        .pages(pageInfo.getPages())
                        .startRow(pageInfo.getStartRow())
                        .endRow(pageInfo.getEndRow())
                        .total(pageInfo.getTotal())
                        .pageIndex(pageInfo.getPageNum())
                        .pageSize(pageInfo.getPageSize())
                        .isLastPage(pageInfo.isIsLastPage())
                        .isFirstPage(pageInfo.isIsFirstPage())
                        .hasNextPage(pageInfo.isHasNextPage())
                        .hasPreviousPage(pageInfo.isHasPreviousPage())
                        .build();
                pageQueryResult.setPage(pagination);
                result = pageQueryResult;
            }

        } else if (annotation != null) {
            throw new IllegalArgumentException(
                    "method \"%s\" declared @PageQuery, but args[0] \"%s\" not implements PageQuerySupport."
                            .formatted(method, arg0)
            );
        } else {
            result = joinPoint.proceed();
        }
        return result;

    }
}
