package com.gallop.microservice.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * author gallop
 * date 2021-11-02 11:04
 * Description:
 * Modified By:
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 2687270050026436873L;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateUser;
}
