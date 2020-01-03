package com.xy.experiment.utils;

public class EmailTemplate {

    
    public static String getEmailTemplate(String url){
        String html = "";
        html = "动态码：" + url ;
        return html;
    }
    
}
