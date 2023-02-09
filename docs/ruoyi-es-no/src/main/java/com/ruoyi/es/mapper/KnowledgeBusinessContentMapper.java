package com.ruoyi.es.mapper;

import com.ruoyi.es.domain.KnowledgeBusinessContent;
import com.ruoyi.es.domain.KnowledgeBusinessExample;

import java.util.List;

public interface KnowledgeBusinessContentMapper {

    /**
     * @param knowledgeBusinessContent
     * @return
     */
    public List<KnowledgeBusinessContent> businessContentList(KnowledgeBusinessContent knowledgeBusinessContent);
    public List<KnowledgeBusinessContent> businessContentMySqlList(KnowledgeBusinessContent knowledgeBusinessContent);

    /**
     * @param knowledgeBusinessContent
     * @return
     */
    public List<KnowledgeBusinessContent> businessSearchtList(KnowledgeBusinessContent knowledgeBusinessContent);
    public List<KnowledgeBusinessContent> businessSearchtMySqlList(KnowledgeBusinessContent knowledgeBusinessContent);

    public List<KnowledgeBusinessContent> businessListAud(KnowledgeBusinessContent knowledgeBusinessContent);
    public List<KnowledgeBusinessContent> businessListAudMySql(KnowledgeBusinessContent knowledgeBusinessContent);

    public int offlineBusinessByIds(String[] ids);

    public List<KnowledgeBusinessContent> selectBusiness(KnowledgeBusinessContent knowledgeBusinessContent);

    public int deleteBusinessData();

    public int insertBusinessData(List<KnowledgeBusinessExample> list);
}
