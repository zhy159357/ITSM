package com.ruoyi.activiti.domain;

import java.io.Serializable;

public class CmBizSingleSJ   implements Serializable {

    private static final long serialVersionUID = 1L;

    private String changeSingleId;//变更单ID

    private String changeCode;//变更单单号

    private String changeApplicant;//创建人

    private String createTime;//创建时间

    private String changeSingleStatus;//变更单状态

    private String importantLev;//变更风险等级

    private String changeCategoryId;//变更类别Id

    private String expectStartTime; //计划变更开始时间

    private String expectEndTime; //计划变更结束时间

    private String relateSystemId; //涉及系统Id

    private String  checkManager; //审核人

    private String approveManager;//审批人

    private String changeSingleName;//变更主题

    private String changeCauseText; //变更原因

    private String changeDetails; //变更内容

    private String changeProgram; //变更实施方案

    private String ImplementSupviser; //实施监督人（复合人）

    private String practicalStart; //实施开始时间

    private String practicalEnd; //实施结束时间

    private String implementor; //变更实施人(应用系统负责人实施)

    private String implementorPM; //变更实施人(软研项目经理实施)

    private String createOrgId; //创建机构

    private String invalidationMark; //无效标识

    private String sysId; //系统Id

    private String groupId; //工作组Id

    private String fmNo; // 事件编号

    private String fmTitle; //事件标题

    private String chiefManager;

    private String buildChiefManager; //建设处长

    private String changeReason;

    private String changeOrgtype;

    private String changeNum;

    private String chiefyManager;//审核处长

    private String ifAQBL;

    private String sqsPsm;

    private String isTools; //是否使用工具

    private String toolsName; //工具名称

    private String closeTime; //关闭时间

    private String procheckManager;

    private String technologyAuditId; //方案提交人

    private String isChangeOPS;//是否转运维

    private String changeSingleType;//变更单类型

    private String checkOrgName;//业务部门

    private String LOG_performDesc;//退回说明



    private String performDesc;//各个步骤的处理说明

    private String result; //处理结果

    private String logPerformDesc;//关闭说明

    private String changeCopy;//备份方案

    private String changeBack;//回退方案

    //判断新旧数据变更单
    private String sjflag;

    //数据问题单ID
    private String imId;

    //新增数据问题单号
    private String xzimNo;

    //关联数据问题单号
    private String glimNo;

    public String getXzimNo() {
        return xzimNo;
    }

    public void setXzimNo(String xzimNo) {
        this.xzimNo = xzimNo;
    }

    public String getGlimNo() {
        return glimNo;
    }

