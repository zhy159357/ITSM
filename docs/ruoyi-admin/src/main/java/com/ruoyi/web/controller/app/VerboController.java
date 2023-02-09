package com.ruoyi.web.controller.app;
import com.ruoyi.activiti.service.IVerboService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 告警监控
 */
@RestController
@RequestMapping("/appMobile/verbo")
public class VerboController extends BaseController{

    @Autowired
    private IVerboService iVerboService;

    /**
     * 查询告警的列表（也就是显示所有的数据）//未接手，待确认、处理中
     * @return
     */
    @PostMapping("/listSeverity")
    public AjaxResult listsSeverity() {
        List list = iVerboService.selectSeverity();
        return AjaxResult.success(list);
    }

    @PostMapping("/listStatus")
    public AjaxResult listsStatus() {
        List list = iVerboService.selectStatus();
        return AjaxResult.success(list);
    }

    @PostMapping("/listTarget")
    public AjaxResult targetList() {
        List list = iVerboService.targetList();
        return AjaxResult.success(list);
    }
}
