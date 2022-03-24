package com.gallop.microservice.common.exception;

import com.gallop.microservice.common.exception.enums.AbstractBaseExceptionEnum;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;


/**
 * author gallop
 * date 2021-10-05 16:30
 * Description:
 * 认证相关的异常
 * <p>
 * 认证和鉴权的区别：
 * <p>
 * 认证可以证明你能登录系统，认证的过程是校验token的过程
 * 鉴权可以证明你有系统的哪些权限，鉴权的过程是校验角色是否包含某些接口的权限
 */
@Getter
public class AuthException extends AuthenticationException {

    private final Integer code;

    private final String errorMessage;

    public AuthException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }


}
