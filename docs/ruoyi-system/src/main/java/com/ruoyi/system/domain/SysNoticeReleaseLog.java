package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公告发布日志表 AM_BIZ_TASKLOG
 *
 * @author ruoyi
 */
public class SysNoticeReleaseLog {

    private static final long serialVersionUID = 1L;

    /** ID */
    private String amTaskId;

    /** 公告ID */
    private String amBizId;

    /** 更新时间 */
    private String updateTime;

    /** 人员id */
    private String personId;

    /** 公告状态 */
    private String amState;

    /** 过程名称 */
    private String processName;

    /** 描述 */
    private String description;

    /** 编号 */
    private String num;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAmTaskId() {
        return amTaskId;
    }

    public void setAmTaskId(String amTaskId) {
        this.amTaskId = amTaskId;
    }

    public String getAmBizId() {
        return amBizId;
    }

    public void setAmBizId(String amBizId) {
        this.amBizId = amBizId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getAmState() {
        return amState;
    }

    public void setAmState(String amState) {
        this.amState = amState;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    @Override
    public String toString() {
        return "SysNoticeReleaseLog{" +
                "amTaskId='" + amTaskId + '\'' +
                ", amBizId='" + amBizId + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", personId='" + personId + '\'' +
                ", amState='" + amState + '\'' +
                ", processName='" + processName + '\'' +
                ", description='" + description + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
