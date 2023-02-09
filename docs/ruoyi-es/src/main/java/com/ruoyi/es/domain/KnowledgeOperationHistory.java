package com.ruoyi.es.domain;
import com.ruoyi.common.core.domain.BaseEntity;

public class KnowledgeOperationHistory extends BaseEntity {
	
    private static final long serialVersionUID = 1L;
	 /** id */
    private String id;
    /** 知识id */
    private String contentId;
    private String contentTitle;
    /** 操作人 */
    private String operId;
    private String operName;
    /** 操作时间 */
    private String operTime;
    /** 操作 */
    private String operation;
    /** 备注(操作原因) */
    private String remark;
    
    /** 事件类型 */
    private String eventType;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	public String getOperId() {
		return operId;
	}
	public void setOperId(String operId) {
		this.operId = operId;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
    
    
}
