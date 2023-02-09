package com.ruoyi.form.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.domain.*;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.service.*;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/customerForm/change")
public class ChangeController extends BaseController {


    @Autowired
    IChangeService changeService;
    @Autowired
    IOgUserService ogUserService;
    @Autowired
    CustomerFormService customerFormService;
    @Autowired
    Base base;
    @Autowired
    IImplRecordService iImplRecordService;
    @Autowired
    IFormApprovalFreeService formApprovalFreeService;
    @Autowired
    IChangeTaskSceneService changeTaskSceneService;
    @Autowired
    TaskService taskService;

    @PostMapping("/start")
    @ApiOperation("变更单启动流程并跳转到审批弹窗")
    public AjaxResult init(@RequestHeader("CurrentUserId") String currentId) {
        //保存
        return changeService.init(null, null, currentId, new HashMap<>(), true);
    }

    @PostMapping("/set/free/approval")
    @ApiOperation("设置免审批")
    public AjaxResult setAdminFreeApproval(/*@RequestHeader("CurrentUserId") String currentId,*/@RequestParam("changeId") String changeId,
                                                                                                @RequestParam("comment") String comment, @RequestParam("flag") String flag) {
        if ("0".equals(flag)) {
            return AjaxResult.success();
        }
        return changeService.setAdminFreeApproval(flag, comment, changeId);
    }

    @PostMapping("/startTask")
    @ApiOperation("变更任务单的新增并启动流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerFormContent", paramType = "body", dataType = "CustomerFormContent", value = "表单内容", required = true)
    })
    public AjaxResult addAndStartTaskProcess(@RequestBody CustomerFormContent customerFormContent) {
        return changeService.saveTaskAndStart(customerFormContent);
    }


    //任务单转派
    @PostMapping("/transfer/task")
    @ApiOperation("旧任务单转派（作废）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerFormContent", paramType = "body", dataType = "CustomerFormContent", value = "表单内容", required = true)
    })
    public AjaxResult transferTask(@RequestParam("taskId") String taskId, @RequestParam("changeTaskNo") String changeTaskNo, @RequestBody CustomerFormContent customerFormContent) {
        return changeService.transferTask(taskId, changeTaskNo, customerFormContent);
    }

    @PostMapping("/transfer/waitimpltask")
    @ApiOperation("新任务单转派")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerFormContent", paramType = "body", dataType = "CustomerFormContent", value = "表单内容", required = true)
    })
    public AjaxResult transferTaskWaitImplTask(@RequestParam("taskId") String taskId, @RequestParam("changeTaskNo") String changeTaskNo, @RequestParam("transferMan") String transferMan, @RequestBody CustomerFormContent customerFormContent) {
        return changeService.tarnsFerTaskWaitImpl(taskId, changeTaskNo, customerFormContent, transferMan);
    }

    //任务单
    @PostMapping("/rob/task")
    @ApiOperation("任务单抢单")
    public AjaxResult robTask(@RequestParam("changeTaskNo") String changTaskNo) {
        return changeService.robTask(changTaskNo);
    }


    @PostMapping("/charge/over/time")
    @ApiOperation("判断是否计划开始时间超过红线时间")
    public AjaxResult chargePlanTime(@RequestParam("changeTaskNo") String changTaskNo, @RequestParam("planStartDate") String planStartDate) {
        return changeService.chargePlanTime(changTaskNo, planStartDate);
    }

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    RepositoryService repositoryService;

    @PostMapping("/delete/process")
    @ApiOperation("删除流程实例")
    public AjaxResult deleteProcess(@RequestHeader("CurrentUserId") String userId, @RequestParam String id) {
        //根据部署id获取所有的流程实例id
        Map<String, Object> param = new HashMap<>();
        param.put("DEPLOYMENT_ID_", "");
        Map<String, String> query = new HashMap<>();
        query.put("ID_", id);
        String sql = "SELECT DEPLOYMENT_ID_ FROM act_re_procdef WHERE ID_=:ID_";
        String deployId = base.selectObject(sql, "act_re_procdef", param, query, String.class);
        if (deployId == null || "".equals(deployId.trim())) {
            return AjaxResult.warn("该流程未部署");
        }
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().deploymentId(deployId).list();
        list.forEach(p -> {
            runtimeService.deleteProcessInstance(p.getId(), "");
        });
        return AjaxResult.success();
    }


    @GetMapping("/task/create")
    @ApiOperation("新增变更任务单页面")
    public AjaxResult addTaskPage(@RequestParam("changeNo") String changeNo, @RequestParam("taskDept") String taskDept) {
        return changeService.initTaskByPage(changeNo, taskDept);
    }

    @PostMapping("/init/task")
    @ApiOperation("发起变更任务")
    public AjaxResult initTask(@RequestParam("changeNo") String changeNo, @RequestParam("taskDept") String taskDept) {
        return changeService.initAndSubmitChangeTask(changeNo, taskDept);
    }

    @PostMapping("/add/basis")
    @ApiOperation("新增或更新变更依据")
    public AjaxResult addBasis(@RequestParam("changeNo") String changeNo, @RequestParam(value = "basis", required = false, defaultValue = "") String basis) {
        return changeService.addBasis(changeNo, basis);
    }


    //任务单
