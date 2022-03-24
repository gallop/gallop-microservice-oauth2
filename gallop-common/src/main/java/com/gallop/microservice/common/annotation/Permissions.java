package com.gallop.microservice.common.annotation;


import com.gallop.microservice.common.enums.Logical;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解，用于检查权限
 * 使用方式：@Permission表示检查是否有权限访问该资源
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {
    /**
     * 多个值用逗号隔开
     */
    String value() default "";

    Logical logical() default Logical.AND;

}
