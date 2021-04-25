package com.pc.business.controller;

import com.pc.business.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 角色信息
 *
 * @author qhl
 */
@RestController
@RequestMapping(value = "/system/role", produces = {"application/json;charset=UTF-8"})
public class SysRoleController {
    @Autowired
    private ISysRoleService roleService;

    @GetMapping("/list")
    public Set<String> list(@RequestBody Long role) {
        return roleService.selectRolePermissionByUserId(role);
    }
}
