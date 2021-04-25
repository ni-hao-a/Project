package com.pc.business.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ConfigurationProperties(prefix="httpconfig")
@Component
public class httpConfig {

    private String noCheckUrl;
}
