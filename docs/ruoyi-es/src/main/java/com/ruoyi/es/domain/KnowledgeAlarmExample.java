package com.ruoyi.es.domain;

import com.ruoyi.common.annotation.Excel;

import java.io.Serializable;
import java.util.Map;

/**
 * 参数列别对象 KnowledgeAlarmExample
 * @author dayong_sun
 * @date 2021-06-21
 */
public class KnowledgeAlarmExample implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;
    /** 告警名称 */
    private String title;
    
    /** 系统名称 */
    @Excel(name = "系统名称")
    private String content;
    private String contentId;

    /** 告警来源 */
    @Excel(name = "告警来源")
    private String sectitle;
    private String sectitleId;

    /** 告警描述 */
    @Excel(name = "告警描述")
    private String name;
    
    /** 检索结果 */
    @Excel(name = "检索结果")
    private String knowledgeTitle;
    private String knowledgeId;
    
    /** 请求参数 */
    private Map<String, Object> params;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSectitle() {
        return sectitle;
    }

    public void setSectitle(String sectitle) {
        this.sectitle = sectitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getSectitleId() {
		return sectitleId;
	}

	public void setSectitleId(String sectitleId) {
		this.sectitleId = sectitleId;
	}

	public String getKnowledgeTitle() {
		return knowledgeTitle;
	}

	public void setKnowledgeTitle(String knowledgeTitle) {
		this.knowledgeTitle = knowledgeTitle;
	}

	public String getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
    
    
}
