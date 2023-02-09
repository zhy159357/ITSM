package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.shiro.util.AuthorizationUtils;
import com.ruoyi.system.service.ISysAppService;
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
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/app")
public class SysAppController extends BaseController {
    private String prefix = "system/app";

    @Autowired
    private ISysAppService appService;

    @RequiresPermissions("system:app:view")
    @GetMapping()
    public String app() {
        return prefix + "/app";
    }

    @RequiresPermissions("system:app:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysApp app) {
        startPage();
        List<SysApp> list = appService.selectAppList(app);
        return getDataTable(list);
    }

    @Log(title = "系统管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:app:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysApp app) {
        List<SysApp> list = appService.selectAppList(app);
        ExcelUtil<SysApp> util = new ExcelUtil<SysApp>(SysApp.class);
        return util.exportExcel(list, "系统数据");
    }

    /**
     * 新增系统
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存系统
     */
    @RequiresPermissions("system:app:add")
    @Log(title = "系统管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysApp app) {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(appService.checkAppNameUnique(app))) {
            return error("新增系统'" + app.getSysname() + "'失败，系统名称已存在");
        }
        app.setCreateBy(ShiroUtils.getLoginName());
        app.setId(UUID.getUUIDStr());
        return toAjax(appService.insertApp(app));

    }

    /**
     * 修改系统
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("app", appService.selectById(id));
        return prefix + "/edit";
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:app:edit")
    @Log(title = "系统管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysApp app) {
        appService.checkAppNameUnique(app);
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(appService.checkAppNameUnique(app))) {
            return error("修改系统'" + app.getSysname() + "'失败，系统名称已存在");
        }

        app.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(appService.updateApp(app));
    }


    @RequiresPermissions("system:app:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(appService.deleteAppByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkAppNameUnique")
    @ResponseBody
    public String checkAppNameUnique(SysApp app) {
        return appService.checkAppNameUnique(app);
    }

    /**
     * 系统状态修改
     */
    @Log(title = "系统管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:app:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysApp app) {
        appService.checkAppAllowed(app);
        return toAjax(appService.changeStatus(app));
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree() {
        return prefix + "/tree";
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/authrole/{id}")
    public String authRole(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("app", appService.selectById(id));
        return prefix + "/authrole";
    }

    @RequiresPermissions("system:app:llist")
    @PostMapping("/llist/{sysid}")
    @ResponseBody
    public TableDataInfo llist(@PathVariable("sysid") String sysid,SysApp app) {
        startPage();
        app.setAppid(sysid);
        List<OgRole> list = appService.selectAppRoleLList(app);
        return getDataTable(list);
    }

    @RequiresPermissions("system:app:rlist")
    @PostMapping("/rlist/{sysid}")
    @ResponseBody
    public TableDataInfo rlist(@PathVariable("sysid") String sysid,SysApp app) {
        startPage();
        app.setAppid(sysid);
        List<OgRole> list = appService.selectAppRoleRList(app);
        return getDataTable(list);
    }

    @RequiresPermissions("system:app:ladd")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/ladd")
    @ResponseBody
    public AjaxResult ladd(String appid,String ids) {
        return toAjax(appService.insertSysRole(appid,ids));
    }

    @RequiresPermissions("system:app:ladd")
    @Log(title = "系统对应角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/rremove")
    @ResponseBody
    public AjaxResult rremove( String appid,String ids) {
        return toAjax(appService.deleteAppRoleByIds(appid,ids));
    }
    @GetMapping("/useapp")
    public String useapp() {
        return prefix + "/useapporder";
    }

    @PostMapping("/changeSysOrder")
    @ResponseBody
    public AjaxResult changeSysOrder( String id,String value) {
        String userId= ShiroUtils.getUserId();
        return toAjax(appService.changeSysOrder(userId,id,value));
    }

    @PostMapping("/orderlist")
    @ResponseBody
    public TableDataInfo orderlist(SysApp app) {
        startPage();
        String userId= ShiroUtils.getUserId();
        app.setUserid(userId);
        List<SysApp> list = appService.selectAppOrderList(app);
        return getDataTable(list);
    }

}