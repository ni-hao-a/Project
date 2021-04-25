package com.pc.business.exception;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {

    /**
     * 系统类异常
     */
    SUCCESS(0, "成功结果"),
    FAILURE(1, "失败结果"),
    SERVER_INTERNAL_ERROR(500, "系统异常，请联系管理员查看日志"),


    /**
     * 业务类异常
     */
    USER_NAME_IS_EMPTY(100101, "用户名字为空");
    private int code;
    private String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
