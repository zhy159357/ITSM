package com.ruoyi.activiti.controller.itsm.fmbiz;

import com.ruoyi.activiti.domain.FmBizKh;
import com.ruoyi.activiti.service.IFmBizKhService;
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
 * 2021业务事件考核Controller
 *
 * @author liul
 * @date 2021-03-15
 */
@Controller
@RequestMapping("/system/kh")
public class FmBizKhController extends BaseController {
    private String prefix = "system/kh";
    private String prefixFm = "fmbiz";

    @Autowired
    private IFmBizKhService fmBizKhService;

    @GetMapping("appraisalDataList")
    public String kh() {
        return prefixFm + "/assessmentList/appraisalDataList";
    }

    /**
     * 查询2021业务事件考核列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmBizKh fmBizKh) {
        String dateTime = fmBizKhService.getDateTime(fmBizKh.getDatetime());
        fmBizKh.setDatetime(dateTime);
        List<FmBizKh> list = fmBizKhService.selectFmBizKhList(fmBizKh);
        return getDataTable(list);
    }

    /**
     * 导出2021业务事件考核列表
     */
    @Log(title = "事件单考核情况汇总表", businessType = BusinessType.EXPORT)
    @PostMapping("/exportList")
    @ResponseBody
    public AjaxResult export(FmBizKh fmBizKh) {
        String dateTime = fmBizKhService.getDateTime(fmBizKh.getDatetime());
        fmBizKh.setDatetime(dateTime);
        List<FmBizKh> list = fmBizKhService.selectFmBizKhList(fmBizKh);
        ExcelUtil<FmBizKh> util = new ExcelUtil<FmBizKh>(FmBizKh.class);
        return util.exportExcel(list, "事件单考核情况汇总表");
    }

    /**
     * 新增2021业务事件考核
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存2021业务事件考核
     */
    @Log(title = "2021业务事件考核", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmBizKh fmBizKh) {
        return toAjax(fmBizKhService.insertFmBizKh(fmBizKh));
    }

    /**
     * 修改2021业务事件考核
     */
    @GetMapping("/edit/{kid}")
    public String edit(@PathVariable("kid") String kid, ModelMap mmap) {
        FmBizKh fmBizKh = fmBizKhService.selectFmBizKhById(kid);
        mmap.put("fmBizKh", fmBizKh);
        return prefix + "/edit";
    }

    /**
     * 修改保存2021业务事件考核
     */
    @Log(title = "2021业务事件考核", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmBizKh fmBizKh) {
        return toAjax(fmBizKhService.updateFmBizKh(fmBizKh));
    }

    /**
     * 删除2021业务事件考核
     */
    @Log(title = "2021业务事件考核", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(fmBizKhService.deleteFmBizKhByIds(ids));
    }

    /**
     * 系统选择弹出页
     *
     * @return
     */
    @GetMapping("/application")
    public String selectOneApplication() {
        return prefixFm + "/assessmentList/selectTwoApplication";
    }
}
