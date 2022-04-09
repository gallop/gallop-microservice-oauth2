package com.gallop.microservice.security.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * author gallop
 * date 2022-04-06 14:36
 * Description:
 * Modified By:
 */
public interface SmsCodeRepository {
    /**
     * 保存验证码
     * @param request
     * @param code
     */
    void save(ServletWebRequest request, ValidateCode code);
    /**
     * 获取验证码
     * @param request
     * @return
     */
    ValidateCode get(ServletWebRequest request);
    /**
     * 移除验证码
     * @param request
     */
    void remove(ServletWebRequest request);
}
