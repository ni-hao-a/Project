package com.pc.model.rlzy.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DictDataReq {
    private Long dictCode;
    private Long[] dictCodes;
    private String dictType;
}
