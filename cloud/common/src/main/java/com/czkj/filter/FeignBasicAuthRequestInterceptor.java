
package com.czkj.filter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * feign调用转发请求头和请求参数
 *
 * @author zhangqh
 * @date 2020-03-21
 */
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                template.header(name, values);
            }
        }
/*        String loginName = (String)request.getSession().getAttribute("loginName");
        template.header("loginName", loginName);*/
        Enumeration<String> bodyNames = request.getParameterNames();
        StringBuffer body = new StringBuffer();
        if (bodyNames != null) {
            while (bodyNames.hasMoreElements()) {
                String name = bodyNames.nextElement();
                String values = request.getParameter(name);
//                body.append(name).append("=").append(values).append("&");
                //添加token转发
                if ("access_token".equals(name)) {
                    body.append(name).append("=").append(values);
                    break;
                }
            }
        }
        if (body.length() != 0) {
//            body.deleteCharAt(body.length() - 1);
            template.body(body.toString());
        }

    }
}

