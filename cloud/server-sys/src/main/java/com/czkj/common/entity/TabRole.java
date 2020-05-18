package com.czkj.common.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @ApiModelProperty(value="主键id",name="id")
    private String id;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @ApiModelProperty(value="角色编码",name="code",required = true)
    private String code;

    /**
     * 角色名
     */
    @NotBlank(message = "角色名不能为空")
    @ApiModelProperty(value="角色名",name="name",required = true)
    private String name;

    /**
     * 是否可用
     */
    @ApiModelProperty(value="是否可用",name="available",hidden = true)
    private String available;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间",name="createTime",hidden = true)
    private Date createTime;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value="最后修改时间",name="lastUpdateTime",hidden = true)
    private Date lastUpdateTime;

    @ApiModelProperty(value="资源")
    private List<TabPermission> tabPermissions = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available == null ? null : available.trim();
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

    public List<TabPermission> getTabPermissions() {
        return tabPermissions;
    }

    public void setTabPermissions(List<TabPermission> tabPermissions) {
        this.tabPermissions = tabPermissions;
    }

    @Override
    public String toString() {
        return "TabRole{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", available='" + available + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", tabPermissions=" + tabPermissions +
                '}';
    }
}