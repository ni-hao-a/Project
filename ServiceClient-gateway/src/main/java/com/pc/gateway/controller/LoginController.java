package com.pc.gateway.controller;

import com.pc.gateway.bean.Result;
import com.pc.gateway.bean.UserInfoBean;
import com.pc.gateway.mapper.LoginMapper;
import com.pc.gateway.utils.ResultUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(value = "/pc/api-gateway/gateway/v1/rlzy", produces = "application/json")
public class LoginController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoginMapper loginMapper;

    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
    public String checkLogin() {
        String token = request.getHeader("token");
        return "hello,welcome" + token;
    }

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@Valid @RequestBody UserInfoBean req) {
        int user = loginMapper.getUserCount(req.getUserName(), req.getPassword());
        if (user <= 0) {
            return ResultUtil.error(500, "用户名或密码错误");
        }
        UserInfoBean userInfo = loginMapper.getUserInfo(req.getUserName());
        return ResultUtil.success(userInfo);
    }
}
