package com.gallop.microservice.user.service;

import cn.hutool.core.lang.Dict;
import com.gallop.microservice.common.base.PagedResult;
import com.gallop.microservice.user.param.SysRoleParam;
import com.gallop.microservice.user.pojo.Role;

import java.util.List;
import java.util.Set;

/**
 * author gallop
 * date 2020-04-19 11:03
 * Description:
 *
 * Modified By:
 */
 public interface RoleService {
     Set<String> queryByIds(Integer[] roleIds);
     PagedResult querySelective(SysRoleParam sysRoleParam);
     /**
     * 系统角色下拉（用于授权角色时选择）
     *
     * @return 增强版hashMap，格式：[{"id":456, "code":"zjl", "name":"总经理"}]
     */
     List<Dict> options();
     Role findById(Integer id);
     void add(SysRoleParam sysRoleParam);
     void deleteById(SysRoleParam sysRoleParam);
     void edit(SysRoleParam sysRoleParam);
     //boolean updateByRole(Role role);
     boolean checkExist(String name);
     List<Role> queryAll();

    /**
     * 授权菜单
     */
    void grantMenu(SysRoleParam sysRoleParam);
}
