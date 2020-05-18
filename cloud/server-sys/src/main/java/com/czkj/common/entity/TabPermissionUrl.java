package com.czkj.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

public class TabPermissionUrl implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id",value = "主键",hidden = true)
    private String id;

    @ApiModelProperty(name = "name",value = "URL",required = true)
    private String name;

    @ApiModelProperty(name = "perId",value = "权限id")
    private String perId;

    @ApiModelProperty(name = "remark",value = "描述")
    private String remark;

    @ApiModelProperty(name = "createTime",value = "创建时间",hidden = true)
    private Date createTime;

    @ApiModelProperty(name = "lastUpdateTime",value = "修改时间",hidden = true)
    private Date lastUpdateTime;

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

    public String getPerId() {
        return perId;
    }

    public void setPerId(String perId) {
        this.perId = perId;
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
        return "TabPermissionUrl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", perId=" + perId +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}