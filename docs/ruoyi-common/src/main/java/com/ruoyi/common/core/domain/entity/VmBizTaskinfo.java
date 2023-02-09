package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 版本任务对象 vm_biz_taskinfo
 * 
 * @author ruoyi
 * @date 2021-01-06
 */
public class VmBizTaskinfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务序号ID  */
    private String taskId;

    /** 版本申请单ID   */
    private String versionInfoId;

    /** 任务名称 */
    private String taskName;

    /** 任务类型 */
    private String taskTypeId;

    /** 任务状态  */
    @Excel(name = "任务状态",sort = 6,readConverterExp = "1=待下载,2=已下载,3=已完成")
    private String taskStatus;

    /** 任务内容  */
    private String taskContent;

    /** 任务处理组 */
    private String taskDealGroupId;

    /** 任务处理人  */
    private String taskDealUserId;
    @Excel(name = "附件下载人",sort = 7)
    private String taskDealUserName;

    /** 前置任务序号 */
    private String frontTaskId;

    /** 后续任务序号 */
    private String followTaskId;

    /** 预计开始时间 */
    private String estStartDate;

    /** 预计完成时间 */
    @Excel(name = "版本下载时间",sort = 9)
    private String estFinishDate;

    /** 任务处理结果 */
    private String taskDealResult;

    /** 实际开始时间 */
    @Excel(name = "升级时间",sort = 3)
    private String actualStartTime;

    /** 实际完成时间 */
    private String actualFinishTime;

    /** 无效标记 */
    private String invalidationMark;

    /** 任务处理时间  */
    private String taskproducetime;

    /** 修改时间  */
    @Excel(name = "确认操作时间",sort = 10)
    private String update_time;

    /** 机构代码 */
    private String org;

    /** 任务编号 */
    @Excel(name = "任务单号",sort = 1)
    private String taskNo;

    private String userId;

    private String manPhone;

    /** 短信回复标志 0未回复 1已回复 */
    private String messageFlag;

    private String completeUserId;
    @Excel(name = "确认完成人",sort = 8)
    private String completeUserName;

    private VmBizInfo vmBizInfo;

    private OgOrg ogOrg;

    /**
     * 以下三个字段版本单号｜版本简称｜所属单位
     */
    @Excel(name = "版本单号",sort = 2)
    private String versionInfoNo;
    @Excel(name = "版本简称",sort = 4)
    private String versionInfoName;
    @Excel(name = "所属单位",sort = 5)
    private String orgName;

    /** 是否引发重大故障 */
    @Excel(name = "引发生产故障",sort = 11,readConverterExp = "1=是,2=否")
    private String ifBigFault;
    /** 故障情况描述 */
    @Excel(name = "故障情况描述",sort = 12)
    private String upgradeMemo;

    /** 升级结果填报 */
    @Excel(name = "升级结果填报",sort = 13,readConverterExp = "1=完成,2=回退,3=其他异常")
    private String ifResultSmoothly;
    /** 异常或回退内容 */
    @Excel(name = "异常或回退内容",sort = 14)
    private String backExceptionMemo;
    /** 升级次日开发是否现场支持 */
    @Excel(name = "现场支持",sort = 15,readConverterExp = "1=是,2=否")
    private String ifSupport;

    /** 升级支持地点 */
    @Excel(name = "升级支持地点",sort = 16)
    private String supportAddress;
    /** 故障情况描述 */
    @Excel(name = "升级支持人员",sort = 17)
    private String supportPhone;

    public void setTaskId(String taskId) 
    {
        this.taskId = taskId;
    }

    public String getTaskId() 
    {
        return taskId;
    }
    public void setVersionInfoId(String versionInfoId) 
    {
        this.versionInfoId = versionInfoId;
    }

    public String getVersionInfoId() 
    {
        return versionInfoId;
    }
    public void setTaskName(String taskName) 
    {
        this.taskName = taskName;
    }

    public String getTaskName() 
    {
        return taskName;
    }
    public void setTaskTypeId(String taskTypeId) 
    {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskTypeId() 
    {
        return taskTypeId;
    }
    public void setTaskStatus(String taskStatus) 
    {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() 
    {
        return taskStatus;
    }
    public void setTaskContent(String taskContent) 
    {
        this.taskContent = taskContent;
    }

    public String getTaskContent() 
    {
        return taskContent;
    }
    public void setTaskDealGroupId(String taskDealGroupId) 
    {
        this.taskDealGroupId = taskDealGroupId;
    }

    public String getTaskDealGroupId() 
    {
        return taskDealGroupId;
    }
    public void setTaskDealUserId(String taskDealUserId) 
    {
        this.taskDealUserId = taskDealUserId;
    }

    public String getTaskDealUserId() 
    {
        return taskDealUserId;
    }
    public void setFrontTaskId(String frontTaskId) 
    {
        this.frontTaskId = frontTaskId;
    }

    public String getFrontTaskId() 
    {
        return frontTaskId;
    }
    public void setFollowTaskId(String followTaskId) 
    {
        this.followTaskId = followTaskId;
    }

    public String getFollowTaskId() 
    {
        return followTaskId;
    }
    public void setEstStartDate(String estStartDate) 
    {
        this.estStartDate = estStartDate;
    }

    public String getEstStartDate() 
    {
        return estStartDate;
    }
    public void setEstFinishDate(String estFinishDate) 
    {
        this.estFinishDate = estFinishDate;
    }

    public String getEstFinishDate() 
    {
        return estFinishDate;
    }
    public void setTaskDealResult(String taskDealResult) 
    {
        this.taskDealResult = taskDealResult;
    }

    public String getTaskDealResult() 
    {
        return taskDealResult;
    }
    public void setActualStartTime(String actualStartTime) 
    {
        this.actualStartTime = actualStartTime;
    }

    public String getActualStartTime() 
    {
        return actualStartTime;
    }
    public void setActualFinishTime(String actualFinishTime) 
    {
        this.actualFinishTime = actualFinishTime;
    }

    public String getActualFinishTime() 
    {
        return actualFinishTime;
    }
    public void setInvalidationMark(String invalidationMark) 
    {
        this.invalidationMark = invalidationMark;
    }

    public String getInvalidationMark() 
    {
        return invalidationMark;
    }
    public void setTaskproducetime(String taskproducetime) 
    {
        this.taskproducetime = taskproducetime;
    }

    public String getTaskproducetime() 
    {
        return taskproducetime;
    }
    public void setOrg(String org) 
    {
        this.org = org;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getOrg()
    {
        return org;
    }
    public void setTaskNo(String taskNo) 
    {
        this.taskNo = taskNo;
    }

    public String getTaskNo() 
    {
        return taskNo;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setManPhone(String manPhone) 
    {
        this.manPhone = manPhone;
    }

    public String getManPhone() 
    {
        return manPhone;
    }
    public void setMessageFlag(String messageFlag) 
    {
        this.messageFlag = messageFlag;
    }

    public String getMessageFlag() 
    {
        return messageFlag;
    }
    public void setCompleteUserId(String completeUserId) 
    {
        this.completeUserId = completeUserId;
    }

    public String getCompleteUserId() 
    {
        return completeUserId;
    }

    public VmBizInfo getVmBizInfo() {
        return vmBizInfo;
    }

    public void setVmBizInfo(VmBizInfo vmBizInfo) {
        if(vmBizInfo != null){
            this.versionInfoNo = vmBizInfo.getVersionInfoNo();
            this.versionInfoName = vmBizInfo.getVersionInfoName();
        }
        this.vmBizInfo = vmBizInfo;
    }

    public OgOrg getOgOrg() {
        return ogOrg;
    }

    public void setOgOrg(OgOrg ogOrg) {
        if(ogOrg != null){
            this.orgName = ogOrg.getOrgName();
        }
        this.ogOrg = ogOrg;
    }

    public String getTaskDealUserName() {
        return taskDealUserName;
    }

    public void setTaskDealUserName(String taskDealUserName) {
        this.taskDealUserName = taskDealUserName;
    }

    public String getCompleteUserName() {
        return completeUserName;
    }

    public void setCompleteUserName(String completeUserName) {
        this.completeUserName = completeUserName;
    }

    public String getVersionInfoNo() {
        return versionInfoNo;
    }

    public void setVersionInfoNo(String versionInfoNo) {
        this.versionInfoNo = versionInfoNo;
    }

    public String getVersionInfoName() {
        return versionInfoName;
    }

    public void setVersionInfoName(String versionInfoName) {
        this.versionInfoName = versionInfoName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getIfBigFault() {
        return ifBigFault;
    }

    public void setIfBigFault(String ifBigFault) {
        this.ifBigFault = ifBigFault;
    }

    public String getUpgradeMemo() {
        return upgradeMemo;
    }

    public void setUpgradeMemo(String upgradeMemo) {
        this.upgradeMemo = upgradeMemo;
    }

    public String getIfResultSmoothly() {
        return ifResultSmoothly;
    }

    public void setIfResultSmoothly(String ifResultSmoothly) {
        this.ifResultSmoothly = ifResultSmoothly;
    }

    public String getIfSupport() {
        return ifSupport;
    }

    public void setIfSupport(String ifSupport) {
        this.ifSupport = ifSupport;
    }

    public String getSupportAddress() {
        return supportAddress;
    }

    public void setSupportAddress(String supportAddress) {
        this.supportAddress = supportAddress;
    }

    public String getSupportPhone() {
        return supportPhone;
    }

    public void setSupportPhone(String supportPhone) {
        this.supportPhone = supportPhone;
    }

    public String getBackExceptionMemo() {
        return backExceptionMemo;
    }

    public void setBackExceptionMemo(String backExceptionMemo) {
        this.backExceptionMemo = backExceptionMemo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("taskId", getTaskId())
            .append("versionInfoId", getVersionInfoId())
            .append("taskName", getTaskName())
            .append("taskTypeId", getTaskTypeId())
            .append("taskStatus", getTaskStatus())
            .append("taskContent", getTaskContent())
            .append("taskDealGroupId", getTaskDealGroupId())
            .append("taskDealUserId", getTaskDealUserId())
            .append("frontTaskId", getFrontTaskId())
            .append("followTaskId", getFollowTaskId())
            .append("estStartDate", getEstStartDate())
            .append("estFinishDate", getEstFinishDate())
            .append("taskDealResult", getTaskDealResult())
            .append("actualStartTime", getActualStartTime())
            .append("actualFinishTime", getActualFinishTime())
            .append("updateTime", getUpdateTime())
            .append("invalidationMark", getInvalidationMark())
            .append("taskproducetime", getTaskproducetime())
            .append("org", getOrg())
            .append("taskNo", getTaskNo())
            .append("userId", getUserId())
            .append("manPhone", getManPhone())
            .append("messageFlag", getMessageFlag())
            .append("completeUserId", getCompleteUserId())
            .toString();
    }
}
