package com.ruoyi.system.http.entegorserver.entity;

/**
 * 用户同步属性
 * 
 * @author Administrator
 *
 */

public class UserSyncApproval {

	// 用户唯一标识
	private String userKey;
	// 全名
	private String userName;
	/** 用戶狀態 */
	private String locked;
	/** 所屬部門 */
	private String department;
	/** 密碼 */
	private String password;
	/** 郵箱 */
	private String email;
	/** 电话号码 */
	private String telphone;
	//
	private String ideCode;

	private String model;

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getIdeCode() {
		return ideCode;
	}

	public void setIdeCode(String ideCode) {
		this.ideCode = ideCode;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
