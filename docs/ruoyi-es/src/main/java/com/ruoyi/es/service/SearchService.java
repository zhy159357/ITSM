package com.ruoyi.es.service;

import com.ruoyi.es.domain.KnowledgeAlarmExample;
import com.ruoyi.es.domain.KnowledgeBusinessContent;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeSearch;

import java.util.List;
import java.util.Map;

/**
 * @author dayong_sun
 * @version 1.0
 */
public interface SearchService {

    List<KnowledgeSearch> selectSearchList(KnowledgeSearch knowledgeSearch);

    KnowledgeSearch selectSearchById(String id);

    List<KnowledgeSearch> selectContentList(KnowledgeSearch knowledgeSearch);

    int updateById(KnowledgeSearch knowledgeSearch);

    int save(KnowledgeSearch knowledgeSearch);

    int deleteById(String id);

    List<KnowledgeContent> search(Map<String, String> searchMap);

    List<KnowledgeContent> selectContentByIdsAndParams(List<String> ids, Map<String, String> params);
    
    KnowledgeContent selectContentById(String id);

	List<KnowledgeAlarmExample> alarmAnalizeList(KnowledgeAlarmExample knowledgeAlarmExample);

    List<KnowledgeAlarmExample> alarmExportList(KnowledgeAlarmExample knowledgeAlarmExample);

	List<KnowledgeBusinessContent> businessAnalizeList(KnowledgeBusinessContent businessContent);
	
	List<KnowledgeContent> searchMonitorApi(Map<String, String> searchMap);

    List<KnowledgeAlarmExample> selectAlarm(KnowledgeAlarmExample knowledgeAlarmExample);

	int selectAlarmCount();

}
