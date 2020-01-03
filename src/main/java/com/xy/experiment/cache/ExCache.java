package com.xy.experiment.cache;

import java.util.HashMap;
import java.util.Map;

public class ExCache {

    private Map<String, Object> userCache;  // 用户是否已经登陆
    private Map<String, Object> emailCache; // 重置密码，判断是否过期

}
