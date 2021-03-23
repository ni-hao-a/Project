package com.pc.business.exception;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
    SUCCESS(0, "成功"),
    SERVER_INTERNAL_ERROR(500, "成功");

    private int code;
    private String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
