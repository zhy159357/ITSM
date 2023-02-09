package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysCategory;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.CategoryMapper;
import com.ruoyi.system.service.ICategoryService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl  implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Ztree> selectDeptTree(SysCategory sysCategory) {

        List<SysCategory> categoryList = categoryMapper.selectCategoryList(sysCategory);
        List<Ztree> ztrees = initZtree(categoryList);
        return ztrees;
    }

    @Override
    public List<SysCategory> selectCategoryList(SysCategory sysCategory) {
        return  categoryMapper.selectCategoryList(sysCategory);
    }

    @Override
    public SysCategory selectCategoryById(Long categoryId) {
        return categoryMapper.selectCategoryById(categoryId);
    }


    @Override
    public int insertCategory(SysCategory category) {
        category.setCreateBy(ShiroUtils.getLoginName());
        SysCategory info = categoryMapper.selectCategoryById(category.getCategoryParentId());
        String ancestors =  info.getAncestors() + "," + category.getCategoryParentId();
        category.setAncestors(ancestors);
        return  categoryMapper.insertCategory(category);

    }

    @Override
    public String selectAncestors(Long parentId) {
        return categoryMapper.selectAncestors(parentId.toString());
    }

    @Override
    public int updateCategoryByTier(SysCategory sys_category) {
        return categoryMapper.updateCategoryByTier(sys_category);
    }

    @Override
    public int deleteCategoryByIds(String ids) throws Exception {
        Long[] userIds = Convert.toLongArray(ids);

        return categoryMapper.deleteCategoryByIds(userIds);
    }

    @Override
    public int updateCategory(SysCategory sysCategory) {
        sysCategory.setUpdateBy(ShiroUtils.getLoginName());
        return categoryMapper.updateCategory(sysCategory);
    }


    public List<Ztree> initZtree(List<SysCategory> categoryList)
    {
        return initZtree(categoryList, null);
    }


    public List<Ztree> initZtree(List<SysCategory> categoryList,List categoryRole)
    {

        List<Ztree> ztrees = new ArrayList<Ztree>();

        for (SysCategory category : categoryList)
        {
            if ("0".equals(category.getDelFlag()))
            {
                Ztree ztree = new Ztree();
                //ztree.setId(category.getCategoryId());
                //ztree.setpId(category.getCategoryParentId());
                ztree.setName(category.getCategoryName());
                ztree.setTitle(category.getCategoryName());
                ztree.setChecked(true);

                ztrees.add(ztree);
            }
        }
        return ztrees;
    }
}
