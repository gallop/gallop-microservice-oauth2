package com.gallop.microservice.security.validate.code.sms;

import com.gallop.microservice.security.validate.code.ValidateCodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author gallop
 * date 2022-04-06 15:01
 * Description:短信验证码过滤器
 * Modified By:
 */
@Component("smsCodeFilter")
@Slf4j
public class SmsCodeFilter extends OncePerRequestFilter {
    /**
     * 验证码校验失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private SmsCodeProcessor smsCodeProcessor;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isValidateCodeRequest(request)){
            try {
                smsCodeProcessor.validate(new ServletWebRequest(request, response));
                log.info("验证码校验通过");
            } catch (ValidateCodeException exception) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidateCodeRequest(HttpServletRequest request){
        boolean result = false;
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            //这里先临时硬编码，短信登入请求认证的才进此过滤器。
            if (pathMatcher.match("/auth/mobile", request.getRequestURI())) {
                result = true;
            }
        }

        return result;
    }
}
