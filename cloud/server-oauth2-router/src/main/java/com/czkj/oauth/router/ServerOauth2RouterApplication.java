package com.czkj.oauth.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ServerOauth2RouterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerOauth2RouterApplication.class, args);
    }


}
