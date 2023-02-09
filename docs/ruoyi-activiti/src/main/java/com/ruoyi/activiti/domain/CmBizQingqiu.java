package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 变更请求单对象 CM_BIZ_QINGQIU
 * 
 * @author zhangxu
 * @date 2020-12-21
 */
public class CmBizQingqiu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String changeId;

    /** 变更单号 */
    @Excel(name = "变更单号")
    private String changeCode;

    /** 变更标题 */
    @Excel(name = "变更标题")
    private String changeSingleName;

    /*申请人名称*/
    @Excel(name = "申请人")
    private String applyName;

    /*变更分类名称*/
    @Excel(name = "变更分类")
    private String typeName;

    /** 提交时间 */
    @Excel(name = "提交时间")
    private String submitTime;

    /** 状态 */
    @Excel(name = "状态",
            readConverterExp = "0100=待提交,0200=退回待修改,0300=待审批,0400=待受理,0500=已关闭,0600=待分管领导审批,0501=已作废")
    private String stauts;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String addTime;

    /** 创建机构id */
    private String createrOrgId;

    /** 申请人 */
    private String applicantId;

    /** 变更分类 */
    private String changeCategoryId;

    /** 所涉应用系统 */
    private String sysid;

    /** 涉及系统名称 */
    @Excel(name = "涉及系统名称")
    private String sysname;

    /** 风险等级 */
    private String importantLev;

    /** 是否通知业务 */
    private String isNotice;

    /** 是否影响连续性 */
    private String isStop;

    /** 变更请求来源 */
    private String changeResource;

    /** 变更原因 */
    private String changeCauseText;

    /** 变更内容 */
    private String changeDetails;

    /** 受理机构 */
    private String checkOrg;

    /** 受理人 */
    private String checkerId;

    /** 受理时间 */
    private String practicleTime;

    /** 审核人 */
    private String implementorId;

    /** 审核机构 */
    private String implementorOrgid;

    /** 变更所处阶段 */
    private String changeStage;

    /** 无效标记 1有效 0无效*/
    private String invalidationMark;

    /** 分管领导审核人 */
    private String fucheckerId;

    /** 分管领导审核人名称 */
    private String fucheckerName;

    // 通过标识  true通过  false不通过
    private String passFlag;

    /*流程ID*/
    private String instanceId;

    /*审核人名称*/
    private String checkName;

    /*受理人名称*/
    private String acceptanceName;

    /*申请机构名称*/
    private String applyOrgName;

    /*审核机构名称*/
    private String checkOrgName;

    /*受理机构名称*/
    private String acceptanceOrgName;

    /*orleID*/
    private String role;

    /*机构层级码*/
    private String[] state;

    /*我的标识0 我创建的 1  我处理的*/
    private String myIdentity;

    /*协同受理人机构ids*/
    private String[] togetherAcceptanceOrgIds;

    /*协同受理人机构名称*/
    private String togetherAcceptanceOrgName;

    /*协同受理人名称*/
    private String togetherAcceptanceName;

    /*处理人ID集合*/
    private String dealIdList;

    /** 分管领导审核人名称 */
    private String fucheckerFlag;



    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getChangeCode() {
        return changeCode;
    }

    public void setChangeCode(String changeCode) {
        this.changeCode = changeCode;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getCreaterOrgId() {
        return createrOrgId;
    }

    public void setCreaterOrgId(String createrOrgId) {
        this.createrOrgId = createrOrgId;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getChangeCategoryId() {
        return changeCategoryId;
    }

    public void setChangeCategoryId(String changeCategoryId) {
        this.changeCategoryId = changeCategoryId;
    }

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getImportantLev() {
        return importantLev;
    }

    public void setImportantLev(String importantLev) {
        this.importantLev = importantLev;
    }

    public String getIsNotice() {
        return isNotice;
    }

    public void setIsNotice(String isNotice) {
        this.isNotice = isNotice;
    }

    public String getIsStop() {
        return isStop;
    }

    public void setIsStop(String isStop) {
        this.isStop = isStop;
    }

    public String getChangeResource() {
        return changeResource;
    }

    public void setChangeResource(String changeResource) {
        this.changeResource = changeResource;
    }

    public String getChangeSingleName() {
        return changeSingleName;
    }

    public void setChangeSingleName(String changeSingleName) {
        this.changeSingleName = changeSingleName;
    }

    public String getChangeCauseText() {
        return changeCauseText;
    }

    public void setChangeCauseText(String changeCauseText) {
        this.changeCauseText = changeCauseText;
    }

    public String getChangeDetails() {
        return changeDetails;
    }

    public void setChangeDetails(String changeDetails) {
        this.changeDetails = changeDetails;
    }

    public String getCheckOrg() {
        return checkOrg;
    }

    public void setCheckOrg(String checkOrg) {
        this.checkOrg = checkOrg;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getPracticleTime() {
        return practicleTime;
    }

    public void setPracticleTime(String practicleTime) {
        this.practicleTime = practicleTime;
    }

    public String getImplementorId() {
        return implementorId;
    }

    public void setImplementorId(String implementorId) {
        this.implementorId = implementorId;
    }

    public String getImplementorOrgid() {
        return implementorOrgid;
    }

    public void setImplementorOrgid(String implementorOrgid) {
        this.implementorOrgid = implementorOrgid;
    }

    public String getStauts() {
        return stauts;
    }

    public void setStauts(String stauts) {
        this.stauts = stauts;
    }

    public String getChangeStage() {
        return changeStage;
    }

    public void setChangeStage(String changeStage) {
        this.changeStage = changeStage;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getFucheckerId() {
        return fucheckerId;
    }

    public void setFucheckerId(String fucheckerId) {
        this.fucheckerId = fucheckerId;
    }

    public String getFucheckerName() {
        return fucheckerName;
    }

    public void setFucheckerName(String fucheckerName) {
        this.fucheckerName = fucheckerName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getAcceptanceName() {
        return acceptanceName;
    }

    public void setAcceptanceName(String acceptanceName) {
        this.acceptanceName = acceptanceName;
    }

    public String getApplyOrgName() {
        return applyOrgName;
    }

    public void setApplyOrgName(String applyOrgName) {
        this.applyOrgName = applyOrgName;
    }

    public String getCheckOrgName() {
        return checkOrgName;
    }

    public void setCheckOrgName(String checkOrgName) {
        this.checkOrgName = checkOrgName;
    }

    public String getAcceptanceOrgName() {
        return acceptanceOrgName;
    }

    public void setAcceptanceOrgName(String acceptanceOrgName) {
        this.acceptanceOrgName = acceptanceOrgName;
    }

    public String getPassFlag() {
        return passFlag;
    }

    public void setPassFlag(String passFlag) {
        this.passFlag = passFlag;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getState() {
        return state;
    }

    public void setState(String[] state) {
        this.state = state;
    }

    public String getMyIdentity() {
        return myIdentity;
    }

    public void setMyIdentity(String myIdentity) {
        this.myIdentity = myIdentity;
    }

    public String[] getTogetherAcceptanceOrgIds() {
        return togetherAcceptanceOrgIds;
    }

    public void setTogetherAcceptanceOrgIds(String[] togetherAcceptanceOrgIds) {
        this.togetherAcceptanceOrgIds = togetherAcceptanceOrgIds;
    }

    public String getTogetherAcceptanceOrgName() {
        return togetherAcceptanceOrgName;
    }

    public void setTogetherAcceptanceOrgName(String togetherAcceptanceOrgName) {
        this.togetherAcceptanceOrgName = togetherAcceptanceOrgName;
    }

    public String getTogetherAcceptanceName() {
        return togetherAcceptanceName;
    }

    public void setTogetherAcceptanceName(String togetherAcceptanceName) {
        this.togetherAcceptanceName = togetherAcceptanceName;
    }

    public String getDealIdList() {
        return dealIdList;
    }

    public void setDealIdList(String dealIdList) {
        this.dealIdList = dealIdList;
    }

    public String getFucheckerFlag() {
        return fucheckerFlag;
    }

    public void setFucheckerFlag(String fucheckerFlag) {
        this.fucheckerFlag = fucheckerFlag;
    }

    @Override
    public String toString() {
        return "CmBizQingqiu{" +
                "changeId='" + changeId + '\'' +
                ", changeCode='" + changeCode + '\'' +
                ", addTime='" + addTime + '\'' +
                ", createrOrgId='" + createrOrgId + '\'' +
                ", submitTime='" + submitTime + '\'' +
                ", applicantId='" + applicantId + '\'' +
                ", changeCategoryId='" + changeCategoryId + '\'' +
                ", sysId='" + sysid + '\'' +
                ", sysName='" + sysname + '\'' +
                ", importantLev='" + importantLev + '\'' +
                ", isNotice='" + isNotice + '\'' +
                ", isStop='" + isStop + '\'' +
                ", changeResource='" + changeResource + '\'' +
                ", changeSingleName='" + changeSingleName + '\'' +
                ", changeDetails='" + changeDetails + '\'' +
                ", checkOrg='" + checkOrg + '\'' +
                ", checkerId='" + checkerId + '\'' +
                ", practicleTime='" + practicleTime + '\'' +
                ", implementorId='" + implementorId + '\'' +
                ", implementorOrgid='" + implementorOrgid + '\'' +
                ", stauts='" + stauts + '\'' +
                ", changeStage='" + changeStage + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", fucheckerId='" + fucheckerId + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", typeName='" + typeName + '\'' +
                ", applyName='" + applyName + '\'' +
                ", checkName='" + checkName + '\'' +
                ", acceptanceName='" + acceptanceName + '\'' +
                ", applyOrgName='" + applyOrgName + '\'' +
                ", checkOrgName='" + checkOrgName + '\'' +
                ", acceptanceOrgName='" + acceptanceOrgName + '\'' +
                '}';
    }
}
