package com.pc.model.rlzy.entity;


import lombok.Data;

/**
 * 用户和岗位关联 r_sys_user_post
 *
 * @author qhl
 */
@Data
public class SysUserPost {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;
}
