package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
    * 变更单
    */
@ApiModel(value="变更单")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "design_biz_change")
public class DesignBizChange {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 实例ID
     */
    @TableField(value = "instance_id")
    @ApiModelProperty(value="实例ID")
    private String instanceId;

    /**
     * 备用字段1
     */
    @TableField(value = "extra1",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="备用字段1")
    private String extra1;

    /**
     * 备用字段2
     */
    @TableField(value = "extra2")
    @ApiModelProperty(value="备用字段2")
    private String extra2;

    /**
     * 备用字段3
     */
    @TableField(value = "extra3")
    @ApiModelProperty(value="备用字段3")
    private String extra3;

    /**
     * 备用字段4
     */
    @TableField(value = "extra4")
    @ApiModelProperty(value="备用字段4")
    private String extra4;

    /**
     * 备用字段5
     */
    @TableField(value = "extra5")
    @ApiModelProperty(value="备用字段5")
    private String extra5;

    /**
     * 状态
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    @ApiModelProperty(value="创建时间")
    private Date createdTime;

    /**
     * 创建人
     */
    @TableField(value = "created_by")
    @ApiModelProperty(value="创建人")
    private String createdBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    @ApiModelProperty(value="更新时间")
    private Date updatedTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by")
    @ApiModelProperty(value="更新人")
    private String updatedBy;

    /**
     * 变更标题
     */
    @TableField(value = "title",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="变更标题")
    private String title;

    /**
     * 变更描述
     */
    @TableField(value = "description",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="变更描述")
    private String description;

    /**
     * 变更验证
     */
    @TableField(value = "verification")
    @ApiModelProperty(value="变更验证")
    private String verification;

    /**
     * 变更类型
     */
    @TableField(value = "`type`")
    @ApiModelProperty(value="变更类型")
    private String type;

    /**
     * 变更依据类型
     */
    @TableField(value = "basisType")
    @ApiModelProperty(value="变更依据类型")
    private String basistype;

    /**
     * 变更依据
     */
    @TableField(value = "basis")
    @ApiModelProperty(value="变更依据")
    private String basis;

    /**
     * 当前处理人
     */
    @TableField(value = "currentProcessor")
    @ApiModelProperty(value="当前处理人")
    private String currentprocessor;

    /**
     * 变更状态
     */
    @TableField(value = "changeStatus")
    @ApiModelProperty(value="变更状态")
    private String changestatus;

    /**
     * 变更原因
     */
    @TableField(value = "reason",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="变更原因")
    private String reason;

    /**
     * 紧急变更原因
     */
    @TableField(value = "urgentReason",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="紧急变更原因")
    private String urgentreason;

    /**
     * 初始风险级别
     */
    @TableField(value = "riskLevel")
    @ApiModelProperty(value="初始风险级别")
    private String risklevel;

    /**
     * 当前风险级别
     */
    @TableField(value = "currentRiskLevel")
    @ApiModelProperty(value="当前风险级别")
    private String currentrisklevel;

    /**
     * 涉及系统
     */
    @TableField(value = "referApp")
    @ApiModelProperty(value="涉及系统")
    private String referapp;

    /**
     * 发起科室
     */
    @TableField(value = "startDept")
    @ApiModelProperty(value="发起科室")
    private String startdept;

    /**
     * 发起科室负责人
     */
    @TableField(value = "startDeptLeader")
    @ApiModelProperty(value="发起科室负责人")
    private String startdeptleader;

    /**
     * 发起人
     */
    @TableField(value = "starter")
    @ApiModelProperty(value="发起人")
    private String starter;

    /**
     * 发起人电话
     */
    @TableField(value = "starterTel")
    @ApiModelProperty(value="发起人电话")
    private String startertel;

    /**
     * 应用版本信息
     */
    @TableField(value = "appVersion")
    @ApiModelProperty(value="应用版本信息")
    private String appversion;

    /**
     * 关闭代码
     */
    @TableField(value = "closedCode")
    @ApiModelProperty(value="关闭代码")
    private String closedcode;

    /**
     * 失败原因
     */
    @TableField(value = "failReason")
    @ApiModelProperty(value="失败原因")
    private String failreason;

    /**
     * 补流程
     */
    @TableField(value = "supplyProcess")
    @ApiModelProperty(value="补流程")
    private Integer supplyprocess;

    /**
     * 完成情况
     */
    @TableField(value = "`completion`")
    @ApiModelProperty(value="完成情况")
    private String completion;

    /**
     * 计划开始时间
     */
    @TableField(value = "planStartDate")
    @ApiModelProperty(value="计划开始时间")
    private String planstartdate;

    /**
     * 计划结束时间
     */
    @TableField(value = "planCompleteDate")
    @ApiModelProperty(value="计划结束时间")
    private String plancompletedate;

    /**
     * 计划描述
     */
    @TableField(value = "`plan`",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="计划描述")
    private String plan;

    /**
     * 变更提交时间
     */
    @TableField(value = "submitDate")
    @ApiModelProperty(value="变更提交时间")
    private String submitdate;

    /**
     * 按时提交时间
     */
    @TableField(value = "dateOfSubmitOnTime")
    @ApiModelProperty(value="按时提交时间")
    private String dateofsubmitontime;

    /**
     * 实施接单时间
     */
    @TableField(value = "implementTakeDate")
    @ApiModelProperty(value="实施接单时间")
    private String implementtakedate;

    /**
     * 实施开始时间
     */
    @TableField(value = "implementStartDate")
    @ApiModelProperty(value="实施开始时间")
    private String implementstartdate;

