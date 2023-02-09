package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 【请填写功能名称】对象 sm_biz_taskinfo
 * 
 * @author ruoyi
 * @date 2021-01-12
 */
public class SmBizTaskinfo implements Serializable
{
    private static final long serialVersionUID = 1L;


    private String taskFormId;//任务ID


    private String taskId;//关联作业

    @Excel(name = "任务编号",sort = 1)
    private String taskFromNo;//任务编号


    private String publishTime;//任务发布时间


    private String taskPerformTime;//任务执行时间

    @Excel(name = "创建时间",sort = 3)
    private String generateTime;//任务单生成时间

    @Excel(name = "任务状态",sort = 2,
            readConverterExp = "01=未发布,02=未填报,03=已填报")
    private String taskFormStatus;//任务单信息表状态


    private String updateTime;//更新时间


    private String invalidationMark;//无效标志

    @Excel(name = "执行日期",sort = 4)
    private String performDate;//执行日期--计划


    private String excuteDate;//计划任务日期-实际


    private String excuteTime;//执行任务时间-实际


    private String performDeptId;//任务处理机构


    private String performUserId;//任务处理人id


    private String performDutyId;//任务处理值班


    private String checkerId;//审核人


    private String excuteDescription;//任务处理描述


    private String inspectDescription;//检查描述


    private String workBeginTime;//处理开始时间


    private String workEndTime;//处理结束时间


    private String relateChange;//关联变更单号


    private String performGroupId;//任务处理工作组


    private String isChange;//是否关联变更


    private String change;//关联变更


    private SmBizScheduling smBizScheduling =new SmBizScheduling();//计划对象

    private SmBizTask smBizTask  ;//任务对象

    private String endPublishTime;//结束发布发时间

    private String endworkBeginTime;//结束任务填报时间

    private String grpname;//工作组名称

    private String ogoname;//机构名称

    private String endgenerateTime;//任务单结束时间

    private String writeTimeStart;//填报时间（进展情况）

    private String writeTimeEnd;//填报至时间(进展情况)

    private Map<String, Object> params;//请求参数

    private String performUserName;//处理人

    private String planStartDate;//计划时间（填报页面）

    private String planEndDate;//计划至时间（填报页面）

    private String isWrite;//是否有人填报  0 否  1 是

    /**文件路径*/
    private String filePath;
    /**文件名*/
    private String fileName;

    //数据库类型
    private String  dbType;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIsWrite() {
        return isWrite;
    }

