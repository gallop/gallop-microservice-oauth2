package com.gallop.microservice.security.handler;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.gallop.microservice.common.base.BaseResult;
import com.gallop.microservice.common.base.BaseResultUtil;
import com.gallop.microservice.common.exception.AuthException;
import com.gallop.microservice.security.validate.code.ValidateCodeException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * author gallop
 * date 2022-04-06 17:05
 * Description:
 * Modified By:
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        e.printStackTrace();
        PrintWriter writer = httpServletResponse.getWriter();
        BaseResult resp = BaseResultUtil.build(HttpStatus.HTTP_UNAUTHORIZED,"登陆失败",null);

        if(e instanceof AuthException){
            AuthException exception = (AuthException)e;
            //System.out.println("======="+exception.getCode()+",msg="+exception.getErrorMessage());
            resp.setCode(exception.getCode());
            resp.setMsg(exception.getErrorMessage());
        }else if (e instanceof BadCredentialsException) {
            resp.setMsg("用户名或密码错误，请检查");
        }else if(e instanceof LockedException){
            resp.setMsg("账户被锁定，请联系管理员");
        }else if(e instanceof CredentialsExpiredException){
            resp.setMsg("密码已过期，请联系管理员");
        }else if(e instanceof AccountExpiredException){
            resp.setMsg("账户已过期，请联系管理员");
        }else if(e instanceof DisabledException){
            resp.setMsg("账户被禁用，请联系管理员");
        }else if(e instanceof ValidateCodeException){
            resp.setMsg(e.getMessage());
        }
        writer.write(JSON.toJSONString(resp));
        writer.flush();
        writer.close();
    }
}
