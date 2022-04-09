package com.gallop.microservice.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * author gallop
 * date 2022-04-06 14:19
 * Description:
 * Modified By:
 */
@Component
@ConfigurationProperties(prefix = "gallop.security")
public class SecurityProperties {

    /**
     * 验证码配置
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }
}
