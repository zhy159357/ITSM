package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【资源变更单】对象 cm_biz_single
 *
 * @author ruoyi
 * @date 2021-01-13
 */
public class CmBizSingle extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String changeId;

    /**
     * 变更单号
     */
    @Excel(name = "变更单号", sort = 1)
    private String changeCode;

    /**
     * 提交时间
     */
    private String applicationSubmitTime;

    /**
     * 变更分类
     */
    private String changeCategoryId;

    /**
     * 变更所处阶段
     */
    private String changeStage;

    /**
     * 计划变更开始时间
     */
    private String expectStartTime;

    /**
     * 变更申请人
     */
    private String applicantId;

    /**
     * 变更类型
     */
    @Excel(name = "变更类型", sort = 9, readConverterExp = "0=常规变更,1=重大变更,2=简单变更")
    private String changeType;

    /**
     * 变更单状态
     */
    @Excel(name = "变更单状态", sort = 11, readConverterExp = "0100=待提交,0200=退回待修改,0300=待评估,0400=待审批,0500=待实施,0600=作废,0801=成功关闭,0802=回退关闭,0803=引入问题,0804=否决,0401=待副总审批")
    private String changeSingleStauts;

    /**
     * 变更风险等级
     */
    private String importantLev;

    /**
     * 计划变更结束时间
     */
    private String expectEndTime;

    /**
     * 停止时间
     */
    private String closuerOften;

    /**
     * 设备变更情况
     */
    private String changeEquipment;

    /**
     * 设备标识
     */
    private String equipmentFlag;

    /**
     * 变更工作组
     */
    private String groupPerson;

    /**
     * 变更评估人
     */
    private String changeManagerId;

    /**
     * 变更主题
     */
    @Excel(name = "标题", sort = 2)
    private String changeSingleName;

    /**
     * 变更原因
     */
    private String changeCauseText;

    /**
     * 变更内容
     */
    private String changeDetails;

    /**
     * 变更实施方案
     */
    private String changeProgram;

    /**
     * 实施监督人
     */
    private String implementSupviserId;

    /**
     * 实施开始时间
     */
    @Excel(name = "实施时间", sort = 7)
    private String practicleStart;

    /**
     * 实施结束时间
     */
    private String practicleEnd;

    /**
     * 变更影响
     */
    private String changeAffectDesccription;

    /**
     * 附件
     */
    private String accessId;

    /**
     * 备注
     */
    private String notesText;

    /**
     * 有效标志
     */
    private String invalidationMark;

    /**
     * 变更实施人
     */
    private String implementorId;
    @Excel(name = "实施人", sort = 5)
    private String implementorName;
    /**
     * 创建机构
     */
    private String createrOrgId;

    /**
     * 创建机构
     */
    @Excel(name = "创建时间", sort = 8)
    private String createtime;

    /**
     * 处理结果
     */
    @Excel(name = "实施结果", sort = 6, readConverterExp = "0=实施成功,1=实施失败,回退变更前状态,2=实施后引入问题")
    private String performResult;

    /**
     * 变更审批人
     */
    private String checkerId;

    /**
     * 计划是否停止
     */
    private String isStop;

    /**
     * 变更来源
     */
    private String changeResource;

    /**
     * 是否通知
     */
    private String isNotice;

    /**
     * 评估机构
     */
    private String changeOrg;
    @Excel(name = "评估机构", sort = 12)
    private String changeOrgName;
    /**
     * 实施机构
     */
    private String implementorOrgid;

    /**
     * 审批机构
     */
    private String checkOrg;

    @Excel(name = "变更机构", sort = 14)
    private String checkOrgName;
    /**
     * 是否涉及外系统
     */
    private String isRSystem;

    /**
     * 外围系统名称
     */
    private String otherSystem;

    /**
     * 实际是否停止
     */
    private String isStopReal;

    /**
     * 所涉应用系统名称
     */
    @Excel(name = "所涉系统", sort = 4)
    private String sysname;

    /**
     * 所涉应用系统id
     */
    private String sysid;

    /**
     * 协同变更评估人
     */
    private String xtchangeManagerId;

    /**
     * 协同评估机构
     */
    private String changeXtorg;

    /**
     * 是否紧急变更1是0否
     */
    @Excel(name = "是否紧急变更", sort = 13, readConverterExp = "1=是,0=否")
    private String isJjbg;

    /**
     * 是否及时
     */
    private String isJs;

    /**
     * 变更原因
     */
    private String changeReason;

    /**
     * 分管领导审批
     */
    private String fucheckerId;
    //以下只做展示使用
    /**
     * 申请人姓名
     */
    @Excel(name = "申请人", sort = 3)
    private String applicantName;
    /**
     * 分类名
     */
    @Excel(name = "变更分类", sort = 10)
    private String changeCategoryName;
    /**
     * 业务ID
     */
    private String bizId;
    /**
     * 是否通过技术委员会
     */
    private String ifOasis;

    /**
     * 是否自动化变更
     */
    @Excel(name = "是否自动化", sort = 15, readConverterExp = "1=是,0=否")
    private String ifAuto;

    /**
     * 是否手工启动
     */
    private String ifMastart;

    /**
     * 分管领导审阅标志
     */
    private String fucheckerFlag;

    private String ifAddWork;

    public String getIfAddWork() {
        return ifAddWork;
    }

    public void setIfAddWork(String ifAddWork) {
        this.ifAddWork = ifAddWork;
    }

    public String getCheckOrgName() {
        return checkOrgName;
    }

    public void setCheckOrgName(String checkOrgName) {
        this.checkOrgName = checkOrgName;
    }

    public String getIfMastart() {
        return ifMastart;
    }

    public void setIfMastart(String ifMastart) {
        this.ifMastart = ifMastart;
    }

    public String getIfAuto() {
        return ifAuto;
    }

    public void setIfAuto(String ifAuto) {
        this.ifAuto = ifAuto;
    }

    public String getIfOasis() {
        return ifOasis;
    }

    public void setIfOasis(String ifOasis) {
        this.ifOasis = ifOasis;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeCode(String changeCode) {
        this.changeCode = changeCode;
    }

    public String getChangeCode() {
        return changeCode;
    }

    public void setApplicationSubmitTime(String applicationSubmitTime) {
        this.applicationSubmitTime = applicationSubmitTime;
    }

    public String getApplicationSubmitTime() {
        return applicationSubmitTime;
    }

    public void setChangeCategoryId(String changeCategoryId) {
        this.changeCategoryId = changeCategoryId;
    }

    public String getChangeCategoryId() {
        return changeCategoryId;
    }

    public void setChangeStage(String changeStage) {
        this.changeStage = changeStage;
    }

    public String getChangeStage() {
        return changeStage;
    }

    public void setExpectStartTime(String expectStartTime) {
        this.expectStartTime = expectStartTime;
    }

    public String getExpectStartTime() {
        return expectStartTime;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeSingleStauts(String changeSingleStauts) {
        this.changeSingleStauts = changeSingleStauts;
    }

    public String getChangeSingleStauts() {
        return changeSingleStauts;
    }

    public void setImportantLev(String importantLev) {
        this.importantLev = importantLev;
    }

    public String getImportantLev() {
        return importantLev;
    }

    public void setExpectEndTime(String expectEndTime) {
        this.expectEndTime = expectEndTime;
    }

    public String getExpectEndTime() {
        return expectEndTime;
    }

    public void setClosuerOften(String closuerOften) {
        this.closuerOften = closuerOften;
    }

    public String getClosuerOften() {
        return closuerOften;
    }

    public void setChangeEquipment(String changeEquipment) {
        this.changeEquipment = changeEquipment;
    }

    public String getChangeEquipment() {
        return changeEquipment;
    }

    public void setEquipmentFlag(String equipmentFlag) {
        this.equipmentFlag = equipmentFlag;
    }

    public String getEquipmentFlag() {
        return equipmentFlag;
    }

    public void setGroupPerson(String groupPerson) {
        this.groupPerson = groupPerson;
    }

    public String getGroupPerson() {
        return groupPerson;
    }

    public void setChangeManagerId(String changeManagerId) {
        this.changeManagerId = changeManagerId;
    }

    public String getChangeManagerId() {
        return changeManagerId;
    }

    public void setChangeSingleName(String changeSingleName) {
        this.changeSingleName = changeSingleName;
    }

    public String getChangeSingleName() {
        return changeSingleName;
    }

    public void setChangeCauseText(String changeCauseText) {
        this.changeCauseText = changeCauseText;
    }

    public String getChangeCauseText() {
        return changeCauseText;
    }

    public void setChangeDetails(String changeDetails) {
        this.changeDetails = changeDetails;
    }

    public String getChangeDetails() {
        return changeDetails;
    }

    public void setChangeProgram(String changeProgram) {
        this.changeProgram = changeProgram;
    }

    public String getChangeProgram() {
        return changeProgram;
    }

    public void setImplementSupviserId(String implementSupviserId) {
        this.implementSupviserId = implementSupviserId;
    }

    public String getImplementSupviserId() {
        return implementSupviserId;
    }

    public void setPracticleStart(String practicleStart) {
        this.practicleStart = practicleStart;
    }

    public String getPracticleStart() {
        return practicleStart;
    }

    public void setPracticleEnd(String practicleEnd) {
        this.practicleEnd = practicleEnd;
    }

    public String getPracticleEnd() {
        return practicleEnd;
    }

    public void setChangeAffectDesccription(String changeAffectDesccription) {
        this.changeAffectDesccription = changeAffectDesccription;
    }

    public String getChangeAffectDesccription() {
        return changeAffectDesccription;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setNotesText(String notesText) {
        this.notesText = notesText;
    }

    public String getNotesText() {
        return notesText;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setImplementorId(String implementorId) {
        this.implementorId = implementorId;
    }

    public String getImplementorId() {
        return implementorId;
    }

    public void setCreaterOrgId(String createrOrgId) {
        this.createrOrgId = createrOrgId;
    }

    public String getCreaterOrgId() {
        return createrOrgId;
    }

    public void setPerformResult(String performResult) {
        this.performResult = performResult;
    }

    public String getPerformResult() {
        return performResult;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setIsStop(String isStop) {
        this.isStop = isStop;
    }

    public String getIsStop() {
        return isStop;
    }

    public void setChangeResource(String changeResource) {
        this.changeResource = changeResource;
    }

    public String getChangeResource() {
        return changeResource;
    }

    public void setIsNotice(String isNotice) {
        this.isNotice = isNotice;
    }

    public String getIsNotice() {
        return isNotice;
    }

    public void setChangeOrg(String changeOrg) {
        this.changeOrg = changeOrg;
    }

    public String getChangeOrg() {
        return changeOrg;
    }

    public void setImplementorOrgid(String implementorOrgid) {
        this.implementorOrgid = implementorOrgid;
    }

    public String getImplementorOrgid() {
        return implementorOrgid;
    }

    public void setCheckOrg(String checkOrg) {
        this.checkOrg = checkOrg;
    }

    public String getCheckOrg() {
        return checkOrg;
    }

    public void setIsRSystem(String isRSystem) {
        this.isRSystem = isRSystem;
    }

    public String getIsRSystem() {
        return isRSystem;
    }

    public void setOtherSystem(String otherSystem) {
        this.otherSystem = otherSystem;
    }

    public String getOtherSystem() {
        return otherSystem;
    }

    public void setIsStopReal(String isStopReal) {
        this.isStopReal = isStopReal;
    }

    public String getIsStopReal() {
        return isStopReal;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getSysid() {
        return sysid;
    }

    public void setXtchangeManagerId(String xtchangeManagerId) {
        this.xtchangeManagerId = xtchangeManagerId;
    }

    public String getXtchangeManagerId() {
        return xtchangeManagerId;
    }

    public void setChangeXtorg(String changeXtorg) {
        this.changeXtorg = changeXtorg;
    }

    public String getChangeXtorg() {
        return changeXtorg;
    }

    public void setIsJjbg(String isJjbg) {
        this.isJjbg = isJjbg;
    }

    public String getIsJjbg() {
        return isJjbg;
    }

    public void setIsJs(String isJs) {
        this.isJs = isJs;
    }

    public String getIsJs() {
        return isJs;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setFucheckerId(String fucheckerId) {
        this.fucheckerId = fucheckerId;
    }

    public String getFucheckerId() {
        return fucheckerId;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getChangeCategoryName() {
        return changeCategoryName;
    }

    public void setChangeCategoryName(String changeCategoryName) {
        this.changeCategoryName = changeCategoryName;
    }

    public String getImplementorName() {
        return implementorName;
    }

    public void setImplementorName(String implementorName) {
        this.implementorName = implementorName;
    }

    public String getChangeOrgName() {
        return changeOrgName;
    }

    public void setChangeOrgName(String changeOrgName) {
        this.changeOrgName = changeOrgName;
    }

    public String getFucheckerFlag() {
        return fucheckerFlag;
    }

    public void setFucheckerFlag(String fucheckerFlag) {
        this.fucheckerFlag = fucheckerFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("changeId", getChangeId())
                .append("changeCode", getChangeCode())
                .append("createtime", getCreatetime())
                .append("applicationSubmitTime", getApplicationSubmitTime())
                .append("changeCategoryId", getChangeCategoryId())
                .append("changeStage", getChangeStage())
                .append("expectStartTime", getExpectStartTime())
                .append("applicantId", getApplicantId())
                .append("changeType", getChangeType())
                .append("changeSingleStauts", getChangeSingleStauts())
                .append("importantLev", getImportantLev())
                .append("expectEndTime", getExpectEndTime())
                .append("closuerOften", getClosuerOften())
                .append("changeEquipment", getChangeEquipment())
                .append("equipmentFlag", getEquipmentFlag())
                .append("groupPerson", getGroupPerson())
                .append("changeManagerId", getChangeManagerId())
                .append("changeSingleName", getChangeSingleName())
                .append("changeCauseText", getChangeCauseText())
                .append("changeDetails", getChangeDetails())
                .append("changeProgram", getChangeProgram())
                .append("implementSupviserId", getImplementSupviserId())
                .append("practicleStart", getPracticleStart())
                .append("practicleEnd", getPracticleEnd())
                .append("changeAffectDesccription", getChangeAffectDesccription())
                .append("accessId", getAccessId())
                .append("notesText", getNotesText())
                .append("invalidationMark", getInvalidationMark())
                .append("implementorId", getImplementorId())
                .append("createrOrgId", getCreaterOrgId())
                .append("performResult", getPerformResult())
                .append("checkerId", getCheckerId())
                .append("isStop", getIsStop())
                .append("changeResource", getChangeResource())
                .append("isNotice", getIsNotice())
                .append("changeOrg", getChangeOrg())
                .append("implementorOrgid", getImplementorOrgid())
                .append("checkOrg", getCheckOrg())
                .append("isRSystem", getIsRSystem())
                .append("otherSystem", getOtherSystem())
                .append("isStopReal", getIsStopReal())
                .append("sysname", getSysname())
                .append("sysid", getSysid())
                .append("xtchangeManagerId", getXtchangeManagerId())
                .append("changeXtorg", getChangeXtorg())
                .append("isJjbg", getIsJjbg())
                .append("isJs", getIsJs())
                .append("changeReason", getChangeReason())
                .append("fucheckerId", getFucheckerId())
                .toString();
    }
}
