package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysCategory;

import java.util.List;

public interface CategoryMapper {

    public List<SysCategory> selectCategoryList(SysCategory sysCategory) ;

    SysCategory selectCategoryById(Long categoryId);

    int insertCategory(SysCategory category);

    String selectAncestors(String parentId);

    int updateCategoryByTier(SysCategory sys_category);

    int deleteCategoryByIds(Long[] ids);

    int updateCategory(SysCategory sysCategory);
}
