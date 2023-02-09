package com.ruoyi.es.service;

import com.ruoyi.es.domain.KnowledgeCollect;
import com.ruoyi.es.domain.KnowledgeContent;

import java.util.List;

/**
 * 类别 Service接口
 * @author dayong_sun
 * @date 2021-04-09
 */
public interface KnowledgeCollectionService
{

    /**
     * 查询收藏列表
     * @param knowledgeCollect 知识收藏
     * @return 知识集合
     */
    public List<KnowledgeCollect> selectKnowledgeCollectionList(KnowledgeCollect knowledgeCollect);

    /**
     * 批量删除
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteKnowledgeCollectionByIds(String ids);

    /**
     * 收藏(添加)
     * @param knowledgeCollect
     * @return
     */
	public int insertKnowledgeCollection(KnowledgeCollect knowledgeCollect);
	
	/**
	 * 该contentId的知识是否被userId的用户收藏了
	 * @param userId
	 * @param contentId
	 * @return
	 */
	public boolean isCollected(String userId, String contentId);
}
