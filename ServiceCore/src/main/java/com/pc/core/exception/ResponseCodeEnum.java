package com.pc.core.exception;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {

    /**
     * 系统类异常
     */
    SUCCESS(0, "成功结果"),
    FAILURE(1, "失败结果"),
    INFO_ERROR(1, "转换信息流错误"),
    SERVER_INTERNAL_ERROR(500, "系统异常，请联系管理员查看日志"),


    /**
     * 业务类异常
     */
    USERNAME_OR_PWD_IS_ERROR(100101, "用户名或密码错误"),
    VERIFICATION_CODE_IS_EMPTY(100102, "验证码为空"),
    VERIFICATION_CODE_IS_EXPIRED(100103, "验证码已过期"),
    VERIFICATION_CODE_IS_ERROR(100104, "验证码输入有误，请重新输入"),
    TOKEN_IS_EMPTY(100105, "用户凭证为空"),
    TOKEN_IS_EXPIRED(100106, "凭证已过期，请重新登录"),
    USER_VER_FAILURE(100107, "验证失败"),
    AUTH__FAILURE(100108, "验证失败"),
    USER_NAME_IS_EMPTY(100109, "用户名字为空"),
    ADD_CONFIG_FAILURE(100109, "新增参数配置失败"),
    UPDATE_CONFIG_FAILURE(100109, "修改参数配置失败"),
    ADD_DEPT_FAILURE(100109, "新增部门失败");

    private int code;
    private String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
