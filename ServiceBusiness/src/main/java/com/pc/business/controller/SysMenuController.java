package com.pc.business.controller;

import com.pc.business.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 菜单信息
 *
 * @author qhl
 */
@RestController
@RequestMapping(value = "/system/menu", produces = {"application/json;charset=UTF-8"})
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Set<String> list(@RequestBody Long userId) {
        return menuService.selectMenuPermsByUserId(userId);
    }
}