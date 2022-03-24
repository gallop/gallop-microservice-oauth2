package com.gallop.microservice.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gallop.microservice.common.enums.CommonStatusEnum;
import com.gallop.microservice.user.mapper.PermissionMapper;
import com.gallop.microservice.user.pojo.Permission;
import com.gallop.microservice.user.service.PermissionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * author gallop
 * date 2021-08-24 15:43
 * Description:
 * Modified By:
 */
@Service(version = "1.0.0")
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Override
    public List<Permission> findByUserId(long uid) {
       // List<Permission> permissions = new ArrayList<>();
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permission::getId, uid).eq(Permission::getDeleted, CommonStatusEnum.ENABLE.getCode());

        List<Permission> permissionList = this.list(queryWrapper);

        return permissionList;
    }

    @Override
    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();

        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permission::getRoleId, roleIds).eq(Permission::getDeleted, CommonStatusEnum.ENABLE.getCode());

        List<Permission> permissionList = this.list(queryWrapper);
        for (Permission permission : permissionList) {
            permissions.add(permission.getPermission());
        }
        return permissions;
    }

    @Override
    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();

        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getRoleId, roleId).eq(Permission::getDeleted, CommonStatusEnum.ENABLE.getCode());

        List<Permission> permissionList = this.list(queryWrapper);
        for (Permission permission : permissionList) {
            permissions.add(permission.getPermission());
        }
        return permissions;
    }

    @Override
    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getRoleId, roleId)
                .eq(Permission::getPermission,"*")
                .eq(Permission::getDeleted, CommonStatusEnum.ENABLE.getCode());

        return this.count(queryWrapper) != 0;
    }

    @Override
    public void deleteByRoleId(Integer roleId) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getRoleId, roleId);

        this.remove(queryWrapper);
    }

    @Override
    public void add(Permission permission) {
        permission.setAddTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());

        this.save(permission);
    }

    @Override
    public void saveRolePermission(String roleId, List<String> permissionIds, List<String> lastPermissionIds) {
        List<String> add = getDiff(lastPermissionIds,permissionIds);
        if(add!=null && add.size()>0) {
            List<Permission> list = new ArrayList<Permission>();
            for (String p : add) {
                if(StringUtils.isNotEmpty(p)) {
                    Permission rolepms = new Permission();
                    rolepms.setRoleId(Integer.parseInt(roleId));
                    rolepms.setPermission(p);
                    rolepms.setAddTime(LocalDateTime.now());
                    rolepms.setUpdateTime(LocalDateTime.now());
                    rolepms.setDeleted(false);
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }

        List<String> delete = getDiff(permissionIds,lastPermissionIds);
        if(delete!=null && delete.size()>0) {
            for (String permissionId : delete) {
                this.remove(new QueryWrapper<Permission>().lambda().eq(Permission::getRoleId, roleId).eq(Permission::getPermission, permissionId));
            }
        }
    }

    /**
     * 从diff中找出main中没有的元素
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(List<String> main,List<String> diff){
        if(ObjectUtil.isEmpty(diff)) {
            return null;
        }
        if(ObjectUtil.isEmpty(main)) {
            return diff;
        }

        //String[] mainArr = main.split(",");
        //String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap<>();
        for (String string : main) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diff) {
            if(ObjectUtil.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }
}