    public void setGlimNo(String glimNo) {
        this.glimNo = glimNo;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getSjflag() {
        return sjflag;
    }

    public void setSjflag(String sjflag) {
        this.sjflag = sjflag;
    }

    public String getLOG_performDesc() {
        return LOG_performDesc;
    }

    public void setLOG_performDesc(String LOG_performDesc) {
        this.LOG_performDesc = LOG_performDesc;
    }

    public String getCheckOrgName() {
        return checkOrgName;
    }

    public void setCheckOrgName(String checkOrgName) {
        this.checkOrgName = checkOrgName;
    }

    public String getChangeSingleId() {
        return changeSingleId;
    }

    public void setChangeSingleId(String changeSingleId) {
        this.changeSingleId = changeSingleId;
    }

    public String getChangeCode() {
        return changeCode;
    }

    public void setChangeCode(String changeCode) {
        this.changeCode = changeCode;
    }

    public String getChangeApplicant() {
        return changeApplicant;
    }

    public void setChangeApplicant(String changeApplicant) {
        this.changeApplicant = changeApplicant;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getChangeSingleStatus() {
        return changeSingleStatus;
    }

    public void setChangeSingleStatus(String changeSingleStatus) {
        this.changeSingleStatus = changeSingleStatus;
    }

    public String getImportantLev() {
        return importantLev;
    }

    public void setImportantLev(String importantLev) {
        this.importantLev = importantLev;
    }

    public String getChangeCategoryId() {
        return changeCategoryId;
    }

    public void setChangeCategoryId(String changeCategoryId) {
        this.changeCategoryId = changeCategoryId;
    }

    public String getExpectStartTime() {
        return expectStartTime;
    }

    public void setExpectStartTime(String expectStartTime) {
        this.expectStartTime = expectStartTime;
    }

    public String getExpectEndTime() {
        return expectEndTime;
    }

    public void setExpectEndTime(String expectEndTime) {
        this.expectEndTime = expectEndTime;
    }

    public String getRelateSystemId() {
        return relateSystemId;
    }

    public void setRelateSystemId(String relateSystemId) {
        this.relateSystemId = relateSystemId;
    }

    public String getCheckManager() {
        return checkManager;
    }

    public void setCheckManager(String checkManager) {
        this.checkManager = checkManager;
    }

    public String getApproveManager() {
        return approveManager;
    }

    public void setApproveManager(String approveManager) {
        this.approveManager = approveManager;
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

    public String getChangeProgram() {
        return changeProgram;
    }

    public void setChangeProgram(String changeProgram) {
        this.changeProgram = changeProgram;
    }

    public String getImplementSupviser() {
        return ImplementSupviser;
    }

    public void setImplementSupviser(String implementSupviser) {
        ImplementSupviser = implementSupviser;
    }

    public String getPracticalStart() {
        return practicalStart;
    }

    public void setPracticalStart(String practicalStart) {
        this.practicalStart = practicalStart;
    }

    public String getPracticalEnd() {
        return practicalEnd;
    }

    public void setPracticalEnd(String practicalEnd) {
        this.practicalEnd = practicalEnd;
    }

    public String getImplementor() {
        return implementor;
    }

    public void setImplementor(String implementor) {
        this.implementor = implementor;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFmNo() {
        return fmNo;
    }

    public void setFmNo(String fmNo) {
        this.fmNo = fmNo;
    }

    public String getFmTitle() {
        return fmTitle;
    }

    public void setFmTitle(String fmTitle) {
        this.fmTitle = fmTitle;
    }

    public String getChiefManager() {
        return chiefManager;
    }

    public void setChiefManager(String chiefManager) {
        this.chiefManager = chiefManager;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangeOrgtype() {
        return changeOrgtype;
    }

    public void setChangeOrgtype(String changeOrgtype) {
        this.changeOrgtype = changeOrgtype;
    }

    public String getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(String changeNum) {
        this.changeNum = changeNum;
    }

    public String getChiefyManager() {
        return chiefyManager;
    }

    public void setChiefyManager(String chiefyManager) {
        this.chiefyManager = chiefyManager;
    }

    public String getIfAQBL() {
        return ifAQBL;
    }

    public void setIfAQBL(String ifAQBL) {
        this.ifAQBL = ifAQBL;
    }

    public String getSqsPsm() {
        return sqsPsm;
    }

    public void setSqsPsm(String sqsPsm) {
        this.sqsPsm = sqsPsm;
    }

    public String getIsTools() {
        return isTools;
    }

    public void setIsTools(String isTools) {
        this.isTools = isTools;
    }

    public String getToolsName() {
        return toolsName;
    }

    public void setToolsName(String toolsName) {
        this.toolsName = toolsName;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getProcheckManager() {
        return procheckManager;
    }

    public void setProcheckManager(String procheckManager) {
        this.procheckManager = procheckManager;
    }

    public String getTechnologyAuditId() {
        return technologyAuditId;
    }

    public void setTechnologyAuditId(String technologyAuditId) {
        this.technologyAuditId = technologyAuditId;
    }

    public String getImplementorPM() {
        return implementorPM;
    }

    public void setImplementorPM(String implementorPM) {
        this.implementorPM = implementorPM;
    }

    public String getBuildChiefManager() {
        return buildChiefManager;
    }

    public void setBuildChiefManager(String buildChiefManager) {
        this.buildChiefManager = buildChiefManager;
    }

    public String getIsChangeOPS() {
        return isChangeOPS;
    }

    public void setIsChangeOPS(String isChangeOPS) {
        this.isChangeOPS = isChangeOPS;
    }


    public String getChangeSingleType() {
        return changeSingleType;
    }

    public void setChangeSingleType(String changeSingleType) {
        this.changeSingleType = changeSingleType;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLogPerformDesc() {
        return logPerformDesc;
    }

    public void setLogPerformDesc(String logPerformDesc) {
        this.logPerformDesc = logPerformDesc;
    }

    public String getChangeCopy() {
        return changeCopy;
    }

    public void setChangeCopy(String changeCopy) {
        this.changeCopy = changeCopy;
    }

    public String getChangeBack() {
        return changeBack;
    }

    public void setChangeBack(String changeBack) {
        this.changeBack = changeBack;
    }
}
