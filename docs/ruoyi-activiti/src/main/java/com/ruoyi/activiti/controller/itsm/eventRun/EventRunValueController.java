package com.ruoyi.activiti.controller.itsm.eventRun;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IPubParaValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("eventRun/Dict/Data")
public class EventRunValueController extends BaseController {
    private String prefix = "run/data";

    @Autowired
    private IPubParaValueService pubParaValueService;
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PubParaValue pubParaValue)
    {
        startPage();
        List<PubParaValue> list = pubParaValueService.selectParaValueList(pubParaValue);
        return getDataTable(list);
    }

    @GetMapping("/add/{paraId}")
    public String add(@PathVariable String paraId, ModelMap mmap)
    {
        mmap.put("paraId",paraId);
        return prefix + "/add";
    }


    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated PubParaValue pubParaValue)
    {
        pubParaValue.setParaValueId(UUID.getUUIDStr());
        return toAjax(pubParaValueService.insertParaValueData(pubParaValue));
    }


    @GetMapping("/edit/{paraValueId}")
    public String edit(@PathVariable("paraValueId") String paraValueId, ModelMap mmap)
    {
        mmap.put("pubParaValue", pubParaValueService.selectParaValueDataById(paraValueId));
        return prefix + "/edit";
    }

    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated PubParaValue pubParaValue)
    {
        return toAjax(pubParaValueService.updateParaValueData(pubParaValue));
    }

    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        PubParaValue pubParaValue = new PubParaValue();
        pubParaValue.setParaValueId(ids);
        return toAjax(pubParaValueService.deleteParaValueDataByIds(pubParaValue));
    }
}
