package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【省内自助查询】对象 self_service_query
 *
 * @author liul
 * @date 2021-11-05
 */
public class SelfServiceQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 单号
     */
    @Excel(name = "单号", sort = 1)
    private String no;

    /**
     * 创建机构
     */
    private String createOrgId;

    @Excel(name = "创建机构", sort = 2)
    private String createOrgName;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", sort = 3)
    private String createrTime;

    /**
     * 创建人
     */

    private String createrId;

    @Excel(name = "创建人", sort = 4)
    private String createName;
    /**
     * 创建人电话
     */
    @Excel(name = "创建人电话", sort = 5)
    private String createPhone;

    /**
     * 发生时间
     */
    @Excel(name = "发生时间", sort = 6)
    private String occurrenceTime;

    /**
     * 发生部门
     */
    @Excel(name = "发生部门", sort = 7)
    private String occurrenceAddress;

    /**
     * 客户身份证号
     */
    @Excel(name = "客户身份证号", sort = 8)
    private String customerIdcard;


    /**
     * 所属系统
     */
    private String sysid;
    @Excel(name = "所属系统", sort = 9)
    private String sysName;

    /**
     * 客户姓名
     */
    @Excel(name = "客户姓名", sort = 10)
    private String customerName;

    /**
     * 交易账号
     */
    @Excel(name = "交易账号", sort = 11)
    private String transactionAccount;

    /**
     * 当事人
     */
    @Excel(name = "当事人", sort = 12)
    private String faultReportName;

    /**
     * 当事人电话
     */
    @Excel(name = "当事人电话", sort = 13)
    private String reportPhone;

    /**
     * 事件标题
     */
    @Excel(name = "事件标题", sort = 14)
    private String faultDecriptSummary;

    /**
     * 事件描述
     */
    @Excel(name = "查询理由及事件描述", sort = 15)
    private String faultDecriptDetail;

    /**
     * 计划查询内容
     */
    @Excel(name = "计划查询内容", sort = 16)
    private String planQueryContent;

    /**
     * 截止时间
     */
    @Excel(name = "截止时间", sort = 17)
    private String deadline;

    /**
     * 状态
     */
    @Excel(name = "状态", sort = 18, readConverterExp = "0=已提交,1=草稿,2=关闭")
    private String state;

    /**
     * $column.columnComment
     */
    private String n4;

    @Excel(name = "执行工具次数", sort = 19)
    private String n1;

    /**
     * $column.columnComment
     */
    private String n5;

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getCreateOrgName() {
        return createOrgName;
    }

    public void setCreateOrgName(String createOrgName) {
        this.createOrgName = createOrgName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreaterTime(String createrTime) {
        this.createrTime = createrTime;
    }

    public String getCreaterTime() {
        return createrTime;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreatePhone(String createPhone) {
        this.createPhone = createPhone;
    }

    public String getCreatePhone() {
        return createPhone;
    }

    public void setOccurrenceTime(String occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    public String getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceAddress(String occurrenceAddress) {
        this.occurrenceAddress = occurrenceAddress;
    }

    public String getOccurrenceAddress() {
        return occurrenceAddress;
    }

    public void setCustomerIdcard(String customerIdcard) {
        this.customerIdcard = customerIdcard;
    }

    public String getCustomerIdcard() {
        return customerIdcard;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getSysid() {
        return sysid;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setTransactionAccount(String transactionAccount) {
        this.transactionAccount = transactionAccount;
    }

    public String getTransactionAccount() {
        return transactionAccount;
    }

    public void setFaultReportName(String faultReportName) {
        this.faultReportName = faultReportName;
    }

    public String getFaultReportName() {
        return faultReportName;
    }

    public void setReportPhone(String reportPhone) {
        this.reportPhone = reportPhone;
    }

    public String getReportPhone() {
        return reportPhone;
    }

    public void setFaultDecriptSummary(String faultDecriptSummary) {
        this.faultDecriptSummary = faultDecriptSummary;
    }

    public String getFaultDecriptSummary() {
        return faultDecriptSummary;
    }

    public void setFaultDecriptDetail(String faultDecriptDetail) {
        this.faultDecriptDetail = faultDecriptDetail;
    }

    public String getFaultDecriptDetail() {
        return faultDecriptDetail;
    }

    public void setPlanQueryContent(String planQueryContent) {
        this.planQueryContent = planQueryContent;
    }

    public String getPlanQueryContent() {
        return planQueryContent;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setN4(String n4) {
        this.n4 = n4;
    }

    public String getN4() {
        return n4;
    }

    public void setN1(String n1) {
        this.n1 = n1;
    }

    public String getN1() {
        return n1;
    }

    public void setN5(String n5) {
        this.n5 = n5;
    }

    public String getN5() {
        return n5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("no", getNo())
                .append("createOrgId", getCreateOrgId())
                .append("createrTime", getCreaterTime())
                .append("createrId", getCreaterId())
                .append("createPhone", getCreatePhone())
                .append("occurrenceTime", getOccurrenceTime())
                .append("occurrenceAddress", getOccurrenceAddress())
                .append("customerIdcard", getCustomerIdcard())
                .append("sysid", getSysid())
                .append("customerName", getCustomerName())
                .append("transactionAccount", getTransactionAccount())
                .append("faultReportName", getFaultReportName())
                .append("reportPhone", getReportPhone())
                .append("faultDecriptSummary", getFaultDecriptSummary())
                .append("faultDecriptDetail", getFaultDecriptDetail())
                .append("planQueryContent", getPlanQueryContent())
                .append("deadline", getDeadline())
                .append("state", getState())
                .append("n4", getN4())
                .append("n1", getN1())
                .append("n5", getN5())
                .toString();
    }
}
