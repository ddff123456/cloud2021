package com.ye.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface LoadBalancer {

    //获得所有的服务实例
    ServiceInstance instances(List<ServiceInstance> serviceInstances);
}
