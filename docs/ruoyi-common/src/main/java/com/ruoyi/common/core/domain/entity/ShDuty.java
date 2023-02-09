package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class ShDuty extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** ID */
	@Excel(name = "值班")
	private String id;
	
	private String dutyid;

	/** 值班组编号 */
	private String userNo;
	private String updateTime;
	
	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	private String group_code;

	/** 日期 */
	@Excel(name = "值班人")
	private String nickname;

	/** 节假日名称 */
	@Excel(name = "节假日名称")
	private String name;

	/** 值班职责 */
	@Excel(name = "值班职责", readConverterExp = "0组长,1组员")
	private String position;
	/** 值班职责 */
	@Excel(name = "值班类别", readConverterExp = "0白班,1晚班")
	private String kind;
	/** 值班电话 */
	@Excel(name = "phone")
	private String phone;

	/** 值班时间,yyyy-MM-dd */
	@Excel(name = "duty_time")
	private String duty_time;
	/** 值班时间,yyyy-MM-dd */
	@Excel(name = "值班说明")
	private String remark;
	/** 审批状态,0:未通过,1:已通过 */
	@Excel(name = "审批状态", readConverterExp = "0未通过,1已通过")
	private String status;
	/** 值班人工号 */
	@Excel(name = "user_no")
	private String user_no;

	@Excel(name = "group_name")
	private String group_name;

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroup_code() {
		return group_code;
	}

	public void setGroup_code(String group_code) {
		this.group_code = group_code;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDuty_time() {
		return duty_time;
	}

	public void setDuty_time(String duty_time) {
		this.duty_time = duty_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser_no() {
		return user_no;
	}

	public void setUser_no(String user_no) {
		this.user_no = user_no;
	}

	public String getDutyid() {
		return dutyid;
	}

	public void setDutyid(String dutyid) {
		this.dutyid = dutyid;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("dutyid", getDutyid()).append("id", getId())
				.append("group_code", getGroup_code()).append("nickname", getNickname()).append("position", getName())
				.append("kind", getPosition()).append("phone", getPhone()).append("duty_time", getDuty_time())
				.append("remark", getRemark()).append("status", getStatus()).append("group_name", getGroup_name())
				.append("user_no", getUser_no()).toString();
	}
}
