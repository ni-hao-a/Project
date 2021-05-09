package com.pc.business.controller;

import com.pc.business.service.ISysMenuService;
import com.pc.business.service.ISysUserService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.model.ResponseBean;
import com.pc.core.utils.ResponseUtil;
import com.pc.model.rlzy.entity.SysMenu;
import com.pc.model.rlzy.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 用户信息
 *
 * @author qhl
 */
@RestController
@RequestMapping(value = "/pc/base/business/v1/rlzy/user", produces = {"application/json;charset=UTF-8"})
public class SysUserController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private RSysUserFeignService userFeignService;
    @Autowired
    private ISysMenuService menuService;


    /**
     * 获取路由
     *
     * @return ResponseBean
     */
    @RequestMapping(value = "/getRoute", method = RequestMethod.POST)
    public ResponseBean getRoute() {
        SysUser user = userFeignService.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return ResponseUtil.success(menuService.buildMenus(menus));
    }
}
