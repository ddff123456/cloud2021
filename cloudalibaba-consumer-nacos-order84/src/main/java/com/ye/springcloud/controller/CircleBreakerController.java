package com.ye.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ye.springcloud.entities.CommonResult;
import com.ye.springcloud.entities.Payment;
import com.ye.springcloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class CircleBreakerController {

    /**
     *      openfeign的自动熔断
     * 测试84调用9003，此时故意关闭9003微服务提供者，84消费侧自动降级，不会被耗死。
     * **/
    @Autowired
    private PaymentFeignService paymentFeignService;


    /**
     * nacos已经集成了ribbon，实现了负载均衡了.
     * */
    @RequestMapping("/consumer/fallback/{id}")
    //@SentinelResource(value = "fallback")//没有配置
    //设置自定义兜底方法handlerFallback
    //@SentinelResource(value = "fallback", fallback = "handlerFallback") //fallback只负责业务异常
    //@SentinelResource(value = "fallback",blockHandler = "blockHandler") //blockHandler只负责sentinel控制台配置违规
    /**如果fallback和blockhandler同时触发，那么blockhandler的优先级比较高  **/
    @SentinelResource(value = "fallback",fallback = "handlerFallback",blockHandler = "blockHandler")  //同时配置fallback和blockhandler
    public CommonResult<Payment> fallback(@PathVariable Long id)
    {
        CommonResult<Payment> result = paymentFeignService.paymentSQL(id);

        //完善报错信息，给用户更加友好的提示
        if (id == 4) {
            throw new IllegalArgumentException ("IllegalArgumentException,非法参数异常....");
        }else if (result.getData() == null) {
            throw new NullPointerException ("NullPointerException,该ID没有对应记录,空指针异常");
        }

        return result;
    }


    //本例是fallback(只负责业务异常)
    public CommonResult handlerFallback(@PathVariable  Long id,Throwable e) {
        Payment payment = new Payment(id,"null");
        //通过e.getMessage() 将自己抛出的异常提示展示出来
        return new CommonResult<>(444,"兜底异常handlerFallback,exception内容  "+e.getMessage(),payment);
    }

    //本例是blockHandler(只针对sentinel控制台的配置违规)
    public CommonResult blockHandler(@PathVariable  Long id, BlockException blockException) {
        Payment payment = new Payment(id,"null");
        return new CommonResult<>(445,"blockHandler-sentinel限流,无此流水: blockException  "+blockException.getMessage(),payment);
    }


}