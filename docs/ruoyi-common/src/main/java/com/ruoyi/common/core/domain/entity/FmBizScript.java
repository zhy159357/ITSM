package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【业务事件单自动化脚本记录表】对象 fm_biz_script
 *
 * @author liul
 * @date 2021-03-30
 */
public class FmBizScript extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String fbsId;

    /**
     * 业务事件单ID
     */
    private String fmId;

    /**
     * 执行人ID
     */
    private String executorId;
    private String executorName;
    /**
     * 执行工作组ID
     */
    private String executorGroupid;
    private String executorGroupName;

    /**
     * 执行结果
     */
    private String executorResult;

    /**
     * 执行状态
     */
    private String resultStatus;

    /**
     * 脚本名称
     */
    private String scriptName;

    /**
     * 脚本参数
     */
    private String scriptPara;

    /**
     * 脚本ID
     */
    private String scriptId;

    /**
     * 执行时间
     */
    private String executorTime;

    /**
     * 执行结束时间
     */
    private String executorEndTime;

    private String flowId;

    private String scriptVersion;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScriptVersion() {
        return scriptVersion;
    }

    public void setScriptVersion(String scriptVersion) {
        this.scriptVersion = scriptVersion;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getExecutorEndTime() {
        return executorEndTime;
    }

    public void setExecutorEndTime(String executorEndTime) {
        this.executorEndTime = executorEndTime;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getExecutorGroupName() {
        return executorGroupName;
    }

    public void setExecutorGroupName(String executorGroupName) {
        this.executorGroupName = executorGroupName;
    }

    public void setFbsId(String fbsId) {
        this.fbsId = fbsId;
    }

    public String getFbsId() {
        return fbsId;
    }

    public void setFmId(String fmId) {
        this.fmId = fmId;
    }

    public String getFmId() {
        return fmId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorGroupid(String executorGroupid) {
        this.executorGroupid = executorGroupid;
    }

    public String getExecutorGroupid() {
        return executorGroupid;
    }

    public void setExecutorResult(String executorResult) {
        this.executorResult = executorResult;
    }

    public String getExecutorResult() {
        return executorResult;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptPara(String scriptPara) {
        this.scriptPara = scriptPara;
    }

    public String getScriptPara() {
        return scriptPara;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setExecutorTime(String executorTime) {
        this.executorTime = executorTime;
    }

    public String getExecutorTime() {
        return executorTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fbsId", getFbsId())
                .append("fmId", getFmId())
                .append("executorId", getExecutorId())
                .append("executorGroupid", getExecutorGroupid())
                .append("executorResult", getExecutorResult())
                .append("resultStatus", getResultStatus())
                .append("scriptName", getScriptName())
                .append("scriptPara", getScriptPara())
                .append("scriptId", getScriptId())
                .append("executorTime", getExecutorTime())
                .toString();
    }
}
