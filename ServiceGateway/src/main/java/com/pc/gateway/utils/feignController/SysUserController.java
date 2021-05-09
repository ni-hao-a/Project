package com.pc.gateway.utils.feignController;

import com.pc.core.utils.ServletUtils;
import com.pc.gateway.utils.service.TokenService;
import com.pc.model.rlzy.entity.SysUser;
import com.pc.model.rlzy.login.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户信息
 *
 * @author qhl
 */
@RestController
@RequestMapping(value = "/system/user", produces = {"application/json;charset=UTF-8"})
public class SysUserController {
    @Autowired
    private TokenService service;

    /**
     * 获取当前登录用户
     *
     * @return ResponseBean
     */
    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    public SysUser getUser() {
        return service.getLoginUser(ServletUtils.getRequest()).getUser();
    }

    /**
     * 获取当前登录用户详细信息
     *
     * @return ResponseBean
     */
    @RequestMapping(value = "/getLoginUser", method = RequestMethod.POST)
    public LoginUser getLoginUser() {
        return service.getLoginUser(ServletUtils.getRequest());
    }
}
