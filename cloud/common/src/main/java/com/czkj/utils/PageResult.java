package com.czkj.utils;

import java.util.List;

/**
 * @author SunMin
 * @description 分页封装
 * @create 2020/4/16
 * @since 1.0.0
 */
public class PageResult<T>{
    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 每页显示条数
     */
    private Integer size;

    /**
     * 总条数
     */
    private Integer totalCount;

    /**
     * 总页面
     */
    private Integer totalPage;

    /**
     * 当前页显示数据
     */
    private List<T> items;

    public PageResult() {
    }

    public PageResult(Integer currentPage, Integer size, Integer totalCount, List<T> items) {
        this.currentPage = currentPage;
        this.size = size;
        this.totalCount = totalCount;
        this.items = items;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotalPage() {
        return (int)Math.ceil((double) totalCount/size);
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "currentPage=" + currentPage +
                ", size=" + size +
                ", totalCount=" + totalCount +
                ", items=" + items +
                ", totalPage=" + totalPage +
                '}';
    }
}
