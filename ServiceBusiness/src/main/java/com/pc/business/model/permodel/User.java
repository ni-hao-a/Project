package com.pc.business.model.permodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String empCode; // 员工编号
    private String empName; // 员工姓名
    private String orgId; // 组织编号
    private String orgsectorId; // 部门编号
    private String position; // 职务
    private String post; // 岗位
    private String IDCard; // 身份证
    private String birth; // 出生日期
    private String age; // 年龄
    private String phone; // 联系方式
}
