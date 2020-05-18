package com.czkj.user.dao.impl;


import com.czkj.user.dao.UserDao;
import com.czkj.common.entity.TabCustomer;
import com.czkj.common.entity.TabRole;
import com.czkj.common.entity.TabSubscriber;
import com.czkj.common.entity.TabUserRole;
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
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * @author SunMin
 * @description 用户信息处理持久层实现
 * @create 2020/4/14
 * @since 1.0.0
 */
@Repository
public class UserDaoImpl<T> implements UserDao<T> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addUserJ(TabSubscriber user) {
        //定义sql
        String sql = "insert into tab_subscriber(id,mobile,password,loginstatus,headimg,createtime) values(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getId(),
                user.getMobile(),
                user.getPassword(),
                "0",
                user.getHeadImg(),
                new Date());
    }

    @Override
    public TabSubscriber selectUserByKey(String key, String value, String keyId) {
        SqlRowSet sqlRowSet = null;
        if (StringUtils.isNotBlank(keyId)) {
            //定义sql
            String sql = "select id,mobile,password,headimg,loginstatus from tab_subscriber where " + key + "=? and id<>?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, value, keyId);
        } else {
            //定义sql
            String sql = "select id,mobile,password,headimg,loginstatus from tab_subscriber where " + key + "=?";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql, value);
        }

        if (sqlRowSet.next()) {
            //创建用户对象
            TabSubscriber tabSubscriber = new TabSubscriber();
            //遍历结果集，获取数据
            tabSubscriber.setId(sqlRowSet.getString("id"));
            tabSubscriber.setMobile(sqlRowSet.getString("mobile"));
            tabSubscriber.setPassword(sqlRowSet.getString("password"));
            tabSubscriber.setHeadImg(sqlRowSet.getString("headimg"));
            tabSubscriber.setLoginStatus(sqlRowSet.getString("loginstatus"));
            return tabSubscriber;
        }
        return null;
    }

    @Override
    public void updateUserJ(String id, String mobile, String headImg) {
        int result = 0;
        //定义sql
        String sql = "update tab_subscriber set ";
        if (StringUtils.isNotBlank(mobile)) {
            sql += "mobile=? where id = ?";
            jdbcTemplate.update(sql, mobile, id);
        } else if (StringUtils.isNotBlank(headImg)) {
            sql += "headimg=? where id=?";
            jdbcTemplate.update(sql, headImg, id);
        }
    }

    @Override
    public void updateUserPassword(String password, String id) {
        //定义sql
        String sql = "update tab_subscriber set password=? where id = ?";

        jdbcTemplate.update(sql, password, id);
    }

    @Override
    public void updateCustomerMobile(String mobile, String custid) {
        String sql = "update tab_customer set mobile = ? where id=?";
        jdbcTemplate.update(sql, mobile, custid);
    }

    @Override
    public String addCustomer(TabCustomer tabCustomer) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        //通过jdbctemplate返回主键
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "insert into tab_customer(name,sex,certtype,certid,custidentify,mobile,createtime) values(?,?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tabCustomer.getName());
                ps.setString(2, tabCustomer.getSex());
                ps.setString(3, tabCustomer.getCertType());
                ps.setString(4, tabCustomer.getCertid());
                ps.setString(5, "1");
                ps.setString(6, tabCustomer.getMobile());
                ps.setTimestamp(7, timestamp);
                return ps;
            }
        }, keyHolder);
        String keyid = keyHolder.getKey().toString();
        return keyid;
    }

    @Override
    public void updateUcustid(String custid, String id) {
        jdbcTemplate.update("update tab_subscriber set custid=? where id=?", custid, id);
    }

    @Override
    public PageResult<TabSubscriber> seletAllUser(Integer currentPage, Integer size) {
        List<TabSubscriber> userList = new ArrayList<>();

        String sql = "select id,mobile,loginstatus from tab_subscriber ";
        //查询用户所有记录总条数
        String sqlForCount = "select count(1) from tab_subscriber";
        //获取
        Integer totalCount = jdbcTemplate.queryForObject(sqlForCount, Integer.class);

        if (null != currentPage && null != size) {
            sql += "limit ?,?";
            userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabSubscriber.class), (currentPage - 1) * size, size);

        } else {
            userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabSubscriber.class));

        }
        for (TabSubscriber tabSubscriber : userList) {
            //获取去对应客户信息
            TabCustomer tabCustomer = selectCustomerByUid(tabSubscriber.getId());
            if (tabCustomer != null) {
                tabSubscriber.setTabCustomer(tabCustomer);
            }
        }
        return new PageResult(currentPage, size, totalCount, userList);
    }

    @Override
    public List<TabRole> queryRoleList(String userId) {
        String sql = "select role.id,role.name from tab_role role right JOIN tab_user_role userRole on role.id=userRole.sys_role_id left JOIN tab_subscriber user on userRole.sys_user_id = user.id where user.id=?";
        List<TabRole> roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TabRole.class), userId);
        return roleList;
    }

    @Override
    public TabCustomer selectCustomerByCertid(String certid) {

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id from tab_customer where certid=?", certid);
        while (sqlRowSet.next()) {
            TabCustomer tabCustomer = new TabCustomer();
            tabCustomer.setId(sqlRowSet.getString("id"));
            return tabCustomer;
        }
        return null;
    }

    @Override
    public TabCustomer selectCustomerByUid(String id) {
        String sql = "select customer.name from tab_subscriber as user right JOIN tab_customer as customer on user.custid=customer.id where user.id=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        while (sqlRowSet.next()) {
            TabCustomer tabCustomer = new TabCustomer();
            tabCustomer.setName(sqlRowSet.getString("name"));
//            tabCustomer.setSex(sqlRowSet.getString("sex"));
//            tabCustomer.setCertid(sqlRowSet.getString("certid"));
//            tabCustomer.setCertType(sqlRowSet.getString("certtype"));
            return tabCustomer;
        }
        return null;
    }

    @Override
    public TabSubscriber selectAllUserByUid(String id) {
        TabSubscriber tabSubscriber = selectUserByKey("id", id, null);
//        if (tabSubscriber != null) {
//            //获取对应客户数据
//            TabCustomer tabCustomer = selectCustomerByUid(tabSubscriber.getId());
//            tabSubscriber.setTabCustomer(tabCustomer);
        return tabSubscriber;
    }

    @Override
    public void saveUserAndRole(String roleId, String userId) {
        String sql = "insert into tab_user_role(sys_user_id,sys_role_id,create_time,last_update_time) values(?,?,?,?)";
        jdbcTemplate.update(sql, userId,
                roleId,
                new Date(),
                new Date());
    }

    @Override
    public void updateUser(TabSubscriber user) {
        String sql = "update tab_subscriber mobile=?,password=? where id=?";
        jdbcTemplate.update(sql, user.getMobile(), user.getPassword(), user.getId());
    }

//    @Override
//    public void updateCustomer(TabCustomer tabCustomer) {
//        String sql = "update tab_customer set name=?,sex=?,certtype=?,certid=? where id=?";
//        jdbcTemplate.update(sql, tabCustomer.getName(), tabCustomer.getSex(), tabCustomer.getCertType(), tabCustomer.getCertid(), tabCustomer.getId());
//    }

    @Override
    public void deleteUser(String userid) {
        jdbcTemplate.update("delete from tab_subscriber where id=?", userid);
    }

    @Override
    public void deleteUserAndRole(String userId) {
        String sql = "delete from tab_user_role where sys_user_id=?";
        jdbcTemplate.update(sql, userId);
    }


}
