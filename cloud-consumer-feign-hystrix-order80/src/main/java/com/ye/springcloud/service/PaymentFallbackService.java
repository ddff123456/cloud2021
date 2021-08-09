package com.ye.springcloud.service;

import org.springframework.stereotype.Component;

@Component
//统一为接口里面的方法进行异常处理（例如对PaymentHystrixService使用，相当于为整个服务端的方法进行降级，实现了解耦）
public class PaymentFallbackService implements PaymentHystrixService
{
    @Override
    public String paymentInfo_OK(Integer id)
    {
        return "-----PaymentFallbackService fall back-paymentInfo_OK ,o(╥﹏╥)o";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id)
    {
        return "-----PaymentFallbackService fall back-paymentInfo_TimeOut ,o(╥﹏╥)o";
    }
}

