package com.czkj.auth.dao.impl;

import com.czkj.auth.dao.LoginDao;
import com.czkj.auth.entity.TabRole;
import com.czkj.auth.entity.TabSubscriber;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SunMin
 * @description 实现
 * @create 2020/4/15
 * @since 1.0.0
 */
@Repository
public class LoginDaoImpl implements LoginDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public TabSubscriber login(String username) {
        //定义sql
        String sql = " select id,password from tab_subscriber where id=?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, username);

        while (sqlRowSet.next()){
            TabSubscriber tabSubscriber = new TabSubscriber();
            tabSubscriber.setId(sqlRowSet.getString("id"));
            tabSubscriber.setPassword(sqlRowSet.getString("password"));
            List<TabRole> tabRoles = queryRoleListByUserName(username);
            if (tabRoles.size()>0){
                List<TabRole> roleList = new ArrayList<>();
                for (TabRole tabRole : tabRoles) {
                    roleList.add(tabRole);
                }
                tabSubscriber.setTabRoleList(roleList);
            }
            return tabSubscriber;
        }
        return null;
    }

    @Override
    public void updateLoginStatus(String username,boolean login) {
        String sql = "";
        if (login){
            sql = "update tab_subscriber set loginstatus=1 where id=?";
        }else {
            sql = "update tab_subscriber set loginstatus=0 where id=?";
        }
        jdbcTemplate.update(sql,username);
    }

    @Override
    public List<TabRole> queryRoleListByUserName(String userName) {
        String sql = "select role.name from tab_role role right JOIN tab_user_role userRole on role.id=userRole.sys_role_id left JOIN tab_subscriber user on userRole.sys_user_id = user.id where user.id=?";
        List<TabRole> roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), userName);
        return roleList;
    }
}
