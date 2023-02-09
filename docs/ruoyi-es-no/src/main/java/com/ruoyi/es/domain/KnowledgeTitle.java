package com.ruoyi.es.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 参数列别对象 knowledge_category
 * @date 2021-4-14
 */
public class KnowledgeTitle implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 标题id */
    private String id;
    /** 父级标题id */
    private String parentId;
    private String parentName;
    /** 名称 */
    private String name;

    /** 标题等级 */
    private String category;
    /** 事件类型*/
    private String eventType;
    /** 创建人 */
    private String createId;
    private String createName;
    /** 创建时间 */
    private String createTime;
    /** 状态*/
    private String status;

    private int isUsed;
    private String sysId;
    private String sysName;
	private Map<String, Object> params;

	public Map<String, Object> getParams()
	{
		if (params == null)
		{
			params = new HashMap<>();
		}
		return params;
	}

	public void setParams(Map<String, Object> params)
	{
		this.params = params;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

    
    
    

}
