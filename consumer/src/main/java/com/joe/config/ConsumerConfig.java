package com.joe.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConsumerConfig {

    @Bean
    @LoadBalanced //负载均衡 使用ribbon,默认进行轮询访问，还有随机，权重的
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
