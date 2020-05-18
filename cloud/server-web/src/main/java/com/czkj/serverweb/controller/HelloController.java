package com.czkj.serverweb.controller;

import com.auth0.jwt.interfaces.Claim;
import com.czkj.serverweb.service.HelloService;
import com.czkj.utils.JWTUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zhangqh
 * @date 2020-03-18
 */
@RestController
public class HelloController {
    @Resource
    HelloService service;

    @GetMapping(value = "/hello")
    public String hello() {
        return service.sayHiFromClientOne();
    }

    @GetMapping(value = "/hi")
    public String hi(HttpServletRequest request) throws Exception{
        String token = request.getParameter("access_token");
        Map<String, Claim> map = null;
        if(StringUtils.isNotBlank(token)){
            map = JWTUtil.verifyToken(token);
        }
        String username = "";
        if(map != null && map.size()>0){
            username = map.get("user_name").asString();
        }
        return "hello " + username + " ,i am from webapp";
    }
}
