package com.xy.experiment.vo;

import java.io.Serializable;
import java.util.Date;

public class UserVo implements Serializable {

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

    private String email;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private static final long serialVersionUID = 1L;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

}