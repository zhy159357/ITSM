package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;

public class CustomFormJsonVo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** ID */
	private String id;
	private String form_id;
	private String version;
	private String data;
	private String increased_data;
	private int is_current;
	private String name;
	private int type;
	private String code;
	private String charset;
	private String html;
	private String json;
	private int status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getForm_id() {
		return form_id;
	}

	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getIncreased_data() {
		return increased_data;
	}

	public void setIncreased_data(String increased_data) {
		this.increased_data = increased_data;
	}

	public int getIs_current() {
		return is_current;
	}

	public void setIs_current(int is_current) {
		this.is_current = is_current;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
