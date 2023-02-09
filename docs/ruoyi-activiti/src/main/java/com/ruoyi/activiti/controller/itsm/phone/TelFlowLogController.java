package com.ruoyi.activiti.controller.itsm.phone;

import java.util.List;

import com.ruoyi.activiti.domain.TelFlowLog;
import com.ruoyi.activiti.service.ITelFlowLogService;
import com.ruoyi.common.utils.uuid.UUID;
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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 电话事件单（流程）
 */
@Controller
@RequestMapping("/log")
public class TelFlowLogController extends BaseController
{
    private String prefix = "log";

    @Autowired
    private ITelFlowLogService telFlowLogService;

    @GetMapping()
    public String log()
    {
        return prefix + "/log";
    }

    /**
     * 查询【流程】列表
     */
    @PostMapping("/list/{id}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("id")String id)
    {
        startPage();
        TelFlowLog tfl=new TelFlowLog();
        tfl.setTelId(id);
        List<TelFlowLog> list = telFlowLogService.selectTelFlowLogList(tfl);
        return getDataTable(list);
    }

    /**
     * 导出【流程】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TelFlowLog telFlowLog)
    {
        List<TelFlowLog> list = telFlowLogService.selectTelFlowLogList(telFlowLog);
        ExcelUtil<TelFlowLog> util = new ExcelUtil<TelFlowLog>(TelFlowLog.class);
        return util.exportExcel(list, "log");
    }

    /**
     * 新增【流程】
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TelFlowLog telFlowLog)
    {
        telFlowLog.setLogId(UUID.getUUIDStr());
        return toAjax(telFlowLogService.insertTelFlowLog(telFlowLog));
    }

    /**
     * 修改【流程】
     */
    @GetMapping("/edit/{logId}")
    public String edit(@PathVariable("logId") String logId, ModelMap mmap)
    {
        TelFlowLog telFlowLog = telFlowLogService.selectTelFlowLogById(logId);
        mmap.put("telFlowLog", telFlowLog);
        return prefix + "/edit";
    }

    /**
     * 修改保存【流程】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TelFlowLog telFlowLog)
    {
        return toAjax(telFlowLogService.updateTelFlowLog(telFlowLog));
    }

    /**
     * 删除【流程】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(telFlowLogService.deleteTelFlowLogByIds(ids));
    }
}
