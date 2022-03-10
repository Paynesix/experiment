package com.xy.experiment.entity;

import java.io.Serializable;
import java.util.Date;

public class ExperimentUser implements Serializable {
    /**  */
    private Long id;

    /** 账户 同一个账号只存在一个是启用的，新增和修改时需要加锁 */
    private String account;

    /** 密码 密码+用户id MD5 */
    private String password;

    /** 0 无效 1 正常 2 冻结 3 锁定 4 删除 */
    private String status;

    /** 用户真实姓名 */
    private String name;

    /** 学校 */
    private String schoolName;

    /** 冻结到什么时间 定时任务在每个小时判断一次时候解冻 */
    private Date freezetime;

    /** 手机号 */
    private String phone;

    /** 账户有效期开始时间 */
    private Date validateStart;

    /** 账户有效期结束时间 */
    private Date validateEnd;

    /** 校验密码错误时间 当前时间和这个时间比，如果时间间隔小于五分钟则不更新这个时间 */
    private Date passwordErrorTime;

    /** 五分钟内密码输入错误次数 如果错误达到五次则锁定用户24小时 */
    private Integer passwordErrorNum;
    /** 用户标志 0-学生 1-管理员 */
    private Integer userTag;

    private String email;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;
    /** 备注 */
    private String memo;
    /** 版本 V1-版本 V2-版本 */
    private String version;

    private static final long serialVersionUID = 1L;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserTag() {
        return userTag;
    }

    public void setUserTag(Integer userTag) {
        this.userTag = userTag;
    }

    /**  */
    public Long getId() {
        return id;
    }

    /**  */
    public void setId(Long id) {
        this.id = id;
    }

    /** 账户 同一个账号只存在一个是启用的，新增和修改时需要加锁 */
    public String getAccount() {
        return account;
    }

    /** 账户 同一个账号只存在一个是启用的，新增和修改时需要加锁 */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /** 密码 密码+用户account MD5 */
    public String getPassword() {
        return password;
    }

    /** 密码 密码+用户account MD5 */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /** 0 无效 1 正常 2 冻结 3 锁定 4 删除 */
    public String getStatus() {
        return status;
    }

    /** 0 无效 1 正常 2 冻结 3 锁定 4 删除 */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /** 用户真实姓名 */
    public String getName() {
        return name;
    }

    /** 用户真实姓名 */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /** 学校 */
    public String getSchoolName() {
        return schoolName;
    }

    /** 学校 */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName == null ? null : schoolName.trim();
    }

    /** 冻结到什么时间 定时任务在每个小时判断一次时候解冻 */
    public Date getFreezetime() {
        return freezetime;
    }

    /** 冻结到什么时间 定时任务在每个小时判断一次时候解冻 */
    public void setFreezetime(Date freezetime) {
        this.freezetime = freezetime;
    }

    /** 手机号 */
    public String getPhone() {
        return phone;
    }

    /** 手机号 */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /** 账户有效期开始时间 */
    public Date getValidateStart() {
        return validateStart;
    }

    /** 账户有效期开始时间 */
    public void setValidateStart(Date validateStart) {
        this.validateStart = validateStart;
    }

    /** 账户有效期结束时间 */
    public Date getValidateEnd() {
        return validateEnd;
    }

    /** 账户有效期结束时间 */
    public void setValidateEnd(Date validateEnd) {
        this.validateEnd = validateEnd;
    }

    /** 校验密码错误时间 当前时间和这个时间比，如果时间间隔小于五分钟则不更新这个时间 */
    public Date getPasswordErrorTime() {
        return passwordErrorTime;
    }

    /** 校验密码错误时间 当前时间和这个时间比，如果时间间隔小于五分钟则不更新这个时间 */
    public void setPasswordErrorTime(Date passwordErrorTime) {
        this.passwordErrorTime = passwordErrorTime;
    }

    /** 五分钟内密码输入错误次数 如果错误达到五次则锁定用户24小时 */
    public Integer getPasswordErrorNum() {
        return passwordErrorNum;
    }

    /** 五分钟内密码输入错误次数 如果错误达到五次则锁定用户24小时 */
    public void setPasswordErrorNum(Integer passwordErrorNum) {
        this.passwordErrorNum = passwordErrorNum;
    }

    /** 创建时间 */
    public Date getCreateTime() {
        return createTime;
    }

    /** 创建时间 */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /** 修改时间 */
    public Date getUpdateTime() {
        return updateTime;
    }

    /** 修改时间 */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}