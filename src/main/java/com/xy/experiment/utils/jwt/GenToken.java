package com.xy.experiment.utils.jwt;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class GenToken {
    private static String secret = "16jmp2";
    private static String aeskey = "SbYymvfZ8UjEmShxRAB0b1Dtaa0uGjDOOJa/f0Mbuo4=";
    private static Long issueId = 100400L;

    public static void main(String[] args) {
        long now = new Date().getTime();
        System.out.println("now======>" + now);
        JSONObject param = new JSONObject();
        param.put("id", "851");
        param.put("un", "test1");
        param.put("dis", "林黛玉");
        String json = param.toString();
        try {
            String xjwt = JwtUtil.encrty(json);
            System.out.println("xjwt========>" + xjwt);

            String strJson = JwtUtil.dencrty(xjwt);
            System.out.println("strJson==========>" + strJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
