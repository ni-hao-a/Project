package com.pc.business.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@ConfigurationProperties(prefix="httpconfig")
@Component
public class HttpConfig {
    private List<String> noCheckUrl; // 无需校验url
    private String key; // aes密钥
}
