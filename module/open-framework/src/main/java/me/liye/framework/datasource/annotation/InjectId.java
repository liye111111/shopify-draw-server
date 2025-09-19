package me.liye.framework.datasource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用於Mapper的insert方法，聲明方法的入參需要注入ID屬性
 * @author knight@momo.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectId {
    /**
     *
     * @return true: replace id value if not null
     */
    String replace() default "false";
}
