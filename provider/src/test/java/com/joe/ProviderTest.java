package com.joe;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class ProviderTest {

    /**
     * 限流 关联
     * @throws Exception
     */
    @Test
    public void linkTest() throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 100; i++) {
            restTemplate.getForObject("http://localhost:8081/list",String.class);
            Thread.sleep(200);
        }
    }

    /**
     * 限流 排队
     * @throws Exception
     */
    @Test
    public void waitTest() throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 100; i++) {
            restTemplate.getForObject("http://localhost:8081/index",String.class);
            Thread.sleep(500);
        }
    }

    /**
     * 降级
     * @throws Exception
     */
    @Test
    public void reduceTest() throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 100; i++) {
            restTemplate.getForObject("http://localhost:8081/index",String.class);
            Thread.sleep(10);
        }
    }

}
