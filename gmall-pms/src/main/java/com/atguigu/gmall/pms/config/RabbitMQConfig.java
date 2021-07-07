package com.atguigu.gmall.pms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class RabbitMQConfig {


    @Autowired
    private RabbitTemplate  rabbitTemplate;

    @PostConstruct
    public void init(){
        //交换机消息确认
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack){
                log.error("消息为到达交换机：信息：{}",cause);
            }
        });
        //队列消息确认
        this.rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息未到达队列：返回码：{}, 返回值：{}, 交换机：{}, 路由键：{}, 返回信息：{}"
                    ,replyCode,replyText,exchange,routingKey,message);
        });
    }
}
