package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调度事件单
 */
public class FmDd  implements Serializable {


    private static final long serialVersionUID = 1L;

    private String fmddId;

    @Excel(name = "请求单编号")
    private String faultNo;

    @Excel(name = "主题")
    private String faultTitle;

    @Excel(name = "调度类型", readConverterExp = "1=生产变更报备,2=生产调度申请")
    private String faultType;

    @Excel(name = "当前状态",
            readConverterExp = "1=待提交,2=省内退回,3=全国中心退回,4=待审核,5=待处理,6=处理中,7=已处理,8=待关闭,9=已关闭,10=已作废")
    private String currentState;

    @Excel(name = "创建时间")
    private String createTime;

    @Excel(name = "计划操作时间")
    private String planTime;

    /**
     * 机构id
     */
    private String createorgId;

    @Excel(name = "创建机构")
    private String orgName;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 审核人
     */
    private String checkerId;

    /**
     * 标签
     */
    private String label;

    /**
     * 创建人名称
     */
    private String createName;


    /**
     * 电话
     */
    private String telPhone;

    /**
     * 其他联系电话
     */
    private String otherContact;

    /**
     * 风险等级，0=低,1=中,2=高
     */
    private String urgentLev;

    /**
     * 所处阶段
     */
    private String type;

    /**
     * 调度内容
     */
    private String faultContent;

    /**
     * 联系人
     */
    private String faultContactName;

    /**
     * 参与处理人员ID
     */
    private String participatorIds;

    /**
     * 有效标志
     */
    private String invalidationMark;

    /**
     * 部门id
     */
    private String orgId;

    /**
     * 机构
     */
    private OgOrg ogOrg;

    /**
     * 创建时间至结束之间
     */
    private String endCreateTime;

    /**
     * 结束时间
     */
    private String endPlanTime;

    /**
     * 创建人id
     */
    private String pId;

    /**
     * 创建人名称
     */
    private String pName;

    /**
     * 人员
     */
    private OgPerson ogPerson;

    /**
     * 通过标识  true通过  false不通过
     */
    private String passFlag;

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    /**
     * 调度事件单id集合
     */
    private List<String> ids;

    /**
     * 协同处理的人员id
     */
    private String xtIds;

    public String getXtIds() {
        return xtIds;
    }

    public void setXtIds(String xtIds) {
        this.xtIds = xtIds;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getFmddId() {
        return fmddId;
    }

    public void setFmddId(String fmddId) {
        this.fmddId = fmddId;
    }

    public String getCreateorgId() {
        return createorgId;
    }

    public void setCreateorgId(String createorgId) {
        this.createorgId = createorgId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
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

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getOtherContact() {
        return otherContact;
    }

    public void setOtherContact(String otherContact) {
        this.otherContact = otherContact;
    }

    public String getUrgentLev() {
        return urgentLev;
    }

    public void setUrgentLev(String urgentLev) {
        this.urgentLev = urgentLev;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getFaultContent() {
        return faultContent;
    }

    public void setFaultContent(String faultContent) {
        this.faultContent = faultContent;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getFaultContactName() {
        return faultContactName;
    }

    public void setFaultContactName(String faultContactName) {
        this.faultContactName = faultContactName;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public OgOrg getOgOrg() {
        return ogOrg;
    }

    public void setOgOrg(OgOrg ogOrg) {
        this.ogOrg = ogOrg;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getEndPlanTime() {
        return endPlanTime;
    }

    public void setEndPlanTime(String endPlanTime) {
        this.endPlanTime = endPlanTime;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public OgPerson getOgPerson() {
        return ogPerson;
    }

    public void setOgPerson(OgPerson ogPerson) {
        this.ogPerson = ogPerson;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getPassFlag() {
        return passFlag;
    }

    public void setPassFlag(String passFlag) {
        this.passFlag = passFlag;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "FmDd{" +
                "fmddId='" + fmddId + '\'' +
                ", createorgId='" + createorgId + '\'' +
                ", createId='" + createId + '\'' +
                ", checkerId='" + checkerId + '\'' +
                ", faultNo='" + faultNo + '\'' +
                ", faultTitle='" + faultTitle + '\'' +
                ", faultType='" + faultType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", telPhone='" + telPhone + '\'' +
                ", otherContact='" + otherContact + '\'' +
                ", urgentLev='" + urgentLev + '\'' +
                ", type='" + type + '\'' +
                ", currentState='" + currentState + '\'' +
                ", faultContent='" + faultContent + '\'' +
                ", planTime='" + planTime + '\'' +
                ", faultContactName='" + faultContactName + '\'' +
                ", participatorIds='" + participatorIds + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", orgId='" + orgId + '\'' +
                ", orgName='" + orgName + '\'' +
                ", ogOrg=" + ogOrg +
                ", endCreateTime='" + endCreateTime + '\'' +
                ", endPlanTime='" + endPlanTime + '\'' +
                ", pId='" + pId + '\'' +
                ", pName='" + pName + '\'' +
                ", ogPerson=" + ogPerson +
                '}';
    }
}
