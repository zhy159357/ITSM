package com.ruoyi.form.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventIncSearchRespVo {

    private String transInstno;// 交易网点号
    private String transTeller;// 操作员号
    private String transTellerName;// 操作员名称
    private String transDate;// 上报时间
    private String eventNo;// 柜面事件编号
    private String systemName;// 应用系统
    private String imCode;// IM事件编号
    private String eventResolution;// 解决方案
    // 0星：zero:0 1星：one:1 2星：two:2 3星：there:3 4星：four:4 5星：five:5
    private String evenTenvaluation;// 满意度评价
    private String evenTile;// 事件标题
    private String evenTdesc;// 事件情况描述
    // 总行事件：Head:0 分行事件：Branch:1
    private String evenHeadBranch;// 总分行事件标识
    // 中心侧事件：Center:0 网点侧事件：Net:1
    private String eventCenterNet;// 中心侧/网点侧事件标识
    // 电话：Phone:0 监控：Monitor:1 日常检查：Daily:2 客服派单：Service:3 邮件报修：Email:4 容量管理：Capacity:5 应急演练：Emergency:6 其他：Other:7 网页报障：SelfService:8 移动报障：MoibleService:9 IM报障：IMService:10 柜面报障：TIPService:11 PAD报障：PADService:12 电话(事件报备)：PhoneINC:13 运营助手：OperationAssistant:14
    private String eventSource;// 报障的来源
    private String eventPriority;// 事件优先级
    private String eventFactor;// 事件影响面
    private String eventLevel;// 事件级别
    private String eventReasonType;// 事件原因
    private String eventAssigneegrp;// 事件当前受派组
    private String eventAssignee;// 事件当前受派人
    // 新建：New:0 已指派：Assigned:1 进行中：In Progress:2 已解决：Resolved:4 已关闭：Closed:5 已取消：Cancelled:6
    private String eventStatus;// 事件状态
    // 分派到服务台：Assigned_HelpDesk:0 分派到一线：Assinged_OneLine:1 分派到二线：Assigned_TwoLine:2 一线处理中：Progress_OneLine:3 二线处理中：Progress_TwoLine:4 等待第三方厂商：Wait_ThreeOther:5 服务台处理中：Progress_HelpDesk:6 发起人确认中：Check_HelpDesk:7
    private String eventStatusReason;// 状态理由
    private String eventcCloseCode;// 关闭代码
    private String eventReporttime;// 事件开启时间
    private String eventResolvetime;// 事件解决时间
    private String eventCloseTime;// 事件关闭时间
    // 是：Yes:0
    private String eventPendmark;// 挂起标识
    private String eventCancelFlag;// 客户撤单标识
    private String eventCancelTime;// 客户撤单时间
    private String eventExpeditedFlag;// 客户加急标识

    /**
     * reportOrg
     * reportPerson
     * reportPhone
     * reportAddress
     * cashBoxNo
     * transCode
     * transBusName
     * transSerialNo
     * transType
     * emergencyLevel
     * transCurrency
     * debitNo
     * creditNo
     * transAmount
     * exceptionCount
     * expectProcessTime
     * confirmFlag
     * processTime
     * evaluationScore
     * evaluateLabel
     * envaluationDesc
     * eventReasonType
     * belongDept
     */
    private String reportOrg;// 上报机构
    private String reportPerson;// 上报人
    private String reportPhone;// 上报人联系方式
    private String reportAddress;// 上报人地址
    private String cashBoxNo;// 柜员钱箱
    private String transCode;// 交易码
    private String transBusName;// 交易名称
    private String transSerialNo;// 交易流水
    private String transType;// 交易类型
    private String emergencyLevel;// 紧急程度
    private String transCurrency;// 交易币种
    private String debitNo;// 借方账号
    private String creditNo;// 贷方账号
    private String transAmount;// 交易金额
    private String exceptionCount;// N天内的异常事件
    private String expectProcessTime;// 预计处理时长
    private String confirmFlag;// 确认标识
    private String processTime;// 事件处理时长
    private String evaluationScore;// 评价分数
    private String evaluateLabel;// 评价标签
    private String envaluationDesc;// 意见及建议评价内容
    private String belongSystem;// 所属系统
    private String belongDept;// 归属部门
}
