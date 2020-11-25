package com.joe.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.joe.entity.Order;
import com.joe.service.ProviderServer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ProviderController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${server.port}")
    private String port;

    @Autowired
    private ProviderServer providerServer;

    int i = 0;

    @GetMapping("/index")
    private String index(){
        i++;
        if (i%2==0) throw new RuntimeException();
        return "I am Provider,Port："+port;
    }

    @GetMapping("/list")
    private String list(){
        return "list";
    }

    @GetMapping("/test1")
    private String test1(){
        return providerServer.test("test1");
    }

    @GetMapping("/test2")
    private String test2(){
        return providerServer.test("test2");
    }

    @GetMapping("/hot")
    @SentinelResource("hot")
    public String hot(@RequestParam(value = "num1",required = false) Integer num1,
                      @RequestParam(value = "num2",required = false) Integer num2){
        return num1 + "-" + num2;
    }

    @GetMapping("/create")
    public Order create(){
        Order order = new Order(
                1,
                "张三",
                "123123",
                "软件园",
                new Date()
        );
        this.rocketMQTemplate.convertAndSend("myTopic",order);
        return order;
    }

    @GetMapping("/api1/demo1")
    public String demo1(){
        return "demo";
    }

    @GetMapping("/api1/demo2")
    public String demo2(){
        return "demo";
    }

    @GetMapping("/api2/demo1")
    public String demo3(){
        return "demo";
    }

    @GetMapping("/api2/demo2")
    public String demo4(){
        return "demo";
    }

}
