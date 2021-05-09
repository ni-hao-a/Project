package com.pc.model.rlzy.entity;

import lombok.Data;

/**
 * 用户和角色关联 r_sys_user_role
 *
 * @author qhl
 */
@Data
public class SysUserRole {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

}
