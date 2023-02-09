package com.ruoyi.es.mapper;

import com.ruoyi.es.domain.KnowledgeTitle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KnowledgeTitleMapper {

	List<KnowledgeTitle> selectKnowledgeTitleList(KnowledgeTitle knowledgeTitle);

	List<KnowledgeTitle> selectKnowledgeTitleMySqlList(KnowledgeTitle knowledgeTitle);

	int insertKnowledgeTitle(KnowledgeTitle knowledgeTitle);

	KnowledgeTitle selectKnowledgeTitleById(String id);

	int updateKnowledgeTitle(KnowledgeTitle knowledgeTitle);

	int deleteKnowledgeTitleById(String id);

	int isUsed(String id);

	int updateKnowledgeTitleSys(KnowledgeTitle knowledgeTitle);

	int checkSysUnique(KnowledgeTitle knowledgeTitle);

	List<KnowledgeTitle> selectKnowledgeTitleSysList(@Param("knowledgeTitle")KnowledgeTitle knowledgeTitle, @Param("orgId")String orgId);

	List<KnowledgeTitle> selectKnowledgeTitleSysMySqlList(@Param("knowledgeTitle")KnowledgeTitle knowledgeTitle, @Param("orgId")String orgId);

	public List<KnowledgeTitle> selectTitleTree(KnowledgeTitle knowledgeTitle);

	List<KnowledgeTitle> selectKnowledgeTitleList2(KnowledgeTitle knowledgeTitle);

	List<KnowledgeTitle> selectKnowledgeTitleByName(
			KnowledgeTitle knowledgeTitle);

	int synchronizeMonitoringContent();
}
