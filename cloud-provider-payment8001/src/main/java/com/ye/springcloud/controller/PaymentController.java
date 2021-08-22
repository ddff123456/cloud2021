package com.ye.springcloud.controller;


import com.ye.springcloud.entities.CommonResult;
import com.ye.springcloud.entities.Payment;
import com.ye.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")  //读取配置文件里面的server.port
    private String serverPort;

    /**
     利用DiscoveryClient，
     将注册进eureka里面的微服务，可以通过服务发现来获得该服务的信息
    **/
    @Autowired
    private DiscoveryClient discoveryClient;


    //注意返回通用返回类
    @PostMapping("create")
    public CommonResult create(@RequestBody Payment payment){   //记得加上RequestBody才能传一个json
        int result = paymentService.create(payment);

        log.info("***** 插入结果" + result);

        if(result > 0){
            return new CommonResult(200,"插入数据成功",result);
        }else{
            return new CommonResult(404,"插入数据库失败",null);
        }

    }

    @GetMapping("get/{id}")
    public CommonResult getPaymentById(@PathVariable Long id){

        Payment payment = paymentService.getPaymentById(id);

        log.info("***** 插入结果" + payment);

        if(payment != null){
            return new CommonResult(200,"查询成功,serverPort"+serverPort,payment);
        }else{
            return new CommonResult(404,"没有对应记录，查询ID:"+id,null);

        }
    }


    /**
     * discoveryClient的作用：
     * 作用是只要调用这个接口，就可以通过这个接口就可以调用注册的实例了
     * 从而不用暴露微服务注册的实例的接口
     * */
    @GetMapping("discovery")
    public Object discovery(){
        //获得活动清单
        List<String> services = discoveryClient.getServices();

        for (String element : services) {
            log.info("*****element:"+element);
        }

        //获得相应微服务名称所对应的所有实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");

        for (ServiceInstance instance : instances) {
            //分别的到实例id，实例的主机名称，实例的端口号,实例的url
            log.info(instance.getServiceId() + "\t"+instance.getHost() + "\t" + instance.getPort()+"\t"+instance.getUri());

        }

        return discoveryClient;
    }


    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;//返回服务接口
    }

    //利用Feign进行超时控制
    @GetMapping("payment/feign/timeout")
    public String paymentFeignTimeout(){

        //暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return serverPort;
    }


    @GetMapping("/payment/zipkin")
    public String paymentZipkin() {
        return "hi ,i'am paymentzipkin server fall back，welcome to here, O(∩_∩)O哈哈~";
    }


}
