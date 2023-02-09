package com.ruoyi.activiti.controller.itsm.duty;

import com.ruoyi.activiti.service.IDutySchedulingService;
import com.ruoyi.activiti.service.IDutySubstituteService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.DutySubstitute;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.SubstituteStatus;
import com.ruoyi.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 值班视图Controller
 * @author dayong_sun
 * @date 2020-12-06
 */
@Controller
@RequestMapping("duty/substitute")
public class SubstituteController extends BaseController {
    private String prefix = "duty/substitute";

    @Autowired
    private IDutySubstituteService dutySubstituteService;
    @Autowired
    private IDutySchedulingService dutySchedulingService;

    @GetMapping("/apply")
    public String apply(ModelMap mmap) {
        mmap.put("statusList",SubstituteStatus.getList());
        return prefix + "/apply";
    }

    @GetMapping("/audit")
    public String audit(ModelMap mmap) {
        mmap.put("statusList",SubstituteStatus.getList());
        return prefix + "/audit";
    }

    /**
     * 查询替班列表
     */
    @PostMapping("/alist")
    @ResponseBody
    public TableDataInfo alist(DutySubstitute dutySubstitute) {
        startPage();
        dutySubstitute.setPid(ShiroUtils.getOgUser().getpId());
        List<DutySubstitute> list = dutySubstituteService.selectSubstituteList(dutySubstitute);
        for(DutySubstitute substitute : list){
            substitute.setStatus(SubstituteStatus.getName(substitute.getStatus()));
        }
        return getDataTable(list);
    }

    /**
     * 查询替班列表
     */
    @PostMapping("/tlist")
    @ResponseBody
    public TableDataInfo tlist(DutySubstitute dutySubstitute) {
        startPage();
        dutySubstitute.setTid(ShiroUtils.getOgUser().getpId());
        List<DutySubstitute> list = dutySubstituteService.selectSubstituteList(dutySubstitute);
        for(DutySubstitute substitute : list){
            substitute.setStatus(SubstituteStatus.getName(substitute.getStatus()));
        }
        return getDataTable(list);
    }

    /**
     * 审核替班信息
     */
    @GetMapping("/edit/{substituteId}")
    public String edit(@PathVariable("substituteId") String substituteId, ModelMap mmap) {
        DutySubstitute substitute = dutySubstituteService.selectSubstituteById(substituteId);
        mmap.put("substitute",substitute);
        return prefix + "/edit";
    }

    /**
     * 保存替班信息
     */
    @Log(title = "参数列别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DutySubstitute dutySubstitute) {
        return toAjax(dutySubstituteService.updateDutySubstitute(dutySubstitute));
    }

}
