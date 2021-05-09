package com.pc.gateway.utils.feignService;

import com.pc.core.config.FeignConfig;
import com.pc.model.rlzy.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "service-business", url = "http://localhost:7004", configuration = FeignConfig.class)
@Component
public interface RSysUserFeignService {

    @RequestMapping(value = "/system/user/list", method = RequestMethod.POST)
    SysUser selectUserByUserName(String userName);
}
