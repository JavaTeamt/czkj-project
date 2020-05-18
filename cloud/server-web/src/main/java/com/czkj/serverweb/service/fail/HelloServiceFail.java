package com.czkj.serverweb.service.fail;

import com.czkj.serverweb.service.HelloService;
import org.springframework.stereotype.Component;

/**
 * @author zhangqh
 * @date 2020-03-18
 */

@Component
public class HelloServiceFail implements HelloService {
    @Override
    public String sayHiFromClientOne() {
        return "网络繁忙，请稍后再试-_-。PS：服务消费者自己提供的信息";
    }
}
