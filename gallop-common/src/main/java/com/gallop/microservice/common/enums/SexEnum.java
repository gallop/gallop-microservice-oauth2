package com.gallop.microservice.common.enums;

import lombok.Getter;

/**
 * author gallop
 * date 2021-11-20 8:02
 * Description: 性别枚举
 * Modified By:
 */
@Getter
public enum SexEnum {

    /**
     * 男
     */
    MAN(1, "男"),

    /**
     * 女
     */
    WOMAN(2, "女"),

    /**
     * 未知
     */
    NONE(3, "未知");

    private final Integer code;

    private final String message;

    SexEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
