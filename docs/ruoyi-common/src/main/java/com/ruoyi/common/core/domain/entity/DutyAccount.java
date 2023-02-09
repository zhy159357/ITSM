package com.ruoyi.common.core.domain.entity;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * 参数列别对象 duty_account
 * @author dayong_sun
 * @date 2021-03-15
 */
public class DutyAccount implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;
    /** accountPid */
    private String accountPid;
    /** pid */
    private String pid;
    /** username */
    private String username;
    /** password */
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountPid() {
        return accountPid;
    }

    public void setAccountPid(String accountPid) {
        this.accountPid = accountPid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
