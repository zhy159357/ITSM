package com.ruoyi.form.controller.customerForm;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.service.StatisticalAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName StatisticalAnalysisController
 * @Description 首页统计分析
 * @Author JiaQi Zhang
 * @Date 2022/9/29 15:42
 */
@RestController
@Api(tags = "首页控制台统计分析")
@RequestMapping("/customerForm")
public class StatisticalAnalysisController {


    @Resource
    StatisticalAnalysisService statisticalAnalysisService;


    @GetMapping("/statisticalAnalysis")
    @ApiOperation("统计分析")
    public AjaxResult statisticalAnalysis(){
        return statisticalAnalysisService.statisticalAnalysis();
    }
}
