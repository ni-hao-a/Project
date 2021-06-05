package com.pc.model.rlzy.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeptReq {
    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 角色id
     */
    private Long roleId;
}
