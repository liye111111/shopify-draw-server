package me.liye.framework.datasource.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.framework.datasource.annotation.InjectId;
import me.liye.framework.datasource.id.impl.CachedIDGenerator;
import me.liye.open.share.dataobject.InjectIdSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author knight@momo.com
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class InjectIdAspect {
    final CachedIDGenerator idGenerator;

    @Around("@annotation(me.liye.framework.datasource.annotation.InjectId)")
    public Object inject(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        InjectId injectId = method.getAnnotation(InjectId.class);
        boolean replace = "true".equals(injectId.replace());

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length < 1) {
            log.debug("ignore inject id,because args is empty.");
        } else {
            for (Object arg : args) {
                if (arg instanceof InjectIdSupport) {
                    InjectIdSupport sis = ((InjectIdSupport) arg);

                    Long id = sis.getId();
                    if (id == null || replace) {
                        id = idGenerator.getUID();
                        sis.setId(id);
                        log.debug("set id for object {},{}: ", id, arg);
                    } else {
                        log.debug("ignore inject id,because id is not empty.{}，{}", id, arg);
                    }
                }
            }
        }

        return joinPoint.proceed();
    }
}
