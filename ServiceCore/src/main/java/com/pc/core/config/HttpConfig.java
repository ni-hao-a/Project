package com.pc.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@Component
public class HttpConfig {
    private List<String> noCheckUrl; // 无需校验url
    private String key; // aes密钥
}
