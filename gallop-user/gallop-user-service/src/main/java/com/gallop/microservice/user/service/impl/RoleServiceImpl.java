package com.gallop.microservice.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gallop.microservice.common.base.PagedResult;
import com.gallop.microservice.common.constant.CommonConstant;
import com.gallop.microservice.common.enums.CommonStatusEnum;
import com.gallop.microservice.common.exception.ServiceException;
import com.gallop.microservice.common.exception.enums.RoleExceptionEnum;
import com.gallop.microservice.user.mapper.RoleMapper;
import com.gallop.microservice.user.param.SysRoleParam;
import com.gallop.microservice.user.pojo.Role;
import com.gallop.microservice.user.pojo.SysUser;
import com.gallop.microservice.user.service.PermissionService;
import com.gallop.microservice.user.service.RoleService;
import com.gallop.microservice.user.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author gallop
 * date 2021-10-04 21:54
 * Description:
 * Modified By:
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private PermissionService permissionService;

    @Override
    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if (roleIds.length == 0) {
            return roles;
        }
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId, roleIds).eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());
        List<Role> roleList = this.list(queryWrapper);
        for (Role role : roleList) {
            roles.add(role.getName());
        }
        return roles;
    }

    @Override
    public PagedResult querySelective(SysRoleParam sysRoleParam) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(sysRoleParam.getName())) {
            queryWrapper.likeRight(Role::getName,sysRoleParam.getName());
        }
        if (!StringUtils.isEmpty(sysRoleParam.getCode())) {
            queryWrapper.likeRight(Role::getCode,sysRoleParam.getCode());
        }

        queryWrapper.eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());
        //queryWrapper.orderByDesc(Role::getName);
        PageHelper.startPage(sysRoleParam.getPage(), sysRoleParam.getPageSize());
        List<Role> list = this.list(queryWrapper);
        PageInfo<Role> pageList = new PageInfo<>(list);
        return new PagedResult(pageList);
    }

    @Override
    public List<Dict> options() {
        List<Dict> dictList = CollectionUtil.newArrayList();
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        //如果当前登录用户不是超级管理员，则查询自己拥有的
        //TODO
        /*if (!LoginContextHolder.getContext().isSuperAdmin()) {

            //查询自己拥有的
            List<Integer> loginUserRoleIds = Arrays.asList(LoginContextHolder.getContext().getSysLoginUser().getUser().getRoleIds());
            if (ObjectUtil.isEmpty(loginUserRoleIds)) {
                return dictList;
            }
            queryWrapper.in(Role::getId, loginUserRoleIds);
        }*/
        //只查询正常状态
        queryWrapper.eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());
        this.list(queryWrapper)
                .forEach(sysRole -> {
                    Dict dict = Dict.create();
                    dict.put(CommonConstant.ID, sysRole.getId());
                    dict.put(CommonConstant.CODE, sysRole.getCode());
                    dict.put(CommonConstant.NAME, sysRole.getName());
                    dictList.add(dict);
                });
        return dictList;
    }

    @Override
    public Role findById(Integer id) {
        Role role = this.getById(id);
        return role;
    }

    @Override
    public void add(SysRoleParam sysRoleParam) {
        //校验参数，检查是否存在相同的名称和编码
        checkRoleRepeat(sysRoleParam, false);
        Role sysRole = new Role();
        BeanUtil.copyProperties(sysRoleParam, sysRole);
        sysRole.setStatus(CommonStatusEnum.ENABLE.getCode());
        this.save(sysRole);
    }

    @Override
    public void deleteById(SysRoleParam sysRoleParam) {
        // 如果当前角色所对应管理员仍存在，则拒绝删除角色。
        List<SysUser> sysUserList = sysUserService.findAll();
        for(SysUser sysUser : sysUserList){
            Integer[] roleIds = sysUser.getRoleIds();
            for(Integer roleId : roleIds){
                if(sysRoleParam.getId()== roleId.intValue()){
                    throw new ServiceException(RoleExceptionEnum.ROLE_HAS_USER);
                }
            }
        }

        Role sysRole = this.querySysRole(sysRoleParam.getId());
        sysRole.setStatus(CommonStatusEnum.DELETED.getCode());
        this.updateById(sysRole);
    }

    @Override
    public void edit(SysRoleParam sysRoleParam) {
        Role sysRole = this.querySysRole(sysRoleParam.getId());
        //校验参数，检查是否存在相同的名称和编码
        checkRoleRepeat(sysRoleParam, true);
        BeanUtil.copyProperties(sysRoleParam, sysRole);
        //不能修改状态，用修改状态接口修改状态
        sysRole.setStatus(null);
        this.updateById(sysRole);
    }


    @Override
    public boolean checkExist(String name) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.eq(Role::getName,name);
        }

        queryWrapper.eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());

        return this.count(queryWrapper)!= 0;
    }

    @Override
    public List<Role> queryAll() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, CommonStatusEnum.ENABLE.getCode());

        return this.list(queryWrapper);
    }

    @Override
    public void grantMenu(SysRoleParam sysRoleParam) {
        this.querySysRole(sysRoleParam.getId());
        permissionService.saveRolePermission(sysRoleParam.getId().toString(),sysRoleParam.getGrantMenuIdList(),sysRoleParam.getLastPermissionIds());
    }


    /**
     * 获取系统角色
     *
     */
    private Role querySysRole(long roleId) {
        Role sysRole = this.getById(roleId);
        if (ObjectUtil.isNull(sysRole)) {
            throw new ServiceException(RoleExceptionEnum.ROLE_NOT_EXIST);
        }
        return sysRole;
    }

    /**
     * 校验参数，检查是否存在相同角色的名称和编码
     *
     */
    private void checkRoleRepeat(SysRoleParam sysRoleParam, boolean isExcludeSelf) {
        Integer id = sysRoleParam.getId();
        String name = sysRoleParam.getName();
        String code = sysRoleParam.getCode();

        LambdaQueryWrapper<Role> queryWrapperByName = new LambdaQueryWrapper<>();
        queryWrapperByName.eq(Role::getName, name)
                .ne(Role::getStatus, CommonStatusEnum.DELETED.getCode());

        LambdaQueryWrapper<Role> queryWrapperByCode = new LambdaQueryWrapper<>();
        queryWrapperByCode.eq(Role::getCode, code)
                .ne(Role::getStatus, CommonStatusEnum.DELETED.getCode());

        //是否排除自己，如果排除自己则不查询自己的id
        if (isExcludeSelf) {
            queryWrapperByName.ne(Role::getId, id);
            queryWrapperByCode.ne(Role::getId, id);
        }
        int countByName = this.count(queryWrapperByName);
        int countByCode = this.count(queryWrapperByCode);

        if (countByName >= 1) {
            throw new ServiceException(RoleExceptionEnum.ROLE_NAME_REPEAT);
        }
        if (countByCode >= 1) {
            throw new ServiceException(RoleExceptionEnum.ROLE_CODE_REPEAT);
        }
    }
}
