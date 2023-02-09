package com.ruoyi.form.domain;

import lombok.Data;
import lombok.ToString;

/**
 * 接口发起事件单传输对象
 */
@Data
@ToString
public class EventForeignVo {
    /** 上报部门 */
    private String reportOrg;
    /** 上报人 */
    private String reportPerson;

    /** 上报电话 */
    private String reportPhone;

    /** 上报人邮箱地址 */
    private String reportEmail;

    /** 上报人账号 */
    private String reportAccount;

    /** 事件标题 */
    private String eventTitle;

    /** 事件详述 */
    private String eventInfo;

    /** 客户信息 */
    private String customInfo;

    /** 来源 01-监控；02-柜面；03-客服 */
    private String sourceType;

    /** 受派组 */
    private String assignedGroup;
    private String assignedGroupName;

    /** 受派人 */
    private String assignedPerson;
    private String assignedPersonName;

    /** 告警id */
    private String alarmId;

    /** 批量标签 */
    private String batchLabel;

    /** 事件单编号 */
    private String eventNo;

    /** 事件优先级 */
    private String eventLevel;
}
