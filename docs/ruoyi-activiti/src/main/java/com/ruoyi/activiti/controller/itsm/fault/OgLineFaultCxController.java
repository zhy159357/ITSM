package com.ruoyi.activiti.controller.itsm.fault;

import com.ruoyi.activiti.domain.SysLine;
import com.ruoyi.activiti.service.IOgLineFaultService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 【查询硬件条线故障】Controller
 * 
 * @author ruoyi
 * @date 2021-07-20
 */
@Controller
@RequestMapping("fault/cx")
public class OgLineFaultCxController extends BaseController
{
    private String prefix = "fault/cx";

    @Autowired
    private IOgLineFaultService ogLineFaultService;

    @GetMapping()
    public String fault()
    {
        return prefix + "/fault";
    }

    /**
     * 查询【硬件】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysLine sysLine)
    {
        startPage();
        if(StringUtils.isNotEmpty(sysLine.getCreateTimeTwo())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(sysLine.getCreateTimeTwo());
            sysLine.setCreateTimeTwo(parseStart);
        }
        List<SysLine> list = ogLineFaultService.selectOgLineFaultList(sysLine);
        return getDataTable(list);
    }

    /**
     * 详情【硬件】列表
     */
    @GetMapping("/detail/{hardwareId}")
    public String detail(@PathVariable("hardwareId") String hardwareId, ModelMap mmap)
    {
        SysLine sysLine = ogLineFaultService.selectOgLineFaultById(hardwareId);
        mmap.put("sysLine",sysLine);
        return prefix + "/detail";
    }

    /**
     * 导出【硬件】列表
     */
    @Log(title = "【导出】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysLine sysLine)
    {
        List<SysLine> list = ogLineFaultService.selectOgLineFaultList(sysLine);
        ExcelUtil<SysLine> util = new ExcelUtil<SysLine>(SysLine.class);
        return util.exportExcel(list, "fault");
    }

}
