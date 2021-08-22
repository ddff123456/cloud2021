package com.ye.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//手动刷新配置文件的那一章节也用了这个配置文件
@RefreshScope //支持Nacos的配置动态刷新功能。(一旦配置本地的配置文件修改了之后，会自动刷新nacos上的信息)
public class ConfigClientController
{
    @Value("${config.info}")
    private String configInfo;  //从nacos服务中心中拿到对应配置文件中config.info的值

    @GetMapping("/config/info")
    public String getConfigInfo() {
        return configInfo;
    }
}
