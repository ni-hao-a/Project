package com.pc.gateway.utils.feignService;

import com.pc.core.config.FeignConfig;
import com.pc.model.rlzy.entity.SysMenu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Set;

@FeignClient(name = "service-business", url = "http://localhost:7004", configuration = FeignConfig.class)
@Component
public interface RSysMenuFeignService {

    @RequestMapping(value = "/system/menu/list", method = RequestMethod.POST)
    Set<String> selectMenuPermsByUserId(@RequestBody Long userId);

    @RequestMapping(value = "/system/menu/getMenu", method = RequestMethod.POST)
    List<SysMenu> selectMenuTreeByUserId(@RequestBody Long userId);
}
