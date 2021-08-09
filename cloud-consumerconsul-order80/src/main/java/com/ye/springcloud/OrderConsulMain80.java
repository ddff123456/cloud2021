package com.ye.springcloud;

import com.ye.myrule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableDiscoveryClient //该注解用于向使用consul或者zookeeper作为注册中心时注册服务
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = MySelfRule.class)   //使用自己定义的负载均衡规则
public class OrderConsulMain80
{
    public static void main(String[] args) {
        SpringApplication.run(OrderConsulMain80.class, args);
    }
}

