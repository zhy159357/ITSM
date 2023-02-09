package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 【 实体: 作业表】对象 sm_biz_task
 * 
 * @author ruoyi
 * @date 2021-01-12
 */
public class SmBizTask implements Serializable
{
    private static final long serialVersionUID = 1L;


    private String taskId;//任务ID


    private String schedulingId;//计划单ID


    private String taskNo;//任务编号


    private String performUserName;//当前处理人名称


    private String handlerDeptName;//任务处理部门名称


    private String taskTitle;//任务标题


    private String taskDescription;//任务描述


    private String taskEffectDate;//生效日期


    private String taskFailureDate;//失效日期


    private String character;//执行频度


    private String taskTypeId;//任务类型


    private String startTime;//开始时间-执行日期


    private String taskHoursWorked;//任务工作时间


    private String taskCycle;//任务周期


    private String taskFrequency;//任务频率

    private String updateTime;//更新时间

    private String performTime;//执行时间


    private String invalidationMark;//无效标志


    private String workStatus;//工作状态

    private String createTime;//创建时间


    private String dutyPostName;//值班岗位名称


    private String process;//进度过程


    private String checkerId;//审查人


    private String performUserId;//执行人员id


    private String handlerDeptId;//执行机构id


    private String dutyPostId;//值班岗位id


    private String hour;//小时


    private String min;//分钟


    private String day;//天数



    private String performWeeks;//执行工作周


    private String performMonths;//执行工作月


    private String performDay;//执行工作日


    private String createOrgId;//创建机构id

    /** 是否发送短信 1:是 ； 2:否	 */
    private String msgDoor;//信息

    /** 提前的小时 */
    private String msgHour;//信息小时

    /** 提前的分钟 */
    private String msgMin;//信息分钟

    /**二期新增字段*/
    private String sendRange;//发送范围：机构或者工作组


    private String performDeptId;//处理机构


    private String performDeptName;//处理机构名称


    private String performGroupId;//处理工作组ID


    private String performGroupName;//处理工作组名称


    private String receiveRoleId;//接收角色ID


    private String receiveRoleName;//接收角色名称



    public String getPerformTime() {
        return performTime;
    }

