package com.pc.gateway.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
}
