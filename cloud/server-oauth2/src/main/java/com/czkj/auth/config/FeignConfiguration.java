package com.czkj.auth.config;

import com.czkj.filter.FeignBasicAuthRequestInterceptor;
import com.czkj.filter.FeignHystrixConcurrencyStrategyIntellif;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangqh
 * @date 2020-04-13
 */
@Configuration
public class FeignConfiguration {

    @Bean
    public FeignBasicAuthRequestInterceptor requestInterceptor(){
        return new FeignBasicAuthRequestInterceptor();
    }

    @Bean
    public FeignHystrixConcurrencyStrategyIntellif hystrixConcurrencyStrategy(){
        return new FeignHystrixConcurrencyStrategyIntellif();
    }
}