    /**
     * 实施结束时间
     */
    @TableField(value = "implementOverDate")
    @ApiModelProperty(value="实施结束时间")
    private String implementoverdate;

    /**
     * 实施结单时间
     */
    @TableField(value = "implementStatementDate")
    @ApiModelProperty(value="实施结单时间")
    private String implementstatementdate;

    /**
     * 是否按时提交
     */
    @TableField(value = "ontime")
    @ApiModelProperty(value="是否按时提交")
    private String ontime;

    /**
     * 评审变更实际提交时间
     */
    @TableField(value = "approvalSubmitDate")
    @ApiModelProperty(value="评审变更实际提交时间")
    private String approvalsubmitdate;

    /**
     * 审批人
     */
    @TableField(value = "approval")
    @ApiModelProperty(value="审批人")
    private String approval;

    /**
     * 变更级别
     */
    @TableField(value = "changeLevel")
    @ApiModelProperty(value="变更级别")
    private String changelevel;

    /**
     * 紧急变更发起标识0=未发起紧急变更，1=发起紧急变更
     */
    @TableField(value = "urgentFlag")
    @ApiModelProperty(value="紧急变更发起标识0=未发起紧急变更，1=发起紧急变更")
    private Integer urgentflag;

    /**
     * 是否走分行流程标识，0=不走，1=走
     */
    @TableField(value = "branchFlag")
    @ApiModelProperty(value="是否走分行流程标识，0=不走，1=走")
    private Integer branchflag;

    /**
     * 排序
     */
    @TableField(value = "`sequence`")
    @ApiModelProperty(value="排序",hidden = true)
    private String sequence;

    /**
     * 上传附件
     */
    @TableField(value = "uploadFile")
    @ApiModelProperty(value="上传附件",hidden = true)
    private String uploadfile;

    /**
     * 引发可用性事件
     */
    @TableField(value = "incident_relation_flag")
    @ApiModelProperty(value="引发可用性事件")
    private Integer incidentRelationFlag;

    /**
     * 事件单号
     */
    @TableField(value = "incident_no")
    @ApiModelProperty(value="事件单号",hidden = true)
    private String incidentNo;

    /**
     * 变更预审节点应该通过哪个分支的recode
     */
    @TableField(value = "pre_recode")
    @ApiModelProperty(value="变更预审节点应该通过哪个分支的recode",hidden = true)
    private Integer preRecode;

    public static final String COL_ID = "id";

    public static final String COL_INSTANCE_ID = "instance_id";

    public static final String COL_EXTRA1 = "extra1";

    public static final String COL_EXTRA2 = "extra2";

    public static final String COL_EXTRA3 = "extra3";

    public static final String COL_EXTRA4 = "extra4";

    public static final String COL_EXTRA5 = "extra5";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_CREATED_BY = "created_by";

    public static final String COL_UPDATED_TIME = "updated_time";

    public static final String COL_UPDATED_BY = "updated_by";

    public static final String COL_TITLE = "title";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_VERIFICATION = "verification";

    public static final String COL_TYPE = "type";

    public static final String COL_BASISTYPE = "basisType";

    public static final String COL_BASIS = "basis";

    public static final String COL_CURRENTPROCESSOR = "currentProcessor";

    public static final String COL_CHANGESTATUS = "changeStatus";

    public static final String COL_REASON = "reason";

    public static final String COL_URGENTREASON = "urgentReason";

    public static final String COL_RISKLEVEL = "riskLevel";

    public static final String COL_CURRENTRISKLEVEL = "currentRiskLevel";

    public static final String COL_REFERAPP = "referApp";

    public static final String COL_STARTDEPT = "startDept";

    public static final String COL_STARTDEPTLEADER = "startDeptLeader";

    public static final String COL_STARTER = "starter";

    public static final String COL_STARTERTEL = "starterTel";

    public static final String COL_APPVERSION = "appVersion";

    public static final String COL_CLOSEDCODE = "closedCode";

    public static final String COL_FAILREASON = "failReason";

    public static final String COL_SUPPLYPROCESS = "supplyProcess";

    public static final String COL_COMPLETION = "completion";

    public static final String COL_PLANSTARTDATE = "planStartDate";

    public static final String COL_PLANCOMPLETEDATE = "planCompleteDate";

    public static final String COL_PLAN = "plan";

    public static final String COL_SUBMITDATE = "submitDate";

    public static final String COL_DATEOFSUBMITONTIME = "dateOfSubmitOnTime";

    public static final String COL_IMPLEMENTTAKEDATE = "implementTakeDate";

    public static final String COL_IMPLEMENTSTARTDATE = "implementStartDate";

    public static final String COL_IMPLEMENTOVERDATE = "implementOverDate";

    public static final String COL_IMPLEMENTSTATEMENTDATE = "implementStatementDate";

    public static final String COL_ONTIME = "ontime";

    public static final String COL_APPROVALSUBMITDATE = "approvalSubmitDate";

    public static final String COL_APPROVAL = "approval";

    public static final String COL_CHANGELEVEL = "changeLevel";

    public static final String COL_URGENTFLAG = "urgentFlag";

    public static final String COL_BRANCHFLAG = "branchFlag";

    public static final String COL_SEQUENCE = "sequence";

    public static final String COL_UPLOADFILE = "uploadFile";

    public static final String COL_INCIDENT_RELATION_FLAG = "incident_relation_flag";

    public static final String COL_INCIDENT_NO = "incident_no";

    public static final String COL_PRE_RECODE = "pre_recode";
}