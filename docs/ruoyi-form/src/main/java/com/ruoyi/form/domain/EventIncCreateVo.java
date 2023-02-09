package com.ruoyi.form.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventIncCreateVo {
    private String eventNo;// 新柜面单号
    private String eventSource;// 报障来源
    private String transDate;// 上报时间
    private String systemName;// 应用系统名称
    private String evenTile;// 事件标题
    private String evenTdesc;// 事件内容
    private String transInstno;// 上报人机构
    private String transTeller;// 上报人工号
    private String transName;// 上报人姓名
    private String reportPhone;// 上报人联系方式
    private String reportAddress;// 上报人地址
    private String emergencyLevel;// 紧急程度

    // 柜面特有字段
    private String cashBoxNo;// 柜员钱箱
    private String transNo;// 交易网点号
    private String transCode;// 交易码
    private String transBusName;// 交易名称
    private String transSerialNo;// 交易流水
    private String transType;// 交易类型
    private String transCurrency;// 交易币种
    private String debitNo;// 借方账号
    private String creditNo;// 贷方账号
    private String transAmount;// 交易金额
    private String exceptionCount;// N天内的异常事件
    private String expectProcessTime;
    private String operatorNo;// 操作员工号
    private String operatorName;// 操作员名称

    // 客服特有字段
    private String businessCategory;// 业务大类
    private String businessSubCategory;// 业务小类
    private String workContent;// 工单内容
    private String reminderLog;// 催单日志
    private String processLog;// 评价内容
    private String custInfo;// 客户信息
    private String files;// 附件路径

    /**告警中心特有字段***/
    private String selfHealing;// 是否自愈
    private String selfDesc;// 自愈策略
    private String changeNo;// 关联变更id
    private String richInfo;// 丰富内容
    private String priority;// 优先级
    private String dispatchType;// 派单类型

    /** 受派组 */
    private String assignedGroup;
    private String assignedGroupName;

    /** 受派人 */
    private String assignedPerson;
    private String assignedPersonName;

    // E事通特有字段
    private String involveBusiness;//涉及交易
    private String errorMsg;//错误信息
    private String faultDesc;//故障现象
    private String involveCust;//是否涉及客户 1-是；0否
    private String custAccount;//客户账号
    private String custName;//客户名称
    private String custNumber;//客户证件号
    private String operateTime;//操作时间 yyyy-MM-dd HH:mm:ss
    private String involveAmount;//涉及金额,精确到分，四舍五入
    private String reportPerson;//上报人
    private String reportOrg;//上报人机构
    private String orgFlag;//总分行事件标记
    private String infLevel;//影响程度 （见事件单枚举说明）
    private String financeFlag;//是否财务类事件 （见事件单枚举说明）
    private String complainFlag;//是否涉及投诉 （见事件单枚举说明）
    private String initFirstLevel;//初始事件类型一级 （itsm默认填充，不传）
}
