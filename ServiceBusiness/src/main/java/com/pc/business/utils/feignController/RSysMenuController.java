package com.pc.business.utils.feignController;

import com.pc.business.service.ISysMenuService;
import com.pc.model.rlzy.entity.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 菜单信息
 *
 * @author qhl
 */
@RestController
@RequestMapping(value = "/system/menu", produces = {"application/json;charset=UTF-8"})
public class RSysMenuController {
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Set<String> list(@RequestBody Long userId) {
        return menuService.selectMenuPermsByUserId(userId);
    }

    /**
     * 获取菜单列表
     */
    @RequestMapping(value = "/getMenu", method = RequestMethod.POST)
    public List<SysMenu> getMenu(@RequestBody Long userId) {
        return menuService.selectMenuTreeByUserId(userId);
    }
}