package com.czkj.auth.service.impl;

import com.czkj.auth.dao.LoginDao;
import com.czkj.auth.entity.TabRole;
import com.czkj.auth.entity.TabSubscriber;
import com.czkj.exception.BusinessException;
import com.czkj.res.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangqh
 * @date 2020-03-31
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private LoginDao loginDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username:" + username);
        TabSubscriber user = loginDao.login(username);
        if (user != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            //获取角色信息
            if (user.getTabRoleList()!=null&&user.getTabRoleList().size()>0){
                for (TabRole tabRole : user.getTabRoleList()) {
                    authorities.add(new SimpleGrantedAuthority(tabRole.getName()));
                }
            }
            log.info("用户对应的角色=="+user.getTabRoleList());
            System.out.println("用户对应的角色=="+user.getTabRoleList());
            try {
                //登录成功，则修改用户登录状态
                loginDao.updateLoginStatus(username,true);
            } catch (Exception e) {
                log.error("修改登录转态时出错");
                throw new BusinessException("异常-_-");
            }
            return new User(user.getId(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException(username + " is not found");
        }
    }

}
