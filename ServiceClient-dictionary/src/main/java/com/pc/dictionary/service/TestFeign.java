package com.pc.dictionary.service;


import com.pc.dictionary.error.SchedualServiceHiHystric;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 测试feign
 * 与ribbon相比区别在于省略了路径
 * ribbon：http://service-7001/hi?  并且需要restTemplate
 * feign：只需要配置feignClient连接到对应的服务即可
 */
@FeignClient(value = "service-gateway",fallback = SchedualServiceHiHystric.class)
public interface  TestFeign {
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}
