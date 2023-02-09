package com.ruoyi.es.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.es.domain.KnowledgeBusinessContent;
import com.ruoyi.es.domain.KnowledgeBusinessExample;
import com.ruoyi.es.domain.KnowledgeContent;

import java.util.List;

/**
 * @date 2021-05-27
 * dayong_sun
 */
public interface KnowledgeBusinessContentService
{
    public List<KnowledgeBusinessContent> businessContentList(KnowledgeBusinessContent knowledgeBusinessContent);

    public List<KnowledgeBusinessContent> businessSearchtList(KnowledgeBusinessContent knowledgeBusinessContent);

    public List<KnowledgeBusinessContent> businessListAud(KnowledgeBusinessContent knowledgeBusinessContent);

    public int auditBusiness(KnowledgeContent knowledgeContent);

    public int auditNoBusiness(KnowledgeContent knowledgeContent);

    public int applyBusiness(KnowledgeContent knowledgeContent);

    public int offlineBusinessByIds(String ids);

    public String getStatusByUserId(String userId);

    public void deleteSearchAndEs(String id);

    public void saveSearchAndEs(KnowledgeContent knowledgeContent);

    public int selectCreateIdById(String id);

    public int insertContent(KnowledgeContent knowledgeContent);

    public int updateCont(KnowledgeContent knowledgeContent);

    public int auditContent(KnowledgeContent knowledgeContent);

    public String importBusinessData(List<KnowledgeBusinessExample> alarmList);
    
    public AjaxResult esCheckAndSave(String id);

    public int backApply(KnowledgeContent knowledgeContent);

    public int trashApply(KnowledgeContent knowledgeContent);

    public int forceBack(KnowledgeContent knowledgeContent);

    public int forceTrash(KnowledgeContent knowledgeContent);

    int selectIssuefxByNo(KnowledgeContent knowledgeContent);

    String selectBusinessCreateIdById(String id);

    public KnowledgeContent selectIssuefxIdForKnowledgeId(String id);
}
