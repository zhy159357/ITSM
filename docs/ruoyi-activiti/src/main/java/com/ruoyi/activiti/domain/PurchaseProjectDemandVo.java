package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

public class PurchaseProjectDemandVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String changeId;

    /** 采购项目类别 */
    @Excel(name = "采购项目类别", dictType = "purchase_project_type")
    @NotBlank(message = "采购项目类别不可为空")
    private String purchaseProjectType;

    /** 采购计划编号 */
    private String purchaseNo;

    /** 采购项目名称 */
    @Excel(name = "采购项目名称")
    @NotBlank(message = "采购项目名称不可为空")
    private String projectName;

    /** 采购内容 */
    @Excel(name = "采购具体内容")
    @NotBlank(message = "采购具体内容不可为空")
    private String purchaseContent;

    /** 采购内容 */
    @Excel(name = "采购背景、必要性说明")
    @NotBlank(message = "采购背景、必要性说明不可为空")
    private String purchaseBackground;

    /** 采购方式建议 */
    @Excel(name = "采购方式建议", dictType = "purchase_mode")
    @NotBlank(message = "采购方式建议不可为空")
    private String purchaseMode;

    /** 项目总金额（万元） */
    @Excel(name = "项目总金额（万元）")
    @NotBlank(message = "项目总金额（万元）不可为空")
    private String projectTotalAmount;

    /** 支出金额（万元） */
    @Excel(name = "2022年支出金额（万元）")
    @NotBlank(message = "当年支出金额（万元）不可为空")
    private String payTotalAmount;

    /** 物资到货/服务提供时间 */
    @Excel(name = "物资到货/服务提供时间")
    @NotBlank(message = "物资到货/服务提供时间不可为空")
    private String serviceProvideTime;

    /** 采购有效期 */
    @Excel(name = "采购有效期")
    @NotBlank(message = "采购有效期不可为空")
    private String purchaseEffectivePeriod;

    /** 资金落实情况 */
    @Excel(name = "资金落实情况", dictType = "capital_impl_situation")
    @NotBlank(message = "资金落实情况不可为空")
    private String capitalImplSituation;

    /** 需求处室 */
    @Excel(name = "需求处室")
    @NotBlank(message = "需求处室不可为空")
    private String demandDept;

    /** 需求处室联系人 */
    @Excel(name = "联系人")
    @NotBlank(message = "联系人不可为空")
    private String demandPerson;

    /** 联系方式 */
    @Excel(name = "联系方式")
    @NotBlank(message = "联系方式不可为空")
    private String demandPersonPhone;

    /** 规范书计划提交时间 */
    @Excel(name = "规范书计划提交时间")
    private String techSubmitTime;

    /** 采购申请计划提交时间 */
    @Excel(name = "采购申请计划提交时间")
    private String planSubmitTime;

    /** 采购实施计划启动时间 */
    @Excel(name = "采购实施计划启动时间")
    private String implStartTime;

    /** 采购实施计划完成时间 */
    @Excel(name = "采购实施计划完成时间")
    private String implEndTime;

    /** 合同计划签订时间 */
    @Excel(name = "合同计划签订时间")
    @NotBlank(message = "合同计划签订时间不可为空")
    private String contractSignTime;

    /** 调整内容 */
    private String adjustContent;

    /** 调整原因 */
    private String adjustReason;

    /** 正式提交采购申请时间 */
    private String formalSubmitTime;

    /** 预计合同签订时间 */
    private String estimateContractSignTime;

    /** 采购部对接人 */
    private String purchaseOppositePerson;

    /** 当前进度 */
    private String currentProgress;

    /** 风险提示 */
    private String riskWarning;

    /** 备注 */
    private String memo;

    /** 采购计划状态 */
    private String purchasePlanStatus;

    /** 部门领导审批人 */
    private String purchaseApproveId;
    private String purchaseApproveName;

    /** 审批状态，01-待审批；02-已审批 */
    private String purchaseStatus;

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getPurchaseProjectType() {
        return purchaseProjectType;
    }

    public void setPurchaseProjectType(String purchaseProjectType) {
        this.purchaseProjectType = purchaseProjectType;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPurchaseContent() {
        return purchaseContent;
    }

    public void setPurchaseContent(String purchaseContent) {
        this.purchaseContent = purchaseContent;
    }

    public String getPurchaseBackground() {
        return purchaseBackground;
    }

    public void setPurchaseBackground(String purchaseBackground) {
        this.purchaseBackground = purchaseBackground;
    }

    public String getPurchaseMode() {
        return purchaseMode;
    }

    public void setPurchaseMode(String purchaseMode) {
        this.purchaseMode = purchaseMode;
    }

    public String getProjectTotalAmount() {
        return projectTotalAmount;
    }

    public void setProjectTotalAmount(String projectTotalAmount) {
        this.projectTotalAmount = projectTotalAmount;
    }

    public String getPayTotalAmount() {
        return payTotalAmount;
    }

    public void setPayTotalAmount(String payTotalAmount) {
        this.payTotalAmount = payTotalAmount;
    }

    public String getServiceProvideTime() {
        return serviceProvideTime;
    }

    public void setServiceProvideTime(String serviceProvideTime) {
        this.serviceProvideTime = serviceProvideTime;
    }

    public String getPurchaseEffectivePeriod() {
        return purchaseEffectivePeriod;
    }

    public void setPurchaseEffectivePeriod(String purchaseEffectivePeriod) {
        this.purchaseEffectivePeriod = purchaseEffectivePeriod;
    }

    public String getCapitalImplSituation() {
        return capitalImplSituation;
    }

    public void setCapitalImplSituation(String capitalImplSituation) {
        this.capitalImplSituation = capitalImplSituation;
    }

    public String getDemandDept() {
        return demandDept;
    }

    public void setDemandDept(String demandDept) {
        this.demandDept = demandDept;
    }

    public String getDemandPerson() {
        return demandPerson;
    }

    public void setDemandPerson(String demandPerson) {
        this.demandPerson = demandPerson;
    }

    public String getDemandPersonPhone() {
        return demandPersonPhone;
    }

    public void setDemandPersonPhone(String demandPersonPhone) {
        this.demandPersonPhone = demandPersonPhone;
    }

    public String getTechSubmitTime() {
        return techSubmitTime;
    }

    public void setTechSubmitTime(String techSubmitTime) {
        this.techSubmitTime = techSubmitTime;
    }

    public String getPlanSubmitTime() {
        return planSubmitTime;
    }

    public void setPlanSubmitTime(String planSubmitTime) {
        this.planSubmitTime = planSubmitTime;
    }

    public String getImplStartTime() {
        return implStartTime;
    }

    public void setImplStartTime(String implStartTime) {
        this.implStartTime = implStartTime;
    }

    public String getImplEndTime() {
        return implEndTime;
    }

    public void setImplEndTime(String implEndTime) {
        this.implEndTime = implEndTime;
    }

    public String getContractSignTime() {
        return contractSignTime;
    }

    public void setContractSignTime(String contractSignTime) {
        this.contractSignTime = contractSignTime;
    }

    public String getAdjustContent() {
        return adjustContent;
    }

    public void setAdjustContent(String adjustContent) {
        this.adjustContent = adjustContent;
    }

    public String getAdjustReason() {
        return adjustReason;
    }

    public void setAdjustReason(String adjustReason) {
        this.adjustReason = adjustReason;
    }

    public String getFormalSubmitTime() {
        return formalSubmitTime;
    }

    public void setFormalSubmitTime(String formalSubmitTime) {
        this.formalSubmitTime = formalSubmitTime;
    }

    public String getEstimateContractSignTime() {
        return estimateContractSignTime;
    }

    public void setEstimateContractSignTime(String estimateContractSignTime) {
        this.estimateContractSignTime = estimateContractSignTime;
    }

    public String getPurchaseOppositePerson() {
        return purchaseOppositePerson;
    }

    public void setPurchaseOppositePerson(String purchaseOppositePerson) {
        this.purchaseOppositePerson = purchaseOppositePerson;
    }

    public String getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(String currentProgress) {
        this.currentProgress = currentProgress;
    }

    public String getRiskWarning() {
        return riskWarning;
    }

    public void setRiskWarning(String riskWarning) {
        this.riskWarning = riskWarning;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPurchasePlanStatus() {
        return purchasePlanStatus;
    }

    public void setPurchasePlanStatus(String purchasePlanStatus) {
        this.purchasePlanStatus = purchasePlanStatus;
    }

    public String getPurchaseApproveId() {
        return purchaseApproveId;
    }

    public void setPurchaseApproveId(String purchaseApproveId) {
        this.purchaseApproveId = purchaseApproveId;
    }

    public String getPurchaseApproveName() {
        return purchaseApproveName;
    }

    public void setPurchaseApproveName(String purchaseApproveName) {
        this.purchaseApproveName = purchaseApproveName;
    }

    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(String purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }
}
