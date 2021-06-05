package com.pc.business.utils.feignController;

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
public class RSysRoleController {
    @Autowired
    private ISysRoleService roleService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Set<String> list(@RequestBody Long role) {
        return roleService.selectRolePermissionByUserId(role);
    }
}
