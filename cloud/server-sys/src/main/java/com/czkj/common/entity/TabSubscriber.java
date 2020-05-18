package com.czkj.common.entity;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author SunMin
 * @description 用户实体类
 * @create 2020/4/14
 * @since 1.0.0
 */
public class TabSubscriber implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键id",name="id")
    @NotBlank(message = "登录账号不能为空")
    private String id;

    @ApiModelProperty(value="手机号",name="mobile",required = true)
    @NotBlank(message = "请输入手机号")
    @Pattern(regexp="^1[34578]\\d{9}$",message="手机号码格式不正确")
    private String mobile;

    @ApiModelProperty(value="密码",name="password",required = true)
    @NotBlank(message = "请输入密码")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,16}$",message = "密码不能包含特殊字符并且长度6-16")
    private String password;

    @ApiModelProperty(value="客户id",name="custid",hidden = true)
    private String  custid;

    @ApiModelProperty(value="登录状态",name="loginStatus",hidden = true)
    private String loginStatus;

    @ApiModelProperty(value="头像路径",name="headImg",hidden = true)
    private String headImg;

    @ApiModelProperty(value="创建时间",name="createTime",hidden = true)
    private Date createTime;

    @ApiModelProperty(value="客户实体类")
    private TabCustomer tabCustomer;

    @ApiModelProperty(value="角色实体类集合(一对多)")
    private List<TabRole> tabRoleList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public TabCustomer getTabCustomer() {
        return tabCustomer;
    }

    public void setTabCustomer(TabCustomer tabCustomer) {
        this.tabCustomer = tabCustomer;
    }

    public List<TabRole> getTabRoleList() {
        return tabRoleList;
    }

    public void setTabRoleList(List<TabRole> tabRoleList) {
        this.tabRoleList = tabRoleList;
    }

    @Override
    public String toString() {
        return "TabSubscriber{" +
                "id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", custid=" + custid +
                ", loginStatus='" + loginStatus + '\'' +
                ", headImg='" + headImg + '\'' +
                ", createTime='" + createTime + '\'' +
                ", tabCustomer=" + tabCustomer +
                ", tabRoleList=" + tabRoleList +
                '}';
    }
}
