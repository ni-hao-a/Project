package com.pc.model.rlzy.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnlineReq {
    private int pageNum;
    private int pageSize;
    private String ipAddr;
    private String userName;
}
