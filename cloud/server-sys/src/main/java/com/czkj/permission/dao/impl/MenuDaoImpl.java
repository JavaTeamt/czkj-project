package com.czkj.permission.dao.impl;

import com.czkj.common.entity.TabPermission;
import com.czkj.common.entity.TabPermissionUrl;
import com.czkj.common.entity.TabRole;
import com.czkj.permission.dao.MenuDao;
import com.czkj.utils.PageResult;
import lombok.extern.slf4j.Slf4j;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SunMin
 * @Description:实现
 * @Date:Create：in 2020/4/22 11:27
 * @Modified By：
 */
@Repository
@Slf4j
public class MenuDaoImpl implements MenuDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PageResult<TabPermission> queryAllList(String available, String name, Integer currentPage, Integer size) {

        //总条数
        int totalCount = 0;

        List<TabPermission> permissionList = new ArrayList<>();
        //查询总记录数
        String sqlByCount = "select count(1) from tab_permission where 1=1 ";

        String sql = "select id,name,remark,last_update_time from tab_permission where 1=1 ";
        if (null != currentPage && null != size) {
            if (StringUtils.isNotBlank(available)) {
                sql += "and available = ? limit ?,?";
                sqlByCount += "and available = ?";
                totalCount = jdbcTemplate.queryForObject(sqlByCount, Integer.class, available);
                permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class), available, (currentPage - 1) * size, size);
            } else if (StringUtils.isNotBlank(name)) {
                sql += "and name like concat('%',?,'%') limit ?,?";
                sqlByCount += "and name like concat('%',?,'%')";
                totalCount = jdbcTemplate.queryForObject(sqlByCount, Integer.class, name);
                permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class), name, (currentPage - 1) * size, size);
            } else if (StringUtils.isNotBlank(available) && StringUtils.isNotBlank(name)) {
                sql += "and like concat('%',?,'%') and available=? limit ?,?";
                sqlByCount += "and name like concat('%',?,'%') and available=?";
                totalCount = jdbcTemplate.queryForObject(sqlByCount, Integer.class, name, available);
                permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class), name, available, (currentPage - 1) * size, size);
            } else {
                sql += "limit ?,?";
                totalCount = jdbcTemplate.queryForObject(sqlByCount, Integer.class);
                permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class), (currentPage - 1) * size, size);
            }
        } else if (StringUtils.isNotBlank(available)) {
            sql += "and available = ?";
            permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class), available);
        } else if (StringUtils.isNotBlank(name)) {
            sql += "and name like concat('%',?,'%')";
            permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class), name);

        } else if (StringUtils.isNotBlank(available) && StringUtils.isNotBlank(name)) {
            sql += "and name like concat('%',?,'%') and available=?";
            permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class), name, available);
        } else {
            permissionList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermission.class));
        }

        //遍历过去URL信息
        if (permissionList.size() > 0) {
            for (int i = 0; i < permissionList.size(); i++) {
                //根据权限id获取对应URL
                List<TabPermissionUrl> tabPermissionUrls = queryAllUrlList(permissionList.get(i).getId());
                permissionList.get(i).setUrlList(tabPermissionUrls);
            }
        }
        return new PageResult<>(currentPage, size, totalCount, permissionList);
    }

    @Override
    public List<TabPermissionUrl> queryAllUrlList(String perId) {
        String sql = "select name,remark from tab_permission_url where per_id =?";
        List<TabPermissionUrl> urlList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermissionUrl.class), perId);
        return urlList;
    }

    @Override
    public String savePermission(String name, String remark) {
        log.info("url类型=" + name.getClass().toString() + ",描述信息类型=" + remark.getClass().toString());
        //获取当前时间
        Timestamp timestamp = new Timestamp(new Date().getTime());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into tab_permission(name,available,remark,create_time) values(?,?,?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, "1");
                ps.setString(3, remark);
                ps.setTimestamp(4, timestamp);
                return ps;
            }
        }, keyHolder);
        String key = keyHolder.getKey().toString();
        return key;
    }

    @Override
    public void savePerUrl(TabPermissionUrl tabPermissionUrl) {
        String sql = "insert into tab_permission_url(name,per_id,available,remark,create_time,last_update_time) values(?,?,?,?,?,?)";
        System.out.println("创建时间为：" + new Date() + ",最后修改日期为：" + tabPermissionUrl.getLastUpdateTime());
        jdbcTemplate.update(sql, tabPermissionUrl.getName(), tabPermissionUrl.getPerId(), "1", tabPermissionUrl.getRemark(), new Date(), tabPermissionUrl.getLastUpdateTime());
    }

    @Override
    public TabPermission queryPermission(String key) {
        String sql = "select id,name,remark,available from tab_permission where id=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, key);
        while (sqlRowSet.next()) {
            TabPermission tabPermission = new TabPermission();
            tabPermission.setId(sqlRowSet.getString("id"));
            tabPermission.setName(sqlRowSet.getString("name"));
            tabPermission.setRemark(sqlRowSet.getString("remark"));
            tabPermission.setAvailable(sqlRowSet.getString("available"));
            List<TabPermissionUrl> permissionUrls = queryAllUrlList(tabPermission.getId());
            if (permissionUrls.size() > 0) {
                tabPermission.setUrlList(permissionUrls);
            }
            return tabPermission;
        }
        return null;
    }

    @Override
    public List<TabPermissionUrl> queryPerUrlList(String perId) {
        String sql = "select name from tab_permission_url where per_id=?";
        List<TabPermissionUrl> permissionUrls = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabPermissionUrl.class), perId);
        return permissionUrls;
    }

    @Override
    public void updatePermission(String name, String remark, String key) {
        String sql = "update tab_permission set name=?,remark = ?,last_update_time=? where id =?";
        jdbcTemplate.update(sql, name, remark, new Date(), key);
    }

    @Override
    public void deletePerUrlByPerId(String perId) {
        String sql = "delete from tab_permission_url where per_id=?";
        jdbcTemplate.update(sql, perId);
    }

    @Override
    public TabPermission queryPerByName(String name, String keyId) {
        SqlRowSet sqlRowSet = null;
        String sql = "select name from tab_permission where name=? ";
        if (StringUtils.isNotBlank(keyId)) {
            sql += "and id<>?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, name, keyId);
        } else {
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, name);
        }
        while (sqlRowSet.next()) {
            TabPermission tabPermission = new TabPermission();
            tabPermission.setName(sqlRowSet.getString("name"));
            return tabPermission;
        }
        return null;
    }

    @Override
    public TabPermissionUrl queryPerUrlByUrl(String url, String perId) {
        SqlRowSet sqlRowSet = null;
        String sql = "select name,per_id from tab_permission_url where name=? ";
        if (StringUtils.isNotBlank(perId)) {
            sql += "and per_id<>?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, url, perId);
        } else {
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, url);
        }
        while (sqlRowSet.next()) {
            TabPermissionUrl tabPermissionUrl = new TabPermissionUrl();
            tabPermissionUrl.setName(sqlRowSet.getString("name"));
            tabPermissionUrl.setPerId(sqlRowSet.getString("per_id"));
            return tabPermissionUrl;
        }
        return null;
    }

    @Override
    public void updatePermissionForAvlia(String available, String id) {
        String sql = "update tab_permission set available=? where id=? ";
        jdbcTemplate.update(sql, available, id);
    }

    @Override
    public void updatePerUrlAvailable(String available, String perId) {
        String sql = "update tab_permission_url set available=? where per_id=? ";
        jdbcTemplate.update(sql, available, perId);
    }

    @Override
    public List<TabRole> queryRoleList(String pid) {
        String sql = "select r.name from tab_permission p RIGHT JOIN tab_role_permission rp on p.id=rp.sys_permission_id LEFT JOIN tab_role r on r.id=sys_role_id where p.id=?";
        List<TabRole> tabRoles = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), pid);
        return tabRoles;
    }
}
