package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class SysReq extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String id;
	/** 系统编码  */
	@Excel(name = "系统编码 ")
	private String code;
	/** 系统名称  */
	@Excel(name = "系统名称 ")
	private String name;
	/** 所属机构 */
	private String organid;
	@Excel(name = "所属机构 ")
	private String organ;
	/** 所属处室 */
	private String officeid;
	private String office;
	/** 所属业务部门 */
	private String deptid;
	private String dept;
	/** 所属负责人 */
	private String chargeid;
	private String charge;
	/** 系统类别  */
	private String sysclassid;
	@Excel(name = "系统类别 ")
	private String sysclass;
	/** 参与考核  */
	private int check;
	/** 是否重要系统  */
	private int important;
	/** 申请人=createBy 导出用*/
    @Excel(name = "申请人")
    private String reqUser;
    /** 审核机构 */
    private String auditDeptid;
    @Excel(name = "受理处室")
    private String auditDept;
	 /** 状态 */
    private int mark;  
    /** 申请时间=createTime 导出用*/
    @Excel(name = "创建日期")
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getSysclass() {
		return sysclass;
	}

	public void setSysclass(String sysclass) {
		this.sysclass = sysclass;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public int getImportant() {
		return important;
	}

	public void setImportant(int important) {
		this.important = important;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getReqUser() {
		return reqUser;
	}

	public void setReqUser(String reqUser) {
		this.reqUser = reqUser;
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

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public String getOrganid() {
		return organid;
	}

	public String getOfficeid() {
		return officeid;
	}

	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public void setOrganid(String organid) {
		this.organid = organid;
	}

	public String getSysclassid() {
		return sysclassid;
	}

	public void setSysclassid(String sysclassid) {
		this.sysclassid = sysclassid;
	}

	public String getChargeid() {
		return chargeid;
	}

	public void setChargeid(String chargeid) {
		this.chargeid = chargeid;
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
