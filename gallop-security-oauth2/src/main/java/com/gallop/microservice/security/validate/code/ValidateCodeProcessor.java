package com.gallop.microservice.security.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * author gallop
 * date 2022-04-06 14:07
 * Description: 校验码处理器
 * Modified By:
 */
public interface ValidateCodeProcessor {
     /**
      * @date 2022-04-06 14:08
      * Description: 创建校验码
      * Param:
      * return:
      **/
    void create(ServletWebRequest request) throws Exception;

     /**
      * @date 2022-04-06 14:08
      * Description: 校验验证码
      * Param:
      * return:
      **/
    void validate(ServletWebRequest servletWebRequest);
}
