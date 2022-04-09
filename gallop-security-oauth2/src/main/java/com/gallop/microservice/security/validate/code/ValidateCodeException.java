package com.gallop.microservice.security.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * author gallop
 * date 2022-04-06 14:09
 * Description:
 * Modified By:
 */
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = -6781317566711825516L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
