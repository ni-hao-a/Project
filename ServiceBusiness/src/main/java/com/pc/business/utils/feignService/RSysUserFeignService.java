package com.pc.business.utils.feignService;

import com.pc.core.config.FeignConfig;
import com.pc.model.rlzy.entity.SysUser;
import com.pc.model.rlzy.login.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "service-gateway", url = "http://localhost:7001", configuration = {FeignConfig.class})
@Component
public interface RSysUserFeignService {

    @RequestMapping(value = "/system/user/getUser", method = RequestMethod.POST)
    SysUser getUser();

    @RequestMapping(value = "/system/user/getLoginUser", method = RequestMethod.POST)
    LoginUser getLoginUser();

    @RequestMapping(value = "/system/user/setLoginUser", method = RequestMethod.POST)
    void setLoginUser(LoginUser req);
}
