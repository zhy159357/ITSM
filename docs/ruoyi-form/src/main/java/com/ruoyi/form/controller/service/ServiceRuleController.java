package com.ruoyi.form.controller.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.service.IServiceRuleService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 服务模块-规则接口
 */
@RestController
@RequestMapping("/customerForm/service")
public class ServiceRuleController {
    @Autowired
    private IServiceRuleService ruleService;

    @PostMapping("/ticketTypes")
    public AjaxResult ticketTypes() {
        return AjaxResult.success("获取成功", this.ruleService.getTicketTypes());
    }

    @PostMapping("/ruleTypes")
    public AjaxResult ruleTypes() {
        return AjaxResult.success("获取成功", this.ruleService.getRuleTypes());
    }

    @PostMapping("/recheckStatus")
    public AjaxResult recheckStatus() {
        return AjaxResult.success("获取成功", this.ruleService.recheckStatus());
    }

    @PostMapping("/ruleStatus")
    public AjaxResult ruleStatus() {
        return AjaxResult.success("获取成功", this.ruleService.ruleStatus());
    }

    @PostMapping("/commitUsers")
    public AjaxResult commitUsers() {
        return AjaxResult.success("获取成功", this.ruleService.commitUsers());
    }

    @PostMapping("/allUsers")
    public AjaxResult checkPeoples(@Param("f") Integer f) {
        return AjaxResult.success("获取成功", this.ruleService.allUsers(f));
    }
}
