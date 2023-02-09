package com.ruoyi.es.service;

import java.util.List;

import com.ruoyi.es.domain.KnowledgeComment;

public interface KnowledgeCommentService {

	List<KnowledgeComment> selectCommentByContentId(String contentid);
	
	long selectCommentCountByContentId(String contentid, String status);
	
	List<KnowledgeComment> selectCommentByReplyId(String replyid);

	int insertKnowledgeComment(KnowledgeComment knowledgeComment);
}
