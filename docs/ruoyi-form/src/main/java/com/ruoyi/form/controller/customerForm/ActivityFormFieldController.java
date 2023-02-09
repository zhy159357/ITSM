package com.ruoyi.form.controller.customerForm;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.form.domain.ButtonAction;
import com.ruoyi.form.domain.FieldActivityNode;
import com.ruoyi.form.domain.FormStatusActivityNode;
import com.ruoyi.form.domain.FormStepStatus;
import com.ruoyi.form.service.ButtonActionService;
import com.ruoyi.form.service.FieldActivityNodeService;
import com.ruoyi.system.mapper.PubParaValueMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ActivityFormFieldController
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/7/25 10:58
 */
@RestController
@RequestMapping("/customerForm")
@Api(tags = "自定义表单和流程节点关联模块")
@CrossOrigin
public class ActivityFormFieldController extends BaseController {

    @Resource
    FieldActivityNodeService fieldActivityNodeService;

    @Resource
    PubParaValueMapper pubParaValueMapper;

    @Resource
    ButtonActionService buttonActionService;

    @GetMapping("/getFormField")
    @ApiOperation("获取最新版本表单字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "表单code", required = true),
            @ApiImplicitParam(name = "sid", value = "节点流程ID", required = true),
    })
    public AjaxResult getFormField(String code, String sid) {
        AjaxResult formField = fieldActivityNodeService.getFormField(code, sid);
        return formField;
    }


    @PostMapping("/setFormFieldShowStatus")
    @ApiOperation("设置表单字段在节点的显示状态")
    @ApiImplicitParam(name = "fieldActivityNodes", value = "字段节点显示状态集合", allowMultiple = true, paramType = "body", dataType = "FieldActivityNode")
    public AjaxResult setFormFieldShowStatus(@RequestBody List<FieldActivityNode> fieldActivityNodes) {
        AjaxResult result = fieldActivityNodeService.setFormFieldShowStatus(fieldActivityNodes);
        return result;
    }

    @PostMapping("/setFormStatus")
    @ApiOperation("设置表单在每个节点的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "表单code", required = true),
            @ApiImplicitParam(name = "formStatusActivityNodes", value = "节点和状态关联集合", allowMultiple = true, paramType = "body", dataType = "FormStatusActivityNode")
    })
    public AjaxResult setFormStatus(String code, @RequestBody List<FormStatusActivityNode> formStatusActivityNodes) {
        AjaxResult result = fieldActivityNodeService.setFormStatus(code, formStatusActivityNodes);
        return result;
    }

    @GetMapping("/getFormStatusOrStep")
    @ApiOperation("获得表单在每个节点的状态或者步骤码值")
    @ApiImplicitParam(name = "paramName", value = "参数类型名称", required = true)
    public AjaxResult getFormStatus(String paramName) {
        List<PubParaValue> pubParaValues = pubParaValueMapper.selectPubParaValueByParaName(paramName);
        return AjaxResult.success(pubParaValues);
    }

    @PostMapping("/setFormStep")
    @ApiOperation("设置表单状态的步骤")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "表单code", required = true),
            @ApiImplicitParam(name = "formStepStatuses", value = "表单步骤和状态关联", allowMultiple = true, paramType = "body", dataType = "FormStepStatus")
    })
    public AjaxResult setFormStep(String code, @RequestBody List<FormStepStatus> formStepStatuses) {
        AjaxResult result = fieldActivityNodeService.setFormStep(code, formStepStatuses);
        return result;
    }

    @PostMapping("/getCurrentNodeFormStep")
    @ApiOperation("获取表单状态的步骤")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "表单code", required = true),
            @ApiImplicitParam(name = "bizStatusName", value = "当前节点业务状态名", required = true)
    })
    public AjaxResult getCurrentNodeFormStep(String code, String bizStatusName) {
        AjaxResult result = fieldActivityNodeService.getCurrentNodeFormStep(code, bizStatusName);
        return result;
    }

    @PostMapping("/getCurrentNodeFormBizStatus")
    @ApiOperation("获取表单当前节点状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "表单code", required = true),
            @ApiImplicitParam(name = "nodeId", value = "当前节点sid", required = true)
    })
    public AjaxResult getCurrentNodeFormBizStatus(String code, String nodeId) {
        AjaxResult result = fieldActivityNodeService.getCurrentNodeFormBizStatus(code, nodeId);
        return result;
    }

    @GetMapping("/getApproveTemplate")
    @ApiOperation("获取审批模板列表")
    public AjaxResult getApproveTemplate() {
        AjaxResult approveTemplate = fieldActivityNodeService.getApproveTemplate();
        return approveTemplate;
    }


    @PostMapping("/setButtonAction")
    @ApiOperation("设置按钮动作")
    public AjaxResult setButtonAction(ButtonAction buttonAction) {
        buttonActionService.saveOrUpdate(buttonAction,Wrappers.<ButtonAction>update().eq(ButtonAction.COL_ACTIVITY_NODE_ID,buttonAction.getActivityNodeId()));
        return AjaxResult.success();
    }


    @PostMapping("/getButtonAction")
    @ApiOperation("获取按钮动作")
    @ApiImplicitParam(name = "sid",required = true)
    public AjaxResult getButtonAction(String sid) {
        ButtonAction one = buttonActionService.getOne(Wrappers.<ButtonAction>query().eq(ButtonAction.COL_ACTIVITY_NODE_ID, sid));
        return AjaxResult.success(one);
    }


    @GetMapping("/synchronize")
    @ApiOperation("配置同步")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "表单code",required = true),
            @ApiImplicitParam(name = "type",value = "类型",required = true)
    })
    public AjaxResult synchronizeFlag(String code,String type){
        AjaxResult synchronize = fieldActivityNodeService.synchronize(code, type);
        return synchronize;
    }


}
