package com.gallop.microservice.common.enums;

import lombok.Getter;

/**
 * author gallop
 * date 2021-11-15 13:50
 * Description: 管理员类型枚举
 * Modified By:
 */
@Getter
public enum AdminTypeEnum {

    /**
     * 超级管理员
     */
    SUPER_ADMIN(1, "超级管理员"),

    /**
     * 非管理员
     */
    NONE(0, "非管理员");

    private final Integer code;

    private final String message;

    AdminTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
