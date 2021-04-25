package com.pc.gateway.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Data
public class VerificationCodeBean {
    private String code; // 验证码
    private BufferedImage image; // 图片
}
