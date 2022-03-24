package com.gallop.microservice.auth.service;

import com.alibaba.fastjson.JSON;
import com.gallop.microservice.user.domain.UserInfoJwt;
import com.gallop.microservice.user.pojo.SysUser;
import com.gallop.microservice.user.service.PermissionService;
import com.gallop.microservice.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * author gallop
 * date 2021-06-24 16:35
 * Description:
 * Modified By:
 */
@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private ClientsService clientsService;
    @Reference(version = "${user.service.version}",loadbalance = "roundrobin")
    private SysUserService sysUserService;

    @Reference(version = "1.0.0",loadbalance = "roundrobin")
    private PermissionService permissionService;

     /**
      * @date 2022-03-11 9:59
      * Description: 这里其实是做了两次用户登入认证，一次是clientId 的认证（在WebSecurityConfig 配置的httpBasic 方式）；
      * 第二次是普通用户要获取token 的登入认证，要到业务用户表 去获取信息。
      * 也就是clientId 和普通用户的认证都调用了此方法。
      * Param: 举例：clientId = client_001， username= admin
      * return:
      **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //当clientId 未登入时，此时传进来的username是 clientId，所以参数username 可能是clientId，也可能是业务用户帐户
        System.err.println("======username:"+username);
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        //clientId 登入认证处理逻辑
        if(authentication==null){
            //clientId 存储于数据库的处理方式：
            /*ClientDetails clientDetails = clientsService.getByClientId(username);
            if(clientDetails!=null){
                String clientId = clientDetails.getClientId();
                //密码
                String clientSecret = clientDetails.getClientSecret();
                System.err.println(">>>>clientSecret="+clientSecret);
                return new User(username,clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }*/

            //clientId 存于配额文件的处理方式
            return new User(username,passwordEncoder.encode(clientSecret), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        //业务用户帐户 登入认证的处理逻辑
        System.err.println("common-userName="+username);
        SysUser sysUser = sysUserService.getByUsername(username);
        if (null == sysUser) {
            log.warn("用户{}不存在", username);
            throw new UsernameNotFoundException(username);
        }
        log.warn("user:{}",sysUser);
        Set<String> permissionList = permissionService.queryByRoleIds(sysUser.getRoleIds());
        System.err.println(">>>>>permissionList-size="+permissionList.size());
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            for (String sysPermission : permissionList) {
                authorityList.add(new SimpleGrantedAuthority(sysPermission));
            }
        }

        UserInfoJwt userInfo = new UserInfoJwt(sysUser.getUsername(), sysUser.getPassword(), authorityList);
        userInfo.setSysUser(sysUser);
        log.info("请求认证用户: {}", JSON.toJSONString(sysUser));

        return userInfo;
    }
}
