package com.czkj.permission.dao;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabPermissionUrl;
import com.czkj.utils.PageResult;

import java.util.List;

/**
 * @Author: SunMin
 * @Description:菜单管理
 * @Date:Create：in 2020/4/22 11:22
 * @Modified By：
 */
public interface MenuDao {


    /**
     * 查询所有权限信息
     * @param available 可用标识
     * @param name 权限名
     * @param currentPage 当前页
     * @param size 每页显示条数
     * @return
     */
    PageResult<TabPermission> queryAllList(String available,String name, Integer currentPage, Integer size);


    /**
     * 根据资源id查询对应URL信息
     * @param perId 权限id
     * @return
     */
    List<TabPermissionUrl> queryAllUrlList(String perId);

    /**
     * 新增权限数据及选择对应URL
     * @param name 权限名
     * @param remark 权限描述
     */
    String savePermission(String name,String remark);

    /**
     * 新增权限URL
     * @param tabPermissionUrl
     */
    void savePerUrl(TabPermissionUrl tabPermissionUrl);

    /**
     * 根据主键获取权限信息
     * @param key 主键
     */
    TabPermission queryPermission(String key);

    /**
     * 根据权限id获取对应url
     * @param perId 权限id
     * @return
     */
    List<TabPermissionUrl> queryPerUrlList(String perId);

    /**
     * 修改权限数据
     * @param name 权限名
     * @param key 权限主键
     * @param remark 权限描述信息
     */
    void updatePermission(String name,String remark,String key);

    /**
     * 删除指定权限id的URL
     * @param perId 权限id
     */
    void deletePerUrlByPerId(String perId);

    /**
     * 查询权限对应权限名是否存在
     * @param name 权限名
     * @param keyId 主键id-用于修改校验的标识
     * @return
     */
    TabPermission queryPerByName(String name,String keyId);

    /**
     * 查询用户输入的url是否存在
     * @param url url
     * @return
     */
    TabPermissionUrl queryPerUrlByUrl(String url);


    /**
     * 修改权限可用状态
     * @param id 权限id
     * @param available 是否可用
     */
    void updatePermissionForAvlia(String available, String id);

    /**
     * 修改权限URL可用状态
     * @param perId 权限id
     * @param available 是否可用
     */
    void updatePerUrlAvailable(String available, String perId);

    /**
     * 查询权限及对应角色信息
     * @param pid 资源id
     * @return
     */
    TabPermission queryPermissionAndRole(String pid);
}
