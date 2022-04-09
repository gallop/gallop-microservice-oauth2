package com.gallop.microservice.security.validate.code.sms;

import com.gallop.microservice.security.properties.SecurityProperties;
import com.gallop.microservice.security.validate.code.ValidateCode;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * author gallop
 * date 2022-04-06 14:16
 * Description:
 * Modified By:
 */
@Component("smsCodeGenerator")
public class SmsCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}
