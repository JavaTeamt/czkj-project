package com.czkj;

import com.czkj.permission.dao.MenuDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: SunMin
 * @Description:
 * @Date:Create：in 2020/5/7 22:35
 * @Modified By：
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestService {

    @Autowired
    private MenuDao menuDao;
//
//    @Test
//    public void test(){
//        String name = "test";
//        String result = menuDao.savePermission(name);
//        System.out.println("结果=="+result);
//    }
}
