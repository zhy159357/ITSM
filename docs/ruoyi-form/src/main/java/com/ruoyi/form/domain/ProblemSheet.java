package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 问题单对象 problem_sheet
 * 
 * @author ruoyi
 * @date 2022-06-21
 */
public class ProblemSheet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 问题单id */
    private String problemId;

    /** 问题单编号 */
    @Excel(name = "问题单编号")
    private String problemNo;

    /** 阶段 0-未提交(草稿);1-处理中(合规审核、技术审核、已指派、调查中、根因已明解决方案未定、解决方案审核中);
                                                2-已解决(已解决审核人确认中、已解决管理员确认中、根因已明解决中、已解决发起人确认中、已解决发起部室经理确认中、已解决总经理室审批中、观察中);
						3-已关闭(已取消和已关闭) */
    @Excel(name = "阶段")
    private String stage;

    /** 状态 0-草稿;1-合规审核;2-技术审核;3-已指派;4-调查中;5-根因已明解决方案未定;6-解决方案审核中;7-根因已明解决中;8-已解决审核人确认中;
                                                 9-已解决管理员确认中;10-已解决发起人确认中;11-已解决发起部室经理确认中;12-已解决总经理室审批中;13-观察中;14-取消;15-关闭 */
    @Excel(name = "状态")
    private String status;

    /** 状态理由 */
    private String statusReason;

    /** 问题当前处理人 */
    private String problemCurrentHandler;
    @Excel(name = "问题当前处理人")
    @TableField(exist = false)
    private String problemCurrentHandlerName;

    /** 问题发起部室经理 */

    private String problemOriginateDepartManager;
    @Excel(name = "问题发起部室经理")
    @TableField(exist = false)
    private String problemOriginateDepartManagerName;

    /** 问题发起人 */
    private String problemOriginator;
    @Excel(name = "问题发起人")
    @TableField(exist = false)
    private String problemOriginatorName;

    /** 问题管理员 */
    private String problemManager;
    @Excel(name = "问题管理员")
    @TableField(exist = false)
    private String problemManagerName;

    /** 问题标题 */
    @Excel(name = "问题标题")
    private String problemTitle;

    /** 问题现象描述 */
    @Excel(name = "问题现象描述")
    private String problemDescription;

    /** 来源 */
    @Excel(name = "来源")
    private String problemSource;

    /** 类别 */
    @Excel(name = "类别")
    private String problemCategory;
    @TableField(exist = false)
    private String problemCategoryName;

    /** 子类 */
    @Excel(name = "子类")
    private String problemSubclass;
    @TableField(exist = false)
    private String problemSubclassName;

    /** 条目 */
    @Excel(name = "条目")
    private String problemEntry;
    @TableField(exist = false)
    private String problemEntryName;

    /** 子条目 */
    @Excel(name = "子条目")
    private String problemSubentry;
    @TableField(exist = false)
    private String problemSubentryName;

    /** 事件是否有临时解决措施标记 0-否(没有解决方案,不会弹出事件临时解决措施输入框,系统默认值:无)；1-是(有解决方案,会弹出事件临时解决措施输入框) */
    @Excel(name = "事件是否有临时解决措施标记")
    private String tempSolveFlag;

    /** 事件临时解决措施 */
    @Excel(name = "事件临时解决措施")
    private String tempSolutions;

    /** 问题类型 1-性能容量问题;2-可用性问题;3-账务类问题;4-账务类问题;5-功能问题;6-数据质量问题;7-流程管理类问题;8-其他 */
    @Excel(name = "问题类型")
    private String problemType;

    /** 风险程度 0-低;1-高 */
    @Excel(name = "风险程度 0-低;1-高")
    private String riskLevel;

    /** 发生频率 0-低(一月以上不发生);1-中(一月数次以上);2-高(一周数次以上) */
    @Excel(name = "发生频率")
    private String frequency;

    /** 优先级 0-低;1-高 */
    @Excel(name = "优先级 0-低;1-高")
    private String priority;

    /** 影响业务中断 0-否;1-是 */
    @Excel(name = "影响业务中断 0-否;1-是")
    private String impactServiceInterruptFlag;

    /** 计划完成时间 */
    @Excel(name = "计划完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planSolveTime;

    /** 问题牵头部室 */
    private String problemSolverDepartment;
    @TableField(exist = false)
    @Excel(name = "问题牵头部室")
    private String problemSolverDepartmentName;

    /** 问题审核人 */
    private String problemAuditor;
    @TableField(exist = false)
    @Excel(name = "问题审核人")
    private String problemAuditorName;

    /** 问题牵头人 */
    private String problemSolver;
    @TableField(exist = false)
    @Excel(name = "问题牵头人")
    private String problemSolverName;

    /** 根因分析汇总 */
    @Excel(name = "根因分析汇总")
    private String problemCauseSummary;

    /** 根因分类一 */
    @Excel(name = "根因分类一")
    private String problemCauseClass1;
    @TableField(exist = false)
    private String problemCauseClass1Name;

    /** 根因分类二 */
    @Excel(name = "根因分类二")
    private String problemCauseClass2;
    @TableField(exist = false)
    private String problemCauseClass2Name;

    /** 解决方案汇总 */
    @Excel(name = "解决方案汇总")
    private String problemSolutionSummary;

    /** 解决方案修改次数 */
    @Excel(name = "解决方案修改次数")
    private Long solutionModifyNum;

    /** 计划解决时间修改次数 */
    @Excel(name = "计划解决时间修改次数")
    private Long planSolveTimeModifyNum;

    /** 解决完成情况 */
    @Excel(name = "解决完成情况")
    private String resolutionCompletion;

    /** 问题牵头人上次更新时间 */
    @Excel(name = "问题牵头人上次更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date problemSolverLastUpdated;

    /** 解决时间 */
    @Excel(name = "解决时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date problemSolveTime;

    /** 观察期限 */
    @Excel(name = "观察期限")
    private String observationPeriod;

    /** 观察说明 */
    @Excel(name = "观察说明")
    private String observationExplain;

    /** 问题重新打开次数 */
    @Excel(name = "问题重新打开次数")
    private Long problemReopenNum;

    /** 问题取消说明 */
    @Excel(name = "问题取消说明")
    private String problemCancelInstruction;

    /** 问题取消原因 */
    @Excel(name = "问题取消原因")
    private String problemCancelReason;

    /** 问题关闭时间(包括关闭时间和取消时间（取消要有取消说明）) */
    @Excel(name = "问题关闭时间(包括关闭时间和取消时间", readConverterExp = "取=消要有取消说明")
    private Date problemCloseTime;

    /** 问题关闭分类 */
    @Excel(name = "问题关闭分类")
    private String problemCloseType;

    public String getProblemCauseClass1Name() {
        return problemCauseClass1Name;
    }

    public void setProblemCauseClass1Name(String problemCauseClass1Name) {
        this.problemCauseClass1Name = problemCauseClass1Name;
    }

    public String getProblemCauseClass2Name() {
        return problemCauseClass2Name;
    }

    public void setProblemCauseClass2Name(String problemCauseClass2Name) {
        this.problemCauseClass2Name = problemCauseClass2Name;
    }

    public String getProblemCategoryName() {
        return problemCategoryName;
    }

    public void setProblemCategoryName(String problemCategoryName) {
        this.problemCategoryName = problemCategoryName;
    }

    public String getProblemSubclassName() {
        return problemSubclassName;
    }

    public void setProblemSubclassName(String problemSubclassName) {
        this.problemSubclassName = problemSubclassName;
    }

    public String getProblemEntryName() {
        return problemEntryName;
    }

    public void setProblemEntryName(String problemEntryName) {
        this.problemEntryName = problemEntryName;
    }

    public String getProblemSubentryName() {
        return problemSubentryName;
    }

    public void setProblemSubentryName(String problemSubentryName) {
        this.problemSubentryName = problemSubentryName;
    }

    public String getProblemCurrentHandlerName() {
        return problemCurrentHandlerName;
    }

    public void setProblemCurrentHandlerName(String problemCurrentHandlerName) {
        this.problemCurrentHandlerName = problemCurrentHandlerName;
    }

    public String getProblemOriginateDepartManagerName() {
        return problemOriginateDepartManagerName;
    }

    public void setProblemOriginateDepartManagerName(String problemOriginateDepartManagerName) {
        this.problemOriginateDepartManagerName = problemOriginateDepartManagerName;
    }

    public String getProblemOriginatorName() {
        return problemOriginatorName;
    }

    public void setProblemOriginatorName(String problemOriginatorName) {
        this.problemOriginatorName = problemOriginatorName;
    }

    public String getProblemManagerName() {
        return problemManagerName;
    }

    public void setProblemManagerName(String problemManagerName) {
        this.problemManagerName = problemManagerName;
    }

    public String getProblemSolverDepartmentName() {
        return problemSolverDepartmentName;
    }

    public void setProblemSolverDepartmentName(String problemSolverDepartmentName) {
        this.problemSolverDepartmentName = problemSolverDepartmentName;
    }

    public String getProblemAuditorName() {
        return problemAuditorName;
    }

    public void setProblemAuditorName(String problemAuditorName) {
        this.problemAuditorName = problemAuditorName;
    }

    public String getProblemSolverName() {
        return problemSolverName;
    }

    public void setProblemSolverName(String problemSolverName) {
        this.problemSolverName = problemSolverName;
    }

    public void setProblemId(String problemId)
    {
        this.problemId = problemId;
    }

    public String getProblemId()
    {
        return problemId;
    }
    public void setProblemNo(String problemNo)
    {
        this.problemNo = problemNo;
    }

    public String getProblemNo()
    {
        return problemNo;
    }
    public void setStage(String stage)
    {
        this.stage = stage;
    }

    public String getStage()
    {
        return stage;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setStatusReason(String statusReason)
    {
        this.statusReason = statusReason;
    }

    public String getStatusReason()
    {
        return statusReason;
    }
    public void setProblemCurrentHandler(String problemCurrentHandler)
    {
        this.problemCurrentHandler = problemCurrentHandler;
    }

    public String getProblemCurrentHandler()
    {
        return problemCurrentHandler;
    }
    public void setProblemOriginateDepartManager(String problemOriginateDepartManager)
    {
        this.problemOriginateDepartManager = problemOriginateDepartManager;
    }

    public String getProblemOriginateDepartManager()
    {
        return problemOriginateDepartManager;
    }
    public void setProblemOriginator(String problemOriginator)
    {
        this.problemOriginator = problemOriginator;
    }

    public String getProblemOriginator()
    {
        return problemOriginator;
    }
    public void setProblemManager(String problemManager)
    {
        this.problemManager = problemManager;
    }

    public String getProblemManager()
    {
        return problemManager;
    }
    public void setProblemTitle(String problemTitle)
    {
        this.problemTitle = problemTitle;
    }

    public String getProblemTitle()
    {
        return problemTitle;
    }
    public void setProblemDescription(String problemDescription)
    {
        this.problemDescription = problemDescription;
    }

    public String getProblemDescription()
    {
        return problemDescription;
    }
    public void setProblemSource(String problemSource)
    {
        this.problemSource = problemSource;
    }

    public String getProblemSource()
    {
        return problemSource;
    }
    public void setProblemCategory(String problemCategory)
    {
        this.problemCategory = problemCategory;
    }

    public String getProblemCategory()
    {
        return problemCategory;
    }
    public void setProblemSubclass(String problemSubclass)
    {
        this.problemSubclass = problemSubclass;
    }

    public String getProblemSubclass()
    {
        return problemSubclass;
    }
    public void setProblemEntry(String problemEntry)
    {
        this.problemEntry = problemEntry;
    }

    public String getProblemEntry()
    {
        return problemEntry;
    }
    public void setProblemSubentry(String problemSubentry)
    {
        this.problemSubentry = problemSubentry;
    }

    public String getProblemSubentry()
    {
        return problemSubentry;
    }
    public void setTempSolveFlag(String tempSolveFlag)
    {
        this.tempSolveFlag = tempSolveFlag;
    }

    public String getTempSolveFlag()
    {
        return tempSolveFlag;
    }
    public void setTempSolutions(String tempSolutions)
    {
        this.tempSolutions = tempSolutions;
    }

    public String getTempSolutions()
    {
        return tempSolutions;
    }
    public void setProblemType(String problemType)
    {
        this.problemType = problemType;
    }

    public String getProblemType()
    {
        return problemType;
    }
    public void setRiskLevel(String riskLevel)
    {
        this.riskLevel = riskLevel;
    }

    public String getRiskLevel()
    {
        return riskLevel;
    }
    public void setFrequency(String frequency)
    {
        this.frequency = frequency;
    }

    public String getFrequency()
    {
        return frequency;
    }
    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getPriority()
    {
        return priority;
    }
    public void setImpactServiceInterruptFlag(String impactServiceInterruptFlag)
    {
        this.impactServiceInterruptFlag = impactServiceInterruptFlag;
    }

    public String getImpactServiceInterruptFlag()
    {
        return impactServiceInterruptFlag;
    }
    public void setPlanSolveTime(Date planSolveTime)
    {
        this.planSolveTime = planSolveTime;
    }

    public Date getPlanSolveTime()
    {
        return planSolveTime;
    }
    public void setProblemSolverDepartment(String problemSolverDepartment)
    {
        this.problemSolverDepartment = problemSolverDepartment;
    }

    public String getProblemSolverDepartment()
    {
        return problemSolverDepartment;
    }
    public void setProblemAuditor(String problemAuditor)
    {
        this.problemAuditor = problemAuditor;
    }

    public String getProblemAuditor()
    {
        return problemAuditor;
    }
    public void setProblemSolver(String problemSolver)
    {
        this.problemSolver = problemSolver;
    }

    public String getProblemSolver()
    {
        return problemSolver;
    }
    public void setProblemCauseSummary(String problemCauseSummary)
    {
        this.problemCauseSummary = problemCauseSummary;
    }

    public String getProblemCauseSummary()
    {
        return problemCauseSummary;
    }
    public void setProblemCauseClass1(String problemCauseClass1)
    {
        this.problemCauseClass1 = problemCauseClass1;
    }

    public String getProblemCauseClass1()
    {
        return problemCauseClass1;
    }
    public void setProblemCauseClass2(String problemCauseClass2)
    {
        this.problemCauseClass2 = problemCauseClass2;
    }

    public String getProblemCauseClass2()
    {
        return problemCauseClass2;
    }
    public void setProblemSolutionSummary(String problemSolutionSummary)
    {
        this.problemSolutionSummary = problemSolutionSummary;
    }

    public String getProblemSolutionSummary()
    {
        return problemSolutionSummary;
    }
    public void setSolutionModifyNum(Long solutionModifyNum)
    {
        this.solutionModifyNum = solutionModifyNum;
    }

    public Long getSolutionModifyNum()
    {
        return solutionModifyNum;
    }
    public void setPlanSolveTimeModifyNum(Long planSolveTimeModifyNum)
    {
        this.planSolveTimeModifyNum = planSolveTimeModifyNum;
    }

    public Long getPlanSolveTimeModifyNum()
    {
        return planSolveTimeModifyNum;
    }
    public void setResolutionCompletion(String resolutionCompletion)
    {
        this.resolutionCompletion = resolutionCompletion;
    }

    public String getResolutionCompletion()
    {
        return resolutionCompletion;
    }
    public void setProblemSolverLastUpdated(Date problemSolverLastUpdated)
    {
        this.problemSolverLastUpdated = problemSolverLastUpdated;
    }

    public Date getProblemSolverLastUpdated()
    {
        return problemSolverLastUpdated;
    }
    public void setProblemSolveTime(Date problemSolveTime)
    {
        this.problemSolveTime = problemSolveTime;
    }

    public Date getProblemSolveTime()
    {
        return problemSolveTime;
    }
    public void setObservationPeriod(String observationPeriod)
    {
        this.observationPeriod = observationPeriod;
    }

    public String getObservationPeriod()
    {
        return observationPeriod;
    }
    public void setObservationExplain(String observationExplain)
    {
        this.observationExplain = observationExplain;
    }

    public String getObservationExplain()
    {
        return observationExplain;
    }
    public void setProblemReopenNum(Long problemReopenNum)
    {
        this.problemReopenNum = problemReopenNum;
    }

    public Long getProblemReopenNum()
    {
        return problemReopenNum;
    }
    public void setProblemCancelInstruction(String problemCancelInstruction)
    {
        this.problemCancelInstruction = problemCancelInstruction;
    }

    public String getProblemCancelInstruction()
    {
        return problemCancelInstruction;
    }
    public void setProblemCancelReason(String problemCancelReason)
    {
        this.problemCancelReason = problemCancelReason;
    }

    public String getProblemCancelReason()
    {
        return problemCancelReason;
    }
    public void setProblemCloseTime(Date problemCloseTime)
    {
        this.problemCloseTime = problemCloseTime;
    }

    public Date getProblemCloseTime()
    {
        return problemCloseTime;
    }
    public void setProblemCloseType(String problemCloseType)
    {
        this.problemCloseType = problemCloseType;
    }

    public String getProblemCloseType()
    {
        return problemCloseType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("problemId", getProblemId())
            .append("problemNo", getProblemNo())
            .append("stage", getStage())
            .append("status", getStatus())
            .append("statusReason", getStatusReason())
            .append("problemCurrentHandler", getProblemCurrentHandler())
            .append("problemOriginateDepartManager", getProblemOriginateDepartManager())
            .append("problemOriginator", getProblemOriginator())
            .append("problemManager", getProblemManager())
            .append("problemTitle", getProblemTitle())
            .append("problemDescription", getProblemDescription())
            .append("problemSource", getProblemSource())
            .append("problemCategory", getProblemCategory())
            .append("problemSubclass", getProblemSubclass())
            .append("problemEntry", getProblemEntry())
            .append("problemSubentry", getProblemSubentry())
            .append("tempSolveFlag", getTempSolveFlag())
            .append("tempSolutions", getTempSolutions())
            .append("problemType", getProblemType())
            .append("riskLevel", getRiskLevel())
            .append("frequency", getFrequency())
            .append("priority", getPriority())
            .append("impactServiceInterruptFlag", getImpactServiceInterruptFlag())
            .append("planSolveTime", getPlanSolveTime())
            .append("problemSolverDepartment", getProblemSolverDepartment())
            .append("problemAuditor", getProblemAuditor())
            .append("problemSolver", getProblemSolver())
            .append("problemCauseSummary", getProblemCauseSummary())
            .append("problemCauseClass1", getProblemCauseClass1())
            .append("problemCauseClass2", getProblemCauseClass2())
            .append("problemSolutionSummary", getProblemSolutionSummary())
            .append("solutionModifyNum", getSolutionModifyNum())
            .append("planSolveTimeModifyNum", getPlanSolveTimeModifyNum())
            .append("resolutionCompletion", getResolutionCompletion())
            .append("problemSolverLastUpdated", getProblemSolverLastUpdated())
            .append("problemSolveTime", getProblemSolveTime())
            .append("observationPeriod", getObservationPeriod())
            .append("observationExplain", getObservationExplain())
            .append("problemReopenNum", getProblemReopenNum())
            .append("problemCancelInstruction", getProblemCancelInstruction())
            .append("problemCancelReason", getProblemCancelReason())
            .append("problemCloseTime", getProblemCloseTime())
            .append("problemCloseType", getProblemCloseType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
