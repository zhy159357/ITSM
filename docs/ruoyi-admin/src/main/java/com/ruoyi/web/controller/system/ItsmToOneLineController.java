package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/sendOneLine")
public class ItsmToOneLineController extends BaseController {

    @Autowired
    private IFmBizService iFmBizService;

    /**
     * 一线管理系统调用根据单号获取运维事件单
     *
     * @param map
     * @return
     */
//    @PostMapping("/getFmBiz")
//    @ResponseBody
//    public AjaxResult getFmBiz(@RequestBody Map<String, String> map) {
//        return iFmBizService.getFmBizOneLineApi(map);
//    }

    /**
     * 一线管理系统调用根据单号获取监控事件单
     *
     * @param map
     * @return
     */
//    @PostMapping("/getEventRun")
//    @ResponseBody
//    public AjaxResult getEventRun(@RequestBody Map<String, String> map) {
//        return null;
//    }
}
