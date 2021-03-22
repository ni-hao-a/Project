package com.pc.dictionary.controller;

import com.pc.dictionary.service.TestFeign;
import com.pc.dictionary.service.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api-center/rlzy/center")
public class HumanResourcesCenterController {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    TestFeign testFeign;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String sayHello(String name){
        return "Hello"+" "+name;
    }

    @RequestMapping(value = "/hi")
    public String hi(@RequestParam String name) {
        return testRestTemplate.hiService( name );
    }



    @GetMapping(value = "/feign")
    public String sayHi(@RequestParam String name) {
        return testFeign.sayHiFromClientOne( name );
    }
}
