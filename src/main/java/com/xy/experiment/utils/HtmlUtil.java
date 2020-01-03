//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xy.experiment.utils;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlUtil {
    private static final Logger logger = LoggerFactory.getLogger(HtmlUtil.class);
    public static final String ATTR_JSON_OBJECT = "com.gzhc365.web.json.obj";

    public HtmlUtil() {
    }

    public static void writerJson(HttpServletResponse response, Object object) {
        response.setContentType("application/json");
        String json = JSON.toJSONString(object);
        logger.info("Response" + json);
        writer(response, json);
    }

    public static void writerCustomize(HttpServletResponse response, String data, String contextType) {
        response.setContentType(contextType == null ? "application/json" : contextType);
        logger.info("Response" + data);
        writer(response, data);
    }

    private static void writer(HttpServletResponse response, String str) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = null;
            out = response.getWriter();
            out.print(str);
            out.flush();
            out.close();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }
}
