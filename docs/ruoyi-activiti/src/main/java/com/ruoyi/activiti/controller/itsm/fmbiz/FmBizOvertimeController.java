package com.ruoyi.activiti.controller.itsm.fmbiz;

import com.ruoyi.activiti.domain.FmBizOverTime;
import com.ruoyi.activiti.service.IFmBizKhService;
import com.ruoyi.activiti.service.IFmBizOvertimeService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 业务事件单考核超时Controller
 *
 * @author liul
 * @date 2021-03-15
 */
@Controller
@RequestMapping("/system/overtime")
public class FmBizOvertimeController extends BaseController {
    private String prefix = "system/overtime";
    private String prefixFm = "fmbiz";

    @Autowired
    private IFmBizOvertimeService fmBizOvertimeService;
    @Autowired
    private IFmBizKhService iFmBizKhService;

    @GetMapping("emergencytDataList")
    public String emergencytDataList(ModelMap mmap) {
        mmap.put("ifjjtype", "1");
        return prefixFm + "/assessmentList/emergencytDataList";
    }

    @GetMapping("nonEmergencytDataList")
    public String nonEmergencytDataList(ModelMap mmap) {
        mmap.put("ifjjtype", "2");
        return prefixFm + "/assessmentList/nonEmergencytDataList";
    }

    /**
     * 查询业务事件单考核超时列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmBizOverTime fmBizOvertime) {
        String dateTime = iFmBizKhService.getDateTime(fmBizOvertime.getDatetime());
        fmBizOvertime.setDatetime(dateTime);
        List<FmBizOverTime> list = fmBizOvertimeService.selectFmBizOvertimeList(fmBizOvertime);
        return getDataTable(list);
    }

    /**
     * 导出业务事件单考核超时列表
     */
    @Log(title = "业务事件单考核超时", businessType = BusinessType.EXPORT)
    @PostMapping("/exportList")
    @ResponseBody
    public AjaxResult exportList(FmBizOverTime fmBizOvertime) {
        String dateTime = iFmBizKhService.getDateTime(fmBizOvertime.getDatetime());
        fmBizOvertime.setDatetime(dateTime);
        List<FmBizOverTime> list = fmBizOvertimeService.selectFmBizOvertimeList(fmBizOvertime);
        ExcelUtil<FmBizOverTime> util = new ExcelUtil<FmBizOverTime>(FmBizOverTime.class);
        return util.exportExcel(list, "事件单超时详情表");
    }

    /**
     * 新增业务事件单考核超时
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存业务事件单考核超时
     */
    @Log(title = "业务事件单考核超时", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmBizOverTime fmBizOvertime) {
        return toAjax(fmBizOvertimeService.insertFmBizOvertime(fmBizOvertime));
    }

    /**
     * 修改业务事件单考核超时
     */
    @GetMapping("/edit/{singelNumber}")
    public String edit(@PathVariable("singelNumber") String singelNumber, ModelMap mmap) {
        FmBizOverTime fmBizOvertime = fmBizOvertimeService.selectFmBizOvertimeById(singelNumber);
        mmap.put("fmBizOvertime", fmBizOvertime);
        return prefix + "/edit";
    }

    /**
     * 修改保存业务事件单考核超时
     */
    @Log(title = "业务事件单考核超时", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmBizOverTime fmBizOvertime) {
        return toAjax(fmBizOvertimeService.updateFmBizOvertime(fmBizOvertime));
    }

    /**
     * 删除业务事件单考核超时
     */
    @Log(title = "业务事件单考核超时", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(fmBizOvertimeService.deleteFmBizOvertimeByIds(ids));
    }

    /**
     * 系统选择弹出页
     *
     * @return
     */
    @GetMapping("/application")
    public String selectOneApplication() {
        return prefixFm + "/assessmentList/selectOneApplication";
    }

}
