package com.czkj.permission.service;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabPermissionUrl;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.utils.PageResult;

import java.util.Date;
import java.util.List;

/**
 * @Author: SunMin
 * @Description:菜单管理逻辑处理
 * @Date:Create：in 2020/4/22 11:38
 * @Modified By：
 */
public interface MenuService {

    /**
     * 查询所有权限信息
     *
     * @param available 可用标识
     * @param name 权限名
     * @param currentPage 当前页
     * @param size 每页显示条数
     * @return
     */
    PageResult<TabPermission> getAllList(String available,String name, Integer currentPage, Integer size);

    /**
     * 新增权限
     * @param tabPermission 实体类接收参数
     * @return
     */
    boolean savePermission(TabPermission tabPermission);

    /**
     * 根据主键获取权限信息
     *
     * @param key 主键
     */
    TabPermission getPermissionByKey(String key);

    /**
     * 修改权限数据
     * @param tabPermission 使用实体类接收参数
     * @return
     */
    boolean updatePerByKey(TabPermission tabPermission);

    /**
     * 检验信息是否存在
     * @param name 权限名
     * @param url 权限url
     * @param keyId 主键id-用于修改校验标识
     * @return
     */
    boolean queryExit(String name,String url,String keyId);

    /**
     * 废弃指定资源
     * @param key 主键id
     * @return
     */
    boolean deleteTabpermission(String key);

    /**
     * 启用资源
     * @param key 主键id
     * @return
     */
    boolean enablePermission(String key);

    /**
     * 查询资源及对应角色信息
     * @param pid 资源id
     * @return
     */
    TabPermission getPermissionAndRole(String pid);


}
