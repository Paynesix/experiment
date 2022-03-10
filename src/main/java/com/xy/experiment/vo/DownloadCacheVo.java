package com.xy.experiment.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DownloadCacheVo implements Serializable {

    /** 账户 同一个账号只存在一个是启用的，新增和修改时需要加锁 */
    private String account;

    /**上次下载时间*/
    private LocalDateTime downloadDate;

    /**下载次数*/
    private int downCount;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public LocalDateTime getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(LocalDateTime downloadDate) {
        this.downloadDate = downloadDate;
    }

    public int getDownCount() {
        return downCount;
    }

    public void setDownCount(int downCount) {
        this.downCount = downCount;
    }
}
