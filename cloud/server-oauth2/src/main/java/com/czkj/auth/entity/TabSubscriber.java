package com.czkj.auth.entity;

import io.swagger.annotations.ApiModelProperty;

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

    /**
     * 登录账号
     */
    private String id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 客户id
     */
    private Long custid;

    /**
     * 登录状态
     */
    private String loginStatus;

    /**
     * 头像路径
     */
    private String headImg;

    /**
     * 创建时间
     */
    private Date createTime;

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

    public Long getCustid() {
        return custid;
    }

    public void setCustid(Long custid) {
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
                ", createTime=" + createTime +
                ", tabRoleList=" + tabRoleList +
                '}';
    }
}
