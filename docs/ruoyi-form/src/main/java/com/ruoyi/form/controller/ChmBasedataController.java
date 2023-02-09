package com.ruoyi.form.controller;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysDeptVo;
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
import com.ruoyi.form.domain.ChmBasedata;
import com.ruoyi.form.service.IChmBasedataService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * chmDataController
 * 
 * @author zhangxu
 * @date 2022-11-19
 */
@Controller
@RequestMapping("system/baseData")
public class ChmBasedataController extends BaseController
{
    private String prefix = "form/basedata";

    @Autowired
    private IChmBasedataService chmBasedataService;

    @GetMapping()
    public String basedata()
    {
        return prefix + "/basedata";
    }

    /**
     * 查询chmData列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ChmBasedata chmBasedata)
    {
        startPage();
        List<ChmBasedata> list = chmBasedataService.selectChmBasedataList(chmBasedata);
        return getDataTable(list);
    }
    /**
     * 查询chmData列表
     */
    @PostMapping("/listSelect")
    @ResponseBody
    public AjaxResult listSelect()
    {
        List<SysDeptVo> list = chmBasedataService.selectChmBasedataListSelect();
        return AjaxResult.success(list);
    }


    /**
     * 导出chmData列表
     */
    @Log(title = "chmData", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ChmBasedata chmBasedata)
    {
        List<ChmBasedata> list = chmBasedataService.selectChmBasedataList(chmBasedata);
        ExcelUtil<ChmBasedata> util = new ExcelUtil<ChmBasedata>(ChmBasedata.class);
        return util.exportExcel(list, "basedata");
    }

    /**
     * 新增chmData
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存chmData
     */
    @Log(title = "chmData", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ChmBasedata chmBasedata)
    {
        return toAjax(chmBasedataService.insertChmBasedata(chmBasedata));
    }

    /**
     * 修改chmData
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ChmBasedata chmBasedata = chmBasedataService.selectChmBasedataById(id);
        mmap.put("chmBasedata", chmBasedata);
        return prefix + "/edit";
    }

    /**
     * 修改保存chmData
     */
    @Log(title = "chmData", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ChmBasedata chmBasedata)
    {
        return toAjax(chmBasedataService.updateChmBasedata(chmBasedata));
    }

    /**
     * 删除chmData
     */
    @Log(title = "chmData", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(chmBasedataService.deleteChmBasedataByIds(ids));
    }
}
