package com.pc.gateway.utils.feignService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@FeignClient(name = "service-business", url = "http://localhost:7004")
@Component
public interface RSysRoleFeignService {

    @RequestMapping(value = "/system/role/list", method = RequestMethod.POST)
    Set<String> selectRolePermissionByUserId(@RequestBody Long userId);
}
