package com.ruoyi.common.core.domain.entity;

import java.io.Serializable;

/**
 * 排班对象 duty_substitute
 * @author dayong_sun
 * @date 2020-02-06
 */
public class DutySubstitute implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String substituteId;

    /** schedulingId */
    private String schedulingId;

    /** pid */
    private String pid;

    /** pname */
    private String pname;

    /** tid */
    private String tid;

    /** tname */
    private String tname;

    /** type_no */
    private String typeNo;

    /** type_name */
    private String typeName;

    /** 值班时间 */
    private String dutyDate;

    /** 联系电话 */
    private String status;

    /** 备注 */
    private String remark;

    private String startTime;

    private String endTime;

    public String getSubstituteId() {
        return substituteId;
    }

    public void setSubstituteId(String substituteId) {
        this.substituteId = substituteId;
    }

    public String getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(String schedulingId) {
        this.schedulingId = schedulingId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(String typeNo) {
        this.typeNo = typeNo;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
