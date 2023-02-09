package com.ruoyi.es.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.es.domain.KnowledgeCategory;
import com.ruoyi.es.service.KnowledgeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类别Controller
 * @author dayong_sun
 * @date 2021-03-12
 */
@Controller
@RequestMapping("knowledge/category")
public class CategoryController extends BaseController {
    private String prefix = "knowledge/category";

    @Autowired(required=false)
    private KnowledgeCategoryService categoryService;

    @GetMapping()
    public String category() {
        return prefix + "/category";
    }

    /**
     * 查询类别列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KnowledgeCategory knowledgeCategory) {
        startPage();
        List<KnowledgeCategory> list = categoryService.selectKnowledgeCategoryList(knowledgeCategory);
        return getDataTable(list);
    }

    /**
     * 新增类别
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存类别
     */
    @Log(title = "类别", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KnowledgeCategory knowledgeCategory) {
        return toAjax(categoryService.insertKnowledgeCategory(knowledgeCategory));
    }

    /**
     * 修改类别
     */
    @GetMapping("/edit/{categoryId}")
    public String edit(@PathVariable("categoryId") String categoryId, ModelMap mmap) {
        KnowledgeCategory category =  categoryService.selectKnowledgeCategoryById(categoryId);
        mmap.put("knowledgeCategory", category);
        return prefix + "/edit";
    }

    /**
     * 修改保存类别
     */
    @Log(title = "类别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KnowledgeCategory knowledgeCategory) {
        return toAjax( categoryService.updateKnowledgeCategory(knowledgeCategory));
    }

    /**
     * 删除类别
     */
    @Log(title = "类别", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax( categoryService.deleteKnowledgeCategoryByIds(ids));
    }

    /**
     * 校验数据
     */
    @PostMapping("/checkDataUnique")
    @ResponseBody
    public String checkDataUnique(KnowledgeCategory knowledgeCategory)
    {
        return  categoryService.checkDataUnique(knowledgeCategory);
    }
}
