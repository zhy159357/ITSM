package com.ruoyi.form.controller.customerForm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.service.ComprehensiveQueryService;
import com.ruoyi.form.strategy.impl.CustomerStrategyContext;
import com.ruoyi.form.vo.ComprehensiveQuery;
import com.ruoyi.form.vo.ExportExcelParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName ComprehensiveQueryController
 * @Description 综合查询模块
 * @Author JiaQi Zhang
 * @Date 2022/9/20 13:42
 */
@RestController
@RequestMapping("/customerForm")
@Api(tags = "综合查询模块")
@CrossOrigin
@RequiredArgsConstructor
public class ComprehensiveQueryController extends BaseController {

    private final ComprehensiveQueryService comprehensiveQueryService;

    private final CustomerStrategyContext customerStrategyContext;

    @PostMapping("/comprehensiveQuery")
    @ApiOperation("综合查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comprehensiveQuery",value = "表单列表搜索条件",dataType = "ComprehensiveQuery"),
    })
    public AjaxResult comprehensiveQuery(@RequestBody ComprehensiveQuery comprehensiveQuery){
        Page page = buildPageObject();
        return  comprehensiveQueryService.comprehensiveQuery(page,comprehensiveQuery);
    }


    @GetMapping("/comprehensiveQuery")
    @ApiOperation("获取表单存储信息详情(包含审批节点中已填写的表单信息)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表名",required = true),
            @ApiImplicitParam(name = "id",value = "表单列表ID",required = true),
            @ApiImplicitParam(name = "processId",value = "流程实例ID",required = false)
    })
    public AjaxResult getFormJsonData(String code,Integer id,String processId){

        return customerStrategyContext.customerDetailsPage(code, id,processId);
    }

    @GetMapping("/getBizCode")
    @ApiOperation("获取流程名称及code")
    public AjaxResult getBizCode(){
        return comprehensiveQueryService.getBizCode();
    }


    @PostMapping("/exportExcel")
    @ApiOperation("导出excel数据")
    public void exportExcel(@RequestBody ExportExcelParams params, HttpServletResponse response){
        comprehensiveQueryService.exportExcel(params,response);
    }

}
