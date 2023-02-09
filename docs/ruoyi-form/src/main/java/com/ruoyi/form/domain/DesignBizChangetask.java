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
    * 变更任务单
    */
@ApiModel(value="变更任务单")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "design_biz_changetask")
public class DesignBizChangetask {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键",hidden = true)
    private Long id;

    /**
     * 实例ID
     */
    @TableField(value = "instance_id")
    @ApiModelProperty(value="实例ID",hidden = true)
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
    @ApiModelProperty(value="备用字段2",hidden = true)
    private String extra2;

    /**
     * 备用字段3
     */
    @TableField(value = "extra3")
    @ApiModelProperty(value="备用字段3",hidden = true)
    private String extra3;

    /**
     * 备用字段4
     */
    @TableField(value = "extra4")
    @ApiModelProperty(value="备用字段4",hidden = true)
    private String extra4;

    /**
     * 备用字段5
     */
    @TableField(value = "extra5")
    @ApiModelProperty(value="备用字段5",hidden = true)
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
    @ApiModelProperty(value="创建时间",hidden = true)
    private Date createdTime;

    /**
     * 创建人
     */
    @TableField(value = "created_by")
    @ApiModelProperty(value="创建人",hidden = true)
    private String createdBy;

    /**
     * 创建人名字
     */
    @TableField(value = "created_by_name")
    @ApiModelProperty(value="创建人名字",hidden = true)
    private String createdByName;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    @ApiModelProperty(value="更新时间",hidden = true)
    private Date updatedTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by")
    @ApiModelProperty(value="更新人",hidden = true)
    private String updatedBy;

    /**
     * 请求变更单号
     */
    @TableField(value = "changeNo",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="请求变更单号")
    private String changeno;

    /**
     * 任务场景类型
     */
    @TableField(value = "`type`")
    @ApiModelProperty(value="任务场景类型")
    private String type;

    /**
     * 任务内容
     */
    @TableField(value = "content",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="任务内容")
    private String content;

    /**
     * 预审人
     */
    @TableField(value = "preApproval")
    @ApiModelProperty(value="预审人",hidden = true)
    private String preapproval;

    /**
     * 当前处理人
     */
    @TableField(value = "currentProcessor")
    @ApiModelProperty(value="当前处理人")
    private String currentprocessor;

    /**
     * 创建人所在部门
     */
    @TableField(value = "created_dept_name")
    @ApiModelProperty(value="创建人所在部门",hidden = true)
    private String createdDeptName;

    /**
     * 任务状态
     */
    @TableField(value = "taskStatus")
    @ApiModelProperty(value="任务状态",hidden = true)
    private String taskstatus;

    /**
     * 受派人
     */
    @TableField(value = "assignee")
    @ApiModelProperty(value="受派人",hidden = true)
    private String assignee;

    /**
     * 受派组
     */
    @TableField(value = "assignedGroup")
    @ApiModelProperty(value="受派组")
    private String assignedgroup;

    /**
     * 部门
     */
    @TableField(value = "taskDept")
    @ApiModelProperty(value="部门",hidden = true)
    private String taskdept;

    /**
     * 期望实施时间
     */
    @TableField(value = "wantImplTime")
    @ApiModelProperty(value="期望实施时间",hidden = true)
    private String wantimpltime;

    /**
     * 是否并包
     */
    @TableField(value = "mergeFlag")
    @ApiModelProperty(value="是否并包",hidden = true)
    private Integer mergeflag;

    /**
     * 并包任务编号
     */
    @TableField(value = "mergeTaskNo")
    @ApiModelProperty(value="并包任务编号",hidden = true)
    private String mergetaskno;

    /**
     * 分支号
     */
    @TableField(value = "branchId")
    @ApiModelProperty(value="分支号",hidden = true)
    private String branchid;

    /**
     * 变更应用系统
     */
    @TableField(value = "referSys")
    @ApiModelProperty(value="变更应用系统",hidden = true)
    private String refersys;

    /**
     * 是否涉及业务系统中断
     */
    @TableField(value = "referBusinessCenterFlag")
    @ApiModelProperty(value="是否涉及业务系统中断")
    private Integer referbusinesscenterflag;

