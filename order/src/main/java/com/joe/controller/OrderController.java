package com.joe.controller;

import com.joe.entity.Order;
import com.joe.service.OrderServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/save")
    @GlobalTransactional
    public String save(){
        //调用order的保存方法
        Order order = new Order();
        order.setName("张三");
        orderService.save(order);
        int i = 1/0;
        //调用play的保存方法
        restTemplate.getForObject("http://localhost:8021/save",String.class);
        return "保存成功";
    }

}
