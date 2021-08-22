package com.ye.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FlowLimitController {
    @GetMapping("/testA")
    public String testA()
    {
        //测试以线程数作为过滤对象
        try{
            TimeUnit.MILLISECONDS.sleep(800);//毫秒
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        return "------testA";
    }

    @GetMapping("/testB")
    public String testB()
    {
        log.info(Thread.currentThread().getName()+"\t"+"...testB");
        return "------testB";
    }

    //当访问超过5次后，就会出发降级，不会出现error的页面，但是一旦少于5次就会出现报错页面
    @GetMapping("/testD")
    public String testD() {
        log.info("testD 异常比例");
        int age = 10/0;
        return "------testD";
    }


    //测试异常数
    //访问 http://localhost:8401/testE，
    // 第一次访问绝对报错，因为除数不能为零，我们看到error窗口，但是达到5次报错后，进入熔断后降级。
    @GetMapping("/testE")
    public String testE()
    {
        log.info("testE 测试异常数");
        int age = 10/0;
        return "------testE 测试异常数";
    }


    /**
     * 对特定的热点进行限流和监控，并且自定义默认的兜底方法
     * 限流出问题后，都是用sentinel系统默认的提示: Blocked by Sentinel (flow limiting)
     *
     * 我们能不能自定？类似hystrix，某个方法出问题了，就找对应的兜底降级方法?
     *
     * 结论 - 从HystrixCommand到@SentinelResource
     *
     */


    /**                             根据传入的热点进行热点限流                    **/

    //对p1和p2两个热点参数进行自定义的规则

    @GetMapping("/testHotKey")
    //这个SentinelResource注解就是定义我们的自定义的兜底方法的注解
    @SentinelResource(value = "testHotKey",blockHandler/*兜底方法*/ = "deal_testHotKey")
    //注意前台传进来的热点参数需要blockHandler里面的兜底方法的参数要一致才能采用这个兜底方法
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2) {

        //int age = 10/0; 这个程序的异常不归兜底函数管，只管sentinel控制台的错
        return "------testHotKey";
    }

    /*兜底方法*/
    public String deal_testHotKey (String p1, String p2, BlockException exception) {
        //sentinel系统默认的提示：Blocked by Sentinel (flow limiting)
        return "------deal_testHotKey,o(╥﹏╥)o";  //自定义的提示语句
    }



}

