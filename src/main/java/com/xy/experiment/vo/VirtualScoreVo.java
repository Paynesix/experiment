package com.xy.experiment.vo;

import java.io.Serializable;

/**
 * 虚拟实验分数
 */
public class VirtualScoreVo implements Serializable {

    private String username; //用户名
    private String projectTitle; //实验名称
    private String childProjectTitle; //子实验名称 可选，适用于一个实验中包含多个子实验项目
    private Integer status; //实验结果 1：完成；2：未完成
    private Integer score; //实验成绩 0 ~100，百分制
    private String startDate; //实验开始时间 13位时间戳
    private String endDate; //实验结束时间 13位时间戳
    private Integer timeUsed; //实验用时 分钟
    private String issuerId; //接入平台编号 由“实验空间”分配给实验教学项目的编号
    private Integer attachmentId; //实验报告（PDF、DOC等） 通过附件上传服务获取到的附件ID
    private String account; // 学生账号
    private String demo;

    private static final long serialVersionUID = 1L;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getChildProjectTitle() {
        return childProjectTitle;
    }

    public void setChildProjectTitle(String childProjectTitle) {
        this.childProjectTitle = childProjectTitle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(Integer timeUsed) {
        this.timeUsed = timeUsed;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }
}