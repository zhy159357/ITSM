package com.ruoyi.activiti.controller.itsm.fmbiz;

import com.ruoyi.activiti.service.IFmBizLifeService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBizLife;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件单效率Controller
 * 
 * @author ruoyi
 * @date 2021-01-21
 */
@Controller
@RequestMapping("/system/life")
public class FmBizLifeController extends BaseController
{
    private String prefix = "system/life";
    private String prefix_public = "fmbiz";

    @Autowired
    private IFmBizLifeService fmBizLifeService;

    @GetMapping()
    public String life()
    {
        return prefix + "/life";
    }

    /**
     *
     *事件单效率页面
     * @return
     */
    @GetMapping("/lifeList")
    public String lifeList() {
        return prefix_public + "/appr/fmBizLife";
    }
    /**
     * 查询事件单效率
     */
    @PostMapping("/getLifeList")
    @ResponseBody
    public TableDataInfo getLifeList(FmBizLife fmBizLife)
    {
        List<FmBizLife> list = fmBizLifeService.selectFmBizLifeList(fmBizLife);
        return getDataTable(list);
    }

    /**
     * 导出事件单效率
     */
    @Log(title = "事件单效率", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmBizLife fmBizLife)
    {
            fmBizLife = new FmBizLife();

        List<FmBizLife> list = fmBizLifeService.selectFmBizLifeList(fmBizLife);
        ExcelUtil<FmBizLife> util = new ExcelUtil<FmBizLife>(FmBizLife.class);
        return util.exportExcel(list, "事件单效率");
    }

    /**
     * 新增事件单效率
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存事件单效率
     */
    @RequiresPermissions("system:life:add")
    @Log(title = "事件单效率", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmBizLife fmBizLife)
    {
        return toAjax(fmBizLifeService.insertFmBizLife(fmBizLife));
    }

    /**
     * 修改事件单效率
     */
    @GetMapping("/edit/{fmLifeId}")
    public String edit(@PathVariable("fmLifeId") String fmLifeId, ModelMap mmap)
    {
        FmBizLife fmBizLife = fmBizLifeService.selectFmBizLifeById(fmLifeId);
        mmap.put("fmBizLife", fmBizLife);
        return prefix + "/edit";
    }

    /**
     * 修改保存事件单效率
     */
    @RequiresPermissions("system:life:edit")
    @Log(title = "事件单效率", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmBizLife fmBizLife)
    {
        return toAjax(fmBizLifeService.updateFmBizLife(fmBizLife));
    }

    /**
     * 删除事件单效率
     */
    @RequiresPermissions("system:life:remove")
    @Log(title = "事件单效率", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(fmBizLifeService.deleteFmBizLifeByIds(ids));
    }
}
