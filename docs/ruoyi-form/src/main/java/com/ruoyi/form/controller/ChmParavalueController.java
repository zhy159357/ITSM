package com.ruoyi.form.controller;

import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.SysDeptVo;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.form.domain.ChmParavalue;
import com.ruoyi.form.service.IChmParavalueService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 基础数据Controller
 * 
 * @author ruoyi
 * @date 2022-10-24
 */
@Controller
@RequestMapping("system/chm/paravalue")
public class ChmParavalueController extends BaseController
{
    private String prefix = "form/paravalue";

    @Autowired
    private IChmParavalueService chmParavalueService;

    @GetMapping()
    public String paravalue()
    {
        return prefix + "/paravalue";
    }

    /**
     * 查询基础数据列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ChmParavalue chmParavalue)
    {
        startPage();
        List<ChmParavalue> list = chmParavalueService.selectChmParavalueList(chmParavalue);
        return getDataTable(list);
    }
    /**
     * 查询基础数据列表
     */
    @PostMapping("/listJson")
    @ResponseBody
    @ApiOperation(value = "硬件报障基础数据")
    public AjaxResult listJson(@RequestBody JSONObject jsonObject)
    {
        ChmParavalue chmParavalue=new ChmParavalue();
        if(StringUtils.isEmpty(jsonObject.get("parentId"))){
            return AjaxResult.success();
        }
        chmParavalue.setParentId(Long.valueOf(jsonObject.get("parentId").toString()));
        List<ChmParavalue> list = chmParavalueService.selectChmParavalueList(chmParavalue);
        List<SysDeptVo> sysDeptVoList = new LinkedList();

        if(!CollectionUtils.isEmpty(list)) {
            list.forEach(para -> {
                SysDeptVo sysDeptVo = new SysDeptVo();
                sysDeptVo.setValue(para.getId().toString());
                sysDeptVo.setLabel(para.getName());
                sysDeptVoList.add(sysDeptVo);
            });
        }
        return AjaxResult.success(sysDeptVoList);
    }
    /**
     * 查询基础数据列表
     */
    @PostMapping("/selectList")
    @ResponseBody
    @ApiOperation(value = "硬件报障基础数据")
    public List<ChmParavalue> selectList(@RequestBody JSONObject jsonObject)
    {
        List<ChmParavalue> list = chmParavalueService.selectChmParavalueListRy(jsonObject.getString("parentId"));
        return list;
    }
    /**
     * 查询基础数据列表所有三级数据
     */
    @PostMapping("/listJsonThree")
    @ResponseBody
    @ApiOperation(value = "硬件报障基础数据")
    public AjaxResult listJsonThree(@RequestBody JSONObject jsonObject)
    {
        ChmParavalue chmParavalue=new ChmParavalue();
        String parentId=jsonObject.getString("parentId");
        if(StringUtils.isEmpty(parentId)){
            chmParavalue.setLevels("3");
        }else {
            chmParavalue.setParentId(Long.valueOf(parentId));
        }
        List<ChmParavalue> list = chmParavalueService.selectChmParavalueList(chmParavalue);
        List<SysDeptVo> sysDeptVoList = new LinkedList();
        if(!CollectionUtils.isEmpty(list)) {
            list.forEach(para -> {
                SysDeptVo sysDeptVo = new SysDeptVo();
                sysDeptVo.setValue(para.getId().toString());
                sysDeptVo.setLabel(para.getName());
                sysDeptVoList.add(sysDeptVo);
            });
        }
        return AjaxResult.success(sysDeptVoList);
    }
    /**
     * 查询基础数据列表父级
     */
    @PostMapping("/listJsonParent")
    @ResponseBody
    @ApiOperation(value = "查询基础数据列表父级")
    public AjaxResult listJsonParent(@RequestBody JSONObject jsonObject)
    {
        ChmParavalue chmParavalue=new ChmParavalue();
        if(StringUtils.isEmpty(jsonObject.get("id"))){
            return AjaxResult.error("请选择三级菜单!");
        }
        chmParavalue=chmParavalueService.selectChmParavalueById(jsonObject.getLong("id"));
        ChmParavalue chmParavalue1=new ChmParavalue();
        chmParavalue1.setId(chmParavalue.getParentId());
        List<ChmParavalue> list = chmParavalueService.selectChmParavalueList(chmParavalue1);
        List<SysDeptVo> sysDeptVoList = new LinkedList();
        if(!CollectionUtils.isEmpty(list)) {
            list.forEach(para -> {
                SysDeptVo sysDeptVo = new SysDeptVo();
                sysDeptVo.setValue(para.getId().toString());
                sysDeptVo.setLabel(para.getName());
                sysDeptVoList.add(sysDeptVo);
            });
        }
        return AjaxResult.success(sysDeptVoList);
    }
    /**
     * 导出基础数据列表
     */
    @Log(title = "基础数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ChmParavalue chmParavalue)
    {
        List<ChmParavalue> list = chmParavalueService.selectChmParavalueList(chmParavalue);
        ExcelUtil<ChmParavalue> util = new ExcelUtil<ChmParavalue>(ChmParavalue.class);
        return util.exportExcel(list, "paravalue");
    }

    /**
     * 新增基础数据
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存基础数据
     */
    @Log(title = "基础数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ChmParavalue chmParavalue)
    {
        return toAjax(chmParavalueService.insertChmParavalue(chmParavalue));
    }

    /**
     * 修改基础数据
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ChmParavalue chmParavalue = chmParavalueService.selectChmParavalueById(id);
        mmap.put("chmParavalue", chmParavalue);
        return prefix + "/edit";
    }

    /**
     * 修改保存基础数据
     */
    @Log(title = "基础数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ChmParavalue chmParavalue)
    {
        return toAjax(chmParavalueService.updateChmParavalue(chmParavalue));
    }

    /**
     * 删除基础数据
     */
    @Log(title = "基础数据", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(chmParavalueService.deleteChmParavalueByIds(ids));
    }
}
