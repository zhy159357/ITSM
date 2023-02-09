package com.ruoyi.es.service;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.es.domain.KnowledgeTitle;

import java.util.List;

public interface KnowledgeTitleService {

	List<KnowledgeTitle> selectKnowledgeTitleList(KnowledgeTitle knowledgeTitle);

	int insertKnowledgeTitle(KnowledgeTitle knowledgeTitle);

	KnowledgeTitle selectKnowledgeTitleById(String id);

	int updateKnowledgeTitle(KnowledgeTitle knowledgeTitle);

	int deleteKnowledgeTitleById(String id);

	boolean isUsed(String id);

	int updateKnowledgeTitleSys(KnowledgeTitle knowledgeTitle);

	boolean checkSysUnique(KnowledgeTitle knowledgeTitle);

	List<KnowledgeTitle> selectKnowledgeTitleSysList(KnowledgeTitle knowledgeTitle);

	public List<Ztree> selectTitleTree(KnowledgeTitle knowledgeTitle);

	List<KnowledgeTitle> selectKnowledgeTitleList2(KnowledgeTitle knowledgeTitle);

	public FmBiz getGroupIdByKnowledgek(FmBiz fmBiz);


	List<KnowledgeTitle> selectKnowledgeTitleByName(
			KnowledgeTitle knowledgeTitle);


	void synchronizeMonitoringContent();
}
