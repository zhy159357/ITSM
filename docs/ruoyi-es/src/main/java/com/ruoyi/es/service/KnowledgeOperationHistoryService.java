package com.ruoyi.es.service;

import java.util.List;

import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeOperationHistory;

public interface KnowledgeOperationHistoryService {

	public List<KnowledgeOperationHistory> selectKnowledgeOperationHistoryList(
			KnowledgeOperationHistory knowledgeOperationHistory);
	
	public int insertOperationHistory(KnowledgeContent knowledgeContent);

	public int insertOperationHistory(String contentId, String status, String reason);
}
