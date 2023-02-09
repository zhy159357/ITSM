package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * 值班人员对象 DutyPerson
 * @author dayong_sun
 * @date 2020-01-19
 */

public class DutyLog implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;

    /** 值班人员唯一标识 */
    private String userId;

    /** 值班人员姓名 */
    private String userName;

    /** 值班人员手机 */
    private String userPhone;

    /** 值班日志 */
    private String logContent;

    /** 日志提交时间 */
    private String logTime;

    /** 日志修改时间 */
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
