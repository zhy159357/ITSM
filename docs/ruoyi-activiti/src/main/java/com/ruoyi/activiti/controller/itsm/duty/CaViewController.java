package com.ruoyi.activiti.controller.itsm.duty;

import com.ruoyi.activiti.service.IDutySchedulingService;
import com.ruoyi.activiti.service.IDutyViewService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.DutyPerson;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.common.core.domain.entity.DutySubstitute;
import com.ruoyi.common.core.domain.entity.DutyVersion;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.ruoyi.activiti.constants.VersionStatusConstants.QGZX_ORGID;


/**
 * 值班视图Controller
 * @author dayong_sun
 * @date 2022-03-07
 */
@Controller
@RequestMapping("duty/caview")
public class CaViewController extends BaseController {
    private String prefix = "duty/view";

    @Autowired
    private IDutyViewService dutyViewService;
    @Autowired
    private IDutySchedulingService dutySchedulingService;

    @GetMapping()
    public String scheduling(ModelMap modelMap) {
        modelMap.put("today", DateUtils.getDate());
        return prefix + "/caview";
    }

    /**
     * 查询值班视图列表
     */
    @PostMapping("/list")
    @ResponseBody
    public List<DutyScheduling> list(DutyScheduling dutyScheduling) {
//        startPage();//去掉分页
        return dutyViewService.selectDutySchedulingList(dutyScheduling);
    }

    /**
     * 查询值班视图列表
     */
    @PostMapping("/exclehead")
    @ResponseBody
    public Map<String,Object> exclehead(DutyVersion dutyVersion) {
        return dutyViewService.selectVersionByDutyDateMysql(dutyVersion);
    }

    @PostMapping("/exportExcel")
    @ResponseBody
    public AjaxResult exportExcel(DutyVersion dutyVersion)
    {
        return dutyViewService.exportExcelMysql(dutyVersion);
    }

    /**
     * 查询值班视图列表
     */
    @PostMapping("/calendar")
    @ResponseBody
    public List<DutyVersion> calendar(DutyVersion dutyVersion) {
        return dutyViewService.selectCalendar(dutyVersion);
    }

    /**
     * 新增参数列别
     */
    @GetMapping("/substitute/{dutyDate}")
    public String add(@PathVariable("dutyDate") String dutyDate, ModelMap mmap) {
        List<DutyVersion> version = dutyViewService.selectSchedulingById(dutyDate);
        List<DutyPerson> userlist = dutySchedulingService.selectOgPersonList(QGZX_ORGID);
        mmap.put("version",version);
        mmap.put("versionOne",version.get(0));
        mmap.put("userlist", userlist);
        return prefix + "/substitute";
    }

    /**
     * 校验数据是否存在
     */
    @Log(title = "参数列别", businessType = BusinessType.DELETE)
    @PostMapping("/addcheck")
    @ResponseBody
    public int addCheck(DutySubstitute dutySubstitute) {
        return dutyViewService.addCheck(dutySubstitute);
    }

    /**
     * 校验当前月份是否存在值班信息
     */
    @Log(title = "参数列别", businessType = BusinessType.DELETE)
    @PostMapping("/addDateCheck")
    @ResponseBody
    public int addDateCheck(DutySubstitute dutySubstitute) {
        return dutyViewService.addDateCheck(dutySubstitute);
    }

    /**
     * 新增替班记录
     */
    @Log(title = "替班记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DutySubstitute dutySubstitute) {
        return toAjax(dutyViewService.insertDutySubstitute(dutySubstitute));
    }

}
