package com.joe;

import com.alibaba.fastjson.JSON;
import com.joe.entity.Order;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@Log4j2
@SpringBootTest
public class RockmqTest {

    /**
     * 消息生产者
     * @throws MQClientException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQBrokerException
     */
    @Test
    public void producerTest() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //创建消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("myConsumer");
        //设置NameServer
        producer.setNamesrvAddr("192.168.0.43:9876");
        //启动生产者
        producer.start();
        //构建消息对象
        Order order = new Order(
                1,
                "张三",
                "123123",
                "软件园",
                new Date()
        );
        Message message = new Message("myTopic","myTag", JSON.toJSONString(order).getBytes());
        //发送消息
        SendResult result = producer.send(message, 6000);
        System.out.println(result);
        //关闭生产者
        producer.shutdown();
    }

}
