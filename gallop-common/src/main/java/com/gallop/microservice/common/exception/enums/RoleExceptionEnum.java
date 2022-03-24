package com.gallop.microservice.common.exception.enums;


import com.gallop.microservice.common.annotation.ExpEnumType;
import com.gallop.microservice.common.constant.SysExpEnumConstant;

/**
 * author gallop
 * date 2021-10-04 22:54
 * Description: 系统角色相关异常枚举
 * Modified By:
 */
@ExpEnumType(module = SysExpEnumConstant.SYS_EXP_MODULE_CODE,category = SysExpEnumConstant.SYS_ROLE_EXCEPTION_ENUM)
public enum RoleExceptionEnum implements AbstractBaseExceptionEnum{
    /**
     * 角色不存在
     */
    ROLE_NOT_EXIST(1, "角色不存在"),

    /**
     * 角色编码重复
     */
    ROLE_CODE_REPEAT(2, "角色编码重复，请检查code参数"),

    /**
     * 角色名称重复
     */
    ROLE_NAME_REPEAT(3, "角色名称重复，请检查name参数"),

    ROLE_HAS_USER(4,"当前角色存在管理员，不能删除");

    private final Integer code;

    private final String message;

    RoleExceptionEnum(Integer code, String message) {
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
