package com.ruoyi.form.aspectj;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.ChangeUtil;
import com.ruoyi.form.activiti.listener.ChangeNodeCompleteListener;
import com.ruoyi.form.domain.*;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.service.*;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgDeputyCfgService;
import com.ruoyi.system.service.IOgPersonService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName
 * @Description TODO
 * @Author zzz
 * @Date
 */
@Aspect
@Component
@Slf4j
public class CompleteAspectj extends Base {

    @Autowired
    TaskService taskService;
    @Autowired
    IChangeDeptService changeDeptService;
    @Autowired
    IOgPersonService ogPersonService;
    @Autowired
    IChangeTaskOrgService changeTaskOrgService;
    @Autowired
    HistoryService historyService;
    @Autowired
    IRiskAssessmentService riskAssessmentService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    IDesignBizShieldWarnService designBizShieldWarnService;
    @Autowired
    ForeignService foreignService;
    @Autowired
    IOgDeputyCfgService ogDeputyCfgService;
    @Autowired
    IChangeService changeService;
    @Autowired
    IChangeTaskSceneService changeTaskSceneService;

    @Pointcut("execution(public * com.ruoyi.form.service.impl.CustomerFormServiceImpl.complete(..))")
    public void buttonPointCut() {
    }

    @Around("buttonPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] param = joinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder("???????????????????????????");
        Arrays.stream(param).forEach(a -> {
            stringBuilder.append(a + ",");
        });
        log.info("???????????????????????????" + stringBuilder.toString());
        if (param.length == 5) {
            String code = param[0].toString();
            CustomerVo customerVo = (CustomerVo) param[4];
            String instanceId = param[2].toString();
            String taskDefinitionKey = "";
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).list();
            if (!taskList.isEmpty()) {
                Task task = taskList.get(0);
                taskDefinitionKey = task.getTaskDefinitionKey();
            }
            if (CHANGE.equals(code)) {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
                if (processInstance.isSuspended()) {
                    return AjaxResult.warn("??????????????????");
                }
                String changeId = getChangeColumnValueBySingleCondition(ChangeFieldEnum.ID, ChangeFieldEnum.INSTANCE_ID, instanceId);
                String status = getChangeColumnValueBySingleCondition(ChangeFieldEnum.STATUS, ChangeFieldEnum.ID, changeId);
                Object type = customerVo.getCustomerFormContent().getFields().get(ChangeFieldEnum.TYPE.getName());
                Object riskLevel = customerVo.getCustomerFormContent().getFields().get(ChangeFieldEnum.CURRENT_RISK_LEVEL.getName());
                Object reason = customerVo.getCustomerFormContent().getFields().get(ChangeFieldEnum.REASON.getName());
                Object urgentReason = customerVo.getCustomerFormContent().getFields().get(ChangeFieldEnum.URGENT_REASON.getName());
                Object changeLevel = customerVo.getCustomerFormContent().getFields().get(ChangeFieldEnum.CHANGE_LEVLE.getName());
                if (ChangeDefineKeyEnum.changeMangerApprovalWhenBranch.getName().equals(taskDefinitionKey)) {
                    Map<String, Object> variables = customerVo.getVariables();
                    Object recode = variables.get("recode");
                    if ("1".equals(recode.toString().trim())) {
                        Object o = variables.get(ADMIN_ORG_LIST);
                        if (o == null) {
                            return AjaxResult.warn("?????????????????????");
                        }
                        if (!(o instanceof List)) {
                            return AjaxResult.warn("???????????????");
                        }
                        List list = (List) o;
                        if (list.isEmpty()) {
                            return AjaxResult.warn("?????????????????????");
                        }
                    }
                }
                String branchFlag = getChangeColumnValueBySingleCondition(ChangeFieldEnum.BRANCH_FLAG, ChangeFieldEnum.INSTANCE_ID, instanceId);
                if (ChangeStatusEnum.unSubmit.getName().equals(status)) {
                    //????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    String changeNo = getChangeColumnValueBySingleCondition(ChangeFieldEnum.EXTRA1, ChangeFieldEnum.ID, changeId);
                    List<String> sysList = getSysListByChangeNo(changeNo);
                    sysList = sysList.stream().distinct().collect(Collectors.toList());
                    RiskAssessment result = riskAssessmentService.selectRiskAssessmentById(changeNo);
                    if (result == null) {
                        return AjaxResult.warn("????????????????????????");
                    }
                    String referSysStr = result.getReferSys();
                    String[] referArray = referSysStr.split(",");
                    List<String> sys = Arrays.stream(referArray).distinct().collect(Collectors.toList());
                    if (!sysList.isEmpty() && !sys.containsAll(sysList)) {
                        return AjaxResult.warn("?????????????????????????????????????????????????????????????????????????????????????????????");
                    }
                    if (getTaskCountByChangeId(changeId) == 0) {
                        return AjaxResult.warn("???????????????????????????????????????????????????????????????");
                    } else if (/*getTaskCountByChangeIdAndStatus(changeId, "?????????") != 0 ||*/ getTaskCount(changeId, ChangeTaskStatusEnum.registered, 0) != 0) {
                        return AjaxResult.warn("??????????????????????????????");
                    } else if (reason == null || type == null ||
                            "".equals(type.toString().trim()) || "".equals(reason.toString().trim())) {
                        return AjaxResult.warn("?????????????????????");
                    } else if (riskLevel == null || "".equals(riskLevel.toString().trim())) {
                        return AjaxResult.warn("????????????????????????");
                    } else if ("2".equals(type.toString())) {
                        if (urgentReason == null || "".equals(urgentReason.toString().trim())) {
                            return AjaxResult.warn("???????????????????????????");
                        }
                        String orgId = getChangeTableStarterOrgId(changeId);
                        if (changeDeptService.isDeptAndSystem(orgId)) {
                            ChangeDeptEntity changeDeptEntity = changeDeptService.selectByDeptId(orgId);
                            Integer overSize = Integer.parseInt(changeDeptEntity.getOverSize());
                            if (overSize <= 0) {
                                return AjaxResult.warn("??????????????????????????????");
                            }
                        }
                    } else if ("1".equals(type.toString()) && !"1".equals(riskLevel.toString().trim())) {
                        return AjaxResult.warn("??????????????????????????????????????????????????????????????????????????????");
                    }
                    if ("1".equals(branchFlag)) {
                        //?????????????????????????????????????????????????????????
                        if (changeLevel == null || "".equals(changeLevel.toString().trim())) {
                            return AjaxResult.warn("???????????????????????????");
                        }
                    }
                   /* String submitDate = getChangeColumnValueBySingleCondition(ChangeFieldEnum.SUBMIT_DATE, ChangeFieldEnum.INSTANCE_ID, instanceId);
                    if (submitDate != null && !"".equals(submitDate)) {
                        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        String oldType = getChangeColumnValueBySingleCondition(ChangeFieldEnum.TYPE, ChangeFieldEnum.INSTANCE_ID, instanceId);
                        if (!type.toString().equals(oldType)) {
                            return AjaxResult.warn("????????????????????????????????????????????????");
                        }
                    }*/
                }

                if (ChangeDefineKeyEnum.changeMangerApproval.getName().equals(taskDefinitionKey)) {
                    Map<String, Object> variables = ((CustomerVo) param[4]).getVariables();
                    Integer recode = Integer.valueOf(variables.get(RECODE).toString());
                    if (recode == 3) {
                        //????????????????????????????????????????????????
                        Integer count = getTaskCountByChangeIdAndTaskColumn(changeId, ChangeTaskFieldEnum.BACK_TO_APPROVAL_FLAG, "1");
                        if (count == 0) {
                            return AjaxResult.warn("???????????????????????????????????????");
                        }
                    }
                }
                //?????????????????????????????????????????????????????????????????????????????????????????????
                if (ChangeDefineKeyEnum.adminApproval.getName().equals(taskDefinitionKey)) {
                    Object recodeObj = customerVo.getVariables().get(RECODE);
                    Integer recode = Integer.parseInt(recodeObj.toString());
                    List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).list();
                    String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();
                    if (recode == 1) {
                        List<String> deputyList = getAdminDeputyIdList(userId);
                        deputyList.add(userId);
                        List<String> users = new ArrayList<>();
                        list.forEach(p -> {
                            String current = p.getAssignee();
                            if (!deputyList.contains(current)) {
                                users.add(current);
                            }else if(!userId.equals(current)){
                                taskService.complete(p.getId());
                            }
                        });
                        String userList = updateAdminCurrentProcessor(users, changeId);
                        customerVo.getCustomerFormContent().getFields().put(ChangeFieldEnum.CURRENT_PROCESSOR.getName(), userList);
                    }
                }
            } else if (CHANGE_TASK.equals(code)) {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
                if (processInstance.isSuspended()) {
                    return AjaxResult.warn("????????????????????????");
                }
                String changeTaskNo = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.EXTRA1, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                String status = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.STATUS, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                Map<String, Object> variables = ((CustomerVo) param[4]).getVariables();
                String remedyFlagStr = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.REMEDY_FLAG, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                String type = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.TYPE.getName()).toString();
                Integer remedy = Integer.parseInt(remedyFlagStr);
                if (remedy == 0) {
                    //????????????????????????????????????????????????????????????????????????
                    Object assignee = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.ASSIGNEE.getName());
                    Object preApproval = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PRE_APPROVAL.getName());
                    if (assignee.toString().trim().equals(preApproval.toString().trim())) {
                        return AjaxResult.warn("??????????????????????????????????????????");
                    }
                }
                Object deployWay = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.DEPLOY_WAY.getName());
                if (ChangeTaskStatusEnum.registered.getName().equals(status)) {
                    //????????????
                    //???????????????????????????????????????????????????
                    Object referSys = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.REFER_SYS.getName());
                    String fundamental = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName()).toString();
                    if (deployWay == null || "".equals(deployWay.toString().trim())) {
                        return AjaxResult.warn("????????????????????????");
                    }
                    Object taskDept = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.TASKDEPT.getName());
                    ChangeTaskOrg changeTaskOrg = changeTaskOrgService.getByOrgName("???????????????");
                    if (changeTaskOrg.getOrgid().equals(taskDept) && (referSys == null || "".equals(referSys.toString().trim()))) {
                        return AjaxResult.warn("???????????????????????????????????????");
                    }
                    String refer = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.REFER_FLAG.getName()).toString();
                    if ("1".equals(refer) && (referSys == null || "".equals(referSys.toString().trim()))) {
                        return AjaxResult.warn("????????????????????????");
                    }
                    if ("2".equals(refer)) {
                        if ("".equals(fundamental.trim())) {
                            return AjaxResult.warn("????????????????????????");
                        }
                        if ("[]".equals(fundamental.trim())) {
                            return AjaxResult.warn("????????????????????????");
                        }
                    }
                    //TODO ??????????????????????????????
                   /* Page<Map<String, Object>> page = new Page<>();
                    Page<Map<String, Object>> resultPage = changeService.getAllFieldValuePageBySingleCondition(type, "changeTaskNo", changeTaskNo, page);
                    if (resultPage != null&&resultPage.getTotal()==0) {
                        return AjaxResult.warn("??????????????????");
                    }*/
                }
                Object mergeFlag = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.MERGE_FLAG.getName());
                Object preTaskFlag = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PRE_TASK_FLAG.getName());
                if (mergeFlag != null) {
                    Object mergeTaskNo = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.MERGE_TASK_NO.getName());
                    AjaxResult ajaxResult = checkMergeNo(Integer.parseInt(mergeFlag.toString()), mergeTaskNo, changeTaskNo);
                    if (Integer.parseInt(ajaxResult.get(AjaxResult.CODE_TAG).toString()) != AjaxResult.Type.SUCCESS.value()) {
                        return ajaxResult;
                    }
                }
                if (preTaskFlag != null && "1".equals(preTaskFlag.toString())) {
                    Object preTaskNo = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PRE_TASK_NO.getName());
                    if (preTaskNo == null || "".equals(preTaskNo.toString())) {
                        //TODO ??????????????????????????????
                        return AjaxResult.warn("???????????????????????????");
                    }
                }
                if (ChangeTaskDefineKeyEnum.remedySubmit.getName().equals(taskDefinitionKey)) {
                    //??????????????????????????????
                    String taskId = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ID, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                    String leader = getTaskAssigneeGroupLeaderUserId(taskId);
                    if (leader == null) {
                        return AjaxResult.warn("??????????????????????????????");
                    }
                }
                //TODO ??????????????????????????????
                /*if(ChangeTaskStatusEnum.preApprovaling.getName().equals(status)){
                    Page<Map<String, Object>> page = new Page<>();
                    Page<Map<String, Object>> resultPage = changeService.getAllFieldValuePageBySingleCondition(type, "changeTaskNo", changeTaskNo, page);
                    if (resultPage != null&&resultPage.getTotal()==0) {
                        return AjaxResult.warn("??????????????????");
                    }
                }*/
               /* if((ChangeTaskDefineKeyEnum.taskPreApproval.getName().equals(taskDefinitionKey)||ChangeTaskDefineKeyEnum.remedyApproval.getName().equals(taskDefinitionKey))
                    &&variables.get(RECODE)!=null&&"1".equals(variables.get(RECODE).toString())){
                    String deployWayStr = deployWay.toString();
                    if(!"1".equals(deployWayStr)){
                        //????????????????????? ????????????????????????????????????
                        ChangeTaskScene changeTaskScene = changeTaskSceneService.getChangeTaskSceneByCode(type);
                        String sceneName = changeTaskScene.getName();
                        String orgId = changeTaskScene.getOrgid();
                        ChangeTaskScene scene = changeTaskSceneService.getAutoChangeTaskScene(orgId, sceneName);
                        if(scene==null){
                            return AjaxResult.warn("??????????????????");
                        }
                        String autoType = scene.getCode();
                        Page<Map<String, Object>> page = new Page<>();
                        Page<Map<String, Object>> resultPage = changeService.getAllFieldValuePageBySingleCondition(autoType, "changeTaskNo", changeTaskNo, page);
                        if (resultPage != null&&resultPage.getTotal()==0) {
                            return AjaxResult.warn("??????????????????");
                        }
                    }
                }*/
                if (ChangeTaskStatusEnum.waitImpl.getName().equals(status) || (ChangeTaskStatusEnum.preApprovaling.getName().equals(status)&&variables.get(RECODE)!=null&&"1".equals(variables.get(RECODE).toString()))) {
                    Object planStartDate = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PLAN_START_DATE.getName());
                    Object planOverDate = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PLAN_OVER_DATE.getName());
                    if (planStartDate == null || "".equals(planStartDate.toString()) || planOverDate == null || "".equals(planOverDate.toString())) {
                        return AjaxResult.warn("????????????????????????");
                    }
                    String planStartDateStr = planStartDate.toString();
                    String planOverDateStr = planOverDate.toString();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime planStartDateTime = LocalDateTime.parse(planStartDateStr, dateTimeFormatter);
                    LocalDateTime planOverDateTime = LocalDateTime.parse(planOverDateStr, dateTimeFormatter);
                    if (!planStartDateTime.isBefore(planOverDateTime)) {
                        return AjaxResult.warn("???????????????????????????????????????????????????");
                    }
                    LocalDateTime planOverDateTimeLine = planStartDateTime.plus(24, ChronoUnit.HOURS);
                    if (planOverDateTime.isAfter(planOverDateTimeLine)) {
                        return AjaxResult.warn("???????????????????????????????????????????????????24??????????????????????????????????????????");
                    }
                }
                if (ChangeTaskStatusEnum.waitImpl.getName().equals(status)) {
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    /*LocalDateTime now = LocalDateTime.now();
                    String planStartDate = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PLAN_START_DATE.getName()).toString();
                    String planOverDate = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PLAN_OVER_DATE.getName()).toString();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime planStartDateTime = LocalDateTime.parse(planStartDate, dateTimeFormatter);
                    LocalDateTime planOverDateTime = LocalDateTime.parse(planOverDate, dateTimeFormatter);
                    if (now.isBefore(planStartDateTime) || now.isAfter(planOverDateTime)) {
                        return AjaxResult.warn("??????????????????????????????????????????????????????????????????");
                    }*/
                    //?????????????????????????????????
                    List<DesignBizShieldWarn> dataList = designBizShieldWarnService.selectDesignBizShieldWarnByChangeTaskNo(changeTaskNo);
                    if (!dataList.isEmpty()) {
                        Map<String, Object> condition = parseToCondition(dataList);
                        System.out.println(JSON.toJSONString(condition));
                        ChangeUtil.ALARM_POOL.submit(changeTaskNo, () -> {
                            String name = "????????????????????????" + "???" + changeTaskNo + "???";
                            String timeType = "perm";
                            Map<String, Object> data = new HashMap<>();
                            data.put("id", System.currentTimeMillis());
                            data.put("name", name);
                            data.put("active", true);
                            data.put("timeType", timeType);
                            data.put("changeId", changeTaskNo);
                            data.put("condition", condition);
                            AjaxResult ajaxResult = foreignService.maintain(data);
                            System.out.println(JSON.toJSONString(ajaxResult));
                        });
                    }
                }
                if (ChangeTaskStatusEnum.impling.getName().equals(status)) {
                    Object recodeObj = variables.get(RECODE);
                    if (recodeObj != null) {
                        Integer recode = Integer.valueOf(recodeObj.toString());
                        if (recode == 2) {
                            //???????????????????????????????????????????????????
                            String remedyFlag = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.REMEDY_FLAG, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                            Integer flag = Integer.parseInt(remedyFlag);
                            if (flag == 1) {
                                return AjaxResult.warn("?????????????????????????????????");
                            }
                            Object implResult = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.IMPL_RESULT.getName());
                            if (implResult == null || !"3".equals(implResult.toString())) {
                                return AjaxResult.warn("??????????????????????????????");
                            }
                            List<String> remedyReason = (List<String>) customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.REMEDY_REASON.getName());
                            remedyReason = remedyReason.stream().filter(p -> !"".equals(p.trim())).collect(Collectors.toList());
                            if (remedyReason.isEmpty()) {
                                return AjaxResult.warn("?????????????????????");
                            }
                            Map<String, Object> old = customerVo.getCustomerFormContent().getFields();
                            Map<String, Object> map = createRemedy(old, instanceId);
                            customerVo.getCustomerFormContent().getFields().putAll(map);
                        } else if (recode == 1) {
                            //??????????????????????????????????????????
                            Object checkMan = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.CHECK_MAN.getName());
                            Object result = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.IMPL_RESULT.getName());
                            if (checkMan == null || "".equals(checkMan.toString().trim())) {
                                return AjaxResult.warn("??????????????????");
                            }
                            if (result == null || "".equals(result.toString().trim())) {
                                return AjaxResult.warn("?????????????????????");
                            }
                        }
                        ChangeUtil.ALARM_POOL.submit(changeTaskNo, () -> {
                            AjaxResult ajaxResult = foreignService.updateActive(changeTaskNo, false);
                            log.info(JSON.toJSONString(ajaxResult));
                        });
                        ChangeUtil.ALARM_POOL.shutDown(changeTaskNo);
                    }
                }
            }
        }
        Object proceed = joinPoint.proceed();
        if (param.length == 5) {
            String code = param[0].toString();
            String instanceId = param[2].toString();
            CustomerVo customerVo = (CustomerVo) param[4];
            String taskDefinitionKey = "";
            List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).list();
            if (!list.isEmpty()) {
                taskDefinitionKey = list.get(0).getTaskDefinitionKey();
            }
            if (CHANGE.equals(code)) {
                //?????????????????????,??????????????????????????????????????????
                String changeId = getChangeColumnValueBySingleCondition(ChangeFieldEnum.ID, ChangeFieldEnum.INSTANCE_ID, instanceId);
                String status = getChangeColumnValueBySingleCondition(ChangeFieldEnum.STATUS, ChangeFieldEnum.ID, changeId);
                if (ChangeStatusEnum.preApproval.getName().equals(status) && taskAllPassed(changeId, ChangeTaskStatusEnum.preApprovalPassed)) {
                    activeChangeSecPartByInstanceId(instanceId);
                }
                if (ChangeDefineKeyEnum.adminApproval.getName().equals(taskDefinitionKey)) {
                    Object recodeObj = customerVo.getVariables().get(RECODE);
                    Integer recode = Integer.parseInt(recodeObj.toString());
                    if (recode == 0) {
                        list.forEach(p -> {
                            String taskId = p.getId();
                            taskService.complete(taskId);
                        });
                        ChangeNodeCompleteListener.map.put(changeId, 0);
                    }
                }
            } else if (CHANGE_TASK.equalsIgnoreCase(code)) {
                /*??????????????????????????????????????????????????????????????????????????????????????????????????????????????????*/
                Map<String, Object> params = new HashMap<>();
                Map<String, String> querys = new HashMap<>();
                params.put(ChangeTaskFieldEnum.STATUS.getName(), "");
                params.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), "");
                params.put(ChangeTaskFieldEnum.MERGE_TASK_NO.getName(), "");
                querys.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), instanceId);
                Map<String, Object> result = selectMap(ChangeTableNameEnum.CHANGE_TASK, params, querys);
                Object mergeTaskNo = result.get(ChangeTaskFieldEnum.MERGE_TASK_NO.getName());
                if (mergeTaskNo != null && !"".equals(mergeTaskNo.toString().trim())) {
                    Map<String, Object> paramMap = new HashMap<>();
                    Map<String, String> queryMap = new HashMap<>();
                    paramMap.put(ChangeTaskFieldEnum.PRE_APPROVAL.getName(), "");
                    paramMap.put(ChangeTaskFieldEnum.ASSIGNEE.getName(), "");
                    paramMap.put(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName(), "");
                    queryMap.put(ChangeTaskFieldEnum.EXTRA1.getName(), mergeTaskNo.toString().trim());
                    Map<String, Object> resultMap = selectMap(ChangeTableNameEnum.CHANGE_TASK, paramMap, queryMap);
                    Map<String, String> query = new HashMap<>();
                    query.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), instanceId);
                    update(ChangeTableNameEnum.CHANGE_TASK, resultMap, query);
                }
            }
        }

        log.info("?????????????????????????????????????????????????????????????????????");
        AjaxResult result = (AjaxResult) proceed;
        if (param.length == 5) {
            String code = param[0].toString();
            String instanceId = param[2].toString();
            String taskDefinitionKey = "";
            List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).list();
            if (!list.isEmpty()) {
                taskDefinitionKey = list.get(0).getTaskDefinitionKey();
            }
            if (CHANGE.equals(code)) {
                String status = getChangeColumnValueBySingleCondition(ChangeFieldEnum.STATUS, ChangeFieldEnum.INSTANCE_ID, instanceId);
                if (ChangeStatusEnum.preApproval.getName().equals(status)) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String now = dateTimeFormatter.format(LocalDateTime.now());
                    updateChangeSingle(ChangeFieldEnum.INSTANCE_ID, instanceId, ChangeFieldEnum.SUBMIT_DATE, now);
                }
                syncChangeStatus(ChangeFieldEnum.INSTANCE_ID, instanceId);
                ChangeUtil.ADPM_POOL.submit(instanceId, () -> {
                    updateAdpmChange(instanceId);
                });
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String nowDate = dateTimeFormatter.format(now);
                updateChangeSingle(ChangeFieldEnum.INSTANCE_ID,instanceId,ChangeFieldEnum.UPDATE_TIME,nowDate);
            } else if (CHANGE_TASK.equals(code)) {
                syncTaskStatus(ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).list();
                if (!taskList.isEmpty()) {
                    Task task = taskList.get(0);
                    taskDefinitionKey = task.getTaskDefinitionKey();
                }
                if (ChangeTaskDefineKeyEnum.REMEDY_PRE_APPROVAL.getName().equals(taskDefinitionKey)) {
                    //????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    remedyPass(instanceId);
                }
                ChangeUtil.ADPM_POOL.submit(instanceId, () -> {
                    updateAdpmTask(instanceId);
                });
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String nowDate = dateTimeFormatter.format(now);
                updateChangeTaskSingle(ChangeTaskFieldEnum.INSTANCE_ID,instanceId,ChangeTaskFieldEnum.UPDATED_TIME,nowDate);
            }
        }
        System.out.println(result.toString());
        return result;
    }
}

