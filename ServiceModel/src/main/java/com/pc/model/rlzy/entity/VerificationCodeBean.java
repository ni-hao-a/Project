package com.pc.model.rlzy.entity;

import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class VerificationCodeBean {
    private String code; // 验证码
    private BufferedImage image; // 图片
}
