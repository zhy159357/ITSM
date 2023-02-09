package com.ruoyi.es.mapper;

import com.ruoyi.es.domain.KnowledgeCollect;
import com.ruoyi.es.domain.KnowledgeContent;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 类别类表Mapper接口
 * @author dayong_sun
 * @date 2021-04-09
 */
public interface KnowledgeCollectionMapper
{

    /**
     * 查询列表
     * @param knowledgeCollect 知识表
     * @return 知识表集合
     */
    public List<KnowledgeCollect> selectKnowledgeCollectionList(KnowledgeCollect knowledgeCollect);
    public List<KnowledgeCollect> selectKnowledgeCollectionMysqlList(KnowledgeCollect knowledgeCollect);

    /**
     * 批量删除
     * @param collectionIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteKnowledgeCollectionByIds(String[] collectionIds);

	public int insertKnowledgeCollection(KnowledgeCollect knowledgeCollect);

	public int isCollected(@Param("createId")String createId, @Param("contentId")String contentId);


}
