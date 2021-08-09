package com.ye.springcloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Slf4j
/**
 * 这个是自定义过滤GlobalFilter
 */
public class MyLogGateWayFilter implements GlobalFilter, Ordered
{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        log.info("***********come in MyLogGateWayFilter:  "+new Date());

        //使用网关访问(localhost:9527)要求带着uname这个请求参数,不然就会被过滤掉，访问失败
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");

        //不合法，
        if(uname == null)
        {
            log.info("*******用户名为null，非法用户，o(╥﹏╥)o");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);  //设置不被接收的状态码
            return exchange.getResponse().setComplete();  //返回错误方法
        }

        //否则合法用户去下一个过滤链进行下一项过滤
        return chain.filter(exchange);
    }

    @Override
    public int getOrder()
    {
        return 0;
    }
}