    /**
     * 是否有前置任务
     */
    @TableField(value = "preTaskFlag")
    @ApiModelProperty(value="是否有前置任务",hidden = true)
    private Integer pretaskflag;

    /**
     * 前置任务编号
     */
    @TableField(value = "preTaskNo")
    @ApiModelProperty(value="前置任务编号",hidden = true)
    private String pretaskno;

    /**
     * 部署方式
     */
    @TableField(value = "deployWay")
    @ApiModelProperty(value="部署方式",hidden = true)
    private String deployway;

    /**
     * 任务变更内容描述
     */
    /*@TableField(value = "taskContent",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="任务变更内容描述")
    private String taskcontent;*/

    /**
     * 备份步骤
     */
    @TableField(value = "backupStep")
    @ApiModelProperty(value="备份步骤",hidden = true)
    private String backupstep;

    /**
     * 操作步骤
     */
    @TableField(value = "operateStep")
    @ApiModelProperty(value="操作步骤",hidden = true)
    private String operatestep;

    /**
     * 验证步骤
     */
    @TableField(value = "verificationStep")
    @ApiModelProperty(value="验证步骤",hidden = true)
    private String verificationstep;

    /**
     * 回退步骤
     */
    @TableField(value = "returnStep")
    @ApiModelProperty(value="回退步骤",hidden = true)
    private String returnstep;

    /**
     * 影响范围
     */
    @TableField(value = "infRange")
    @ApiModelProperty(value="影响范围",hidden = true)
    private String infrange;

    /**
     * 计划开始时间
     */
    @TableField(value = "planStartDate")
    @ApiModelProperty(value="计划开始时间",hidden = true)
    private String planstartdate;

    /**
     * 计划结束时间
     */
    @TableField(value = "planOverDate")
    @ApiModelProperty(value="计划结束时间",hidden = true)
    private String planoverdate;

    /**
     * 计划描述
     */
    @TableField(value = "planDescription")
    @ApiModelProperty(value="计划描述",hidden = true)
    private String plandescription;

    /**
     * 排班时间
     */
    @TableField(value = "scheduleDate")
    @ApiModelProperty(value="排班时间",hidden = true)
    private String scheduledate;

    /**
     * 排班实施人员
     */
    @TableField(value = "scheduleMan")
    @ApiModelProperty(value="排班实施人员",hidden = true)
    private String scheduleman;

    /**
     * 自动化触发方式
     */
    @TableField(value = "triggerMode")
    @ApiModelProperty(value="自动化触发方式",hidden = true)
    private String triggermode;

    /**
     * 自动触发时间点
     */
    @TableField(value = "autoTrigger")
    @ApiModelProperty(value="自动触发时间点",hidden = true)
    private String autotrigger;

    /**
     * 自动变更结果核验人
     */
    @TableField(value = "autoVerifier")
    @ApiModelProperty(value="自动变更结果核验人",hidden = true)
    private String autoverifier;

    /**
     * 是否涉及屏蔽
     */
    @TableField(value = "shieldFlag")
    @ApiModelProperty(value="是否涉及屏蔽",hidden = true)
    private Integer shieldflag;

    /**
     * 是否涉及堡垒机
     */
    @TableField(value = "fortressMachineFlag")
    @ApiModelProperty(value="是否涉及堡垒机",hidden = true)
    private Integer fortressmachineflag;

    /**
     * 堡垒机IP
     */
    @TableField(value = "fortressMachineIP")
    @ApiModelProperty(value="堡垒机IP",hidden = true)
    private String fortressmachineip;

    /**
     * 堡垒机用户
     */
    @TableField(value = "fortressMachineUser")
    @ApiModelProperty(value="堡垒机用户",hidden = true)
    private String fortressmachineuser;

    /**
     * 实施接单时间
     */
    @TableField(value = "implReceivedDate")
    @ApiModelProperty(value="实施接单时间",hidden = true)
    private String implreceiveddate;

    /**
     * 实施结单时间
     */
    @TableField(value = "implOverDate")
    @ApiModelProperty(value="实施结单时间",hidden = true)
    private String imploverdate;

