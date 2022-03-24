
package com.gallop.microservice.common.exception.enums;

import com.gallop.microservice.common.annotation.ExpEnumType;
import com.gallop.microservice.common.constant.SysExpEnumConstant;

/**
 * 系统用户相关异常枚举
 *
 * @author xuyuxiang
 * @date 2020/3/23 9:32
 */
@ExpEnumType(module = SysExpEnumConstant.SYS_EXP_MODULE_CODE, category = SysExpEnumConstant.SYS_USER_EXCEPTION_ENUM)
public enum SysUserExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(1, "用户不存在"),

    /**
     * 账号已存在
     */
    USER_ACCOUNT_REPEAT(2, "账号已存在，请检查account参数"),

    /**
     * 原密码错误
     */
    USER_PWD_ERROR(3, "原密码错误，请检查password参数"),

    /**
     * 新密码与原密码相同
     */
    USER_PWD_REPEAT(4, "新密码与原密码相同，请检查newPassword参数"),

    /**
     * 不能删除超级管理员
     */
    USER_CAN_NOT_DELETE_ADMIN(5, "不能删除超级管理员"),

    /**
     * 不能修改超级管理员状态
     */
    USER_CAN_NOT_UPDATE_ADMIN(6, "不能修改超级管理员状态"),

    /**
     * 新密码与原密码相同
     */
    USER_PWD_EMPTY(7, "密码不能为空，请设置密码");

    private final Integer code;

    private final String message;

    SysUserExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return ExpEnumCodeCreateFactory.getExpEnumCode(this.getClass(),code);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
