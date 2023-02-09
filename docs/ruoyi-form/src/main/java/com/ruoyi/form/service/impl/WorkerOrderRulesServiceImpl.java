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
 * @Description 工单规则实体类
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
            //根据表单版本ID获取当前表单当前版本的字段
            List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
            //获取表名的中文名
            String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
            //过滤需要记录数值变更的字段
            List<Map<String, String>> recordDataChanged = formFieldInfos.stream().filter(map -> String.valueOf(map.get("record_data_changed")).equals("1")).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(recordDataChanged)) {
                String selectKey = recordDataChanged.stream().map(a -> a.get("name")).collect(Collectors.joining(",")) + "," + RedundancyFieldEnum.extra1.name;
                //获取原有字段值
                Map<String, Object> oldValue = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(selectKey).eq("id", bizId)).get(0);
                List<OperationDetails> operationDetails = new ArrayList<>();
                //在新的key-value中筛选出需要记录变更的字段值
                for (Map.Entry<String, Object> oldEntry : oldValue.entrySet()) {
                    //不处理编号
                    if (oldEntry.getKey().equals(RedundancyFieldEnum.extra1.name)) {
                        continue;
                    }
                    //处理附件
                    if (oldEntry.getKey().contains("file")) {
                        //判断文件是否相等
                        if (oldEntry.getValue() != null && !oldEntry.getValue().equals(customerFormContent.getFields().get(oldEntry.getKey()))) {
                            List<String> oldFiles = JSONObject.parseArray(String.valueOf(oldEntry.getValue()), String.class);
                            List<String> newFiles = JSONObject.parseArray(String.valueOf(customerFormContent.getFields().get(oldEntry.getKey())), String.class);
                            if (oldFiles == null || newFiles == null) {
                                continue;
                            }
                            //判断是否有新增文件
                            String newInsertFiles = newFiles.stream().filter(a -> !oldFiles.contains(a)).collect(Collectors.joining("、"));
                            //判断是否有删除文件
                            String oldDeleteFiles = oldFiles.stream().filter(a -> !newFiles.contains(a)).collect(Collectors.joining("、"));
                            String desc = CustomerBizInterceptor.currentUserThread.get().getPname();
                            if (!StringUtils.isEmpty(newInsertFiles)) {
                                desc = desc + "上传了：" + newInsertFiles + "文件";
                            } else if (!oldDeleteFiles.isEmpty()) {
                                desc = desc + "删除了：" + oldDeleteFiles + "文件";
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
                                .description(CustomerBizInterceptor.currentUserThread.get().getPname() + "将字段：" + recordDataChanged.stream().filter(a -> a.get("name").equals(oldEntry.getKey())).findFirst().get().get("description") + "的值从：" + oldEntry.getValue() + " 变更为：" + customerFormContent.getFields().get(oldEntry.getKey()))
                                .build();
                        operationDetails.add(op);
                    }
                }

                //记录操作记录
                operationDetailsService.saveBatch(operationDetails);
            }
            //走修改 把通用字段中的修改时间及修改人添加至字段中
            customerFormContent.getFields().put("updated_time", DateUtils.getNowDate());
            customerFormContent.getFields().put("updated_by", CustomerBizInterceptor.currentUserThread.get().getUserId());
            resultItem = customerFormMapper.updateById(customerFormContent);
            designBizJsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", bizTablePrefix, code)).bizId(bizId).jsonData(customerFormContent.getDataJson()).build();
            designBizJsonDataService.update(designBizJsonData, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_ID, bizId).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
        } else {
            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
            String loginUser = CustomerBizInterceptor.currentUserThread.get().getUserId();
            //走添加 把通用字段中的创建时间、修改时间、创建人、修改人添加至字段中
            customerFormContent.getFields().put("created_time", DateUtils.getNowDate());
            if (!WorkOrderInformation.workerOrder.getCode().equals(code)){
                customerFormContent.getFields().put("created_by", loginUser);
                customerFormContent.getFields().put("updated_by", loginUser);
            }
            customerFormContent.getFields().put("updated_time", DateUtils.getNowDate());
            customerFormContent.getFields().put("status", "待提交");
            customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, generatorService.createNoAsLength("GD", 3));
            customerFormContent.getFields().put(RedundancyFieldEnum.extra2.name, currentTableInfo.get("version"));
            resultItem = customerFormMapper.insert(customerFormContent);
            designBizJsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", bizTablePrefix, code)).bizId(customerFormContent.getId()).jsonData(customerFormContent.getDataJson()).build();
            designBizJsonDataService.save(designBizJsonData);
            bizId = customerFormContent.getId();
            //根据表单版本ID获取当前表单当前版本的字段
            List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
            //获取表名的中文名
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
        //获取业务流程编号
        List<Map<String, Object>> bizNos = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("id", businessKey));
        CustomerStrategyUtil.verificationNull(bizNos, CustomerBusinessEnum.BIZ_DATA_NOT_EXIST);
        //启动流程
        log.info("start processSubmit.Transactional.code:" + code + ",businessKey:" + businessKey);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(code, bizNos.get(0).get(RedundancyFieldEnum.extra1.getName()).toString(), variables);
        //把流程实例ID设置到业务表中
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        customerFormMapper.setInstanceId(instance.getId(), businessKey);
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        log.info("start processSubmit.Transactional.code:" + code + ",businessKey:" + businessKey + ",bizNos:" + bizNos);
        FormStatusActivityNode formStatusActivityNode = formStatusActivityNodeService.getOne(Wrappers.<FormStatusActivityNode>query().eq(FormStatusActivityNode.COL_ACTIVITY_NODE_ID, task.getTaskDefinitionKey()).eq(FieldActivityNode.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
        if (!Optional.ofNullable(formStatusActivityNode).isPresent()) {
            throw new BusinessException("此工单没有在" + task.getName() + "节点配置状态~~");
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
        //审批按钮集合
        List<Map<String, Object>> approveButtonList = new ArrayList<>();
        //动作按钮集合
        List<Map<String, Object>> actionButtonList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        dynamicTableName(code);
        List<Map<String, Object>> baseBizData = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name, STATUS).eq(ID, id));
        if (CollectionUtils.isEmpty(baseBizData)) {
            throw new BusinessException("表单数据不存在!");
        }
        String status = baseBizData.get(0).get("status").toString();
        // 根据流程id查询task任务不存在则标识流程已结束或已取消，此时查询已办内容
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
                //构造没有走到流程中信息
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
        //获取当前任务节点的流条件  用于前端生成按钮
        Task currentTaskNode = taskList.get(0);
        //构造弹窗按钮信息
        buildButtonInfos(code, processId, approveButtonList, actionButtonList, result, currentTaskNode, id);

        //获取当前流程实例当前任务节点的当前表单信息
        TaskFormData currentTaskFormInfo = formService.getTaskFormData(currentTaskNode.getId());
        //构造弹窗表单信息集合
        buildPopJsonInfos(code, version, processId, formDataJson, formKeys, result, currentTaskNode, currentTaskFormInfo);
        // 调查中,根因已明制定解决方案,解决中三个节点任务页签追加相关操作
        Map<String, Object> taskFlagMap = new HashMap<>();
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(result);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult complete(String code, String taskId, String instanceId, String statusStr, CustomerVo customerVo) {
        Task t = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (ObjectUtil.isEmpty(t)) {
            throw new BusinessException("流程任务已处理或不存在，请刷新列表并检查!");
        }
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        //获取原有业务数据值
        dynamicTableName(code);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("instance_id",instanceId);
        List<Map<String, Object>> oldValue = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq("instance_id", instanceId));
        //如果工单模块需要复核的话  需判断是否选择了复核人  为了防止页面数据错乱（例：表单填写了需要复核，但是在这一步用户点击不需要复核）
        if (WorkOrderInformation.workerOrder.getCode().equals(code)&&t.getName().equals("处理完成")&&oldValue.get(0).get("worker_order_review_flag").equals("1")){
            if (customerVo.getVariables().get("recode").equals("0")){
                throw new BusinessException("该流程需要复核，请选择复核人~");
            }
        }

        taskService.addComment(taskId, instanceId, statusStr);
        // 被委派人处理完成任务
        // p.s. 被委托的流程需要先 resolved 这个任务再提交。
        // 所以在 complete 之前需要先 resolved
        // resolveTask() 要在 claim() 之前，不然 act_hi_taskinst 表的 assignee 字段会为 null
        // resolveTask() 要在 claim() 之前，不然 act_hi_taskinst 表的 assignee 字段会为 null
        taskService.resolveTask(taskId, customerVo.getVariables());
        // 只有签收任务，act_hi_taskinst 表的 assignee 字段才不为 null
        taskService.claim(taskId, CustomerBizInterceptor.currentUserThread.get().getUserId());
        taskService.complete(taskId, customerVo.getVariables());
        //判断当前流程实例的任务数是否为1 如果为1则说明只剩一个任务 走完任务进入到下一节点
        //完成当前节点的审批后获取 此流程实例的任务
        String nextFormStatus = "";
        String nextTaskId = "";
        String nextTaskHandle = "";
        List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).list();
        if (CollectionUtil.isNotEmpty(list)) {
            FormStatusActivityNode formStatusActivityNode = formStatusActivityNodeService.getOne(Wrappers.<FormStatusActivityNode>query().eq(FormStatusActivityNode.COL_ACTIVITY_NODE_ID, list.get(0).getTaskDefinitionKey()).eq(FormStatusActivityNode.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
            //A节点审批完之后进入B节点   B节点的状态
            nextFormStatus = Optional.ofNullable(formStatusActivityNode).map(FormStatusActivityNode::getBizStatusName).orElse("");
            if (list.get(0).getName().equals(t.getName())){
                nextFormStatus="已转派";
            }
            if (t.getName().equals("工单复核")){
                nextFormStatus="复核退回";
            }
            nextTaskId=list.get(0).getId();
            //如果规则工单的审批表达式为leader的话 则查询单子创建人的所在部室领导并赋值给nextTaskHandle 因为在PopJson接口中默认的值都为applyUserId
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
            nextFormStatus = "已生效";
        }
        if (WorkOrderInformation.workerOrder.getCode().equals(code)){
            customerVo.getCustomerFormContent().getFields().put("worker_order_current_handled_group","");
        }
        customerVo.getCustomerFormContent().getFields().put("worker_order_current_handled_by",nextTaskHandle);
        customerVo.getCustomerFormContent().getFields().put("status",nextFormStatus);
        customerVo.getCustomerFormContent().getFields().put(RedundancyFieldEnum.extra3.name,nextTaskId);
        customerVo.getCustomerFormContent().getFields().put("updated_time",new Date());
        //更新数据
        this.insertOrUpdate( code, customerVo.getCustomerFormContent());
        //判断审批完后页面是否继续审批
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

        //规则工单结束完流程后 判断工单生成规则是否为单次 单次在这里生成工单  多次通过定时任务生成工单
        if ("已生效".equals(nextFormStatus)&&oldValue.get(0).get("worker_order_rules_type").toString().equals("single")){
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
            //TODO  这里的附件其实应该是从工单规则的JSON中获取响应的模块然后把相关的数据填充到工单流程模块中  不然工单模块无法下载和展示
            workerOrderInfo.getFields().put("worker_order_files",oldValue.get(0).get("worker_order_files"));
            //获取工单表单样式JSON
            DesignFormVersion designFormVersion = designFormVersionMapper.selectOne(Wrappers.<DesignFormVersion>query().eq("code", String.format("%s_%s", bizTablePrefix, WorkOrderInformation.workerOrder.getCode())).eq(DesignFormVersion.COL_IS_CURRENT, "1"));
            workerOrderInfo.setDataJson(VueDataJsonUtil.analysisDataJson(designFormVersion.getJson(), workerOrderInfo.getFields()));
            workerOrderInfo.setCode(WorkOrderInformation.workerOrder.getCode());
            AjaxResult insertResult = this.insertOrUpdate(WorkOrderInformation.workerOrder.getCode(), workerOrderInfo);
            //提交申请
            //构建提交申请时的流条件
            Map<String,Object> startMap=new HashMap<>();
            startMap.put("receiver",orgId);
            this.processSubmit(WorkOrderInformation.workerOrder.getCode(),insertResult.get("data").toString(),"",startMap);
        }
        return AjaxResult.success(resultMap);
    }


    @Override
    public AjaxResult list(String code, String type, Page page) {
        //   获取表单版本ID、表单版本号
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        if (ObjectUtil.isEmpty(currentTableInfo)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getDesc());
        }
        //根据表单版本ID获取当前表单当前版本的字段
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
        //查询当前表单的所有版本
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
        //获取当前流程任务的发起人
        String applyUserId = customerFormMapper.selectApplyUser(processId);
        String processDefinitionId = runtimeService.createProcessInstanceQuery().processInstanceId(//
                        taskService.createTaskQuery().taskId(currentTaskNode.getId()).singleResult().getProcessInstanceId())//
                .singleResult().getProcessDefinitionId();
        //获取bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        //获取当前流程里面主进程
        FlowNode currentNodeObject = (FlowNode) bpmnModel.getFlowElement(currentTaskNode.getTaskDefinitionKey());
        List<SequenceFlow> currentNodeOutgoingFlows = currentNodeObject.getOutgoingFlows();
        for (SequenceFlow a : currentNodeOutgoingFlows) {
            //获取下一节点的目标对象
            FlowElement targetFlowElement = a.getTargetFlowElement();
            //如果下一节点是用户任务  则查找下一节点是否动态配置了候选人或代理人
            if (targetFlowElement instanceof UserTask) {
                Map<String, Object> map = new HashMap<>();
                // assignee为流程图中配置的EL表达式   agentUserId为表达式的具体的值 获取下一节点任务的代理人  代理人和候选人全部用EL表达式表示  //判断当前任务节点的代理人和候选人是否为EL动态表示
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
                //如果动态配置候选人或代理人则把下一节点动态设置的EL表达式给前端  前端进行字符分割  在审批的时间会传入EL表达式中配置的key
                //目前暂时考虑退回到申请人的业务场景  查出来改单子的申请人信息 将值返回给前端  在B节点审批不通过退回给申请人时  前端就能拿到相应的数据进行参数封装传递后端
                //将流条件和动态候选人/代理人作为参数调用complete审批接口  {"key": "value"}
                if (StringUtils.isNotEmpty(assignee)) {
                    if ("工单处理".equals(((UserTask) targetFlowElement).getName())){
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
                        //获取下一节点任务的代理人  代理人和候选人全部用EL表达式表示  //判断当前任务节点的代理人和候选人是否为EL动态表示
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
                            if (currentTaskNode.getName().equals("工单复核")&&"工单处理".equals(((UserTask) targetFlowElement).getName())){
                                //获取流程变量
                               applyUserId=(String)runtimeService.getVariable(currentTaskNode.getExecutionId(),"handle");
                            }
                            map.put("agentExpression", assignee);
                            map.put("agentUserId", applyUserId);
                            map.put("expression", gatewayFlow.getConditionExpression());//流条件
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
        //获取已完成的审批记录【流程走到节点A的时间会在历史表中添加一条历史任务数据  结束标识为未结束】
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processId)
                .finished()
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();
        //过滤任务节点没有配置表单key的元素
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
            //获取当前表单当前节点的状态
            List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("status").eq("instance_id", processId));
            // 获取design_form_version表中的主键ID
            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, a), StringUtils.isEmpty(version) ? null : Integer.valueOf(version));

            //构造单子进度条
            if (code.equals(a)) {
                //获取当前表单所有已配置的步骤条
                List<FormStepStatus> formStepStatusList = formStepStatusService.list(Wrappers.<FormStepStatus>query().select(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")).groupBy(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).orderByAsc(FormStepStatus.COL_SORT));

                FormStepStatus formStepStatus = formStepStatusService.getOne(Wrappers.<FormStepStatus>query().eq(FormStepStatus.COL_BIZ_STATUS_NAME, list.get(0).get("status")).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
                Map<String, Object> formStepStatusMap = new HashMap<>();
                formStepStatusMap.put("formStepStatusList", formStepStatusList);
                if (ObjectUtil.isEmpty(formStepStatus)){
                    formStepStatusMap.put("currenFormStep", "处理");
                }else {
                    formStepStatusMap.put("currenFormStep", formStepStatus.getStepName());
                }
                Map<String, String> formStatus = new HashMap<>();
                formStatus.put("lable", "工单状态");
                formStatus.put("index", "lastIndex");
                formStatus.put("value", Optional.ofNullable(list).isPresent() ? list.get(0).get("status").toString() : "");


                String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
                //获取业务表编号
                List<Map<String, Object>> bizNos = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("instance_id", processId));

                Map<String, String> bizNoMap = new HashMap<>();
                bizNoMap.put("lable", formName + "编号");
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
                //获取当前带数据的json
                DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                        .eq(DesignBizJsonData.COL_BIZ_ID, approveBusinessKey)
                        .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, a)));
                resultMap.put("formId", currentNodeFormInfo.getBizId());
                resultMap.put("formJson", currentNodeFormInfo.getJsonData());
                resultMap.put("formCode", a);
                //根据activity定义的sid和表单版本id查询该表单在该节点的字段状态
                List<FieldActivityNode> fieldActivityNodes = fieldActivityNodeService.list(Wrappers.<FieldActivityNode>query()
                        .eq(FieldActivityNode.COL_FORM_VERSION_ID, Long.valueOf(currentTableInfo.get("id")))
                        .eq(FieldActivityNode.COL_ACTIVITY_NODE_ID, currentTaskNode.getTaskDefinitionKey()));
                resultMap.put("fieldActivityStatus", fieldActivityNodes);
            } else {
                //根据表单版本ID获取表单json数据
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
