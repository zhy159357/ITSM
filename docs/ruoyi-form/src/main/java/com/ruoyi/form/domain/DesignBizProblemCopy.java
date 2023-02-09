package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
    * 问题单
    */
@ApiModel(value="问题单")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "design_biz_problem_copy1")
public class DesignBizProblemCopy {
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
    @Excel(name = "问题编号")
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
    @Excel(name = "状态")
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
    @Excel(name = "上次修改日期")
    private Date updatedTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by")
    @ApiModelProperty(value="更新人")
    @Excel(name = "上次修改者")
    private String updatedBy;

    /**
     * 问题标题
     */
    @TableField(value = "problem_title",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="问题标题")
    @Excel(name = "问题标题")
    private String problemTitle;

    /**
     * 问题发起人
     */
    @TableField(value = "originator_id")
    @ApiModelProperty(value="问题发起人")
    @Excel(name = "问题开启人")
    private String originatorId;

    /**
     * 计划完成时间
     */
    @TableField(exist = false)
    @Excel(name = "问题解决期限")
    private Date planCompleteTime1;

    /**
     * 计划完成时间
     */
    @TableField(value = "plan_complete_time")
    @ApiModelProperty(value="计划完成时间")
    private String planCompleteTime;

    /**
     * 问题发起部室
     */
    @TableField(value = "ori_dep_id")
    @ApiModelProperty(value="问题发起部室")
    @Excel(name = "问题开启人部门")
    private String oriDepId;

    /**
     * 问题发起部室经理
     */
    @TableField(value = "ori_dep_manager_id")
    @ApiModelProperty(value="问题发起部室经理")
    private String oriDepManagerId;

    /**
     * 问题创建时间
     */
    @TableField(value = "problem_created_time")
    @ApiModelProperty(value="问题创建时间")
    private String problemCreatedTime;

    /**
     * 问题现象描述
     */
    @TableField(value = "problem_description",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="问题现象描述")
    @Excel(name = "问题现象描述")
    private String problemDescription;

    /**
     * 临时解决方案
     */
    @TableField(value = "temp_solutions",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="临时解决方案")
    @Excel(name = "事件临时解决措施")
    private String tempSolutions;

    /**
     * 类别
     */
    @TableField(value = "problem_category")
    @ApiModelProperty(value="类别")
    @Excel(name = "类别")
    private String problemCategory;

    /**
     * 相关应用系统
     */
    @TableField(value = "system_id")
    @ApiModelProperty(value="相关应用系统")
    private String systemId;

    /**
     * 问题来源
     */
    @TableField(value = "problem_source")
    @ApiModelProperty(value="问题来源")
    @Excel(name = "来源")
    private String problemSource;

    /**
     * 子类
     */
    @TableField(value = "problem_subclz")
    @ApiModelProperty(value="子类")
    @Excel(name = "子类")
    private String problemSubclz;

    /**
     * 风险程度
     */
    @TableField(value = "risk_level")
    @ApiModelProperty(value="风险程度")
//    @Excel(name = "风险程度")
    private String riskLevel;

    /**
     * 问题类型
     */
    @TableField(value = "problem_type")
    @ApiModelProperty(value="问题类型")
    @Excel(name = "问题类型")
    private String problemType;

    /**
     * 条目
     */
    @TableField(value = "problem_entry")
    @ApiModelProperty(value="条目")
    @Excel(name = "条目")
    private String problemEntry;

    /**
     * 发生频率
     */
    @TableField(value = "frequency")
    @ApiModelProperty(value="发生频率")
//    @Excel(name = "发生频率")
    private String frequency;

    /**
     * 阶段
     */
    @TableField(value = "stage")
    @ApiModelProperty(value="阶段")
    private String stage;

    /**
     * 子条目分类一
     */
    @TableField(value = "problem_subentry1")
    @ApiModelProperty(value="子条目分类一")
    @Excel(name = "子条目")
    private String problemSubentry1;

    /**
     * 子条目分类二
     */
    @TableField(value = "problem_subentry2")
    @ApiModelProperty(value="子条目分类二")
    private String problemSubentry2;

    /**
     * 优先级
     */
    @TableField(value = "priority")
    @ApiModelProperty(value="优先级")
    @Excel(name = "优先级")
    private String priority;

    /**
     * 影响业务中断
     */
    @TableField(value = "interrupt_flag")
    @ApiModelProperty(value="影响业务中断")
    @Excel(name = "影响业务中断")
    private String interruptFlag;

    /**
     * 是否已知错误
     */
    @TableField(value = "for_error")
    @ApiModelProperty(value="是否已知错误")
    @Excel(name = "根因明确")
    private String forError;

