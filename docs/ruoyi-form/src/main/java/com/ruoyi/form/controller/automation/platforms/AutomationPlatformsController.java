package com.ruoyi.form.controller.automation.platforms;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.entity.AutomationPlatFormsParams;
import com.ruoyi.form.entity.automation.CheckStatus;
import com.ruoyi.form.entity.automation.EquipmentInfo;
import com.ruoyi.form.entity.automation.JsonParamArr;
import com.ruoyi.form.service.AutomationPlatformsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/automation/platforms")
public class AutomationPlatformsController {

    @Autowired
    private AutomationPlatformsService automationPlatformsService;

    @PostMapping("commitTask")
    @ResponseBody
    @ApiOperation("对接自动化平台")
    public AjaxResult commitTask(@RequestBody AutomationPlatFormsParams automationPlatFormsParams) {
        //特殊参数
        ArrayList<JsonParamArr> jsonParamArrs = new ArrayList<>();
        JsonParamArr jsonParamArr = new JsonParamArr("test001","String","test001","这是一条测试数据");
        jsonParamArrs.add(jsonParamArr);
        automationPlatFormsParams.setJsonParamArrList(jsonParamArrs);
        List<CheckStatus> checkStatus = automationPlatformsService.commitTask(automationPlatFormsParams);
        return AjaxResult.success(checkStatus);
    }

    @GetMapping("getEquipmentInfoUrl")
    @ResponseBody
    @ApiOperation("获取设备信息")
    public AjaxResult getEquipmentInfoUrl() {
        List<EquipmentInfo> equipmentInfoUrl = automationPlatformsService.getEquipmentInfoUrl();
        return AjaxResult.success(equipmentInfoUrl);
    }
}
