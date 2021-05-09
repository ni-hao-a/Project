package com.pc.model.rlzy.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReq {
    private int pageNum; // 当前页码
    private int pageSize; // 每页显示数量
    private String name; // 姓名
}
