package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// 开启Feign客服端功能
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AuthCrowdMainClass {

    public static void main(String[] args) {
        SpringApplication.run(AuthCrowdMainClass.class,args);
    }
}
