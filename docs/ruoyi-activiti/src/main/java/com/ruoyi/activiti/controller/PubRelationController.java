package com.ruoyi.activiti.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 关联关系Controller
 * 
 * @author zx
 * @date 2021-01-25
 */
@Controller
@RequestMapping("system/relation")
public class PubRelationController extends BaseController
{
    private String prefix = "system/relation";

    @Autowired
    private IPubRelationService pubRelationService;

    @GetMapping()
    public String relation()
    {
        return prefix + "/relation";
    }

    /**
     * 查询关联关系列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PubRelation pubRelation)
    {
        startPage();
        List<PubRelation> list = pubRelationService.selectPubRelationList(pubRelation);
        return getDataTable(list);
    }

    /**
     * 导出关联关系列表
     */
    @Log(title = "关联关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PubRelation pubRelation)
    {
        List<PubRelation> list = pubRelationService.selectPubRelationList(pubRelation);
        ExcelUtil<PubRelation> util = new ExcelUtil<PubRelation>(PubRelation.class);
        return util.exportExcel(list, "relation");
    }

    /**
     * 新增关联关系
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存关联关系
     */
    @Log(title = "关联关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(PubRelation pubRelation)
    {
        List<PubRelation> list=pubRelationService.selectPubRelationList(pubRelation);
        if(CollectionUtils.isEmpty(list)){
            pubRelation.setRelationId(IdUtils.simpleUUID());
            return toAjax(pubRelationService.insertPubRelation(pubRelation));
        }else {
            return toAjax(1);
        }
    }
    /**
     * 新增保存关联关系
     */
    @Log(title = "关联关系", businessType = BusinessType.INSERT)
    @PostMapping("/addAll")
    @ResponseBody
    @Transactional
    public AjaxResult addSave(@RequestParam(value ="data") String data)
    {
        logger.info("+============================================="+data);
        JSONArray jsonArray= JSON.parseArray(data);
        for(int i=0;i<jsonArray.size();i++){
            JSONObject js=JSONObject.parseObject(jsonArray.getJSONObject(i).toJSONString());
            PubRelation pubRelation=JSONObject.parseObject(js.toJSONString(), PubRelation.class);
            List<PubRelation> list=pubRelationService.selectPubRelationList(pubRelation);
            if(CollectionUtils.isEmpty(list)){
                pubRelation.setRelationId(IdUtils.simpleUUID());
                pubRelationService.insertPubRelation(pubRelation);
            }
        }
        return toAjax(1);
    }
    /**
     * 修改关联关系
     */
    @GetMapping("/edit/{relationId}")
    public String edit(@PathVariable("relationId") String relationId, ModelMap mmap)
    {
        PubRelation pubRelation = pubRelationService.selectPubRelationById(relationId);
        mmap.put("pubRelation", pubRelation);
        return prefix + "/edit";
    }

    /**
     * 修改保存关联关系
     */
    @Log(title = "关联关系", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(PubRelation pubRelation)
    {
        return toAjax(pubRelationService.updatePubRelation(pubRelation));
    }

    /**
     * 删除关联关系
     */
    @Log(title = "关联关系", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(pubRelationService.deletePubRelationByIds(ids));
    }
}
