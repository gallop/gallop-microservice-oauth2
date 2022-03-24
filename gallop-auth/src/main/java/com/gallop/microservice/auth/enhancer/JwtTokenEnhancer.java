package com.gallop.microservice.auth.enhancer;

import com.gallop.microservice.user.domain.UserInfoJwt;
import com.gallop.microservice.auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * author gallop
 * date 2022-03-17 12:31
 * Description:
 * Modified By:
 */
public class JwtTokenEnhancer implements TokenEnhancer {
    @Autowired
    MyUserDetailsService userDetailsService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Map<String, Object> info = new HashMap<>();
        String name = authentication.getName();
        info.put("user_name", name);
        Object principal = authentication.getPrincipal();
        UserInfoJwt userJwt = null;
        if(principal instanceof  UserInfoJwt){
            userJwt = (UserInfoJwt) principal;
        }else{
            //refresh_token默认不去调用userdetailService获取用户信息，这里我们手动去调用，得到 UserJwt
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            userJwt = (UserInfoJwt) userDetails;
        }
        info.put("name", userJwt.getUsername());
        info.put("mobile",userJwt.getSysUser().getPhone());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            info.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        System.err.println("after convertUserAuthentication>>>>>"+info.toString());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
