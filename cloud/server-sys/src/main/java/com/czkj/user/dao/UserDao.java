package com.czkj.user.dao;

import com.czkj.common.entity.TabCustomer;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabSubscriber;
import com.czkj.utils.PageResult;

import java.util.List;

/**
 * 用户信息处理持久层接口
 * @author hello
 */
public interface UserDao<T> {

    /**
     * 添加用户信息-注册
     * @param user 用户实体接收需要添加属性的数据
     * @return
     */
    void addUserJ(TabSubscriber user);

    /**
     * 查询用户对应信息是否存在
     * @param key 字段名
     * @param value 值
     * @param keyId 主键id用于修改校验标识
     * @return
     */
    TabSubscriber selectUserByKey(String key, String value,String keyId);

    /**
     * 修改用户基本信息-用户基本信息
     * @param id 登录账号
     * @param mobile 手机号
     * @param headImg 头像
     * @return
     */
    void updateUserJ(String id, String mobile, String headImg);

    /**
     * 修改用户手机密码
     * @param password 密码
     * @param id 登录账号
     * @return
     */
    void updateUserPassword(String password, String id);

    /**
     * 修改对应客户手机号
     * @param mobile 手机号
     * @param custid 客户id
     * @return
     */
    void updateCustomerMobile(String mobile, String custid);

    /**
     * 添加客户信息
     * @param tabCustomer 实体接收参数数据
     * @return
     */
    String addCustomer(TabCustomer tabCustomer);

    /**
     * 修改当前用户客户id
     * @param custid 客户id
     * @param id 登录账号
     * @return
     */
    void updateUcustid(String custid, String id);

    /**
     * 查询所有用户信息及用户对应客户(分页)
     * @param currentPage 当前页
     * @param size 每页显示条数
     * @return
     */
    PageResult<TabSubscriber> seletAllUser(Integer currentPage, Integer size);

    /**
     * 获取用户对应角色
     * @param userId 用户id
     * @return
     */
    List<TabRole> queryRoleList(String userId);
    /**
     * 根据身份证查询客户id-绑定用户
     * @param certid 身份证编码
     * @return
     */
    TabCustomer selectCustomerByCertid(String certid);

    /**
     * 根据用户登录账号查询客户信息
     * @param id 登录账号
     * @return
     */
    TabCustomer selectCustomerByUid(String id);

    /**
     * 根据登录账号查询用户信息及相关客户信息
     * @param id 登录账号
     * @return
     */
    TabSubscriber selectAllUserByUid(String id);

    /**
     * 添加用户角色关系绑定记录
     * @param roleId 角色id数组
     * @param userId 用户id
     */
    void saveUserAndRole(String roleId,String userId);

    /**
     * 编辑用户
     * @param user 实体
     * @return
     */
    void updateUser(TabSubscriber user);

//    /**
//     * 修改客户信息
//     * @param tabCustomer
//     * @return
//     */
//    void updateCustomer(TabCustomer tabCustomer);

    /**
     * 删除用户
     * @param userid 登录账号
     * @return
     */
    void deleteUser(String userid);

    /**
     * 根据用户角色关系表中用户id删除指定记录
     * @param userId 用户id
     */
    void deleteUserAndRole(String userId);

}
