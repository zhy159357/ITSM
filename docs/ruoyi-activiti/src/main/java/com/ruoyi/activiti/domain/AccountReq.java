package com.ruoyi.activiti.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class AccountReq extends BaseEntity {
	 private static final long serialVersionUID = 1L;

	    /** 主键ID */
	    private String id;
	    /** 姓名 */
	    @Excel(name = "姓名 ")
	    private String name;
	    /** 性别 */
	    @Excel(name = "性别", readConverterExp="1=男,2=女")
	    private int gender;
	    /** 移动电话 */
	    @Excel(name = "移动电话")
	    private String mobPhone;    
	    /** 所属机构 */
	    private String deptid;
	    @Excel(name = "所属机构")
	    private String dept;
	    /** 申请人=createBy 导出用*/
	    @Excel(name = "申请人")
	    private String reqUser;
	    /** 受理处室 */
	    private String auditDeptid;
	    @Excel(name = "审核机构")
	    private String auditDept;
	    /** 状态 */
	    private int mark;    
	    /** 申请时间=createTime 导出用*/
	    @Excel(name = "申请时间")
	    private String reqTime;
	    private String note;
	    
	    /** 学历 */
	    private int education;
	    /** 籍贯 */
	    private String birthPlace;
	    /** 职位 */
	    private String job;
	    /** 负责人 */
	    private int director;
	    /** 顺序 */
	    private long order;
	    /** 生日 */
	    private String birthday;
	    /** 办公电话 */
	    private String officePhone;
	    /** 邮箱 */
	    private String email;
	    /** 家庭住址 */
	    private String address;
	    
	    private String otherPhone;
	    /** 审核人 */
	    private String auditorid;
	    private String auditor;
	    /** 处理人 */
	    private String dealerid;
	    private String dealer;
	    /** 审核意见 */
	    private String auditSug;
	    /** 处理意见 */
	    private String dealSug;
	    
	    private String dealDeptid;
	    private String dealDept;
	    /** 申请单状态*/
	    @Excel(name = "当前状态", readConverterExp="1=待提交,2=待审核,3=待处理,4=已关闭")
	    private int status;
	    
	    private int reqType;
	    private String pid;
	    
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
		public String getMobPhone() {
			return mobPhone;
		}
		public void setMobPhone(String mobPhone) {
			this.mobPhone = mobPhone;
		}
		public int getGender() {
			return gender;
		}
		public void setGender(int gender) {
			this.gender = gender;
		}
		public String getDeptid() {
			return deptid;
		}
		public void setDeptid(String deptid) {
			this.deptid = deptid;
		}
		public String getDept() {
			return dept;
		}
		public void setDept(String dept) {
			this.dept = dept;
		}
		public String getAcceptDept() {
			return auditDept;
		}
		public void setAcceptDept(String auditDept) {
			this.auditDept = auditDept;
		}
		public int getMark() {
			return mark;
		}
		public void setMark(int mark) {
			this.mark = mark;
		}
		public int getEducation() {
			return education;
		}
		public void setEducation(int education) {
			this.education = education;
		}
		public String getBirthPlace() {
			return birthPlace;
		}
		public void setBirthPlace(String birthPlace) {
			this.birthPlace = birthPlace;
		}
		public String getJob() {
			return job;
		}
		public void setJob(String job) {
			this.job = job;
		}
		public int getDirector() {
			return director;
		}
		public void setDirector(int director) {
			this.director = director;
		}
		public long getOrder() {
			return order;
		}
		public void setOrder(long order) {
			this.order = order;
		}
		public String getBirthday() {
			return birthday;
		}
		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}
		public String getOfficePhone() {
			return officePhone;
		}
		public void setOfficePhone(String officePhone) {
			this.officePhone = officePhone;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
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

		public String getAuditor() {
			return auditor;
		}
		public void setAuditor(String auditor) {
			this.auditor = auditor;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getDealer() {
			return dealer;
		}
		public void setDealer(String dealer) {
			this.dealer = dealer;
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
		public String getAuditDept() {
			return auditDept;
		}
		public void setAuditDept(String auditDept) {
			this.auditDept = auditDept;
		}
		public String getOtherPhone() {
			return otherPhone;
		}
		public void setOtherPhone(String otherPhone) {
			this.otherPhone = otherPhone;
		}
		public String getDealDept() {
			return dealDept;
		}
		public void setDealDept(String dealDept) {
			this.dealDept = dealDept;
		}
		public int getReqType() {
			return reqType;
		}
		public void setReqType(int reqType) {
			this.reqType = reqType;
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
		public String getDealerid() {
			return dealerid;
		}
		public void setDealerid(String dealerid) {
			this.dealerid = dealerid;
		}
		public String getDealDeptid() {
			return dealDeptid;
		}
		public void setDealDeptid(String dealDeptid) {
			this.dealDeptid = dealDeptid;
		}
		public String getPid() {
			return pid;
		}
		public void setPid(String pid) {
			this.pid = pid;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		
		
		
	    
	    
}
