//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xy.experiment.utils.shunde;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AES {
    private static final Logger logger = LoggerFactory.getLogger(AES.class);
    protected static String DEFAULT_AES_ALG = "AES/CBC/PKCS5Padding";
    protected static String DEFAULT_AES_MODE_PADD = "CBC/PKCS5Padding";
    protected static int BLOCK_SIZE = 16;
    private static final byte[] KEY_IV = new byte[]{31, 50, 105, 90, 113, 45, 95, 20, 78, 106, 121, 82, 59, 45, 58, 69};

    public AES() {
    }

    public static boolean encode(InputStream ins, OutputStream outs, Key key, String modePadding, byte[] iv) {
        try {
            Cipher cipher = null;
            if (modePadding == null) {
                cipher = Cipher.getInstance(DEFAULT_AES_ALG);
            } else if (modePadding.startsWith("AES/")) {
                cipher = Cipher.getInstance(modePadding);
            } else {
                cipher = Cipher.getInstance("AES/" + modePadding);
            }

            int max_block_size = BLOCK_SIZE;
            if (iv != null) {
                IvParameterSpec ips = new IvParameterSpec(iv);
                cipher.init(1, key, ips);
            } else {
                cipher.init(1, key);
            }

            byte[] buffer = new byte[max_block_size];
            Object var9 = null;

            int readSize;
            byte[] enData;
            while((readSize = ins.read(buffer)) > 0) {
                enData = cipher.update(buffer, 0, readSize);
                if (enData != null && enData.length > 0) {
                    outs.write(enData);
                }
            }

            enData = cipher.doFinal();
            if (enData != null && enData.length > 0) {
                outs.write(enData);
            }

            return true;
        } catch (Throwable var10) {
            logger.error("AES encode fail", var10);
            return false;
        }
    }

    public static byte[] encode(byte[] srcData, Key key, String modePadding) {
        InputStream in = new ByteArrayInputStream(srcData);
        int srcLen = srcData.length;
        int max_block_size = BLOCK_SIZE;
        int outLen = (srcLen + max_block_size - 1) / max_block_size + BLOCK_SIZE;
        ByteArrayOutputStream out = new ByteArrayOutputStream(outLen);
        boolean result = encode(in, out, key, modePadding, KEY_IV);
        return !result ? null : out.toByteArray();
    }

    public static String encode(String srcData, String key) throws Exception {
        byte[] keyData = HexCode.decode(key);
        SecretKeySpec skeySpec = new SecretKeySpec(keyData, "AES");
        byte[] enData = encode(srcData.getBytes(), skeySpec, (String)null);
        return HexCode.encode(enData, true);
    }

    public static String encode2base64(String srcData, String key, boolean urlSafe) throws Exception {
        byte[] keyData = HexCode.decode(key);
        SecretKeySpec skeySpec = new SecretKeySpec(keyData, "AES");
        byte[] enData = encode(srcData.getBytes(), skeySpec, (String)null);
        return Base64.encode(enData, urlSafe);
    }

    public static String encode2base64(String srcData, String key) throws Exception {
        return encode2base64(srcData, key, false);
    }

    public static boolean decode(InputStream ins, OutputStream outs, Key key, String modePadding) {
        try {
            Cipher cipher = null;
            if (modePadding == null) {
                cipher = Cipher.getInstance(DEFAULT_AES_ALG);
            } else if (modePadding.startsWith("AES/")) {
                cipher = Cipher.getInstance(modePadding);
            } else {
                cipher = Cipher.getInstance("AES/" + modePadding);
            }

            int max_block_size = BLOCK_SIZE;
            IvParameterSpec ips = new IvParameterSpec(KEY_IV);
            cipher.init(2, key, ips);
            byte[] buffer = new byte[max_block_size];
            Object var9 = null;

            int readSize;
            byte[] deData;
            while((readSize = ins.read(buffer)) > 0) {
                deData = cipher.update(buffer, 0, readSize);
                if (deData != null && deData.length > 0) {
                    outs.write(deData);
                }
            }

            deData = cipher.doFinal();
            if (deData != null && deData.length > 0) {
                outs.write(deData);
            }

            return true;
        } catch (Throwable var10) {
            logger.error("AES decode fail", var10);
            return false;
        }
    }

    public static byte[] decode(byte[] srcData, Key key, String modePadding) {
        InputStream in = new ByteArrayInputStream(srcData);
        int srcLen = srcData.length;
        int max_block_size = BLOCK_SIZE;
        int outLen = (srcLen + max_block_size - 1) / max_block_size;
        ByteArrayOutputStream out = new ByteArrayOutputStream(outLen);
        boolean result = decode(in, out, key, modePadding);
        return !result ? null : out.toByteArray();
    }

    public static String decode(String srcData, String key) throws Exception {
        byte[] keyData = HexCode.decode(key);
        SecretKeySpec skeySpec = new SecretKeySpec(keyData, "AES");
        byte[] enData = HexCode.decode(srcData);
        byte[] deData = decode(enData, skeySpec, (String)null);
        return new String(deData);
    }

    public static String decodeFromBase64(String srcData, String key) throws Exception {
        byte[] keyData = HexCode.decode(key);
        SecretKeySpec skeySpec = new SecretKeySpec(keyData, "AES");
        byte[] enData = Base64.decode(srcData);
        byte[] deData = decode(enData, skeySpec, (String)null);
        return new String(deData);
    }

    public static void main(String[] args) {
        try {
            String srcData = "2b6ce6afb0356424bb10089002cf85dc";
            String key = "b6ce6afb0356424bb10089002cf85dca";
            String enData = encode2base64(srcData, key, true);
            System.out.println(enData);
            String deData = decodeFromBase64(enData, key);
            System.out.println(deData);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }
}