    public void setPerformTime(String performTime) {
        this.performTime = performTime;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDutyPostName() {
        return dutyPostName;
    }

    public void setDutyPostName(String dutyPostName) {
        this.dutyPostName = dutyPostName;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getPerformUserId() {
        return performUserId;
    }

    public void setPerformUserId(String performUserId) {
        this.performUserId = performUserId;
    }

    public String getHandlerDeptId() {
        return handlerDeptId;
    }

    public void setHandlerDeptId(String handlerDeptId) {
        this.handlerDeptId = handlerDeptId;
    }

    public String getDutyPostId() {
        return dutyPostId;
    }

    public void setDutyPostId(String dutyPostId) {
        this.dutyPostId = dutyPostId;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getPerformWeeks() {
        return performWeeks;
    }

    public void setPerformWeeks(String performWeeks) {
        this.performWeeks = performWeeks;
    }

    public String getPerformMonths() {
        return performMonths;
    }

    public void setPerformMonths(String performMonths) {
        this.performMonths = performMonths;
    }

    public String getPerformDay() {
        return performDay;
    }

    public void setPerformDay(String performDay) {
        this.performDay = performDay;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getMsgDoor() {
        return msgDoor;
    }

    public void setMsgDoor(String msgDoor) {
        this.msgDoor = msgDoor;
    }

    public String getMsgHour() {
        return msgHour;
    }

    public void setMsgHour(String msgHour) {
        this.msgHour = msgHour;
    }

    public String getMsgMin() {
        return msgMin;
    }

    public void setMsgMin(String msgMin) {
        this.msgMin = msgMin;
    }

    public String getSendRange() {
        return sendRange;
    }

    public void setSendRange(String sendRange) {
        this.sendRange = sendRange;
    }

    public String getPerformDeptName() {
        return performDeptName;
    }

    public void setPerformDeptName(String performDeptName) {
        this.performDeptName = performDeptName;
    }

    public String getPerformDeptId() {
        return performDeptId;
    }

    public void setPerformDeptId(String performDeptId) {
        this.performDeptId = performDeptId;
    }

    public String getPerformGroupId() {
        return performGroupId;
    }

    public void setPerformGroupId(String performGroupId) {
        this.performGroupId = performGroupId;
    }

    public String getPerformGroupName() {
        return performGroupName;
    }

    public void setPerformGroupName(String performGroupName) {
        this.performGroupName = performGroupName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getReceiveRoleName() {
        return receiveRoleName;
    }

    public void setReceiveRoleName(String receiveRoleName) {
        this.receiveRoleName = receiveRoleName;
    }

    public String getReceiveRoleId() {
        return receiveRoleId;
    }

    public void setReceiveRoleId(String receiveRoleId) {
        this.receiveRoleId = receiveRoleId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(String schedulingId) {
        this.schedulingId = schedulingId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getPerformUserName() {
        return performUserName;
    }

    public void setPerformUserName(String performUserName) {
        this.performUserName = performUserName;
    }

    public String getHandlerDeptName() {
        return handlerDeptName;
    }

    public void setHandlerDeptName(String handlerDeptName) {
        this.handlerDeptName = handlerDeptName;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskEffectDate() {
        return taskEffectDate;
    }

    public void setTaskEffectDate(String taskEffectDate) {
        this.taskEffectDate = taskEffectDate;
    }

    public String getTaskFailureDate() {
        return taskFailureDate;
    }

    public void setTaskFailureDate(String taskFailureDate) {
        this.taskFailureDate = taskFailureDate;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTaskHoursWorked() {
        return taskHoursWorked;
    }

    public void setTaskHoursWorked(String taskHoursWorked) {
        this.taskHoursWorked = taskHoursWorked;
    }

    public String getTaskCycle() {
        return taskCycle;
    }

    public void setTaskCycle(String taskCycle) {
        this.taskCycle = taskCycle;
    }

    public String getTaskFrequency() {
        return taskFrequency;
    }

    public void setTaskFrequency(String taskFrequency) {
        this.taskFrequency = taskFrequency;
    }

    @Override
    public String toString() {
        return "SmBizTask{" +
                "performTime='" + performTime + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", workStatus='" + workStatus + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", dutyPostName='" + dutyPostName + '\'' +
                ", process='" + process + '\'' +
                ", checkerId='" + checkerId + '\'' +
                ", performUserId='" + performUserId + '\'' +
                ", handlerDeptId='" + handlerDeptId + '\'' +
                ", dutyPostId='" + dutyPostId + '\'' +
                ", hour='" + hour + '\'' +
                ", min='" + min + '\'' +
                ", performWeeks='" + performWeeks + '\'' +
                ", performMonths='" + performMonths + '\'' +
                ", performDay='" + performDay + '\'' +
                ", createOrgId='" + createOrgId + '\'' +
                ", msgDoor='" + msgDoor + '\'' +
                ", msgHour='" + msgHour + '\'' +
                ", msgMin='" + msgMin + '\'' +
                ", sendRange='" + sendRange + '\'' +
                ", performDeptName='" + performDeptName + '\'' +
                ", performDeptId='" + performDeptId + '\'' +
                ", performGroupId='" + performGroupId + '\'' +
                ", performGroupName='" + performGroupName + '\'' +
                ", day='" + day + '\'' +
                ", receiveRoleName='" + receiveRoleName + '\'' +
                ", receiveRoleId='" + receiveRoleId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", schedulingId='" + schedulingId + '\'' +
                ", taskNo='" + taskNo + '\'' +
                ", performUserName='" + performUserName + '\'' +
                ", handlerDeptName='" + handlerDeptName + '\'' +
                ", taskTitle='" + taskTitle + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskEffectDate='" + taskEffectDate + '\'' +
                ", taskFailureDate='" + taskFailureDate + '\'' +
                ", character='" + character + '\'' +
                ", taskTypeId='" + taskTypeId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", taskHoursWorked='" + taskHoursWorked + '\'' +
                ", taskCycle='" + taskCycle + '\'' +
                ", taskFrequency='" + taskFrequency + '\'' +
                '}';
    }
}
