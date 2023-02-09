package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysBind;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.shiro.util.AuthorizationUtils;
import com.ruoyi.system.service.ISysBindService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统信息
 *
 * @author Mr.Xy
 */
@Controller
@RequestMapping("/system/bind")
public class SysBindController extends BaseController {
    private String prefix = "system/bind";

    @Autowired
    private ISysBindService bindService;

    @GetMapping()
    public String bind() {
        return prefix + "/bind";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysBind bind) {
        startPage();
        List<SysBind> list = bindService.selectBindList(bind);
        return getDataTable(list);
    }

    @Log(title = "系统管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:bind:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysBind bind) {
        List<SysBind> list = bindService.selectBindList(bind);
        ExcelUtil<SysBind> util = new ExcelUtil<SysBind>(SysBind.class);
        return util.exportExcel(list, "系统数据");
    }

    /**
     * 新增话机绑定
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存系统
     */
    @RequiresPermissions("system:bind:add")
    @Log(title = "系统管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysBind bind) {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(bindService.checkBindNameUnique(bind))) {
            return error("新增系统'" + bind + "'失败，系统名称已存在");
        }
        bind.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(bindService.insertBind(bind));

    }

    /**
     * 修改话机绑定
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("bind", bindService.selectById(id));
        return prefix + "/edit";
    }

    /**
     * 修改话机绑定
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("bind", bindService.selectById(id));
        return prefix + "/detail";
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:bind:edit")
    @Log(title = "系统管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysBind bind) {
        bindService.checkBindNameUnique(bind);
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(bindService.checkBindNameUnique(bind))) {
            return error("修改系统'" + "'失败，系统名称已存在");
        }
        bind.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(bindService.updateBind(bind));
    }


    @RequiresPermissions("system:bind:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(bindService.deleteBindByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkBindNameUnique")
    @ResponseBody
    public String checkBindNameUnique(SysBind bind) {
        return bindService.checkBindNameUnique(bind);
    }

    /**
     * 系统状态修改
     */
    @Log(title = "系统管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:bind:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysBind bind) {
        bindService.checkBindAllowed(bind);
        return toAjax(bindService.changeStatus(bind));
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree() {
        return prefix + "/tree";
    }

}