package com.ruoyi.es.domain;

import java.io.Serializable;
import java.util.List;

public class KnowledgeComment implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String id; 
    private String createId;//评论人ID
    private String createName;//评论人
    private String contentId; //评论的知识ID
    private String title;//评论的知识标题
    private String createTime;//评论时间
    private String remark;//评论内容
    private String status;//好评=0 差评=1
    
    private String replyId;//回复的评论id
    private List<KnowledgeComment> replyList; //该评论的回复

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

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public List<KnowledgeComment> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<KnowledgeComment> replyList) {
		this.replyList = replyList;
	}

	
    
    

}
