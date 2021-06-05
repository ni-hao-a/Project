package com.pc.model.rlzy.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigReq {
    private String configKey; // 初始化配置标志
    private Long configId; // 配置id
    private Long[] ids; // 配置id集
}
