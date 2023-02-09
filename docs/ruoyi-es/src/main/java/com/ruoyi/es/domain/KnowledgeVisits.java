package com.ruoyi.es.domain;

import java.io.Serializable;

public class KnowledgeVisits  implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String id; 
    private String createId;//访问人ID
    private String contentId; //访问的知识ID
    private String createTime;//访问时间
    private long visitCount;//访问次数
    private KnowledgeContent knowledgeContent;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public long getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(long visitCount) {
		this.visitCount = visitCount;
	}
	public KnowledgeContent getKnowledgeContent() {
		return knowledgeContent;
	}
	public void setKnowledgeContent(KnowledgeContent knowledgeContent) {
		this.knowledgeContent = knowledgeContent;
	}
    
    

}
