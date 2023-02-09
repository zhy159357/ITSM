package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;

public class OgReport {

    private String reportId;
    @Excel(name = "来电人员机构")
    private String crewSetting;
    @Excel(name = "系统名称")
    private String systemName;
    @Excel(name = "业务人员培训不到位")
    private String trainNoplace;
    @Excel(name = "业务人员操作有误")
    private String serviceOperate;
    @Excel(name = "系统功能不完善或不满足")
    private String faultinessDissatisfy;
    @Excel(name = "省内系统原因")
    private String systemCause;
    @Excel(name = "行内关联系统原因")
    private String lnlineIssue;
    @Excel(name = "数据一致性问题")
    private String dateIssue;
    @Excel(name = "信息系统原因")
    private String messageIssue;
    @Excel(name = "系统程序BUG")
    private String systemBug;
    @Excel(name = "版本或者变更引起")
    private String versionsChange;
    @Excel(name = "客户操作问题")
    private String clientIssue;
    @Excel(name = "第三方或他行系统")
    private String thirdParty;
    @Excel(name = "系统分析相关资讯")
    private String systemMessage;
    @Excel(name = "其他或疑难")
    private String elseKnotty;
    @Excel(name = "基础问题咨询")
    private String issueMessage;
    @Excel(name = "转事件单")
    private String turnIncident;
    @Excel(name = "小计")
    private String smallPlan;
    @Excel(name = "占比")
    private String holdContrast;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getCrewSetting() {
        return crewSetting;
    }

    public void setCrewSetting(String crewSetting) {
        this.crewSetting = crewSetting;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getTrainNoplace() {
        return trainNoplace;
    }

    public void setTrainNoplace(String trainNoplace) {
        this.trainNoplace = trainNoplace;
    }

    public String getServiceOperate() {
        return serviceOperate;
    }

    public void setServiceOperate(String serviceOperate) {
        this.serviceOperate = serviceOperate;
    }

    public String getFaultinessDissatisfy() {
        return faultinessDissatisfy;
    }

    public void setFaultinessDissatisfy(String faultinessDissatisfy) {
        this.faultinessDissatisfy = faultinessDissatisfy;
    }

    public String getSystemCause() {
        return systemCause;
    }

    public void setSystemCause(String systemCause) {
        this.systemCause = systemCause;
    }

    public String getLnlineIssue() {
        return lnlineIssue;
    }

    public void setLnlineIssue(String lnlineIssue) {
        this.lnlineIssue = lnlineIssue;
    }

    public String getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(String dateIssue) {
        this.dateIssue = dateIssue;
    }

    public String getMessageIssue() {
        return messageIssue;
    }

    public void setMessageIssue(String messageIssue) {
        this.messageIssue = messageIssue;
    }

    public String getSystemBug() {
        return systemBug;
    }

    public void setSystemBug(String systemBug) {
        this.systemBug = systemBug;
    }

    public String getVersionsChange() {
        return versionsChange;
    }

    public void setVersionsChange(String versionsChange) {
        this.versionsChange = versionsChange;
    }

    public String getClientIssue() {
        return clientIssue;
    }

    public void setClientIssue(String clientIssue) {
        this.clientIssue = clientIssue;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public String getElseKnotty() {
        return elseKnotty;
    }

    public void setElseKnotty(String elseKnotty) {
        this.elseKnotty = elseKnotty;
    }

    public String getIssueMessage() {
        return issueMessage;
    }

    public void setIssueMessage(String issueMessage) {
        this.issueMessage = issueMessage;
    }

    public String getTurnIncident() {
        return turnIncident;
    }

    public void setTurnIncident(String turnIncident) {
        this.turnIncident = turnIncident;
    }

    public String getSmallPlan() {
        return smallPlan;
    }

    public void setSmallPlan(String smallPlan) {
        this.smallPlan = smallPlan;
    }

    public String getHoldContrast() {
        return holdContrast;
    }

    public void setHoldContrast(String holdContrast) {
        this.holdContrast = holdContrast;
    }
}
