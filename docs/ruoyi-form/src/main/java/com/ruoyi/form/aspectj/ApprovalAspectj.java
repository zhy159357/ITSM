package com.ruoyi.form.aspectj;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.domain.DesignFormVersion;
import com.ruoyi.form.domain.FieldActivityNode;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.service.FormVersionService;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @ClassName
 * @Description TODO
 * @Author zzz
 * @Date
 */
@Aspect
@Component
@Slf4j
public class ApprovalAspectj extends Base {

    @Autowired
    TaskService taskService;
    @Autowired
    ISysApplicationManagerService appService;
    @Autowired
    IOgPersonService ogPersonService;
    @Autowired
    FormVersionService formVersionService;


    @Pointcut("execution(public * com.ruoyi.form.service.impl.CustomerFormServiceImpl.approvalPopUp(..))")
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
        Object proceed = joinPoint.proceed();
        AjaxResult result = (AjaxResult) proceed;
        System.out.println(result.toString());
        if (param.length == 5) {
            String code = param[0].toString();
            if (param[1] == null) {
                return result;
            }
            String instanceId = param[1].toString();
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).list();
            String taskDefinitionKey = "";
            if (!taskList.isEmpty()) {
                Task task = taskList.get(0);
                taskDefinitionKey = task.getTaskDefinitionKey();
            }
            Map<String, Object> map = (Map<String, Object>) result.get("data");
            if (CHANGE.equals(code)) {
                String status = getChangeColumnValueBySingleCondition(ChangeFieldEnum.STATUS, ChangeFieldEnum.INSTANCE_ID, instanceId);
                String type = param[3].toString();
                List<Map<String, Object>> buttonInfoList = (List<Map<String, Object>>) map.get("actionButtonList");
                Map<String, Object> button1 = new HashMap<>();
                button1.put("buttonName", "查看任务详情");
                button1.put("buttonUrlPath", "/change/view/task/list/page");
                buttonInfoList.add(button1);
                if ("2".equals(type)) {
                    List<Map<String, Object>> jsonData = (List<Map<String, Object>>) map.get("jsonData");
                    Map<String, Object> data = jsonData.get(0);
                    List<FieldActivityNode> fieldActivityStatusList = (List<FieldActivityNode>) data.get("fieldActivityStatus");
                    /*如果是走总行流程，则变更级别只读*/
                    String branchFlag = getChangeColumnValueBySingleCondition(ChangeFieldEnum.BRANCH_FLAG, ChangeFieldEnum.INSTANCE_ID, instanceId);
                    if ("0".equals(branchFlag)) {
                        fieldActivityStatusList.stream().filter(p -> ChangeFieldEnum.CHANGE_LEVLE.getName().equals(p.getFieldName())).findFirst().ifPresent(field -> field.setFieldStatus("0"));
                    }
                    if (ChangeDefineKeyEnum.generalManager.getName().equals(taskDefinitionKey)) {
                        //去掉保存按钮
                        Object save = buttonInfoList.get(1);
                        if (save != null) {
                            buttonInfoList.remove(1);
                        }
                    }else {
                        Map<String, Object> button3 = new HashMap<>();
                        button3.put("buttonName", "风险评估");
                        button3.put("buttonUrlPath", "/customerForm/assessment/form/{changeNo}");
                        buttonInfoList.add(button3);
                    }

                    if (ChangeStatusEnum.unSubmit.getName().equals(status)) {
                        //TODO 把节点拿出来修改该节点属于接口传来不可变的字段状态
                        List<Map<String, Object>> approveButtonList = (List<Map<String, Object>>) map.get("approveButtonList");
                        Map<String, Object> buttonMap = new HashMap<>();
                        buttonMap.put("agentExpression", null);
                        buttonMap.put("agentUserId", null);
                        buttonMap.put("expression", null);
                        buttonMap.put("name", "提交");
                        approveButtonList.add(buttonMap);
                        Map<String, Object> button = new HashMap<>();
                        button.put("buttonName", "发起任务");
                        button.put("buttonUrlPath", "/taskPage");
                        Map<String, Object> mapField = new HashMap<>();
                        //变更单与变更任务单的映射字段带入
                        button.put("mapField", mapField);
                        Map<String, Object> mapFixedField = new HashMap<>();
                        List<Map<String, Object>> appendList = (List<Map<String, Object>>) map.get("appenJsondata");
                        appendList.stream().anyMatch(p -> {
                            String label = p.get("lable").toString();
                            if ("变更单编号".equals(label)) {
                                String changeNo = p.get("value").toString();
                                mapFixedField.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
                                return true;
                            }
                            return false;
                        });
                        button.put("mapFixedField", mapFixedField);
                        Map<String, Object> deleteButton = new HashMap<>();
                        deleteButton.put("buttonName", CustomerFlowConstants.cancelButtonName);
                        deleteButton.put("buttonUrlPath", "/customerForm/change/cancel");
                        buttonInfoList.add(deleteButton);
                        buttonInfoList.add(button);
                    }
                }

                List<Map<String, Object>> jsonData = (List<Map<String, Object>>) map.get("jsonData");
                //String formJson = jsonData.get(0).get("formJson").toString();
                DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", "design_biz_change").eq("is_current", 1));
                String formJson = formVersion.getJson();
                //表单值输入
                List<Map<String, Object>> list = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeFieldEnum.INSTANCE_ID.getName(), instanceId);
                Map<String, Object> dataMap = list.get(0);
                String dataJson = VueDataJsonUtil.analysisDataJson(formJson, dataMap);
                jsonData.get(0).put("formJson", dataJson);
                //修改下按钮顺序
                List<Map<String, Object>> approveButtonList = (List<Map<String, Object>>) map.get("approveButtonList");
                if (approveButtonList != null && !approveButtonList.isEmpty()) {
                    Map<String, Object> button = approveButtonList.stream().filter(p -> {
                        Object o = p.get("name");
                        return o != null && "通过".equals(o.toString());
                    }).findFirst().orElse(null);
                    Map<String, Object> prepared = approveButtonList.stream().filter(p -> {
                        Object o = p.get("name");
                        return o != null && "变更准备".equals(o.toString());
                    }).findFirst().orElse(null);
                    if (prepared != null) {
                        approveButtonList.remove(prepared);
                        approveButtonList.add(prepared);
                    }
                    if (button != null) {
                        approveButtonList.remove(button);
                        approveButtonList.add(button);
                    }
                }
            } else if (CHANGE_TASK.equals(code)) {
                String status = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.STATUS, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                map.put("formStepInfo", null);
                //TODO 如果是补救单的原单，没有instancid需要另外考虑
                List<Map<String, Object>> list = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.INSTANCE_ID.getName(), instanceId);
                List<Map<String, Object>> jsonData = (List<Map<String, Object>>) map.get("jsonData");
                //String formJson = jsonData.get(0).get("formJson").toString();
                DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", "design_biz_changeTask").eq("is_current", 1));
                String formJson = formVersion.getJson();
                Map<String, Object> dataMap = list.get(0);
                String taskDept = dataMap.get(ChangeTaskFieldEnum.TASKDEPT.getName()).toString();
                dataMap = transferTaskMultipleChoiceFieldMap(dataMap);
                String dataJson = VueDataJsonUtil.analysisDataJson(formJson, dataMap);
                String type = param[3].toString();
                // 如果是预审通过，并且当前登录人是变更经理
                if ("2".equals(type)) {
                    if (ChangeTaskDefineKeyEnum.submitTask.getName().equals(taskDefinitionKey)) {
                        List<Map<String, Object>> approveButtonList = (List<Map<String, Object>>) map.get("approveButtonList");
                        Map<String, Object> buttonMap = new HashMap<>();
                        buttonMap.put("agentExpression", null);
                        buttonMap.put("agentUserId", null);
                        buttonMap.put("expression", null);
                        buttonMap.put("name", "提交");
                        approveButtonList.add(buttonMap);
                    }
                    if (ChangeTaskStatusEnum.registered.getName().equals(status)) {
                        List<Map<String, Object>> buttonInfoList = (List<Map<String, Object>>) map.get("actionButtonList");
                        Object save = buttonInfoList.get(1);
                        if (save != null) {
                            buttonInfoList.remove(1);
                        }
                        //TODO 把节点拿出来修改该节点属于接口传来不可变的字段状态
                        Map<String, Object> button = new HashMap<>();
                        button.put("buttonName", "转派");
                        button.put("buttonUrlPath", "/customerForm/change/transfer/waitimpltask");
                        Map<String, Object> button2 = new HashMap<>();
                        button2.put("buttonName", "屏蔽告警");
                        button2.put("buttonUrlPath", "/changeData/shield/warn/page");
                        buttonInfoList.add(button);
                        buttonInfoList.add(button2);
                        String changeId = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.CHANGE_ID, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                        String creator = getChangeColumnValueBySingleCondition(ChangeFieldEnum.CREATOR, ChangeFieldEnum.ID, changeId);
                        String current = CustomerBizInterceptor.currentUserThread.get().getpId();
                        if (current.equals(creator)) {
                            Map<String, Object> deleteButton = new HashMap<>();
                            deleteButton.put("buttonName", CustomerFlowConstants.cancelButtonName);
                            deleteButton.put("buttonUrlPath", "/customerForm/change/task/cancel");
                            buttonInfoList.add(deleteButton);
                        }
                    }
                    if (ChangeTaskStatusEnum.waitImpl.getName().equals(status)) {
                        Map<String, Object> button = new HashMap<>();
                        button.put("buttonName", "转派");
                        button.put("buttonUrlPath", "/customerForm/change/transfer/waitimpltask");
                        button.put("deptEditFlag", 1);
                        List<Map<String, Object>> buttonInfoList = (List<Map<String, Object>>) map.get("actionButtonList");
                        buttonInfoList.add(button);
                    }
                    if (ChangeTaskDefineKeyEnum.implement.getName().equals(taskDefinitionKey)) {
                        //如果是补救单的话，去掉新建补救任务单的按钮
                        String remedyFlag = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.REMEDY_FLAG, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                        Integer re = Integer.parseInt(remedyFlag);
                        if (re == 1) {
                            List<Map<String, Object>> buttonInfoList = (List<Map<String, Object>>) map.get("approveButtonList");
                            buttonInfoList.removeIf(itMap -> "${recode==2}".equals(itMap.get("expression")));
                        }
                        Map<String, Object> button = new HashMap<>();
                        button.put("buttonName", "启动自动任务");
                        button.put("buttonUrlPath", "/customerForm/change/auto/start");
                        List<Map<String, Object>> buttonInfoList = (List<Map<String, Object>>) map.get("actionButtonList");
                        buttonInfoList.add(button);
                    }
                    if (ChangeTaskDefineKeyEnum.taskPreApproval.getName().equals(taskDefinitionKey)) {
                        Map<String, Object> button = new HashMap<>();
                        button.put("buttonName", "转派");
                        button.put("buttonUrlPath", "/customerForm/change/transfer/waitimpltask");
                        button.put("deptEditFlag", 1);
                        List<Map<String, Object>> buttonInfoList = (List<Map<String, Object>>) map.get("actionButtonList");
                        buttonInfoList.add(button);
                    }
                    if (ChangeTaskStatusEnum.preApprovaling.getName().equals(status)) {
                        Map<String, Object> button2 = new HashMap<>();
                        button2.put("buttonName", "屏蔽告警");
                        button2.put("buttonUrlPath", "/changeData/shield/warn/page");
                        List<Map<String, Object>> buttonInfoList = (List<Map<String, Object>>) map.get("actionButtonList");
                        buttonInfoList.add(button2);
                        Map<String, Object> params = new HashMap<>();
                        params.put(ChangeTaskFieldEnum.PLAN_START_DATE.getName(), "");
                        params.put(ChangeTaskFieldEnum.REFER_SYS.getName(), "");
                        Map<String, String> query = new HashMap<>();
                        query.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), instanceId);
                        Map<String, Object> resultMap = selectMap(ChangeTableNameEnum.CHANGE_TASK, params, query);
                        Object planStartDateObj = resultMap.get(ChangeTaskFieldEnum.PLAN_START_DATE.getName());
                        if (planStartDateObj == null || "".equals(planStartDateObj.toString().trim())) {
                            //获取系统的红线时间
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String referSys = "";
                            Object sys = resultMap.get(ChangeTaskFieldEnum.REFER_SYS.getName());
                            if (sys != null) {
                                referSys = sys.toString();
                            }
                            String startDateTime = "";
                            String overDateTime = "";
                            if (referSys != null && !"".equals(referSys)) {
                                OgSys sysApp = appService.selectOgSysBySysCode(referSys);
                                if (sysApp != null && sysApp.getRedlineTime() != null) {
                                    String redLine = sysApp.getRedlineTime();
                                    LocalDateTime startDate = LocalDateTime.of(LocalDate.now(), LocalTime.parse(redLine));
                                    LocalDateTime overDate = startDate.plus(4, ChronoUnit.HOURS);
                                    startDateTime = dateTimeFormatter.format(startDate);
                                    overDateTime = dateTimeFormatter.format(overDate);
                                }
                            }
                            Map<String, Object> re = new HashMap<>();
                            re.put(ChangeTaskFieldEnum.PLAN_START_DATE.getName(), startDateTime);
                            re.put(ChangeTaskFieldEnum.PLAN_OVER_DATE.getName(), overDateTime);
                            dataJson = VueDataJsonUtil.analysisDataJson(dataJson, re);
                        }
                    }
                } else if ("3".equals(type)) {
                    OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(ogUser.getpId());
                    if (taskDept.equals(ogPerson.getOrgId()) && ChangeTaskStatusEnum.waitImpl.getName().equals(status)) {
                        Map<String, Object> button = new HashMap<>();
                        button.put("buttonName", "抢单");
                        button.put("buttonUrlPath", "/customerForm/change/rob/task");
                        List<Map<String, Object>> buttonList = (List<Map<String, Object>>) map.get("actionButtonList");
                        buttonList.add(button);
                    }
                }
                jsonData.get(0).put("formJson", dataJson);
            }
        }
        log.info("目标方法执行完后得到返回值并进行相应对象转化：");
        return result;
    }
}

