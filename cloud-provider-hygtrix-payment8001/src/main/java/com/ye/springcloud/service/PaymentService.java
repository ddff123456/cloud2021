package com.ye.springcloud.service;


import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
//用这个类模拟超时和正常的情况
public class PaymentService {


    /**
     服务降级
     */

    //正常访问肯定ok
    public String paymentInfo_OK(Integer id)
    {
        return "线程池:  "+Thread.currentThread().getName()+"  paymentInfo_OK,id:  "+id+"\t"+"O(∩_∩)O哈哈~";
    }

    //这个是模拟出错的情况
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandle" /*指定善后方法名*/,commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="3000")
    })   //出现超时超时错误之后调用回调函数（兜底函数）
    public String paymentInfo_TimeOut(Integer id)
    {

        /**
         * 我们发现两个异常都是用设置的兜底函数进行处理
         */
        int age = 10/0;    //设置一个计算异常
        int timenumber = 5;
        try {
            TimeUnit.MILLISECONDS.sleep(5000);  //设置一个超时异常
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池:  "+Thread.currentThread().getName()+"PaymentInfo_Timeout,id:  "+id+"\t"+"O(∩_∩)O哈哈~"+"  耗时(秒):"+timenumber;
    }

    //超时之后进行处理的函数（兜底函数）
    public String paymentInfo_TimeOutHandle(Integer id){
        return "线程池:  "+Thread.currentThread().getName()+"  8001系统繁忙或者运行报错，请稍后再试,id:  "+id+"\t"+"o(╥﹏╥)o";
    }

    /*============================================================================*/
    /**
     * 服务熔断
     *
     * 如果失败的次数占比大于限定的失败率的话，那么服务就会跳闸，就算这时候的命令是正确的依然不能正常访问，
     * 然后只有正确率慢慢上升，闸口才会慢慢开放（半开半和）
     *  然后才能正常访问。
     */


    /*
                                  断路器的三个重要参数
     1.快照时间窗：       断路器确定是否打开需要统计一些请求和错误数据，而统计的时间范围就是快照时间窗，默认为最近的10秒。

     2.请求总数阀值：     在快照时间窗内，必须满足请求总数阀值才有资格熔断。默认为20，意味着在10秒内，如果该hystrix命令的调用次数不足20次7,即使所有的请求都超时或其他原因失败，断路器都不会打开。

     3.错误百分比阀值：   当请求总数在快照时间窗内超过了阀值，比如发生了30次调用，如果在这30次调用中，有15次发生了超时异常，也就是超过50%的错误百分比，在默认设定50%阀值情况下，这时候就会将断路器打开。

    */
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), // 时间窗口期(经过多久后恢复一次尝试)
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),// 失败率达到多少后跳闸（百分比）
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {

        if(id < 0) {
            throw new RuntimeException("******id 不能负数");
        }
        //这个是Hutool包里面的方法
        String serialNumber = IdUtil.simpleUUID();   //相当于UUID.randomUUID().toString()

        return Thread.currentThread().getName()+"\t"+"调用成功，流水号: " + serialNumber;
    }
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能负数，请稍后再试，/(ㄒoㄒ)/~~   id: " +id;
    }

}