/*    @PostMapping("/remedy/task")
    @ApiOperation("新建补救任务单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "列表任务ID", required = true),
            @ApiImplicitParam(name = "instanceId", value = "流程实例ID", required = true),
            @ApiImplicitParam(name = "customerVo", value = "参数对象", paramType = "body", required = true)
    })
    public AjaxResult remedyTask(String taskId, String instanceId, @RequestBody CustomerVo customerVo) {
        return changeService.remedyTask(taskId, instanceId, customerVo);
    }*/

    @PostMapping("/backCompletion")
    @ApiOperation("变更准备阶段退回补全")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "列表任务ID", required = true),
            @ApiImplicitParam(name = "instanceId", value = "流程实例ID", required = true),
            @ApiImplicitParam(name = "customerVo", value = "参数对象", paramType = "body", required = true)
    })
    public AjaxResult backCompletion(String taskId, String instanceId, @RequestBody CustomerVo customerVo) {
        return changeService.backCompletion(taskId, instanceId, customerVo);
    }

    @GetMapping("/changeTaskList")
    @ApiOperation("变更任务单列表")
    public AjaxResult changeTaskList(@RequestParam("changeNo") String changeNo) {
        return changeService.changeTaskList(changeNo);
    }

    @GetMapping("/taskView")
    @ApiOperation("展示单个任务单页面")
    public AjaxResult viewOrComplete(/*@RequestParam("id") Integer id, */@RequestParam("changeTaskId") String changeTaskId) {
        //如果当前登录人是变更经理，则改为出现审批页面
        //String code = "changeTask";
        /*String changeMangerId = base.getChangeMangerUserId();
        if(CustomerBizInterceptor.currentUserThread.get().getUserId().equals(changeMangerId)){
            return customerFormService.approvalPopUp(code,processId,String.valueOf(changeService.getCurrentVersion(ChangeTableNameEnum.CHANGE_TASK.getName())));
        }else {*/
        return changeService.viewTaskPage(changeTaskId);
        /*}*/
    }

    @GetMapping("/view/page")
    @ApiOperation("展示变更单页面")
    public AjaxResult viewChangePage(@RequestParam("changeNo") String changeNo) {
        return changeService.viewChangePage(changeNo);
    }

    @GetMapping("/view/table/page")
    @ApiOperation("任务场景表单页面")
    public AjaxResult viewTablePage(@RequestParam("code") String code, @RequestParam("changeTaskNo") String changeTaskNo) {
        return changeService.viewTaskSceneTablePageAndData(code, changeTaskNo);
    }

    @GetMapping("/view/auto/table/page")
    @ApiOperation("自动任务场景表单页面")
    public AjaxResult viewAutoTablePage(@RequestParam("code") String code, @RequestParam("changeTaskNo") String changeTaskNo) {
        ChangeTaskScene changeTaskScene = changeTaskSceneService.getChangeTaskSceneByCode(code);
        if (changeTaskScene == null) {
            return AjaxResult.warn("无此场景表单");
        }
        String sceneName = changeTaskScene.getName();
        String orgId = changeTaskScene.getOrgid();
        ChangeTaskScene scene = changeTaskSceneService.getAutoChangeTaskScene(orgId, sceneName);
        if (scene == null) {
            return AjaxResult.warn("无自动化场景表单");
        }
        String autoCode = scene.getCode();
        return changeService.viewTaskSceneTablePageAndData(autoCode, changeTaskNo);
    }

    @PostMapping("/task/back/preApproval")
    @ApiOperation("设置任务是否需要退回预审")
    public AjaxResult backToPreApproval(@RequestParam("changeTaskNo") String changeTaskNo, @RequestParam("flag") Integer flag) {
        String changeMangerId = base.getChangeMangerUserId();
        String deputyUserId = base.getVaildDeputyUserId(changeMangerId);
        String currentUserId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        if (currentUserId == null) {
            return AjaxResult.warn("当前登录人不可为null");
        }
        if (currentUserId.equals(changeMangerId) || currentUserId.equals(deputyUserId)) {
            return changeService.setBackToPreApprovalFlag(changeTaskNo, flag);
        } else {
            return AjaxResult.warn("当前登录人非变更经理或变更经理代理人，不允许操作");
        }
    }

    @PostMapping("/task/back/start")
    @ApiOperation("设置任务是否需要退回发起（已注册状态）")
    public AjaxResult backToStart(@RequestParam("changeTaskNo") String changeTaskNo, @RequestParam("flag") Integer flag) {
        String changeMangerId = base.getChangeMangerUserId();
        String deputyUserId = base.getVaildDeputyUserId(changeMangerId);
        String currentUserId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        if (currentUserId == null) {
            return AjaxResult.warn("当前登录人不可为null");
        }
        if (currentUserId.equals(changeMangerId) || currentUserId.equals(deputyUserId)) {
            return changeService.setBackToStart(changeTaskNo, flag);
        } else {
            return AjaxResult.warn("当前登录人非变更经理或变更经理代理人，不允许操作");
        }
    }

 /*   @PostMapping("/create")
    @ApiOperation("给其他模块启动变更单用")
    public AjaxResult otherModelCreateChange(@RequestHeader("CurrentUserId") String currentId, @RequestParam("basisType") String basisType, @RequestBody String basis) {
        ChangeBasisTypeEnum type = ChangeBasisTypeEnum.valueOf(basisType);
        JSONObject object = JSONObject.parseObject(basis);
        return changeService.init(currentId,new HashMap<>(), false);
    }*/

    @PostMapping("/auto/start")
    @ApiOperation("自动实施任务启动")
    public AjaxResult autoStartDevTask(@RequestHeader("CurrentUserId") String CurrentUserId, @RequestParam("changeTaskNo") String changeTaskNo) {
        return changeService.autoStartDevTaskNew(changeTaskNo);
        //return changeService.autoStartDevTask(changeTaskNo);
    }

    @PostMapping("/auto/task/status")
    @ApiOperation("获取自动化任务状态")
    public AjaxResult getAutoStartStatus(@RequestHeader("CurrentUserId") String CurrentUserId, @RequestParam("changeTaskNo") String changeTaskNo) {
        return changeService.getAutoStatusNew(changeTaskNo);
        //return changeService.getAutoStatus(changeTaskNo);
    }


    @PostMapping("/cancel")
    @ApiOperation("撤销变更流程以及关联的任务单流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "变更单主键", paramType = "query", required = true)
    })
    public AjaxResult cancelProcessByKey(String id) {
        return changeService.cancelChangeProcess(id);
    }

    @GetMapping("/cancel/charge")
    @ApiOperation("撤销变更流程以及关联的任务单流程之前判断下是否有并包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "变更单主键", paramType = "query", required = true)
    })
    public AjaxResult cancelProcessChargeByKey(String id) {
        return changeService.getAllMergeTaskInfoList(id);
    }

    @PostMapping("/task/cancel")
    @ApiOperation("撤销变更任务单流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "changeTaskNo", value = "变更任务单单号", paramType = "query", required = true)
    })
    public AjaxResult cancelProcessByTaskKey(String changeTaskNo) {
        return changeService.cancelChangeTaskProcess(changeTaskNo);
    }


    @GetMapping("/task/cancel/charge")
    @ApiOperation("撤销变更任务单流程之前检查下是否有并包任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "changeTaskNo", value = "变更任务单单号", paramType = "query", required = true)
    })
    public AjaxResult cancelProcessChargeByTaskKey(String changeTaskNo) {
        return changeService.getAllMergeTaskInfoListByTaskNo(changeTaskNo);
    }

    @PostMapping("/adjust/risklevel")
    @ApiOperation("调整风险级别")
    public AjaxResult adjustRiskLevel(@RequestBody RiskAccessRecord riskAccessRecord) {
        return changeService.adjustRiskLevel(riskAccessRecord);
    }

    @PostMapping("/scene/table/data/save")
    @ApiOperation("保存场景表单数据")
    public AjaxResult addOrUpdate(@RequestParam("code") String code, @RequestBody Map<String, Object> fields, @RequestParam(value = "autoFlag", defaultValue = "0") String autoFlag) {
        if ("1".equals(autoFlag)) {
            ChangeTaskScene changeTaskScene = changeTaskSceneService.getChangeTaskSceneByCode(code);
            String sceneName = changeTaskScene.getName();
            String orgId = changeTaskScene.getOrgid();
            ChangeTaskScene scene = changeTaskSceneService.getAutoChangeTaskScene(orgId, sceneName);
            code = scene.getCode();
            return changeService.addOrUpdateSceneData(code, fields);
        } else if ("0".equals(autoFlag)) {
            return changeService.addOrUpdateSceneData(code, fields);
        } else {
            return AjaxResult.warn("请传入正确的标识");
        }
    }

    @PostMapping("/scene/table/data/del")
    @ApiOperation("根据id删除一条场景表单数据（暂不使用）")
    public AjaxResult delScene(@RequestParam("code") String code, @RequestParam("id") String id, @RequestParam(value = "autoFlag", defaultValue = "0") String autoFlag) {
        if ("1".equals(autoFlag)) {
            ChangeTaskScene changeTaskScene = changeTaskSceneService.getChangeTaskSceneByCode(code);
            String sceneName = changeTaskScene.getName();
            String orgId = changeTaskScene.getOrgid();
            ChangeTaskScene scene = changeTaskSceneService.getAutoChangeTaskScene(orgId, sceneName);
            code = scene.getCode();
            return changeService.delSceneDataById(code, id);
        } else if ("0".equals(autoFlag)) {
            return changeService.delSceneDataById(code, id);
        } else {
            return AjaxResult.warn("请传入正确的标识");
        }
    }

    @PostMapping("/scene/table/data/sync")
    @ApiOperation("同步场景表单数据")
    public AjaxResult syncSceneToAuto(@RequestParam("code") String code, @RequestParam("changeTaskNo") String changeTaskNo) {
        return changeService.syncSceneDataToAuto(code, changeTaskNo);
    }

    @GetMapping("/arrange/tasklist")
    @ApiOperation("排班模块获取任务列表数据")
    public AjaxResult taskListByStatus(@RequestParam("status") String status) {
        String currentUserId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        ChangeTaskStatusEnum changeTaskStatusEnum = ChangeTaskStatusEnum.valueOf(status);
        List<Map<String, Object>> list = iImplRecordService.getChangeTaskListByStatusAndCurrentUserId(currentUserId, changeTaskStatusEnum);
        return AjaxResult.success(list);
    }

    @GetMapping("/arrange/approvalPage")
    @ApiOperation("排班模块获取跳转标识")
    public AjaxResult arrangePage(@RequestParam("changeTaskNo") String changeTaskNo) {
        List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
        Map<String, Object> map = list.get(0);
        String status = map.get("status").toString();
        Map<String, Object> result = new HashMap<>();
        if (ChangeTaskStatusEnum.waitImpl.getName().equals(status) || ChangeTaskStatusEnum.impling.getName().equals(status)) {
            /*String instanceId = map.get("instance_id").toString();
            Integer id = Integer.valueOf(map.get("id").toString());
            String version = changeService.getChangeTaskVersion();
            return customerFormService.approvalPopUp("changeTask",instanceId,id,"2",version);*/
            result.put("flag", 1);
        } else {
            result.put("flag", 2);
            //return AjaxResult.warn("当前状态不允许跳转");
        }
        return AjaxResult.success(result);
    }

    @GetMapping("/charge/current/approval")
    @ApiOperation("判断当前登陆人是否是当前处理人")
    public AjaxResult currentApprovalFlag(@RequestParam("id") String id, @RequestParam("code") String code) {
        String approval = "";
        String instanceId = "";
        if ("change".equals(code)) {
            approval = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.APPROVAL, ChangeFieldEnum.ID, id);
            instanceId = base.getChangeInstanceIdByChangeId(id);
        } else if ("changeTask".equals(code)) {
            approval = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.APPROVAL, ChangeTaskFieldEnum.ID, id);
            instanceId = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.INSTANCE_ID, ChangeTaskFieldEnum.ID, id);
        } else {
            return AjaxResult.warn("请传入正确的code");
        }
        AjaxResult success = AjaxResult.success();
        if(approval==null||"".equals(approval.trim())||instanceId==null||"".equals(instanceId.trim())){
            success.put("type", 3);
            return success;
        }
        String currentUserId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        String taskId = "";
        List<String> approvalList = Arrays.asList(approval.split(","));
        if (approvalList.contains(currentUserId)) {
            Task task = taskService.createTaskQuery().processInstanceId(instanceId).taskCandidateOrAssigned(currentUserId).singleResult();
            if (task == null) {
                success.put("type", 3);
            } else {
                taskId = task.getId();
                success.put("type", 2);
            }
        } else {
            success.put("type", 3);
        }
        success.put("taskId", taskId);
        return success;
    }
}
