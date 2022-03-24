package com.gallop.microservice.user.domain;

import com.gallop.microservice.user.pojo.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * author gallop
 * date 2021-07-01 14:03
 * Description:
 * Modified By:
 */

public class UserInfoJwt extends User {
    private SysUser sysUser;

    public UserInfoJwt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}
