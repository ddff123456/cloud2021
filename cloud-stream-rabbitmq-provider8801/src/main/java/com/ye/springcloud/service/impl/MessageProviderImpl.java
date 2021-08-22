package com.ye.springcloud.service.impl;

import com.ye.springcloud.service.IMessageProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import javax.annotation.Resource;
import java.util.UUID;




//@EnableBinding(Source.class)是定义消息生产者的一个发送管道
@EnableBinding(Source.class) //定义消息的推送管道，相当于output生产者
//这个service是跟RabbitMQ打交道的,不是与controller打交道了,所以不用添加@Service注解
public class MessageProviderImpl implements IMessageProvider
{
    @Resource
    private MessageChannel output; // 消息发送管道

    @Override
    public String send()
    {
        String serial = UUID.randomUUID().toString();
        //用output这个管道往这个消息中间件发送消息流水号
        output.send(MessageBuilder.withPayload(serial).build());
        System.out.println("*****serial: "+serial);
        return null;
    }
}
