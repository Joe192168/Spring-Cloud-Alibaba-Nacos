package com.joe.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@Slf4j
public class ConsumerContoller {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/instance")
    public List<ServiceInstance> instanceList(){
        return discoveryClient.getInstances("provider-server");
    }

    @GetMapping("/index")
    public String index(){
        //1、找到实例
        List<ServiceInstance> list = discoveryClient.getInstances("provider-server");
        //产生一个随机数
        int i = ThreadLocalRandom.current().nextInt(list.size());
        ServiceInstance serviceInstance = list.get(i);
        String uri = serviceInstance.getUri()+"/index";
        log.info("调用的端口为：{}",serviceInstance.getPort());
        //2、调用
        return "调用端口为"+serviceInstance.getPort()+"的服务，返回结果是："+restTemplate.getForObject(uri,String.class);
    }

    @GetMapping("/uriRibbon")
    public String uriRibbon(){
        return restTemplate.getForObject("http://provider-server/index",String.class);
    }

}
