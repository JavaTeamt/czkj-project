package com.czkj.serverweb.config;

import com.czkj.filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangqh
 * @date 2020-04-02
 */
@Configuration
public class ConfigurationFilter {

    @Bean
    public FilterRegistrationBean tokenFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new TokenFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("tokenFilter");
        registrationBean.setOrder(0);
        return registrationBean;
    }

}
