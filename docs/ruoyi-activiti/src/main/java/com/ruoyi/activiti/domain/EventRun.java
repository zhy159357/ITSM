package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 运行事件单对象 event_run
 *
 * @author zx
 * @date 2021-03-04
 */
public class EventRun
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String eventId;

    /** 事件标题 */
    @Excel(name = "事件标题")
    private String eventTitle;

    /** 单号 */
    @Excel(name = "单号")
    private String eventNo;
    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createTime;
    /** 事件来源 */
    @Excel(name="事件来源")
    private String eventSource;
    /** 应用系统名称 */
    @Excel(name = "应用系统名称")
    private String appSystemName;
    /** 事件等级0一般，1急，2紧急（告警级别：1-警告、2-错误、3-紧急） */
    @Excel(name = "事件等级")
    private String eventLevel;
    /** 事件类型（0-应用、1-系统、2-网络） */
    @Excel(name = "事件类型")
    private String eventType;
    /**状态 **/
    @Excel(name="事件状态")
    private String status;
    /** 关闭时间 */
    @Excel(name = "关闭时间")
    private String closeTime;

    /** 事件描述 */
    private String eventDescr;

    /** 应用系统编码 */
    private String appSystemCode;
    /** 告警来源 */
    private String reportSource;

    /** 发生时间 */
    private String reportTime;

    /** 所属中心--所属中心（0-合肥中心、1-亦庄中心、2-丰台中心） */
    private String affiliatedCenter;

    /** 处理角色-0-网络告警派单（合肥）1-网络告警派单（亦庄）2-网络告警派单（丰台）3-系统告警派单（合肥）4-系统告警派单（亦庄）5-系统告警派单（丰台）6-应用告警派单7-告警派单（无所属中心） */
    private String handleRoles;

    /** 知识库知识ID */
    private String sysAttendant;

    /** 知识库知识标题 */
    private String webAttendant;

    /** 应用管理员 */
    private String appAttendant;

    /** 处理人 */
    private String handlePerson;

    /** 处理时间 */
    private String handleTime;

    /** 报警原因 */
    private String reportReason;

    /** 处理描述 */
    @Excel(name = "处理描述")
    private String handleDescr;

    /** 创建人 */
    private String createId;
    /** 请求报文**/
    private String requestClob;
    /** 负责人 **/
    private String chargePerson;
    /**预计恢复时间**/
    private String anticipateTime;
    /**补充说明 **/
    private String supplementExplain;
    /**值班账号**/
    private String dutyAccount;
    /** 请求参数 */
    private Map<String, Object> params=new HashMap<>();

    public String getDutyAccount() {
        return dutyAccount;
    }

    public void setDutyAccount(String dutyAccount) {
        this.dutyAccount = dutyAccount;
    }

    public String getChargePerson() {
        return chargePerson;
    }

    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson;
    }

    public String getAnticipateTime() {
        return anticipateTime;
    }

    public void setAnticipateTime(String anticipateTime) {
        this.anticipateTime = anticipateTime;
    }

    public String getSupplementExplain() {
        return supplementExplain;
    }

    public void setSupplementExplain(String supplementExplain) {
        this.supplementExplain = supplementExplain;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestClob() {
        return requestClob;
    }

    public void setRequestClob(String requestClob) {
        this.requestClob = requestClob;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public String getEventId()
    {
        return eventId;
    }
    public void setEventTitle(String eventTitle)
    {
        this.eventTitle = eventTitle;
    }

    public String getEventTitle()
    {
        return eventTitle;
    }
    public void setEventNo(String eventNo)
    {
        this.eventNo = eventNo;
    }

    public String getEventNo()
    {
        return eventNo;
    }
    public void setCloseTime(String closeTime)
    {
        this.closeTime = closeTime;
    }

    public String getCloseTime()
    {
        return closeTime;
    }
    public void setEventDescr(String eventDescr)
    {
        this.eventDescr = eventDescr;
    }

    public String getEventDescr()
    {
        return eventDescr;
    }
    public void setAppSystemCode(String appSystemCode)
    {
        this.appSystemCode = appSystemCode;
    }

    public String getAppSystemCode()
    {
        return appSystemCode;
    }
    public void setAppSystemName(String appSystemName)
    {
        this.appSystemName = appSystemName;
    }

    public String getAppSystemName()
    {
        return appSystemName;
    }
    public void setReportSource(String reportSource)
    {
        this.reportSource = reportSource;
    }

    public String getReportSource()
    {
        return reportSource;
    }
    public void setEventSource(String eventSource)
    {
        this.eventSource = eventSource;
    }

    public String getEventSource()
    {
        return eventSource;
    }
    public void setEventLevel(String eventLevel)
    {
        this.eventLevel = eventLevel;
    }

    public String getEventLevel()
    {
        return eventLevel;
    }
    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    public String getEventType()
    {
        return eventType;
    }
    public void setReportTime(String reportTime)
    {
        this.reportTime = reportTime;
    }

    public String getReportTime()
    {
        return reportTime;
    }
    public void setAffiliatedCenter(String affiliatedCenter)
    {
        this.affiliatedCenter = affiliatedCenter;
    }

    public String getAffiliatedCenter()
    {
        return affiliatedCenter;
    }
    public void setHandleRoles(String handleRoles)
    {
        this.handleRoles = handleRoles;
    }

    public String getHandleRoles()
    {
        return handleRoles;
    }
    public void setSysAttendant(String sysAttendant)
    {
        this.sysAttendant = sysAttendant;
    }

    public String getSysAttendant()
    {
        return sysAttendant;
    }
    public void setWebAttendant(String webAttendant)
    {
        this.webAttendant = webAttendant;
    }

    public String getWebAttendant()
    {
        return webAttendant;
    }
    public void setAppAttendant(String appAttendant)
    {
        this.appAttendant = appAttendant;
    }

    public String getAppAttendant()
    {
        return appAttendant;
    }
    public void setHandlePerson(String handlePerson)
    {
        this.handlePerson = handlePerson;
    }

    public String getHandlePerson()
    {
        return handlePerson;
    }
    public void setHandleTime(String handleTime)
    {
        this.handleTime = handleTime;
    }

    public String getHandleTime()
    {
        return handleTime;
    }
    public void setReportReason(String reportReason)
    {
        this.reportReason = reportReason;
    }

    public String getReportReason()
    {
        return reportReason;
    }
    public void setHandleDescr(String handleDescr)
    {
        this.handleDescr = handleDescr;
    }

    public String getHandleDescr()
    {
        return handleDescr;
    }
    public void setCreateId(String createId)
    {
        this.createId = createId;
    }

    public String getCreateId()
    {
        return createId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("eventId", getEventId())
                .append("eventTitle", getEventTitle())
                .append("eventNo", getEventNo())
                .append("createTime", getCreateTime())
                .append("closeTime", getCloseTime())
                .append("eventDescr", getEventDescr())
                .append("appSystemCode", getAppSystemCode())
                .append("appSystemName", getAppSystemName())
                .append("reportSource", getReportSource())
                .append("eventSource", getEventSource())
                .append("eventLevel", getEventLevel())
                .append("eventType", getEventType())
                .append("reporTtime", getReportTime())
                .append("affiliatedCenter", getAffiliatedCenter())
                .append("handleRoles", getHandleRoles())
                .append("sysAttendant", getSysAttendant())
                .append("webAttendant", getWebAttendant())
                .append("appAttendant", getAppAttendant())
                .append("handlePerson", getHandlePerson())
                .append("handleTime", getHandleTime())
                .append("reportReason", getReportReason())
                .append("handleDescr", getHandleDescr())
                .append("createId", getCreateId())
                .toString();
    }
}