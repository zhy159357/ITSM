package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysBind;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysBindService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 系统信息
 *
 * @author Mr.Xy
 */
@Controller
@RequestMapping("/system/bindSearch")
public class SysBindSearchController extends BaseController {
    private String prefix = "system/bind";

    @Autowired
    private ISysBindService bindService;

    @RequiresPermissions("system:bind:view")
    @GetMapping()
    public String bind() {
        return prefix + "/bindSearch";
    }

    @RequiresPermissions("system:bind:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysBind bind) {
        startPage();
        List<SysBind> list = bindService.selectBindList(bind);
        return getDataTable(list);
    }
    /**
     * 查看详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("bind", bindService.selectById(id));
        return prefix + "/detail";
    }

    @Log(title = "系统管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:bind:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysBind bind)
    {
        List<SysBind> list = bindService.selectBindList(bind);
        ExcelUtil<SysBind> util = new ExcelUtil<SysBind>(SysBind.class);
        return util.exportExcel(list, "系统数据");
    }
    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree() {
        return prefix + "/tree";
    }

}