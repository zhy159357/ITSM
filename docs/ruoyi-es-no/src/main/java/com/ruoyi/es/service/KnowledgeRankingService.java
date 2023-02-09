package com.ruoyi.es.service;

import com.ruoyi.es.domain.KnowledgeCategory;
import com.ruoyi.es.domain.KnowledgeRanking;

import java.util.List;

/**
 * 类别 Service接口
 * @author dayong_sun
 * @date 2021-03-12
 */
public interface KnowledgeRankingService
{

    /**
     * 查询排名列表
     * @param knowledgeRanking 知识排名
     * @return 集合
     */
    public List<KnowledgeRanking> selectKnowledgeRankingList(KnowledgeRanking knowledgeRanking);


}
