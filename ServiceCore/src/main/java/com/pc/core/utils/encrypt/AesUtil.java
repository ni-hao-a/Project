package com.pc.core.utils.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
public class AesUtil {

    // 偏移量
    private static final int OFF_SET = 16;
    // 加密器类型:加密算法为AES,加密模式为CBC,补码方式为PKCS5Padding
    private static final String PADDING = "AES/CBC/PKCS5Padding";
    // 算法类型：用于指定生成AES的密钥
    private static final String AES = "AES";


    /**
     * AES加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return 加密后内容
     */
    public static String encrypt(String content, String key) {
        try {
            //构造密钥
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), AES);
            //创建初始向量iv用于指定密钥偏移量(可自行指定但必须为128位)，因为AES是分组加密，下一组的iv就用上一组加密的密文来充当
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, OFF_SET);
            //创建AES加密器
            Cipher cipher = Cipher.getInstance(PADDING);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            //使用加密器的加密模式
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv);
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //使用BASE64对加密后的二进制数组进行编码
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return null;
    }

    /**
     * AES（256）解密
     *
     * @param content 待解密内容
     * @param key     解密密钥
     * @return 解密之后
     */
    public static String decrypt(String content, String key) {
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), AES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, OFF_SET);
            Cipher cipher = Cipher.getInstance(PADDING);
            //解密时使用加密器的解密模式
            cipher.init(Cipher.DECRYPT_MODE, skey, iv);// 初始化
            byte[] result = cipher.doFinal(new Base64().decode(content));
            return new String(result); // 解密
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return null;
    }

    public static void main(String[] args) {
        String text = "isFeign";
        String encrypt = encrypt(text, "xTfwGesIES8wdaDW3w5ts4wTEL3po5Ye");
        String decrypt = decrypt(encrypt, "xTfwGesIES8wdaDW3w5ts4wTEL3po5Ye");
        System.out.println("加密后：" + encrypt);
        System.out.println("解密后：" + decrypt);
    }
}
