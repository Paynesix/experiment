package com.xy.experiment.utils.jwt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

public class JwtUtil {

    private static String secret = "fjbr43";
    private static String aeskey = "s818nmsYyGFp2hX6aRiKb23eD0TxQdU68jnfMnV/C/k=";
    private static Long issueId = 100327L;

//    private experiment String secret = "16jmp2";
//    private experiment String aeskey = "SbYymvfZ8UjEmShxRAB0b1Dtaa0uGjDOOJa/f0Mbuo4=";
//    private experiment Long issueId = 100400L;

    public static void main(String[] args) {
        String token = "AAABbwKsBDABAAAAAAABh%2Bc%3D.g9liPj2S5o7rfmYyejaWJVPuL968sceNgo3GP7lrRA7B3MuuL0awEp718m9a3yYxKyzQfpnmm8up1Wq7%2FwRaYgUudPzl2QWuWkknYMGam7ngK9HiqnTU0aNjbl0UnDycWYRkxccUDGmtZBZvi%2BAtx%2B9%2FJpoZIXqslIhwtJx3w0kResGc14%2FdriWupxK9yl9%2B5UwViIBQEl25ojfXUTRAPQIOVhPe1a7FvE5V6hpqclBmFu6eBN0HM49gizqj3QtRjFjjE8nFH8I%2BeX7Jd4nk43M1p%2Fbwp4rtupYYl0W%2FvPEJQI7DmQj6tK1bkEUleQ3sxoZzBs7r5cgm%2BsT8uMXmRg%3D%3D.oLu5JcFlcrYUmgh3A1fOYjRMiKTXIiQaDOkFDFmRjQE%3D";
        System.out.println("token==" + token);
        try {
            String resultJson = dencrty(token);
            System.out.println("resultJson==" + resultJson);
            String encrtyStr = encrty(resultJson);
            System.out.println("encrtyStr===========>" + encrtyStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //解密token
    public static String dencrty(String token) throws UnsupportedEncodingException {
        //获取当前时间
        long now = new Date().getTime();
        //创建JWT实例
        JWT jwt = new JWT(secret, aeskey, System.nanoTime(), issueId);
        token = URLDecoder.decode(token, "UTF-8");
        //调用解密方法解密token
        String resultJson = jwt.verifyAndDecrypt(token, now);
        return resultJson;
    }

    //加密数据
    public static String encrty(String json) throws UnsupportedEncodingException {
        //获取当前时间
        long now = System.currentTimeMillis();
        //创建JWT实例
        JWT jwt = new JWT(secret, aeskey, System.nanoTime(), issueId);
        //创建payload用来装参数
        ByteBuffer payload = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
        payload.put(json.getBytes("UTF-8")).flip();
        //创建out对象
        ByteBuffer out = ByteBuffer.allocate(1024);
        //调用加密方法，加密参数
        jwt.encryptAndSign(JWT.Type.SYS, payload, out, now + 60 * 60 * 1000);
        String xjwt = new String(out.array(), out.arrayOffset(), out.remaining());
        return URLEncoder.encode(xjwt, "UTF-8");
    }
}
