package com.pc.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author qhl
 */
@Component
@ConfigurationProperties(prefix = "system")
@Getter
@Setter
public class SystemConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 实例演示开关
     */
    private boolean demoEnabled;

    /**
     * 上传路径
     */
    private String profile;

    /**
     * 获取地址开关
     */
    private boolean addressEnabled;

    /**
     * 令牌密钥
     */
    private String secret;

    /**
     * 令牌有效期（默认30分钟）
     */
    private int expireTime;

    /**
     * 令牌自定义标识
     */
    private String header;

    /**
     * 匿名接口访问配置区分系统标识,默认所有ALL
     */
    private String anonymousRout = "ALL";

    /**
     * 验证码有效期（分钟）
     */
    private Integer captchaExpireTime = 10;

    /**
     * 获取头像上传路径
     */
    public String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public String getUploadPath() {
        return getProfile() + "/upload";
    }
}
