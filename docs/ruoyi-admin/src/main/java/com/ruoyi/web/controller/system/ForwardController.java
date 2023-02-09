package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.service.forward.IForwardService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.ApiResData;
import com.ruoyi.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 软研接口测试controller类
 *
 * @author 14735
 */
@Controller
@RequestMapping("/forward")
public class ForwardController extends BaseController {
    @Autowired
    private IForwardService forwardService;

    @PostMapping("/getCenter")
    @ResponseBody
    public ApiResData getCenter() {
        String json = null;
        ApiResData apiResData = null;
        try {
            /*List<Map<String, String>> center = forwardService.getCenter();
            json = JSON.toJSONString(center);*/
            apiResData = new ApiResData("000000", "", json);
        } catch (BusinessException e) {
            apiResData = new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            apiResData = new ApiResData("999999", "系统错误");
            e.printStackTrace();
        }
        return apiResData;
    }

    @PostMapping("/getPubParavaluesByParaName")
    @ResponseBody
    public ApiResData getCenter(@RequestBody Map<String, Object> variables) {
        String json = null;
        ApiResData apiResData = null;
        try {
           /* List paravalues = forwardService.getPubParavaluesByParaName((String) variables.get("paraName"));
            json = JSON.toJSONString(paravalues);*/
            apiResData = new ApiResData("000000", "", json);
        } catch (BusinessException e) {
            apiResData = new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            apiResData = new ApiResData("999999", "系统错误");
            e.printStackTrace();
        }
        return apiResData;
    }

    @PostMapping("/getVmBizInfo")
    @ResponseBody
    public ApiResData getVmBizInfo(@RequestBody Map<String, Object> variables) {
        String json = null;
        ApiResData apiResData = null;
        try {
            Map map = forwardService.getVmBizInfo((String) variables.get("versionInfoId"));
            json = JSON.toJSONString(map);
            apiResData = new ApiResData("000000", "", json);
        } catch (BusinessException e) {
            apiResData = new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            apiResData = new ApiResData("999999", "系统错误");
            e.printStackTrace();
        }
        return apiResData;
    }

    /*@PostMapping("/getNo")
    @ResponseBody
    public ApiResData getNo(@RequestBody Map<String, Object> variables) {
        String json = null;
        ApiResData apiResData = null;
        try {
            Map map = forwardService.getNo((String) variables.get("type"));
            json = JSON.toJSONString(map);
            apiResData = new ApiResData("000000", "", json);
        } catch (BusinessException e) {
            apiResData = new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            apiResData = new ApiResData("999999", "系统错误");
            e.printStackTrace();
        }
        return apiResData;
    }*/

    @PostMapping("/getUserPagerTasks")
    @ResponseBody
    public ApiResData getUserPagerTasks(@RequestBody Map<String, Object> variables) {
        String initiator = (String) variables.get("telephone");
        String name = (String) variables.get("name");
        String caption = (String) variables.get("caption");
        String versionInfoNo = (String) variables.get("versionInfoNo");
        String versionInfoName = (String) variables.get("versionInfoName");
        String versionProducerId = (String) variables.get("versionProducerId");
        Integer pageIndex = (Integer) variables.get("pageIndex");
        Integer pageSize = (Integer) variables.get("pageSize");

        String json = null;
        ApiResData apiResData = null;
        try {
           /* List list = forwardService.getUserPagerTasks(initiator, name, caption, versionInfoNo, versionInfoName, versionProducerId, pageIndex, pageSize);
            list = ListUtils.subList(list, pageIndex, pageSize);
            json = JSON.toJSONString(list);*/
            apiResData = new ApiResData("000000", "", json);
        } catch (BusinessException e) {
            apiResData = new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            apiResData = new ApiResData("999999", "系统错误");
            e.printStackTrace();
        }
        return apiResData;
    }

    @PostMapping("/getFlowImage")
    @ResponseBody
    public ApiResData getFlowImage(@RequestBody Map<String, String> variables) throws Exception {
        String json = null;
        ApiResData apiResData = null;
        try {
            Map map = forwardService.getFlowImage(variables.get("versionInfoId"), "VmBn");
            json = JSON.toJSONString(map);
            apiResData = new ApiResData("000000", "", json);
        } catch (BusinessException e) {
            apiResData = new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            apiResData = new ApiResData("999999", "系统错误");
            e.printStackTrace();
        }
        return apiResData;
    }

    @PostMapping("/saveFlowJs")
    @ResponseBody
    public ApiResData saveFlowJs(@RequestBody Map<String, Object> variables) {
        try {
            forwardService.saveFlowJs((String) variables.get("initiator"), variables);
        } catch (BusinessException e) {
            new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            new ApiResData("999999", "系统错误");
            e.printStackTrace();
        }
        return new ApiResData("000000");
    }

    @PostMapping("/saveFlowJsLd")
    @ResponseBody
    public ApiResData saveFlowJsLd(@RequestBody Map<String, Object> variables) {
        try {
            forwardService.saveFlowJsLd((String) variables.get("initiator"), (String) variables.get("versionInfoId"), (String) variables.get("technologyLeadOpinion"));
        } catch (BusinessException e) {
            new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            new ApiResData("999999", "系统错误");
            e.printStackTrace();
        }
        return new ApiResData("000000");
    }

    @PostMapping("/getPagerFlowLogs")
    @ResponseBody
    public ApiResData getPagerFlowLogs(@RequestBody Map<String, Object> variables) {
        String json = null;
        ApiResData apiResData = null;
        /*try {
            List flowLogs = forwardService.getPagerFlowLogs((String) variables.get("versionInfoId"));
            json = JSON.toJSONString(flowLogs);
            apiResData = new ApiResData("000000", "", json);
        } catch (BusinessException e) {
            apiResData = new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            apiResData = new ApiResData("999999", "系统错误");
            e.printStackTrace();
        }*/
        return apiResData;
    }
}
