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
        StringBuilder stringBuilder = new StringBuilder("增强方法的入参值：");
        Arrays.stream(param).forEach(a -> {
            stringBuilder.append(a + ",");
        });
        log.info("增强方法的入参值：" + stringBuilder.toString());
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
                    return AjaxResult.warn("该变更已锁定");
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
                            return AjaxResult.warn("请传入审批科室");
                        }
                        if (!(o instanceof List)) {
                            return AjaxResult.warn("请传入数组");
                        }
                        List list = (List) o;
                        if (list.isEmpty()) {
                            return AjaxResult.warn("请传入审批科室");
                        }
                    }
                }
                String branchFlag = getChangeColumnValueBySingleCondition(ChangeFieldEnum.BRANCH_FLAG, ChangeFieldEnum.INSTANCE_ID, instanceId);
                if (ChangeStatusEnum.unSubmit.getName().equals(status)) {
                    //判断涉及系统的数量是否和所有的任务单变更应用系统一致，且不可减少，可增加
                    String changeNo = getChangeColumnValueBySingleCondition(ChangeFieldEnum.EXTRA1, ChangeFieldEnum.ID, changeId);
                    List<String> sysList = getSysListByChangeNo(changeNo);
                    sysList = sysList.stream().distinct().collect(Collectors.toList());
                    RiskAssessment result = riskAssessmentService.selectRiskAssessmentById(changeNo);
                    if (result == null) {
                        return AjaxResult.warn("请先进行风险评估");
                    }
                    String referSysStr = result.getReferSys();
                    String[] referArray = referSysStr.split(",");
                    List<String> sys = Arrays.stream(referArray).distinct().collect(Collectors.toList());
                    if (!sysList.isEmpty() && !sys.containsAll(sysList)) {
                        return AjaxResult.warn("所涉系统与改变更所有任务单的变更系统不一致，请重新进行风险评估");
                    }
                    if (getTaskCountByChangeId(changeId) == 0) {
                        return AjaxResult.warn("未关联任何变更任务单，请先发布变更任务单！");
                    } else if (/*getTaskCountByChangeIdAndStatus(changeId, "待提交") != 0 ||*/ getTaskCount(changeId, ChangeTaskStatusEnum.registered, 0) != 0) {
                        return AjaxResult.warn("变更任务单未全部提交");
                    } else if (reason == null || type == null ||
                            "".equals(type.toString().trim()) || "".equals(reason.toString().trim())) {
                        return AjaxResult.warn("有必填字段未填");
                    } else if (riskLevel == null || "".equals(riskLevel.toString().trim())) {
                        return AjaxResult.warn("请进行风险评估！");
                    } else if ("2".equals(type.toString())) {
                        if (urgentReason == null || "".equals(urgentReason.toString().trim())) {
                            return AjaxResult.warn("紧急变更原因未填！");
                        }
                        String orgId = getChangeTableStarterOrgId(changeId);
                        if (changeDeptService.isDeptAndSystem(orgId)) {
                            ChangeDeptEntity changeDeptEntity = changeDeptService.selectByDeptId(orgId);
                            Integer overSize = Integer.parseInt(changeDeptEntity.getOverSize());
                            if (overSize <= 0) {
                                return AjaxResult.warn("紧急变更额度已用完！");
                            }
                        }
                    } else if ("1".equals(type.toString()) && !"1".equals(riskLevel.toString().trim())) {
                        return AjaxResult.warn("普通变更的风险级别只能是低，请重新评估或修改变更类型");
                    }
                    if ("1".equals(branchFlag)) {
                        //如果是分行流程，那么检查下填了变更级别
                        if (changeLevel == null || "".equals(changeLevel.toString().trim())) {
                            return AjaxResult.warn("变更级别不可为空！");
                        }
                    }
                   /* String submitDate = getChangeColumnValueBySingleCondition(ChangeFieldEnum.SUBMIT_DATE, ChangeFieldEnum.INSTANCE_ID, instanceId);
                    if (submitDate != null && !"".equals(submitDate)) {
                        //如果已经有了变更提交时间，说明这是退回来的，有些字段不允许改，比如变更类型
                        String oldType = getChangeColumnValueBySingleCondition(ChangeFieldEnum.TYPE, ChangeFieldEnum.INSTANCE_ID, instanceId);
                        if (!type.toString().equals(oldType)) {
                            return AjaxResult.warn("被退回的变更单不允许修改变更类型");
                        }
                    }*/
                }

                if (ChangeDefineKeyEnum.changeMangerApproval.getName().equals(taskDefinitionKey)) {
                    Map<String, Object> variables = ((CustomerVo) param[4]).getVariables();
                    Integer recode = Integer.valueOf(variables.get(RECODE).toString());
                    if (recode == 3) {
                        //查一下是否有需要退回预审的任务单
                        Integer count = getTaskCountByChangeIdAndTaskColumn(changeId, ChangeTaskFieldEnum.BACK_TO_APPROVAL_FLAG, "1");
                        if (count == 0) {
                            return AjaxResult.warn("未选择需要退回预审的任务单");
                        }
                    }
                }
                //并行审批的时候，如果有人点了否，那么其他的都直接丢掉，直接退回
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
                    return AjaxResult.warn("该变更任务已锁定");
                }
                String changeTaskNo = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.EXTRA1, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                String status = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.STATUS, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                Map<String, Object> variables = ((CustomerVo) param[4]).getVariables();
                String remedyFlagStr = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.REMEDY_FLAG, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                String type = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.TYPE.getName()).toString();
                Integer remedy = Integer.parseInt(remedyFlagStr);
                if (remedy == 0) {
                    //如果不是补救单，那么受派人与预审人不能是同一个人
                    Object assignee = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.ASSIGNEE.getName());
                    Object preApproval = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PRE_APPROVAL.getName());
                    if (assignee.toString().trim().equals(preApproval.toString().trim())) {
                        return AjaxResult.warn("受派人与预审人不可为同一个人");
                    }
                }
                Object deployWay = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.DEPLOY_WAY.getName());
                if (ChangeTaskStatusEnum.registered.getName().equals(status)) {
                    //部署方式
                    //应用支持部的影响的应用系统必须要填
                    Object referSys = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.REFER_SYS.getName());
                    String fundamental = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName()).toString();
                    if (deployWay == null || "".equals(deployWay.toString().trim())) {
                        return AjaxResult.warn("部署方式不可为空");
                    }
                    Object taskDept = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.TASKDEPT.getName());
                    ChangeTaskOrg changeTaskOrg = changeTaskOrgService.getByOrgName("应用支持部");
                    if (changeTaskOrg.getOrgid().equals(taskDept) && (referSys == null || "".equals(referSys.toString().trim()))) {
                        return AjaxResult.warn("应用支持部变更应用系统必填");
                    }
                    String refer = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.REFER_FLAG.getName()).toString();
                    if ("1".equals(refer) && (referSys == null || "".equals(referSys.toString().trim()))) {
                        return AjaxResult.warn("涉及应用系统必填");
                    }
                    if ("2".equals(refer)) {
                        if ("".equals(fundamental.trim())) {
                            return AjaxResult.warn("涉及基础设施必填");
                        }
                        if ("[]".equals(fundamental.trim())) {
                            return AjaxResult.warn("涉及基础设施必填");
                        }
                    }
                    //TODO 需要配合前端一起发布
                   /* Page<Map<String, Object>> page = new Page<>();
                    Page<Map<String, Object>> resultPage = changeService.getAllFieldValuePageBySingleCondition(type, "changeTaskNo", changeTaskNo, page);
                    if (resultPage != null&&resultPage.getTotal()==0) {
                        return AjaxResult.warn("场景参数未填");
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
                        //TODO 判断任务单号是否合规
                        return AjaxResult.warn("请输入前置任务单号");
                    }
                }
                if (ChangeTaskDefineKeyEnum.remedySubmit.getName().equals(taskDefinitionKey)) {
                    //查一下是否有部门领导
                    String taskId = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ID, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                    String leader = getTaskAssigneeGroupLeaderUserId(taskId);
                    if (leader == null) {
                        return AjaxResult.warn("请先配置受派组领导！");
                    }
                }
                //TODO 需要配合前端一起发布
                /*if(ChangeTaskStatusEnum.preApprovaling.getName().equals(status)){
                    Page<Map<String, Object>> page = new Page<>();
                    Page<Map<String, Object>> resultPage = changeService.getAllFieldValuePageBySingleCondition(type, "changeTaskNo", changeTaskNo, page);
                    if (resultPage != null&&resultPage.getTotal()==0) {
                        return AjaxResult.warn("场景参数未填");
                    }
                }*/
               /* if((ChangeTaskDefineKeyEnum.taskPreApproval.getName().equals(taskDefinitionKey)||ChangeTaskDefineKeyEnum.remedyApproval.getName().equals(taskDefinitionKey))
                    &&variables.get(RECODE)!=null&&"1".equals(variables.get(RECODE).toString())){
                    String deployWayStr = deployWay.toString();
                    if(!"1".equals(deployWayStr)){
                        //预审通过的时候 判断是否有自动化场景参数
                        ChangeTaskScene changeTaskScene = changeTaskSceneService.getChangeTaskSceneByCode(type);
                        String sceneName = changeTaskScene.getName();
                        String orgId = changeTaskScene.getOrgid();
                        ChangeTaskScene scene = changeTaskSceneService.getAutoChangeTaskScene(orgId, sceneName);
                        if(scene==null){
                            return AjaxResult.warn("无自动化表单");
                        }
                        String autoType = scene.getCode();
                        Page<Map<String, Object>> page = new Page<>();
                        Page<Map<String, Object>> resultPage = changeService.getAllFieldValuePageBySingleCondition(autoType, "changeTaskNo", changeTaskNo, page);
                        if (resultPage != null&&resultPage.getTotal()==0) {
                            return AjaxResult.warn("无自动化参数");
                        }
                    }
                }*/
                if (ChangeTaskStatusEnum.waitImpl.getName().equals(status) || (ChangeTaskStatusEnum.preApprovaling.getName().equals(status)&&variables.get(RECODE)!=null&&"1".equals(variables.get(RECODE).toString()))) {
                    Object planStartDate = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PLAN_START_DATE.getName());
                    Object planOverDate = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PLAN_OVER_DATE.getName());
                    if (planStartDate == null || "".equals(planStartDate.toString()) || planOverDate == null || "".equals(planOverDate.toString())) {
                        return AjaxResult.warn("计划时间不得为空");
                    }
                    String planStartDateStr = planStartDate.toString();
                    String planOverDateStr = planOverDate.toString();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime planStartDateTime = LocalDateTime.parse(planStartDateStr, dateTimeFormatter);
                    LocalDateTime planOverDateTime = LocalDateTime.parse(planOverDateStr, dateTimeFormatter);
                    if (!planStartDateTime.isBefore(planOverDateTime)) {
                        return AjaxResult.warn("计划开始时间不得晚于计划结束时间！");
                    }
                    LocalDateTime planOverDateTimeLine = planStartDateTime.plus(24, ChronoUnit.HOURS);
                    if (planOverDateTime.isAfter(planOverDateTimeLine)) {
                        return AjaxResult.warn("计划结束时间不允许超过计划开始时间24小时，建议联系发起人分拆任务");
                    }
                }
                if (ChangeTaskStatusEnum.waitImpl.getName().equals(status)) {
                    //如果当前时间比计划开始时间早，或超过了计划结束时间，则不允许审批，提示其更改计划时间
                    /*LocalDateTime now = LocalDateTime.now();
                    String planStartDate = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PLAN_START_DATE.getName()).toString();
                    String planOverDate = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.PLAN_OVER_DATE.getName()).toString();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime planStartDateTime = LocalDateTime.parse(planStartDate, dateTimeFormatter);
                    LocalDateTime planOverDateTime = LocalDateTime.parse(planOverDate, dateTimeFormatter);
                    if (now.isBefore(planStartDateTime) || now.isAfter(planOverDateTime)) {
                        return AjaxResult.warn("当前时间不在计划时间窗口内，请修改计划时间！");
                    }*/
                    //调用创建屏蔽告警的方法
                    List<DesignBizShieldWarn> dataList = designBizShieldWarnService.selectDesignBizShieldWarnByChangeTaskNo(changeTaskNo);
                    if (!dataList.isEmpty()) {
                        Map<String, Object> condition = parseToCondition(dataList);
                        System.out.println(JSON.toJSONString(condition));
                        ChangeUtil.ALARM_POOL.submit(changeTaskNo, () -> {
                            String name = "变更任务屏蔽告警" + "【" + changeTaskNo + "】";
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
                            //如果已经是补救单的话，则不允许操作
                            String remedyFlag = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.REMEDY_FLAG, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                            Integer flag = Integer.parseInt(remedyFlag);
                            if (flag == 1) {
                                return AjaxResult.warn("补救单不允许再建补救单");
                            }
                            Object implResult = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.IMPL_RESULT.getName());
                            if (implResult == null || !"3".equals(implResult.toString())) {
                                return AjaxResult.warn("请修改实施结果为缺陷");
                            }
                            List<String> remedyReason = (List<String>) customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.REMEDY_REASON.getName());
                            remedyReason = remedyReason.stream().filter(p -> !"".equals(p.trim())).collect(Collectors.toList());
                            if (remedyReason.isEmpty()) {
                                return AjaxResult.warn("请输入缺陷原因");
                            }
                            Map<String, Object> old = customerVo.getCustomerFormContent().getFields();
                            Map<String, Object> map = createRemedy(old, instanceId);
                            customerVo.getCustomerFormContent().getFields().putAll(map);
                        } else if (recode == 1) {
                            //检查下有没有复核人和实施结果
                            Object checkMan = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.CHECK_MAN.getName());
                            Object result = customerVo.getCustomerFormContent().getFields().get(ChangeTaskFieldEnum.IMPL_RESULT.getName());
                            if (checkMan == null || "".equals(checkMan.toString().trim())) {
                                return AjaxResult.warn("未填入复核人");
                            }
                            if (result == null || "".equals(result.toString().trim())) {
                                return AjaxResult.warn("请选择实施结果");
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
                //在提交变更单后,也就是变更单状态为变更预审时
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
                /*任务单的并包不是空的，那就把这个任务单的并包更新给所有并这个任务单的并包单号*/
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

        log.info("目标方法执行完后得到返回值并进行相应对象转化：");
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
                    //如果预审人和受派人是旧单子的实施人（也就是发起补救单的人），那么跳过预审
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

