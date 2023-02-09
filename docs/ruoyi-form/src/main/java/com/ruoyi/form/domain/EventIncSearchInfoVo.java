package com.ruoyi.form.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventIncSearchInfoVo {
	private String transDate;// 上报时间
	private String systemName;// 应用系统
	private String imCode;// IM事件编号
	private String eventResolution;// 解决方案
	private String evenTenvaluation;// 满意度评价
	private String evenTile;// 事件标题
	private String evenTdesc;// 事件情况描述
	private String evenHeadBranch;// 总分行事件标识
	private String eventCenterNet;// 中心侧/网点侧事件标识
	private String eventPriority;// 事件优先级
	private String eventFactor;// 事件影响面
	private String eventLevel;// 事件级别
	private String eventAssigneegrp;// 事件当前受派组
	private String eventAssignee;// 事件当前受派人
	private String eventStatus;// 事件状态
	private String eventcCloseCode;// 关闭代码
	private String eventReporttime;// 事件开启时间
	private String eventResolvetime;// 事件解决时间
	private String eventCloseTime;// 事件关闭时间
	private String eventPendmark;// 挂起标识
	private String eventCancelFlag;// 客户撤单标识
    private String eventCancelTime;// 客户撤单时间
    private String eventExpeditedFlag;// 客户加急标识Y-是；N-否
}
