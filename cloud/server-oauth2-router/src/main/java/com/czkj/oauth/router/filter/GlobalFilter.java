package com.czkj.oauth.router.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;

/**
 * @author zhangqh
 * @date 2020-03-19
 * 未启用
 */
public class GlobalFilter extends ZuulFilter {

    @Override
    /**
     * pre：路由之前
     * routing：路由之时
     * post： 路由之后
     * error：发送错误调用
     */
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException{
        return null;
    }
}
