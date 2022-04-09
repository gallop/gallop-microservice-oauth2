package com.gallop.microservice.security.validate.code;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author gallop
 * date 2022-04-06 13:42
 * Description: 验证码信息封装类
 * Modified By:
 */
@Data
public class ValidateCode implements Serializable {
    private static final long serialVersionUID = 3148696347118061332L;

    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(){

    }

    public ValidateCode(String code, int expireIn){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCode(String code, LocalDateTime expireTime){
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
