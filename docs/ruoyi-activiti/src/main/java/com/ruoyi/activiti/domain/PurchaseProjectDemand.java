package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 采购事项需求对象 purchase_project_demand
 * 
 * @author ruoyi
 * @date 2021-11-17
 */
public class PurchaseProjectDemand extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String purchaseId;

    /** 关联purchase_project_demand_change表 */
    private String changeId;

    /** 编号 */
    //@Excel(name = "采购计划编号")
    private String purchaseNo;

    /** 采购项目类别 */
    //@Excel(name = "采购项目类别", sort = 1, readConverterExp = "1=总行采购项目（包括物资、服务）,2=全行集采项目（包括物资、服务）")
    @Excel(name = "采购项目类别", sort = 1, dictType = "purchase_project_type")
    private String purchaseProjectType;

    /** 项目名称 */
    @Excel(name = "项目名称", sort = 2)
    private String projectName;

    /** 采购具体内容 */
    @Excel(name = "采购具体内容", sort = 3)
    private String purchaseContent;

    /** 采购背景、必要性说明 */
    @Excel(name = "采购背景、必要性说明", sort = 4)
    private String purchaseBackground;

    /** 采购方式建议 */
    //@Excel(name = "采购方式建议", sort = 5, readConverterExp = "1=公开招标,2=邀请招标,3=竞争性磋商,4=竞争性谈判,5=单一来源采购,6=询价,7=比选")
    @Excel(name = "采购方式建议", sort = 5, dictType = "purchase_mode")
    private String purchaseMode;

    /** 项目总金额（万元） */
    @Excel(name = "项目总金额（万元）", sort = 6)
    private String projectTotalAmount;

    /** 支出金额（万元） */
    @Excel(name = "支出金额（万元）", sort = 7)
    private String payTotalAmount;

    /** 物资到货/服务提供时间 */
    @Excel(name = "物资到货/服务提供时间", sort = 8)
    private String serviceProvideTime;

    /** 采购有效期 */
    @Excel(name = "采购有效期", sort = 9)
    private String purchaseEffectivePeriod;

    /** 资金落实情况 */
    //@Excel(name = "资金落实情况", sort = 10, readConverterExp = "1=已完成项目立项/费用批复,2=已通过财审会审阅,3=已通过信科委审议,4=预计通过项目立项/费用批复")
    @Excel(name = "资金落实情况", sort = 10, dictType = "capital_impl_situation")
    private String capitalImplSituation;

    /** 需求处室 */
    @Excel(name = "需求处室", sort = 11)
    private String demandDept;

    /** 联系人 */
    @Excel(name = "联系人", sort = 12)
    private String demandPerson;

    /** 联系方式 */
    @Excel(name = "联系方式", sort = 13)
    private String demandMobilPhone;

    /** 规范书计划提交时间 */
    @Excel(name = "规范书计划提交时间", sort = 14)
    private String techSubmitTime;

    /** 采购申请计划提交时间 */
    @Excel(name = "采购申请计划提交时间", sort = 15)
    private String planSubmitTime;

    /** 采购实施计划启动时间 */
    @Excel(name = "采购实施计划启动时间", sort = 16)
    private String implStartTime;

    /** 采购实施计划完成时间 */
    @Excel(name = "采购实施计划完成时间", sort = 17)
    private String implEndTime;

    /** 合同计划签订时间 */
    @Excel(name = "合同计划签订时间", sort = 18)
    private String contractSignTime;

    /** 调整内容 */
    @Excel(name = "采购计划调整内容", sort = 50)
    private String adjustContent;

    /** 调整原因 */
    @Excel(name = "采购计划调整原因", sort = 51)
    private String adjustReason;

    /** 采购计划状态 */
    //@Excel(name = "采购计划状态", sort = 52, readConverterExp = "1=已纳入年度集采计划,2=计划调整已完成,3=未纳入年度集采计划,4=已纳入年度集采计划,5=计划调整未完成")
    //@Excel(name = "采购计划状态", sort = 52, dictType = "purchase_plan_status")
    private String purchasePlanStatus;

    /** 正式提交采购申请时间 */
    @Excel(name = "正式提交采购申请时间", sort = 60)
    private String formalSubmitTime;

    /** 预计合同签订时间 */
    @Excel(name = "预计合同签订时间", sort = 61)
    private String estimateContractSignTime;

    /** 采购部对接人 */
    @Excel(name = "采购部对接人", sort = 62)
    private String purchaseOppositePerson;

    /** 当前进度 */
    /*@Excel(name = "当前进度", sort = 27, readConverterExp = "1=提交采购申请,2=采购申请预审,3=采购申请会签,4=采购申请终审,5=采购申请受理," +
            "6=技术交流,7=制定采购方案,8=完成采购方案，排会待审议,9=完成采购方案审议，制订招标文件,10=发出招标公告，购买标书," +
            "11=完成标书购买，厂商制定标书,12=开标/谈判,13=采购结果上会,14=采购结果公示,15=采购结果移交，开始制订合同,16=合同会签" +
            "17=合同法审,18=行领导审批,19=用印申请,20=厂商用印,21=行内用印,22=取消,23=暂停,24=制定网络安全审查预判报告,25=完成网络安全审查预判报告," +
            "待排会审议,26=完成网络安全审查预判审议，无需申报,27=完成网络安全审查预判审议，准备申报材料,28=网络安全审查申报送")*/
    @Excel(name = "当前进度", sort = 63, dictType = "current_progress")
    private String currentProgress;

    /** 风险提示 */
    //@Excel(name = "风险提示", sort = 28, readConverterExp = "1=无,2=延期风险,3=逆流程风险,4=采购异常-流标,5=采购异常-需求变化,6=采购异常-预算调整,7=采购异常-其他")
    @Excel(name = "风险提示", sort = 64, dictType = "risk_warning")
    private String riskWarning;

    /** 备注 */
    @Excel(name = "备注", sort = 65)
    private String memo;

    /** 部门领导审批人 */
    private String purchaseApproveId;
    private String purchaseApproveName;

    /** 审批状态，01-待审批；02-已审批 */
    private String purchaseStatus;

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getPurchaseProjectType() {
        return purchaseProjectType;
    }

    public void setPurchaseProjectType(String purchaseProjectType) {
        this.purchaseProjectType = purchaseProjectType;
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

    public String getDemandMobilPhone() {
        return demandMobilPhone;
    }

    public void setDemandMobilPhone(String demandMobilPhone) {
        this.demandMobilPhone = demandMobilPhone;
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

    public String getPurchasePlanStatus() {
        return purchasePlanStatus;
    }

    public void setPurchasePlanStatus(String purchasePlanStatus) {
        this.purchasePlanStatus = purchasePlanStatus;
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
