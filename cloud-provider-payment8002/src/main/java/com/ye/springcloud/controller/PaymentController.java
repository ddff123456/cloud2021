package com.ye.springcloud.controller;


import com.ye.springcloud.entities.CommonResult;
import com.ye.springcloud.entities.Payment;
import com.ye.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")  //读取配置文件里面的server.port
    private String serverPort;

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

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;//返回服务接口
    }

}
