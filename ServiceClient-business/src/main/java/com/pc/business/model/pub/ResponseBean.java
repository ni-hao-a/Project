package com.pc.business.model.pub;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBean {
    private int code;
    private String message;
    private Object data;
}
