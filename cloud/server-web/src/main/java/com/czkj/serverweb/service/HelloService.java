package com.czkj.serverweb.service;

import com.czkj.serverweb.service.fail.HelloServiceFail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhangqh
 * @date 2020-03-18
 */
@FeignClient(value = "server-cas",fallback = HelloServiceFail.class)
public interface HelloService {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    String sayHiFromClientOne();
}
