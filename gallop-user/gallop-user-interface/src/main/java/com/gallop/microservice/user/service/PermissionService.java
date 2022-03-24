package com.gallop.microservice.user.service;

import com.gallop.microservice.user.pojo.Permission;

import java.util.List;
import java.util.Set;

/**
 * author gallop
 * date 2021-08-24 15:41
 * Description:
 * Modified By:
 */
public interface PermissionService {
    List<Permission> findByUserId(long uid);
    Set<String> queryByRoleIds(Integer[] roleIds);

    Set<String> queryByRoleId(Integer roleId);

    boolean checkSuperPermission(Integer roleId);

    void deleteByRoleId(Integer roleId);

    void add(Permission permission);

    void saveRolePermission(String roleId, List<String> permissionIds, List<String> lastPermissionIds);
}
