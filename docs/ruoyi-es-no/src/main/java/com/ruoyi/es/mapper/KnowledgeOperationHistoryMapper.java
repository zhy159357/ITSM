package com.ruoyi.es.mapper;

import java.util.List;

import com.ruoyi.es.domain.KnowledgeOperationHistory;

public interface KnowledgeOperationHistoryMapper {

    public int insertKnowledgeOperationHistory(KnowledgeOperationHistory knowledgeOperationHistory);

	public List<KnowledgeOperationHistory> selectKnowledgeOperationHistoryList(KnowledgeOperationHistory knowledgeOperationHistory);
	public List<KnowledgeOperationHistory> selectKnowledgeOperationHistoryMySqlList(KnowledgeOperationHistory knowledgeOperationHistory);

}
