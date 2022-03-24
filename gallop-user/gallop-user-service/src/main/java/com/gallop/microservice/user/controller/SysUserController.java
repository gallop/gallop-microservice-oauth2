package com.gallop.microservice.user.controller;

import com.gallop.microservice.common.annotation.Permissions;
import com.gallop.microservice.common.annotation.RequiresPermissionsDesc;
import com.gallop.microservice.common.base.BaseResultUtil;
import com.gallop.microservice.common.base.PagedResult;
import com.gallop.microservice.user.domain.UserInfoJwt;
import com.gallop.microservice.user.service.SysUserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * author gallop
 * date 2022-03-17 13:49
 * Description:
 * Modified By:
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    @Reference(version = "1.0.0",loadbalance = "roundrobin")
    private SysUserService sysUserService;

    @GetMapping("/list")
    @Permissions("admin:user:list")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="查询")
    public Object list(String username,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(defaultValue = "add_time") String sort,
                       @RequestParam(defaultValue = "desc") String order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Object details = authentication.getDetails();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.err.println("authorities>>>>>>>:"+authorities.toString());
        System.err.println("authentication-name==="+authentication.getName());
        System.err.println("authentication-principal==="+principal.toString());
        System.err.println("authentication-details==="+details.toString());
        UserInfoJwt userJwt = null;
        if(details instanceof UserInfoJwt){
            System.err.println(">>>>>>>>>>>>is UserInfoJwt....");
            userJwt = (UserInfoJwt) details;
            System.err.println("user:"+userJwt.getSysUser().toString());
        }
        PagedResult userList = sysUserService.querySelective(username, page, pageSize, sort, order);
        return BaseResultUtil.success(userList);
    }

}
