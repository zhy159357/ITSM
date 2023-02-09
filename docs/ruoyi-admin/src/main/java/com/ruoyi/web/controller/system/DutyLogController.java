package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IOgTypeinfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数列别Controller
 *
 * @author ruoyi
 * @date 2020-12-06
 */
@Controller
@RequestMapping("/system/dutyLog")
public class DutyLogController extends BaseController
{
    private String prefix = "system/log";

    @RequiresPermissions("system:typeinfo:view")
    @GetMapping()
    public String typeinfo()
    {
        return prefix + "/logList";
    }

}
