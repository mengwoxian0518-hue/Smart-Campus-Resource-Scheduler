package com.campus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流Key前缀
     */
    String key() default "rate_limit:";

    /**
     * 限流时间，单位秒
     */
    int time() default 1;

    /**
     * 限流次数
     */
    int count() default 5;
}
