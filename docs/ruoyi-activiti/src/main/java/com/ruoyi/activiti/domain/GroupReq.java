package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class GroupReq extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String id;
	/** 工作组名称  */
	@Excel(name = "工作组名称 ")
	private String name;
	/** 工作组电话 */
	private String phone;
	/** 隶属机构 */
	private String deptid;
	@Excel(name = "隶属机构 ")
	private String dept;
	/** 所属应用系统 */
	private String sysid;
	@Excel(name = "所属应用系统 ")
	private String sys;
	/** 工作组说明 */
	@Excel(name = "工作组说明  ")
	private String note;
    /** 申请人=createBy 导出用*/
    @Excel(name = "申请人")
    private String reqUser;
    /** 审核机构 */
    private String auditDeptid;
    @Excel(name = "受理机构")
    private String auditDept;
	/** 数据变更是否业务处长审批 */
	private int director;
	 /** 状态 */
    private int mark;
    /** 申请时间=createTime 导出用*/
    @Excel(name = "申请时间")
    private String reqTime;

    /** 审核人 */
    private String auditorid;
    private String auditor;
    /** 处理机构*/
    private String dealDeptid;
    private String dealDept;
    /** 处理人 */
    private String dealerid;
    private String dealer;
    /** 审核意见 */
    private String auditSug;
    /** 处理意见 */
    private String dealSug;
    
    /** 申请单状态*/
    @Excel(name = "当前状态", readConverterExp="1=待提交,2=待审核,3=待处理,4=已关闭")
    private int status;
    
    private int reqType;
    private String groupid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getSys() {
		return sys;
	}

	public void setSys(String sys) {
		this.sys = sys;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getDirector() {
		return director;
	}

	public void setDirector(int director) {
		this.director = director;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getAuditDept() {
		return auditDept;
	}

	public void setAuditDept(String auditDept) {
		this.auditDept = auditDept;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getDealDept() {
		return dealDept;
	}

	public void setDealDept(String dealDept) {
		this.dealDept = dealDept;
	}

	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getAuditSug() {
		return auditSug;
	}

	public void setAuditSug(String auditSug) {
		this.auditSug = auditSug;
	}

	public String getDealSug() {
		return dealSug;
	}

	public void setDealSug(String dealSug) {
		this.dealSug = dealSug;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GroupReq [id=" + id + ", name=" + name + ", phone=" + phone + ", dept=" + dept + ", sys=" + sys
				+ ", note=" + note + ", director=" + director + ", mark=" + mark + ", auditDept=" + auditDept
				+ ", auditor=" + auditor + ", dealDept=" + dealDept + ", dealer=" + dealer + ", auditSug=" + auditSug
				+ ", dealSug=" + dealSug + ", status=" + status + "]";
	}

	public String getReqUser() {
		return reqUser;
	}

	public void setReqUser(String reqUser) {
		this.reqUser = reqUser;
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public int getReqType() {
		return reqType;
	}

	public void setReqType(int reqType) {
		this.reqType = reqType;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}

	public String getAuditDeptid() {
		return auditDeptid;
	}

	public void setAuditDeptid(String auditDeptid) {
		this.auditDeptid = auditDeptid;
	}

	public String getAuditorid() {
		return auditorid;
	}

	public void setAuditorid(String auditorid) {
		this.auditorid = auditorid;
	}

	public String getDealDeptid() {
		return dealDeptid;
	}

	public void setDealDeptid(String dealDeptid) {
		this.dealDeptid = dealDeptid;
	}

	public String getDealerid() {
		return dealerid;
	}

	public void setDealerid(String dealerid) {
		this.dealerid = dealerid;
	}

    
}