    /**
     * 问题牵头部室
     */
    @TableField(value = "solver_dep_id")
    @ApiModelProperty(value="问题牵头部室")
    @Excel(name = "问题牵头部室")
    private String solverDepId;

    /**
     * 根因分类一
     */
    @TableField(value = "cause_clz1")
    @ApiModelProperty(value="根因分类一")
    @Excel(name = "根因分类一")
    private String causeClz1;

    /**
     * 问题当前处理人
     */
    @TableField(value = "current_deal_id")
    @ApiModelProperty(value="问题当前处理人")
    @Excel(name = "当前处理人Id")
    private String currentDealId;

    /**
     * 问题审核人
     */
    @TableField(value = "auditor_id")
    @ApiModelProperty(value="问题审核人")
    private String auditorId;

    /**
     * 根因分类二
     */
    @TableField(value = "cause_clz2")
    @ApiModelProperty(value="根因分类二")
    @Excel(name = "根因分类二")
    private String causeClz2;

    /**
     * 问题管理员
     */
    @TableField(value = "manager_id")
    @ApiModelProperty(value="问题管理员")
    @Excel(name = "问题管理员")
    private String managerId;

    /**
     * 问题牵头人
     */
    @TableField(value = "solver_id")
    @ApiModelProperty(value="问题牵头人")
    @Excel(name = "问题牵头人")
    private String solverId;

    /**
     * 根因分析汇总
     */
    @TableField(value = "cause_summary",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="根因分析汇总")
    @Excel(name = "根因分析汇总")
    private String causeSummary;

    /**
     * 解决方案汇总
     */
    @TableField(value = "solution_summary",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="解决方案汇总")
    @Excel(name = "解决方案汇总")
    private String solutionSummary;

    /**
     * 解决完成情况
     */
    @TableField(value = "resolution_completion",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="解决完成情况")
    @Excel(name = "解决完成情况")
    private String resolutionCompletion;

    /**
     * 附件上传
     */
    @TableField(value = "`file`")
    @ApiModelProperty(value="附件上传")
    private String file;

    /**
     * 问题提交时间
     */
    @TableField(exist = false)
    @Excel(name = "提交日期")
    private Date submitTime1;

    /**
     * 问题提交时间
     */
    @TableField(value = "submit_time")
    @ApiModelProperty(value="问题提交时间")
    private String submitTime;

    /**
     * 问题更新时间
     */
    @TableField(value = "problem_update_time")
    @ApiModelProperty(value="问题更新时间")
    private String problemUpdateTime;

    /**
     * 提交解决方案时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value="提交解决方案时间")
    @Excel(name = "提交解决方案时间")
    private Date submitSolutionTime1;

    /**
     * 提交解决方案时间
     */
    @TableField(value = "submit_solution_time")
    @ApiModelProperty(value="提交解决方案时间")
    private String submitSolutionTime;

    /**
     * 解决方案修改次数
     */
    @TableField(value = "solution_modify_num")
    @ApiModelProperty(value="解决方案修改次数")
    @Excel(name = "解决方案修改次数")
    private Integer solutionModifyNum;

    /**
     * 计划解决时间修改次数
     */
    @TableField(value = "plan_complete_time_modify_num")
    @ApiModelProperty(value="计划解决时间修改次数")
    @Excel(name = "计划完成时间修改次数")
    private Integer planCompleteTimeModifyNum;

    /**
     * 问题重新打开次数
     */
    @TableField(value = "problem_reopen_num")
    @ApiModelProperty(value="问题重新打开次数")
    @Excel(name = "管理员退回次数")
    private Integer problemReopenNum;

    /**
     * 根因明确时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value="根因明确时间")
    @Excel(name = "根因明确时间")
    private Date solverClearTime1;

    /**
     * 根因明确时间
     */
    @TableField(value = "solver_clear_time")
    @ApiModelProperty(value="根因明确时间")
    private String solverClearTime;

    /**
     * 问题发起单位
     */
    @TableField(value = "org_id")
    @ApiModelProperty(value="问题发起单位")
    private String orgId;

    /**
     * 解决时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value="解决时间")
    @Excel(name = "解决时间")
    private Date solveTime1;

    /**
     * 解决时间
     */
    @TableField(value = "solve_time")
    @ApiModelProperty(value="解决时间")
    private String solveTime;

    /**
     * 问题牵头人上次更新时间
     */
    @TableField(value = "solver_last_updated")
    @ApiModelProperty(value="问题牵头人上次更新时间")
    private Date solverLastUpdated;

    /**
     * 观察期限
     */
    @TableField(exist = false)
    @ApiModelProperty(value="观察期限")
    @Excel(name = "问题观察期限")
    private Date observeTime1;

