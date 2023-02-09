package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【实体: 计划表】对象 sm_biz_scheduling
 * 
 * @author ruoyi
 * @date 2021-01-12
 */
public class SmBizScheduling implements Serializable
{
    private static final long serialVersionUID = 1L;


    private String schedulingId;//计划表ID

    @Excel(name = "计划单编号",sort = 1)
    private String schedulingNo;//计划表编号

    @Excel(name = "计划单标题",sort = 2)
    private String schedulingName;//计划表名称

    private String schedulingDescription;//计划表描述

    @Excel(name = "创建时间",sort = 8)
    private String createTime;//创建时间


    @Excel(name = "计划发布时间",sort = 3)
    private String effectiveTime;//有效时间


    private String creatorId;//创建人id


    private String creatorDeptId;//创建机构id


    private String recheckMark;//复查标记


    private String recheckerId;//复查人id


    private String taskNum;//任务编号


    private String updateTime;//更新时间


    private String invalidationMark;//无效标记

    @Excel(name = "计划状态",sort = 5,
            readConverterExp = "01=新建,02=待审核,03=退回待提交,04=待发布,05=已发布,06=完成,07=已关闭")
    /**  01-新建,02-待审核,03-审核通过,04-审核不通过,05-关闭 ,06-退回待修改 */
    private String planStatus;//计划状态

    @Excel(name = "完成进度",sort = 4)
    private String process;//过程


    private String planType;//计划类型


    private String checkPersonId;//审核人id

    @Excel(name = "审核人",sort = 7)
    private String checkPersonName;//审核人名称


    private String checkDescription;//检查描述


    private String masterOrgId;//主机构id


    private String checkTime;//检查时间

    /** 审核是否通过的相关信息 true通过  false不通过 */
    private String planCheckpass;

    /** '0':'未注销过','1':'已注销过'*/
    private String cancelFlag;//删除标记


    private String folder;//所属文件夹

    /**
     * 创建人名称
     */
    @Excel(name = "创建人",sort = 6)
    private String createName;

    private String orgName;// 机构名称


    private String taskTypeId;//任务类型

    /** 是否发送短信 1:是 ； 2:否	 */
    private String msgDoor;//信息

    private String sendRange;//发送范围：机构或者工作组

    private String startTime;

    private String performDate;//执行日期--计划

    private String label;

    private String workGroup;//工作组

    private String flag;//提交标识

    private String endTime;//创建至时间（关闭页面）

    private String endEffTime;//发布至时间（关闭页面）

    /** 请求参数 */
    private Map<String, Object> params;

    //延期表参数  原因
    private String reason;

    //延期表参数  延期时间
    private String releaseTime;

    //延期表参数  处长审核人id
    private String checkId;

    /**延期表参数 处长审核意见 */
    private String ctorChecktext;

    /**延期表参数 计划审核人审核意见 */
    private String jhChecktext;

    //申请延期创建人
    private String delayCreateName;

    //申请延期创建开始时间
    private String delayCreateTime;

    //申请延期创建结束时间
    private String delayEndTime;

    //数据库类型
    private String  dbType;

    public String getDelayCreateName() {
        return delayCreateName;
    }

    public void setDelayCreateName(String delayCreateName) {
        this.delayCreateName = delayCreateName;
    }

    public String getDelayCreateTime() {
        return delayCreateTime;
    }

    public void setDelayCreateTime(String delayCreateTime) {
        this.delayCreateTime = delayCreateTime;
    }

    public String getDelayEndTime() {
        return delayEndTime;
    }

    public void setDelayEndTime(String delayEndTime) {
        this.delayEndTime = delayEndTime;
    }

    /**
     * 例行变更计划id集合
     */
    private List<String> ids;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getEndEffTime() {
        return endEffTime;
    }

