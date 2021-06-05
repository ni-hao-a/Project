package com.pc.business.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.service.ISysRoleService;
import com.pc.business.service.ISysUserService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.business.utils.service.SysPermissionService;
import com.pc.core.constants.UserConstants;
import com.pc.core.model.ResponseBean;
import com.pc.core.poi.ExcelUtil;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.StringUtils;
import com.pc.model.rlzy.entity.SysRole;
import com.pc.model.rlzy.login.LoginUser;
import com.pc.model.rlzy.req.RoleReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/pc/base/business/v1/rlzy/role")
public class SysRoleController {
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private RSysUserFeignService userFeignService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private ISysUserService userService;

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/getRoleList", method = RequestMethod.POST)
    public ResponseBean getRoleList(@RequestBody SysRole req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysRole> list = roleService.selectRoleList(req);
        return ResponseUtil.success(new PageInfo<>(list));
    }

    /**
     * 根据角色编号获取详细信息
     */
    @RequestMapping(value = "/getRoleInfoById", method = RequestMethod.POST)
    public ResponseBean getRoleInfoById(@RequestBody RoleReq req) {
        return ResponseUtil.success(roleService.selectRoleById(req.getRoleId()));
    }

    /**
     * 新增角色
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    public ResponseBean addRole(@Valid @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return ResponseUtil.error(4001, "新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return ResponseUtil.error(4002, "新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(roleService.insertRole(role));

    }

    /**
     * 更新角色
     */
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    public ResponseBean updateRole(@Valid @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return ResponseUtil.error(4003, "修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return ResponseUtil.error(4004, "修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(userFeignService.getUser().getUserName());

        if (roleService.updateRole(role) > 0) {
            // 更新缓存用户权限
            LoginUser loginUser = userFeignService.getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin()) {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                //tokenService.setLoginUser(loginUser); // 更新用户权限信息
            }
            return ResponseUtil.success("更新成功");
        }
        return ResponseUtil.error(4005, "修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
    @RequestMapping(value = "/dataScope", method = RequestMethod.POST)
    public ResponseBean dataScope(@Valid @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        return ResponseUtil.success(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public ResponseBean updateStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/delRole", method = RequestMethod.POST)
    public ResponseBean delRole(@RequestBody RoleReq req) {
        return ResponseUtil.success(roleService.deleteRoleByIds(req.getRoleIds()));
    }

    /**
     * 获取角色选择框列表
     */
    @RequestMapping(value = "/getOptionSelect", method = RequestMethod.POST)
    public ResponseBean getOptionSelect() {
        return ResponseUtil.success(roleService.selectRoleAll());
    }

    /**
     * 导出角色列表
     */
    @RequestMapping(value = "/exportRole", method = RequestMethod.POST)
    public ResponseBean exportRole(@RequestBody SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }
}
