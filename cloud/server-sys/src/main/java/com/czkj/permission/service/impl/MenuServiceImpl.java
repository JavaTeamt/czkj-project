package com.czkj.permission.service.impl;

import com.czkj.common.entity.*;
import com.czkj.permission.dao.MenuDao;
import com.czkj.permission.service.MenuService;
import com.czkj.utils.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Author: SunMin
 * @Description:实现
 * @Date:Create：in 2020/4/22 11:39
 * @Modified By：
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public PageResult<TabPermission> getAllList(String available,String name,Integer currentPage, Integer size) {
        return menuDao.queryAllList(available,name,currentPage,size);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePermission(TabPermission tabPermission) {
        List<TabPermissionUrl> list = new ArrayList();
        try {
            //新增权限，返回主键id
            String key = menuDao.savePermission(tabPermission.getName(),tabPermission.getRemark());
            //获取url数据，添加权限对应url
            if (null!= tabPermission.getUrlList() && tabPermission.getUrlList().size()> 0) {
                //数组去重
                for (int i = 0; i < tabPermission.getUrlList().size(); i++) {
                    if (!list.contains(tabPermission.getUrlList().get(i))){
                        list.add(tabPermission.getUrlList().get(i));
                    }
                }
                for (TabPermissionUrl tabPermissionUrl : list) {
                    tabPermissionUrl.setPerId(key);
                    menuDao.savePerUrl(tabPermissionUrl);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    public TabPermission getPermissionByKey(String key) {
        return menuDao.queryPermission(key);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePerByKey(TabPermission tabPermission) {
        List<TabPermissionUrl> list = new ArrayList();
        try {
            //修改权限数据
            menuDao.updatePermission(tabPermission.getName(),tabPermission.getRemark(),tabPermission.getId());
            //先删除原先url记录,重新添加
            menuDao.deletePerUrlByPerId(tabPermission.getId());
            //获取url数据，添加权限对应url
            if (null!= tabPermission.getUrlList() && tabPermission.getUrlList().size()> 0) {
                //数组去重
                for (int i = 0; i < tabPermission.getUrlList().size(); i++) {
                    if (!list.contains(tabPermission.getUrlList().get(i))) {
                        list.add(tabPermission.getUrlList().get(i));
                    }
                }
                for (TabPermissionUrl tabPermissionUrl : list) {
                    tabPermissionUrl.setPerId(tabPermission.getId());
                    tabPermission.setLastUpdateTime(new Date());
                    menuDao.savePerUrl(tabPermissionUrl);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    public boolean queryExit(String name, String url,String keyId) {
        if (StringUtils.isNotBlank(name)) {
            TabPermission tabPermission = menuDao.queryPerByName(name,keyId);
            if (null != tabPermission) {
                return false;
            }
        }
        if (StringUtils.isNotBlank(url)) {
            TabPermissionUrl tabPermissionUrl = getPerUrlByUrl(url,keyId);
            if (null != tabPermissionUrl) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTabpermission(String key) {
        //不进行真正的删除，修改标识为0表示废弃
        try {
            menuDao.updatePermissionForAvlia("0", key);
            menuDao.updatePerUrlAvailable("0", key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enablePermission(String key) {
        try {
            menuDao.updatePermissionForAvlia("1", key);
            menuDao.updatePerUrlAvailable("1", key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    public  List<TabRole> getRoleList(String pid){
        return menuDao.queryRoleList(pid);
    }

    @Override
    public TabPermissionUrl getPerUrlByUrl(String url, String perId) {
        return menuDao.queryPerUrlByUrl(url,perId);
    }
}
