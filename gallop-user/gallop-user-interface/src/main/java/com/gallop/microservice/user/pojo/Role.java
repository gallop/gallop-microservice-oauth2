package com.gallop.microservice.user.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 角色描述
     */
    //@TableField("`desc`")
    private String remark;


    /**
     * 创建时间
     */
    @TableField("add_time")
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;
}