package com.gallop.microservice.common.exception.enums;


import com.gallop.microservice.common.annotation.ExpEnumType;
import com.gallop.microservice.common.constant.SysExpEnumConstant;

/**
 * author gallop
 * date 2021-10-05 11:27
 * Description: 参数校验异常枚举
 * Modified By:
 */
@ExpEnumType(module = SysExpEnumConstant.SYS_EXP_MODULE_CODE, category = SysExpEnumConstant.PARAM_EXCEPTION_ENUM)
public enum ParamExceptionEnum implements AbstractBaseExceptionEnum {
    /**
     * 参数错误
     */
    PARAM_ERROR(1, "参数错误"),
    /**
     * 字典类型不存在
     */
    DICT_TYPE_NOT_EXIST(2, "字典类型不存在");



    private final Integer code;

    private final String message;

    ParamExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return ExpEnumCodeCreateFactory.getExpEnumCode(this.getClass(), code);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
