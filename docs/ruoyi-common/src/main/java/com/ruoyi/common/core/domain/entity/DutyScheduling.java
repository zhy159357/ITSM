package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;

/**
 * 排班对象 duty_scheduling
 * @author dayong_sun
 * @date 2020-01-19
 */
public class DutyScheduling implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String schedulingId;

    /** pid */
    private String pid;

    /** 姓名 */
    @Excel(name = "姓名",width = 30)
    private String pname;

    /** 联系电话 */
    @Excel(name = "联系电话", cellType = Excel.ColumnType.STRING,width = 30)
    private String mobilephone;

    /** 值班日期 */
    @Excel(name = "值班日期(yyyy/mm/dd)",dateFormat = "yyyy-MM-dd",width = 32)
    private String dutyDate;
    /** 值班时间 */
    @Excel(name = "值班时间(白班/夜班)",width = 30)
    private String timeShift;

    /** 指挥大厅 */
    private String commandHall;

    /** 类别编码 */
    @Excel(name = "类别编码(类别设置)", cellType = Excel.ColumnType.NUMERIC,width = 32)
    private String typeNo;

    /** 类别名称 */
    private String typeName;

    /** 值班人数 */
    private String dutyNumber;

    /** 类别id */
    private String typeinfoId;

    /** 类别id */
    private String parentId;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    /** 主值类别 */
    private String leader;

    /** accountPid */
    private String accountPid;

    /** accountName */
    private String accountName;

    /** id */
    private String id;

    /** orgname */
    private String orgname;

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

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
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

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getTimeShift() {
        return timeShift;
    }

    public String getCommandHall() {
        return commandHall;
    }

    public void setCommandHall(String commandHall) {
        this.commandHall = commandHall;
    }

    public void setTimeShift(String timeShift) {
        this.timeShift = timeShift;
    }

    public String getDutyNumber() {
        return dutyNumber;
    }

    public void setDutyNumber(String dutyNumber) {
        this.dutyNumber = dutyNumber;
    }

    public String getTypeinfoId() {
        return typeinfoId;
    }

    public void setTypeinfoId(String typeinfoId) {
        this.typeinfoId = typeinfoId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getAccountPid() {
        return accountPid;
    }

    public void setAccountPid(String accountPid) {
        this.accountPid = accountPid;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
