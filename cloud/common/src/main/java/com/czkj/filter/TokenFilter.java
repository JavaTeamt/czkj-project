package com.czkj.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.auth0.jwt.interfaces.Claim;
import com.czkj.res.Response;
import com.czkj.utils.JWTUtil;
import com.czkj.wrapper.ModifyHttpServletRequestWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Map;

/**
 * @author zhangqh
 * @date 2020-04-02
 */
public class TokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getParameter("access_token");
        Response baseResponse = null;
        if(StringUtils.isBlank(token)){
            baseResponse = Response.failure("-10","没有token");
        }else{
            Map<String, Claim> map = null;
            try {
                map = JWTUtil.verifyToken(token);
                ModifyHttpServletRequestWrapper requestWrapper = new ModifyHttpServletRequestWrapper(request);
                requestWrapper.putHeader("user-name", map.get("user_name").asString());
                filterChain.doFilter(requestWrapper, response);
            }catch (Exception e){
                baseResponse = Response.failure("-20","token 过期");
            }
        }
        if (baseResponse != null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(401);
            response.getWriter().write(JSONObject.toJSONString(baseResponse,SerializerFeature.WriteMapNullValue));
        }
    }
}
