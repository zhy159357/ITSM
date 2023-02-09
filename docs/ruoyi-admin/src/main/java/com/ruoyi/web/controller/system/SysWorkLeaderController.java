package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ISysWorkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 工作组管理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/workleader")
public class SysWorkLeaderController extends BaseController
{
    private String prefix = "system/work";
    private String prefixleader = "system/workleader";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgUserService ogUserService;

    /*@RequiresPermissions("system:workleader:view")*/
    @GetMapping()
    public String work()
    {
        return prefix + "/groupleader";
    }

    /*@RequiresPermissions("system:workleader:list")*/
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgGroup group)
    {
        startPage();
        group.setOrgId(resetOrgId(group.getOrgId(),group.getOrgName()));
        List<OgGroup> list = workService.selectOgGroupList(group);
        return getDataTable(list);
    }

    @Log(title = "工作组管理", businessType = BusinessType.EXPORT)
    /*@RequiresPermissions("system:workleader:export")*/
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user)
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "工作组数据");
    }
}