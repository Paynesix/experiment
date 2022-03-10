package com.xy.experiment.entity;

import java.io.Serializable;
import java.util.Date;

public class ExperimentExam implements Serializable {
    /**  */
    private Long id;

    /** aCId 在启动VR时，由courseId参数传递过来  更新最高分 龚磊 */
    private String account;
    /** 类型 由VR程序自身决定 */
    private String type;
    /** 开始时间 */
    private String startDate;
    /** 结束时间 */
    private String stopDate;

    /**实验总时长 秒*/
    private Integer duration=0;
    /**使用提示总次数*/
    private Integer hintNum=0;
    /**操作错误总次数*/
    private Integer mistakeNum=0;
    /**由VR内部的评分机制生成的评分*/
    private Integer score=0;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date updateTime;
    /** 备注 */
    private String memo;
    /** 版本 V1-版本 V2-版本 */
    private String version;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getHintNum() {
        return hintNum;
    }

    public void setHintNum(Integer hintNum) {
        this.hintNum = hintNum;
    }

    public Integer getMistakeNum() {
        return mistakeNum;
    }

    public void setMistakeNum(Integer mistakeNum) {
        this.mistakeNum = mistakeNum;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}