package com.ruoyi.es.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.es.domain.KnowledgeCollect;
import com.ruoyi.es.domain.KnowledgeVisits;
import com.ruoyi.es.mapper.KnowledgeVisitsMapper;
import com.ruoyi.es.service.KnowledgeVisitsService;
//@Service
public class KnowledgeVisitsServiceImpl implements KnowledgeVisitsService{
	@Autowired
	private KnowledgeVisitsMapper knowledgeVisitsMapper;
	
	@Override
	public int insertKnowledgeVisits(KnowledgeVisits knowledgeVisits) {
		return knowledgeVisitsMapper.insertKnowledgeVisits(knowledgeVisits);
	}

	@Override
	public List<KnowledgeVisits> selectTopVisitsByUserId(String userId, int count, KnowledgeCollect knowledgeCollect) {
		return knowledgeVisitsMapper.selectTopVisitsByUserId(userId, count, knowledgeCollect);
	}

}
