package com.ruoyi.es.mapper;

import com.ruoyi.es.domain.KnowledgeComment;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface KnowledgeCommentMapper {

	List<KnowledgeComment> selectCommentByContentId(String contentId);

	List<KnowledgeComment> selectCommentByReplyId(String replyId);

	long selectCommentCountByContentId(@Param("contentId")String contentId, @Param("status")String status);

	int insertKnowledgeComment(KnowledgeComment knowledgeComment);


}
