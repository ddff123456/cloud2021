package com.ye.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 *                   两个或多个消费端可能会出现的问题
 * 如果是一个生产端发送消息，那么目前是8802/8803同时都收到了，存在重复消费问题
 * 例如单系统我们做集群部署，都会从RabbitMQ中获取订单信息，那如果一个订单同时被两个服务获取到，那么就会造成数据错误
 * （例如两个不同的部门派月饼，只想给其中一个部分派发月饼，但是现在两个部分都收到了月饼）
 *
 *
 *
 *                      这时我们就可以使用Stream中的消息分组来解决。
 * 注意在Stream中处于同一个group中的多个消费者是竞争关系，就能够保证消息只会被其中一个应用消费一次。不同组是可以全面消费的(重复消费)。
 *不 同的组是可以重复消费的，同一个组内会发生竞争关系，只有其中一个可以消费。
 *
 *
 *                          Stream之group解决消息重复消费
 *原理：     微服务应用放置于同一个group中，就能够保证消息只会被其中一个应用消费一次。
 *          不同的组是可以重复消费的，同一个组内会发生竞争关系，只有其中一个可以消费。
 *
 * 结论：
 * 同一个组的多个微服务实例，每次只会有一个拿到，不同的组是可以重复消费的
 * 8802/8803实现了轮询分组，每次只有一个消费者，8801模块的发的消息只能被8802或8803其中一个接收到，这样避免了重复消费。
 *
 *
 *                           Stream之消息持久化（配置了group就相当于自动做了持久化）
 * 例如如果8802没有配置分组，那么如果此时停掉8802，8801发送消息后再启动8802，我们发现8802并没有接收到之前8801发送的消息
 * 再看配置了group分组的消费端的情况，与8802做一样的操作，有分组属性配置，后台打出来了MQ上的消息。(消息持久化体现)
 *
 */


@Component   //相当于@Service
//@EnableBinding(Sink.class)是定于消费者的一个接收管道
@EnableBinding(Sink.class)  //相当于input消费者
public class ReceiveMessageListenerController
{
    @Value("${server.port}")
    private String serverPort;


    @StreamListener(Sink.INPUT)
    public void input(Message<String> message)
    {
        //打印出生产者发出的消息
        System.out.println("消费者2号,----->接受到的消息: "+message.getPayload()+"\t  port: "+serverPort);
    }
}
