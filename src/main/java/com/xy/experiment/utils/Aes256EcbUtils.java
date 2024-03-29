package com.xy.experiment.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

public class Aes256EcbUtils {

    private static final Logger logger = LoggerFactory.getLogger(Aes256EcbUtils.class);

    private static final String ALGORITHM = "AES/ECB/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * @param str 要被加密的字符串
     * @param key 加/解密要用的长度为32的字节数组（256位）密钥
     * @return byte[] 加密后的字节数组
     */
    public static byte[] Aes256Encode(String str, byte[] key) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES"); // 生成加密解密需要的Key
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            result = cipher.doFinal(str.getBytes("UTF-8"));
        } catch (Exception e) {
            logger.error("sequence:{}, 加密失败", e);
        }
        return result;
    }

    /**
     * @param bytes 要被解密的字节数组
     * @param key 加/解密要用的长度为32的字节数组（256位）密钥
     * @return String 解密后的字符串
     */
    public static String Aes256Decode(byte[] bytes, byte[] key) {
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES"); // 生成加密解密需要的Key
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = cipher.doFinal(bytes);
            result = new String(decoded, "UTF-8");
        } catch (Exception e) {
            logger.error("sequence:{}, 解密失败", e);
        }
        return result;
    }

}
