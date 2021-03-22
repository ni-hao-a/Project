package com.pc.dictionary.error;

import com.pc.dictionary.service.TestFeign;
import org.springframework.stereotype.Component;

@Component
public class SchedualServiceHiHystric implements TestFeign {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry "+name;
    }
}
