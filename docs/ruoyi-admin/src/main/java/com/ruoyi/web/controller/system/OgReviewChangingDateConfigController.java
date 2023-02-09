package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.OgReviewChangingDateConfig;
import com.ruoyi.system.service.IOgReviewChangingDateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 人员 信息操作处理
 */

@Controller
@RequestMapping("/system/rcdc")
public class OgReviewChangingDateConfigController extends BaseController {

    private static final String PREFIX = "system/rcdc";

    @Autowired
    private IOgReviewChangingDateConfigService configService;

    @RequestMapping()
    public String show(){
        return PREFIX + "/index";
    }

    /**
     * 新增人员
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/add";
    }


    /**
     * 新人人员保存
     */
    @PostMapping("/config")
    @ResponseBody
    public AjaxResult config(@RequestBody OgReviewChangingDateConfig config){
        return toAjax(this.configService.config(config));
    }

    @GetMapping("/config")
    @ResponseBody
    public AjaxResult getConfig() {
        return AjaxResult.success("OK", this.configService.getOne(null));
    }
}
