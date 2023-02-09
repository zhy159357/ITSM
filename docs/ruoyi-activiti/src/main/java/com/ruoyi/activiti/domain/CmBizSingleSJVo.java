package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmBizSingleSJVo implements Serializable {
    private String changeSingleId;//变更单ID

    @Excel(name = "变更单号")
    private String changeCode;//变更单单号

    @Excel(name = "事件单号")
    private String fmNo; //业务事件单单号

    @Excel(name = "标题")
    private String fmTitle;

    @Excel(name = "创建人")
    private String changeApplicantName;//创建人名称

    @Excel(name = "审核人")
    private String checkManagerName;//审核人名称

    @Excel(name = "业务部门")
    private String checkOrgName;//业务部门

    @Excel(name = "变更实施人")
    private String implementorName;//实施人名称

    @Excel(name = "审核处长")
    private String chiefyManagerName;//审核处长名字

    @Excel(name = "关闭人")
    private String closeName;

    private String changeApplicant;//创建人

    @Excel(name = "创建时间")
    private String createTimeText;

    @Excel(name = "数据变更类别")
    private String changeCategoryName;//变更类别名称

    @Excel(name = "所属应用系统")
    private String sysName;//所属应用系统名称

    @Excel(name = "所属工作组")
    private String groupName;//所属工作组名称

    private String isToolsName;

    @Excel(name = "是否使用工具",readConverterExp = "0=否,1=是")
    private String isTools;

    @Excel(name = "关闭时间")
    private String closeTime; //关闭时间


    @Excel(name = "状态")
    private String changeSingleStatusName;//变更单状态

    private String createTime;//创建时间

    private String[] statusArray;

    private String changeSingleStatus;//变更单状态

    private String importantLev;//变更风险等级

    private String changeCategoryId;//变更类别Id

    private String expectStartTime; //计划变更开始时间

    private String expectEndTime; //计划变更结束时间

    private String relateSystemId; //涉及系统Id

    private String  checkManager; //审核人

    private String approveManager;//审批人

    @Excel(name = "变更主题")
    private String changeSingleName;//变更主题

    @Excel(name = "变更原因")
    private String changeCauseText; //变更原因

    @Excel(name = "变更内容")
    private String changeDetails; //变更内容

    @Excel(name = "变更实施方案")
    private String changeProgram; //变更实施方案

    private String ImplementSupviser; //实施监督人

    private String practicalStart; //实施开始时间

    private String practicalEnd; //实施结束时间

    private String implementor; //变更实施人

    private String createOrgId; //创建机构

    private String invalidationMark; //无效标识

    private String sysId; //系统Id

    private String groupId; //工作组Id

    private String chiefManager;

    private String changeReason; //变更起因

    private String changeOrgtype; //变更省份

    private String changeNum; //变更条数

    private String chiefyManager; //应用处长审核处长



    private String ifAQBL;

    private String sqsPsm;

    private String toolsName;



    private String startcloseTime; //关闭开始时间

    private String endcloseTime; //关闭结束时间



    private String procheckManager; //省处理人

    private String technologyAuditId; //方案提交人

    private String isChangeOPS;//是否转运维

    private String implementorPM; //变更实施人(软研项目经理实施)

    private String buildChiefManager; //建设处长

    private String updateFlag;

    private String flag;//提交标识

    private String technologyAuditName;//方案提出人名字

    private String procheckManagerName;//省处理人名字

    private String changeOrgtypeName;// 变更省份名称

    private Integer startChangeNum;//起始变更条数

    private Integer endChangeNum;//结束变更条数

    private Date startCreateDate;//开始创建时间

    private Date endCreateDate;// 结束创建时间

    private String startCreateTime;//开始创建时间

    private String endCreateTime;// 结束创建时间

    private String changeSingleType;//变更单类型

    private String orgName;//工具统计

    private String caption;

    private String cmBizSjNum;

    private String isToolsNum;

    private String countEfficiency;

    private String cmBizSjCountType;//统计类型
    /**等级编号*/
    private String levelCode;

    @Excel(name = "备份方案")
    private String changeCopy;//备份方案

    @Excel(name = "回退方案")
    private String changeBack;//回退方案

    private String LOG_performDesc;//退回说明

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

    /** 请求参数 */
    private Map<String, Object> params;

    /**
     * 事务事件单id集合
     */
    private List<String> ids;

    //之前没有的报错之后添加的
    private String chiefManagerName;

    public String getStartcloseTime() {
        return startcloseTime;
    }

    public void setStartcloseTime(String startcloseTime) {
        this.startcloseTime = startcloseTime;
    }

    public String getEndcloseTime() {
        return endcloseTime;
    }

    public void setEndcloseTime(String endcloseTime) {
        this.endcloseTime = endcloseTime;
    }

    public String getChiefManagerName() {
        return chiefManagerName;
    }

    public void setChiefManagerName(String chiefManagerName) {
        this.chiefManagerName = chiefManagerName;
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

    public String getChangeApplicantName() {
        return changeApplicantName;
    }

    public void setChangeApplicantName(String changeApplicantName) {
        this.changeApplicantName = changeApplicantName;
    }

    public String getCheckManagerName() {
        return checkManagerName;
    }

    public void setCheckManagerName(String checkManagerName) {
        this.checkManagerName = checkManagerName;
    }

    public String getImplementorName() {
        return implementorName;
    }

    public void setImplementorName(String implementorName) {
        this.implementorName = implementorName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public String getIsChangeOPS() {
        return isChangeOPS;
    }

    public void setIsChangeOPS(String isChangeOPS) {
        this.isChangeOPS = isChangeOPS;
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


    public String getProcheckManagerName() {
        return procheckManagerName;
    }

    public void setProcheckManagerName(String procheckManagerName) {
        this.procheckManagerName = procheckManagerName;
    }

    public String getChangeOrgtypeName() {
        return changeOrgtypeName;
    }

    public void setChangeOrgtypeName(String changeOrgtypeName) {
        this.changeOrgtypeName = changeOrgtypeName;
    }

    public Integer getStartChangeNum() {
        return startChangeNum;
    }

    public void setStartChangeNum(Integer startChangeNum) {
        this.startChangeNum = startChangeNum;
    }

    public Integer getEndChangeNum() {
        return endChangeNum;
    }

    public void setEndChangeNum(Integer endChangeNum) {
        this.endChangeNum = endChangeNum;
    }

    public Date getStartCreateDate() {
        return startCreateDate;
    }

    public void setStartCreateDate(Date startCreateDate) {
        this.startCreateDate = startCreateDate;
    }

    public Date getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(Date endCreateDate) {
        this.endCreateDate = endCreateDate;
    }

    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getChangeSingleType() {
        return changeSingleType;
    }

    public void setChangeSingleType(String changeSingleType) {
        this.changeSingleType = changeSingleType;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }


    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getCloseName() {
        return closeName;
    }

    public void setCloseName(String closeName) {
        this.closeName = closeName;
    }

    public String getChangeCategoryName() {
        return changeCategoryName;
    }

    public void setChangeCategoryName(String changeCategoryName) {
        this.changeCategoryName = changeCategoryName;
    }

    public String getChangeSingleStatusName() {
        return changeSingleStatusName;
    }

    public void setChangeSingleStatusName(String changeSingleStatusName) {
        this.changeSingleStatusName = changeSingleStatusName;
    }

    public String[] getStatusArray() {
        return statusArray;
    }

    public void setStatusArray(String[] statusArray) {
        this.statusArray = statusArray;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCmBizSjNum() {
        return cmBizSjNum;
    }

    public void setCmBizSjNum(String cmBizSjNum) {
        this.cmBizSjNum = cmBizSjNum;
    }

    public String getIsToolsNum() {
        return isToolsNum;
    }

    public void setIsToolsNum(String isToolsNum) {
        this.isToolsNum = isToolsNum;
    }

    public String getCountEfficiency() {
        return countEfficiency;
    }

    public void setCountEfficiency(String countEfficiency) {
        this.countEfficiency = countEfficiency;
    }

    public String getCmBizSjCountType() {
        return cmBizSjCountType;
    }

    public void setCmBizSjCountType(String cmBizSjCountType) {
        this.cmBizSjCountType = cmBizSjCountType;
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

    public String getIsToolsName() {
        return isToolsName;
    }

    public void setIsToolsName(String isToolsName) {
        this.isToolsName = isToolsName;
    }

    public String getChiefyManagerName() {
        return chiefyManagerName;
    }

    public void setChiefyManagerName(String chiefyManagerName) {
        this.chiefyManagerName = chiefyManagerName;
    }

    public String getTechnologyAuditName() {
        return technologyAuditName;
    }

    public void setTechnologyAuditName(String technologyAuditName) {
        this.technologyAuditName = technologyAuditName;
    }

    public String getCreateTimeText() {
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }
}
