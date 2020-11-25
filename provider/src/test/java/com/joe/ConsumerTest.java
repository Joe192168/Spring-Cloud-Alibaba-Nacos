package com.joe;

import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

@Log4j2
public class ConsumerTest {

    /**
     * 消费者
     * @param args
     * @throws MQClientException
     */
    public static void main(String[] args) throws MQClientException {
        //创建消息消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("myConsumer");
        //设置NameServer
        consumer.setNamesrvAddr("192.168.0.43:9876");
        //指定订阅的主题和标签
        consumer.subscribe("myTopic","*");
        //回调函数
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                log.info("Message=>{}",list);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者
        consumer.start();
    }

}
