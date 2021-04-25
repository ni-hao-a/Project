package com.pc.gateway.model.route;

import lombok.Data;

@Data
public class SysAnonRoute {
    // 标识id
    private String sysId;
    // 路由地址
    private String sysPath;
    // 请求方式
    private String method;
    // 状态：0-生效，1-失效
    private String status;
    // 备注
    private String remark;
}
