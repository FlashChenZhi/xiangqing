package com.util.pages;

/**
 * 用于extjs grid的分页类
 * User: xiongying
 * Date: 14-10-17
 * Time: 下午18:03
 * To change this template use File | Settings | File Templates.
 */
public class GridPages extends Pages {


    /**
     * 设定当前页
     *
     * @param page
     */
    public void setPage(int page) {
        this.setCurrentPage(page);
    }


    /**
     * 每页显示条数
     *
     * @param limit
     */
    public void setLimit(int limit) {
        this.setPageSize(limit);
    }
}
