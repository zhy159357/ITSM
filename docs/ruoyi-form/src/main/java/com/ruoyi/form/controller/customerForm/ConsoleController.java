package com.ruoyi.form.controller.customerForm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.form.domain.ImplRecord;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.service.*;
import com.ruoyi.form.strategy.CustomerStrategy;
import com.ruoyi.form.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName IncidentController
 * @Description 事件单控制台
 * @Author JiaQi Zhang
 * @Date 2022/10/30 9:29
 */
@RestController
@RequestMapping("/customerForm/console")
@Api(tags = "控制台")
@RequiredArgsConstructor
public class ConsoleController extends BaseController {

    private final CustomerStrategy customerStrategy;
    private final CustomerFormMapper customerFormMapper;
    private final DesignBizIncidentService designBizIncidentService;
    private final DesignBizProblemService designBizProblemService;
    private final DesignBizChangeService designBizChangeService;
    private final DesignBizProblemTaskNewService designBizProblemTaskNewService;
    private final DesignBizChangetaskService designBizChangetaskService;

    /**
     * 概述控制台接口
     *
     * @param searchType 搜索类型
     * @return 返回结果
     */
    @GetMapping("/summaryDesk")
    @ApiOperation("概述控制台")
    @ApiImplicitParam(name = "searchType", value = "检索类型，1-由我提交，2-已指派给我")
    public AjaxResult summaryConsoleDesk(@RequestParam String searchType) {
        Page page = buildPageObject();
        return customerStrategy.summaryConsoleDesk(page,searchType);
    }


    /**
     * 事件控制台
     *
     * @return 返回结果
     */
    @PostMapping("/incidentConsole")
    @ApiOperation("事件控制台")
    public AjaxResult incidentConsole() {
        Page page = buildPageObject();
        return customerStrategy.incidentConsole(page);
    }

    @GetMapping("/getVersionInfos")
    @ApiOperation("获取表单版本号")
    public AjaxResult getIncidentVersion(String code) {
        return AjaxResult.success(customerFormMapper.getCurrentFormAllVersions(String.format("%s_%s", "design_biz", code)));
    }

    @PostMapping("/incidentRetrieval")
    @ApiOperation("事件检索")
    public AjaxResult incidentRetrieval(@RequestBody IncidentRetrievalVo retrievalVo) {
        Page page = buildPageObject();
        return designBizIncidentService.incidentRetrieval(page,retrievalVo);
    }

    @PostMapping("/incidentRetrievalExport")
    @ApiOperation("事件检索导出")
    public void incidentRetrievalExport(@RequestBody IncidentRetrievalVo retrievalVo,HttpServletResponse response) {
        designBizIncidentService.incidentRetrieval(retrievalVo,response);
    }


    @PostMapping("problemRetrieval")
    @ApiOperation("问题检索")
    public AjaxResult problemRetrieval(@RequestBody ProblemRetrievalVo problemRetrievalVo) {
        Page page = buildPageObject();
        return designBizProblemService.problemRetrieval(page, problemRetrievalVo);
    }

    @PostMapping("problemRetrievalExport")
    @ApiOperation("问题检索导出")
    public void problemRetrievalExport(@RequestBody ProblemRetrievalVo problemRetrievalVo, HttpServletResponse httpServletResponse) {
        designBizProblemService.problemRetrieval(problemRetrievalVo,httpServletResponse);
    }


    @PostMapping("problemTaskRetrieval")
    @ApiOperation("问题任务检索")
    public AjaxResult problemTaskRetrieval(@RequestBody ProblemTaskRetrievalVo problemTaskRetrievalVo) {
        Page page = buildPageObject();
        return designBizProblemTaskNewService.problemTaskRetrieval(page, problemTaskRetrievalVo);
    }

    @PostMapping("problemTaskRetrievalExport")
    @ApiOperation("问题任务检索导出")
    public void problemTaskRetrievalExport(@RequestBody ProblemTaskRetrievalVo problemTaskRetrievalVo,HttpServletResponse httpServletResponse) {
        designBizProblemTaskNewService.problemTaskRetrieval(problemTaskRetrievalVo,httpServletResponse);
    }

    @PostMapping("changeRetrieval")
    @ApiOperation("变更检索")
    public AjaxResult changeRetrieval(@RequestBody ChangeRetrievalVo changeRetrievalVo) {
        Page page = buildPageObject();
        return designBizChangeService.changeRetrieval(page, changeRetrievalVo);
    }

    @PostMapping("changeRetrievalExport")
    @ApiOperation("变更检索导出")
    public void changeRetrieval(@RequestBody ChangeRetrievalVo changeRetrievalVo,HttpServletResponse httpServletResponse) {
       designBizChangeService.changeRetrieval(changeRetrievalVo,httpServletResponse);
    }

    @PostMapping("changeTaskRetrieval")
    @ApiOperation("变更任务检索")
    public AjaxResult changeTaskRetrieval(@RequestBody ChangeTaskRetrievalVo changeRetrievalVo) {
        Page page = buildPageObject();
        return designBizChangetaskService.changeTaskRetrieval(page, changeRetrievalVo);
    }

    @PostMapping("changeTaskRetrievalExport")
    @ApiOperation("变更任务导出")
    public void changeTaskRetrievalExport(@RequestBody ChangeTaskRetrievalVo changeRetrievalVo,HttpServletResponse response) {
        designBizChangetaskService.changeTaskRetrieval(changeRetrievalVo,response);
    }
}
