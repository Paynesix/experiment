package com.xy.experiment.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EmailCacheVo implements Serializable {

    /** 账户 同一个账号只存在一个是启用的，新增和修改时需要加锁 */
    private String email;

    /**邮箱发送时间*/
    private LocalDateTime sendEmailDate;

    /**加密token*/
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getSendEmailDate() {
        return sendEmailDate;
    }

    public void setSendEmailDate(LocalDateTime sendEmailDate) {
        this.sendEmailDate = sendEmailDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
