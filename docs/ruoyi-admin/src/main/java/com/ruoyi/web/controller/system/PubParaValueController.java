package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IPubParaValueService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system/dict/data")
public class PubParaValueController  extends BaseController {
    private String prefix = "system/dict/data";

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

    @PostMapping("/listPubParaValue")
    @ResponseBody
    @ApiOperation("根据paraId获取配置的中文值列表")
    @ApiImplicitParam(name = "paraId",required = true)
    public AjaxResult listPubParaValue(@RequestBody Map<String, String> requestMap)
    {
        List<PubParaValue> pubParaValueList = pubParaValueService.selectPubParaValueById(requestMap.get("paraId"));
        if (CollectionUtils.isEmpty(pubParaValueList)) {
            throw new BusinessException("数据配置为空!");
        }
        List<Map<String, Object>> pubMapList = pubParaValueList.stream().map(pubParaValue -> {
            Map<String, Object> map = new HashMap<>();
            map.put("value", pubParaValue.getValue());
            map.put("label", pubParaValue.getValueDetail());
            return map;
        }).collect(Collectors.toList());
        return AjaxResult.success(pubMapList);
    }

    @PostMapping("/selectPubParaByName")
    @ResponseBody
    public AjaxResult selectPubParaByName(@RequestBody Map<String, Object> params)
    {
        String paraName = (String) params.get("paraName");
        if("eventReason".equals(params.get("type"))) {
            if("9".equals(paraName)) {
                // 事件来源-监控需要查的字典项
                paraName = "event_reason_category_monitor";
            } else {
                // 事件来源-其他情况需要查的字典项
                paraName = "event_reason_category_product";
            }
        }
        List<PubParaValue> pubParaValues = pubParaValueService.selectPubParaValueByParaName(paraName);
        if(CollectionUtils.isEmpty(pubParaValues)) {
            return AjaxResult.error("根据字典key["+paraName+"]未找到字典项！");
        }
        List<Map<String, String>> resultList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(pubParaValues)) {
            pubParaValues.forEach(value -> {
                Map<String, String> map = new HashMap<>();
                map.put("value", value.getValue());
                map.put("label", value.getValueDetail());
                resultList.add(map);
            });
        }
        return AjaxResult.success(resultList);
    }

    /**
     * 故障知识点测试环境给个数据
     */
    @PostMapping("/selectBreakDownKnowledge")
    @ResponseBody
    public AjaxResult selectBreakDownKnowledge(@RequestBody Map<String, Object> params) {
        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("value", "1");
        map.put("label", "内存泄露");
        Map<String, String> map1 = new HashMap<>();
        map1.put("value", "2");
        map1.put("label", "CPU爆满");
        resultList.add(map);
        resultList.add(map1);
        return AjaxResult.success(resultList);
    }
}
