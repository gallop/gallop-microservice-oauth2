package com.gallop.microservice.common.base;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * author gallop
 * date 2021-10-10 16:45
 * Description:
 * Modified By:
 */
@Data
public class PagedResult<T> implements Serializable {
    private static final long serialVersionUID = 3710286336961485674L;
    /**
     * 默认分页彩虹展示数量
     */
    public static final int RAINBOW_NUM = 5;

    /**
     * 第几页
     */
    private Integer pageNo = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 20;

    /**
     * 总页数
     */
    private Integer totalPage = 0;

    /**
     * 总记录数
     */
    private Integer totalRows = 0;

    /**
     * 结果集
     */
    private List<T> rows;

    /**
     * 分页彩虹
     */
    private int[] rainbow;

    public PagedResult() {
    }

    public PagedResult(PageInfo<T> pageInfo) {
        this.setRows(pageInfo.getList());
        this.setTotalRows(Convert.toInt(pageInfo.getTotal()));
        this.setPageNo(Convert.toInt(pageInfo.getPageNum()));
        this.setPageSize(Convert.toInt(pageInfo.getPageSize()));
        this.setTotalPage(PageUtil.totalPage(Convert.toInt(pageInfo.getTotal()),
                Convert.toInt(pageInfo.getSize())));
        this.setRainbow(PageUtil.rainbow(Convert.toInt(pageInfo.getPageNum()),
                Convert.toInt(this.getTotalPage()), RAINBOW_NUM));
    }

}