    /**
     * 实施开始时间
     */
    @TableField(value = "implStartDate")
    @ApiModelProperty(value="实施开始时间",hidden = true)
    private String implstartdate;

    /**
     * 实施结束时间
     */
    @TableField(value = "implEndDate")
    @ApiModelProperty(value="实施结束时间",hidden = true)
    private String implenddate;

    /**
     * 自动化执行任务状态
     */
    @TableField(value = "autoTaskFlag")
    @ApiModelProperty(value="自动化执行任务状态",hidden = true)
    private String autotaskflag;

    /**
     * 复核人
     */
    @TableField(value = "checkMan")
    @ApiModelProperty(value="复核人",hidden = true)
    private String checkman;

    /**
     * 实施结果
     */
    @TableField(value = "implResultCheck")
    @ApiModelProperty(value="实施结果",hidden = true)
    private String implresultcheck;

    /**
     * 所属变更ID
     */
    @TableField(value = "changeId")
    @ApiModelProperty(value="所属变更ID",hidden = true)
    private String changeid;

    /**
     * 审批人
     */
    @TableField(value = "approval")
    @ApiModelProperty(value="审批人")
    private String approval;

    /**
     * 0=不是补救单，1=是补救单
     */
    @TableField(value = "remedyFlag")
    @ApiModelProperty(value="0=不是补救单，1=是补救单",hidden = true)
    private Integer remedyflag;

    /**
     * 0=未复核过，1=复核过
     */
    @TableField(value = "reviewed")
    @ApiModelProperty(value="0=未复核过，1=复核过",hidden = true)
    private Integer reviewed;

    /**
     * 任务名称
     */
    @TableField(value = "taskName")
    @ApiModelProperty(value="任务名称",hidden = true)
    private String taskname;

    /**
     * 0=未预审退回，1=预审退回过
     */
    @TableField(value = "preApprovalBackFlag")
    @ApiModelProperty(value="0=未预审退回，1=预审退回过",hidden = true)
    private Integer preapprovalbackflag;

    /**
     * 0=未占用，1=已占用
     */
    @TableField(value = "lockFlag")
    @ApiModelProperty(value="0=未占用，1=已占用",hidden = true)
    private Integer lockflag;

    /**
     * 0=不需要退回预审，1=需要退回预审
     */
    @TableField(value = "backToApprovalFlag")
    @ApiModelProperty(value="0=不需要退回预审，1=需要退回预审",hidden = true)
    private Integer backtoapprovalflag;

    /**
     * 任务验证方案
     */
    @TableField(value = "changeTaskVerification")
    @ApiModelProperty(value="任务验证方案",hidden = true)
    private String changetaskverification;

    /**
     * 顺序
     */
    @TableField(value = "`sequence`")
    @ApiModelProperty(value="顺序",hidden = true)
    private Long sequence;

    /**
     * 变更手册附件上传
     */
    @TableField(value = "enclosureUploadFile")
    @ApiModelProperty(value="变更手册附件上传",hidden = true)
    private String enclosureuploadfile;

    /**
     * 实施记录
     */
    @TableField(value = "implRecord")
    @ApiModelProperty(value="实施记录",hidden = true)
    private String implrecord;

    /**
     * 任务单号
     */
    @TableField(value = "changeTaskNo",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="任务单号")
    private String changetaskno;

    /**
     * 自动化场景类型
     */
    @TableField(value = "auto_scene_type")
    @ApiModelProperty(value="自动化场景类型",hidden = true)
    private String autoSceneType;

    /**
     * 涉及提权应用
     */
    @TableField(value = "refer_token_app")
    @ApiModelProperty(value="涉及提权应用",hidden = true)
    private String referTokenApp;

    /**
     * 缺陷原因
     */
    @TableField(value = "remedyReason")
    @ApiModelProperty(value="缺陷原因",hidden = true)
    private String remedyreason;

    /**
     * 实施人ID
     */
    @TableField(value = "impl_man")
    @ApiModelProperty(value="实施人ID")
    private String implMan;

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

    public static final String COL_CREATED_BY_NAME = "created_by_name";

    public static final String COL_UPDATED_TIME = "updated_time";

    public static final String COL_UPDATED_BY = "updated_by";

