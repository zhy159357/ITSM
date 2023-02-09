package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对象 vm_biz_info
 * 
 * @author ruoyi
 * @date 2020-12-15
 */
public class VmBizInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 版本信息单id */
    private String versionInfoId;
    /** 版本信息单编号 */
    @Excel(name = "版本编号",sort = 1)
    private String versionInfoNo;

    /** 版本信息单名称 */
    @Excel(name = "版本简称",sort = 2)
    private String versionInfoName;
    /** 版本制作人 */
    private String versionProducerId;
    @Excel(name = "版本发起人",sort = 3)
    private String versionProducerName;

    /** 版本创建时间 */
    private String versionCreateTime;
    /** 所属应用系统 */
    private String sysId;

    /** 升级内容摘要 */
    @Excel(name = "版本摘要",sort = 10)
    private String versionName;
    /** 版本信息单编号 */
    private String versionNo;

    /** 功能描述 */
    private String versionDescription;
    /** 版本升级内容 */
    private String versionUpgradeContent;

    /** 业务变更说明 */
    private String businessChangeDescription;
    /** 更新环境 */
    private String upgradeEnvironment;

    /** 涉及变更的内容 */
    private String noticeText;
    /** 版本状态 */
    @Excel(name = "状态",sort = 9,readConverterExp="1=待提交,2=待安全审核,3=待业务/技术审核,4=待业务审核,6=待技术审核," +
            "7=待业务主管审批,8=待技术主管审批,9=待版本审核,10=待运维审核,11=待发布审批,12=已发布,13=待紧急审批,14=作废")
    private String versionStatus;

    /** 升级支持人员 */
    private String technicalSupportId;
    /** 升级支持电话 */
    private String technicalSupportPhone;

    /** 更新时间 */
    private String update_time;
    /** 属性 */
    @Excel(name = "版本属性",sort = 6,readConverterExp="1=版本,2=工具")
    private String versionAttr;

    /**  */
    private String testReport;
    /** 业务测试人员 */
    private String questionId;

    /**  */
    private String sendMsgFlag;
    /** 版本类型 */
    private String versionType;
    private String versionTypeName;

    /** 是否安全审核 */
    private String ifSafetyAudit;
    /** 下发技术局 */
    private String issuedUnit;

    /** 开始升级时间 */
    @Excel(name = "升级时间",sort = 4)
    private String startUpgradeTime;
    /** 完成升级时间 */
    private String endUpgradeTime;

    /** 涉及更新的库 */
    private String acceptOrganization;
    /** 是否紧急审批 */
    private String acceptRole;

    /** 涉及更新的服务 */
    private String createunits;
    /** 涉及重启的服务 */
    private String copyRequiteUnit;

    /**  */
    private String businessState;
    /**  */
    private String technologyState;

    /**  */
    private String vmProActlogId;
    /** 技术测试人员 */
    private String drafterId;

    /** 涉及模块/服务 */
    private String issuedNo;
    /** 安全审核人 */
    private String safetyAuditId;
    private String safetyAuditName;

    /** 技术审核人 */
    private String technologyAuditId;
    private String technologyAuditName;
    /** 业务审核人 */
    private String businessAuditId;
    private String businessAuditName;

    /** 业务主管 */
    private String businessApprovalId;
    private String businessApprovalName;
    /** 技术审批人 */
    private String technologyApprovalId;
    private String technologyApprovalName;

    /** 应用审批人 */
    private String useApprovalId;
    @Excel(name = "版本管理员",sort = 7)
    private String useApprovalName;
    /** 发布审批人 */
    private String uploaderApprovalId;
    private String uploaderApprovalName;

    /** 是否发布公告 */
    private String ifIssueNotice;
    /** 下发分行 */
    private String issuedBranch;

    /** 总行数据中心 */
    private String agencyCenter;
    /** 失效标识 */
    private String invalidationMark;

    /**  */
    private String agencyZx;
    /** 业务需求内容 */
    private String businessRequirementText;

    /** 测试要点 */
    private String businessValidationText;
    /** 是否通报办公室,1/是，2/否 */
    private String ifReportOffice;

    /** 是否报备银保监会,1/是，2/否 */
    private String ifReportCbrc;
    /** 是否通报客服中心,1/是，2/否 */
    private String ifReportCallCenter;

    /** 是否需停业升级,1/是，2/否 */
    private String ifStopBusiness;
    /** 所涉应用系统名称 */
    private String sysName;

    /** 所涉应用系统ID */
    private String sys_Id;
    /** 应用处长审批人 */
    private String useDivisionChiefApprovalId;
    @Excel(name = "运维主管",sort = 8)
    private String useDivisionChiefApprovalName;

    /** 紧急审批人(徐总) */
    private String jjspApprovalId;
    private String jjspApprovalName;
    /** 业务部门 */
    private String businessOrg;

    /** 应用系统作为关联用*/
    private OgSys ogSys;
    /**所属系统页面导出用*/
    @Excel(name = "所属系统",sort = 5)
    private String systemName;

    /**是否自动化*/
    @Excel(name = "是否自动化",sort = 11, readConverterExp="1=是,2=否")
    private String isAutomate;
    /**自动化实施人*/
    private String automateAuditId;
    private String automateAuditName;

    //下发分行名称，页面展示用
    private String issuedBranchName;
    //下发分局名称，页面展示用
    private String issuedUnitName;
    //总行数据中心名称，页面展示用
    private String agencyCenterName;

    // 通过标识  true通过  false不通过
    private String passFlag;

    public String getVersionInfoId() {
        return versionInfoId;
    }

    public void setVersionInfoId(String versionInfoId) {
        this.versionInfoId = versionInfoId;
    }

    public String getVersionInfoNo() {
        return versionInfoNo;
    }

    public void setVersionInfoNo(String versionInfoNo) {
        this.versionInfoNo = versionInfoNo;
    }

    public String getVersionInfoName() {
        return versionInfoName;
    }

    public void setVersionInfoName(String versionInfoName) {
        this.versionInfoName = versionInfoName;
    }

    public String getVersionProducerId() {
        return versionProducerId;
    }

    public void setVersionProducerId(String versionProducerId) {
        this.versionProducerId = versionProducerId;
    }

    public String getVersionCreateTime() {
        return versionCreateTime;
    }

    public void setVersionCreateTime(String versionCreateTime) {
        this.versionCreateTime = versionCreateTime;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    public String getVersionUpgradeContent() {
        return versionUpgradeContent;
    }

    public void setVersionUpgradeContent(String versionUpgradeContent) {
        this.versionUpgradeContent = versionUpgradeContent;
    }

    public String getBusinessChangeDescription() {
        return businessChangeDescription;
    }

    public void setBusinessChangeDescription(String businessChangeDescription) {
        this.businessChangeDescription = businessChangeDescription;
    }

    public String getUpgradeEnvironment() {
        return upgradeEnvironment;
    }

    public void setUpgradeEnvironment(String upgradeEnvironment) {
        this.upgradeEnvironment = upgradeEnvironment;
    }

    public String getNoticeText() {
        return noticeText;
    }

    public void setNoticeText(String noticeText) {
        this.noticeText = noticeText;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(String versionStatus) {
        this.versionStatus = versionStatus;
    }

    public String getTechnicalSupportId() {
        return technicalSupportId;
    }

    public void setTechnicalSupportId(String technicalSupportId) {
        this.technicalSupportId = technicalSupportId;
    }

    public String getTechnicalSupportPhone() {
        return technicalSupportPhone;
    }

    public void setTechnicalSupportPhone(String technicalSupportPhone) {
        this.technicalSupportPhone = technicalSupportPhone;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getVersionAttr() {
        return versionAttr;
    }

    public void setVersionAttr(String versionAttr) {
        this.versionAttr = versionAttr;
    }

    public String getTestReport() {
        return testReport;
    }

    public void setTestReport(String testReport) {
        this.testReport = testReport;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSendMsgFlag() {
        return sendMsgFlag;
    }

    public void setSendMsgFlag(String sendMsgFlag) {
        this.sendMsgFlag = sendMsgFlag;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getIfSafetyAudit() {
        return ifSafetyAudit;
    }

    public void setIfSafetyAudit(String ifSafetyAudit) {
        this.ifSafetyAudit = ifSafetyAudit;
    }

    public String getIssuedUnit() {
        return issuedUnit;
    }

    public void setIssuedUnit(String issuedUnit) {
        this.issuedUnit = issuedUnit;
    }

    public String getStartUpgradeTime() {
        return startUpgradeTime;
    }

    public void setStartUpgradeTime(String startUpgradeTime) {
        this.startUpgradeTime = startUpgradeTime;
    }

    public String getEndUpgradeTime() {
        return endUpgradeTime;
    }

    public void setEndUpgradeTime(String endUpgradeTime) {
        this.endUpgradeTime = endUpgradeTime;
    }

    public String getAcceptOrganization() {
        return acceptOrganization;
    }

    public void setAcceptOrganization(String acceptOrganization) {
        this.acceptOrganization = acceptOrganization;
    }

    public String getAcceptRole() {
        return acceptRole;
    }

    public void setAcceptRole(String acceptRole) {
        this.acceptRole = acceptRole;
    }

    public String getCreateunits() {
        return createunits;
    }

    public void setCreateunits(String createunits) {
        this.createunits = createunits;
    }

    public String getCopyRequiteUnit() {
        return copyRequiteUnit;
    }

    public void setCopyRequiteUnit(String copyRequiteUnit) {
        this.copyRequiteUnit = copyRequiteUnit;
    }

    public String getBusinessState() {
        return businessState;
    }

    public void setBusinessState(String businessState) {
        this.businessState = businessState;
    }

    public String getTechnologyState() {
        return technologyState;
    }

    public void setTechnologyState(String technologyState) {
        this.technologyState = technologyState;
    }

    public String getVmProActlogId() {
        return vmProActlogId;
    }

    public void setVmProActlogId(String vmProActlogId) {
        this.vmProActlogId = vmProActlogId;
    }

    public String getDrafterId() {
        return drafterId;
    }

    public void setDrafterId(String drafterId) {
        this.drafterId = drafterId;
    }

    public String getIssuedNo() {
        return issuedNo;
    }

    public void setIssuedNo(String issuedNo) {
        this.issuedNo = issuedNo;
    }

    public String getSafetyAuditId() {
        return safetyAuditId;
    }

    public void setSafetyAuditId(String safetyAuditId) {
        this.safetyAuditId = safetyAuditId;
    }

    public String getTechnologyAuditId() {
        return technologyAuditId;
    }

    public void setTechnologyAuditId(String technologyAuditId) {
        this.technologyAuditId = technologyAuditId;
    }

    public String getBusinessAuditId() {
        return businessAuditId;
    }

    public void setBusinessAuditId(String businessAuditId) {
        this.businessAuditId = businessAuditId;
    }

    public String getBusinessApprovalId() {
        return businessApprovalId;
    }

    public void setBusinessApprovalId(String businessApprovalId) {
        this.businessApprovalId = businessApprovalId;
    }

    public String getTechnologyApprovalId() {
        return technologyApprovalId;
    }

    public void setTechnologyApprovalId(String technologyApprovalId) {
        this.technologyApprovalId = technologyApprovalId;
    }

    public String getUseApprovalId() {
        return useApprovalId;
    }

    public void setUseApprovalId(String useApprovalId) {
        this.useApprovalId = useApprovalId;
    }

    public String getUploaderApprovalId() {
        return uploaderApprovalId;
    }

    public void setUploaderApprovalId(String uploaderApprovalId) {
        this.uploaderApprovalId = uploaderApprovalId;
    }

    public String getIfIssueNotice() {
        return ifIssueNotice;
    }

    public void setIfIssueNotice(String ifIssueNotice) {
        this.ifIssueNotice = ifIssueNotice;
    }

    public String getIssuedBranch() {
        return issuedBranch;
    }

    public void setIssuedBranch(String issuedBranch) {
        this.issuedBranch = issuedBranch;
    }

    public String getAgencyCenter() {
        return agencyCenter;
    }

    public void setAgencyCenter(String agencyCenter) {
        this.agencyCenter = agencyCenter;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getAgencyZx() {
        return agencyZx;
    }

    public void setAgencyZx(String agencyZx) {
        this.agencyZx = agencyZx;
    }

    public String getBusinessRequirementText() {
        return businessRequirementText;
    }

    public void setBusinessRequirementText(String businessRequirementText) {
        this.businessRequirementText = businessRequirementText;
    }

    public String getBusinessValidationText() {
        return businessValidationText;
    }

    public void setBusinessValidationText(String businessValidationText) {
        this.businessValidationText = businessValidationText;
    }

    public String getIfReportOffice() {
        return ifReportOffice;
    }

    public void setIfReportOffice(String ifReportOffice) {
        this.ifReportOffice = ifReportOffice;
    }

    public String getIfReportCbrc() {
        return ifReportCbrc;
    }

    public void setIfReportCbrc(String ifReportCbrc) {
        this.ifReportCbrc = ifReportCbrc;
    }

    public String getIfReportCallCenter() {
        return ifReportCallCenter;
    }

    public void setIfReportCallCenter(String ifReportCallCenter) {
        this.ifReportCallCenter = ifReportCallCenter;
    }

    public String getIfStopBusiness() {
        return ifStopBusiness;
    }

    public void setIfStopBusiness(String ifStopBusiness) {
        this.ifStopBusiness = ifStopBusiness;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSys_Id() {
        return sys_Id;
    }

    public void setSys_Id(String sys_Id) {
        this.sys_Id = sys_Id;
    }

    public String getUseDivisionChiefApprovalId() {
        return useDivisionChiefApprovalId;
    }

    public void setUseDivisionChiefApprovalId(String useDivisionChiefApprovalId) {
        this.useDivisionChiefApprovalId = useDivisionChiefApprovalId;
    }

    public String getJjspApprovalId() {
        return jjspApprovalId;
    }

    public void setJjspApprovalId(String jjspApprovalId) {
        this.jjspApprovalId = jjspApprovalId;
    }

    public String getBusinessOrg() {
        return businessOrg;
    }

    public void setBusinessOrg(String businessOrg) {
        this.businessOrg = businessOrg;
    }

    public OgSys getOgSys() {
        return ogSys;
    }

    public void setOgSys(OgSys ogSys) {
        if(ogSys != null){
            this.systemName = ogSys.getCaption();
        }else{
            ogSys = new OgSys();
        }
        this.ogSys = ogSys;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getIssuedBranchName() {
        return issuedBranchName;
    }

    public void setIssuedBranchName(String issuedBranchName) {
        this.issuedBranchName = issuedBranchName;
    }

    public String getIssuedUnitName() {
        return issuedUnitName;
    }

    public void setIssuedUnitName(String issuedUnitName) {
        this.issuedUnitName = issuedUnitName;
    }

    public String getAgencyCenterName() {
        return agencyCenterName;
    }

    public void setAgencyCenterName(String agencyCenterName) {
        this.agencyCenterName = agencyCenterName;
    }

    public String getSafetyAuditName() {
        return safetyAuditName;
    }

    public void setSafetyAuditName(String safetyAuditName) {
        this.safetyAuditName = safetyAuditName;
    }

    public String getTechnologyAuditName() {
        return technologyAuditName;
    }

    public void setTechnologyAuditName(String technologyAuditName) {
        this.technologyAuditName = technologyAuditName;
    }

    public String getBusinessAuditName() {
        return businessAuditName;
    }

    public void setBusinessAuditName(String businessAuditName) {
        this.businessAuditName = businessAuditName;
    }

    public String getBusinessApprovalName() {
        return businessApprovalName;
    }

    public void setBusinessApprovalName(String businessApprovalName) {
        this.businessApprovalName = businessApprovalName;
    }

    public String getTechnologyApprovalName() {
        return technologyApprovalName;
    }

    public void setTechnologyApprovalName(String technologyApprovalName) {
        this.technologyApprovalName = technologyApprovalName;
    }

    public String getUseApprovalName() {
        return useApprovalName;
    }

    public void setUseApprovalName(String useApprovalName) {
        this.useApprovalName = useApprovalName;
    }

    public String getUploaderApprovalName() {
        return uploaderApprovalName;
    }

    public void setUploaderApprovalName(String uploaderApprovalName) {
        this.uploaderApprovalName = uploaderApprovalName;
    }

    public String getUseDivisionChiefApprovalName() {
        return useDivisionChiefApprovalName;
    }

    public void setUseDivisionChiefApprovalName(String useDivisionChiefApprovalName) {
        this.useDivisionChiefApprovalName = useDivisionChiefApprovalName;
    }

    public String getJjspApprovalName() {
        return jjspApprovalName;
    }

    public void setJjspApprovalName(String jjspApprovalName) {
        this.jjspApprovalName = jjspApprovalName;
    }

    public String getVersionProducerName() {
        return versionProducerName;
    }

    public void setVersionProducerName(String versionProducerName) {
        this.versionProducerName = versionProducerName;
    }

    public String getVersionTypeName() {
        return versionTypeName;
    }

    public void setVersionTypeName(String versionTypeName) {
        this.versionTypeName = versionTypeName;
    }

    public String getPassFlag() {
        return passFlag;
    }

    public void setPassFlag(String passFlag) {
        this.passFlag = passFlag;
    }

    public String getIsAutomate() {
        return isAutomate;
    }

    public void setIsAutomate(String isAutomate) {
        this.isAutomate = isAutomate;
    }

    public String getAutomateAuditId() {
        return automateAuditId;
    }

    public void setAutomateAuditId(String automateAuditId) {
        this.automateAuditId = automateAuditId;
    }

    public String getAutomateAuditName() {
        return automateAuditName;
    }

    public void setAutomateAuditName(String automateAuditName) {
        this.automateAuditName = automateAuditName;
    }

    @Override
    public String toString() {
        return "VmBizInfo{" +
                "versionInfoId='" + versionInfoId + '\'' +
                ", versionInfoNo='" + versionInfoNo + '\'' +
                ", versionInfoName='" + versionInfoName + '\'' +
                ", versionProducerId='" + versionProducerId + '\'' +
                ", versionProducerName='" + versionProducerName + '\'' +
                ", versionCreateTime='" + versionCreateTime + '\'' +
                ", sysId='" + sysId + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionNo='" + versionNo + '\'' +
                ", versionDescription='" + versionDescription + '\'' +
                ", versionUpgradeContent='" + versionUpgradeContent + '\'' +
                ", businessChangeDescription='" + businessChangeDescription + '\'' +
                ", upgradeEnvironment='" + upgradeEnvironment + '\'' +
                ", noticeText='" + noticeText + '\'' +
                ", versionStatus='" + versionStatus + '\'' +
                ", technicalSupportId='" + technicalSupportId + '\'' +
                ", technicalSupportPhone='" + technicalSupportPhone + '\'' +
                ", update_time='" + update_time + '\'' +
                ", versionAttr='" + versionAttr + '\'' +
                ", testReport='" + testReport + '\'' +
                ", questionId='" + questionId + '\'' +
                ", sendMsgFlag='" + sendMsgFlag + '\'' +
                ", versionType='" + versionType + '\'' +
                ", versionTypeName='" + versionTypeName + '\'' +
                ", ifSafetyAudit='" + ifSafetyAudit + '\'' +
                ", issuedUnit='" + issuedUnit + '\'' +
                ", startUpgradeTime='" + startUpgradeTime + '\'' +
                ", endUpgradeTime='" + endUpgradeTime + '\'' +
                ", acceptOrganization='" + acceptOrganization + '\'' +
                ", acceptRole='" + acceptRole + '\'' +
                ", createunits='" + createunits + '\'' +
                ", copyRequiteUnit='" + copyRequiteUnit + '\'' +
                ", businessState='" + businessState + '\'' +
                ", technologyState='" + technologyState + '\'' +
                ", vmProActlogId='" + vmProActlogId + '\'' +
                ", drafterId='" + drafterId + '\'' +
                ", issuedNo='" + issuedNo + '\'' +
                ", safetyAuditId='" + safetyAuditId + '\'' +
                ", safetyAuditName='" + safetyAuditName + '\'' +
                ", technologyAuditId='" + technologyAuditId + '\'' +
                ", technologyAuditName='" + technologyAuditName + '\'' +
                ", businessAuditId='" + businessAuditId + '\'' +
                ", businessAuditName='" + businessAuditName + '\'' +
                ", businessApprovalId='" + businessApprovalId + '\'' +
                ", businessApprovalName='" + businessApprovalName + '\'' +
                ", technologyApprovalId='" + technologyApprovalId + '\'' +
                ", technologyApprovalName='" + technologyApprovalName + '\'' +
                ", useApprovalId='" + useApprovalId + '\'' +
                ", useApprovalName='" + useApprovalName + '\'' +
                ", uploaderApprovalId='" + uploaderApprovalId + '\'' +
                ", uploaderApprovalName='" + uploaderApprovalName + '\'' +
                ", ifIssueNotice='" + ifIssueNotice + '\'' +
                ", issuedBranch='" + issuedBranch + '\'' +
                ", agencyCenter='" + agencyCenter + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", agencyZx='" + agencyZx + '\'' +
                ", businessRequirementText='" + businessRequirementText + '\'' +
                ", businessValidationText='" + businessValidationText + '\'' +
                ", ifReportOffice='" + ifReportOffice + '\'' +
                ", ifReportCbrc='" + ifReportCbrc + '\'' +
                ", ifReportCallCenter='" + ifReportCallCenter + '\'' +
                ", ifStopBusiness='" + ifStopBusiness + '\'' +
                ", sysName='" + sysName + '\'' +
                ", sys_Id='" + sys_Id + '\'' +
                ", useDivisionChiefApprovalId='" + useDivisionChiefApprovalId + '\'' +
                ", useDivisionChiefApprovalName='" + useDivisionChiefApprovalName + '\'' +
                ", jjspApprovalId='" + jjspApprovalId + '\'' +
                ", jjspApprovalName='" + jjspApprovalName + '\'' +
                ", businessOrg='" + businessOrg + '\'' +
                ", ogSys=" + ogSys +
                ", systemName='" + systemName + '\'' +
                ", isAutomate='" + isAutomate + '\'' +
                ", automateAuditId='" + automateAuditId + '\'' +
                ", automateAuditName='" + automateAuditName + '\'' +
                ", issuedBranchName='" + issuedBranchName + '\'' +
                ", issuedUnitName='" + issuedUnitName + '\'' +
                ", agencyCenterName='" + agencyCenterName + '\'' +
                ", passFlag='" + passFlag + '\'' +
                '}';
    }
}
