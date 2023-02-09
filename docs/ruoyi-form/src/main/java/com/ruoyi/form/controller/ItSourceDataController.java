package com.ruoyi.form.controller;

import java.util.List;

import com.ruoyi.form.domain.ItSourceData;
import com.ruoyi.form.service.IItSourceDataService;
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
 * 元数据Controller
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
@Controller
@RequestMapping("/source/data")
public class ItSourceDataController extends BaseController
{
    private String prefix = "source/data";

    @Autowired
    private IItSourceDataService itSourceDataService;

    @GetMapping()
    public String data()
    {
        return prefix + "/data";
    }

    /**
     * 查询元数据列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ItSourceData itSourceData)
    {
        startPage();
        List<ItSourceData> list = itSourceDataService.selectItSourceDataList(itSourceData);
        return getDataTable(list);
    }

    /**
     * 导出元数据列表
     */
    @Log(title = "元数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ItSourceData itSourceData)
    {
        List<ItSourceData> list = itSourceDataService.selectItSourceDataList(itSourceData);
        ExcelUtil<ItSourceData> util = new ExcelUtil<ItSourceData>(ItSourceData.class);
        return util.exportExcel(list, "data");
    }

    /**
     * 新增元数据
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存元数据
     */
    @Log(title = "元数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ItSourceData itSourceData)
    {
        return toAjax(itSourceDataService.insertItSourceData(itSourceData));
    }

    /**
     * 修改元数据
     */
    @GetMapping("/edit/{sourceDataId}")
    public String edit(@PathVariable("sourceDataId") String sourceDataId, ModelMap map)
    {
        ItSourceData itSourceData = itSourceDataService.selectItSourceDataById(sourceDataId);
        map.put("itSourceData", itSourceData);
        return prefix + "/edit";
    }

    /**
     * 修改保存元数据
     */
    @Log(title = "元数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ItSourceData itSourceData)
    {
        return toAjax(itSourceDataService.updateItSourceData(itSourceData));
    }

    /**
     * 删除元数据
     */
    @Log(title = "元数据", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(itSourceDataService.deleteItSourceDataByIds(ids));
    }
}
