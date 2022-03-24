package com.gallop.microservice.user.param;

import com.gallop.microservice.common.base.BaseParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * author gallop
 * date 2021-11-02 10:54
 * Description: 系统字典类型参数
 */
@Data
public class SysDictTypeParam extends BaseParam {
    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class, changeStatus.class})
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空，请检查name参数", groups = {add.class, edit.class})
    private String name;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空，请检查code参数", groups = {add.class, edit.class, dropDown.class,})
    private String code;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空，请检查sort参数", groups = {add.class, edit.class})
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空，请检查status参数", groups = {changeStatus.class})
    private Integer status;
}
