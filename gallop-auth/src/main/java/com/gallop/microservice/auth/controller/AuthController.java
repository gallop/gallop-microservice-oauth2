package com.gallop.microservice.auth.controller;

import com.gallop.microservice.user.domain.AuthToken;
import com.gallop.microservice.auth.request.LoginRequest;
import com.gallop.microservice.auth.service.AuthService;
import com.gallop.microservice.common.base.BaseResult;
import com.gallop.microservice.common.base.BaseResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author gallop
 * date 2022-03-10 17:02
 * Description:
 * Modified By:
 */
@RestController
@RequestMapping("/")
public class AuthController {
    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Autowired
    AuthService authService;

    @PostMapping("/userlogin")
    public BaseResult login(LoginRequest loginRequest) {
        if(loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())){
            //ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if(loginRequest == null || StringUtils.isEmpty(loginRequest.getPassword())){
            //ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        //账号
        String username = loginRequest.getUsername();
        //密码
        String password = loginRequest.getPassword();

        //申请令牌
        AuthToken authToken =  authService.login(username,password,clientId,clientSecret);

        //用户身份令牌
        String access_token = authToken.getAccess_token();

        return BaseResultUtil.success(access_token);
    }
}
