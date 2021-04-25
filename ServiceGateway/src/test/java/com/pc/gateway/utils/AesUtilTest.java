package com.pc.gateway.utils;

import com.pc.core.utils.encryutils.AesUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@SpringBootTest
@Component
public class AesUtilTest {

    private static String key = "Shayekis25487956";

    @Autowired
    private AesUtil aesUtil;

    @Test
    public void encrypt() {
        String context = "hello";
        System.out.println(aesUtil.encrypt(context, key));
    }

    @Test
    public void decrypt() {
        String context = "hello";
        System.out.println(aesUtil.decrypt(context, key));
    }
}