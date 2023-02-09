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
    * 问题任务
    */
@ApiModel(value="问题任务")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "design_biz_problem_task_new")
public class DesignBizProblemTaskNew {
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
     * 问题编号
     */
    @TableField(value = "problem_no",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="问题编号")
    private String problemNo;

    /**
     * 阶段
     */
    @TableField(value = "stage")
    @ApiModelProperty(value="阶段")
    private String stage;

    /**
     * 问题任务当前处理人
     */
    @TableField(value = "current_handler_id")
    @ApiModelProperty(value="问题任务当前处理人")
    private String currentHandlerId;

    /**
     * 问题任务处理部室 
     */
    @TableField(value = "assistant_dept_id")
    @ApiModelProperty(value="问题任务处理部室 ")
    private String assistantDeptId;

    /**
     * 类别
     */
    @TableField(value = "problem_category")
    @ApiModelProperty(value="类别")
    private String problemCategory;

    /**
     * 问题任务发起部室
     */
    @TableField(value = "ori_dep_id")
    @ApiModelProperty(value="问题任务发起部室")
    private String oriDepId;

    /**
     * 问题协查人
     */
    @TableField(value = "assistant_id")
    @ApiModelProperty(value="问题协查人")
    private String assistantId;

    /**
     * 子类
     */
    @TableField(value = "problem_subclz")
    @ApiModelProperty(value="子类")
    private String problemSubclz;

    /**
     * 问题任务发起人
     */
    @TableField(value = "originator_id")
    @ApiModelProperty(value="问题任务发起人")
    private String originatorId;

    /**
     * 问题任务说明
     */
    @TableField(value = "problem_desc",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="问题任务说明")
    private String problemDesc;

    /**
     * 条目
     */
    @TableField(value = "problem_entry")
    @ApiModelProperty(value="条目")
    private String problemEntry;

    /**
     * 问题任务计划完成时间
     */
    @TableField(value = "plan_complete_time")
    @ApiModelProperty(value="问题任务计划完成时间")
    private String planCompleteTime;

    /**
     * 问题任务内容
     */
    @TableField(value = "problem_content",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="问题任务内容")
    private String problemContent;

    /**
     * 子条目分类一
     */
    @TableField(value = "problem_sub_entry1")
    @ApiModelProperty(value="子条目分类一")
    private String problemSubEntry1;

    /**
     * 问题任务处理情况
     */
    @TableField(value = "handle_info",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="问题任务处理情况")
    private String handleInfo;

    /**
     * 风险程度
     */
    @TableField(value = "risk_level")
    @ApiModelProperty(value="风险程度")
    private String riskLevel;

    /**
     * 子条目分类二
     */
    @TableField(value = "problem_sub_entry2")
    @ApiModelProperty(value="子条目分类二")
    private String problemSubEntry2;

    /**
     * 发生频率
     */
    @TableField(value = "frequency")
    @ApiModelProperty(value="发生频率")
    private String frequency;

    /**
     * 前置问题任务编号
     */
    @TableField(value = "pre_task_num")
    @ApiModelProperty(value="前置问题任务编号")
    private String preTaskNum;

    /**
     * 附件上传
     */
    @TableField(value = "`file`")
    @ApiModelProperty(value="附件上传",hidden = true)
    private String file;

    /**
     * 问题任务创建时间
     */
    @TableField(value = "task_created_time")
    @ApiModelProperty(value="问题任务创建时间")
    private String taskCreatedTime;

    /**
     * 问题任务更新时间
     */
    @TableField(value = "task_updated_time")
    @ApiModelProperty(value="问题任务更新时间")
    private String taskUpdatedTime;

    /**
     * 问题任务申请关闭时间
     */
    @TableField(value = "apply_close_time")
    @ApiModelProperty(value="问题任务申请关闭时间")
    private String applyCloseTime;

    /**
     * 问题任务关闭时间
     */
    @TableField(value = "close_time")
    @ApiModelProperty(value="问题任务关闭时间")
    private String closeTime;

    /**
     * 关闭理由
     */
    @TableField(value = "close_reason")
    @ApiModelProperty(value="关闭理由")
    private String closeReason;

    /**
     * 问题协查人更新时间
     */
    @TableField(value = "assistant_update_time")
    @ApiModelProperty(value="问题协查人更新时间")
    private String assistantUpdateTime;

    /**
     * 重新打开次数
     */
    @TableField(value = "reopen_num")
    @ApiModelProperty(value="重新打开次数")
    private Integer reopenNum;

    /**
     * 相关应用系统
     */
    @TableField(value = "system_id")
    @ApiModelProperty(value="相关应用系统")
    private String systemId;

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

    public static final String COL_PROBLEM_NO = "problem_no";

    public static final String COL_STAGE = "stage";

    public static final String COL_CURRENT_HANDLER_ID = "current_handler_id";

    public static final String COL_ASSISTANT_DEPT_ID = "assistant_dept_id";

    public static final String COL_PROBLEM_CATEGORY = "problem_category";

    public static final String COL_ORI_DEP_ID = "ori_dep_id";

    public static final String COL_ASSISTANT_ID = "assistant_id";

    public static final String COL_PROBLEM_SUBCLZ = "problem_subclz";

    public static final String COL_ORIGINATOR_ID = "originator_id";

    public static final String COL_PROBLEM_DESC = "problem_desc";

    public static final String COL_PROBLEM_ENTRY = "problem_entry";

    public static final String COL_PLAN_COMPLETE_TIME = "plan_complete_time";

    public static final String COL_PROBLEM_CONTENT = "problem_content";

    public static final String COL_PROBLEM_SUB_ENTRY1 = "problem_sub_entry1";

    public static final String COL_HANDLE_INFO = "handle_info";

    public static final String COL_RISK_LEVEL = "risk_level";

    public static final String COL_PROBLEM_SUB_ENTRY2 = "problem_sub_entry2";

    public static final String COL_FREQUENCY = "frequency";

    public static final String COL_PRE_TASK_NUM = "pre_task_num";

    public static final String COL_FILE = "file";

    public static final String COL_TASK_CREATED_TIME = "task_created_time";

    public static final String COL_TASK_UPDATED_TIME = "task_updated_time";

    public static final String COL_APPLY_CLOSE_TIME = "apply_close_time";

    public static final String COL_CLOSE_TIME = "close_time";

    public static final String COL_CLOSE_REASON = "close_reason";

    public static final String COL_ASSISTANT_UPDATE_TIME = "assistant_update_time";

    public static final String COL_REOPEN_NUM = "reopen_num";

    public static final String COL_SYSTEM_ID = "system_id";
}