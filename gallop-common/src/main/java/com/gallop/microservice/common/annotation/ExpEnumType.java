package com.gallop.microservice.common.annotation;

import java.lang.annotation.*;

/**
 * author gallop
 * date 2021-10-05 10:58
 * Description:
 * 标识在ExceptionEnum类上，用来标识类级别异常枚举编码
 *   <p>
 *   异常枚举编码由3部分组成，如下：
 *   <p>
 *   模块编码（2位） + 分类编码（4位） + 具体项编码（至少1位）
 *   <p>
 *   模块编码和分类编码在ExpEnumCodeConstant类中声明
 * Modified By:
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExpEnumType {
    /**
     * 模块编码
     */
    int module() default 99;

    /**
     * 分类编码
     */
    int category() default 9999;
}
