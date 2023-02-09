package com.ruoyi.es.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.es.domain.KnowledgeCollect;
import com.ruoyi.es.domain.KnowledgeVisits;

public interface KnowledgeVisitsMapper {

	int insertKnowledgeVisits(KnowledgeVisits knowledgeVisits);

	List<KnowledgeVisits> selectTopVisitsByUserId(@Param("userId")String userId, @Param("count")int count, @Param("knowledgeCollect")KnowledgeCollect knowledgeCollect);

}
