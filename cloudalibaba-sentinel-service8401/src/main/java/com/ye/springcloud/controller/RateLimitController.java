package com.ye.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ye.springcloud.entities.CommonResult;
import com.ye.springcloud.entities.Payment;
import com.ye.springcloud.hander.CustomerBlockHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * SentinelResource配置(测试自定义的兜底方法)
 * 按资源名称限流 + 后续处理
 */
@RestController
public class RateLimitController {


    //按资源名称限流测试
    @GetMapping("/byResource")
    //定义自定义的兜底方法
    @SentinelResource(value = "byResource",blockHandler = "handleException")
    public CommonResult byResource() {
        return new CommonResult(200,"按资源名称限流测试OK",new Payment(2020L,"serial001"));
    }

    public CommonResult handleException(BlockException exception) {
        return new CommonResult(444,exception.getClass().getCanonicalName()+"\t 服务不可用");
    }



    //按url限流测试
    @GetMapping("/rateLimit/byUrl")
    @SentinelResource(value = "byUrl")  //使用系统默认自带的兜底方法
    public CommonResult byUrl()
    {
        return new CommonResult(200,"按url限流测试OK",new Payment(2020L,"serial002"));
    }



                /**为了防止每个api都要定义一个自己自定义的默认兜底方法 **/
    //对自定义限流逻辑进行解耦，定义一个CustomerBlockHandler类
    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,//<-------- 自定义限流处理类
            blockHandler = "handlerException2")//<-----------
    public CommonResult customerBlockHandler()
    {
        return new CommonResult(200,"按客戶自定义",new Payment(2020L,"serial003"));
    }
}
