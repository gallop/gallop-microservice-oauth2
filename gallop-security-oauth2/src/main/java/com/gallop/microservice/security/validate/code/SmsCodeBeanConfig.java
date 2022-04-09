package com.gallop.microservice.security.validate.code;

import com.gallop.microservice.security.validate.code.sms.DefaultSmsCodeSender;
import com.gallop.microservice.security.validate.code.sms.SmsCodeSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author gallop
 * date 2022-04-06 14:46
 * Description:
 * Modified By:
 */
@Configuration
public class SmsCodeBeanConfig {
    /**
     * 短信验证码发送器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }
}
