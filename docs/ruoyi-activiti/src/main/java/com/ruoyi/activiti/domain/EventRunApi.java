package com.ruoyi.activiti.domain;

import io.swagger.annotations.ApiModelProperty;


public class EventRunApi {
    private static final long serialVersionUID = 1L;
    /**告警ID
    private String Id;
    /**
     * 事件标题
     */
    @ApiModelProperty("事件标题")
    private String eventTitle;
    /**
     * 事件描述
     */
    @ApiModelProperty("事件描述")
    private String eventDescr;
    /**
     * 应用系统编码
     */
    @ApiModelProperty("应用系统编码")
    private String appSystemCode;
    /**
     * 应用系统名称
      */
    @ApiModelProperty("应用系统名称")
    private String appSystemName;
    /**
     * 告警来源
     */
    @ApiModelProperty("告警来源")
    private String reportSource;
    /**
     * 事件来源
     */
    @ApiModelProperty("事件来源")
    private String eventSource;
    /**
     * 事件等级
     */
    @ApiModelProperty("事件等级")
    private String eventLevel;
    /**
     * 事件类型
     */
    @ApiModelProperty("事件类型")
    private String eventType;
    /**
     * 告警时间
     *
     */
    private String reportTime;
    /**
     * 所属中心
     */
    @ApiModelProperty(" 所属中心")
    private String affiliatedCenter;
    /**
     * 处理角色
     */
    @ApiModelProperty(" 处理角色")
    private String handleRoles;
    /**
     * 系统管理员
     */
    @ApiModelProperty("系统管理员")
    private String sysAttendant;
    /**
     * 网络管理员
     */
    @ApiModelProperty("网络管理员")
    private String webAttendant;
    /**
     * 应用管理员
     */
    @ApiModelProperty("应用管理员")
    private String appAttendant;
    /**
     * 请求报文
     */
    private String requestClob;

    public String getRequestClob() {
        return requestClob;
    }

    public void setRequestClob(String requestClob) {
        this.requestClob = requestClob;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescr() {
        return eventDescr;
    }

    public void setEventDescr(String eventDescr) {
        this.eventDescr = eventDescr;
    }

    public String getAppSystemCode() {
        return appSystemCode;
    }

    public void setAppSystemCode(String appSystemCode) {
        this.appSystemCode = appSystemCode;
    }

    public String getAppSystemName() {
        return appSystemName;
    }

    public void setAppSystemName(String getAppSystemName) {
        this.appSystemName = getAppSystemName;
    }

    public String getReportSource() {
        return reportSource;
    }

    public void setReportSource(String reportSource) {
        this.reportSource = reportSource;
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
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

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getAffiliatedCenter() {
        return affiliatedCenter;
    }

    public void setAffiliatedCenter(String affiliatedCenter) {
        this.affiliatedCenter = affiliatedCenter;
    }

    public String getHandleRoles() {
        return handleRoles;
    }

    public void setHandleRoles(String handleRoles) {
        this.handleRoles = handleRoles;
    }

    public String getSysAttendant() {
        return sysAttendant;
    }

    public void setSysAttendant(String sysAttendant) {
        this.sysAttendant = sysAttendant;
    }

    public String getWebAttendant() {
        return webAttendant;
    }

    public void setWebAttendant(String webAttendant) {
        this.webAttendant = webAttendant;
    }

    public String getAppAttendant() {
        return appAttendant;
    }

    public void setAppAttendant(String appAttendant) {
        this.appAttendant = appAttendant;
    }
}