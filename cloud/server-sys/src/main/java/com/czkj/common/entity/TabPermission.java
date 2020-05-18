package com.czkj.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键id",name="id")
    private String id;

    @ApiModelProperty(value="资源名称",name="name",required = true)
    private String name;

    @ApiModelProperty(name = "available",value = "可用标识：0-不可用，1-可用",hidden = true)
    private String available;

    @ApiModelProperty(name = "remark",value = "描述")
    private String remark;

    @ApiModelProperty(name = "createTime",value = "创建日期",hidden = true)
    private Date createTime;

    @ApiModelProperty(name = "createTime",value = "最后修改日期",hidden = true)
    private Date lastUpdateTime;

    @ApiModelProperty(value = "url集合，一个权限对应多条url",name = "urlList")
    private List<TabPermissionUrl> urlList=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TabPermissionUrl> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<TabPermissionUrl> urlList) {
        this.urlList = urlList;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TabPermission{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", available='" + available + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", urlList=" + urlList +
                '}';
    }
}