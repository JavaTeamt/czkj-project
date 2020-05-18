package com.czkj.role.service;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.utils.PageResult;

import java.util.List;

/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/4/24 12:49
 * @Modified By：
 */
public interface RoleService {

    /**
     * 获取所有角色信息
     * @param page 当前页
     * @param size 每页显示条数
     * @param roleName 角色名
     * @param available 是否可用
     * @return
     */
    PageResult queryRoleList(Integer page, Integer size, String roleName, String available);

    /**
     * 校验角色信息是否存在
     * @param roleName 角色名
     * @param roleCode 角色编码
     * @param keyid 主键id，用于修改校验标识属性
     * @return
     */
    boolean validateRoleExit(String roleName, String roleCode,String keyid);

    /**
     *  新增角色信息
     * @param tabRole 实体类接收参数
     * @return
     */
    boolean saveRole(TabRole tabRole);

    /**
     * 获取对应指定角色信息及角色对应权限
     * @param roleId 角色id
     * @return
     */
    TabRole queryRoleById(String roleId);

    /**
     * 绑定角色权限关系存储关系数据
     * @param perIds 权限id数组
     * @param roleId 角色id
     * @return
     */
    boolean savePermissionAndRole(String[] perIds,String roleId);

    /**
     * 修改角色信息
     * @param tabRole 实体类接收参数
     */
    boolean updateRoleById(TabRole tabRole);


    /**
     * 删除指定角色-批量删除
     * @param roleId 角色id
     * @return
     */
    boolean deleteRole(String roleId);

//    /**
//     * 根据角色id查询关系表中是否存在数据
//     * @param roleId
//     * @return
//     */
//    List<TabRolePermission> queryByRoleId(String roleId);

    /**
     * 获取角色对应权限id，用于id绑定，显示用户绑定的权限
     * @param rid 角色id
     * @return
     */
    List<TabPermission> queryPermissionListByRole(String rid);

    /**
     * 启用角色
     * @param roleId 角色id
     * @return
     */
    boolean enableRole(String roleId);

}
