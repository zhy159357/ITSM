package com.ruoyi.form.controller.customerForm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.CustomerFormListDTO;
import com.ruoyi.form.entity.CompleteParamDto;
import com.ruoyi.form.strategy.CustomerStrategy;
import com.ruoyi.form.vo.DetailsPageVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName CustomerStrategyController
 * @Description 自定义策略控制层
 * @Author JiaQi Zhang
 * @Date 2022/10/2 16:20
 */
@RestController
@RequestMapping("/customerForm")
@Api(tags = "自定义策略")
@RequiredArgsConstructor
public class CustomerStrategyController extends BaseController {


    private final CustomerStrategy customerStrategy;




    @PostMapping("/strategy/list/{code}")
    @ApiOperation("自定义表单数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表单名称",required = true),
            @ApiImplicitParam(name = "customerFormListDTO",value = "表单列表搜索条件",dataType = "CustomerFormListDTO"),
            @ApiImplicitParam(name = "type",value = "类型数据类型：1、全部列表（后续改为只查看自己填写的的）2、代办列表 3、已办列表"),
    })
    public AjaxResult customerList(@PathVariable("code") String code,String type,@RequestBody CustomerFormListDTO customerFormListDTO) {
        Page page = buildPageObject();
        AjaxResult result = customerStrategy.list(code,type, page,customerFormListDTO);
        return result;
    }


    @PostMapping("/strategy/insertOrUpdate")
    @ApiOperation("自定义表单新增/修改数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表单名称",required = true),
            @ApiImplicitParam(name = "customerFormContent",paramType = "body",dataType = "CustomerFormContent",value = "表单内容",required = true)
    })
    public AjaxResult insertOrUpdate(String fromCode, String id, String code, @RequestBody CustomerFormContent customerFormContent) {
        AjaxResult result = customerStrategy.insertOrUpdate(fromCode, id, code ,customerFormContent);
        return result;
    }


    @GetMapping("/customerDetailsPage")
    @ApiOperation("自定义策略->详情页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表名",required = true),
            @ApiImplicitParam(name = "processId",value = "流程实例"),
            @ApiImplicitParam(name = "id",value = "业务主表ID",required = true),
            @ApiImplicitParam(name = "type",value = "类型",required = true),
            @ApiImplicitParam(name = "version",value = "版本号",required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "OK",response = DetailsPageVO.class)
    })
    public AjaxResult getHistoryApproveList(String code, String processId, Integer id, String type, String version){

        return customerStrategy.customerDetailsPage(code,processId,id,type,version);
    }


    @PostMapping("/processComplete")
    @ApiOperation("自定义策略->流程审批")
    public AjaxResult complete(@RequestBody CompleteParamDto completeParamDto){
        return customerStrategy.processComplete(completeParamDto);
    }


}
