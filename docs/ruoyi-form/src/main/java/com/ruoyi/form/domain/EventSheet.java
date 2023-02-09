package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 事件单对象 event_sheet
 *
 * @author zc
 * @date 2022-06-07
 */
@Data
public class EventSheet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流程实例id */
    private String instanceId;

    /** id */
    private Long Id;

    /** 事件单id */
    private String eventId;

    /** 事件单编号 */
    @Excel(name = "事件单编号")
    private String eventNo;

    /*************** 发起环节start ***************/
    /** 上报部门 */
    @Excel(name = "上报部门")
    private String reportOrg;

    /** 上报人 */
    @Excel(name = "上报人")
    private String reportPerson;

    /** 上报电话 */
    @Excel(name = "上报电话")
    private String reportPhone;

    /** 事件标题 */
    @Excel(name = "事件标题")
    private String eventTitle;

    /** 事件详述 */
    @Excel(name = "事件详述")
    private String eventInfo;

    /** 客户信息 */
    @Excel(name = "客户信息")
    private String customInfo;

    /** 总分行事件标记 0-总行；1-分行 */
    @Excel(name = "总分行事件标记 0-总行；1-分行")
    private String orgFlag;

    /** 中心侧/网点侧事件 0-中心测事件；1-网点测事件 */
    @Excel(name = "中心侧/网点侧事件 0-中心测事件；1-网点测事件")
    private String sideFlag;

    /** 类别 */
    @Excel(name = "类别")
    private String eventCategory;
    @TableField(exist = false)
    private String eventCategoryName;

    /** 子类 */
    @Excel(name = "子类")
    private String eventSubclass;
    @TableField(exist = false)
    private String eventSubclassName;

    /** 条目 */
    @Excel(name = "条目")
    private String eventEntry;
    @TableField(exist = false)
    private String eventEntryName;

    /** 子条目 */
    @Excel(name = "子条目")
    private String eventSubentry;
    @TableField(exist = false)
    private String eventSubentryName;

    /** 来源 */
    @Excel(name = "来源")
    private String eventSource;

    /** 影响程度 */
    @Excel(name = "影响程度")
    private String infLevel;

    /** 影响范围 */
    @Excel(name = "影响范围")
    private String infRange;

    /** 优先级 */
    @Excel(name = "优先级")
    private String eventPriority;

    /** 目标解决日期 */
    @Excel(name = "目标解决日期")
    private String targetResolveDate;

    /** 一级 */
    @Excel(name = "一级")
    private String eventFirstType;
    @TableField(exist = false)
    private String eventFirstTypeName;

    /** 二级 */
    @Excel(name = "二级")
    private String eventSecondType;
    @TableField(exist = false)
    private String eventSecondTypeName;

    /** 三级 */
    @Excel(name = "三级")
    private String eventThreeType;
    @TableField(exist = false)
    private String eventThreeTypeName;

    /** 是否加急，0-否；1-是 */
    @Excel(name = "是否加急，0-否；1-是")
    private String urgentFlag;

    /** 是否财务类事件 */
    @Excel(name = "是否财务类事件")
    private String financeFlag;
    /*************** 发起环节end ***************/

    /*************** 分派环节start ***************/
    /** 受派组 */
    @Excel(name = "受派组")
    private String assignedGroup;
    @TableField(exist = false)
    private String assignedGroupName;

    /** 受派人 */
    @Excel(name = "受派人")
    private String assignedPerson;
    @TableField(exist = false)
    private String assignedPersonName;

    /** 状态 */
    @Excel(name = "状态")
    private String eventStatus;
    @TableField(exist = false)
    private String eventStatusName;

    /** 事件分类 */
    @Excel(name = "事件分类")
    private String eventType;

    /** 事件助手处理结果 */
    @Excel(name = "事件助手处理结果")
    private String eventHelperResult;

    /** 事件自愈处理结果 */
    @Excel(name = "事件自愈处理结果")
    private String eventHealResult;

    /** 解决方案 */
    @Excel(name = "解决方案")
    private String eventSolution;

    /** 事件原因类型（一级） */
    @Excel(name = "事件原因类型（一级）")
    private String eventReasonType;

    /** 事件原因（二级） */
    @Excel(name = "事件原因（二级）")
    private String eventReason;

    /** 受影响的系统 */
    @Excel(name = "受影响的系统")
    private String infSystem;

    /** 实际故障系统 */
    @Excel(name = "实际故障系统")
    private String actualFaultSystem;

    /** 故障知识点 */
    @Excel(name = "故障知识点")
    private String faultKnowPoint;

    /** 影响面 */
    @Excel(name = "影响面")
    private String infFace;

    /** 二线部门 */
    @Excel(name = "二线部门")
    private String secondOrg;

    /** 二线处理部门 */
    @Excel(name = "二线处理部门")
    private String secondDealOrg;

    /** 二线处理人员 */
    @Excel(name = "二线处理人员")
    private String secondDealPerson;

    /** 二线解决方案是否有效 0-否；1-是 */
    @Excel(name = "二线解决方案是否有效 0-否；1-是")
    private String solutionValidFlag;

    /** 是否数据质量问题 0-否；1-是 */
    @Excel(name = "是否数据质量问题 0-否；1-是")
    private String dataQualityFlag;

    /** 关闭代码  */
    @Excel(name = "关闭代码")
    private String closeCode;

    /** 事件级别 */
    @Excel(name = "事件级别")
    private String eventLevel;

    /** 关联的变更、需求号 */
    @Excel(name = "关联的变更、需求号")
    private String changeNo;

    /** 关联的问题 */
    @Excel(name = "关联的问题")
    private String problemNo;

    /** 历史事件单id */
    private String historyEventId;

    /** IT服务台分派人 */
    private String assignId;
    @TableField(exist = false)
    private String assignName;

    /** 接单（工作组id） */
    private String receiveGroupId;
    @TableField(exist = false)
    private String receiveGroupName;

    /** 处理人id */
    private String dealId;
    @TableField(exist = false)
    private String dealName;

    /** 领取人id */
    private String drawId;
    @TableField(exist = false)
    private String drawName;

    /** 预解决人id 二线剞劂环节处理人id*/
    private String preSolutionId;
    @TableField(exist = false)
    private String preSolutionName;

    /** 解决人id 一线解决环节处理人id*/
    private String solutionId;
    @TableField(exist = false)
    private String solutionName;

    /** 关闭人id */
    private String closeId;
    @TableField(exist = false)
    private String closeName;

    /** 退回补全标识 */
    private String backFlag;
}
