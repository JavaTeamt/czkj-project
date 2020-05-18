package com.czkj.auth.dao;


import com.czkj.auth.entity.TabRole;
import com.czkj.auth.entity.TabSubscriber;

import java.util.List;

public interface LoginDao {
    /**
     * 用户登录
     *
     * @return
     */
    TabSubscriber login(String username);

    /**
     * 修改对应用户登录状态
     * @param username 登录账号
     * @param login 登录标识（判断是否登录）
     */
    void updateLoginStatus(String username,boolean login);

    /**
     * 根据用户名获取用户所拥有的权限
     * @param userName 登录账号（用户名）
     * @return
     */
    List<TabRole> queryRoleListByUserName(String userName);
}
