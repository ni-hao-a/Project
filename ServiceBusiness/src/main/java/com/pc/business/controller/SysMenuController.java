package com.pc.business.controller;

import com.pc.business.service.ISysMenuService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.constants.Constants;
import com.pc.core.constants.UserConstants;
import com.pc.core.model.ResponseBean;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.StringUtils;
import com.pc.model.rlzy.entity.SysMenu;
import com.pc.model.rlzy.req.MenuReq;
import com.pc.model.rlzy.req.RoleReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author qhl
 */
@RestController
@RequestMapping(value = "/pc/base/business/v1/rlzy/menu", produces = {"application/json;charset=UTF-8"})
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private RSysUserFeignService userFeignService;

    /**
     * 获取菜单列表
     */
    @RequestMapping(value = "/getMenuList", method = RequestMethod.POST)
    public ResponseBean getMenuList(@RequestBody SysMenu menu) {
        Long userId = userFeignService.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return ResponseUtil.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @RequestMapping(value = "/getMenuById", method = RequestMethod.POST)
    public ResponseBean getMenuById(@RequestBody MenuReq req) {
        return ResponseUtil.success(menuService.selectMenuById(req.getMenuId()));
    }

    /**
     * 获取菜单下拉树列表
     */
    @RequestMapping(value = "/getMenuTree", method = RequestMethod.POST)
    public ResponseBean getMenuTree(@RequestBody SysMenu req) {
        Long userId = userFeignService.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(req, userId);
        return ResponseUtil.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @RequestMapping(value = "/getMenuTreeByRoleId", method = RequestMethod.POST)
    public ResponseBean getMenuTreeByRoleId(@RequestBody RoleReq req) {
        List<SysMenu> menus = menuService.selectMenuList(userFeignService.getUser().getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("checkedKeys", menuService.selectMenuListByRoleId(req.getRoleId()));
        map.put("menus", menuService.buildMenuTreeSelect(menus));
        return ResponseUtil.success(map);
    }

    /**
     * 新增菜单
     */
    @RequestMapping(value = "/addMenu", method = RequestMethod.POST)
    public ResponseBean addMenu(@Valid @RequestBody SysMenu req) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(req))) {
            return ResponseUtil.error(2001, "新增菜单'" + req.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(req.getIsFrame())
                && !StringUtils.startsWithAny(req.getPath(), Constants.HTTP, Constants.HTTPS)) {
            return ResponseUtil.error(2002, "新增菜单'" + req.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        req.setCreateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(menuService.insertMenu(req));
    }

    /**
     * 修改菜单
     */
    @RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
    public ResponseBean updateMenu(@Valid @RequestBody SysMenu req) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(req))) {
            return ResponseUtil.error(2003, "修改菜单'" + req.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(req.getIsFrame())
                && !StringUtils.startsWithAny(req.getPath(), Constants.HTTP, Constants.HTTPS)) {
            return ResponseUtil.error(2004, "修改菜单'" + req.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (req.getMenuId().equals(req.getParentId())) {
            return ResponseUtil.error(2005, "修改菜单'" + req.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        req.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(menuService.updateMenu(req));
    }

    /**
     * 删除菜单
     */
    @RequestMapping(value = "/delMenu", method = RequestMethod.POST)
    public ResponseBean delMenu(@Valid @RequestBody MenuReq req) {
        if (menuService.hasChildByMenuId(req.getMenuId())) {
            return ResponseUtil.error(2006, "存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(req.getMenuId())) {
            return ResponseUtil.error(2007, "菜单已分配,不允许删除");
        }
        return ResponseUtil.success(menuService.deleteMenuById(req.getMenuId()));
    }
}