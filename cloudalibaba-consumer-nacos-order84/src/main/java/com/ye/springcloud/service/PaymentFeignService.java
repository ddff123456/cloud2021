package com.ye.springcloud.service;


import com.ye.springcloud.entities.CommonResult;
import com.ye.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "nacos-payment-provider") //生产者的服务名(注意大写的会报错，可能是nacos的问题)
public interface PaymentFeignService {


    @GetMapping(value = "/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);
}
