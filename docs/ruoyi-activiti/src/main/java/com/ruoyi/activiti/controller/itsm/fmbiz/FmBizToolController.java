package com.ruoyi.activiti.controller.itsm.fmbiz;

import com.ruoyi.activiti.service.IFmBizToolService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBizTool;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 工具使用情况Controller
 *
 * @author ruoyi
 * @date 2021-01-21
 */
@Controller
@RequestMapping("/system/tool")
public class FmBizToolController extends BaseController {
    private String prefix = "system/tool";
    private String prefix_public = "fmbiz";

    @Autowired
    private IFmBizToolService fmBizToolService;

    @RequiresPermissions("system:tool:view")
    @GetMapping()
    public String tool() {
        return prefix + "/tool";
    }

    /**
     * 工具使用统计页面
     *
     * @return
     */
    @GetMapping("/fmbizTool")
    public String fmbizTool() {
        return prefix_public + "/appr/fmbizTool";
    }

    /**
     * 查询工具使用情况列表
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmBizTool fmBizTool) {
        startPage();
        List<FmBizTool> list = fmBizToolService.selectFmBizToolList(fmBizTool);
        return getDataTable(list);
    }

    /**
     * 导出工具使用情况列表
     */
    @Log(title = "工具使用情况", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmBizTool fmBizTool) {
        fmBizTool = new FmBizTool();

        List<FmBizTool> list = fmBizToolService.selectFmBizToolAppr(fmBizTool);

        if (fmBizTool.getParams().containsKey("countType") && "2".equals(fmBizTool.getParams().get("countType"))) {
            list = fmBizToolService.selectFmBizToolAppr2(fmBizTool);
        } else {
            list = fmBizToolService.selectFmBizToolAppr(fmBizTool);
        }
        ExcelUtil<FmBizTool> util = new ExcelUtil<FmBizTool>(FmBizTool.class);
        return util.exportExcel(list, "事件单工具使用情况");
    }

    /**
     * 新增工具使用情况
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存工具使用情况
     */
    @RequiresPermissions("system:tool:add")
    @Log(title = "工具使用情况", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmBizTool fmBizTool) {
        return toAjax(fmBizToolService.insertFmBizTool(fmBizTool));
    }

    /**
     * 修改工具使用情况
     */
    @GetMapping("/edit/{fbtId}")
    public String edit(@PathVariable("fbtId") String fbtId, ModelMap mmap) {
        FmBizTool fmBizTool = fmBizToolService.selectFmBizToolById(fbtId);
        mmap.put("fmBizTool", fmBizTool);
        return prefix + "/edit";
    }

    /**
     * 修改保存工具使用情况
     */
    @RequiresPermissions("system:tool:edit")
    @Log(title = "工具使用情况", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmBizTool fmBizTool) {
        return toAjax(fmBizToolService.updateFmBizTool(fmBizTool));
    }

    /**
     * 删除工具使用情况
     */
    @RequiresPermissions("system:tool:remove")
    @Log(title = "工具使用情况", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(fmBizToolService.deleteFmBizToolByIds(ids));
    }

    /**
     * 查询工具使用情况统计
     */
    @PostMapping("/selectFmBizTool")
    @ResponseBody
    public TableDataInfo selectFmBizTool(FmBizTool ft) throws ParseException {
        String startCreatTime = ft.getParams().get("startCreatTime").toString();
        String endCreatTime = ft.getParams().get("endCreatTime").toString();
        if (!"".equals(startCreatTime) && null != startCreatTime) {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startCreatTime);
            String d1 = new SimpleDateFormat("yyyyMMdd").format(date1);
            ft.getParams().put("startCreatTime", d1 + "000000");
        } else {
            Date date = new Date();//获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间
            String time = DateUtils.handleTimeYYYYMMDDHHMMSS(calendar.getTime());
            ft.getParams().put("startCreatTime", time);
        }
        if (!"".equals(endCreatTime) && null != endCreatTime) {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endCreatTime);
            String d2 = new SimpleDateFormat("yyyyMMdd").format(date2);
            ft.getParams().put("endCreatTime", d2 + "240000");
        } else {
            ft.getParams().put("endCreatTime", DateUtils.dateTimeNow());
        }
        List<FmBizTool> list = fmBizToolService.selectFmBizToolAppr(ft);

        if (ft.getParams().containsKey("countType") && "2".equals(ft.getParams().get("countType"))) {
            list = fmBizToolService.selectFmBizToolAppr2(ft);
        } else {
            list = fmBizToolService.selectFmBizToolAppr(ft);
        }
        return getDataTable(list);
    }
}
