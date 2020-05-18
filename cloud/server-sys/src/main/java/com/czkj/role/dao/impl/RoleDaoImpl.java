package com.czkj.role.dao.impl;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabPermissionUrl;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabRolePermission;
import com.czkj.permission.dao.MenuDao;
import com.czkj.role.dao.RoleDao;
import com.czkj.utils.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/4/24 9:39
 * @Modified By：
 */
@Repository
public class RoleDaoImpl implements RoleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MenuDao menuDao;

    @Override
    public PageResult<TabRole> queryRoleList(Integer page, Integer size, String roleName, String available) {

        //初始化
        Integer totalCount = null;

        //初始化
        List<TabRole> roleList = new ArrayList<>();

        //查询总条数
        String sqlForCount = "select count(1) from tab_role where 1=1 ";

        //查询全部角色信息
        String sql = "select id,code,name,available,last_update_time from tab_role where 1=1 ";

        if (null != page && null != size) {
            //如果满足条件就加条件查询
            if (StringUtils.isNotBlank(roleName) && StringUtils.isNotBlank(available)) {
                sql += "and available=? and name like concat('%',?,'%') limit ?,?";
                sqlForCount += "and available=? and name like concat('%',?,'%') ";
                totalCount = jdbcTemplate.queryForObject(sqlForCount, Integer.class, available, roleName);
                roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), available, roleName, (page - 1) * size, size);
            } else if (StringUtils.isNotBlank(roleName)) {
                sql += "and name like concat('%',?,'%') limit ?,?";
                sqlForCount += "and name like concat('%',?,'%') ";
                totalCount = jdbcTemplate.queryForObject(sqlForCount, Integer.class, roleName);
                roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), roleName, (page - 1) * size, size);
            } else if (StringUtils.isNotBlank(available)) {
                sql += "and available=? limit ?,?";
                sqlForCount += "and available=? ";
                totalCount = jdbcTemplate.queryForObject(sqlForCount, Integer.class, available);
                roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), available, (page - 1) * size, size);
            } else {
                sql += "limit ?,?";
                totalCount = jdbcTemplate.queryForObject(sqlForCount, Integer.class);
                roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), (page - 1) * size, size);
            }

        } else if (StringUtils.isNotBlank(roleName) && StringUtils.isNotBlank(available)) {
            sql += "and available=? and name like concat('%',?,'%')";
            roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), available, roleName);
        } else if (StringUtils.isNotBlank(available)) {
            sql += "and available=?";
            roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class));
        } else if (StringUtils.isNotBlank(roleName)) {
            sql += "and name like concat('%',?,'%')";
            roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), roleName);
        } else {
            roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class));
        }
        return new PageResult<>(page, size, totalCount, roleList);
    }

    @Override
    public List<TabPermission> queryPermissionListByRole(String rid) {
        String permissionSql = "select p.id from tab_permission p RIGHT JOIN  tab_role_permission rp on p.id=rp.sys_permission_id left JOIN  tab_role r on r.id=rp.sys_role_id where r.id=?";
        List<TabPermission> permissions = jdbcTemplate.query(permissionSql, new BeanPropertyRowMapper<>(TabPermission.class), rid);
        if (permissions.size() > 0) {
            for (int i = 0; i < permissions.size(); i++) {
                List<TabPermissionUrl> permissionUrls = menuDao.queryPerUrlList(permissions.get(i).getId());
                permissions.get(i).setUrlList(permissionUrls);
            }
        }
        return permissions;
    }

    @Override
    public void addRole(String name, String code) {
        String sql = "insert into tab_role(code,name,available,create_time) values(?,?,?,?)";
        jdbcTemplate.update(sql,code,name,"1",new Date());
    }

    @Override
    public void addRoleAndPermission(String perId,String roleId) {
        String sql = "insert into tab_role_permission(sys_role_id,sys_permission_id,create_time,last_update_time) values(?,?,?,?)";
        jdbcTemplate.update(sql, roleId,
                perId,
                new Date(),
                new Date());
    }

    @Override
    public TabRole queryRoleById(String roleId) {
        TabRole tabRole = selectKeyOfValue("id", roleId, null);
        return tabRole;
    }

    @Override
    public void updateRoleById(TabRole tabRole) {
        String sql = "update tab_role set code=?,name=?,last_update_time=? where id=?";
        jdbcTemplate.update(sql, tabRole.getCode(),
                tabRole.getName(),
                new Date(),
                tabRole.getId());
    }

    @Override
    public List<TabRolePermission> queryRelationByRoleId(String roleId) {
        String sql = "select id from tab_role_permission where 1=1 and sys_role_id=?";
        List<TabRolePermission> tabRolePermissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRolePermission.class), roleId);
        return tabRolePermissionList;
    }

    @Override
    public void deleteRoleAndPer(String roleId) {
        jdbcTemplate.update("delete from tab_role_permission where sys_role_id=?", roleId);
    }

    @Override
    public void updateRoleAvailable(String available,String roleId) {
        jdbcTemplate.update("update tab_role set available=? where id=?", available,roleId);
    }

    @Override
    public TabRole selectKeyOfValue(String key, String value, String keyid) {
        SqlRowSet sqlRowSet = null;
        if (StringUtils.isNotBlank(keyid)) {
            String sql = "select id,code,name from tab_role where " + key + "=? and id<>?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, value, keyid);
        } else {
            String sql = "select id,code,name,available from tab_role where " + key + "=?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, value);
        }
        while (sqlRowSet.next()) {
            TabRole tabRole = new TabRole();
            tabRole.setId(sqlRowSet.getString("id"));
            tabRole.setCode(sqlRowSet.getString("code"));
            tabRole.setName(sqlRowSet.getString("name"));
            return tabRole;
        }
        return null;
    }
}
