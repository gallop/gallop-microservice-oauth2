package com.gallop.microservice.common.exception.enums;


import com.gallop.microservice.common.annotation.ExpEnumType;

/**
 * author gallop
 * date 2021-10-05 11:07
 * Description: 异常枚举code值快速创建
 * Modified By:
 */
public class ExpEnumCodeCreateFactory {
    public static Integer getExpEnumCode(Class<?> clazz, int code) {

        // 默认的异常响应码
        Integer defaultCode = Integer.valueOf("" + 99 + 9999 + 9);

        if (clazz == null) {
            return defaultCode;
        } else {
            ExpEnumType expEnumType = clazz.getAnnotation(ExpEnumType.class);
            if (expEnumType == null) {
                return defaultCode;
            }
            return Integer.valueOf("" + expEnumType.module() + expEnumType.category() + code);
        }

    }
}
