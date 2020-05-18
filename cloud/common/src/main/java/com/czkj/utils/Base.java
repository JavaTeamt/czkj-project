package com.czkj.utils;

import com.auth0.jwt.interfaces.Claim;
import com.czkj.utils.JWTUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author SunMin
 * @description
 * @create 2020/4/16
 * @since 1.0.0
 */
public class Base {

    public static String getUserName(HttpServletRequest request){
        String token = request.getParameter("access_token");
        Map<String, Claim> map = null;
        try {
            if(StringUtils.isNotBlank(token)){
                map = JWTUtil.verifyToken(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String username = "";
        if(map != null && map.size()>0){
            username = map.get("user_name").asString();
        }
        return username;
    }
}
