package com.xy.experiment.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class HMACSHA256 {
	// SECRET KEY
	/**
	 * 将加密后的字节数组转换成字符串
	 * 
	 * @param b 字节数组
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

		}
		return message;
	}


	/*
	 * 利用Apache的工具类实现SHA-256加密
	 * 所需jar包下載 http://pan.baidu.com/s/1nuKxYGh
	 *
	 * @param str 加密后的报文
	 * @return
	 */

	public static String SHA256(String str) {
		MessageDigest messageDigest;
		String encdeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
			encdeStr = Hex.encodeHexString(hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encdeStr;
	}
}
