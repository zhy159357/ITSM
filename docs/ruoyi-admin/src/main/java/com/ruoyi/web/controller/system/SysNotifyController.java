package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.ruoyi.system.domain.SysNotify;
import com.ruoyi.system.service.ISysNotifyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 系统通知Controller
 *
 * @author zx
 * @date 2021-08-26
 */
@Controller
@RequestMapping("/system/notify")
public class SysNotifyController extends BaseController
{
    private String prefix = "system/notify";

    @Autowired
    private ISysNotifyService sysNotifyService;
    /**
     * 查询系统通知列表 (监控)
     */
    @PostMapping("/list")
    @ResponseBody
    public AjaxResult list(String userId)
    {
        SysNotify sysNotify=new SysNotify();
        sysNotify.setUserId(userId);
        sysNotify.setType("FmYx");
        sysNotify.setIsPush("0");
        List<SysNotify> list = sysNotifyService.selectSysNotifyList(sysNotify);
        StringBuilder text=new StringBuilder();
        for(SysNotify notify:list){
           text.append(notify.getText()+",");
           notify.setIsPush("1");
            sysNotifyService.updateSysNotify(notify);
        }
        if(StringUtils.isNotEmpty(text)){
            text.deleteCharAt(text.length()-1);
            return success("新增监控事件单任务，单号为："+text.toString());
        }else{
            return success("");
        }

    }

    /**
     * 导出系统通知列表
     */
    @Log(title = "系统通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysNotify sysNotify)
    {
        List<SysNotify> list = sysNotifyService.selectSysNotifyList(sysNotify);
        ExcelUtil<SysNotify> util = new ExcelUtil<SysNotify>(SysNotify.class);
        return util.exportExcel(list, "notify");
    }

    /**
     * 新增系统通知
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存系统通知
     */
    @Log(title = "系统通知", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysNotify sysNotify)
    {
        return toAjax(sysNotifyService.insertSysNotify(sysNotify));
    }

    /**
     * 修改系统通知
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        SysNotify sysNotify = sysNotifyService.selectSysNotifyById(id);
        mmap.put("sysNotify", sysNotify);
        return prefix + "/edit";
    }

    /**
     * 修改保存系统通知
     */
    @Log(title = "系统通知", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysNotify sysNotify)
    {
        return toAjax(sysNotifyService.updateSysNotify(sysNotify));
    }

    /**
     * 删除系统通知
     */
    @Log(title = "系统通知", businessType = BusinessType.DELETE)
    @PostMapping( "/removeAll")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        sysNotifyService.deleteSysNotifyByIds(ids);
        return success("清理成功");
    }
}
