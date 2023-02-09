package com.ruoyi.activiti.controller.itsm.duty;

import com.ruoyi.activiti.service.IDutyAccountService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 值班绑定Controller
 * @author dayong_sun
 * @date 2020-03-29
 */
@Controller
@RequestMapping("duty/bind")
public class BindController extends BaseController {
    private String prefix = "duty/bind";

    @Autowired
    private IDutyAccountService dutyAccountService;

    @GetMapping()
    public String scheduling(ModelMap mmap) {
        OgUser ogUser = ShiroUtils.getOgUser();
        mmap.put("ogUser",ogUser);
        return prefix + "/bind";
    }

    /**
     * 值班绑定添加校验
     */
    @Log(title = "值班绑定", businessType = BusinessType.INSERT)
    @PostMapping("/addCheck")
    @ResponseBody
    public Map<String,Object> addCheck(DutyAccount dutyAccount) {
        return dutyAccountService.addCheck(dutyAccount);
    }

    /**
     * 新增值班绑定
     */
    @Log(title = "值班绑定", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DutyAccount dutyAccount) {
        return toAjax(dutyAccountService.insertDutyAccount(dutyAccount));
    }

}
