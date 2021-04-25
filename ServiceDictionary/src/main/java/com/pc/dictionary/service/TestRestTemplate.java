package com.pc.dictionary.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 测试restTemplate+ribbon
 */
@Service
public class TestRestTemplate {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        return restTemplate.getForObject("http://service-gateway/hi?name=" + name, String.class);
    }

    /**
     * 断路器访问
     *
     * @param name
     * @return
     */
    public String hiError(String name) {
        return "hi," + name + ",sorry,error!";
    }
}
