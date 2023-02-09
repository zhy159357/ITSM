package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * wenti对象 im_biz_issuefx
 *
 * @author ruoyi
 * @date 2021-01-04
 */
public class ImBizIssuefx extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    /** ID */
    private String issuefxId;
    /** 问题单号 */
    @Excel(name = "问题单号")
    private String issuefxNo;
    /** 问题标题 */
    @Excel(name = "问题标题")
    private String issuefxName;

    /** 涉及系统 */
    @Excel(name = "涉及系统")
    private String sysName;

    @Excel(name="问题所在机构名")
    private String issueOrgName;
    /** 问题分类 */
    @Excel(name = "问题分类",readConverterExp = "1=系统,2=网络,3=应用,4=动力,5=其他,6=运营")
    private String issueFenlei;
    /** 问题类型 */
    @Excel(name = "问题类型",readConverterExp = "1=引起资金风险,2=产生账务差错,3=引发系统故障,4=引发手工修改数据,5=造成事件单激增,6=产生客户投诉,7=老旧设备,8=应用性能,9=业务流程,10=功能不全,11=灾备系统,12=监控系统,13=信息安全,14=其他隐患,99=其他")
    private String issueType;
    /** 问题来源 */
    @Excel(name = "问题来源",readConverterExp = "1=手工填写,2=业务事件单转,3=运行事件单转,5=数据变更单转")
    private String issuesource;
    /** 创建时间 */
    @Excel(name = "创建时间")
    private String creatTime;
    @Excel(name="问题状态",readConverterExp = "0=待提交,1=待评估,2=待审核,3=待分发,401=待业务复核,5=待技术处理,6=待预解决,7=待业务受理,8=待解决,9=待关闭,10=待修改,11=关闭,12=待数据中心处理,13=待数据中心预解决,14=待数据中心解决,15=作废")
    /** 状态 */
    private String currentState;
    /** 创建人 */
    private String createrId;
    /** 问题等级 */
    @Excel(name = "问题等级",readConverterExp = "1=低,2=中,3=高")
    private String seriousLev;
    /** 问题所在机构 */
  //  @Excel(name = "问题所在机构")
    private String issueOrg;
    /** 审核人 */
    private String reviewerId;
    /** 问题发现人 */
    @Excel(name = "问题发现人")
    private String reportname;

    /** 发现人联系方式 */
    @Excel(name = "发现人联系方式")
    private String reportphone;

    /** 发现时间 */
    @Excel(name = "发现时间")
    private String reporttime;

    /** 问题描述 */
    @Excel(name="问题描述")
    private String issuefxText;

    /** 问题初步分析 */
    private String issuefxScheme;

    /** 处理标识 */
   // @Excel(name = "处理标识")
    private String dealStatus;

    /** 创建机构 */
    private String createOrg;

    /** 处理人ID 数据中心处理人*/
    private String dealId;

    /** 业务部门 */
    private String businessOrg;

    /** 技术经理 */
    @Excel(name="技术经理")
    private String auditId;

    /** 类型 2：业务-问题 3：监控-问题 1：手工 */
    //@Excel(name = "类型 2：业务-问题 3：监控-问题 1：手工")
    private String flag;

    /** 影响描述 */
    @Excel(name="影响")
    private String issuefxImpact;

    /** 处理描述 */
    private String dealDescription;

    /** 转发次数 */
    private long multicount;

    /** 业务经理 */
    private String businessId;

    /** 计划解决方案 */
    @Excel(name = "方案建议")
    private String planDesc;
    /** 实施进度;
     */
    @Excel(name = "整改进度")
    private String unitSchedule;
    /** 业务处理意见 */
    private String buDealDesc;
    /** 验证解决状态 */
    private String solvStatus;

    /** 业务受理人 */
    private String bussId;

    /** 初步分析方案 */
    private String papDesc;

    /** 问题原因分类(问题解决时候由处理人选择) */
    private String issueYyfenlei;

    /** 协同评估人 */
    private String multiusers;

    /** 分发时间 */
    @Excel(name="分发时间")
    private String distributeTime;

    /** 受理时间 */
    @Excel(name="受理时间")
    private String acceptTime;

    /** 预解决时间 */
    @Excel(name="预解决时间")
    private String preSolutionTime;

    /** 解决时间 */
    @Excel(name="解决时间")
    private String solutionTime;
    /** 计划处理时间 */
    @Excel(name="计划处理时间")
    private String expectTime;
    /** 实际完成时间 */
    @Excel(name="实际处理时间")
    private String realityTime;
    @Excel(name="关联资源变更")
    /** 资源变更单 */
    private String csno;
    /** 关联业务事件单 */
    @Excel(name = "关联业务事件单")
    private String fmNo;

    /** 关联数据变更单 */
    @Excel(name = "关联数据变更单")
    private String cmNo;
    /** 关联变更单或版本单号 */
    @Excel(name = "关联监控事件单")
    private String fmjkNo;
    /** 业务复核状态 */
    private String hanguptask;
    private String bufuheDesc;
    private String sysId;
    /**版本发布单号**/
    private String fuNo;
    /**解决情况**/
    private String dealDetail;
    /** 预计解决时间;（中文：不可编辑，时间：不可编辑）
     */
    private String jjTime;

    /** 是否数据中心处理-----“问题归属”
     */
    private String isInside;

    /** 整改实施状态 */
    private String iStatus;
    /** 系统重要级别 */
    private String importLevel;

    /** 最近一次关联故障时间 */
    private String lastTime;

    /** 实施单位;
     */
    private String putUnit;

    /** 无效标志 */
    private String invalidationMark="0";

    /**当前状态问题单所属部门**/
    private String subDept;

    /** 当前一级分类 */
    private String oneType;

    /** 当前一级分类 */
    @Excel(name = "问题原因一级分类")
    private String oneTypeName;//当前一级分类

    /** 当前二级分类 */
    private String twoType;

    /** 当前一级分类 */
    @Excel(name = "问题原因二级分类")
    private String twoTypeName;//导出当前二级分类

    /** 当前三级分类 */
    private String threeType;

    /** 当前一级分类 */
    @Excel(name = "问题原因三级分类")
    private String threeTypeName;//导出当前三级分类

    /** 交易名称 */
    @Excel(name = "交易名称")
    private String tardingName;

    /** 关联事件单个数1 */
    @Excel(name = "关联事件单个数")
    private String relationFmbizCount;
    /** 审核人 */
    private String reviewerName;

    public String getSubDept() {
        return subDept;
    }

    public void setSubDept(String subDept) {
        this.subDept = subDept;
    }

    public String getJjTime() {
        return jjTime;
    }

    public void setJjTime(String jjTime) {
        this.jjTime = jjTime;
    }

    public String getIsInside() {
        return isInside;
    }

    public void setIsInside(String isInside) {
        this.isInside = isInside;
    }

    public String getiStatus() {
        return iStatus;
    }

    public void setiStatus(String iStatus) {
        this.iStatus = iStatus;
    }

    public String getImportLevel() {
        return importLevel;
    }

    public void setImportLevel(String importLevel) {
        this.importLevel = importLevel;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getPutUnit() {
        return putUnit;
    }

    public void setPutUnit(String putUnit) {
        this.putUnit = putUnit;
    }

    public String getUnitSchedule() {
        return unitSchedule;
    }

    public void setUnitSchedule(String unitSchedule) {
        this.unitSchedule = unitSchedule;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getDealDetail() {
        return dealDetail;
    }

    public void setDealDetail(String dealDetail) {
        this.dealDetail = dealDetail;
    }

    public String getFuNo() {
        return fuNo;
    }

    public void setFuNo(String fuNo) {
        this.fuNo = fuNo;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getIssueOrgName() {
        return issueOrgName;
    }

    public void setIssueOrgName(String issueOrgName) {
        this.issueOrgName = issueOrgName;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getBufuheDesc() {
        return bufuheDesc;
    }

    public void setBufuheDesc(String bufuheDesc) {
        this.bufuheDesc = bufuheDesc;
    }

    public void setIssuefxId(String issuefxId)
    {
        this.issuefxId = issuefxId;
    }

    public String getIssuefxId()
    {
        return issuefxId;
    }
    public void setIssuefxNo(String issuefxNo)
    {
        this.issuefxNo = issuefxNo;
    }

    public String getIssuefxNo()
    {
        return issuefxNo;
    }
    public void setCurrentState(String currentState)
    {
        this.currentState = currentState;
    }

    public String getCurrentState()
    {
        return currentState;
    }
    public void setCreaterId(String createrId)
    {
        this.createrId = createrId;
    }

    public String getCreaterId()
    {
        return createrId;
    }
    public void setCreatTime(String creatTime)
    {
        this.creatTime = creatTime;
    }

    public String getCreatTime()
    {
        return creatTime;
    }
    public void setIssueType(String issueType)
    {
        this.issueType = issueType;
    }

    public String getIssueType()
    {
        return issueType;
    }
    public void setIssueFenlei(String issueFenlei)
    {
        this.issueFenlei = issueFenlei;
    }

    public String getIssueFenlei()
    {
        return issueFenlei;
    }
    public void setSeriousLev(String seriousLev)
    {
        this.seriousLev = seriousLev;
    }

    public String getSeriousLev()
    {
        return seriousLev;
    }
    public void setIssueOrg(String issueOrg)
    {
        this.issueOrg = issueOrg;
    }

    public String getIssueOrg()
    {
        return issueOrg;
    }
    public void setReviewerId(String reviewerId)
    {
        this.reviewerId = reviewerId;
    }

    public String getReviewerId()
    {
        return reviewerId;
    }
    public void setIssuesource(String issuesource)
    {
        this.issuesource = issuesource;
    }

    public String getIssuesource()
    {
        return issuesource;
    }
    public void setReportname(String reportname)
    {
        this.reportname = reportname;
    }

    public String getReportname()
    {
        return reportname;
    }
    public void setReportphone(String reportphone)
    {
        this.reportphone = reportphone;
    }

    public String getReportphone()
    {
        return reportphone;
    }
    public void setReporttime(String reporttime)
    {
        this.reporttime = reporttime;
    }

    public String getReporttime()
    {
        return reporttime;
    }

    public void setIssuefxName(String issuefxName)
    {
        this.issuefxName = issuefxName;
    }

    public String getIssuefxName()
    {
        return issuefxName;
    }
    public void setIssuefxText(String issuefxText)
    {
        this.issuefxText = issuefxText;
    }

    public String getIssuefxText()
    {
        return issuefxText;
    }
    public void setIssuefxScheme(String issuefxScheme)
    {
        this.issuefxScheme = issuefxScheme;
    }

    public String getIssuefxScheme()
    {
        return issuefxScheme;
    }
    public void setDealStatus(String dealStatus)
    {
        this.dealStatus = dealStatus;
    }

    public String getDealStatus()
    {
        return dealStatus;
    }
    public void setCreateOrg(String createOrg)
    {
        this.createOrg = createOrg;
    }

    public String getCreateOrg()
    {
        return createOrg;
    }
    public void setFmNo(String fmNo)
    {
        this.fmNo = fmNo;
    }

    public String getFmNo()
    {
        return fmNo;
    }

    public void setDealId(String dealId)
    {
        this.dealId = dealId;
    }

    public String getDealId()
    {
        return dealId;
    }
    public void setBusinessOrg(String businessOrg)
    {
        this.businessOrg = businessOrg;
    }

    public String getBusinessOrg()
    {
        return businessOrg;
    }
    public void setAuditId(String auditId)
    {
        this.auditId = auditId;
    }

    public String getAuditId()
    {
        return auditId;
    }
    public void setFlag(String flag)
    {
        this.flag = flag;
    }

    public String getFlag()
    {
        return flag;
    }
    public void setIssuefxImpact(String issuefxImpact)
    {
        this.issuefxImpact = issuefxImpact;
    }

    public String getIssuefxImpact()
    {
        return issuefxImpact;
    }
    public void setDealDescription(String dealDescription)
    {
        this.dealDescription = dealDescription;
    }

    public String getDealDescription()
    {
        return dealDescription;
    }
    public void setMulticount(long multicount)
    {
        this.multicount = multicount;
    }

    public long getMulticount()
    {
        return multicount;
    }

    public void setExpectTime(String expectTime)
    {
        this.expectTime = expectTime;
    }

    public String getExpectTime()
    {
        return expectTime;
    }
    public void setRealityTime(String realityTime)
    {
        this.realityTime = realityTime;
    }

    public String getRealityTime()
    {
        return realityTime;
    }
    public void setBuDealDesc(String buDealDesc)
    {
        this.buDealDesc = buDealDesc;
    }

    public String getBuDealDesc() {
        return buDealDesc;
    }

    public String getCmNo() {
        return cmNo;
    }

    public void setCmNo(String cmNo) {
        this.cmNo = cmNo;
    }

    public String getFmjkNo() {
        return fmjkNo;
    }

    public void setFmjkNo(String fmjkNo) {
        this.fmjkNo = fmjkNo;
    }

    public void setSolvStatus(String solvStatus)
    {
        this.solvStatus = solvStatus;
    }

    public String getSolvStatus()
    {
        return solvStatus;
    }
    public void setBussId(String bussId)
    {
        this.bussId = bussId;
    }

    public String getBussId()
    {
        return bussId;
    }
    public void setPapDesc(String papDesc)
    {
        this.papDesc = papDesc;
    }

    public String getPapDesc()
    {
        return papDesc;
    }
    public void setPlanDesc(String planDesc)
    {
        this.planDesc = planDesc;
    }

    public String getPlanDesc()
    {
        return planDesc;
    }
    public void setIssueYyfenlei(String issueYyfenlei)
    {
        this.issueYyfenlei = issueYyfenlei;
    }

    public String getIssueYyfenlei()
    {
        return issueYyfenlei;
    }
    public void setMultiusers(String multiusers)
    {
        this.multiusers = multiusers;
    }

    public String getMultiusers()
    {
        return multiusers;
    }
    public void setDistributeTime(String distributeTime)
    {
        this.distributeTime = distributeTime;
    }

    public String getDistributeTime()
    {
        return distributeTime;
    }
    public void setAcceptTime(String acceptTime)
    {
        this.acceptTime = acceptTime;
    }

    public String getAcceptTime()
    {
        return acceptTime;
    }
    public void setPreSolutionTime(String preSolutionTime)
    {
        this.preSolutionTime = preSolutionTime;
    }

    public String getPreSolutionTime()
    {
        return preSolutionTime;
    }
    public void setSolutionTime(String solutionTime)
    {
        this.solutionTime = solutionTime;
    }

    public String getSolutionTime()
    {
        return solutionTime;
    }
    public void setCsno(String csno)
    {
        this.csno = csno;
    }

    public String getCsno()
    {
        return csno;
    }
    public void setHanguptask(String hanguptask)
    {
        this.hanguptask = hanguptask;
    }

    public String getHanguptask()
    {
        return hanguptask;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOneType() {
        return oneType;
    }

    public void setOneType(String oneType) {
        this.oneType = oneType;
    }

    public String getOneTypeName() {
        return oneTypeName;
    }

    public void setOneTypeName(String oneTypeName) {
        this.oneTypeName = oneTypeName;
    }

    public String getTwoType() {
        return twoType;
    }

    public void setTwoType(String twoType) {
        this.twoType = twoType;
    }

    public String getTwoTypeName() {
        return twoTypeName;
    }

    public void setTwoTypeName(String twoTypeName) {
        this.twoTypeName = twoTypeName;
    }

    public String getThreeType() {
        return threeType;
    }

    public void setThreeType(String threeType) {
        this.threeType = threeType;
    }

    public String getThreeTypeName() {
        return threeTypeName;
    }

    public void setThreeTypeName(String threeTypeName) {
        this.threeTypeName = threeTypeName;
    }

    public String getTardingName() {
        return tardingName;
    }

    public void setTardingName(String tardingName) {
        this.tardingName = tardingName;
    }

    public String getRelationFmbizCount() {
        return relationFmbizCount;
    }

    public void setRelationFmbizCount(String relationFmbizCount) {
        this.relationFmbizCount = relationFmbizCount;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("issuefxId", getIssuefxId())
                .append("issuefxNo", getIssuefxNo())
                .append("currentState", getCurrentState())
                .append("createrId", getCreaterId())
                .append("creatTime", getCreatTime())
                .append("issueType", getIssueType())
                .append("issueFenlei", getIssueFenlei())
                .append("seriousLev", getSeriousLev())
                .append("issueOrg", getIssueOrg())
                .append("reviewerId", getReviewerId())
                .append("issuesource", getIssuesource())
                .append("reportname", getReportname())
                .append("reportphone", getReportphone())
                .append("reporttime", getReporttime())
                .append("sysname", getSysName())
                .append("issuefxName", getIssuefxName())
                .append("issuefxText", getIssuefxText())
                .append("issuefxScheme", getIssuefxScheme())
                .append("dealStatus", getDealStatus())
                .append("createOrg", getCreateOrg())
                .append("fmNo", getFmNo())
                .append("cmNo", getCmNo())
                .append("fmjkNo", getFmjkNo())
                .append("dealId", getDealId())
                .append("businessOrg", getBusinessOrg())
                .append("auditId", getAuditId())
                .append("flag", getFlag())
                .append("funo", getFuNo())
                .append("issuefxImpact", getIssuefxImpact())
                .append("dealDescription", getDealDescription())
                .append("multicount", getMulticount())
                .append("businessid", getBusinessId())
                .append("expectTime", getExpectTime())
                .append("realityTime", getRealityTime())
                .append("buDealDesc", getBuDealDesc())
                .append("solvStatus", getSolvStatus())
                .append("bussId", getBussId())
                .append("papDesc", getPapDesc())
                .append("planDesc", getPlanDesc())
                .append("issueYyfenlei", getIssueYyfenlei())
                .append("multiusers", getMultiusers())
                .append("distributeTime", getDistributeTime())
                .append("acceptTime", getAcceptTime())
                .append("preSolutionTime", getPreSolutionTime())
                .append("solutionTime", getSolutionTime())
                .append("csno", getCsno())
                .append("hanguptask", getHanguptask())
                .toString();
    }
}