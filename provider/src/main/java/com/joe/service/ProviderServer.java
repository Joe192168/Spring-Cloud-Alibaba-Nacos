package com.joe.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

@Service
public class ProviderServer {

    //对server中的test资源进行保护，进行链路设置进行限流
    @SentinelResource("test")
    public String test(String str){
        return str;
    }

}
