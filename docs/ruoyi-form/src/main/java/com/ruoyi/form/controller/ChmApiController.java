package com.ruoyi.form.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.form.domain.DesignBizChm;
import com.ruoyi.form.service.ChmApiService;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 硬件报障接口调用
 */
@Controller
@RequestMapping("/itsmChm")
public class ChmApiController {
    @Autowired
    private ChmApiService chmApiService;
    protected final Logger logger = LoggerFactory.getLogger(ChmApiController.class);
    /**
     * 机构人员信息同步
     * @return
     */
    @GetMapping("/getAllOrgandPerson")
    public String getAllOrgandPerson(){
        return "";
    }

    /**
     * 创建硬件报障单
     * @param jsonObject
     * @return
     */
    @ApiOperation("硬件开单接口")
    @PostMapping("/createEvent")
    @ResponseBody
    public JSONObject createEvent(@RequestBody JSONObject jsonObject){
        logger.info("======CHM=移动端调用开单接口入参 ："+jsonObject);
        return chmApiService.createEvent(jsonObject);
    }
    @PostMapping("/getUserListDetail")
    @ApiOperation(value = "硬件保障订单数据查询")
    @ResponseBody
    public JSONObject getUserListDetail(@RequestBody JSONObject jsonObject){
        logger.info("======CHM=硬件保障订单数据查询入参 ："+jsonObject);
        return chmApiService.getUserListDetail(jsonObject);
    }
    /**
     * 调用openApi 合胜开单接口
     * @param imCode
     * @return
     */
    @PostMapping("/createHeshengOpenApi")
    @ApiOperation(value = "调用openApi 合胜开单接口")
    @ResponseBody
    public JSONObject createHeshengOpenApi(@RequestBody String imCode) throws Exception {
        OgUser ogUser= CustomerBizInterceptor.currentUserThread.get();
        return chmApiService.createHeshengOpenApi(imCode,ogUser,"");
    }
    /**
     * 调用openApi 合胜进度查询
     * @param imCode
     * @return
     */
    @PostMapping("/getprocessingProgress")
    @ApiOperation(value = "调用openApi 合胜进度查询")
    @ResponseBody
    public JSONObject getprocessingProgress(@RequestBody String imCode) throws Exception {
        return chmApiService.getprocessingProgress(imCode);
    }
    /**
     * 调用openApi 合胜进度查询列表
     * @param imCode
     * @return
     */
    @PostMapping("/getprocessingProgressList")
    @ApiOperation(value = "调用openApi 合胜进度查询列表")
    @ResponseBody
    public AjaxResult getprocessingProgressList(String imCode) throws Exception {
        return chmApiService.getprocessingProgressList(imCode);
    }

    /**
     * 合胜回调 返回处理结果接口
     * @param request
     * @return
     */
    @PostMapping("/receiveResult")
    @ApiOperation(value = "合胜回调 返回处理结果接口")
    @ResponseBody
    public JSONObject receiveResult(@RequestBody JSONObject request){
        logger.info("======CHM=合胜回调 返回处理结果接口入参 ："+request);
        return chmApiService.receiveResult(request);
    }
    /**
     * 合胜回调 白名单校验接口
     * @param request
     * @return
     */
    @PostMapping("/whitelistVerification")
    @ApiOperation(value = "合胜回调 白名单校验接口")
    @ResponseBody
    public JSONObject whitelistVerification(@RequestBody String request){
        logger.info("======CHM=合胜回调 白名单校验接口入参 ："+request);
        JSONObject reJson= JSON.parseObject(request);
        return chmApiService.whitelistVerification(reJson);
    }
}
