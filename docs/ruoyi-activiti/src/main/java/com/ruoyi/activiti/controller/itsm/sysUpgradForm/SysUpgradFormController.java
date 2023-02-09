package com.ruoyi.activiti.controller.itsm.sysUpgradForm;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.SysUpgradForm;
import com.ruoyi.system.service.ISysUpgradFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * 系统升级申请单
 */
@Controller
@RequestMapping("/system/upgradForm")
public class SysUpgradFormController extends BaseController {

    private String prefix = "system/upgradForm";

    @Autowired
    private ISysUpgradFormService sysUpgradFormService;

    @GetMapping()
    public String upgradForm()
    {
        return prefix + "/upgradForm";
    }

    /**
     *
     * 查询列表
     * @param sysUpgradForm
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUpgradForm sysUpgradForm)
    {
        List<SysUpgradForm> list = sysUpgradFormService.selectSysUpgradFormList(sysUpgradForm);
        return getDataTable_ideal(list);
    }

    /**
     * 跳转新增页面
     */
    @GetMapping("/add")
    public String add(ModelMap modelMap) {
        modelMap.put("id", UUID.getUUIDStr());
        return prefix + "/add";
    }

    /**
     * 新增保存工具使用情况
     */
    @Log(title = "新增系统升级申请单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysUpgradForm sysUpgradForm) {
        SysUpgradForm sysBo =null;
        sysBo = sysUpgradFormService.selectSysUpgradFormByCode(sysUpgradForm.getSysNumber());
        if(sysBo!=null){
            return error("编号已存在");
        }
        sysUpgradForm.setCreatTime(String.valueOf((new Date()).getTime()));
        return toAjax(sysUpgradFormService.insertSysUpgradForm(sysUpgradForm));
    }

    /**
     * 修改查询
     * @param id
     * @return
     */
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        SysUpgradForm sysUpgradForm = sysUpgradFormService.selectSysUpgradFormById(id);
        ajaxResult.put("data",sysUpgradForm);
        return  ajaxResult;
    }
    /**
     *
     * 修改
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        mmap.put("data", sysUpgradFormService.selectSysUpgradFormById(id));
        return prefix + "/edit";
    }

    @Log(title = "修改系统升级申请单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysUpgradForm sysUpgradForm)
    {
        int size = 0;
        SysUpgradForm bo = sysUpgradFormService.selectSysUpgradFormById(sysUpgradForm.getId());
        //比较修改前后是否相同
        if (!sysUpgradForm.getSysNumber().equals(bo.getSysNumber())) {
            size = sysUpgradFormService.getByCount(sysUpgradForm.getSysNumber());
        }
        if (size > 0) {
            return error("编号已存在");
        } else {
            return toAjax(sysUpgradFormService.updateSysUpgradForm(sysUpgradForm));
        }
    }

    @Log(title = "删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        sysUpgradFormService.deleteSysUpgradFormByIds(ids);
        return AjaxResult.success("已删除");
    }
}
