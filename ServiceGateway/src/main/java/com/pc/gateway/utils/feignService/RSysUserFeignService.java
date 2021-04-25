package com.pc.gateway.utils.feignService;

import com.pc.gateway.bean.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@FeignClient(name = "service-business", url = "http://localhost:7004")
@Component
public interface RSysUserFeignService {

    @RequestMapping(value = "/system/user/list", method = RequestMethod.POST)
    SysUser selectUserByUserName(String userName);
}
