package com.pc.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBean {
    private int code;
    private String msg;
    private Object data;
}
