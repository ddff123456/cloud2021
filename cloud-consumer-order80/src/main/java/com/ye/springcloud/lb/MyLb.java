package com.ye.springcloud.lb;



import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class MyLb implements LoadBalancer {

    //原子类
    private AtomicInteger atomicInteger = new AtomicInteger(0);  //用于更新当前访问次数，利用其原子性

    public final int getAndIncrement(){  //得到当前的服务数目并增加（自旋锁）
        int current;
        int next;
        do{
            current = this.atomicInteger.get();  //得到当前的值
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
                //compareAndSet中的第一个参数是期望值，第二个参数是新值
        }while(!this.atomicInteger.compareAndSet(current,next));  //如果期望值等于当前值(current)，则此方法将新值作为atomicInteger返回
        System.out.println("****第几次访问，次数next:" + next);
        return next;
    }


    //负载均衡算法：rest接口第几次请求数 % 服务器集群总数量 = 实际调用服务器位置下标  ，每次服务重启动后rest接口计数从1开始。
    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {

        int index = getAndIncrement() % serviceInstances.size();

        return serviceInstances.get(index);
    }
}