    /**
     * 观察期限
     */
    @TableField(value = "observe_time")
    @ApiModelProperty(value="观察期限")
    private String observeTime;

    /**
     * 问题关闭时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value="问题关闭时间")
    @Excel(name = "问题关闭时间")
    private Date closeTime1;

    /**
     * 问题关闭时间
     */
    @TableField(value = "close_time")
    @ApiModelProperty(value="问题关闭时间")
    private String closeTime;

    @Excel(name = "问题取消时间")
    @TableField(exist = false)
    private Date cancelTime;
    /**
     * 问题取消说明
     */
    @TableField(value = "cancel_note",condition = SqlCondition.LIKE)
    @ApiModelProperty(value="问题取消说明")
    @Excel(name = "取消说明")
    private String cancelNote;

    /**
     * 问题取消原因
     */
    @TableField(value = "cancel_reason")
    @ApiModelProperty(value="问题取消原因")
    private String cancelReason;

    /**
     * 问题关闭代码
     */
    @TableField(value = "close_type")
    @ApiModelProperty(value="问题关闭代码")
    private String closeType;


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

    public static final String COL_PROBLEM_TITLE = "problem_title";

    public static final String COL_ORIGINATOR_ID = "originator_id";

    public static final String COL_PLAN_COMPLETE_TIME = "plan_complete_time";

    public static final String COL_ORI_DEP_ID = "ori_dep_id";

    public static final String COL_ORI_DEP_MANAGER_ID = "ori_dep_manager_id";

    public static final String COL_PROBLEM_CREATED_TIME = "problem_created_time";

    public static final String COL_PROBLEM_DESCRIPTION = "problem_description";

    public static final String COL_TEMP_SOLUTIONS = "temp_solutions";

    public static final String COL_PROBLEM_CATEGORY = "problem_category";

    public static final String COL_SYSTEM_ID = "system_id";

    public static final String COL_PROBLEM_SOURCE = "problem_source";

    public static final String COL_PROBLEM_SUBCLZ = "problem_subclz";

    public static final String COL_RISK_LEVEL = "risk_level";

    public static final String COL_PROBLEM_TYPE = "problem_type";

    public static final String COL_PROBLEM_ENTRY = "problem_entry";

    public static final String COL_FREQUENCY = "frequency";

    public static final String COL_STAGE = "stage";

    public static final String COL_PROBLEM_SUBENTRY1 = "problem_subentry1";

    public static final String COL_PROBLEM_SUBENTRY2 = "problem_subentry2";

    public static final String COL_PRIORITY = "priority";

    public static final String COL_INTERRUPT_FLAG = "interrupt_flag";

    public static final String COL_FOR_ERROR = "for_error";

    public static final String COL_SOLVER_DEP_ID = "solver_dep_id";

    public static final String COL_CAUSE_CLZ1 = "cause_clz1";

    public static final String COL_CURRENT_DEAL_ID = "current_deal_id";

    public static final String COL_AUDITOR_ID = "auditor_id";

    public static final String COL_CAUSE_CLZ2 = "cause_clz2";

    public static final String COL_MANAGER_ID = "manager_id";

    public static final String COL_SOLVER_ID = "solver_id";

    public static final String COL_CAUSE_SUMMARY = "cause_summary";

    public static final String COL_SOLUTION_SUMMARY = "solution_summary";

    public static final String COL_RESOLUTION_COMPLETION = "resolution_completion";

    public static final String COL_FILE = "file";

    public static final String COL_SUBMIT_TIME = "submit_time";

    public static final String COL_PROBLEM_UPDATE_TIME = "problem_update_time";

    public static final String COL_SUBMIT_SOLUTION_TIME = "submit_solution_time";

    public static final String COL_SOLUTION_MODIFY_NUM = "solution_modify_num";

    public static final String COL_PLAN_COMPLETE_TIME_MODIFY_NUM = "plan_complete_time_modify_num";

    public static final String COL_PROBLEM_REOPEN_NUM = "problem_reopen_num";

    public static final String COL_SOLVER_CLEAR_TIME = "solver_clear_time";

    public static final String COL_ORG_ID = "org_id";

    public static final String COL_SOLVE_TIME = "solve_time";

    public static final String COL_SOLVER_LAST_UPDATED = "solver_last_updated";

    public static final String COL_OBSERVE_TIME = "observe_time";

    public static final String COL_CLOSE_TIME = "close_time";

    public static final String COL_CANCEL_NOTE = "cancel_note";

    public static final String COL_CANCEL_REASON = "cancel_reason";

    public static final String COL_CLOSE_TYPE = "close_type";
}