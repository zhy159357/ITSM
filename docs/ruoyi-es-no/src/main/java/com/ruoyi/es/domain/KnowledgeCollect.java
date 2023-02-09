package com.ruoyi.es.domain;

import java.io.Serializable;

public class KnowledgeCollect implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String id; 
    private String kcid;
    private String createId;//收藏人ID
    private String contentId; //收藏的知识ID
    private String status;//收藏=0 推荐=1 下载=2
    private String createTime;//創建時間
    private KnowledgeContent knowledgeContent;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getKcid() {
		return kcid;
	}

	public void setKcid(String kcid) {
		this.kcid = kcid;
	}

	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public KnowledgeContent getKnowledgeContent() {
		return knowledgeContent;
	}
	public void setKnowledgeContent(KnowledgeContent knowledgeContent) {
		this.knowledgeContent = knowledgeContent;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