    public void setIsWrite(String isWrite) {
        this.isWrite = isWrite;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    public String getPerformUserName() {
        return performUserName;
    }

    public void setPerformUserName(String performUserName) {
        this.performUserName = performUserName;
    }

    public String getWriteTimeStart() {
        return writeTimeStart;
    }

    public void setWriteTimeStart(String writeTimeStart) {
        this.writeTimeStart = writeTimeStart;
    }

    public String getWriteTimeEnd() {
        return writeTimeEnd;
    }

    public void setWriteTimeEnd(String writeTimeEnd) {
        this.writeTimeEnd = writeTimeEnd;
    }

    public String getEndgenerateTime() {
        return endgenerateTime;
    }

    public void setEndgenerateTime(String endgenerateTime) {
        this.endgenerateTime = endgenerateTime;
    }




    public String getEndworkBeginTime() {
        return endworkBeginTime;
    }

    public void setEndworkBeginTime(String endworkBeginTime) {
        this.endworkBeginTime = endworkBeginTime;
    }

    public String getGrpname() {
        return grpname;
    }

    public void setGrpname(String grpname) {
        this.grpname = grpname;
    }

    public String getOgoname() {
        return ogoname;
    }

    public void setOgoname(String ogoname) {
        this.ogoname = ogoname;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
    public String getEndPublishTime() {
        return endPublishTime;
    }

    public void setEndPublishTime(String endPublishTime) {
        this.endPublishTime = endPublishTime;
    }

    public SmBizScheduling getSmBizScheduling() {
        if(smBizScheduling==null){
            return new SmBizScheduling();
        }
        return smBizScheduling;
    }

    public void setSmBizScheduling(SmBizScheduling smBizScheduling) {
        if(this.smBizScheduling==null){
            this.smBizScheduling=new SmBizScheduling();
        }
        this.smBizScheduling = smBizScheduling;
    }

    public SmBizTask getSmBizTask() {
        return smBizTask;
    }

    public void setSmBizTask(SmBizTask smBizTask) {
        this.smBizTask = smBizTask;
    }

    public String getTaskFormId() {
        return taskFormId;
    }

    public void setTaskFormId(String taskFormId) {
        this.taskFormId = taskFormId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskFromNo() {
        return taskFromNo;
    }

    public void setTaskFromNo(String taskFromNo) {
        this.taskFromNo = taskFromNo;
    }

    public String getTaskPerformTime() {
        return taskPerformTime;
    }

    public void setTaskPerformTime(String taskPerformTime) {
        this.taskPerformTime = taskPerformTime;
    }

    public String getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(String generateTime) {
        this.generateTime = generateTime;
    }

    public String getTaskFormStatus() {
        return taskFormStatus;
    }

    public void setTaskFormStatus(String taskFormStatus) {
        this.taskFormStatus = taskFormStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getPerformDate() {
        return performDate;
    }

    public void setPerformDate(String performDate) {
        this.performDate = performDate;
    }

    public String getExcuteDate() {
        return excuteDate;
    }

    public void setExcuteDate(String excuteDate) {
        this.excuteDate = excuteDate;
    }

    public String getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(String excuteTime) {
        this.excuteTime = excuteTime;
    }

    public String getPerformDeptId() {
        return performDeptId;
    }

    public void setPerformDeptId(String performDeptId) {
        this.performDeptId = performDeptId;
    }

    public String getPerformUserId() {
        return performUserId;
    }

    public void setPerformUserId(String performUserId) {
        this.performUserId = performUserId;
    }

    public String getPerformDutyId() {
        return performDutyId;
    }

    public void setPerformDutyId(String performDutyId) {
        this.performDutyId = performDutyId;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getExcuteDescription() {
        return excuteDescription;
    }

    public void setExcuteDescription(String excuteDescription) {
        this.excuteDescription = excuteDescription;
    }

    public String getInspectDescription() {
        return inspectDescription;
    }

    public void setInspectDescription(String inspectDescription) {
        this.inspectDescription = inspectDescription;
    }

    public String getWorkBeginTime() {
        return workBeginTime;
    }

    public void setWorkBeginTime(String workBeginTime) {
        this.workBeginTime = workBeginTime;
    }

    public String getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }

    public String getRelateChange() {
        return relateChange;
    }

    public void setRelateChange(String relateChange) {
        this.relateChange = relateChange;
    }

    public String getPerformGroupId() {
        return performGroupId;
    }

    public void setPerformGroupId(String performGroupId) {
        this.performGroupId = performGroupId;
    }

    public String getIsChange() {
        return isChange;
    }

    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    @Override
    public String toString() {
        return "SmBizTaskinfo{" +
                "taskFormId='" + taskFormId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskFromNo='" + taskFromNo + '\'' +
                ", taskPerformTime='" + taskPerformTime + '\'' +
                ", generateTime='" + generateTime + '\'' +
                ", taskFormStatus='" + taskFormStatus + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", performDate='" + performDate + '\'' +
                ", excuteDate='" + excuteDate + '\'' +
                ", excuteTime='" + excuteTime + '\'' +
                ", performDeptId='" + performDeptId + '\'' +
                ", performUserId='" + performUserId + '\'' +
                ", performDutyId='" + performDutyId + '\'' +
                ", checkerId='" + checkerId + '\'' +
                ", excuteDescription='" + excuteDescription + '\'' +
                ", inspectDescription='" + inspectDescription + '\'' +
                ", workBeginTime='" + workBeginTime + '\'' +
                ", workEndTime='" + workEndTime + '\'' +
                ", relateChange='" + relateChange + '\'' +
                ", performGroupId='" + performGroupId + '\'' +
                ", isChange='" + isChange + '\'' +
                ", change='" + change + '\'' +
                ", publishTime='" + publishTime + '\'' +
                '}';
    }
}
