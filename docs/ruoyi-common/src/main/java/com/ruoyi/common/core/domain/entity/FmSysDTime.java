package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业务事件单系统处理用时对象 fm_sys_d_time
 *
 * @author ruoyi
 * @date 2021-01-21
 */
public class FmSysDTime extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String fmSysDTimeId;

    /**
     * 事件单ID
     */
    private String bizId;

    /**
     * 系统ID
     */
    private String sysId;

    /**
     * 操作名称
     */
    private String performName;

    private String dealUseTime;

    private String groupId;
    /**
     * 步骤处理人
     */
    private String dealId;
    /**
     * 处理时间
     */
    @Excel(name = "处理时间", sort = 14)
    private String dealTime;
    @Excel(name = "步骤处理人", sort = 12)
    private String dealName;
    @Excel(name = "事件单编号", sort = 1)
    private String faultNo;
    @Excel(name = "事件标题", sort = 2)
    private String faultDecriptSummary;
    @Excel(name = "所属系统", sort = 3)
    private String systemName;
    @Excel(name = "所属工作组", sort = 4)
    private String createGroupName;
    @Excel(name = "事件分类", sort = 5)
    private String fmKindName;
    @Excel(name = "是否紧急", sort = 6, readConverterExp = "1=是,2=否")
    private String ifJj;
    @Excel(name = "事件起因", sort = 7, readConverterExp = "01=业务人员培训不到位,02=版本或变更引起,03=系统功能不完善或不满足,04=信息系统原因,05=系统程序BUG,06=数据一致性问题,07=行内关联系统原因,08=客户操作问题,09=第三方或他行系统原因.10=省内系统原因,11=数据分析相关咨询,12=其他或疑难,13=业务人员操作有误")
    private String fmCause;
    @Excel(name = "事件等级", sort = 8, readConverterExp = "1=低,2=中,3=高")
    private String seriousLev;
    @Excel(name = "当前状态:", sort = 9, readConverterExp = "01=待提交,02=待分派,03=待处理,04=处理中,05=退回,06=待评价,07=省中心退回,08=待领取(省),09=关闭,10=作废,11=处理中(省)")
    private String currentState;
    @Excel(name = "创建人", sort = 10)
    private String createName;
    @Excel(name = "创建时间", sort = 11)
    private String creatTime;
    @Excel(name = "转全国中心时间", sort = 13)
    private String toQgzxTime;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getFaultNo() {
        return faultNo;
    }

    public void setFaultNo(String faultNo) {
        this.faultNo = faultNo;
    }

    public String getFaultDecriptSummary() {
        return faultDecriptSummary;
    }

    public void setFaultDecriptSummary(String faultDecriptSummary) {
        this.faultDecriptSummary = faultDecriptSummary;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCreateGroupName() {
        return createGroupName;
    }

    public void setCreateGroupName(String createGroupName) {
        this.createGroupName = createGroupName;
    }

    public String getFmKindName() {
        return fmKindName;
    }

    public void setFmKindName(String fmKindName) {
        this.fmKindName = fmKindName;
    }

    public String getIfJj() {
        return ifJj;
    }

    public void setIfJj(String ifJj) {
        this.ifJj = ifJj;
    }

    public String getFmCause() {
        return fmCause;
    }

    public void setFmCause(String fmCause) {
        this.fmCause = fmCause;
    }

    public String getSeriousLev() {
        return seriousLev;
    }

    public void setSeriousLev(String seriousLev) {
        this.seriousLev = seriousLev;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
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

    public String getToQgzxTime() {
        return toQgzxTime;
    }

    public void setToQgzxTime(String toQgzxTime) {
        this.toQgzxTime = toQgzxTime;
    }

    public void setFmSysDTimeId(String fmSysDTimeId) {
        this.fmSysDTimeId = fmSysDTimeId;
    }

    public String getFmSysDTimeId() {
        return fmSysDTimeId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setPerformName(String performName) {
        this.performName = performName;
    }

    public String getPerformName() {
        return performName;
    }

    public void setDealUseTime(String dealUseTime) {
        this.dealUseTime = dealUseTime;
    }

    public String getDealUseTime() {
        return dealUseTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fmSysDTimeId", getFmSysDTimeId())
                .append("bizId", getBizId())
                .append("sysId", getSysId())
                .append("performName", getPerformName())
                .append("dealUseTime", getDealUseTime())
                .toString();
    }
}
