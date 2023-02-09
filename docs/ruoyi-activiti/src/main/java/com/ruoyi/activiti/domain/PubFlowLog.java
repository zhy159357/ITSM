package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程记录对象 pub_flow_log
 * 
 * @author ruoyi
 * @date 2020-12-25
 */
public class PubFlowLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事件单日志id */
    private String logId;

    /** 业务单ID */
    @Excel(name = "业务单ID")
    private String bizId;

    /** 流程类型(KEY) */
    @Excel(name = "流程类型(KEY)")
    private String logType;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 操作人 */
    @Excel(name = "操作人")
    private String performerId;

    /** 操作组ID */
    @Excel(name = "操作组ID")
    private String performerGroupId;

    /** 操作机构ID */
    @Excel(name = "操作机构ID")
    private String performerOrgId;

    /** 操作人名称 */
    @Excel(name = "操作人名称")
    private String performerName;

    /** 操作人联系方式 */
    @Excel(name = "操作人联系方式")
    private String performerTel;

    /** 操作组名 */
    @Excel(name = "操作组名")
    private String performerGroupName;

    /** 操作机构名 */
    @Excel(name = "操作机构名")
    private String performerOrgName;

    /** 操作时间 */
    @Excel(name = "操作时间")
    private String performerTime;

    /** 操作名称 */
    @Excel(name = "操作名称")
    private String performName;

    /** 操作描述 */
    @Excel(name = "操作描述")
    private String performDesc;

    /** 下一步流程ID */
    @Excel(name = "下一步流程ID")
    private String nextTaskId;

    /** 下一步流程任务名称 */
    @Excel(name = "下一步流程任务名称")
    private String nextTaskName;

    /** 下一步处理人 */
    @Excel(name = "下一步处理人")
    private String nextPerformerDesc;

    /** 下一步流程联系方式 */
    @Excel(name = "下一步流程联系方式")
    private String nextPerformerTel;

    /** 步骤处理用时 */
    @Excel(name = "步骤处理用时")
    private String sysResidenceTime;

    /** 状态 */
    @Excel(name = "状态")
    private String currentState;

    /** 序号 */
    @Excel(name = "序号")
    private String serialNum;
    /**值班账号**/
    private String dutyAccount;
    /**值班账号ID **/
    private String dutyId;

    public String getDutyAccount() {
        return dutyAccount;
    }

    public void setDutyAccount(String dutyAccount) {
        this.dutyAccount = dutyAccount;
    }

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public void setLogId(String logId) 
    {
        this.logId = logId;
    }

    public String getLogId() 
    {
        return logId;
    }
    public void setBizId(String bizId) 
    {
        this.bizId = bizId;
    }

    public String getBizId() 
    {
        return bizId;
    }
    public void setLogType(String logType) 
    {
        this.logType = logType;
    }

    public String getLogType() 
    {
        return logType;
    }
    public void setTaskName(String taskName) 
    {
        this.taskName = taskName;
    }

    public String getTaskName() 
    {
        return taskName;
    }
    public void setPerformerId(String performerId) 
    {
        this.performerId = performerId;
    }

    public String getPerformerId() 
    {
        return performerId;
    }
    public void setPerformerGroupId(String performerGroupId) 
    {
        this.performerGroupId = performerGroupId;
    }

    public String getPerformerGroupId() 
    {
        return performerGroupId;
    }
    public void setPerformerOrgId(String performerOrgId) 
    {
        this.performerOrgId = performerOrgId;
    }

    public String getPerformerOrgId() 
    {
        return performerOrgId;
    }
    public void setPerformerName(String performerName) 
    {
        this.performerName = performerName;
    }

    public String getPerformerName() 
    {
        return performerName;
    }
    public void setPerformerTel(String performerTel) 
    {
        this.performerTel = performerTel;
    }

    public String getPerformerTel() 
    {
        return performerTel;
    }
    public void setPerformerGroupName(String performerGroupName) 
    {
        this.performerGroupName = performerGroupName;
    }

    public String getPerformerGroupName() 
    {
        return performerGroupName;
    }
    public void setPerformerOrgName(String performerOrgName) 
    {
        this.performerOrgName = performerOrgName;
    }

    public String getPerformerOrgName() 
    {
        return performerOrgName;
    }
    public void setPerformerTime(String performerTime) 
    {
        this.performerTime = performerTime;
    }

    public String getPerformerTime() 
    {
        return performerTime;
    }
    public void setPerformName(String performName) 
    {
        this.performName = performName;
    }

    public String getPerformName() 
    {
        return performName;
    }
    public void setPerformDesc(String performDesc) 
    {
        this.performDesc = performDesc;
    }

    public String getPerformDesc() 
    {
        return performDesc;
    }
    public void setNextTaskId(String nextTaskId) 
    {
        this.nextTaskId = nextTaskId;
    }

    public String getNextTaskId() 
    {
        return nextTaskId;
    }
    public void setNextTaskName(String nextTaskName) 
    {
        this.nextTaskName = nextTaskName;
    }

    public String getNextTaskName() 
    {
        return nextTaskName;
    }
    public void setNextPerformerDesc(String nextPerformerDesc) 
    {
        this.nextPerformerDesc = nextPerformerDesc;
    }

    public String getNextPerformerDesc() 
    {
        return nextPerformerDesc;
    }
    public void setNextPerformerTel(String nextPerformerTel) 
    {
        this.nextPerformerTel = nextPerformerTel;
    }

    public String getNextPerformerTel() 
    {
        return nextPerformerTel;
    }
    public void setSysResidenceTime(String sysResidenceTime) 
    {
        this.sysResidenceTime = sysResidenceTime;
    }

    public String getSysResidenceTime() 
    {
        return sysResidenceTime;
    }
    public void setCurrentState(String currentState) 
    {
        this.currentState = currentState;
    }

    public String getCurrentState() 
    {
        return currentState;
    }
    public void setSerialNum(String serialNum) 
    {
        this.serialNum = serialNum;
    }

    public String getSerialNum() 
    {
        return serialNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("bizId", getBizId())
            .append("logType", getLogType())
            .append("taskName", getTaskName())
            .append("performerId", getPerformerId())
            .append("performerGroupId", getPerformerGroupId())
            .append("performerOrgId", getPerformerOrgId())
            .append("performerName", getPerformerName())
            .append("performerTel", getPerformerTel())
            .append("performerGroupName", getPerformerGroupName())
            .append("performerOrgName", getPerformerOrgName())
            .append("performerTime", getPerformerTime())
            .append("performName", getPerformName())
            .append("performDesc", getPerformDesc())
            .append("nextTaskId", getNextTaskId())
            .append("nextTaskName", getNextTaskName())
            .append("nextPerformerDesc", getNextPerformerDesc())
            .append("nextPerformerTel", getNextPerformerTel())
            .append("sysResidenceTime", getSysResidenceTime())
            .append("currentState", getCurrentState())
            .append("serialNum", getSerialNum())
            .toString();
    }
}
