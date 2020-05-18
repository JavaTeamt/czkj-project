package com.czkj.serverweb.config;

import com.czkj.exception.ExceptionHandleAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhangqh
 * @date 2020-04-02
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public ExceptionHandleAdvice exceptionHandleAdvice(){
        return new ExceptionHandleAdvice();
    }
}
