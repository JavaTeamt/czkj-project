package com.czkj.user.service;


import com.czkj.common.entity.TabCustomer;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabSubscriber;
import com.czkj.common.entity.TabUserRole;
import com.czkj.utils.PageResult;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


/**
 * @author hello
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param user
     */
    boolean userRegister(TabSubscriber user);

    /**
     * 校验账号，手机号是否存在
     * @param id 登录账号
     * @param moblie 手机号
     * @param keyId 主键id-用于修改校验标识
     * @return
     */
    boolean vUserExits(String id, String moblie,String keyId);

    /**
     * 验证对应用户是否绑定手机号
     * @param id 登录账号
     * @return
     */
    String vPhoneExits(String id);

    /**
     * 忘记密码-设置新密码
     * @param password 新密码
     * @param id 登录账号
     * @return
     */
    boolean forgetPassoword(String password, String id);

    /**
     * 更换手机号
     * @param phone 用户手机号
     * @param id 登录账号
     * @return
     */
    boolean updateUserPhone(String phone, String id);

    /**
     * 更换头像
     * @param headImg 头像显示路径（加文件名）-存入数据库
     * @param id 登录账号
     * @return
     */
    boolean updateHeadImg(MultipartFile headImg, String id);

    /**
     * 根据登录账号获取用户信息
     * @param id 登录账号
     * @return
     */
    TabSubscriber getUserById(String id);

    /**
     * 实名认证
     * @param tabCustomer 实体接收参数数据
     * @param userId 登录账号
     * @return
     */
    boolean Authentication(TabCustomer tabCustomer, String userId);

    /**
     * 显示用户列表（分页）
     * @param currentPage 当前页
     * @param size 每页显示条数
     * @return
     */
    PageResult getUserList(Integer currentPage, Integer size);

    /**
     * 获取用户对应角色
     * @param userId 用户id
     * @return
     */
    List<TabRole> getRoleListByUId(String userId);


    /**
     * 修改用户
     * @param tabSubscriber 实体类接收参数
     * @return
     */
    boolean updateUser(TabSubscriber tabSubscriber);

    /**
     * 新增用户
     * @param tabSubscriber 用户实体类
     * @return
     */
    boolean saveUser(TabSubscriber tabSubscriber);

    /**
     * 新增用户角色
     * @param roleIds 角色id
     * @param userId 用户登录账号
     * @return
     */
    boolean saveRoleByUser(String[] roleIds,String userId);

    /**
     * 删除用户
     * @param userId 登录账号
     * @return
     */
    boolean deleteUser(String userId);


}
