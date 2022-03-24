package com.gallop.microservice.auth.config;

import com.gallop.microservice.user.domain.UserInfoJwt;
import com.gallop.microservice.auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
@Deprecated
@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
    @Autowired
    MyUserDetailsService userDetailsService;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        LinkedHashMap response = new LinkedHashMap();
        String name = authentication.getName();
        response.put("user_name", name);

        Object principal = authentication.getPrincipal();
        UserInfoJwt userJwt = null;
        if(principal instanceof  UserInfoJwt){
            userJwt = (UserInfoJwt) principal;
        }else{
            //refresh_token默认不去调用userdetailService获取用户信息，这里我们手动去调用，得到 UserJwt
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            userJwt = (UserInfoJwt) userDetails;
        }
        response.put("name", userJwt.getUsername());
        response.put("mobile",userJwt.getSysUser().getPhone());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        System.err.println("after convertUserAuthentication>>>>>"+response.toString());
        return response;
    }


}
