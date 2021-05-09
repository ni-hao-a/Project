package com.pc.model.rlzy.entity;

import lombok.Data;

/**
 * 角色和部门关联 r_sys_role_dept
 *
 * @author qhl
 */
@Data
public class SysRoleDept {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;
}
