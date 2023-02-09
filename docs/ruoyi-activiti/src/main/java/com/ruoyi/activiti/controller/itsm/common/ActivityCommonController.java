package com.ruoyi.activiti.controller.itsm.common;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 *工作流引擎公共方法
 * @author 14735
 * @date 2020-12-17
 */
@Controller
@RequestMapping("/common")
public class ActivityCommonController extends BaseController {

    @Autowired
    private ISysWorkService sysWorkService;

    @PostMapping("/selectOgPersonByGroupName")
    @ResponseBody
    public TableDataInfo selectOgPersonByGroupName(String groupName){
        startPage();
        List<OgPerson> list = sysWorkService.selectOgPersonByGroupName(groupName);
        return getDataTable(list);
    }
}
