package com.xy.experiment;

import com.xy.experiment.facade.impl.CheckUserImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@SpringBootTest
class ExperimentApplicationTests {

    private final static Logger logger = LoggerFactory.getLogger(CheckUserImpl.class);

    @Test
    void contextLoads() {
        System.out.println("=====================hello world");
    }

    @Test
    void base64Test(){
        Base64.Encoder based64 = Base64.getEncoder();

    }

    @Test
    void AES256(){
        sha256HMAC("123", "123");

    }

    /**
     * sha256_HMAC加密
     *
     * @param message
     *            消息
     * @param secret
     *            秘钥
     * @return 加密后字符串
     */
    public static String sha256HMAC(String message, String secret) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] bytes = sha256HMAC.doFinal(message.getBytes());
            message = byteArrayToHexString(bytes);
        } catch (Exception e) {
            logger.error("");
        }
        return message;
    }

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b
     *            字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }
}
