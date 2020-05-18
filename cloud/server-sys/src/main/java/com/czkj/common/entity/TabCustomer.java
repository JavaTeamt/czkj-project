package com.czkj.common.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author SunMin
 * @description 客户实体类
 * @create 2020/4/14
 * @since 1.0.0
 */
public class TabCustomer implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键id",name="id",hidden = true)
    private String id;

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value="姓名",name="name")
    private String name;

    @NotBlank(message = "性别不能为空")
    @ApiModelProperty(value="性别",name="sex")
    private String sex;

    @NotBlank(message = "证件类型不能为空")
    @ApiModelProperty(value="证件类型",name="certType")
    private String certType;

    @NotBlank(message = "证件编码不能为空")
    @ApiModelProperty(value="证件编码",name="certid")
    private String certid;

    @ApiModelProperty(value="实名认证标识",name="custidentify",hidden = true)
    private String custidentify;

    @ApiModelProperty(value="客户手机号",name="mobile",hidden = true)
    private String mobile;

    @ApiModelProperty(value="创建时间",name="createtime",hidden = true)
    private Date createtime;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertid() {
        return certid;
    }

    public void setCertid(String certid) {
        this.certid = certid;
    }

    public String getCustidentify() {
        return custidentify;
    }

    public void setCustidentify(String custidentify) {
        this.custidentify = custidentify;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "TabCustomer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", certType='" + certType + '\'' +
                ", certid='" + certid + '\'' +
                ", custidentify='" + custidentify + '\'' +
                ", mobile='" + mobile + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}
