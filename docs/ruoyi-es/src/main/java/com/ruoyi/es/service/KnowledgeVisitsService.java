package com.ruoyi.es.service;

import java.util.List;

import com.ruoyi.es.domain.KnowledgeCollect;
import com.ruoyi.es.domain.KnowledgeVisits;

public interface KnowledgeVisitsService {
	int insertKnowledgeVisits(KnowledgeVisits knowledgeVisits);

	List<KnowledgeVisits> selectTopVisitsByUserId(String userId, int count, KnowledgeCollect knowledgeCollect);
}
