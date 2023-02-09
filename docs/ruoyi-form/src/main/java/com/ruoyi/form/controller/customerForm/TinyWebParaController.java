package com.ruoyi.form.controller.customerForm;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.http.entegorserver.entity.LabelValue;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customerForm")
@Api(tags = "自定义表单模块")
@CrossOrigin
public class TinyWebParaController extends BaseController {

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @PostMapping("/selectCheckPersonForTinyWeb")
    @ResponseBody
    @ApiOperation("查询审核人tinyWeb")
    public AjaxResult selectCheckPersonForTinyWeb(@RequestBody Map<String, String> requestMap) {

        List<LabelValue> labelValueList = new ArrayList<>();
        try {

            labelValueList = ogPersonService.selectCheckForTinyWeb(requestMap.get("postIds"));
            if (!CollectionUtils.isEmpty(labelValueList)) {

                for (LabelValue labelValue : labelValueList) {

                    if (labelValue.getSpare().equals("310200190")) {
                        labelValue.setLabel("数据中心-" + labelValue.getLabel());
                    } else if (labelValue.getSpare().equals("310200178")) {
                        labelValue.setLabel("开发中心-" + labelValue.getLabel());
                    } else {
                        labelValue.setLabel("分行-" + labelValue.getLabel());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(labelValueList);
        return AjaxResult.success(labelValueList);
    }

    @PostMapping("/selectContactPersonForTinyWeb")
    @ResponseBody
    @ApiOperation("查询接口人tinyWeb")
    public AjaxResult selectContactPersonForTinyWeb(@RequestBody Map<String, String> requestMap) {

        List<LabelValue> labelValueList = new ArrayList<>();
        try {

            labelValueList = ogPersonService.selectContactPersonForTinyWeb(requestMap.get("orgId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success(labelValueList);
    }

    //应用系统数据源配置接口
    @PostMapping("/selectOgSysForTinyWeb")
    @ResponseBody
    @ApiOperation("查询应用系统tinyWeb")
    public AjaxResult selectOgSysForTinyWeb(@RequestBody Map<String, String> requestMap) {

        List<LabelValue> labelValueList = new ArrayList<>();
        String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        try {

            labelValueList = applicationManagerService.selectOgSysForTinyWeb(requestMap.get("flag"), userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success(labelValueList);

    }

    @PostMapping("/selectPubParaValueForTinyWeb")
    @ResponseBody
    @ApiOperation("查询参数接口tinyWeb")
    public AjaxResult selectPubParaValueForTinyWeb(@RequestBody Map<String, String> requestMap) {

        List<LabelValue> labelValueList = new ArrayList<>();

        try {

            labelValueList = pubParaValueService.selectPubParaValueForTinyWeb(requestMap.get("paraName"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success(labelValueList);
    }
}
