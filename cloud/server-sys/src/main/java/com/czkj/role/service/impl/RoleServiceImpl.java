package com.czkj.role.service.impl;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.permission.dao.MenuDao;
import com.czkj.role.dao.RoleDao;
import com.czkj.role.service.RoleService;
import com.czkj.utils.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/4/24 12:51
 * @Modified By：
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public PageResult queryRoleList(Integer page, Integer size, String roleName, String available) {
        return roleDao.queryRoleList(page, size, roleName, available);
    }

    @Override
    public boolean validateRoleExit(String roleName, String roleCode, String keyid) {
        if (StringUtils.isNotBlank(roleName)) {
            TabRole tabRole = roleDao.selectKeyOfValue("name", new String(roleName).toUpperCase(), keyid);
            if (tabRole != null) {
                return false;
            }
        } else if (StringUtils.isNotBlank(roleCode)) {
            TabRole tabRole = roleDao.selectKeyOfValue("code", new String(roleCode).toUpperCase(), keyid);
            if (tabRole != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveRole(TabRole tabRole) {
        try {
            //执行添加方法，返回主键id
            roleDao.addRole(tabRole.getName(), tabRole.getCode());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePermissionAndRole(String[] perIds,String roleId) {
        try {
            //删除原有关系表中指定角色id记录
            roleDao.deleteRoleAndPer(roleId);
            //如果选择对应权限则绑定对应权限,可选择多个权限
            for (int i=0;i<perIds.length;i++) {
                roleDao.addRoleAndPermission(perIds[i],roleId);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
       return false;
    }

    @Override
    public TabRole queryRoleById(String roleId) {
        TabRole tabRole = roleDao.queryRoleById(roleId);
        return tabRole;
    }

    @Override
    public boolean updateRoleById(TabRole tabRole) {
        //实体化实体类,用来接收更新的数据
        TabRolePermission tabRolePermission = new TabRolePermission();
        try {
            //修改角色
            roleDao.updateRoleById(tabRole);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteRole(String roleId) {
        try {
            roleDao.updateRoleAvailable("0",roleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<TabRolePermission> queryByRoleId(String roleId) {
        return roleDao.queryRelationByRoleId(roleId);
    }

    @Override
    public List<TabPermission> queryPermissionListByRole(String rid) {
        return roleDao.queryPermissionListByRole(rid);
    }

    @Override
    public boolean enableRole(String roleId) {
        try {
            roleDao.updateRoleAvailable("1",roleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
