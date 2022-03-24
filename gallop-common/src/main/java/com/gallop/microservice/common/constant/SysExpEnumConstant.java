package com.gallop.microservice.common.constant;

/**
 * author gallop
 * date 2021-10-05 11:00
 * Description:
 * 系统管理异常枚举编码构成常量
 * <p>
 * 异常枚举编码由3部分组成，如下：
 * <p>
 * 模块编码（2位） + 分类编码（4位） + 具体项编码（至少1位）
 * <p>
 * 模块编码和分类编码在ExpEnumCodeConstant类中声明
 *
 */
public interface SysExpEnumConstant {
    /**
     * 模块分类编码（2位）
     * <p>
     * system模块异常枚举编码
     */
    int SYS_EXP_MODULE_CODE = 70;

    /* 分类编码（4位） */
    /**
     * 系统应用相关异常枚举
     */
    int SYS_APP_EXCEPTION_ENUM = 1100;

    /**
     * 参数校验异常枚举
     */
    int PARAM_EXCEPTION_ENUM = 1200;

    /**
     * 文件信息表相关枚举
     */
    int SYS_FILE_INFO_EXCEPTION_ENUM = 1300;

    /**
     * 请求方法相关异常枚举
     */
    int REQUEST_METHOD_EXCEPTION_ENUM = 1400;

    /**
     * 请求类型相关异常枚举
     */
    int REQUEST_TYPE_EXCEPTION_ENUM = 1500;

    /**
     * 服务器内部相关异常枚举
     */
    int SERVER_EXCEPTION_ENUM = 1600;

    /**
     * 状态相关异常枚举
     */
    int STATUS_EXCEPTION_ENUM = 1700;

    /**
     * 包装相关异常枚举
     */
    int WRAPPER_EXCEPTION_ENUM = 1800;

    /**
     * 系统用户相关异常枚举
     */
    int SYS_USER_EXCEPTION_ENUM = 2000;

    /**
     * 系统角色相关异常枚举
     */
    int SYS_ROLE_EXCEPTION_ENUM = 2400;

    /**
     * Oauth登录相关
     */
    int OAUTH_EXCEPTION_ENUM = 2500;

    /**
     * 认证异常枚举
     */
    int AUTH_EXCEPTION_ENUM = 2501;

    /**
     * 授权和鉴权异常的枚举
     */
    int PERMISSION_EXCEPTION_ENUM = 2502;






}
