package com.ye.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * 通过代码注入bean实现getway的配置
 */
//GetWay配置路由的另外一种方式：代码中注入RouteLocator的Bean
//还有一种方式就是在yml里面配置
public class GateWayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder)
    {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

        //配置一个新的路由规则
        routes.route("path_route_ye",
                r -> r.path("/guonei")  //意思是访问localhost:9527/guonei将会转发到“http://news.baidu.com/guonei”地址
                        .uri("http://news.baidu.com/guonei")).build();

        return routes.build();
    }
}
