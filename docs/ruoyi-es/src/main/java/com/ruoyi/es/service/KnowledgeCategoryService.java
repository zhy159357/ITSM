package com.ruoyi.es.service;

import com.ruoyi.es.domain.KnowledgeCategory;
import com.ruoyi.es.domain.KnowledgeTitle;

import java.util.List;

public interface KnowledgeCategoryService
{

    /**
     * 查询类别 列表
     * @param knowledgeCategory 类别
     * @return 类别 集合
     */
    public List<KnowledgeCategory> selectKnowledgeCategoryList(KnowledgeCategory knowledgeCategory);

    /**
     * 查询类别 
     *
     * @param categoryId 类别 ID
     * @return 类别 
     */
    public KnowledgeCategory selectKnowledgeCategoryById(String categoryId);

    /**
     * 新增类别 
     * @param  knowledgeCategory 类别
     * @return 结果
     */
    public int insertKnowledgeCategory(KnowledgeCategory knowledgeCategory);

    /**
     * 修改类别 
     * @param  knowledgeCategory 类别
     * @return 结果
     */
    public int updateKnowledgeCategory(KnowledgeCategory knowledgeCategory);

    /**
     * 批量删除类别 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteKnowledgeCategoryByIds(String ids);

    /**
     * 删除类别 信息
     * @param categoryId 类别 ID
     * @return 结果
     */
    public int deleteKnowledgeCategoryById(String categoryId);

    /**
     * 校验类别编码是否唯一
     * @param  knowledgeCategory 角色信息
     * @return 结果
     */
    public String checkDataUnique(KnowledgeCategory knowledgeCategory);

    public List<KnowledgeCategory> getCateList();
}
