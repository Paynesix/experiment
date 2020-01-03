package com.xy.experiment.vo;

import java.io.Serializable;
import java.util.Date;

public class ScoreVo implements Serializable {

    /** 账户 同一个账号只存在一个是启用的，新增和修改时需要加锁 */
    private String account;
    /** 学校 */
    private String schoolName;

    /** 开始时间 */
    private String expStart;

    /** 结束时间 */
    private String expEnd;

    /**实验时长 分钟*/
    private Integer expTime;
    /**实验分数*/
    private Integer score;
    /**是否合格 0-合格， 1-不合格*/
    private Integer isQualified;
    /** 备注 */
    private String memo;

    private static final long serialVersionUID = 1L;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getExpStart() {
        return expStart;
    }

    public void setExpStart(String expStart) {
        this.expStart = expStart;
    }

    public String getExpEnd() {
        return expEnd;
    }

    public void setExpEnd(String expEnd) {
        this.expEnd = expEnd;
    }

    public Integer getExpTime() {
        return expTime;
    }

    public void setExpTime(Integer expTime) {
        this.expTime = expTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getIsQualified() {
        return isQualified;
    }

    public void setIsQualified(Integer isQualified) {
        this.isQualified = isQualified;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}