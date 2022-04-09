package com.gallop.microservice.security.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

/**
 * author gallop
 * date 2022-04-06 15:12
 * Description: 短信校验码相关安全配置
 * Modified By:
 */

@Component
public class ValidateCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{

    @Autowired
    private Filter smsCodeFilter;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        //super.configure(builder);
        builder.addFilterBefore(smsCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }
}
