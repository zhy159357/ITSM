package com.ruoyi.form.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.service.IncidentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/customerForm")
@Api(tags = "事件单抢单")
@RestController
@CrossOrigin
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    // 事件单抢单
    @GetMapping("/rob/task")
    @ApiOperation("事件单抢单")
    public AjaxResult robTask(String imCode) {
        return incidentService.robTask(imCode);
    }
}