    public void setEndEffTime(String endEffTime) {
        this.endEffTime = endEffTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getWorkGroup() {
        return workGroup;
    }

    public void setWorkGroup(String workGroup) {
        this.workGroup = workGroup;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPerformDate() {
        return performDate;
    }

    public void setPerformDate(String performDate) {
        this.performDate = performDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }



    public String getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(String schedulingId) {
        this.schedulingId = schedulingId;
    }

    public String getSchedulingNo() {
        return schedulingNo;
    }

    public void setSchedulingNo(String schedulingNo) {
        this.schedulingNo = schedulingNo;
    }

    public String getSchedulingName() {
        return schedulingName;
    }

    public void setSchedulingName(String schedulingName) {
        this.schedulingName = schedulingName;
    }

    public String getSchedulingDescription() {
        return schedulingDescription;
    }

    public void setSchedulingDescription(String schedulingDescription) {
        this.schedulingDescription = schedulingDescription;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorDeptId() {
        return creatorDeptId;
    }

    public void setCreatorDeptId(String creatorDeptId) {
        this.creatorDeptId = creatorDeptId;
    }

    public String getRecheckMark() {
        return recheckMark;
    }

    public void setRecheckMark(String recheckMark) {
        this.recheckMark = recheckMark;
    }

    public String getRecheckerId() {
        return recheckerId;
    }

    public void setRecheckerId(String recheckerId) {
        this.recheckerId = recheckerId;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
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

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getCheckPersonId() {
        return checkPersonId;
    }

    public void setCheckPersonId(String checkPersonId) {
        this.checkPersonId = checkPersonId;
    }

    public String getCheckPersonName() {
        return checkPersonName;
    }

    public void setCheckPersonName(String checkPersonName) {
        this.checkPersonName = checkPersonName;
    }

    public String getCheckDescription() {
        return checkDescription;
    }

    public void setCheckDescription(String checkDescription) {
        this.checkDescription = checkDescription;
    }

    public String getMasterOrgId() {
        return masterOrgId;
    }

    public void setMasterOrgId(String masterOrgId) {
        this.masterOrgId = masterOrgId;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getPlanCheckpass() {
        return planCheckpass;
    }

    public void setPlanCheckpass(String planCheckpass) {
        this.planCheckpass = planCheckpass;
    }

    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getMsgDoor() {
        return msgDoor;
    }

    public void setMsgDoor(String msgDoor) {
        this.msgDoor = msgDoor;
    }

    public String getSendRange() {
        return sendRange;
    }

    public void setSendRange(String sendRange) {
        this.sendRange = sendRange;
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

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getCtorChecktext() {
        return ctorChecktext;
    }

    public void setCtorChecktext(String ctorChecktext) {
        this.ctorChecktext = ctorChecktext;
    }

    public String getJhChecktext() {
        return jhChecktext;
    }

    public void setJhChecktext(String jhChecktext) {
        this.jhChecktext = jhChecktext;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    @Override
    public String toString() {
        return "SmBizScheduling{" +
                "schedulingId='" + schedulingId + '\'' +
                ", schedulingNo='" + schedulingNo + '\'' +
                ", schedulingName='" + schedulingName + '\'' +
                ", schedulingDescription='" + schedulingDescription + '\'' +
                ", createTime='" + createTime + '\'' +
                ", effectiveTime='" + effectiveTime + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", creatorDeptId='" + creatorDeptId + '\'' +
                ", recheckMark='" + recheckMark + '\'' +
                ", recheckerId='" + recheckerId + '\'' +
                ", taskNum='" + taskNum + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", planStatus='" + planStatus + '\'' +
                ", process='" + process + '\'' +
                ", planType='" + planType + '\'' +
                ", checkPersonId='" + checkPersonId + '\'' +
                ", checkPersonName='" + checkPersonName + '\'' +
                ", checkDescription='" + checkDescription + '\'' +
                ", masterOrgId='" + masterOrgId + '\'' +
                ", checkTime='" + checkTime + '\'' +
                ", planCheckpass='" + planCheckpass + '\'' +
                ", cancelFlag='" + cancelFlag + '\'' +
                ", folder='" + folder + '\'' +
                ", createName='" + createName + '\'' +
                ", orgName='" + orgName + '\'' +
                ", taskTypeId='" + taskTypeId + '\'' +
                ", msgDoor='" + msgDoor + '\'' +
                ", sendRange='" + sendRange + '\'' +
                '}';
    }
}
