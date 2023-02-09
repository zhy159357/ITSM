package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysCategory;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数管理类别设置
 */
@Controller
@RequestMapping("/system/category")
public class SysCategoryController extends BaseController {

    private String prefix = "system/category";

    @Autowired
    private ICategoryService categoryService;
    /**
     * 类别信息
     * @return
     */
    @RequestMapping()
    public String list(){
        return prefix+"/list";
    }


    @GetMapping("/add")
    public String add(){
        return prefix+"/add";
    }

    /**
     * 类别信息保存
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysCategory category){
        return toAjax(categoryService.insertCategory(category));
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo show(SysCategory sysCategory){
        startPage();
        List<SysCategory>  list= categoryService.selectCategoryList(sysCategory);
        for(SysCategory cate : list){
            cate.setAncestors(cate.getAncestors().replaceAll(",","/"));
        }
        return getDataTable(list);

    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = categoryService.selectDeptTree(new SysCategory());
        return ztrees;
    }


    @GetMapping(value = { "/selectCategoryTree/{cateogryId}", "/selectCategoryTree/{cateogryId}/{excludeId}" })
    public String selectCategoryTree(@PathVariable("cateogryId") Long categoryId,
                                 @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap)
    {
        mmap.put("category", categoryService.selectCategoryById(categoryId));
        mmap.put("excludeId", excludeId);
        return prefix + "/tree";
    }


    /**
     * 删除类别
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(categoryService.deleteCategoryByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 编辑页面回显
     * @param categoryId
     * @param modelMap
     * @return
     */
    @GetMapping("/edit/{categoryId}")
    public String edit(@PathVariable Long categoryId,ModelMap modelMap){
        SysCategory sysCategory = categoryService.selectCategoryById(categoryId);
        modelMap.put("category",sysCategory);
        return prefix+"/edit";
    }


    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysCategory sysCategory){
        return toAjax(categoryService.updateCategory(sysCategory));
    }
}
