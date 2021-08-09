package com.ye.springcloud.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration  //配置文件主要要加Configuration注解
public class ApplicationContextConfig {


    @Bean  //注入到容器里面
    /**
     * 使用自己手写的负载均衡策略
     */
    //@LoadBalanced //使用@LoadBalanced注解赋予RestTemplate负载均衡的能力（本质是负载均衡 + RestTemplate调用）
    //这样就实现了集群里面的服务交替使用
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
