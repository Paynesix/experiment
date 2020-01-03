package com.xy.experiment.utils;

import com.alibaba.fastjson.JSONObject;
import com.xy.experiment.exceptions.ExperimentException;
import com.xy.experiment.utils.jwt.JwtUtil;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

public class HttpUtils {


    public static String serverURI = "http://202.205.145.156:8017";
    public static String secret = "16jmp2";
    public static String aeskey = "SbYymvfZ8UjEmShxRAB0b1Dtaa0uGjDOOJa/f0Mbuo4=";
    public static Long issueId = 100400L;

    public static String loginId = "test2";
    public static String pwd = "123456";
    
    public final static String UTF_8 = "utf-8";
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String APPLICATION_JSON = "application/json";

    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param urlPath 发送请求的 URL
     * @param json    请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String urlPath, JSONObject json) throws IOException {
        logger.info("发送post请求============>urlPath:{}, json:{}", urlPath, json);
        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlPath);
            post.setHeader(CONTENT_TYPE, APPLICATION_JSON);
            StringEntity s = new StringEntity(URLEncoder.encode(json.toString(), UTF_8), UTF_8);
            s.setContentEncoding(new BasicHeader(CONTENT_TYPE, APPLICATION_JSON));
            post.setEntity(s);
            HttpResponse httpResponse = client.execute(post);
            InputStream in = httpResponse.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, UTF_8));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                strber.append(line + "\n");
            }
            in.close();
            result = strber.toString();
            logger.info("http请求结果==============>", result);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "http请求服务器异常!");
            }
        } catch (ExperimentException e) {
            logger.info("http请求业务异常=========>", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "http请求业务异常!");
        } catch (Exception e) {
            logger.info("http请求系统异常=========>", e);
            throw new ExperimentException(ExperimentException.BIZ_ERROR_CODE, "http请求系统异常!");
        }
        return result;
    }

    public static String upload(String filePath, String fileName) {
        logger.info("文件上传开始=======> filePath:{}, fileName:{}", filePath, fileName);
        String json = "SYS";
        String id = "";
        try {
            File file = new File(filePath + fileName);
            String xjwt = JwtUtil.encrty(json);
            String url = serverURI + "/project/log/attachment/upload?";
            try {
                int totalChunks = 1;
                int current = 0;

                double fileSize = file.length();
                int chunkSize = 1048576;
                double fileCnt = fileSize / 1048576;
                totalChunks = (int) (file.length() / 1048576);
                if (fileCnt > totalChunks) {
                    totalChunks += 1;
                }

                RandomAccessFile raf = null;
                // 获取目标文件 预分配文件所占的空间 在磁盘中创建一个指定大小的文件  r 是只读
                raf = new RandomAccessFile(new File(filePath + fileName), "r");
                long length = raf.length();//文件的总长度
                System.out.println("file length=" + length);
                int count = (int) (Math.ceil((double) length / (double) chunkSize));//文件切片后的分片数

                String[] sr = new String[2];
                String cookie = "";
                for (int i = 0; i < totalChunks; i++) {
                    long begin = i * chunkSize;
                    long end = (long) (((i + 1) * chunkSize > fileSize) ? fileSize : (i + 1) * chunkSize);
                    System.out.println("end=" + end);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int realLength = 0;
                    raf.seek(begin);//将文件流的位置移动到指定begin字节处
                    //while(raf.getFilePointer() <= end && (temp = raf.readLine()) != null)
                    while (raf.getFilePointer() <= end && (realLength = raf.read(buffer)) > 0) {
                        byteArrayOutputStream.write(buffer, 0, realLength);
                    }
                    byte[] fileBytes = byteArrayOutputStream.toByteArray();
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                    String requestURL = url + "&totalChunks=" + totalChunks + "&current=" + i + "&filename=" + fileName + "&chunkSize=" + chunkSize + "&xjwt=" + xjwt;
                    sr = uploadFile(requestURL, fileBytes, cookie);
                    cookie = sr[1];
                    System.out.println("-------POST-------" + sr[0]);
                    System.out.println("-------POST-------" + sr[1]);
                }
                raf.close();

                //读取实验结果
                JSONObject retJson = new JSONObject();
                retJson = JSONObject.parseObject(sr[0]);

                System.out.println("id:" + retJson.get("id"));
                System.out.println("code:" + retJson.get("code"));
                id = (String) retJson.get("id");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static String[] uploadFile(String serverURL, byte[] fileBytes, String cookie) {
        logger.info("上传文件开始======>serverURL:{}", serverURL);
        String[] result = new String[2];
        try {
            CookieManager manager = new CookieManager();
            //设置cookie策略，只接受与你对话服务器的cookie，而不接收Internet上其它服务器发送的cookie
            CookieHandler.setDefault(manager);
            URL url = new URL(serverURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            //httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            // 分界符
            conn.setRequestProperty("Cookie", cookie);
            conn.setConnectTimeout(1000000000);
            OutputStream out = conn.getOutputStream();
            conn.setInstanceFollowRedirects(false);
            out.write(fileBytes);
            out.flush();
            out.close();
            //发送文件
            conn.connect();
            result[1] = conn.getHeaderField("Set-Cookie");
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
                result[0] = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
