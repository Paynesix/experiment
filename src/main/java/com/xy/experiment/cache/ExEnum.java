package com.xy.experiment.cache;

import java.util.HashMap;
import java.util.Map;

public enum ExEnum {

    INSTANCE;
    private Map<String, Object> userCache;  // 用户是否已经登陆
    private Map<String, Object> emailCache; // 重置密码，判断是否过期

    ExEnum() {
        this.userCache = new HashMap<>();
        this.emailCache = new HashMap<>();
    }

    public Map<String, Object> getUserCache() {
        return userCache;
    }

    public Map<String, Object> getEmailCache() {
        return emailCache;
    }

    public static ExEnum getInstance() {
        return INSTANCE;
    }

}