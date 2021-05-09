package com.pc.model.rlzy.user;

import lombok.Data;

@Data
public class UserInfoBean {
    private String userId;
    private String userName;
    private String password;
    private String accountId;
    private String accountName;
    private String code; // 验证码
    private String uuid; // 临时码
    private String token; // 用户凭证
}
