package com.ruoyi.form.controller.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.form.service.IServiceRuleService;
import com.ruoyi.form.service.IServiceTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 服务模块-规则接口
 */
@RestController
@RequestMapping("/customerForm/service")
public class ServiceTicketController {
    @Autowired
    private IServiceTicketService ticketService;

    @PostMapping("/ticketStatus")
    public AjaxResult ruleStatus() {
        return AjaxResult.success("获取成功", this.ticketService.ticketStatus());
    }
}
