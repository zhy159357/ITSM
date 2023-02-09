package com.ruoyi.activiti.controller.itsm.gzfault;

import com.ruoyi.activiti.service.IGzFaultReportLogService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.GzFaultReportLog;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应急事件记录Controller
 * 
 * @author ruoyi
 * @date 2021-08-09
 */
@Controller
@RequestMapping("/gzFault/log")
public class GzFaultReportLogController extends BaseController
{
    private String prefix = "gzFault/report";

    @Autowired
    private IGzFaultReportLogService gzFaultReportLogService;

    /**
     * 查询应急事件记录列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(GzFaultReportLog gzFaultReportLog)
    {
        startPage();
        GzFaultReportLog log = new GzFaultReportLog();
        log.setGzId(gzFaultReportLog.getGzId());
        List<GzFaultReportLog> list = gzFaultReportLogService.selectGzFaultReportLogList(log);
        return getDataTable(list);
    }

    /**
     * 导出应急事件记录列表
     */
    @Log(title = "应急事件记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(GzFaultReportLog gzFaultReportLog)
    {
        List<GzFaultReportLog> list = gzFaultReportLogService.selectGzFaultReportLogList(gzFaultReportLog);
        ExcelUtil<GzFaultReportLog> util = new ExcelUtil<GzFaultReportLog>(GzFaultReportLog.class);
        return util.exportExcel(list, "log");
    }

    /**
     * 新增应急事件记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存应急事件记录
     */
    @Log(title = "应急事件记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(GzFaultReportLog gzFaultReportLog)
    {
        return toAjax(gzFaultReportLogService.insertGzFaultReportLog(gzFaultReportLog));
    }

    /**
     * 修改应急事件记录
     */
    @GetMapping("/edit/{gzLogId}")
    public String edit(@PathVariable("gzLogId") String gzLogId, ModelMap mmap)
    {
        GzFaultReportLog gzFaultReportLog = gzFaultReportLogService.selectGzFaultReportLogById(gzLogId);
        mmap.put("gzFaultReportLog", gzFaultReportLog);
        return prefix + "/edit";
    }

    /**
     * 修改保存应急事件记录
     */
    @Log(title = "应急事件记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(GzFaultReportLog gzFaultReportLog)
    {
        return toAjax(gzFaultReportLogService.updateGzFaultReportLog(gzFaultReportLog));
    }

    /**
     * 删除应急事件记录
     */
    @Log(title = "应急事件记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(gzFaultReportLogService.deleteGzFaultReportLogByIds(ids));
    }

    /**
     * 查看故障历史记录详情信息
     */
    @GetMapping("/detail/{gzLogId}")
    public String logDetail(@PathVariable("gzLogId") String gzLogId, ModelMap map) {
        map.put("gzFaultLog", gzFaultReportLogService.selectGzFaultReportLogById(gzLogId));
        return prefix + "/logDetail";
    }
}
