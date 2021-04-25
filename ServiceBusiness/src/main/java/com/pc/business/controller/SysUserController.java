package com.pc.business.controller;

import com.pc.business.model.system.SysUser;
import com.pc.business.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 用户信息
 *
 * @author qhl
 */
@RestController
@RequestMapping(value = "/system/user", produces = {"application/json;charset=UTF-8"})
public class SysUserController {
    @Autowired
    private ISysUserService userService;

    /**
     * 获取用户列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public SysUser list(@RequestBody String userName) {
        return userService.selectUserByUserName(userName);
    }
}
