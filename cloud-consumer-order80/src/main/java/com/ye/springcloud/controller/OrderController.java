package com.ye.springcloud.controller;


import com.ye.springcloud.entities.CommonResult;

import com.ye.springcloud.entities.Payment;
import com.ye.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {
    //cloud-consumer-order80订单服务访问地址不能写死
    //public static final String  PAYMENT_URL =  "http://localhost:8001";
    //这里写eurrka服务中心注册的名字，使用的端口号是用 “逮到谁就是谁” 的原则
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancer loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/consumer/payment/lb")
    public String getPaymentLB()
    {
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");

        if(instances == null || instances.size() <= 0){
            return null;
        }

        ServiceInstance serviceInstance = loadBalancer.instances(instances);
        URI uri = serviceInstance.getUri();

        return restTemplate.getForObject(uri+"/payment/lb",String.class);

    }



    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){

        //后面两个参数分别是服务端需要的参数和服务端返回的值
        return restTemplate.postForObject(PAYMENT_URL + "/create",payment,CommonResult.class);
    }

    @GetMapping("consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){

        //注意路径前面不能漏了斜杠                        这里服务端不需要参数，那么只用写一个返回值就可以了
        return restTemplate.getForObject(PAYMENT_URL+"/get/"+id,CommonResult.class);
    }


    @GetMapping("consumer/payment/getForEntity/{id}")
    public CommonResult<Payment> getPayment2(@PathVariable("id") Long id){

        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL+"/get/"+id,CommonResult.class);

        if(entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }else{
            return new CommonResult<>(444,"操作失败");
        }
    }

    // ====================> zipkin+sleuth,实现Sleuth链路监控展现
    @GetMapping("/consumer/payment/zipkin")
    public String paymentZipkin()
    {
        String result = restTemplate.getForObject("http://localhost:8001"+"/payment/zipkin/", String.class);
        return result;
    }

}

