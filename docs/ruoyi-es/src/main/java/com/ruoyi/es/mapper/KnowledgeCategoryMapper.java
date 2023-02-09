package com.ruoyi.es.mapper;

import com.ruoyi.es.domain.KnowledgeCategory;
import com.ruoyi.es.domain.KnowledgeTitle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类别类表Mapper接口
 * @author dayong_sun
 * @date 2021-03-12
 */
public interface KnowledgeCategoryMapper
{

    /**
     * 查询类别列表列表
     * @param knowledgeCategory 类别类表
     * @return 类别类表集合
     */
    public List<KnowledgeCategory> selectKnowledgeCategoryList(KnowledgeCategory knowledgeCategory);

    /**
     * 查询类别列表
     * @param categoryId 类别类表ID
     * @return 类别类表
     */
    public KnowledgeCategory selectKnowledgeCategoryById(String categoryId);

    /**
     * 新增类别列表
     * 
     * @param knowledgeCategory 类别列表
     * @return 结果
     */
    public int insertKnowledgeCategory(KnowledgeCategory knowledgeCategory);

    /**
     * 修改类别列表
     * @param knowledgeCategory 类别列表
     * @return 结果
     */
    public int updateKnowledgeCategory(KnowledgeCategory knowledgeCategory);

    /**
     * 删除类别列表
     * @param categoryId 类别列表ID
     * @return 结果
     */
    public int deleteKnowledgeCategoryById(String categoryId);

    /**
     * 批量删除类别列表
     * @param categoryIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteKnowledgeCategoryByIds(String[] categoryIds);

    /**
     * 校验类别名称是否唯一
     * @param categoryName 类别信息
     * @return 结果
     */
    public KnowledgeCategory checkDataUnique(String categoryName);

    public List<KnowledgeCategory> getCateList();

	public KnowledgeTitle getTitleById(@Param("category") String category, @Param("id") String id);
}
