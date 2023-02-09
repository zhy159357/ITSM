package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 impl_record
 * 
 * @author ruoyi
 * @date 2022-10-04
 */
public class ImplRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 实施人id */
    private String userid;



    /** 变更任务单状态 */
    private String taskStatus;

    /*变更编号*/
    @Excel(name="变更编号")
    private String changeNo;


    /*    任务编号*/

    private String changTaskNo;

    /** 变更任务单单号 */
    @Excel(name="任务编号")
    private String changeTaskNo;
    /*     实施系统*/
    @Excel(name="实施系统")
    private String effectSystem;

    /*    变更简要描述*/
    @Excel(name="变更简要描述")
    private String changeDesc;

    /*    关联任务编号*/
    @Excel(name="关联任务编号")
    private String taskNo;


    /*    变更类型*/
    @Excel(name="变更类型")
    private String changeType;

    /*    变更计划开始时间*/
    @Excel(name="变更计划开始时间")
    private String changeStartTime;

    /*    变更计划结束时间*/
    @Excel(name="变更计划结束时间")
    private String changeEndTime;

    /*    变更状态*/
    @Excel(name="变更状态")
    private String changeStatus;
    /*    发起科室*/
    @Excel(name="发起科室")
    private String startDepartment;

    /*    发起人员*/
    @Excel(name="发起人")
    private String startUser;
    /*    投产支持人员*/
    @Excel(name="投产支持人")
    private String holdUser;

    /*    任务预审人*/
    @Excel(name="任务预审人")
    private String checkUser;

    /** 投产实施人 */
    @Excel(name = "投产实施人")
    private String effectUser;

    /** 应用计划开始时间 */
    @Excel(name = "应用计划开始时间")
    private String startTime;

    /** 应用计划结束时间 */
    @Excel(name = "应用计划结束时间")
    private String endTime;

    /** 备注 */
    @Excel(name = "备注")
    private String  remarks;
    /** 是否发布 */
    private String flag;

/*    变更方式*/
    private String changeWay;


    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getChangTaskNo() {
        return changTaskNo;
    }

    public void setChangTaskNo(String changTaskNo) {
        this.changTaskNo = changTaskNo;
    }

    public String getEffectSystem() {
        return effectSystem;
    }

    public void setEffectSystem(String effectSystem) {
        this.effectSystem = effectSystem;
    }

    public String getChangeDesc() {
        return changeDesc;
    }

    public void setChangeDesc(String changeDesc) {
        this.changeDesc = changeDesc;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getChangeWay() {
        return changeWay;
    }

    public void setChangeWay(String changeWay) {
        this.changeWay = changeWay;
    }

    public String getChangeStartTime() {
        return changeStartTime;
    }

    public void setChangeStartTime(String changeStartTime) {
        this.changeStartTime = changeStartTime;
    }

    public String getChangeEndTime() {
        return changeEndTime;
    }

    public void setChangeEndTime(String changeEndTime) {
        this.changeEndTime = changeEndTime;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getStartDepartment() {
        return startDepartment;
    }

    public void setStartDepartment(String startDepartment) {
        this.startDepartment = startDepartment;
    }

    public String getStartUser() {
        return startUser;
    }

    public void setStartUser(String startUser) {
        this.startUser = startUser;
    }

    public String getHoldUser() {
        return holdUser;
    }

    public void setHoldUser(String holdUser) {
        this.holdUser = holdUser;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    public String getUserid()
    {
        return userid;
    }
    public void setChangeTaskNo(String changeTaskNo)
    {
        this.changeTaskNo = changeTaskNo;
    }

    public String getChangeTaskNo()
    {
        return changeTaskNo;
    }
    public void setTaskStatus(String taskStatus)
    {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus()
    {
        return taskStatus;
    }
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getStartTime()
    {
        return startTime;
    }
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getEndTime()
    {
        return endTime;
    }
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getRemarks()
    {
        return remarks;
    }
    public void setEffectUser(String effectUser)
    {
        this.effectUser = effectUser;
    }

    public String getEffectUser()
    {
        return effectUser;
    }
    public void setFlag(String flag)
    {
        this.flag = flag;
    }

    public String getFlag()
    {
        return flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userid", getUserid())
                .append("changeTaskNo", getChangeTaskNo())
                .append("taskStatus", getTaskStatus())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("remarks", getRemarks())
                .append("effectUser", getEffectUser())
                .append("flag", getFlag())
                .toString();
    }
}
