package com.pc.model.rlzy.login;

import lombok.Data;

/**
 * 登录用户身份权限
 *
 * @author qhl
 */
@Data
public class UserToken {
    private static final long serialVersionUID = 1L;
    /**
     * 用户唯一标识
     */
    private String token;
}
