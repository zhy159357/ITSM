package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class EvernRun extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /** 事件id */
    @Excel(name = "事件id")
    private String eventid  ;

    /** 事件单号 */
    @Excel(name = "事件单号")
    private String eventNumber  ;

    /** 事件描述 */
    @Excel(name = "事件描述")
    private String eventDescr  ;

    /** 应用系统 */
    @Excel(name = "应用系统")
    private String applicationSystem  ;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String eventTime  ;

    /** 事件来源 */
    @Excel(name = "事件来源")
    private String eventSource  ;

    /** 事件等级 */
    @Excel(name = "事件等级")
    private String eventLevel  ;

    /** 事件类别 */
    @Excel(name = "事件类型")
    private String eventType  ;

    /** 发生时间 */
    @Excel(name = "发生时间")
    private String occTime  ;
    /** 所属系统 */
    @Excel(name = "所属系统")
    private String eventSystem  ;
    /** 所属工作组 */
    @Excel(name = "所属工作组")
    private String workGroup  ;
    /** 处理人 */
    @Excel(name = "处理人")
    private String eventConductor  ;
    /** 联系电话 */
    @Excel(name = "联系电话")
    private String eventPhone  ;
    /** 事件类别 */
    @Excel(name = "事件类别")
    private String evnetCategory  ;
    /** 事件标题 */
    @Excel(name = "事件标题")
    private String eventTitle  ;
    /** 事件描述 */
    @Excel(name = "事件描述")
    private String eventDateil  ;
    /** 状态 */
    @Excel(name = "状态")
    private String eventStatus  ;

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(String eventNumber) {
        this.eventNumber = eventNumber;
    }

    public String getEventDescr() {
        return eventDescr;
    }

    public void setEventDescr(String eventDescr) {
        this.eventDescr = eventDescr;
    }

    public String getApplicationSystem() {
        return applicationSystem;
    }

    public void setApplicationSystem(String applicationSystem) {
        this.applicationSystem = applicationSystem;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getOccTime() {
        return occTime;
    }

    public void setOccTime(String occTime) {
        this.occTime = occTime;
    }

    public String getEventSystem() {
        return eventSystem;
    }

    public void setEventSystem(String eventSystem) {
        this.eventSystem = eventSystem;
    }

    public String getWorkGroup() {
        return workGroup;
    }

    public void setWorkGroup(String workGroup) {
        this.workGroup = workGroup;
    }

    public String getEventConductor() {
        return eventConductor;
    }

    public void setEventConductor(String eventConductor) {
        this.eventConductor = eventConductor;
    }

    public String getEventPhone() {
        return eventPhone;
    }

    public void setEventPhone(String eventPhone) {
        this.eventPhone = eventPhone;
    }

    public String getEvnetCategory() {
        return evnetCategory;
    }

    public void setEvnetCategory(String evnetCategory) {
        this.evnetCategory = evnetCategory;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDateil() {
        return eventDateil;
    }

    public void setEventDateil(String eventDateil) {
        this.eventDateil = eventDateil;
    }

    @Override
    public String toString() {
        return "EventRun{" +
                "eventid='" + eventid + '\'' +
                ", eventNumber='" + eventNumber + '\'' +
                ", eventDescr='" + eventDescr + '\'' +
                ", applicationSystem='" + applicationSystem + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", eventSource='" + eventSource + '\'' +
                ", eventLevel='" + eventLevel + '\'' +
                ", eventType='" + eventType + '\'' +
                ", occTime='" + occTime + '\'' +
                ", eventSystem='" + eventSystem + '\'' +
                ", workGroup='" + workGroup + '\'' +
                ", eventConductor='" + eventConductor + '\'' +
                ", eventPhone='" + eventPhone + '\'' +
                ", evnetCategory='" + evnetCategory + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", eventDateil='" + eventDateil + '\'' +
                ", eventStatus='" + eventStatus + '\'' +
                '}';
    }
}
