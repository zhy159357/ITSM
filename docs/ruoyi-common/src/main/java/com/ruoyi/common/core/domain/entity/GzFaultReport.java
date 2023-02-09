package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Word;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【故障报告单】对象 gz_fault_report
 * 
 * @author ruoyi
 * @date 2021-08-09
 */
public class GzFaultReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String gzId;

    /** 单号 */
    @Excel(name = "编号", sort = 1)
    private String gzNo;

    /** 系统id */
    private String sysId;

    /** 系统名称 */
    @Word
    @Excel(name = "应用系统名称", sort = 2)
    private String sysName;

    /** 系统级别 */
    private String sysLevel;

    /** 发生时间 */
    @Word
    @Excel(name = "发生日期", sort = 3)
    private String happenTime;

    /** 发生时段 */
    @Word
    @Excel(name = "发生时段", sort = 4)
    private String happenTimeInterval;

    /** 影响业务时长 */
    @Word
    @Excel(name = "影响业务时长", sort = 5)
    private String gzInfluenceBusDuration;

    /** 故障现象或影响 */
    @Word
    @Excel(name = "现象或影响", sort = 6)
    private String gzAppearance;

    /** 对相关系统的影响 */
    @Word
    @Excel(name = "对相关系统的影响", sort = 7)
    private String gzSysInfluence;

    /** 对客户服务交易影响程度（影响交易量） */
    @Word
    @Excel(name = "对客户服务交易影响程度", sort = 8)
    private String gzInfluenceDegree;

    /** 是否有资金风险 */
    @Excel(name = "是否有资金风险", sort = 9, readConverterExp = "1=是,0=否")
    private String ifCapitalRisk;

    /** 影响账户数或金额 */
    @Word
    @Excel(name = "影响账户数或金额", sort = 10)
    private String gzInfluenceAccount;

    /** 影响的客户数 */
    @Word
    @Excel(name = "影响的客户数", sort = 11)
    private String gzInfluenceCustom;

    /** 是否造成账务差错、延迟入账 */
    @Word(paraName = "if_imp")
    @Excel(name = "是否造成账务差错、延迟入账", sort = 12, readConverterExp = "1=是,0=否")
    private String ifErrorAccount;

    /** 是否对客户信息造成损害 */
    @Word(paraName = "if_imp")
    @Excel(name = "是否对客户信息造成损害", sort = 13, readConverterExp = "1=是,0=否")
    private String ifCustomDamage;

    /** 是否有客户投诉 */
    @Excel(name = "是否有客户投诉", sort = 14, readConverterExp = "1=是,0=否")
    private String ifCustomComplaint;

    /** 监控是否告警 */
    //@Excel(name = "监控是否告警", sort = 15, readConverterExp = "1=是,0=否")
    @Word(paraName = "if_imp")
    private String ifMonitorAlarm;

    /** 是否已进行监控但未告警 */
    //@Excel(name = "是否已进行监控但未告警", sort = 16, readConverterExp = "1=是,0=否")
    @Word(paraName = "if_imp")
    private String ifMonitorNotAlarm;

    /** 是否首次误报/漏报 */
    //@Excel(name = "是否首次误报/漏报")
    @Word(paraName = "if_imp")
    private String ifFirstFalseAlarm;

    /** 监控告警（告警内容） */
    //@Excel(name = "监控告警")
    @Word
    private String gzMonitorMessage;

    /** 解决方法（处置措施） */
    @Excel(name = "解决方法", sort = 17)
    private String gzResolvent;

    /** 处理过程 */
    @Word
    @Excel(name = "处理过程", sort = 18)
    private String gzDealProcess;

    /** 原因（简述） */
    @Excel(name = "原因（简述）", sort = 19)
    private String gzReason;

    /** 事件原因分析 */
    @Word
    @Excel(name = "事件原因分析", sort = 20)
    private String gzEventCauseAnaly;

    /** 疑似版本（是/否） */
    @Excel(name = "疑似版本", sort = 21, readConverterExp = "1=是,0=否")
    private String ifSuspectedVersion;

    /** 事件类型 */
    @Word(paraName = "gz_event_type")
    @Excel(name = "事件类型", sort = 22, readConverterExp = "1=应用系统异常,2=数据库,3=中间件,4=主机,5=网络,6=基础设施,7=维护,8=第三方影响")
    private String eventType;

    /** 应急类型 */
    @Word(paraName = "GZ_TYPE")
    @Excel(name = "应急类型", sort = 23, readConverterExp = "1=应用故障,2=非应用故障,3=第三方故障,4=应用/非应用")
    private String gzType;

    /** 事件原因类型 */
    @Word(paraName = "event_reason_type")
    @Excel(name = "事件原因类型", sort = 24, readConverterExp = "1=版本管理不到位,2=沟通管理不及时,3=配置管理不完备,4=升级方案不完善,5=升级操作不规范,6=分析设计不完善,7=代码缺陷,8=测试不充分,9=数据库,10=中间件,11=主机,12=网络,13=基础设施")
    private String eventReasonType;

    /** 是否已彻底解决 */
    @Excel(name = "是否已彻底解决", sort = 25, readConverterExp = "1=是,0=否")
    private String ifCompleteSolve;

    /** 整改措施 */
    @Word
    @Excel(name = "整改措施", sort = 26)
    private String gzRectificationMeasures;

    /** 整改计划 */
    @Word
    @Excel(name = "整改计划", sort = 27)
    private String gzRectificationPlan;

    /** 整改计划实施人员 */
    @Word
    @Excel(name = "整改计划实施人员", sort = 28)
    private String gzRectificationPlanPerson;

    /** 是否提交问题单或建议函 */
    @Word(paraName = "if_imp")
    @Excel(name = "是否提交问题单或建议函", sort = 29, readConverterExp = "1=是,0=否")
    private String ifSubmitProblem;

    /** 问题单号或建议函号 */
    @Word
    @Excel(name = "问题单号或建议函号", sort = 30)
    private String problemNo;

    /** 监控整改计划（如监控未报警填写） */
    @Word
    @Excel(name = "监控整改计划", sort = 31)
    private String gzMonitorRectificationPlan;

    /** 监控整改计划实施人员（同上） */
    @Word
    @Excel(name = "监控整改计划实施人员", sort = 33)
    private String gzMonitorRectificationPlanPerson;

    /** 应急事件单状态 */
    private String gzFaultStatus;

    /** 分析事件影响人ID */
    private String appAssessId;
    /** 事件原因分析人ID */
    private String faultAnalyId;
    /** 整改报告人ID */
    private String faultRectyId;
    /** 确认报告人ID */
    private String faultConfirmId;
    /** 应用复核人ID */
    private String appCheckId;
    /** 审核报告人ID */
    private String confirmSubmitId;

    /** 分析事件影响人 */
    private String appAssessName;
    /** 事件原因分析人 */
    private String faultAnalyName;
    /** 整改报告人 */
    private String faultRectyName;
    /** 确认报告人 */
    private String faultConfirmName;
    /** 应用复核人 */
    private String appCheckName;
    /** 审核报告人 */
    private String confirmSubmitName;
    /** 创建人 */
    @Word
    private String createName;
    /** 创建人手机号 */
    @Word
    private String createPhone;

    /** 是否需要领导确认 */
    private String ifLeaderConfirm;

    public void setGzId(String gzId)
    {
        this.gzId = gzId;
    }

    public String getGzId() 
    {
        return gzId;
    }
    public void setGzNo(String gzNo) 
    {
        this.gzNo = gzNo;
    }

    public String getGzNo() 
    {
        return gzNo;
    }
    public void setSysId(String sysId) 
    {
        this.sysId = sysId;
    }

    public String getSysId() 
    {
        return sysId;
    }
    public void setSysName(String sysName) 
    {
        this.sysName = sysName;
    }

    public String getSysName() 
    {
        return sysName;
    }
    public void setSysLevel(String sysLevel) 
    {
        this.sysLevel = sysLevel;
    }

    public String getSysLevel() 
    {
        return sysLevel;
    }
    public void setHappenTime(String happenTime) 
    {
        this.happenTime = happenTime;
    }

    public String getHappenTime() 
    {
        return happenTime;
    }
    public void setHappenTimeInterval(String happenTimeInterval) 
    {
        this.happenTimeInterval = happenTimeInterval;
    }

    public String getHappenTimeInterval() 
    {
        return happenTimeInterval;
    }
    public void setGzInfluenceBusDuration(String gzInfluenceBusDuration) 
    {
        this.gzInfluenceBusDuration = gzInfluenceBusDuration;
    }

    public String getGzInfluenceBusDuration() 
    {
        return gzInfluenceBusDuration;
    }
    public void setGzAppearance(String gzAppearance) 
    {
        this.gzAppearance = gzAppearance;
    }

    public String getGzAppearance() 
    {
        return gzAppearance;
    }
    public void setGzSysInfluence(String gzSysInfluence) 
    {
        this.gzSysInfluence = gzSysInfluence;
    }

    public String getGzSysInfluence() 
    {
        return gzSysInfluence;
    }
    public void setGzInfluenceDegree(String gzInfluenceDegree) 
    {
        this.gzInfluenceDegree = gzInfluenceDegree;
    }

    public String getGzInfluenceDegree() 
    {
        return gzInfluenceDegree;
    }
    public void setIfCapitalRisk(String ifCapitalRisk) 
    {
        this.ifCapitalRisk = ifCapitalRisk;
    }

    public String getIfCapitalRisk() 
    {
        return ifCapitalRisk;
    }
    public void setGzInfluenceAccount(String gzInfluenceAccount) 
    {
        this.gzInfluenceAccount = gzInfluenceAccount;
    }

    public String getGzInfluenceAccount() 
    {
        return gzInfluenceAccount;
    }
    public void setGzInfluenceCustom(String gzInfluenceCustom) 
    {
        this.gzInfluenceCustom = gzInfluenceCustom;
    }

    public String getGzInfluenceCustom() 
    {
        return gzInfluenceCustom;
    }
    public void setIfErrorAccount(String ifErrorAccount) 
    {
        this.ifErrorAccount = ifErrorAccount;
    }

    public String getIfErrorAccount() 
    {
        return ifErrorAccount;
    }
    public void setIfCustomDamage(String ifCustomDamage) 
    {
        this.ifCustomDamage = ifCustomDamage;
    }

    public String getIfCustomDamage() 
    {
        return ifCustomDamage;
    }
    public void setIfCustomComplaint(String ifCustomComplaint) 
    {
        this.ifCustomComplaint = ifCustomComplaint;
    }

    public String getIfCustomComplaint() 
    {
        return ifCustomComplaint;
    }
    public void setIfMonitorAlarm(String ifMonitorAlarm) 
    {
        this.ifMonitorAlarm = ifMonitorAlarm;
    }

    public String getIfMonitorAlarm() 
    {
        return ifMonitorAlarm;
    }
    public void setIfMonitorNotAlarm(String ifMonitorNotAlarm) 
    {
        this.ifMonitorNotAlarm = ifMonitorNotAlarm;
    }

    public String getIfMonitorNotAlarm() 
    {
        return ifMonitorNotAlarm;
    }
    public void setIfFirstFalseAlarm(String ifFirstFalseAlarm) 
    {
        this.ifFirstFalseAlarm = ifFirstFalseAlarm;
    }

    public String getIfFirstFalseAlarm() 
    {
        return ifFirstFalseAlarm;
    }
    public void setGzMonitorMessage(String gzMonitorMessage) 
    {
        this.gzMonitorMessage = gzMonitorMessage;
    }

    public String getGzMonitorMessage() 
    {
        return gzMonitorMessage;
    }
    public void setGzResolvent(String gzResolvent) 
    {
        this.gzResolvent = gzResolvent;
    }

    public String getGzResolvent() 
    {
        return gzResolvent;
    }
    public void setGzDealProcess(String gzDealProcess) 
    {
        this.gzDealProcess = gzDealProcess;
    }

    public String getGzDealProcess() 
    {
        return gzDealProcess;
    }
    public void setGzReason(String gzReason) 
    {
        this.gzReason = gzReason;
    }

    public String getGzReason() 
    {
        return gzReason;
    }
    public void setGzEventCauseAnaly(String gzEventCauseAnaly) 
    {
        this.gzEventCauseAnaly = gzEventCauseAnaly;
    }

    public String getGzEventCauseAnaly() 
    {
        return gzEventCauseAnaly;
    }
    public void setIfSuspectedVersion(String ifSuspectedVersion) 
    {
        this.ifSuspectedVersion = ifSuspectedVersion;
    }

    public String getIfSuspectedVersion() 
    {
        return ifSuspectedVersion;
    }
    public void setEventType(String eventType) 
    {
        this.eventType = eventType;
    }

    public String getEventType() 
    {
        return eventType;
    }
    public void setGzType(String gzType) 
    {
        this.gzType = gzType;
    }

    public String getGzType() 
    {
        return gzType;
    }
    public void setEventReasonType(String eventReasonType) 
    {
        this.eventReasonType = eventReasonType;
    }

    public String getEventReasonType() 
    {
        return eventReasonType;
    }
    public void setIfCompleteSolve(String ifCompleteSolve) 
    {
        this.ifCompleteSolve = ifCompleteSolve;
    }

    public String getIfCompleteSolve() 
    {
        return ifCompleteSolve;
    }
    public void setGzRectificationMeasures(String gzRectificationMeasures) 
    {
        this.gzRectificationMeasures = gzRectificationMeasures;
    }

    public String getGzRectificationMeasures() 
    {
        return gzRectificationMeasures;
    }
    public void setGzRectificationPlan(String gzRectificationPlan) 
    {
        this.gzRectificationPlan = gzRectificationPlan;
    }

    public String getGzRectificationPlan() 
    {
        return gzRectificationPlan;
    }
    public void setGzRectificationPlanPerson(String gzRectificationPlanPerson) 
    {
        this.gzRectificationPlanPerson = gzRectificationPlanPerson;
    }

    public String getGzRectificationPlanPerson() 
    {
        return gzRectificationPlanPerson;
    }
    public void setIfSubmitProblem(String ifSubmitProblem) 
    {
        this.ifSubmitProblem = ifSubmitProblem;
    }

    public String getIfSubmitProblem() 
    {
        return ifSubmitProblem;
    }
    public void setProblemNo(String problemNo) 
    {
        this.problemNo = problemNo;
    }

    public String getProblemNo() 
    {
        return problemNo;
    }
    public void setGzMonitorRectificationPlan(String gzMonitorRectificationPlan) 
    {
        this.gzMonitorRectificationPlan = gzMonitorRectificationPlan;
    }

    public String getGzMonitorRectificationPlan() 
    {
        return gzMonitorRectificationPlan;
    }
    public void setGzMonitorRectificationPlanPerson(String gzMonitorRectificationPlanPerson) 
    {
        this.gzMonitorRectificationPlanPerson = gzMonitorRectificationPlanPerson;
    }

    public String getGzMonitorRectificationPlanPerson() 
    {
        return gzMonitorRectificationPlanPerson;
    }

    public String getAppAssessId() {
        return appAssessId;
    }

    public void setAppAssessId(String appAssessId) {
        this.appAssessId = appAssessId;
    }

    public String getFaultAnalyId() {
        return faultAnalyId;
    }

    public void setFaultAnalyId(String faultAnalyId) {
        this.faultAnalyId = faultAnalyId;
    }

    public String getFaultRectyId() {
        return faultRectyId;
    }

    public void setFaultRectyId(String faultRectyId) {
        this.faultRectyId = faultRectyId;
    }

    public String getFaultConfirmId() {
        return faultConfirmId;
    }

    public void setFaultConfirmId(String faultConfirmId) {
        this.faultConfirmId = faultConfirmId;
    }

    public String getAppCheckId() {
        return appCheckId;
    }

    public void setAppCheckId(String appCheckId) {
        this.appCheckId = appCheckId;
    }

    public String getAppAssessName() {
        return appAssessName;
    }

    public void setAppAssessName(String appAssessName) {
        this.appAssessName = appAssessName;
    }

    public String getFaultAnalyName() {
        return faultAnalyName;
    }

    public void setFaultAnalyName(String faultAnalyName) {
        this.faultAnalyName = faultAnalyName;
    }

    public String getFaultRectyName() {
        return faultRectyName;
    }

    public void setFaultRectyName(String faultRectyName) {
        this.faultRectyName = faultRectyName;
    }

    public String getFaultConfirmName() {
        return faultConfirmName;
    }

    public void setFaultConfirmName(String faultConfirmName) {
        this.faultConfirmName = faultConfirmName;
    }

    public String getConfirmSubmitId() {
        return confirmSubmitId;
    }

    public void setConfirmSubmitId(String confirmSubmitId) {
        this.confirmSubmitId = confirmSubmitId;
    }

    public String getConfirmSubmitName() {
        return confirmSubmitName;
    }

    public void setConfirmSubmitName(String confirmSubmitName) {
        this.confirmSubmitName = confirmSubmitName;
    }

    public String getAppCheckName() {
        return appCheckName;
    }

    public void setAppCheckName(String appCheckName) {
        this.appCheckName = appCheckName;
    }

    public String getIfLeaderConfirm() {
        return ifLeaderConfirm;
    }

    public void setIfLeaderConfirm(String ifLeaderConfirm) {
        this.ifLeaderConfirm = ifLeaderConfirm;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreatePhone() {
        return createPhone;
    }

    public void setCreatePhone(String createPhone) {
        this.createPhone = createPhone;
    }

    public String getGzFaultStatus() {
        return gzFaultStatus;
    }

    public void setGzFaultStatus(String gzFaultStatus) {
        this.gzFaultStatus = gzFaultStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("gzId", getGzId())
            .append("gzNo", getGzNo())
            .append("sysId", getSysId())
            .append("sysName", getSysName())
            .append("sysLevel", getSysLevel())
            .append("happenTime", getHappenTime())
            .append("happenTimeInterval", getHappenTimeInterval())
            .append("gzInfluenceBusDuration", getGzInfluenceBusDuration())
            .append("gzAppearance", getGzAppearance())
            .append("gzSysInfluence", getGzSysInfluence())
            .append("gzInfluenceDegree", getGzInfluenceDegree())
            .append("ifCapitalRisk", getIfCapitalRisk())
            .append("gzInfluenceAccount", getGzInfluenceAccount())
            .append("gzInfluenceCustom", getGzInfluenceCustom())
            .append("ifErrorAccount", getIfErrorAccount())
            .append("ifCustomDamage", getIfCustomDamage())
            .append("ifCustomComplaint", getIfCustomComplaint())
            .append("ifMonitorAlarm", getIfMonitorAlarm())
            .append("ifMonitorNotAlarm", getIfMonitorNotAlarm())
            .append("ifFirstFalseAlarm", getIfFirstFalseAlarm())
            .append("gzMonitorMessage", getGzMonitorMessage())
            .append("gzResolvent", getGzResolvent())
            .append("gzDealProcess", getGzDealProcess())
            .append("gzReason", getGzReason())
            .append("gzEventCauseAnaly", getGzEventCauseAnaly())
            .append("ifSuspectedVersion", getIfSuspectedVersion())
            .append("eventType", getEventType())
            .append("gzType", getGzType())
            .append("eventReasonType", getEventReasonType())
            .append("ifCompleteSolve", getIfCompleteSolve())
            .append("gzRectificationMeasures", getGzRectificationMeasures())
            .append("gzRectificationPlan", getGzRectificationPlan())
            .append("gzRectificationPlanPerson", getGzRectificationPlanPerson())
            .append("ifSubmitProblem", getIfSubmitProblem())
            .append("problemNo", getProblemNo())
            .append("gzMonitorRectificationPlan", getGzMonitorRectificationPlan())
            .append("gzMonitorRectificationPlanPerson", getGzMonitorRectificationPlanPerson())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
