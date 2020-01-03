package com.xy.experiment.utils.jwt;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

public class SendData {
    private static String serverURI = "http://202.205.145.156:8017";

    public static void main(String[] args) {
		checkStatus();

        loginUser("test1", "123456");
        String attachmentId = upload();
        sendData(attachmentId);
    }

    public static String loginUser(String loginId, String pwd) {
        try {
            String cnonce = getRandomString(16);
            String nonce = getRandomString(16);
            try {
                pwd = SHA256(nonce + SHA256(pwd).toUpperCase() + cnonce).toUpperCase();
                System.out.println("===========>" + pwd);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //base64(HmacSHA256(based64(raw header) + '.' + base64(raw payload), secret key))

            String url = serverURI + "/sys/api/user/validate?";
            String postUrl = url + "username=" + loginId + "&password=" + pwd + "&nonce=" + nonce + "&cnonce=" + cnonce;
            System.out.println(postUrl);

            try {
                String sr = sendPost(postUrl, "");
                JSONObject retJson = new JSONObject();
                retJson = JSONObject.parseObject(sr);

                return  retJson.toJSONString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String upload() {
        String json = "SYS";
        String id = "";
        try {
            File file = new File("E:\\zxh\\online\\strand.jpg");
            String xjwt = JwtUtil.encrty(json);
            String url = serverURI + "/project/log/attachment/upload?";
            String totalChunks = "1";
            String current = "0";
            String filename = "测试文件.pdf";
            String chunkSize = "5048576";

            filename = URLEncoder.encode(filename, "UTF-8");
            //上传实验文件
            String params = url
                    + "totalChunks=" + totalChunks
                    + "&current=" + current
                    + "&filename=" + filename
                    + "&chunkSize=" + chunkSize
                    + "&xjwt=" + xjwt;
            System.out.println("上传实验文件参数===========>" + params);
            String sr = uploadFile(params, file);

            //读取实验结果
            JSONObject retJson = JSONObject.parseObject(sr);
            id = (String) retJson.get("id");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void checkStatus() {
        JSONObject param = new JSONObject();
        param.put("username", "test1");
        param.put("issuerId", "100400");

        String json = param.toString();
        try {
            String xjwt = JwtUtil.encrty(json);
            String url = serverURI + "/third/api/test/result/upload?xjwt=" + xjwt;
            System.out.println("=============>" + url);
            try {
                String sr = sendPost(url, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void sendData(String attachmentId) {
        long now = new Date().getTime();
        System.out.println("now========>" + now);
        JSONObject param = new JSONObject();
        param.put("username", "test");
        param.put("projectTitle", "井字架仿真实验平台");
        param.put("status", 1);
        param.put("score", 94);
        param.put("startDate", now - 4000000);
        param.put("endDate", now - 1000000);
        param.put("timeUsed", 15);
        param.put("issuerId", "100400");
        param.put("attachmentId", attachmentId);
        String json = param.toString();
        try {
            String xjwt = JwtUtil.encrty(json);
            String url = serverURI + "/project/log/upload?xjwt=" + xjwt;
            System.out.println("url==================>" + url);
            try {
                String sr = sendPost(url, "");
                JSONObject retJson = JSONObject.parseObject(sr);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String strJson = JwtUtil.dencrty(xjwt);
            System.out.println("strJson==============>" + strJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            //conn.setRequestProperty("method", "POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = in.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            result = sb.toString();
            System.out.println("发送数据后结果===-------POST-------" + result);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String uploadFile(String serverURL, File file) {
        String result = "";
        try {
            URL url = new URL(serverURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/html");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.connect();
            conn.setConnectTimeout(10000);
            OutputStream out = conn.getOutputStream();

            DataInputStream in = new DataInputStream(new FileInputStream(file));

            int bytes = 0;
            byte[] buffer = new byte[1024];
            while ((bytes = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytes);
            }
            in.close();
            out.flush();
            out.close();

            // 得到响应码
            int res = conn.getResponseCode();
            StringBuilder sb2;
            if (res == 200) {
                // 定义BufferedReader输入流来读取URL的响应
                BufferedReader in2 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = in2.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                result = sb.toString();
            }
            System.out.println("上传文件后结果===-------POST-------=======>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getRandomString(int length) {
        String str = "ABCDEF0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(16);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
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
