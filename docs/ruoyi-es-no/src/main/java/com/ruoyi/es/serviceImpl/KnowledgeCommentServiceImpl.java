package com.ruoyi.es.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.es.domain.KnowledgeComment;
import com.ruoyi.es.mapper.KnowledgeCommentMapper;
import com.ruoyi.es.service.KnowledgeCommentService;

//@Service
public class KnowledgeCommentServiceImpl implements KnowledgeCommentService{
    @Autowired
    private KnowledgeCommentMapper knowledgeCommentMapper;
    
	@Override
	public List<KnowledgeComment> selectCommentByContentId(String contentid) {
		List<KnowledgeComment> list = knowledgeCommentMapper.selectCommentByContentId(contentid);
		for(KnowledgeComment comment : list){
			getReply(comment);
		}
		return list;
	}
	
	private KnowledgeComment getReply(KnowledgeComment comment){
		//递归获得该评论的所有replyList
		List<KnowledgeComment> list = selectCommentByReplyId(comment.getId());
		for(KnowledgeComment km : list){
			getReply(km);
		}
		comment.setReplyList(list);
		
		return comment;
	}
	
	@Override
	public long selectCommentCountByContentId(String contentid, String status) {
		return knowledgeCommentMapper.selectCommentCountByContentId(contentid, status);
	}

	@Override
	public List<KnowledgeComment> selectCommentByReplyId(String replyid) {
		return knowledgeCommentMapper.selectCommentByReplyId(replyid);
	}

	@Override
	public int insertKnowledgeComment(KnowledgeComment knowledgeComment) {
		return knowledgeCommentMapper.insertKnowledgeComment(knowledgeComment);
	}
}
