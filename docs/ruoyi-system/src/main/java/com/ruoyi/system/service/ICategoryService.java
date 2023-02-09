package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysCategory;
import com.ruoyi.common.core.domain.entity.SysDept;

import java.util.List;

public interface ICategoryService {

    List<Ztree> selectDeptTree(SysCategory sysCategory);

    List<SysCategory> selectCategoryList(SysCategory sysCategory);

    SysCategory selectCategoryById(Long categoryId);

    int insertCategory(SysCategory category);

    String selectAncestors(Long parentId);

    int updateCategoryByTier(SysCategory sys_category);

    int deleteCategoryByIds(String ids) throws Exception;

    int updateCategory(SysCategory sysCategory);
}