    public static final String COL_CHANGENO = "changeNo";

    public static final String COL_TYPE = "type";

    public static final String COL_CONTENT = "content";

    public static final String COL_PREAPPROVAL = "preApproval";

    public static final String COL_CURRENTPROCESSOR = "currentProcessor";

    public static final String COL_CREATED_DEPT_NAME = "created_dept_name";

    public static final String COL_TASKSTATUS = "taskStatus";

    public static final String COL_ASSIGNEE = "assignee";

    public static final String COL_ASSIGNEDGROUP = "assignedGroup";

    public static final String COL_TASKDEPT = "taskDept";

    public static final String COL_WANTIMPLTIME = "wantImplTime";

    public static final String COL_MERGEFLAG = "mergeFlag";

    public static final String COL_MERGETASKNO = "mergeTaskNo";

    public static final String COL_BRANCHID = "branchId";

    public static final String COL_REFERSYS = "referSys";

    public static final String COL_REFERBUSINESSCENTERFLAG = "referBusinessCenterFlag";

    public static final String COL_PRETASKFLAG = "preTaskFlag";

    public static final String COL_PRETASKNO = "preTaskNo";

    public static final String COL_DEPLOYWAY = "deployWay";

 /*   public static final String COL_TASKCONTENT = "taskContent";*/

    public static final String COL_BACKUPSTEP = "backupStep";

    public static final String COL_OPERATESTEP = "operateStep";

    public static final String COL_VERIFICATIONSTEP = "verificationStep";

    public static final String COL_RETURNSTEP = "returnStep";

    public static final String COL_INFRANGE = "infRange";

    public static final String COL_PLANSTARTDATE = "planStartDate";

    public static final String COL_PLANOVERDATE = "planOverDate";

    public static final String COL_PLANDESCRIPTION = "planDescription";

    public static final String COL_SCHEDULEDATE = "scheduleDate";

    public static final String COL_SCHEDULEMAN = "scheduleMan";

    public static final String COL_TRIGGERMODE = "triggerMode";

    public static final String COL_AUTOTRIGGER = "autoTrigger";

    public static final String COL_AUTOVERIFIER = "autoVerifier";

    public static final String COL_SHIELDFLAG = "shieldFlag";

    public static final String COL_FORTRESSMACHINEFLAG = "fortressMachineFlag";

    public static final String COL_FORTRESSMACHINEIP = "fortressMachineIP";

    public static final String COL_FORTRESSMACHINEUSER = "fortressMachineUser";

    public static final String COL_IMPLRECEIVEDDATE = "implReceivedDate";

    public static final String COL_IMPLOVERDATE = "implOverDate";

    public static final String COL_IMPLSTARTDATE = "implStartDate";

    public static final String COL_IMPLENDDATE = "implEndDate";

    public static final String COL_AUTOTASKFLAG = "autoTaskFlag";

    public static final String COL_CHECKMAN = "checkMan";

    public static final String COL_IMPLRESULTCHECK = "implResultCheck";

    public static final String COL_CHANGEID = "changeId";

    public static final String COL_APPROVAL = "approval";

    public static final String COL_REMEDYFLAG = "remedyFlag";

    public static final String COL_REVIEWED = "reviewed";

    public static final String COL_TASKNAME = "taskName";

    public static final String COL_PREAPPROVALBACKFLAG = "preApprovalBackFlag";

    public static final String COL_LOCKFLAG = "lockFlag";

    public static final String COL_BACKTOAPPROVALFLAG = "backToApprovalFlag";

    public static final String COL_CHANGETASKVERIFICATION = "changeTaskVerification";

    public static final String COL_SEQUENCE = "sequence";

    public static final String COL_ENCLOSUREUPLOADFILE = "enclosureUploadFile";

    public static final String COL_IMPLRECORD = "implRecord";

    public static final String COL_CHANGETASKNO = "changeTaskNo";

    public static final String COL_AUTO_SCENE_TYPE = "auto_scene_type";

    public static final String COL_REFER_TOKEN_APP = "refer_token_app";

    public static final String COL_REMEDYREASON = "remedyReason";

    public static final String COL_IMPL_MAN = "impl_man";
}