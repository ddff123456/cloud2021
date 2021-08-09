package com.ye.springcloud.service;


import com.ye.springcloud.entities.CommonResult;
import com.ye.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {


    //Feign的用法就是相当于封装了restTemplate
    @GetMapping(value = "get/{id}")   //这个是调用了服务端get/{id}这个url对应的api
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

    //利用Feign进行超时控制
    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout();

}
