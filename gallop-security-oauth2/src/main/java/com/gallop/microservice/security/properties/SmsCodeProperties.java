package com.gallop.microservice.security.properties;

import lombok.Data;

/**
 * author gallop
 * date 2022-04-06 14:24
 * Description:
 * Modified By:
 */
@Data
public class SmsCodeProperties {
    /**
     * 验证码长度
     */
    private int length = 6;
    /**
     * 过期时间(单位：秒)
     */
    private int expireIn = 600;
    /**
     * 要拦截的url，多个url用逗号隔开，ant pattern
     */
    private String url;
}
