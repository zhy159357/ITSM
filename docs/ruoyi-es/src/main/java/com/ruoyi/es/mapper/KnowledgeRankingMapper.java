package com.ruoyi.es.mapper;

import com.ruoyi.es.domain.KnowledgeRanking;

import java.util.List;

/**
 * 类别类表Mapper接口
 * @author dayong_sun
 * @date 2021-04-12
 */
public interface KnowledgeRankingMapper
{

    /**
     * 查询排名列表
     * @param knowledgeRanking 知识排名
     * @return 集合
     */
    public List<KnowledgeRanking> selectKnowledgeRankingList(KnowledgeRanking knowledgeRanking);

    /**
     * 查询排名列表
     * @param knowledgeRanking 知识排名
     * @return 集合
     */
    public List<KnowledgeRanking> selectContentByCreateId(KnowledgeRanking knowledgeRanking);

    /**
     * 查询好评
     * @param knowledgeRanking 知识排名
     * @return 集合
     */
    public int selectRankingById(KnowledgeRanking knowledgeRanking);

    /**
     * 查询被访问总量
     * @param knowledgeRanking 知识排名
     * @return 集合
     */
    public int selectVisitsByCreateId(KnowledgeRanking knowledgeRanking);

}
