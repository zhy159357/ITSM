package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 故障报告记录对象 gz_fault_report_log
 * 
 * @author ruoyi
 * @date 2021-08-09
 */
public class GzFaultReportLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String gzLogId;

    /** 故障报告单ID */
    @Excel(name = "故障报告单ID")
    private String gzId;

    /** 单号 */
    @Excel(name = "单号")
    private String gzNo;

    /** 系统id */
    @Excel(name = "系统id")
    private String sysId;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String sysName;

    /** 系统级别 */
    @Excel(name = "系统级别")
    private String sysLevel;

    /** 发生时间 */
    @Excel(name = "发生时间")
    private String happenTime;

    /** 发生时段 */
    @Excel(name = "发生时段")
    private String happenTimeInterval;

    /** 影响业务时长 */
    @Excel(name = "影响业务时长")
    private String gzInfluenceBusDuration;

    /** 故障现象或影响 */
    @Excel(name = "故障现象或影响")
    private String gzAppearance;

    /** 对相关系统的影响 */
    @Excel(name = "对相关系统的影响")
    private String gzSysInfluence;

    /** 对客户服务交易影响程度（影响交易量） */
    @Excel(name = "对客户服务交易影响程度", readConverterExp = "影=响交易量")
    private String gzInfluenceDegree;

    /** 是否有资金风险 */
    @Excel(name = "是否有资金风险")
    private String ifCapitalRisk;

    /** 影响账户数或金额 */
    @Excel(name = "影响账户数或金额")
    private String gzInfluenceAccount;

    /** 影响的客户数 */
    @Excel(name = "影响的客户数")
    private String gzInfluenceCustom;

    /** 是否造成账务差错、延迟入账 */
    @Excel(name = "是否造成账务差错、延迟入账")
    private String ifErrorAccount;

    /** 是否对客户信息造成损害 */
    @Excel(name = "是否对客户信息造成损害")
    private String ifCustomDamage;

    /** 是否有客户投诉 */
    @Excel(name = "是否有客户投诉")
    private String ifCustomComplaint;

    /** 监控是否告警 */
    @Excel(name = "监控是否告警")
    private String ifMonitorAlarm;

    /** 是否已进行监控但未告警 */
    @Excel(name = "是否已进行监控但未告警")
    private String ifMonitorNotAlarm;

    /** 是否首次误报/漏报 */
    @Excel(name = "是否首次误报/漏报")
    private String ifFirstFalseAlarm;

    /** 监控告警（告警内容） */
    @Excel(name = "监控告警", readConverterExp = "告=警内容")
    private String gzMonitorMessage;

    /** 解决方法（处置措施） */
    @Excel(name = "解决方法", readConverterExp = "处=置措施")
    private String gzResolvent;

    /** 处理过程 */
    @Excel(name = "处理过程")
    private String gzDealProcess;

    /** 故障原因（简述） */
    @Excel(name = "故障原因", readConverterExp = "简=述")
    private String gzReason;

    /** 事件原因分析 */
    @Excel(name = "事件原因分析")
    private String gzEventCauseAnaly;

    /** 疑似版本（是/否） */
    @Excel(name = "疑似版本", readConverterExp = "是=/否")
    private String ifSuspectedVersion;

    /** 事件类型 */
    @Excel(name = "事件类型")
    private String eventType;

    /** 故障类型 */
    @Excel(name = "故障类型")
    private String gzType;

    /** 事件原因类型 */
    @Excel(name = "事件原因类型")
    private String eventReasonType;

    /** 是否已彻底解决 */
    @Excel(name = "是否已彻底解决")
    private String ifCompleteSolve;

    /** 整改措施 */
    @Excel(name = "整改措施")
    private String gzRectificationMeasures;

    /** 整改计划 */
    @Excel(name = "整改计划")
    private String gzRectificationPlan;

    /** 整改计划实施人员 */
    @Excel(name = "整改计划实施人员")
    private String gzRectificationPlanPerson;

    /** 是否提交问题单或建议函 */
    @Excel(name = "是否提交问题单或建议函")
    private String ifSubmitProblem;

    /** 问题单号或建议函号 */
    @Excel(name = "问题单号或建议函号")
    private String problemNo;

    /** 监控整改计划（如监控未报警填写） */
    @Excel(name = "监控整改计划", readConverterExp = "如=监控未报警填写")
    private String gzMonitorRectificationPlan;

    /** 监控整改计划实施人员（同上） */
    @Excel(name = "监控整改计划实施人员", readConverterExp = "同=上")
    private String gzMonitorRectificationPlanPerson;

    /** 创建人 */
    private String createName;

    public void setGzLogId(String gzLogId)
    {
        this.gzLogId = gzLogId;
    }

    public String getGzLogId() 
    {
        return gzLogId;
    }
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

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("gzLogId", getGzLogId())
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
            .toString();
    }
}
