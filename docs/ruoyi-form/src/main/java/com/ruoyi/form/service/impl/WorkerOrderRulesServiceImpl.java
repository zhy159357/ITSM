package com.ruoyi.form.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.domain.*;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.ButtonActionMapper;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignFormVersionMapper;
import com.ruoyi.form.service.*;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.form.constants.ProblemConstant.ID;
import static com.ruoyi.form.constants.ProblemConstant.STATUS;

/**
 * @ClassName WorkerOrderRulesServiceImpl
 * @Description ?????????????????????
 * @Author JiaQi Zhang
 * @Date 2022/12/26 9:01
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkerOrderRulesServiceImpl implements WorkerOrderRulesService {
    private final static String bizTablePrefix = "design_biz";
    private final CustomerFormMapper customerFormMapper;
    private final DesignBizJsonDataService designBizJsonDataService;
    private final OperationDetailsService operationDetailsService;
    private final IIdGeneratorService generatorService;
    private final IChangePersonService iChangePersonService;
    private final IdentityService identityService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final FormStatusActivityNodeService formStatusActivityNodeService;
    private final CustomerFormService customerFormService;
    private final FormService formService;
    private final RepositoryService repositoryService;
    private final ButtonActionMapper buttonActionMapper;
    private final HistoryService historyService;
    private final FormStepStatusService formStepStatusService;
    private final FieldActivityNodeService fieldActivityNodeService;
    private final IOgUserService ogUserService;
    private final DesignFormVersionMapper designFormVersionMapper;

    private void dynamicTableName(String code) {
        MybatisPlusConfig.customerTableName.set(String.format("%s_%s", bizTablePrefix, code));
    }

    @Override
    public AjaxResult insertOrUpdate(String code, CustomerFormContent customerFormContent) {
        dynamicTableName(code);
        int resultItem = 0;
        Long bizId = ObjectUtil.isEmpty(customerFormContent.getFields().get("id")) ? null : Long.valueOf(String.valueOf(customerFormContent.getFields().get("id")));
        DesignBizJsonData designBizJsonData = null;

        for (String key : customerFormContent.getFields().keySet()) {
            if (customerFormContent.getFields().get(key) instanceof Collection) {
                customerFormContent.getFields().put(key, JSON.toJSONString(customerFormContent.getFields().get(key)));
            }
        }
        if (bizId != null) {
            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
            //??????????????????ID???????????????????????????????????????
            List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
            //????????????????????????
            String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
            //???????????????????????????????????????
            List<Map<String, String>> recordDataChanged = formFieldInfos.stream().filter(map -> String.valueOf(map.get("record_data_changed")).equals("1")).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(recordDataChanged)) {
                String selectKey = recordDataChanged.stream().map(a -> a.get("name")).collect(Collectors.joining(",")) + "," + RedundancyFieldEnum.extra1.name;
                //?????????????????????
                Map<String, Object> oldValue = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(selectKey).eq("id", bizId)).get(0);
                List<OperationDetails> operationDetails = new ArrayList<>();
                //?????????key-value??????????????????????????????????????????
                for (Map.Entry<String, Object> oldEntry : oldValue.entrySet()) {
                    //???????????????
                    if (oldEntry.getKey().equals(RedundancyFieldEnum.extra1.name)) {
                        continue;
                    }
                    //????????????
                    if (oldEntry.getKey().contains("file")) {
                        //????????????????????????
                        if (oldEntry.getValue() != null && !oldEntry.getValue().equals(customerFormContent.getFields().get(oldEntry.getKey()))) {
                            List<String> oldFiles = JSONObject.parseArray(String.valueOf(oldEntry.getValue()), String.class);
                            List<String> newFiles = JSONObject.parseArray(String.valueOf(customerFormContent.getFields().get(oldEntry.getKey())), String.class);
                            if (oldFiles == null || newFiles == null) {
                                continue;
                            }
                            //???????????????????????????
                            String newInsertFiles = newFiles.stream().filter(a -> !oldFiles.contains(a)).collect(Collectors.joining("???"));
                            //???????????????????????????
                            String oldDeleteFiles = oldFiles.stream().filter(a -> !newFiles.contains(a)).collect(Collectors.joining("???"));
                            String desc = CustomerBizInterceptor.currentUserThread.get().getPname();
                            if (!StringUtils.isEmpty(newInsertFiles)) {
                                desc = desc + "????????????" + newInsertFiles + "??????";
                            } else if (!oldDeleteFiles.isEmpty()) {
                                desc = desc + "????????????" + oldDeleteFiles + "??????";
                            }
                            OperationDetails op = OperationDetails.builder()
                                    .operationType(formName)
                                    .bizNo(oldValue.get(RedundancyFieldEnum.extra1.name).toString())
                                    .oldValue(String.valueOf(oldEntry.getValue()))
                                    .newValue(String.valueOf(customerFormContent.getFields().get(oldEntry.getKey())))
                                    .description(desc)
                                    .build();
                            operationDetails.add(op);
                        }
                    }
                    if (customerFormContent.getFields().get(oldEntry.getKey()) != null &&
                            !customerFormContent.getFields().get(oldEntry.getKey()).equals(oldEntry.getValue())) {
                        OperationDetails op = OperationDetails.builder()
                                .operationType(formName)
                                .bizNo(oldValue.get(RedundancyFieldEnum.extra1.name).toString())
                                .oldValue(String.valueOf(oldEntry.getValue()))
                                .newValue(String.valueOf(customerFormContent.getFields().get(oldEntry.getKey())))
                                .description(CustomerBizInterceptor.currentUserThread.get().getPname() + "????????????" + recordDataChanged.stream().filter(a -> a.get("name").equals(oldEntry.getKey())).findFirst().get().get("description") + "????????????" + oldEntry.getValue() + " ????????????" + customerFormContent.getFields().get(oldEntry.getKey()))
                                .build();
                        operationDetails.add(op);
                    }
                }

                //??????????????????
                operationDetailsService.saveBatch(operationDetails);
            }
            //????????? ???????????????????????????????????????????????????????????????
            customerFormContent.getFields().put("updated_time", DateUtils.getNowDate());
            customerFormContent.getFields().put("updated_by", CustomerBizInterceptor.currentUserThread.get().getUserId());
            resultItem = customerFormMapper.updateById(customerFormContent);
            designBizJsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", bizTablePrefix, code)).bizId(bizId).jsonData(customerFormContent.getDataJson()).build();
            designBizJsonDataService.update(designBizJsonData, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_ID, bizId).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
        } else {
            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
            String loginUser = CustomerBizInterceptor.currentUserThread.get().getUserId();
            //????????? ??????????????????????????????????????????????????????????????????????????????????????????
            customerFormContent.getFields().put("created_time", DateUtils.getNowDate());
            if (!WorkOrderInformation.workerOrder.getCode().equals(code)){
                customerFormContent.getFields().put("created_by", loginUser);
                customerFormContent.getFields().put("updated_by", loginUser);
            }
            customerFormContent.getFields().put("updated_time", DateUtils.getNowDate());
            customerFormContent.getFields().put("status", "?????????");
            customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, generatorService.createNoAsLength("GD", 3));
            customerFormContent.getFields().put(RedundancyFieldEnum.extra2.name, currentTableInfo.get("version"));
            resultItem = customerFormMapper.insert(customerFormContent);
            designBizJsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", bizTablePrefix, code)).bizId(customerFormContent.getId()).jsonData(customerFormContent.getDataJson()).build();
            designBizJsonDataService.save(designBizJsonData);
            bizId = customerFormContent.getId();
            //??????????????????ID???????????????????????????????????????
            List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
            //????????????????????????
            String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
            String bizNo = customerFormContent.getFields().get(RedundancyFieldEnum.extra1.name).toString();
        }
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(bizId);
    }

    @Override
    public AjaxResult getStartProcessCondition(String code) {
        Map<String, Object> resultMap = new HashMap<>();
        if (WorkOrderInformation.workerOrderRules.getCode().equals(code)) {
            OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
            List<ChangeDeptPersonEntity> deptPerson = iChangePersonService.list(Wrappers.<ChangeDeptPersonEntity>query().eq("dept_id", CustomerBizInterceptor.currentUserThread.get().getOrgId()));
            CustomerStrategyUtil.verificationNull(deptPerson, CustomerBusinessEnum.DEPT_LEADER_IS_NOT_CONFIG);
            resultMap.put("startExpression", null);
            resultMap.put("startNextUserExpression", "${leader==" + deptPerson.get(0).getDeptPerson() + "}");
            resultMap.put("isSelected", "0");
            resultMap.put("selectField", null);
        }
        return AjaxResult.success(resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult processSubmit(String code, String businessKey, String version, Map<String, Object> variables) {
        String loginUser = CustomerBizInterceptor.currentUserThread.get().getUserId();
        identityService.setAuthenticatedUserId(loginUser);
        dynamicTableName(code);
        //????????????????????????
        List<Map<String, Object>> bizNos = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("id", businessKey));
        CustomerStrategyUtil.verificationNull(bizNos, CustomerBusinessEnum.BIZ_DATA_NOT_EXIST);
        //????????????
        log.info("start processSubmit.Transactional.code:" + code + ",businessKey:" + businessKey);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(code, bizNos.get(0).get(RedundancyFieldEnum.extra1.getName()).toString(), variables);
        //???????????????ID?????????????????????
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        customerFormMapper.setInstanceId(instance.getId(), businessKey);
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        log.info("start processSubmit.Transactional.code:" + code + ",businessKey:" + businessKey + ",bizNos:" + bizNos);
        FormStatusActivityNode formStatusActivityNode = formStatusActivityNodeService.getOne(Wrappers.<FormStatusActivityNode>query().eq(FormStatusActivityNode.COL_ACTIVITY_NODE_ID, task.getTaskDefinitionKey()).eq(FieldActivityNode.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
        if (!Optional.ofNullable(formStatusActivityNode).isPresent()) {
            throw new BusinessException("??????????????????" + task.getName() + "??????????????????~~");
        }
        String formStatus = formStatusActivityNode.getBizStatusName();
        customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", businessKey).set("status", formStatus).set(RedundancyFieldEnum.extra3.name,task.getId()).set("worker_order_current_handled_by", iChangePersonService.list(Wrappers.<ChangeDeptPersonEntity>query().eq("dept_id", CustomerBizInterceptor.currentUserThread.get().getOrgId())).get(0).getDeptPerson()));
        MybatisPlusConfig.customerTableName.remove();
        log.info("start processSubmit.Transactional.insertIncidentSubEvent.code:" + code + ",businessKey:" + businessKey + ",bizNos:" + bizNos);
        log.info("processSubmit-code:" + code);
        return AjaxResult.success();
    }


    @Override
    public AjaxResult approvalPopUp(String code, String processId, Integer id, String type, String version) {
        List<Map<String, Object>> formDataJson = new ArrayList<>();
        List<String> formKeys = new ArrayList<>();
        //??????????????????
        List<Map<String, Object>> approveButtonList = new ArrayList<>();
        //??????????????????
        List<Map<String, Object>> actionButtonList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        dynamicTableName(code);
        List<Map<String, Object>> baseBizData = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name, STATUS).eq(ID, id));
        if (CollectionUtils.isEmpty(baseBizData)) {
            throw new BusinessException("?????????????????????!");
        }
        String status = baseBizData.get(0).get("status").toString();
        // ????????????id??????task??????????????????????????????????????????????????????????????????????????????
        List<Task> taskList = new ArrayList<>();
        if (StringUtils.isEmpty(processId)) {
            type = "1";
        } else {
            taskList = taskService.createTaskQuery().processInstanceId(processId).list();
            if (code.equals(WorkOrderInformation.incident.getCode()) && CollectionUtils.isEmpty(taskList)) {
                type = "3";
            }
        }
        if ("1".equals(type)) {
            result = customerFormService.getFormInfo(code, id, processId);
            if (StringUtils.isEmpty(processId)) {
                //?????????????????????????????????
                Map<String, Object> saveButton = new HashMap<>();
                saveButton.put("buttonName", CustomerFlowConstants.addButtonName);
                saveButton.put("buttonUrlPath", CustomerFlowConstants.addButtonPath);
                actionButtonList.add(saveButton);
                Map<String, Object> submitButton = new HashMap<>();
                submitButton.put("buttonName", CustomerFlowConstants.submitApplyButtonName);
                submitButton.put("buttonUrlPath", CustomerFlowConstants.submitApplyButtonPath);
                actionButtonList.add(submitButton);
                result.put("actionButtonList", actionButtonList);
                result.put("approveButtonList", null);
                return AjaxResult.success(result);
            } else {
                Map<String, Object> showHistoryPng = new HashMap<>();
                showHistoryPng.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                showHistoryPng.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                actionButtonList.add(showHistoryPng);
                result.put("actionButtonList", actionButtonList);
                result.put("approveButtonList", null);
                return AjaxResult.success(result);
            }

        } else if ("3".equals(type)) {
            result = customerFormService.getFormInfo(code, id, processId);
            Map<String, Object> showHistoryPng = new HashMap<>();
            if (StringUtils.isNotEmpty(processId)) {
                showHistoryPng.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                showHistoryPng.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                actionButtonList.add(showHistoryPng);
            }
            result.put("actionButtonList", actionButtonList);
            result.put("approveButtonList", null);
            result.put("formCodes", CustomerFlowConstants.DICTIONARY);
            return AjaxResult.success(result);
        }
        //????????????????????????????????????  ????????????????????????
        Task currentTaskNode = taskList.get(0);
        //????????????????????????
        buildButtonInfos(code, processId, approveButtonList, actionButtonList, result, currentTaskNode, id);

        //???????????????????????????????????????????????????????????????
        TaskFormData currentTaskFormInfo = formService.getTaskFormData(currentTaskNode.getId());
        //??????????????????????????????
        buildPopJsonInfos(code, version, processId, formDataJson, formKeys, result, currentTaskNode, currentTaskFormInfo);
        // ?????????,??????????????????????????????,???????????????????????????????????????????????????
        Map<String, Object> taskFlagMap = new HashMap<>();
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(result);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult complete(String code, String taskId, String instanceId, String statusStr, CustomerVo customerVo) {
        Task t = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (ObjectUtil.isEmpty(t)) {
            throw new BusinessException("????????????????????????????????????????????????????????????!");
        }
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        //???????????????????????????
        dynamicTableName(code);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("instance_id",instanceId);
        List<Map<String, Object>> oldValue = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq("instance_id", instanceId));
        //????????????????????????????????????  ?????????????????????????????????  ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (WorkOrderInformation.workerOrder.getCode().equals(code)&&t.getName().equals("????????????")&&oldValue.get(0).get("worker_order_review_flag").equals("1")){
            if (customerVo.getVariables().get("recode").equals("0")){
                throw new BusinessException("??????????????????????????????????????????~");
            }
        }

        taskService.addComment(taskId, instanceId, statusStr);
        // ??????????????????????????????
        // p.s. ??????????????????????????? resolved ????????????????????????
        // ????????? complete ??????????????? resolved
        // resolveTask() ?????? claim() ??????????????? act_hi_taskinst ?????? assignee ???????????? null
        // resolveTask() ?????? claim() ??????????????? act_hi_taskinst ?????? assignee ???????????? null
        taskService.resolveTask(taskId, customerVo.getVariables());
        // ?????????????????????act_hi_taskinst ?????? assignee ??????????????? null
        taskService.claim(taskId, CustomerBizInterceptor.currentUserThread.get().getUserId());
        taskService.complete(taskId, customerVo.getVariables());
        //?????????????????????????????????????????????1 ?????????1??????????????????????????? ?????????????????????????????????
        //???????????????????????????????????? ????????????????????????
        String nextFormStatus = "";
        String nextTaskId = "";
        String nextTaskHandle = "";
        List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).list();
        if (CollectionUtil.isNotEmpty(list)) {
            FormStatusActivityNode formStatusActivityNode = formStatusActivityNodeService.getOne(Wrappers.<FormStatusActivityNode>query().eq(FormStatusActivityNode.COL_ACTIVITY_NODE_ID, list.get(0).getTaskDefinitionKey()).eq(FormStatusActivityNode.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
            //A???????????????????????????B??????   B???????????????
            nextFormStatus = Optional.ofNullable(formStatusActivityNode).map(FormStatusActivityNode::getBizStatusName).orElse("");
            if (list.get(0).getName().equals(t.getName())){
                nextFormStatus="?????????";
            }
            if (t.getName().equals("????????????")){
                nextFormStatus="????????????";
            }
            nextTaskId=list.get(0).getId();
            //???????????????????????????????????????leader?????? ?????????????????????????????????????????????????????????nextTaskHandle ?????????PopJson???????????????????????????applyUserId
            if (StringUtils.isNotEmpty(customerVo.getVariables().get("leader"))){
                nextTaskHandle=iChangePersonService.list(Wrappers.<ChangeDeptPersonEntity>query().eq("dept_id", ogUserService.selectOgUserByUserId(oldValue.get(0).get("created_by").toString()).getOrgId())).get(0).getDeptPerson();
            }else if (StringUtils.isNotEmpty(customerVo.getVariables().get("applicant"))){
                nextTaskHandle=customerVo.getVariables().get("applicant").toString();
            }else if (StringUtils.isNotEmpty(customerVo.getVariables().get("handle"))){
                nextTaskHandle=customerVo.getVariables().get("handle").toString();
            }else if (StringUtils.isNotEmpty(customerVo.getVariables().get("checker"))){
                nextTaskHandle=customerVo.getVariables().get("checker").toString();
            }
        } else {
            nextFormStatus = "?????????";
        }
        if (WorkOrderInformation.workerOrder.getCode().equals(code)){
            customerVo.getCustomerFormContent().getFields().put("worker_order_current_handled_group","");
        }
        customerVo.getCustomerFormContent().getFields().put("worker_order_current_handled_by",nextTaskHandle);
        customerVo.getCustomerFormContent().getFields().put("status",nextFormStatus);
        customerVo.getCustomerFormContent().getFields().put(RedundancyFieldEnum.extra3.name,nextTaskId);
        customerVo.getCustomerFormContent().getFields().put("updated_time",new Date());
        //????????????
        this.insertOrUpdate( code, customerVo.getCustomerFormContent());
        //??????????????????????????????????????????
        if (nextTaskHandle.equals(CustomerBizInterceptor.currentUserThread.get().getUserId())){
            resultMap.put("isContinuePopFlag", true);
            oldValue.forEach(a->{
                a.put("taskId",customerVo.getCustomerFormContent().getFields().get(RedundancyFieldEnum.extra3.name));
            });
            resultMap.put("records", oldValue);
        }else {
            resultMap.put("isContinuePopFlag", false);
        }
        MybatisPlusConfig.customerTableName.remove();

        //?????????????????????????????? ??????????????????????????????????????? ???????????????????????????  ????????????????????????????????????
        if ("?????????".equals(nextFormStatus)&&oldValue.get(0).get("worker_order_rules_type").toString().equals("single")){
            dynamicTableName(WorkOrderInformation.workerOrder.getCode());
            CustomerFormContent workerOrderInfo=new CustomerFormContent();
            Map<String,Object> fieldMap=new HashMap<>();
            workerOrderInfo.setFields(fieldMap);
            workerOrderInfo.getFields().put("created_by",oldValue.get(0).get("created_by"));
            workerOrderInfo.getFields().put("updated_by",oldValue.get(0).get("updated_by"));
            workerOrderInfo.getFields().put("worker_order_desc",oldValue.get(0).get("worker_order_desc"));
            workerOrderInfo.getFields().put("worker_order_type",oldValue.get(0).get("worker_order_type"));
            workerOrderInfo.getFields().put("worker_order_dept",oldValue.get(0).get("worker_order_dept"));
            workerOrderInfo.getFields().put("worker_order_review_flag",oldValue.get(0).get("worker_order_review_flag"));
            workerOrderInfo.getFields().put("worker_order_start_time",oldValue.get(0).get("worker_order_start_time"));
            workerOrderInfo.getFields().put("worker_order_end_time",oldValue.get(0).get("worker_order_end_time"));
            String orgId = ogUserService.selectOgUserByUserId(oldValue.get(0).get("created_by").toString()).getOrgId();
            workerOrderInfo.getFields().put("worker_order_current_handled_group",orgId);
            //TODO  ????????????????????????????????????????????????JSON??????????????????????????????????????????????????????????????????????????????  ???????????????????????????????????????
            workerOrderInfo.getFields().put("worker_order_files",oldValue.get(0).get("worker_order_files"));
            //????????????????????????JSON
            DesignFormVersion designFormVersion = designFormVersionMapper.selectOne(Wrappers.<DesignFormVersion>query().eq("code", String.format("%s_%s", bizTablePrefix, WorkOrderInformation.workerOrder.getCode())).eq(DesignFormVersion.COL_IS_CURRENT, "1"));
            workerOrderInfo.setDataJson(VueDataJsonUtil.analysisDataJson(designFormVersion.getJson(), workerOrderInfo.getFields()));
            workerOrderInfo.setCode(WorkOrderInformation.workerOrder.getCode());
            AjaxResult insertResult = this.insertOrUpdate(WorkOrderInformation.workerOrder.getCode(), workerOrderInfo);
            //????????????
            //?????????????????????????????????
            Map<String,Object> startMap=new HashMap<>();
            startMap.put("receiver",orgId);
            this.processSubmit(WorkOrderInformation.workerOrder.getCode(),insertResult.get("data").toString(),"",startMap);
        }
        return AjaxResult.success(resultMap);
    }


    @Override
    public AjaxResult list(String code, String type, Page page) {
        //   ??????????????????ID??????????????????
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        if (ObjectUtil.isEmpty(currentTableInfo)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getDesc());
        }
        //??????????????????ID???????????????????????????????????????
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        if (CollectionUtil.isEmpty(formFieldInfos)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getDesc());
        }
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper queryWrapper=new QueryWrapper();
        dynamicTableName(code);
        if ("1".equals(type)){
            queryWrapper.select("*").eq("created_by",CustomerBizInterceptor.currentUserThread.get().getUserId());
        }else if ("2".equals(type)) {
            queryWrapper.select("*").eq("worker_order_current_handled_by", CustomerBizInterceptor.currentUserThread.get().getUserId());
            queryWrapper.or();
            queryWrapper.eq("worker_order_current_handled_group",CustomerBizInterceptor.currentUserThread.get().getOrgId());
        }
        Page page1 = customerFormMapper.selectMapsPage(page, queryWrapper);
        List<Map<String, Object>> records = (List<Map<String, Object>>) page1.getRecords();
        records.forEach(a->{
            a.put("taskId",a.get(RedundancyFieldEnum.extra3.name));
        });
        page1.setRecords(records);
        //?????????????????????????????????
        List<Map<String, Object>> formVersions = customerFormMapper.getCurrentFormAllVersions(String.format("%s_%s", bizTablePrefix, code));
        resultMap.put("pageListInfo", page1);
        resultMap.put("fieldInfo", formFieldInfos);
        resultMap.put("formVersionInfo", formVersions);
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(resultMap);
    }

    private void buildButtonInfos(String code, String
            processId, List<Map<String, Object>> approveButtonList, List<Map<String, Object>> actionButtonList, Map<String, Object> result, Task currentTaskNode, Integer id) {
        dynamicTableName(code);
        //????????????????????????????????????
        String applyUserId = customerFormMapper.selectApplyUser(processId);
        String processDefinitionId = runtimeService.createProcessInstanceQuery().processInstanceId(//
                        taskService.createTaskQuery().taskId(currentTaskNode.getId()).singleResult().getProcessInstanceId())//
                .singleResult().getProcessDefinitionId();
        //??????bpmnModel??????
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        //?????????????????????????????????
        FlowNode currentNodeObject = (FlowNode) bpmnModel.getFlowElement(currentTaskNode.getTaskDefinitionKey());
        List<SequenceFlow> currentNodeOutgoingFlows = currentNodeObject.getOutgoingFlows();
        for (SequenceFlow a : currentNodeOutgoingFlows) {
            //?????????????????????????????????
            FlowElement targetFlowElement = a.getTargetFlowElement();
            //?????????????????????????????????  ???????????????????????????????????????????????????????????????
            if (targetFlowElement instanceof UserTask) {
                Map<String, Object> map = new HashMap<>();
                // assignee????????????????????????EL?????????   agentUserId??????????????????????????? ????????????????????????????????????  ??????????????????????????????EL???????????????  //?????????????????????????????????????????????????????????EL????????????
                String assignee = ((UserTask) targetFlowElement).getAssignee();
                if (StringUtils.isEmpty(assignee)) {
                    List<String> candidateUsers = ((UserTask) targetFlowElement).getCandidateUsers();
                    assignee = CollectionUtil.isNotEmpty(candidateUsers) ? (candidateUsers.get(0).contains("$") ? candidateUsers.get(0) : "") : (StringUtils.isNotEmpty(assignee) ? (assignee.contains("$") ? assignee : "") : "");
                    if (StringUtils.isEmpty(assignee)) {
                        List<String> candidateGroups = ((UserTask) targetFlowElement).getCandidateGroups();
                        assignee = CollectionUtil.isNotEmpty(candidateGroups) ? (candidateGroups.get(0).contains("$") ? candidateGroups.get(0) : "") : (StringUtils.isNotEmpty(assignee) ? (assignee.contains("$") ? assignee : "") : "");

                    }
                }
                String name = currentTaskNode.getName();
                //????????????????????????????????????????????????????????????????????????EL??????????????????  ????????????????????????  ???????????????????????????EL?????????????????????key
                //???????????????????????????????????????????????????  ???????????????????????????????????? ?????????????????????  ???B??????????????????????????????????????????  ???????????????????????????????????????????????????????????????
                //??????????????????????????????/???????????????????????????complete????????????  {"key": "value"}
                if (StringUtils.isNotEmpty(assignee)) {
                    if ("????????????".equals(((UserTask) targetFlowElement).getName())){
                        applyUserId=CustomerBizInterceptor.currentUserThread.get().getUserId();
                    }
                    map.put("agentExpression", assignee);
                    map.put("agentUserId", applyUserId);
                    map.put("expression", null);
                    map.put("name", name);
                } else {
                    map.put("agentExpression", null);
                    map.put("agentUserId", null);
                    map.put("expression", null);
                    map.put("name", name);
                }
                approveButtonList.add(map);
            }
            if (targetFlowElement instanceof Gateway) {
                List<SequenceFlow> targetOutgoingFlows = ((Gateway) targetFlowElement).getOutgoingFlows();
                for (SequenceFlow gatewayFlow : targetOutgoingFlows) {
                    Map<String, Object> map = new HashMap<>();
                    Map<String, Object> obj = buttonActionMapper.getObj(gatewayFlow.getId());
                    buildButtonInfo(obj, map);
                    FlowElement targetFlowElement1 = gatewayFlow.getTargetFlowElement();
                    if (targetFlowElement1 instanceof UserTask) {
                        //????????????????????????????????????  ??????????????????????????????EL???????????????  //?????????????????????????????????????????????????????????EL????????????
                        String assignee = ((UserTask) targetFlowElement1).getAssignee();
                        if (StringUtils.isEmpty(assignee)) {
                            List<String> candidateUsers = ((UserTask) targetFlowElement1).getCandidateUsers();
                            assignee = CollectionUtil.isNotEmpty(candidateUsers) ? (candidateUsers.get(0).contains("$") ? candidateUsers.get(0) : "") : (StringUtils.isNotEmpty(assignee) ? (assignee.contains("$") ? assignee : "") : "");
                            if (StringUtils.isEmpty(assignee)) {
                                List<String> candidateGroups = ((UserTask) targetFlowElement1).getCandidateGroups();
                                assignee = CollectionUtil.isNotEmpty(candidateGroups) ? (candidateGroups.get(0).contains("$") ? candidateGroups.get(0) : "") : (StringUtils.isNotEmpty(assignee) ? (assignee.contains("$") ? assignee : "") : "");

                            }
                        }
                        if (StringUtils.isNotEmpty(assignee)) {
                            if (currentTaskNode.getName().equals("????????????")&&"????????????".equals(((UserTask) targetFlowElement).getName())){
                                //??????????????????
                               applyUserId=(String)runtimeService.getVariable(currentTaskNode.getExecutionId(),"handle");
                            }
                            map.put("agentExpression", assignee);
                            map.put("agentUserId", applyUserId);
                            map.put("expression", gatewayFlow.getConditionExpression());//?????????
                            map.put("name", gatewayFlow.getName());
                        } else {
                            map.put("agentExpression", null);
                            map.put("agentUserId", null);
                            map.put("expression", gatewayFlow.getConditionExpression());
                            map.put("name", gatewayFlow.getName());

                        }
                    } else {
                        map.put("agentExpression", null);
                        map.put("agentUserId", null);
                        map.put("expression", gatewayFlow.getConditionExpression());
                        map.put("name", gatewayFlow.getName());
                    }

                    approveButtonList.add(map);
                }
            }
            if (targetFlowElement instanceof EndEvent) {
                Map<String, Object> map = new HashMap<>();
                map.put("agentExpression", null);
                map.put("agentUserId", null);
                map.put("expression", null);
                map.put("name", currentTaskNode.getName());
                approveButtonList.add(map);
            }
        }
        Map<String, Object> showHistoryPng = new HashMap<>();
        showHistoryPng.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
        showHistoryPng.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
        Map<String, Object> saveButton = new HashMap<>();
        saveButton.put("buttonName", CustomerFlowConstants.addButtonName);
        saveButton.put("buttonUrlPath", CustomerFlowConstants.addButtonPath);
        actionButtonList.add(showHistoryPng);
        actionButtonList.add(saveButton);
        result.put("approveButtonList", approveButtonList);
        result.put("actionButtonList", actionButtonList);
    }


    private void buildButtonInfo(Map<String, Object> obj, Map<String, Object> map) {
        Map<String, Object> buttonActionResult = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        if (ObjectUtil.isNotNull(obj)) {
            Map<String, Object> buttonJsonMap = new HashMap<>();
            buttonJsonMap.put("prop", "approved_opinion");
            buttonJsonMap.put("val", "");
            buttonActionResult.put("popJson", obj.get("json"));
            list.add(buttonJsonMap);
        } else {
            return;
        }
        buttonActionResult.put("buttonActionList", list);
        map.put("buttonAction", buttonActionResult);
    }


    private void buildPopJsonInfos(String code, String version, String processId, List<Map<String, Object>> formDataJson, List<String> formKeys, Map<String, Object> result, Task
            currentTaskNode, TaskFormData currentTaskFormInfo) {
        String loginUser = CustomerBizInterceptor.currentUserThread.get().getUserId();
        formKeys.add(code);
        //???????????????????????????????????????????????????A?????????????????????????????????????????????????????????  ???????????????????????????
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processId)
                .finished()
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();
        //????????????????????????????????????key?????????
        historicTaskInstanceList.stream().filter(a -> {
            return StringUtils.isNotEmpty(a.getFormKey());
        });
        historicTaskInstanceList.stream().forEach(a -> {
            formKeys.add(a.getFormKey());
        });

        String currentFormKey = Optional.ofNullable(currentTaskFormInfo).map(TaskFormData::getFormKey).orElse("");
        formKeys.add(currentFormKey);
        List<String> distinctFormKey = formKeys.stream().filter(a -> {
            return StringUtils.isNotEmpty(a);
        }).distinct().collect(Collectors.toList());
        distinctFormKey.stream().forEach(a -> {
            Map<String, Object> resultMap = new HashMap<>();
            dynamicTableName(a);
            Integer approveBusinessKey = customerFormMapper.selectOneByProcessId(processId);
            //???????????????????????????????????????
            List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("status").eq("instance_id", processId));
            // ??????design_form_version???????????????ID
            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, a), StringUtils.isEmpty(version) ? null : Integer.valueOf(version));

            //?????????????????????
            if (code.equals(a)) {
                //?????????????????????????????????????????????
                List<FormStepStatus> formStepStatusList = formStepStatusService.list(Wrappers.<FormStepStatus>query().select(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")).groupBy(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).orderByAsc(FormStepStatus.COL_SORT));

                FormStepStatus formStepStatus = formStepStatusService.getOne(Wrappers.<FormStepStatus>query().eq(FormStepStatus.COL_BIZ_STATUS_NAME, list.get(0).get("status")).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
                Map<String, Object> formStepStatusMap = new HashMap<>();
                formStepStatusMap.put("formStepStatusList", formStepStatusList);
                if (ObjectUtil.isEmpty(formStepStatus)){
                    formStepStatusMap.put("currenFormStep", "??????");
                }else {
                    formStepStatusMap.put("currenFormStep", formStepStatus.getStepName());
                }
                Map<String, String> formStatus = new HashMap<>();
                formStatus.put("lable", "????????????");
                formStatus.put("index", "lastIndex");
                formStatus.put("value", Optional.ofNullable(list).isPresent() ? list.get(0).get("status").toString() : "");


                String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
                //?????????????????????
                List<Map<String, Object>> bizNos = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("instance_id", processId));

                Map<String, String> bizNoMap = new HashMap<>();
                bizNoMap.put("lable", formName + "??????");
                bizNoMap.put("index", "firstIndex");
                bizNoMap.put("value", Optional.ofNullable(bizNos).isPresent() ? bizNos.get(0).get(RedundancyFieldEnum.extra1.name).toString() : "");

                List<Map<String, String>> appenJsondata = new ArrayList<>();
                appenJsondata.add(formStatus);
                appenJsondata.add(bizNoMap);
                result.put("appenJsondata", appenJsondata);
                result.put("formStepInfo", formStepStatusMap);
            }


            if (a.equals(currentTaskFormInfo.getFormKey()) || (StringUtils.isEmpty(currentTaskFormInfo.getFormKey()) && a.equals(code))) {
                resultMap.put("isCurrentTaskNode", true);
            } else {
                resultMap.put("isCurrentTaskNode", false);
            }
            if (Optional.ofNullable(approveBusinessKey).isPresent()) {
                //????????????????????????json
                DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                        .eq(DesignBizJsonData.COL_BIZ_ID, approveBusinessKey)
                        .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, a)));
                resultMap.put("formId", currentNodeFormInfo.getBizId());
                resultMap.put("formJson", currentNodeFormInfo.getJsonData());
                resultMap.put("formCode", a);
                //??????activity?????????sid???????????????id??????????????????????????????????????????
                List<FieldActivityNode> fieldActivityNodes = fieldActivityNodeService.list(Wrappers.<FieldActivityNode>query()
                        .eq(FieldActivityNode.COL_FORM_VERSION_ID, Long.valueOf(currentTableInfo.get("id")))
                        .eq(FieldActivityNode.COL_ACTIVITY_NODE_ID, currentTaskNode.getTaskDefinitionKey()));
                resultMap.put("fieldActivityStatus", fieldActivityNodes);
            } else {
                //??????????????????ID????????????json??????
                Map<String, Object> formJsonInfo = customerFormMapper.getFormJsonInfo(currentTableInfo.get("id"));
                resultMap.put("formId", null);
                resultMap.put("formJson", formJsonInfo.get("json"));
                resultMap.put("formCode", a);
                resultMap.put("fieldActivityStatus", null);
            }
            resultMap.put("goFlag", false);
            formDataJson.add(resultMap);
            MybatisPlusConfig.customerTableName.remove();
        });
        result.put("jsonData", formDataJson);
    }
}
