package com.gallop.microservice.user.param;

import com.gallop.microservice.common.base.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * author gallop
 * date 2021-11-21 11:53
 * Description:
 * Modified By:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleParam extends BaseParam {
    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class})
    private Integer id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空，请检查name参数", groups = {add.class, edit.class})
    private String name;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空，请检查code参数", groups = {add.class, edit.class})
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
     * 授权菜单
     */
    @NotNull(message = "授权菜单不能为空，请检查grantMenuIdList参数", groups = {grantMenu.class})
    private List<String> grantMenuIdList;

    /**
     * 修改前的授权菜单
     */
    private List<String> lastPermissionIds;
}
