package com.xy.experiment.utils.shunde;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xy.experiment.utils.shunde.Tuple.Tuple2;

public class RSA {
    private static final Logger logger = LoggerFactory.getLogger(RSA.class);
    public static final int PUBLIC_KEY = 1;
    public static final int PRIVATE_KEY = 2;
    protected static String DEFAULT_RSA_ALG = "RSA/ECB/PKCS1Padding";
    protected static String DEFAULT_RSA_MODE_PADD = "ECB/PKCS1Padding";
    protected static final int MAX_ENCRYPT_BLOCK = 117;
    protected static final int MAX_DECRYPT_BLOCK = 128;

    public RSA() {
    }

    public static Tuple2<PublicKey, PrivateKey> genKeyPair(String keyRandom, int keySize) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(keySize, new SecureRandom(keyRandom.getBytes()));
            KeyPair pair = generator.generateKeyPair();
            PublicKey pubKey = pair.getPublic();
            PrivateKey privKey = pair.getPrivate();
            return new Tuple2(pubKey, privKey);
        } catch (NoSuchAlgorithmException var6) {
            logger.error("not support RSA", var6);
        } catch (Throwable var7) {
            logger.error("generate RSA key fail", var7);
        }

        return null;
    }

    public static Tuple2<String, String> genKeyStrPair(String keyRandom, int keySize) {
        Tuple2<PublicKey, PrivateKey> keyPair = genKeyPair(keyRandom, keySize);
        if (keyPair == null) {
            return null;
        } else {
            String pubStrKey = Base64.encode(((PublicKey)keyPair._1()).getEncoded());
            String privStrKey = Base64.encode(((PrivateKey)keyPair._2()).getEncoded());
            return new Tuple2(pubStrKey, privStrKey);
        }
    }

    public static PublicKey getPublicKey(String key) {
        try {
            byte[] keyBytes = Base64.decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception var5) {
            logger.error("RSA getPublicKey fail", var5);
            return null;
        }
    }

    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] keyBytes = Base64.decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception var5) {
            logger.error("RSA getPrivateKey fail", var5);
            return null;
        }
    }

    public static boolean verifyBySHA1(String text, String sign, String key) {
        try {
            Signature signatureChecker = Signature.getInstance("SHA1WithRSA");
            PublicKey pubKey = getPublicKey(key);
            signatureChecker.initVerify(pubKey);
            signatureChecker.update(text.getBytes());
            byte[] signBytes = Base64.decode(sign);
            return signatureChecker.verify(signBytes);
        } catch (Throwable var6) {
            logger.error("verifyBySHA1 fail", var6);
            return false;
        }
    }

    public static boolean verifyBySHA1(String text, String charset, String sign, String key) {
        try {
            Signature signatureChecker = Signature.getInstance("SHA1WithRSA");
            PublicKey pubKey = getPublicKey(key);
            signatureChecker.initVerify(pubKey);
            signatureChecker.update(text.getBytes(charset));
            byte[] signBytes = Base64.decode(sign);
            return signatureChecker.verify(signBytes);
        } catch (Throwable var7) {
            logger.error("verifyBySHA1 fail", var7);
            return false;
        }
    }

    public static byte[] genByteSignWithSHA1(String text, String key) {
        try {
            Signature signatureChecker = Signature.getInstance("SHA1WithRSA");
            PrivateKey privKey = getPrivateKey(key);
            signatureChecker.initSign(privKey);
            signatureChecker.update(text.getBytes());
            return signatureChecker.sign();
        } catch (Throwable var4) {
            logger.error("genByteSignWithSHA1 fail", var4);
            return null;
        }
    }

    public static byte[] genByteSignWithSHA1(String text, String charset, String key) {
        try {
            Signature signatureChecker = Signature.getInstance("SHA1WithRSA");
            PrivateKey privKey = getPrivateKey(key);
            signatureChecker.initSign(privKey);
            signatureChecker.update(text.getBytes(charset));
            return signatureChecker.sign();
        } catch (Throwable var5) {
            logger.error("genByteSignWithSHA1 fail", var5);
            return null;
        }
    }

    public static String genSignWithSHA1(String text, String charset, String key) {
        return genSignWithSHA1(text, charset, key, false);
    }

    public static String genSignWithSHA1(String text, String charset, String key, boolean urlSafe) {
        byte[] signData = genByteSignWithSHA1(text, charset, key);
        return signData != null && signData.length != 0 ? Base64.encode(signData, urlSafe) : null;
    }

    public static String genSignWithSHA1(String text, String key) {
        return genSignWithSHA1(text, key, false);
    }

    public static String genSignWithSHA1(String text, String key, boolean urlSafe) {
        byte[] signData = genByteSignWithSHA1(text, key);
        return signData != null && signData.length != 0 ? Base64.encode(signData, urlSafe) : null;
    }

    public static boolean encode(InputStream ins, OutputStream outs, Key key, String modePadding) {
        try {
            Cipher cipher = null;
            if (modePadding == null) {
                cipher = Cipher.getInstance(DEFAULT_RSA_ALG);
            } else if (modePadding.startsWith("RSA/")) {
                cipher = Cipher.getInstance(modePadding);
            } else {
                cipher = Cipher.getInstance("RSA/" + modePadding);
            }

            RSAKey rsaKey = (RSAKey)key;
            int keyBitlen = rsaKey.getModulus().bitLength();
            logger.debug("RSA endocde key length:", keyBitlen);
            int max_block_size = keyBitlen / 8 - 11;
            cipher.init(1, key);
            byte[] buffer = new byte[max_block_size];

            int readSize;
            while((readSize = ins.read(buffer)) > 0) {
                byte[] enData = cipher.doFinal(buffer, 0, readSize);
                outs.write(enData);
            }

            return true;
        } catch (Throwable var11) {
            logger.error("RSA encode fail", var11);
            return false;
        }
    }

    public static byte[] encode(byte[] srcData, Key key, String modePadding) {
        InputStream in = new ByteArrayInputStream(srcData);
        int srcLen = srcData.length;
        RSAKey rsaKey = (RSAKey)key;
        int keyBitlen = rsaKey.getModulus().bitLength();
        logger.debug("RSA endocde key length:", keyBitlen);
        int max_block_size = keyBitlen / 8 - 11;
        int outLen = (srcLen + max_block_size - 1) / max_block_size * (keyBitlen / 8);
        ByteArrayOutputStream out = new ByteArrayOutputStream(outLen);
        boolean result = encode(in, out, key, modePadding);
        return !result ? null : out.toByteArray();
    }

    public static byte[] encode(byte[] srcData, Key key) {
        return encode(srcData, key, (String)null);
    }

    public static byte[] encode(byte[] srcData, String key, int keyType) {
        Key encodeKey = null;
        if (keyType == 2) {
            encodeKey = getPrivateKey(key);
        } else {
            encodeKey = getPublicKey(key);
        }

        return encode((byte[])srcData, (Key)encodeKey);
    }

    public static String encode(String srcData, String key, int keyType, boolean urlSafe) {
        byte[] data = encode(srcData.getBytes(), key, keyType);
        return Base64.encode(data, urlSafe);
    }

    public static String encode(String srcData, String key, int keyType) {
        return encode(srcData, key, keyType, false);
    }

    public static String encode(String srcData, String key) {
        return encode((String)srcData, key, 1);
    }

    public static String encode(String srcData, String key, boolean urlSafe) {
        return encode(srcData, key, 1, urlSafe);
    }

    public static boolean decode(InputStream ins, OutputStream outs, Key key, String modePadding) {
        try {
            Cipher cipher = null;
            if (modePadding == null) {
                cipher = Cipher.getInstance(DEFAULT_RSA_ALG);
            } else if (modePadding.startsWith("RSA/")) {
                cipher = Cipher.getInstance(modePadding);
            } else {
                cipher = Cipher.getInstance("RSA/" + modePadding);
            }

            RSAKey rsaKey = (RSAKey)key;
            int keyBitlen = rsaKey.getModulus().bitLength();
            logger.debug("RSA decode key length:", keyBitlen);
            int max_block_size = keyBitlen / 8;
            cipher.init(2, key);
            byte[] buffer = new byte[max_block_size];

            int readSize;
            while((readSize = ins.read(buffer)) > 0) {
                byte[] deData = cipher.doFinal(buffer, 0, readSize);
                outs.write(deData);
            }

            return true;
        } catch (Throwable var11) {
            logger.error("RSA decode fail", var11);
            return false;
        }
    }

    public static byte[] decode(byte[] enData, Key key, String modePadding) {
        InputStream in = new ByteArrayInputStream(enData);
        int len = enData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream(len);
        boolean result = decode(in, out, key, modePadding);
        return !result ? null : out.toByteArray();
    }

    public static byte[] decode(byte[] enData, Key key) {
        return decode(enData, key, (String)null);
    }

    public static byte[] decode(byte[] enData, String key, int keyType) {
        Key decodeKey = null;
        if (keyType == 1) {
            decodeKey = getPublicKey(key);
        } else {
            decodeKey = getPrivateKey(key);
        }

        return decode((byte[])enData, (Key)decodeKey);
    }

    public static String decode(String enData, String key, int keyType) {
        byte[] inData = Base64.decode(enData);
        byte[] data = decode(inData, key, keyType);
        return new String(data);
    }

    public static String decode(String enData, String key) {
        return decode((String)enData, key, 2);
    }

    public static void main(String[] args) {
        try {
            Tuple2<String, String> keys = genKeyStrPair("1234", 1024);
            System.out.println((String)keys._1());
            System.out.println(Base64.formatBase64Str((String)keys._2()));
            System.out.println(Base64.formatBase64Str((String)keys._2()));
            String srcText = "123wsdfQwehPE8ynEwZkHm0XRi80sMQnNJ85wnVqssuV+jAWnR565glvy2ks9aLz6TZAkA5KZ+1axquBMlmKpUaQFv7LXipfkVWF16h4QeWGA/h9xreRcAnt5rbwk2JFD0DupQeZelEWrHxxvaAWBkFhSsFAkBcwTiApqV+I6nAWKv16llzaIvKDqn7hPpXVNM7iV/VipM/zETXeJMvBSHJAm2OdD5eL6UlQ7WttaIS";
            String sign = genSignWithSHA1(srcText, (String)keys._2());
            System.out.println(sign);
            System.out.println(verifyBySHA1(srcText, "12" + sign.substring(2), (String)keys._1()));
            String srcData = "123wsdfQwehPE8ynEwZkHm0XRi80sMQnNJ85wnVqssuV+jAWnR565glvy2ks9aLz6TZAkA5KZ+1axquBMlmKpUaQFv7LXipfkVWF16h4QeWGA/h9xreRcAnt5rbwk2JFD0DupQeZelEWrHxxvaAWBkFhSsFAkBcwTiApqV+I6nAWKv16llzaIvKDqn7hPpXVNM7iV/VipM/zETXeJMvBSHJAm2OdD5eL6UlQ7WttaISLAcGGQKBgF1CZT+Lsf2aLRsD8f/w5yp9/fSlDDmX/F4eFO+qUQUa1S+CrGh4/KiB7PGDeiffnE0yBfMmrHDWZALhLUn1PNoX6iW8QCf+Fq9SrUzGJarYcVyNPr6ATuhQuWGr++qHj3+fys8r+04vEIHO";
            String enData = encode(srcData, (String)keys._1());
            System.out.println(enData);
            String deData = decode(enData, (String)keys._2());
            System.out.println(deData);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }
}
