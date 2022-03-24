package com.gallop.microservice.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gallop.microservice.common.base.PagedResult;
import com.gallop.microservice.common.enums.AdminTypeEnum;
import com.gallop.microservice.common.enums.CommonStatusEnum;
import com.gallop.microservice.common.enums.SexEnum;
import com.gallop.microservice.common.exception.ServiceException;
import com.gallop.microservice.common.exception.enums.StatusExceptionEnum;
import com.gallop.microservice.common.exception.enums.SysUserExceptionEnum;
import com.gallop.microservice.user.mapper.SysUserMapper;
import com.gallop.microservice.user.param.SysUserParam;
import com.gallop.microservice.user.pojo.SysUser;
import com.gallop.microservice.user.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * author gallop
 * date 2021-06-24 16:16
 * Description:
 * Modified By:
 */
@Service(version = "1.0.0")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    //@Resource
    //private PasswordEncoder passwordEncoder;

    /*@Override
    public SysUser getByUsername(String username) {
        SysUser user = new SysUser();
        user.setUserName("gallop");
        //$2a$10$P22hjdX4kNPkbn6wKfujhuvBdgkPGenmpdOOAgk4yBRG5juLG9wDG
        user.setPassword(passwordEncoder.encode("123456"));
        System.err.println("database-pwd:"+user.getPassword());
        return user;
    }*/

    @Override
    public SysUser getByUsername(String username) {
        System.err.println("gallop-user-service:>>username="+username);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        queryWrapper.ne(SysUser::getDeleted, CommonStatusEnum.DELETED.getCode());
        return this.getOne(queryWrapper);
    }

    @Override
    public List<SysUser> findAll() {
        return this.list();
    }

    @Override
    public PagedResult querySelective(String username, Integer page, Integer pageSize, String sort, String order) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isBlank(username)){
            queryWrapper.eq(SysUser::getUsername,username);
        }

        queryWrapper.eq(SysUser::getDeleted, CommonStatusEnum.ENABLE.getCode());
        /*if(!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)){
            order.equalsIgnoreCase("asc")?queryWrapper.orderByAsc("sort")
        }*/

        PageHelper.startPage(page, pageSize);
        List<SysUser> list = this.list(queryWrapper);
        PageInfo<SysUser> pageList = new PageInfo<>(list);
        return new PagedResult(pageList);
    }

    @Override
    public SysUser detail(SysUserParam sysUserParam) {
        SysUser sysUser = this.getById(sysUserParam.getId());
        if (ObjectUtil.isNull(sysUser)) {
            throw new ServiceException(SysUserExceptionEnum.USER_NOT_EXIST);
        }
        return sysUser;
    }

    @Override
    public void add(SysUserParam sysUserParam) {
        checkAccountRepeat(sysUserParam,false); // 判断是否有重复账号
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(sysUserParam, sysUser);
        fillDefaltUserInfo(sysUser); //填充默认信息
        if(ObjectUtil.isNotEmpty(sysUserParam.getPassword())) {
            sysUser.setPassword(BCrypt.hashpw(sysUserParam.getPassword(), BCrypt.gensalt()));
        }else {
            throw new ServiceException(SysUserExceptionEnum.USER_PWD_EMPTY);
        }
        this.save(sysUser);
    }
     /**
      * @date 2021-11-20 8:00
      * Description:填充用户默认字段
      * Param:
      * return:
      **/
    private void fillDefaltUserInfo(SysUser sysUser) {

        if (ObjectUtil.isEmpty(sysUser.getAvatar())) {
            sysUser.setAvatar(null);
        }

        if (ObjectUtil.isEmpty(sysUser.getSex())) {
            sysUser.setSex(SexEnum.NONE.getCode());
        }
        sysUser.setAddTime(LocalDateTime.now());
        sysUser.setStatus(CommonStatusEnum.ENABLE.getCode());
        sysUser.setAdminType(AdminTypeEnum.NONE.getCode());
    }

    @Override
    public void edit(SysUserParam sysUserParam) {
        SysUser sysUser = this.querySysUser(sysUserParam);
        checkAccountRepeat(sysUserParam,true);
        BeanUtil.copyProperties(sysUserParam, sysUser);

        this.updateById(sysUser);
    }

    @Override
    public void changeStatus(SysUserParam sysUserParam) {
        System.err.println("sysUserParam==="+sysUserParam.toString());
        SysUser sysUser = this.querySysUser(sysUserParam);
        //不能修改超级管理员状态
        if (AdminTypeEnum.SUPER_ADMIN.getCode().equals(sysUser.getAdminType())) {
            throw new ServiceException(SysUserExceptionEnum.USER_CAN_NOT_UPDATE_ADMIN);
        }

        Long id = sysUser.getId();

        Integer status = sysUserParam.getStatus();
        //校验状态在不在枚举值里
        CommonStatusEnum.validateStatus(status);

        //更新枚举，更新只能更新未删除状态的
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getId, id)
                .and(i -> i.ne(SysUser::getStatus, CommonStatusEnum.DELETED.getCode()))
                .set(SysUser::getStatus, status);
        boolean update = this.update(updateWrapper);
        if (!update) {
            throw new ServiceException(StatusExceptionEnum.UPDATE_STATUS_ERROR);
        }
    }

    @Override
    public List<Integer> ownRole(SysUserParam sysUserParam) {
        SysUser sysUser = this.querySysUser(sysUserParam);
        return Arrays.asList(sysUser.getRoleIds());
    }

    @Override
    public void grantRole(SysUserParam sysUserParam) {
        SysUser sysUser = this.querySysUser(sysUserParam);
        if(ObjectUtil.isNotEmpty(sysUser)){
            int size = sysUserParam.getGrantRoleIdList().size();
            sysUser.setRoleIds(sysUserParam.getGrantRoleIdList().toArray(new Integer[size]));
            this.updateById(sysUser);
        }
    }

    @Override
    public void resetPwd(SysUserParam sysUserParam) {
        SysUser sysUser = this.querySysUser(sysUserParam);
        String password = "123456";
        sysUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        this.updateById(sysUser);
    }

    @Override
    public void delete(List<SysUserParam> sysUserParamList) {
        sysUserParamList.forEach(sysUserParam -> {
            SysUser sysUser = this.querySysUser(sysUserParam);
            //不能删除超级管理员
            if (AdminTypeEnum.SUPER_ADMIN.getCode().equals(sysUser.getAdminType())) {
                throw new ServiceException(SysUserExceptionEnum.USER_CAN_NOT_DELETE_ADMIN);
            }
            sysUser.setStatus(CommonStatusEnum.DELETED.getCode());
            this.updateById(sysUser);
        });
    }

    private SysUser querySysUser(SysUserParam sysUserParam) {
        SysUser sysUser = this.getById(sysUserParam.getId());
        System.err.println("sysUser====="+sysUserParam.getId());
        if (ObjectUtil.isNull(sysUser)) {
            throw new ServiceException(SysUserExceptionEnum.USER_NOT_EXIST);
        }
        return sysUser;
    }

    /**
     * 检查是否存在相同的账号
     *
     * @author xuyuxiang
     * @date 2020/3/27 16:04
     */
    private void checkAccountRepeat(SysUserParam sysUserParam, boolean isExcludeSelf) {
        Long id = sysUserParam.getId();
        String account = sysUserParam.getUsername();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, account)
                .ne(SysUser::getStatus, CommonStatusEnum.DELETED.getCode());
        //是否排除自己，如果是则查询条件排除自己id
        if (isExcludeSelf) {
            queryWrapper.ne(SysUser::getId, id);
        }
        int countByAccount = this.count(queryWrapper);
        //大于等于1个则表示重复
        if (countByAccount >= 1) {
            throw new ServiceException(SysUserExceptionEnum.USER_ACCOUNT_REPEAT);
        }
    }
}
