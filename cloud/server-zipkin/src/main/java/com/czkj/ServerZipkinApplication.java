package com.czkj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin2.server.internal.EnableZipkinServer;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZipkinServer //表示Zipkin是服务器
public class ServerZipkinApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServerZipkinApplication.class,args);
    }
}
