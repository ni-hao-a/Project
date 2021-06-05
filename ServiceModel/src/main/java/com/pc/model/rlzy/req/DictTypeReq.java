package com.pc.model.rlzy.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DictTypeReq {
    private String dictData;
    private String dictType;
    private Long dictId;
    private Long[] dictIds;
}
