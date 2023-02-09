package com.ruoyi.activiti.controller.itsm.fmbiz;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.activiti.service.IDifficultEventsService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.common.core.domain.entity.DifficultEvents;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【疑难事件】Controller
 *
 * @author liul
 * @date 2021-11-23
 */
@Controller
@RequestMapping("/system/events")
public class DifficultEventsController extends BaseController {
    private String prefix = "difficultEvents";

    @Autowired
    private IDifficultEventsService difficultEventsService;
    @Autowired
    private IFmBizService iFmBizService;

    /**
     * 查询疑难事件菜单打开
     *
     * @return
     */
    @GetMapping("/search")
    public String search() {
        return prefix + "/search";
    }

    /**
     * 待处理疑难事件
     *
     * @return
     */
    @GetMapping("/deal")
    public String deal() {
        return prefix + "/deal";
    }

    /**
     * 查询疑难事件列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DifficultEvents difficultEvents) {
        startPage();
        List<DifficultEvents> list = difficultEventsService.selectDifficultEventsList(difficultEvents);
        return getDataTable(list);
    }

    /**
     * 查询待处理疑难事件列表
     */
    @PostMapping("/dealList")
    @ResponseBody
    public TableDataInfo dealList(DifficultEvents difficultEvents) {
        return getDataTable_ideal(difficultEventsService.getDealList(difficultEvents));
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DifficultEvents difficultEvents) {
        String isCurrentPage = (String) difficultEvents.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<DifficultEvents> list = difficultEventsService.selectDifficultEventsList(difficultEvents);
        ExcelUtil<DifficultEvents> util = new ExcelUtil<DifficultEvents>(DifficultEvents.class);
        return util.exportExcel(list, "疑难事件单");
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }


    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        DifficultEvents difficultEvents = difficultEventsService.selectDifficultEventsById(id);
        mmap.put("difficultEvents", difficultEvents);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DifficultEvents difficultEvents) {
        return toAjax(difficultEventsService.updateDifficultEvents(difficultEvents));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(difficultEventsService.deleteDifficultEventsByIds(ids));
    }

    /**
     * 点击处理按钮
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/deal/{id}")
    public String deal(@PathVariable("id") String id, ModelMap mmap) {
        DifficultEvents difficultEvents = difficultEventsService.selectDifficultEventsById(id);
        mmap.put("difficultEvents", difficultEvents);
        return prefix + "/flow/deal";
    }

    /**
     * 处理疑难事件
     *
     * @param difficultEvents
     * @return
     */
    @PostMapping("/saveFlowDeal")
    @ResponseBody
    public AjaxResult saveFlowDeal(DifficultEvents difficultEvents) {
        return difficultEventsService.saveFlowDeal(difficultEvents);
    }

    /**
     * 所属系统选择页面
     *
     * @return
     */
    @GetMapping("/sysGrouplist")
    public String selectOneApplication() {
        return prefix + "/flow/subpage/selectOneSys";
    }

    /**
     * 根据事件单ID查询疑难事件
     */
    @PostMapping("/fmbizList/{fmId}")
    @ResponseBody
    public TableDataInfo fmbizList(@PathVariable("fmId") String fmId) {
        List<FmBiz> fmBizs = new ArrayList<>();
        FmBiz fb = iFmBizService.selectFmBizById(fmId);
        if (!ObjectUtils.isEmpty(fb)) {
            fmBizs.add(fb);
        }
        return getDataTable(fmBizs);
    }

    /**
     * 查看详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        DifficultEvents difficultEvents = difficultEventsService.selectDifficultEventsById(id);
        mmap.put("difficultEvents", difficultEvents);
        return prefix + "/view";
    }

    /**
     * 根据传入事件单ID查询对应的疑难事件
     *
     * @param fmId
     * @return
     */
    @PostMapping("/diffList/{fmId}")
    @ResponseBody
    public TableDataInfo diffList(String fmId) {
        DifficultEvents de = new DifficultEvents();
        de.setFmId(fmId);
        return getDataTable(difficultEventsService.selectDifficultEventsList(de));
    }
}
