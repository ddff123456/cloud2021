package com.ye.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 这个是Config的服务端
 *                          自动刷新配置文件的原理
 * 使用RabbitMQ利用      crl -X POST "http://localhost:3344/actuator/bus-refresh"
 * 来调用spring-cloud-bus的bus-refresh接口
 * 实现服务端利用消息总线触发一个服务端ConfigServer的/bus/refresh端点，而刷新所有客户端的配置
 *
 *
 *
 *                          如果要实现定点通知
 * 我们这里以刷新运行在3355端口上的config-client（配置文件中设定的应用名称）为例，只通知3355，不通知3366
 * 在上面链接的基础上加上    “微服务名字：端口号”那么就可以实现顶点通知
 * curl -X POST "http://localhost:3344/actuator/bus-refresh/config-client:3355
 */

//访问 http://config-3344.com:3344/master/config-dev.yml 就能成功读取到这个配置文件的内容
//取master分支下的config-dev.yml目录下访问

@SpringBootApplication
@EnableConfigServer  //开启springcloud-config server服务的注解
public class ConfigCenterMain3344
{
    public static void main(String[] args) {
        SpringApplication.run(ConfigCenterMain3344.class, args);
    }
}

