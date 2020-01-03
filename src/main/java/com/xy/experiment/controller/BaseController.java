//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xy.experiment.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xy.experiment.utils.HtmlUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    public static final String EX_CONTEXT = "COM.XY.EX_CONTEXT";
    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String DATA = "data";
    public static final String EXPERIMENT_USER = "experiment_user";
    public static final String UNKNOWN = "unknown";

    public BaseController() {
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip) && ip.indexOf(",") != -1) {
            ip = ip.split(",")[0];
        }

        return ip;
    }

    public Map<String, Object> getRootMap() {
        Map<String, Object> rootMap = new HashMap();
        return rootMap;
    }

    public ModelAndView forword(String viewName, Map<String, ?> context) {
        return new ModelAndView(viewName, context);
    }

    public ModelAndView error(String errMsg) {
        return new ModelAndView("error");
    }

    public static void sendSuccessMessage(HttpServletResponse response, String message) {
        Map<String, Object> result = new HashMap();
        result.put("code", 0);
        result.put("msg", message);
        HtmlUtil.writerJson(response, result);
    }

    public static void sendSuccessData(HttpServletResponse response, Object data) {
        Map<String, Object> result = new HashMap();
        result.put("code", 0);
        result.put("data", data);
        HtmlUtil.writerJson(response, result);
    }

    public static void sendCustomizeData(HttpServletResponse response, String data, String contextType) {
        HtmlUtil.writerCustomize(response, data, contextType);
    }

    public static void sendCustomizeData(HttpServletResponse response, String data) {
        HtmlUtil.writerCustomize(response, data, (String)null);
    }

    public static void sendFailureMessage(HttpServletResponse response, int code, String message) {
        Map<String, Object> result = new HashMap();
        result.put("code", code);
        result.put("msg", message);
        HtmlUtil.writerJson(response, result);
    }

    public static ExContext getExContext(HttpServletRequest request) {
        return (ExContext)request.getAttribute("COM.XY.EX_CONTEXT");
    }
}
