package com.ruoyi.activiti.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.annotation.Excel;

/**
 * 事务事件单对象 fm_sw
 * 
 * @author lyk
 * @date 2020-12-16
 */
public class FmSw implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 事件单Id
     */
    private String fmSwId;

    /**
     * （审批）审核人Id
     */
    private String  checkedId;

    /**
     * 授权人Id
     */
    private String  authId;

    /**
     * 创建机构Id
     */
    private String  createOrgId;

    /**
     * 处理机构Id
     */
    private String  dealOrgId;

    /**
     * 创建人
     */
    private String  createId;

    /**
     * 处理人
     */
    private String  dealerId;

    /**
     * 审计人Id
     */
    private String  auditorId;

    /**
     * 审计人名字
     */
    private String auditorName;

    /**
     * 事件编号
     */
    @Excel(name = "事件单编号")
    private String  faultNo;

    /**
     * 事件标题
     */
    @Excel(name = "事件单标题")
    private String  faultTitle;

    /**
     * 请求事项
     */
    @Excel(name = "请求事项")
    private String  faultKind;

    /**
     * 申请机构名称
     */
    @Excel(name = "申请处室")
    private String createOrgName;

    /**
     * 申请人名称
     */
    @Excel(name = "申请人")
    private String createName;


    /**
     * 审批人名字
     */
    @Excel(name = "审核人")
    private String checkedName;

    /**
     * 受理机构名称
     */
    @Excel(name = "受理处室")
    private String dealOrgname;

    /**
     * 授权人姓名
     */
    @Excel(name = "授权人")
    private String  authName;

    /**
     * 处理人名字
     */
    @Excel(name = "处理人")
    private String dealname;

    /**
     * 处理结果
     */
    @Excel(name = "处理结果")
    private String  dealResult;

    /**
     * 处理内容
     */
    @Excel(name = "处理说明")
    private String  dealContent;

    @Excel(name = "申请时间")
    private String createTime;

    /**
     * 创建时间
     */
    private String showCreateTime;


    @Excel(name = "当前状态")
    private String currentStateName;

    /**
     * 审计结果：0：不通过，1：通过
     */
    private String  auditResult;

    /**
     * 审计时间
     */
    private String  auditTime;

    /**
     *当前状态:   1-“待提交”2-“作废”3-“待处理”7-“待审核”8-“待授权” 9-“已关闭”10-“内部审计”
     */

    private String  currentState;

    /**
     * 事务内容
     */
    private String  faultDescription;

    /**
     * 参与处理人员ID,冗余字段，用于查询x处理过的数据
     */
    private String  participatorIds;
    /**
     * 无效标志，0：无效，1：有效
     */
    private String  invalidationMark;

    /**
     * 申请人名字
     *
     */
    private String pname;

    /**
     * 申请处室
     */
    private String orgname;

    /**
     * 受理处室
     */
    private String checkedOrgname;

    private String startCreateTime;

    private String endCreateTime;

    private String label;
    /*只在查询事务事件单中使用*/
    private String createOrgIdLabel;
    /*只在查询事务事件单中使用*/
    private String dealOrgIdLabel;

    /**
     * 操作说明
     */
    private String logPerformDesc;
    /**
     * 事务事件单id集合
     */
    private List<String> ids;
    /** 请求参数 */
    private Map<String, Object> params;

    /**
     * （审批）审核人2Id
     */
    private String  checkerTwoId;

    /**
     * （审批）审核人3Id
     */
    private String  checkerThreeId;

    /**
     * （审批）审核人4Id
     */
    private String  checkerFourId;

    /**
     * 处理人2Id
     */
    private String  dealerTwoId;

    /**
     * 审批流状态 1为三步流程 2为五步流程
     */
    private String  processStatus;

    /**
     * （审批）审核人2
     */
    @Excel(name = "审核人2")
    private String  checkerTwoName;

    /**
     * （审批）审核人3
     */
    @Excel(name = "审核人3")
    private String  checkerThreeName;

    /**
     * （审批）审核人4
     */
    @Excel(name = "审核人4")
    private String  checkerFourName;

    /**
     * 处理人5
     */
    private String  dealerFiveId;

    /**
     * 处理人5名称
     */
    @Excel(name = "处理人5")
    private String  dealerFiveName;

    public String getFmSwId() {
        return fmSwId;
    }

    public void setFmSwId(String fmSwId) {
        this.fmSwId = fmSwId;
    }

    public String getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(String checkedId) {
        this.checkedId = checkedId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getDealOrgId() {
        return dealOrgId;
    }

    public void setDealOrgId(String dealOrgId) {
        this.dealOrgId = dealOrgId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getFaultNo() {
        return faultNo;
    }

    public void setFaultNo(String faultNo) {
        this.faultNo = faultNo;
    }

    public String getFaultTitle() {
        return faultTitle;
    }

    public void setFaultTitle(String faultTitle) {
        this.faultTitle = faultTitle;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getShowCreateTime() {
        return showCreateTime;
    }

    public void setShowCreateTime(String showCreateTime) {
        this.showCreateTime = showCreateTime;
    }

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getDealContent() {
        return dealContent;
    }

    public void setDealContent(String dealContent) {
        this.dealContent = dealContent;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public String getFaultKind() {
        return faultKind;
    }

    public void setFaultKind(String faultKind) {
        this.faultKind = faultKind;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getParticipatorIds() {
        return participatorIds;
    }

    public void setParticipatorIds(String participatorIds) {
        this.participatorIds = participatorIds;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getCheckedName() {
        return checkedName;
    }

    public void setCheckedName(String checkedName) {
        this.checkedName = checkedName;
    }

    public String getCheckedOrgname() {
        return checkedOrgname;
    }

    public void setCheckedOrgname(String checkedOrgname) {
        this.checkedOrgname = checkedOrgname;
    }

    public String getDealname() {
        return dealname;
    }

    public void setDealname(String dealname) {
        this.dealname = dealname;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDealOrgname() {
        return dealOrgname;
    }

    public void setDealOrgname(String dealOrgname) {
        this.dealOrgname = dealOrgname;
    }

    public String getCreateOrgName() {
        return createOrgName;
    }

    public void setCreateOrgName(String createOrgName) {
        this.createOrgName = createOrgName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }


    public String getCreateOrgIdLabel() {
        return createOrgIdLabel;
    }

    public void setCreateOrgIdLabel(String createOrgIdLabel) {
        this.createOrgIdLabel = createOrgIdLabel;
    }

    public String getDealOrgIdLabel() {
        return dealOrgIdLabel;
    }

    public void setDealOrgIdLabel(String dealOrgIdLabel) {
        this.dealOrgIdLabel = dealOrgIdLabel;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getCurrentStateName() {
        return currentStateName;
    }

    public void setCurrentStateName(String currentStateName) {
        this.currentStateName = currentStateName;
    }

    public String getLogPerformDesc() {
        return logPerformDesc;
    }

    public void setLogPerformDesc(String logPerformDesc) {
        this.logPerformDesc = logPerformDesc;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
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

    public String getCheckerTwoId() {
        return checkerTwoId;
    }

    public void setCheckerTwoId(String checkerTwoId) {
        this.checkerTwoId = checkerTwoId;
    }

    public String getCheckerThreeId() {
        return checkerThreeId;
    }

    public void setCheckerThreeId(String checkerThreeId) {
        this.checkerThreeId = checkerThreeId;
    }

    public String getCheckerFourId() {
        return checkerFourId;
    }

    public void setCheckerFourId(String checkerFourId) {
        this.checkerFourId = checkerFourId;
    }

    public String getDealerTwoId() {
        return dealerTwoId;
    }

    public void setDealerTwoId(String dealerTwoId) {
        this.dealerTwoId = dealerTwoId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getCheckerTwoName() {
        return checkerTwoName;
    }

    public void setCheckerTwoName(String checkerTwoName) {
        this.checkerTwoName = checkerTwoName;
    }

    public String getCheckerThreeName() {
        return checkerThreeName;
    }

    public void setCheckerThreeName(String checkerThreeName) {
        this.checkerThreeName = checkerThreeName;
    }

    public String getCheckerFourName() {
        return checkerFourName;
    }

    public void setCheckerFourName(String checkerFourName) {
        this.checkerFourName = checkerFourName;
    }

    public String getDealerFiveId() {
        return dealerFiveId;
    }

    public void setDealerFiveId(String dealerFiveId) {
        this.dealerFiveId = dealerFiveId;
    }

    public String getDealerFiveName() {
        return dealerFiveName;
    }

    public void setDealerFiveName(String dealerFiveName) {
        this.dealerFiveName = dealerFiveName;
    }
}
