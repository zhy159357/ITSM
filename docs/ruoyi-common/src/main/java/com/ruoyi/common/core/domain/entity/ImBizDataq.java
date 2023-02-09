package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 im_biz_dataq
 *
 * @author ruoyi
 * @date 2021-09-28
 */
public class ImBizDataq extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数据问题单ID
     */
    private String imId;

    /**
     * 数据问题单号
     */
    @Excel(name = "数据问题单号")
    private String imNo;

    /**
     * 问题单状态 01-待提交 02-待分发 03-待受理 04-受理中 05-待评估 06-待关闭 07-已关闭 08-作废
     */
    @Excel(name = "问题单状态 01-待提交 02-待分发 03-待受理 04-受理中 05-待评估 06-待关闭 07-已关闭 08-作废")
    private String currentState;

    /**
     * 创建人ID
     */
    @Excel(name = "创建人")
    private String createrId;

    /**
     * 创建人名称
     */
    @Excel(name = "创建人名称")
    private String createName;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    private String creatTime;

    /**
     * 创建结束时间
     */
    private String endCreateTime;

    /**
     * 问题发生时间
     */
    @Excel(name = "问题发生时间")
    private String problemDiscoveryTime;

    /**
     * 问题结束时间
     */
    private String endproblemDiscoveryTime;

    /**
     * 涉及系统ID
     */
    @Excel(name = "涉及系统ID")
    private String sysId;

    /**
     * 涉及系统名称
     */
    @Excel(name = "涉及系统名称")
    private String sysName;

    /**
     * 问题标题
     */
    @Excel(name = "问题标题")
    private String probleTitle;

    /**
     * 问题描述
     */
    @Excel(name = "问题描述")
    private String probleDesc;

    /**
     * 问题起因
     */
    @Excel(name = "问题起因")
    private String probleCause;

    /**
     * 问题影响
     */
    @Excel(name = "问题影响")
    private String probleImpact;

    /**
     * 应急预案
     */
    @Excel(name = "应急预案")
    private String emergencyPlan;

    /**
     * 业务部门
     */
    @Excel(name = "业务部门")
    private String businessDept;

    /**
     * 业务审核人
     */
    @Excel(name = "业务审核人")
    private String businessId;
    /**
     * 业务审核人名称
     */
    private String businessAuditName;


    /**
     * 业务整改方案
     */
    @Excel(name = "业务整改方案")
    private String businessRectificationPlan;

    /**
     * 业务计划解决时间
     */
    @Excel(name = "业务计划解决时间")
    private String businessPlanTime;

    /**
     * 业务解决情况
     */
    @Excel(name = "业务解决情况")
    private String businessSolutions;

    /**
     * 受否需要技术部门继续解决
     */
    @Excel(name = "受否需要技术部门继续解决")
    private String ifJs;

    /**
     * 技术部门责任人
     */
    @Excel(name = "技术部门责任人")
    private String technicalId;

    /**
     * 技术部门责任人名称
     */
    @Excel(name = "技术部门责任人")
    private String technicalName;

    /**
     * 技术整改方案
     */
    @Excel(name = "技术整改方案")
    private String technicalRectificationPlan;

    /**
     * 技术计划解决时间
     */
    @Excel(name = "技术计划解决时间")
    private String technicalPlanTime;

    /**
     * 技术解决情况
     */
    @Excel(name = "技术解决情况")
    private String technicalSolutions;

    /**
     * 关联版本单号
     */
    @Excel(name = "关联版本单号")
    private String versionNo;

    /**
     * 是否设置观察期
     */
    @Excel(name = "是否设置观察期")
    private String ifPuo;

    /**
     * 观察时间
     */
    @Excel(name = "观察时间")
    private String observationTime;

    /**
     * 验证解决状态 -issue_deal_result
     */
    @Excel(name = "验证解决状态")
    private String vrStatus;

    /**
     * 分管行领导
     */
    private String chargeLeadership;

    /**
     * 系统重要级别
     */
    private String sysLv;

    /**
     * $column.columnComment
     */
    private String n6;

    /**
     * $column.columnComment
     */
    private String n7;

    /**
     * $column.columnComment
     */
    private String n8;

    /**
     * $column.columnComment
     */
    private String n9;

    /**
     * $column.columnComment
     */
    private String n10;

    /**
     * 业务领导审核人ID
     */
    @Excel(name = "业务领导审核人")
    private String businessAppid;

    /**
     * 业务领导审核人名称
     */
    private String businessApprovalName;


    /**
     * 业务整改责任部门
     */
    @Excel(name = "业务整改责任部门")
    private String businessRectificationDept;

    /**
     * 业务整改责任人
     */
    @Excel(name = "业务整改责任人")
    private String businessRectificationId;


    /**
     * 是否业务
     */
    private String isState;

    /**
     * 有效标志
     */
    private String invalidationMark;

    //评估人ID
    private String assessorId;

    //评估人名称
    private String assessorName;

    /**
     * 判断条件
     */
    private String wtflag;

    public String getWtflag() {
        return wtflag;
    }

    public void setWtflag(String wtflag) {
        this.wtflag = wtflag;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getImNo() {
        return imNo;
    }

    public void setImNo(String imNo) {
        this.imNo = imNo;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getProblemDiscoveryTime() {
        return problemDiscoveryTime;
    }

    public void setProblemDiscoveryTime(String problemDiscoveryTime) {
        this.problemDiscoveryTime = problemDiscoveryTime;
    }

    public String getEndproblemDiscoveryTime() {
        return endproblemDiscoveryTime;
    }

    public void setEndproblemDiscoveryTime(String endproblemDiscoveryTime) {
        this.endproblemDiscoveryTime = endproblemDiscoveryTime;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getProbleTitle() {
        return probleTitle;
    }

    public void setProbleTitle(String probleTitle) {
        this.probleTitle = probleTitle;
    }

    public String getProbleDesc() {
        return probleDesc;
    }

    public void setProbleDesc(String probleDesc) {
        this.probleDesc = probleDesc;
    }

    public String getProbleCause() {
        return probleCause;
    }

    public void setProbleCause(String probleCause) {
        this.probleCause = probleCause;
    }

    public String getProbleImpact() {
        return probleImpact;
    }

    public void setProbleImpact(String probleImpact) {
        this.probleImpact = probleImpact;
    }

    public String getEmergencyPlan() {
        return emergencyPlan;
    }

    public void setEmergencyPlan(String emergencyPlan) {
        this.emergencyPlan = emergencyPlan;
    }

    public String getBusinessDept() {
        return businessDept;
    }

    public void setBusinessDept(String businessDept) {
        this.businessDept = businessDept;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessAuditName() {
        return businessAuditName;
    }

    public void setBusinessAuditName(String businessAuditName) {
        this.businessAuditName = businessAuditName;
    }

    public String getBusinessRectificationPlan() {
        return businessRectificationPlan;
    }

    public void setBusinessRectificationPlan(String businessRectificationPlan) {
        this.businessRectificationPlan = businessRectificationPlan;
    }

    public String getBusinessPlanTime() {
        return businessPlanTime;
    }

    public void setBusinessPlanTime(String businessPlanTime) {
        this.businessPlanTime = businessPlanTime;
    }

    public String getBusinessSolutions() {
        return businessSolutions;
    }

    public void setBusinessSolutions(String businessSolutions) {
        this.businessSolutions = businessSolutions;
    }

    public String getIfJs() {
        return ifJs;
    }

    public void setIfJs(String ifJs) {
        this.ifJs = ifJs;
    }

    public String getTechnicalId() {
        return technicalId;
    }

    public void setTechnicalId(String technicalId) {
        this.technicalId = technicalId;
    }

    public String getTechnicalRectificationPlan() {
        return technicalRectificationPlan;
    }

    public void setTechnicalRectificationPlan(String technicalRectificationPlan) {
        this.technicalRectificationPlan = technicalRectificationPlan;
    }

    public String getTechnicalPlanTime() {
        return technicalPlanTime;
    }

    public void setTechnicalPlanTime(String technicalPlanTime) {
        this.technicalPlanTime = technicalPlanTime;
    }

    public String getTechnicalSolutions() {
        return technicalSolutions;
    }

    public void setTechnicalSolutions(String technicalSolutions) {
        this.technicalSolutions = technicalSolutions;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getIfPuo() {
        return ifPuo;
    }

    public void setIfPuo(String ifPuo) {
        this.ifPuo = ifPuo;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }

    public String getVrStatus() {
        return vrStatus;
    }

    public void setVrStatus(String vrStatus) {
        this.vrStatus = vrStatus;
    }

    public String getChargeLeadership() {
        return chargeLeadership;
    }

    public void setChargeLeadership(String chargeLeadership) {
        this.chargeLeadership = chargeLeadership;
    }


    public String getSysLv() {
        return sysLv;
    }

    public void setSysLv(String sysLv) {
        this.sysLv = sysLv;
    }

    public String getN6() {
        return n6;
    }

    public void setN6(String n6) {
        this.n6 = n6;
    }

    public String getN7() {
        return n7;
    }

    public void setN7(String n7) {
        this.n7 = n7;
    }

    public String getN8() {
        return n8;
    }

    public void setN8(String n8) {
        this.n8 = n8;
    }

    public String getN9() {
        return n9;
    }

    public void setN9(String n9) {
        this.n9 = n9;
    }

    public String getN10() {
        return n10;
    }

    public void setN10(String n10) {
        this.n10 = n10;
    }

    public String getBusinessAppid() {
        return businessAppid;
    }

    public void setBusinessAppid(String businessAppid) {
        this.businessAppid = businessAppid;
    }

    public String getBusinessApprovalName() {
        return businessApprovalName;
    }

    public void setBusinessApprovalName(String businessApprovalName) {
        this.businessApprovalName = businessApprovalName;
    }

    public String getBusinessRectificationDept() {
        return businessRectificationDept;
    }

    public void setBusinessRectificationDept(String businessRectificationDept) {
        this.businessRectificationDept = businessRectificationDept;
    }

    public String getBusinessRectificationId() {
        return businessRectificationId;
    }

    public void setBusinessRectificationId(String businessRectificationId) {
        this.businessRectificationId = businessRectificationId;
    }

    public String getIsState() {
        return isState;
    }

    public void setIsState(String isState) {
        this.isState = isState;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getAssessorId() {
        return assessorId;
    }

    public void setAssessorId(String assessorId) {
        this.assessorId = assessorId;
    }

    public String getAssessorName() {
        return assessorName;
    }

    public void setAssessorName(String assessorName) {
        this.assessorName = assessorName;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }
}