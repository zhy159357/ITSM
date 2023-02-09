package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【疑难事件】对象 difficult_events
 *
 * @author liul
 * @date 2021-11-23
 */
public class DifficultEvents extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 单号
     */
    @Excel(name = "单号", sort = 1)
    private String no;

    /**
     * 部门
     */
    private String dept;
    @Excel(name = "所属部门", sort = 2)
    private String deptName;

    /**
     * 运维事件单ID
     */
    private String fmId;
    @Excel(name = "关联事件单号", sort = 11)
    private String fmNo;
    /**
     * 归属系统ID
     */
    private String sysId;
    @Excel(name = "所属系统", sort = 3)
    private String sysName;

    /**
     * 归属工作组ID
     */
    private String groupId;
    @Excel(name = "所属工作组", sort = 4)
    private String groupName;

    /**
     * 处理人ID
     */

    private String dealId;
    @Excel(name = "处理人", sort = 6)
    private String dealName;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", sort = 5)
    private String createrTime;

    /**
     * 处理时间
     */
    @Excel(name = "处理时间", sort = 7)
    private String dealTime;

    /**
     * 处理描述
     */
    @Excel(name = "处理描述", sort = 8)
    private String dealDesc;

    /**
     * 状态
     */
    @Excel(name = "状态", sort = 10, readConverterExp = "01=待处理,02=已处理")
    private String status;

    /**
     * 来源
     */
    @Excel(name = "来源", sort = 9, readConverterExp = "1=手工转,2=处理超时,3=步骤超时")
    private String source;

    private String n1;

    private String n2;

    private String n3;

    private String n4;

    private FmBiz fmBiz;

    public FmBiz getFmBiz() {
        return fmBiz;
    }

    public void setFmBiz(FmBiz fmBiz) {
        if (fmBiz != null) {
            this.fmNo = fmBiz.getFaultNo();
        } else {
            fmBiz = new FmBiz();
        }
        this.fmBiz = fmBiz;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
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

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDept() {
        return dept;
    }

    public void setFmId(String fmId) {
        this.fmId = fmId;
    }

    public String getFmId() {
        return fmId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setCreaterTime(String createrTime) {
        this.createrTime = createrTime;
    }

    public String getCreaterTime() {
        return createrTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealDesc(String dealDesc) {
        this.dealDesc = dealDesc;
    }

    public String getDealDesc() {
        return dealDesc;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setN1(String n1) {
        this.n1 = n1;
    }

    public String getN1() {
        return n1;
    }

    public void setN2(String n2) {
        this.n2 = n2;
    }

    public String getN2() {
        return n2;
    }

    public void setN3(String n3) {
        this.n3 = n3;
    }

    public String getN3() {
        return n3;
    }

    public void setN4(String n4) {
        this.n4 = n4;
    }

    public String getN4() {
        return n4;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("no", getNo())
                .append("dept", getDept())
                .append("fmId", getFmId())
                .append("sysId", getSysId())
                .append("groupId", getGroupId())
                .append("dealId", getDealId())
                .append("createrTime", getCreaterTime())
                .append("dealTime", getDealTime())
                .append("dealDesc", getDealDesc())
                .append("status", getStatus())
                .append("source", getSource())
                .append("n1", getN1())
                .append("n2", getN2())
                .append("n3", getN3())
                .append("n4", getN4())
                .toString();
    }
}
