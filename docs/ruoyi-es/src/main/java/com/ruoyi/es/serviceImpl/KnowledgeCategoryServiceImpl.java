package com.ruoyi.es.serviceImpl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.KnowledgeCategory;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.mapper.KnowledgeCategoryMapper;
import com.ruoyi.es.service.KnowledgeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类别 Service业务层处理
 * @author dayong_sun
 * @date 2021-03-12
 */
@Service("knowledgeCategory")
public class KnowledgeCategoryServiceImpl implements KnowledgeCategoryService {

    @Autowired
    private KnowledgeCategoryMapper categoryMapper;

    /**
     * 查询类别 列表
     * @param knowledgeCategory 类别
     * @return 类别 
     */
    @Override
    public List<KnowledgeCategory> selectKnowledgeCategoryList(KnowledgeCategory knowledgeCategory) {
        return categoryMapper.selectKnowledgeCategoryList(knowledgeCategory);
    }

    /**
     * 查询类别 
     * @param categoryId 类别ID
     * @return 类别 
     */
    @Override
    public KnowledgeCategory selectKnowledgeCategoryById(String categoryId) {
        return categoryMapper.selectKnowledgeCategoryById(categoryId);
    }

    /**
     * 新增类别 
     * @param knowledgeCategory 类别 
     * @return 结果
     */
    @Override
    public int insertKnowledgeCategory(KnowledgeCategory knowledgeCategory) {
        // 赋值主键id
        knowledgeCategory.setCategoryId(UUID.getUUIDStr());
        knowledgeCategory.setCreateBy(ShiroUtils.getOgUser().getpId());
        knowledgeCategory.setCreateDate(DateUtils.getDate());
        return categoryMapper.insertKnowledgeCategory(knowledgeCategory);
    }

    /**
     * 修改类别 
     * @param knowledgeCategory 类别
     * @return 结果
     */
    @Override
    public int updateKnowledgeCategory(KnowledgeCategory knowledgeCategory) {
        knowledgeCategory.setUpdateBy(ShiroUtils.getOgUser().getpId());
        knowledgeCategory.setUpdateDate(DateUtils.getDate());
        return categoryMapper.updateKnowledgeCategory(knowledgeCategory);
    }

    /**
     * 删除类别 对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeCategoryByIds(String ids) {
        return categoryMapper.deleteKnowledgeCategoryByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除类别 信息
     * @param categoryId 类别 ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeCategoryById(String categoryId) {
        return categoryMapper.deleteKnowledgeCategoryById(categoryId);
    }

    /**
     * 校验类别编码是否唯一
     * @param knowledgeCategory 角色信息
     * @return 结果
     */
    @Override
    public String checkDataUnique(KnowledgeCategory knowledgeCategory){
        String categoryName = StringUtils.isNull(knowledgeCategory.getCategoryName()) ? "" : knowledgeCategory.getCategoryName();
        KnowledgeCategory category = categoryMapper.checkDataUnique(knowledgeCategory.getCategoryName());
        if (StringUtils.isNotNull(category) && category.getCategoryName().equals(categoryName))
        {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    @Override
    public List<KnowledgeCategory> getCateList() {
        return categoryMapper.getCateList();
    }

}
