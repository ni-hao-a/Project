package com.pc.business.utils;

import java.security.SecureRandom;
import java.util.Random;

public class SecureRandomUtils {

    /**
     * 生成32位随机数
     *
     * @return 32位的随机数
     */
    public static String get32BitRandom() {
        Long time = System.currentTimeMillis();
        Random random = new SecureRandom();
        Integer number = random.nextInt(900000) + 100000;
        return time + String.valueOf(number);
    }
}
