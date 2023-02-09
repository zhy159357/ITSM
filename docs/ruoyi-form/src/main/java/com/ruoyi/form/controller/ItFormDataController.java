package com.ruoyi.form.controller;

import java.util.List;

import com.ruoyi.form.domain.ItFormData;
import com.ruoyi.form.service.IItFormDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 单模版Controller
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
@Controller
@RequestMapping("/form/data")
public class ItFormDataController extends BaseController
{
    private String prefix = "form/data";

    @Autowired
    private IItFormDataService itFormDataService;

    @RequiresPermissions("system:data:view")
    @GetMapping()
    public String data()
    {
        return prefix + "/data";
    }

    /**
     * 查询单模版列表
     */
    @RequiresPermissions("system:data:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ItFormData itFormData)
    {
        startPage();
        List<ItFormData> list = itFormDataService.selectItFormDataList(itFormData);
        return getDataTable(list);
    }

    /**
     * 导出单模版列表
     */
    @RequiresPermissions("system:data:export")
    @Log(title = "单模版", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ItFormData itFormData)
    {
        List<ItFormData> list = itFormDataService.selectItFormDataList(itFormData);
        ExcelUtil<ItFormData> util = new ExcelUtil<ItFormData>(ItFormData.class);
        return util.exportExcel(list, "data");
    }

    /**
     * 新增单模版
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存单模版
     */
    @RequiresPermissions("system:data:add")
    @Log(title = "单模版", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ItFormData itFormData)
    {
        return toAjax(itFormDataService.insertItFormData(itFormData));
    }

    /**
     * 修改单模版
     */
    @GetMapping("/edit/{formId}")
    public String edit(@PathVariable("formId") String formId, ModelMap mmap)
    {
        ItFormData itFormData = itFormDataService.selectItFormDataById(formId);
        mmap.put("itFormData", itFormData);
        return prefix + "/edit";
    }

    /**
     * 修改保存单模版
     */
    @RequiresPermissions("system:data:edit")
    @Log(title = "单模版", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ItFormData itFormData)
    {
        return toAjax(itFormDataService.updateItFormData(itFormData));
    }

    /**
     * 删除单模版
     */
    @RequiresPermissions("system:data:remove")
    @Log(title = "单模版", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(itFormDataService.deleteItFormDataByIds(ids));
    }
}
