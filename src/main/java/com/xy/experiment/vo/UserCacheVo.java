package com.xy.experiment.vo;

import java.io.Serializable;
import java.util.Date;

public class UserCacheVo implements Serializable {

    /** 账户 同一个账号只存在一个是启用的，新增和修改时需要加锁 */
    private String account;

    /**登陆时间*/
    private Date loginDate;

    private Object object;

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
