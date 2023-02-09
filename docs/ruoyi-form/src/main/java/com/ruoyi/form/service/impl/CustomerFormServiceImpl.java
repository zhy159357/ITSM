package com.ruoyi.form.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.activiti.config.ICustomProcessDiagramGenerator;
import com.ruoyi.activiti.config.WorkflowConstants;
import com.ruoyi.activiti.domain.HistoricActivity;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.mapper.PubFlowLogMapper;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.activiti.service.IProcessService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.common.annotation.CustomerButton;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.ChangeUtil;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.constants.EventFieldConstants;
import com.ruoyi.form.constants.TinywebConstants;
import com.ruoyi.form.controller.customerForm.DeleteMultiInstanceCmd;
import com.ruoyi.form.domain.*;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.mapper.ButtonActionMapper;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignBizChmMapper;
import com.ruoyi.form.mapper.RelationLogMapper;
import com.ruoyi.form.service.*;
import com.ruoyi.form.util.FormUtil;
import com.ruoyi.form.util.GeneralQueryUtil;
import com.ruoyi.form.util.IDCodeConvertChineseUtil;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.form.vo.FormInfoButtonVo;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.mapper.PubParaMapper;
import com.ruoyi.system.mapper.PubParaValueMapper;
import com.ruoyi.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DateUtils.YYYY_MM_DD;
import static com.ruoyi.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;
import static com.ruoyi.form.constants.ProblemConstant.*;
import static com.ruoyi.form.constants.ProblemConstant.ForError.ONE;
import static com.ruoyi.form.constants.ProblemConstant.ForError.ZERO;
import static com.ruoyi.form.constants.ProblemConstant.ProblemSource.EVENT_MANAGE;
import static com.ruoyi.form.constants.ProblemConstant.ProblemSource.MAJOR_EVENT;
import static com.ruoyi.form.constants.ProblemFlowConstants.*;
import static com.ruoyi.form.enums.ProblemStatus.*;

/**
 * 自定义表单业务层实现
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class CustomerFormServiceImpl implements CustomerFormService {

    private static final String PREFIX_IMP = "IMP";
    private static final String PREFIX_IM = "IM";
    private static final String bizTablePrefix = "design_biz";


    private final CustomerFormMapper customerFormMapper;
    private final PubParaMapper pubParaMapper;
    private final PubParaValueMapper pubParaValueMapper;
    private final DesignBizJsonDataService designBizJsonDataService;
    private final CustomerFormService customerFormService;
    private final RuntimeService runtimeService;
    private final IdentityService identityService;
    private final TaskService taskService;
    private final IProcessService processService;
    private final HistoryService historyService;
    private final FormService formService;
    private final RepositoryService repositoryService;
    private final FieldActivityNodeService fieldActivityNodeService;
    private final ISysWorkService sysWorkService;
    private final IOgPersonService ogPersonService;
    private final ISysRoleService sysRoleService;
    private final IOgUserService ogUserService;
    private final ProcessEngine processEngine;
    private final FormStatusActivityNodeService formStatusActivityNodeService;
    private final FormStepStatusService formStepStatusService;
    private final IPubFlowLogService iPubFlowLogService;
    private final IOgPersonService iOgPersonService;
    private final ISysWorkService iSysWorkService;
    private final ISysRoleService iSysRoleService;
    private final ISysDeptService iSysDeptService;
    private final IIdGeneratorService generatorService;
    private final IOgGroupPersonService ogGroupPersonService;
    private final ButtonActionMapper buttonActionMapper;
    private final PubFlowLogMapper pubFlowLogMapper;
    private final IOgTypeinfoService ogTypeinfoService;
    private final ISysApplicationManagerService applicationManagerService;
    private final IPubParaValueService pubParaValueService;
    private final OperationDetailsService operationDetailsService;
    private final IRelationLogService relationLogService;
    private final IImplRecordService iImplRecordService;
    private final EventForeignService eventForeignService;
    private final IChangeTaskSceneService changeTaskSceneService;
    private final RelationLogMapper relationLogMapper;
    private final IChangeService changeService;
    private final IChangePersonService changePersonService;
    private final IncidentSubEventService incidentSubEventService;
    private final EventConsumeDetailsService eventConsumeDetailsService;
    private final EventDutyGroupService eventDutyGroupService;
    private final ICommonTreeService iCommonTreeService;
    private final IChmBasedataService iChmBasedataService;
    @Autowired
    DesignBizChmMapper designBizChmMapper;
    @Autowired
    private ISysDeptService deptService;
    @Resource
    IChangePersonService iChangePersonService;
    @Autowired
    Base base;
    @Override
    public AjaxResult list(String code, String type, Page page, CustomerFormListDTO customerFormListDTO) {
        //   获取表单版本ID、表单版本号
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), customerFormListDTO.getVersion());
        if (ObjectUtil.isEmpty(currentTableInfo)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getDesc());
        }
        List conditionList = customerFormListDTO.getSearchDTOList().stream().filter(p -> (StringUtils.isNotEmpty(p.getSearchKey()))).collect(Collectors.toList());
        List searchList = customerFormListDTO.getSearchDTOList().stream().filter(p -> (StringUtils.isNotEmpty(p.getSearchCondition()))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(conditionList) && !CollectionUtils.isEmpty(searchList)) {
            return AjaxResult.error("请输入搜索条件！");
        }
        //根据表单版本ID获取当前表单当前版本的字段
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        if (CollectionUtil.isEmpty(formFieldInfos)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getDesc());
        }
        //获取表名的中文名
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));

        //构造查询对象的ID集合
        List<String> ids = buildQueryIds(code, currentTableInfo, type);

        Map<String, Object> resultMap = new HashMap<>();
        dynamicTableName(code);
        QueryWrapper queryWrapper = buildQueryWrapper(code, formFieldInfos, customerFormListDTO.getSearchDTOList(), customerFormListDTO.getConCondition(), ids, type, formName);

        Page page1 = customerFormMapper.selectMapsPage(page, queryWrapper);
        //构造返回结果
        buildResultList(code, type, page1);
        //事件单添加催单、挂起标识
        if (WorkOrderInformation.incident.getCode().equals(code)) {
            Map<String, String> reminderMap = new HashMap<>();
            reminderMap.put("name", "remindeFlag");
            reminderMap.put("description", "是否催单");
            reminderMap.put("display", "1");
            reminderMap.put("exportable", "0");
            reminderMap.put("editable", "0");
            reminderMap.put("record_data_change", "0");
            formFieldInfos.add(4, reminderMap);
            Map<String, String> reminderTimeMap = new HashMap<>();
            reminderTimeMap.put("name", "remindeTime");
            reminderTimeMap.put("description", "最近一次催单时间");
            reminderTimeMap.put("display", "1");
            reminderTimeMap.put("exportable", "0");
            reminderTimeMap.put("editable", "0");
            reminderTimeMap.put("record_data_change", "0");
            formFieldInfos.add(5, reminderTimeMap);
            Map<String, String> suspendMap = new HashMap<>();
            suspendMap.put("name", "suspendFlag");
            suspendMap.put("description", "是否挂起");
            suspendMap.put("display", "1");
            suspendMap.put("exportable", "0");
            suspendMap.put("editable", "0");
            suspendMap.put("record_data_change", "0");
            formFieldInfos.add(6, suspendMap);
            Map<String, String> suspendTimeMap = new HashMap<>();
            suspendTimeMap.put("name", "suspendTime");
            suspendTimeMap.put("description", "挂起时间");
            suspendTimeMap.put("display", "1");
            suspendTimeMap.put("exportable", "0");
            suspendTimeMap.put("editable", "0");
            suspendTimeMap.put("record_data_change", "0");
            formFieldInfos.add(7, suspendTimeMap);
        }
        //查询当前表单的所有版本
        List<Map<String, Object>> formVersions = customerFormMapper.getCurrentFormAllVersions(String.format("%s_%s", bizTablePrefix, code));
        resultMap.put("pageListInfo", page1);
        resultMap.put("fieldInfo", formFieldInfos);
        resultMap.put("formVersionInfo", formVersions);
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(resultMap);
    }


    @Override
    public AjaxResult insertOrUpdate(String fromCode, String id, String code, CustomerFormContent customerFormContent) {
        Integer integer = this.customerFormService.dataOperation(fromCode, id, code, customerFormContent);
        return AjaxResult.success(integer);
    }

    @Override
    public AjaxResult insertOrUpdate(String code, CustomerFormContent customerFormContent) {
        Integer integer = this.customerFormService.dataOperation(code, customerFormContent);
        return AjaxResult.success(integer);
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Integer dataOperation(String fromCode, String id, String code, CustomerFormContent customerFormContent) {
//        customerFormContent.setOnlySaveFlag("1");
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
                // 问题单需要统计计划完成时间和解决方案修改次数
                if (ObjectUtil.equal(code, WorkOrderInformation.problem.getCode())) {
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
                        if (!oldEntry.getKey().contains("file") && customerFormContent.getFields().get(oldEntry.getKey()) != null &&
                                !customerFormContent.getFields().get(oldEntry.getKey()).equals(oldEntry.getValue())) {
                            if (oldEntry.getKey().equals(PLAN_COMPLETE_TIME)) {
                                int planUpdateNum = ObjectUtil.isEmpty(customerFormContent.getFields().get(PLAN_COMPLETE_TIME_MODIFY_NUM))
                                        ? 0
                                        : Integer.valueOf(customerFormContent.getFields().get(PLAN_COMPLETE_TIME_MODIFY_NUM).toString()) + 1;
                                customerFormContent.getFields().put(PLAN_COMPLETE_TIME_MODIFY_NUM, planUpdateNum);
                                Map<String, Object> mapDate = new HashMap<>();
                                mapDate.put(PLAN_COMPLETE_TIME_MODIFY_NUM, planUpdateNum);
                                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                            } else if (oldEntry.getKey().equals(SOLUTION_SUMMARY)) {
                                if (!ObjectUtil.isEmpty(oldEntry.getValue())) {
                                    int solutionUpdateNum = ObjectUtil.isEmpty(customerFormContent.getFields().get(SOLUTION_MODIFY_NUM))
                                            ? 0
                                            : Integer.valueOf(customerFormContent.getFields().get(SOLUTION_MODIFY_NUM).toString()) + 1;
                                    customerFormContent.getFields().put(SOLUTION_MODIFY_NUM, solutionUpdateNum);
                                    Map<String, Object> mapDate = new HashMap<>();
                                    mapDate.put(SOLUTION_MODIFY_NUM, solutionUpdateNum);
                                    customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                                }
                            }
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
                } else {
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
                }
                //记录操作记录
                if (!WorkOrderInformation.change.getCode().equals(code)&&!WorkOrderInformation.changeTask.getCode().equals(code)){
                    operationDetailsService.saveBatch(operationDetails);
                }
            }

            if (code.equals(WorkOrderInformation.problem.getCode())) {
                Map<String, Object> problemMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(INSTANCE_ID, ORIGINATOR_ID, SOLVER_ID, AUDITOR_ID, STATUS, "created_time").eq("id", bizId)).get(0);
//                customerFormContent.getFields().put("manual_flag", manualFlag.get("manual_flag").toString());
                customerFormContent.getFields().put("created_by", customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                customerFormContent.getFields().put(PROBLEM_UPDATE_TIME, DateUtils.getDate());
                Map<String, Object> mapDate = new HashMap<>();
                mapDate.put(PROBLEM_UPDATE_TIME, DateUtils.getDate());
//                customerFormContent.getFields().put("created_time", problemMap.get("created_time").toString());
                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                // 各种状态管理员修改人员后只保存不走流程的情况,需要修改当前处理人及设置当前待办人
                if (Arrays.asList(ASSIGNED.getInfo(), SOLUTION_PENDING.getInfo(), UNDER_INVESTIGATION.getInfo(), SOLVING.getInfo()).contains(problemMap.get(STATUS).toString())
                        && !problemMap.get(SOLVER_ID).toString().equals(customerFormContent.getFields().get(SOLVER_ID).toString())) {
                    OgPerson curHandlerPerson = ogPersonService.selectOgPersonById(customerFormContent.getFields().get(SOLVER_ID).toString());
                    Map<String, Object> curMap1 = new HashMap<>();
                    curMap1.put(curHandlerPerson.getpName(), customerFormContent.getFields().get(SOLVER_ID).toString());
                    curMap1.put("default", customerFormContent.getFields().get(SOLVER_ID).toString());

                    List<Task> list = taskService.createTaskQuery().processInstanceId(problemMap.get(INSTANCE_ID).toString()).processDefinitionKey(code).taskCandidateOrAssigned(problemMap.get(SOLVER_ID).toString()).list();
                    if (!CollectionUtils.isEmpty(list)) {
                        taskService.setAssignee(list.get(0).getId(), customerFormContent.getFields().get(SOLVER_ID).toString());
                        customerFormContent.getFields().put(CURRENT_DEAL_ID, customerFormContent.getFields().get(SOLVER_ID));
                        Map<String, Object> curMap2 = new HashMap<>();
                        curMap2.put(CURRENT_DEAL_ID, curMap1);
                        String dataJson1 = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                        customerFormContent.setDataJson(dataJson1);
                    }
                }
                if (Arrays.asList(TECHNOLOGY_AUDIT.getInfo(), SOLUTION_AUDIT.getInfo(), AUDITOR_CONFIRMING.getInfo()).contains(problemMap.get(STATUS).toString())
                        && !problemMap.get(AUDITOR_ID).toString().equals(customerFormContent.getFields().get(AUDITOR_ID).toString())) {
                    OgPerson curHandlerPerson = ogPersonService.selectOgPersonById(customerFormContent.getFields().get(AUDITOR_ID).toString());
                    Map<String, Object> curMap1 = new HashMap<>();
                    curMap1.put(curHandlerPerson.getpName(), customerFormContent.getFields().get(AUDITOR_ID).toString());
                    curMap1.put("default", customerFormContent.getFields().get(AUDITOR_ID).toString());

                    List<Task> list = taskService.createTaskQuery().processInstanceId(problemMap.get(INSTANCE_ID).toString()).processDefinitionKey(code).taskCandidateOrAssigned(problemMap.get(AUDITOR_ID).toString()).list();
                    if (!CollectionUtils.isEmpty(list)) {
                        taskService.setAssignee(list.get(0).getId(), customerFormContent.getFields().get(AUDITOR_ID).toString());
                        customerFormContent.getFields().put(CURRENT_DEAL_ID, customerFormContent.getFields().get(AUDITOR_ID));
                        Map<String, Object> curMap2 = new HashMap<>();
                        curMap2.put(CURRENT_DEAL_ID, curMap1);
                        String dataJson1 = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                        customerFormContent.setDataJson(dataJson1);
                    }
                }
                if (Arrays.asList(OBSERVING.getInfo(), ORIGINATOR_CONFIRMING.getInfo()).contains(problemMap.get(STATUS).toString())
                        && !problemMap.get(ORIGINATOR_ID).toString().equals(customerFormContent.getFields().get(ORIGINATOR_ID).toString())) {
                    OgPerson curHandlerPerson = ogPersonService.selectOgPersonById(customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                    Map<String, Object> curMap1 = new HashMap<>();
                    curMap1.put(curHandlerPerson.getpName(), customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                    curMap1.put("default", customerFormContent.getFields().get(ORIGINATOR_ID).toString());

                    List<Task> list = taskService.createTaskQuery().processInstanceId(problemMap.get(INSTANCE_ID).toString()).processDefinitionKey(code).taskCandidateOrAssigned(problemMap.get(ORIGINATOR_ID).toString()).list();
                    if (!CollectionUtils.isEmpty(list)) {
                        taskService.setAssignee(list.get(0).getId(), customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                        customerFormContent.getFields().put(CURRENT_DEAL_ID, customerFormContent.getFields().get(ORIGINATOR_ID));
                        Map<String, Object> curMap2 = new HashMap<>();
                        curMap2.put(CURRENT_DEAL_ID, curMap1);
                        String dataJson1 = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                        customerFormContent.setDataJson(dataJson1);
                    }
                }

                // 如果状态是待提交
                if (WAIT_SUBMIT.getInfo().equals(problemMap.get(STATUS).toString())) {
                    // 查询管理员组
                    // 查询问题发起人所在机构
                    OgOrg ogOrg = iSysDeptService.selectDeptById(ogPersonService.selectOgPersonById(customerFormContent.getFields().get(ORIGINATOR_ID).toString()).getOrgId());
                    String orgId = changePersonService.selectDept(ogOrg.getOrgId());
                    if (StringUtils.isBlank(orgId)) {
                        throw new BusinessException("问题发起人所在机构不正确,非总行或分行人员!");
                    }
                    OgGroup ogGroup = new OgGroup();
                    // 设置当前登录人所在机构id
                    ogGroup.setOrgId(orgId);
                    ogGroup.setMemo("problem_admin_not_update");// 问题管理员組
                    List<OgGroup> ogGroups = iSysWorkService.selectOgGroupList(ogGroup);
                    if (CollectionUtils.isEmpty(ogGroups)) {
                        throw new BusinessException("该人所在的管理员组不存在!");
                    }
                    String groupid = ogGroups.get(0).getGroupId();
                    List<OgGroupPerson> ogGroupPeopleList = ogGroupPersonService.selectOgGroupPersonById(groupid);
                    if (CollectionUtils.isEmpty(ogGroupPeopleList)) {
                        throw new BusinessException("管理员组没有配置人员!");
                    }
                    //  标识此条数据是通过事件单发起的问题单或者是人工在页面发起的问题单
//                if (StringUtils.isEmpty(fromCode)) {
//                    customerFormContent.getFields().put("manual_flag", FromEventFlag.ONE);
//                }

                    if (ForError.ONE.equals(customerFormContent.getFields().get(FOR_ERROR))) {
                        // 设置根因明确时间
                        customerFormContent.getFields().put(SOLVER_CLEAR_TIME, DateUtils.getDate());
                        Map<String, Object> map = new HashMap<>();
                        map.put(SOLVER_CLEAR_TIME, DateUtils.getDate());
                        customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), map));

                        // 设置当前处理人 管理员
                        List<String> idList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPid()).collect(Collectors.toList());
                        List<String> nameList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPname()).collect(Collectors.toList());
                        OgPerson curHandlerPerson = ogPersonService.selectOgPersonById(customerFormContent.getFields().get(SOLVER_ID).toString());
                        customerFormContent.getFields().put(MANAGER_ID, StringUtils.join(idList, ","));
                        customerFormContent.getFields().put(CURRENT_HANDLER_ID, customerFormContent.getFields().get(SOLVER_ID));
                        Map<String, Object> curMap = new HashMap<>();
                        curMap.put(StringUtils.join(nameList, ","), StringUtils.join(idList, ","));
                        curMap.put("default", StringUtils.join(idList, ","));

                        Map<String, Object> curMap1 = new HashMap<>();
                        curMap1.put(curHandlerPerson.getpName(), customerFormContent.getFields().get(SOLVER_ID).toString());
                        curMap1.put("default", customerFormContent.getFields().get(SOLVER_ID).toString());

                        Map<String, Object> curMap2 = new HashMap<>();
                        curMap2.put(CURRENT_HANDLER_ID, curMap1);
                        curMap2.put(MANAGER_ID, curMap);
                        String dataJson = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                        customerFormContent.setDataJson(dataJson);
                    } else {
                        // 设置当前处理人 管理员
                        List<String> idList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPid()).collect(Collectors.toList());
                        List<String> nameList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPname()).collect(Collectors.toList());
                        customerFormContent.getFields().put(MANAGER_ID, StringUtils.join(idList, ","));
                        customerFormContent.getFields().put(CURRENT_HANDLER_ID, StringUtils.join(idList, ","));
                        Map<String, Object> curMap = new HashMap<>();
                        curMap.put(StringUtils.join(nameList, ","), StringUtils.join(idList, ","));
                        curMap.put("default", StringUtils.join(idList, ","));

                        Map<String, Object> curMap1 = new HashMap<>();
                        curMap1.put(StringUtils.join(nameList, ","), StringUtils.join(idList, ","));
                        curMap1.put("default", StringUtils.join(idList, ","));

                        Map<String, Object> curMap2 = new HashMap<>();
                        curMap2.put(CURRENT_HANDLER_ID, curMap1);
                        curMap2.put(MANAGER_ID, curMap);
                        String dataJson = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                        customerFormContent.setDataJson(dataJson);
                    }
                    OgOrg org = deptService.selectDeptById(orgId);
                    if (org == null) {
                        throw new BusinessException(orgId + ":机构不存在!");
                    }
                    Map<String, Object> orgMap = new HashMap<>();
                    Map<String, Object> orgIdmap = new HashMap<>();
                    orgIdmap.put(org.getOrgName(), orgId);
                    orgIdmap.put("default", orgId);
                    orgMap.put(ORG_ID, orgIdmap);
                    String dataOrgJson = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), orgMap);
                    customerFormContent.setDataJson(dataOrgJson);
                    customerFormContent.getFields().put(ORG_ID, orgId);
                    customerFormContent.getFields().put(PROBLEM_CREATED_TIME, DateUtils.formatDate((Date)problemMap.get("created_time"), YYYY_MM_DD));
                    Map<String, Object> curMap2 = new HashMap<>();
//                    curMap2.put(ORI_DEP_ID, ogOrg.getOrgName());
                    curMap2.put(PROBLEM_CREATED_TIME, DateUtils.formatDate((Date)problemMap.get("created_time"), YYYY_MM_DD));
                    String dataJson = VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), curMap2);
                    customerFormContent.setDataJson(dataJson);
                }
            }
            if (code.equals(WorkOrderInformation.problem_task.getCode())) {
                // 如果状态是待提交
                Map<String, Object> problemMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(STATUS, "created_time", "assistant_id", "instance_id").eq("id", bizId)).get(0);
                if (WAIT_SUBMIT.getInfo().equals(problemMap.get(STATUS).toString())) {
//                    customerFormContent.getFields().put("created_time", problemMap.get("created_time").toString());
                    // 设置当前处理人
                    OgPerson ogPerson = iOgPersonService.selectOgPersonById(customerFormContent.getFields().get("assistant_id").toString());
                    Map<String, Object> curMap1 = new HashMap<>();
                    curMap1.put(ogPerson.getpName(), ogPerson.getpId());
                    curMap1.put("default", ogPerson.getpId());
                    Map<String, Object> curMap2 = new HashMap<>();
                    curMap2.put("current_handler_id", curMap1);
                    String dataJson = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                    customerFormContent.setDataJson(dataJson);
                }
                if (!customerFormContent.getFields().get("assistant_id").equals(problemMap.get("assistant_id").toString())
                        && ("1").equals(customerFormContent.getOnlySaveFlag())) {
                    OgPerson curHandlerPerson = ogPersonService.selectOgPersonById(customerFormContent.getFields().get("assistant_id").toString());
                    Map<String, Object> curMap1 = new HashMap<>();
                    curMap1.put(curHandlerPerson.getpName(), customerFormContent.getFields().get("assistant_id").toString());
                    curMap1.put("default", customerFormContent.getFields().get("assistant_id").toString());

                    // 根据id查询instanceId
                    List<Task> list = taskService.createTaskQuery().processInstanceId(problemMap.get(INSTANCE_ID).toString()).processDefinitionKey(code).taskCandidateOrAssigned(problemMap.get("assistant_id").toString()).list();
                    if (!CollectionUtils.isEmpty(list)) {
                        taskService.setAssignee(list.get(0).getId(), customerFormContent.getFields().get("assistant_id").toString());
                        customerFormContent.getFields().put("current_handler_id", customerFormContent.getFields().get("assistant_id"));
                        Map<String, Object> curMap2 = new HashMap<>();
                        curMap2.put("current_handler_id", curMap1);
                        String dataJson1 = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                        customerFormContent.setDataJson(dataJson1);
                    }
                }
                customerFormContent.getFields().put("task_updated_time", DateUtils.getDate());
                customerFormContent.getFields().put("task_created_time", DateUtils.formatDateStr(problemMap.get("created_time").toString(), YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD));
                Map<String, Object> mapDate = new HashMap<>();
                mapDate.put("task_updated_time", DateUtils.getDate());
                mapDate.put("task_created_time", DateUtils.formatDateStr(problemMap.get("created_time").toString(), YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD));
                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
            }
            //走修改 把通用字段中的修改时间及修改人添加至字段中
            customerFormContent.getFields().put("updated_time", DateUtils.getNowDate());
            customerFormContent.getFields().put("updated_by", CustomerBizInterceptor.currentUserThread.get().getUserId());
            resultItem = customerFormMapper.updateById(customerFormContent);
            designBizJsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", bizTablePrefix, code)).bizId(bizId).jsonData(customerFormContent.getDataJson()).build();
            designBizJsonDataService.update(designBizJsonData, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_ID, bizId).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
        } else {
            String loginUser = CustomerBizInterceptor.currentUserThread.get().getUserId();
            //走添加 把通用字段中的创建时间、修改时间、创建人、修改人添加至字段中
            customerFormContent.getFields().put("created_time", DateUtils.getNowDate());
            customerFormContent.getFields().put("created_by", loginUser);
            customerFormContent.getFields().put("updated_time", DateUtils.getNowDate());
            customerFormContent.getFields().put("updated_by", loginUser);
            customerFormContent.getFields().put("status", "待提交");
            //TODO 这里按照具体的业务去生成你们自己业务单的编号  写逻辑  把值放在207行代码中的map中
            if (code.equals(WorkOrderInformation.problem.getCode())) {
                String json = customerFormContent.getDataJson();
                if (!loginUser.equals(customerFormContent.getFields().get(ORIGINATOR_ID).toString())) {
                    customerFormContent.getFields().put("created_by", customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                    // 重新设置发起人部室经理
//                    OgPerson op = iOgPersonService.selectOgPersonById(customerFormContent.getFields().get(ORIGINATOR_ID).toString());
//                    // 问题单发起部室
//                    QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<ChangeDeptPersonEntity>();
//                    queryWrapper.eq("dept_id", op.getOrgId());
//                    List<ChangeDeptPersonEntity> changeDeptPersonEntityList = changePersonService.list(queryWrapper.orderByDesc("create_date"));
//                    Map<String, Object> personnelAllocationMap = new HashMap<>();
//                    Map<String, Object> oriDeptMap = new HashMap<>();
//                    Map<String, Object> oriDMap = new HashMap<>();
//                    if(CollectionUtil.isNotEmpty(changeDeptPersonEntityList)){
//                        String deptPersonId=changeDeptPersonEntityList.get(0).getDeptPerson();
//                        OgPerson opdept=iOgPersonService.selectOgPersonById(deptPersonId);
//                        if(opdept!=null){
//                            oriDeptMap.put(opdept.getpName(), opdept.getpId());
//                            oriDeptMap.put("default",opdept.getpId());
//                            personnelAllocationMap.put(ORI_DEP_MANAGER_ID, oriDeptMap);
//                            oriDMap.put(changeDeptPersonEntityList.get(0).getOrgName(), changeDeptPersonEntityList.get(0).getOrgId());
//                            oriDMap.put("default",changeDeptPersonEntityList.get(0).getOrgId());
//                            personnelAllocationMap.put(ORI_DEP_ID, oriDMap);
//                            // 人员id转换成名称
//                            String s = VueDataJsonUtil.analysisDataJsonSelect(json, personnelAllocationMap);
//                            customerFormContent.setDataJson(s);
//                        }
//                    }
                }
                // 查询管理员组
                // 查询问题发起人所在机构
                OgOrg ogOrg = iSysDeptService.selectDeptById(ogPersonService.selectOgPersonById(customerFormContent.getFields().get(ORIGINATOR_ID).toString()).getOrgId());
                String orgId = changePersonService.selectDept(ogOrg.getOrgId());
                if (StringUtils.isBlank(orgId)) {
                    throw new BusinessException("问题发起人所在机构不正确,非总行或分行人员!");
                }
                OgGroup ogGroup = new OgGroup();
                // 设置当前登录人所在机构id
                ogGroup.setOrgId(orgId);
                ogGroup.setMemo("problem_admin_not_update");// 问题管理员組
                List<OgGroup> ogGroups = iSysWorkService.selectOgGroupList(ogGroup);
                if (CollectionUtils.isEmpty(ogGroups)) {
                    throw new BusinessException("该人所在管理员组不存在!");
                }
                String groupid = ogGroups.get(0).getGroupId();
                List<OgGroupPerson> ogGroupPeopleList = ogGroupPersonService.selectOgGroupPersonById(groupid);
                if (CollectionUtils.isEmpty(ogGroupPeopleList)) {
                    throw new BusinessException("管理员组没有配置人员!");
                }
                //  标识此条数据是通过事件单发起的问题单或者是人工在页面发起的问题单
//                if (StringUtils.isEmpty(fromCode)) {
//                    customerFormContent.getFields().put("manual_flag", FromEventFlag.ONE);
//                }

                List<String> idList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPid()).collect(Collectors.toList());
                List<String> nameList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPname()).collect(Collectors.toList());
                String idStrList = StringUtils.join(idList, ",");
                String nameStrList = StringUtils.join(nameList, ",");
                if (ForError.ONE.equals(customerFormContent.getFields().get(FOR_ERROR))) {
                    // 设置根因明确时间
                    customerFormContent.getFields().put(SOLVER_CLEAR_TIME, DateUtils.getDate());
                    Map<String, Object> mapDate = new HashMap<>();
                    mapDate.put(SOLVER_CLEAR_TIME, DateUtils.getDate());
                    customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));

                    // 设置当前处理人 管理员
                    OgPerson curHandlerPerson = ogPersonService.selectOgPersonById(customerFormContent.getFields().get(SOLVER_ID).toString());
                    customerFormContent.getFields().put(MANAGER_ID, idStrList);
                    customerFormContent.getFields().put(CURRENT_HANDLER_ID, customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                    Map<String, Object> curMap = new HashMap<>();
                    curMap.put(nameStrList, idStrList);
                    curMap.put("default", idStrList);

                    Map<String, Object> curMap1 = new HashMap<>();
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                    curMap1.put(ogPerson.getpName(), customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                    curMap1.put("default", customerFormContent.getFields().get(ORIGINATOR_ID).toString());

                    Map<String, Object> curMap2 = new HashMap<>();
                    curMap2.put(CURRENT_HANDLER_ID, curMap1);
                    curMap2.put(MANAGER_ID, curMap);
                    String dataJson = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                    customerFormContent.setDataJson(dataJson);
                } else {
                    // 设置当前处理人 管理员
                    customerFormContent.getFields().put(MANAGER_ID, idStrList);
                    customerFormContent.getFields().put(CURRENT_HANDLER_ID, customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                    Map<String, Object> curMap = new HashMap<>();
                    curMap.put(nameStrList, idStrList);
                    curMap.put("default", idStrList);

                    Map<String, Object> curMap1 = new HashMap<>();

                    OgPerson ogPerson = ogPersonService.selectOgPersonById(customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                    curMap1.put(ogPerson.getpName(), customerFormContent.getFields().get(ORIGINATOR_ID).toString());
                    curMap1.put("default", customerFormContent.getFields().get(ORIGINATOR_ID).toString());

                    Map<String, Object> curMap2 = new HashMap<>();
                    curMap2.put(CURRENT_HANDLER_ID, curMap1);
                    curMap2.put(MANAGER_ID, curMap);
                    String dataJson = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                    customerFormContent.setDataJson(dataJson);
                }
                // 问题创建时间和问题更新时间
                customerFormContent.getFields().put(PROBLEM_CREATED_TIME, DateUtils.getDate());
                customerFormContent.getFields().put(PROBLEM_UPDATE_TIME, DateUtils.getDate());
//                customerFormContent.getFields().put(ORI_DEP_ID, ogOrg.getOrgId());
                // 保存发起单位id
                customerFormContent.getFields().put(ORG_ID, orgId);
                OgOrg org = deptService.selectDeptById(orgId);
                if (org == null) {
                    throw new BusinessException(orgId + ":机构不存在!");
                }
                // TODO
//                mapDate.put(ORG_ID, org.getOrgName());
                Map<String, Object> mapDate = new HashMap<>();
                mapDate.put(PROBLEM_CREATED_TIME, DateUtils.getDate());
//                mapDate.put(ORI_DEP_ID, ogOrg.getOrgName());
                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                Map<String, Object> mapDate1 = new HashMap<>();
                mapDate1.put(PROBLEM_UPDATE_TIME, DateUtils.getDate());
                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate1));
                customerFormContent.getFields().put(STAGE, stageMap.get(WAIT_SUBMIT.getInfo()));
                customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, generatorService.createNoAsLength(PREFIX_IMP, 3));
                Map<String, Object> map = new HashMap<>();
                map.put(STAGE, stageMap.get(WAIT_SUBMIT.getInfo()));
                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), map));
                // 问题任务
            } else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
                // 问题任務创建时间和问题更新时间
                customerFormContent.getFields().put("task_created_time", DateUtils.getDate());
                customerFormContent.getFields().put("task_updated_time", DateUtils.getDate());
                Map<String, Object> mapDate = new HashMap<>();
                mapDate.put("task_created_time", DateUtils.getDate());
                mapDate.put("task_updated_time", DateUtils.getDate());
                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                customerFormContent.getFields().put(STAGE, ProblemTaskStatus.stageMap.get(WAIT_SUBMIT.getInfo()));
                customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, generatorService.createProblemTaskNo(customerFormContent.getFields().get(PROBLEM_NO).toString()));
                Map<String, Object> map = new HashMap<>();
                map.put(STAGE, ProblemTaskStatus.stageMap.get(WAIT_SUBMIT.getInfo()));
                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), map));
                // 设置当前处理人
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(customerFormContent.getFields().get("originator_id").toString());
                customerFormContent.getFields().put("current_handler_id", ogPerson.getpId());
                Map<String, Object> curMap1 = new HashMap<>();
                curMap1.put(ogPerson.getpName(), ogPerson.getpId());
                curMap1.put("default", ogPerson.getpId());
                Map<String, Object> curMap2 = new HashMap<>();
                curMap2.put("current_handler_id", curMap1);
                String dataJson = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), curMap2);
                customerFormContent.setDataJson(dataJson);
            } else if (code.equals(WorkOrderInformation.incident.getCode())) {
                // extra5保存事件单当前处理人
                customerFormContent.getFields().put("extra5", customerFormContent.getFields().get("created_by"));
                customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, generatorService.createNoAsLength(PREFIX_IM, 5));
                customerFormContent.getFields().put("status", EventStatusEnum.CREATED.getInfo());
            } else if (code.equals(WorkOrderInformation.chm_task.getCode())) {
                String custNo = generatorService.createNoAsLength("WX", 4);
                if (customerFormContent.getFields().get("source") != null&&customerFormContent.getFields().get("source").toString().equals("1")) {
                    customerFormContent.getFields().put("report_department", customerFormContent.getFields().get("report_department"));
                } else {
                    customerFormContent.getFields().put("source","2");
                    String report = customerFormContent.getFields().get("report_department").toString();
//                    ChmBasedata chmBasedata = iChmBasedataService.selectChmBasedataById(Long.valueOf(report));
//                    customerFormContent.getFields().put("report_department", chmBasedata.getOrgName());
                    ///implement_no 机具号   implement_Id 设备标识号
                    Object implementNo = customerFormContent.getFields().get("implement_no");
                    if (StringUtils.isNotEmpty(implementNo)) {
                        if (implementNo.toString().length() < 8) {
                            throw new BusinessException("机具号不小于8位，请重新填写");
                        }
                    }
                }
                customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, custNo);
                String mobilePhone = customerFormContent.getFields().get("contact_details").toString();
                boolean result=mobilePhone.matches("[0-9]+");
                if(result==false){
                    throw new BusinessException("电话号填写纯数字，请重新填写");
                }else {
                    if(mobilePhone.length()<8){
                        throw new BusinessException("电话号填写8位以上有效号码，请重新填写");
                    }
                }
            } else if (code.equals(WorkOrderInformation.change.getCode())) {
                String custNo = generatorService.createNoAsLength("CHM", 5);
                customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, custNo);
            }else if(code.contains("tinyWeb_")){
                String custNo=generatorService.createNoAsLength("tinyWeb", 5);
                customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, custNo);
            }
            else {
//                customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, DateUtil.format(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(3));
              //生成自定义no和插入状态
                customerFormContent.getFields().put("extra5", customerFormContent.getFields().get("created_by"));
                customerFormContent.getFields().put(RedundancyFieldEnum.extra1.name, generatorService.createNoAsLength(code, 5));
                customerFormContent.getFields().put("status", EventStatusEnum.CREATED.getInfo());
            }
            resultItem = customerFormMapper.insert(customerFormContent);
            designBizJsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", bizTablePrefix, code)).bizId(customerFormContent.getId()).jsonData(customerFormContent.getDataJson()).build();
            designBizJsonDataService.save(designBizJsonData);
            bizId = customerFormContent.getId();
            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
            //根据表单版本ID获取当前表单当前版本的字段
            List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
            //获取表名的中文名
            String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
            String bizNo = customerFormContent.getFields().get(RedundancyFieldEnum.extra1.name).toString();
            // 新增关联单子记录
            if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(fromCode)) {
                if ((fromCode.equals(WorkOrderInformation.problem.getCode()) || fromCode.equals(WorkOrderInformation.problem_task.getCode())) && code.equals(WorkOrderInformation.change.getCode())) {
                    dynamicTableName(fromCode);
                    Map<String, Object> problemFieldMap;
                    RelationLog relationProblemLog = new RelationLog();
                    if (fromCode.equals(WorkOrderInformation.problem.getCode())) {
                        problemFieldMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                                .select(STATUS, CURRENT_HANDLER_ID, RedundancyFieldEnum.extra1.name, PROBLEM_TITLE, CREATED_TIME, UPDATED_TIME)
                                .eq("id", id)).get(0);
                        // 变更单关系页签展示关联的问题单记录
                        relationProblemLog.setStatus(problemFieldMap.get(STATUS).toString());
                        relationProblemLog.setStartDate(DateUtils.formatDate((Date) problemFieldMap.get(CREATED_TIME), YYYY_MM_DD_HH_MM_SS));
                        relationProblemLog.setCurrentHandlerId(problemFieldMap.get(CURRENT_HANDLER_ID).toString());
                        relationProblemLog.setEndDate(Arrays.asList(ProblemStatus.CANCEL.getInfo(), ProblemStatus.CLOSE.getInfo()).contains(problemFieldMap.get(STATUS).toString()) ? DateUtils.formatDate((Date) problemFieldMap.get(UPDATED_TIME), YYYY_MM_DD_HH_MM_SS) : null);
                        relationProblemLog.setRequestNo(problemFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                        relationProblemLog.setRelationNo(bizNo);
                        relationProblemLog.setRelationType("相关");
                        relationProblemLog.setRequestSummary(problemFieldMap.get(RedundancyFieldEnum.extra1.name).toString() + ":" + problemFieldMap.get(PROBLEM_TITLE).toString());
                        relationProblemLog.setRequestType(RelationRequestType.PROBLEM.getCode());
                    } else {
                        // 问题任务
                        problemFieldMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                                .select(STATUS, "current_handler_id", RedundancyFieldEnum.extra1.name, "problem_desc", CREATED_TIME, UPDATED_TIME)
                                .eq("id", id)).get(0);
                        // 变更单关系页签展示关联的问题任务单记录
                        relationProblemLog.setStatus(problemFieldMap.get(STATUS).toString());
                        relationProblemLog.setStartDate(DateUtils.formatDate((Date) problemFieldMap.get(CREATED_TIME), YYYY_MM_DD_HH_MM_SS));
                        relationProblemLog.setCurrentHandlerId(problemFieldMap.get("current_handler_id").toString());
                        relationProblemLog.setEndDate(Arrays.asList(ProblemTaskStatus.CANCEL.getInfo(), ProblemTaskStatus.CLOSE.getInfo()).contains(problemFieldMap.get(STATUS).toString()) ? DateUtils.formatDate((Date) problemFieldMap.get(UPDATED_TIME), YYYY_MM_DD_HH_MM_SS) : null);
                        relationProblemLog.setRequestNo(problemFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                        relationProblemLog.setRelationNo(bizNo);
                        relationProblemLog.setRelationType("相关");
                        relationProblemLog.setRequestSummary(problemFieldMap.get(RedundancyFieldEnum.extra1.name).toString() + ":" + problemFieldMap.get("problem_desc").toString());
                        relationProblemLog.setRequestType(RelationRequestType.PROBLEM_TASK.getCode());
                    }

                    // 问题单或问题任务单关系页签展示关联的变更单记录
                    RelationLog relationChangeLog = new RelationLog();
                    relationChangeLog.setStatus(ChangeStatusEnum.preApproval.getName());
                    relationChangeLog.setStartDate(DateUtils.getTime());
                    relationChangeLog.setCurrentHandlerId(CustomerBizInterceptor.currentUserThread.get().getUserId());
                    relationChangeLog.setEndDate(null);
                    relationChangeLog.setRequestNo(bizNo);
                    relationChangeLog.setRelationNo(problemFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                    relationChangeLog.setRelationType("相关");
                    relationChangeLog.setRequestSummary(customerFormContent.getFields().get(RedundancyFieldEnum.extra1.name).toString() + ":" + customerFormContent.getFields().get("title").toString());
                    relationChangeLog.setRequestType(RelationRequestType.CHANGE.getCode());
                    Arrays.asList(relationChangeLog, relationProblemLog).forEach(relationLog -> {
                        try {
                            relationLogService.save(relationLog);
                        } catch (DuplicateKeyException e) {
                            throw new BusinessException(String.format("编号：%s已关联过编号：%s的单子， 请重新选择关联！", relationLog.getRequestNo(), relationLog.getRelationNo()));
                        }
                    });
//                    MybatisPlusConfig.customerTableName.remove();
                }
                if ((fromCode.equals(WorkOrderInformation.incident.getCode())) && code.equals(WorkOrderInformation.problem.getCode())) {
                    dynamicTableName(fromCode);
                    Map<String, Object> incidentFieldMap;
                    RelationLog relationProblemLog = new RelationLog();
                    if (fromCode.equals(WorkOrderInformation.incident.getCode())) {
                        incidentFieldMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                                .select(STATUS, "extra5", RedundancyFieldEnum.extra1.name, "event_title", CREATED_TIME, UPDATED_TIME)
                                .eq("id", id)).get(0);
                        // 事件单展示关联的问题单记录
                        relationProblemLog.setStatus(WAIT_SUBMIT.getInfo());
                        relationProblemLog.setStartDate(DateUtils.getTime());
                        relationProblemLog.setCurrentHandlerId(CustomerBizInterceptor.currentUserThread.get().getUserId());
                        relationProblemLog.setEndDate(null);
                        relationProblemLog.setRequestNo(bizNo);
                        relationProblemLog.setRelationNo(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                        relationProblemLog.setRelationType("相关");
                        relationProblemLog.setRequestSummary(customerFormContent.getFields().get(RedundancyFieldEnum.extra1.name).toString() + ":" + customerFormContent.getFields().get(PROBLEM_TITLE).toString());
                        relationProblemLog.setRequestType(RelationRequestType.PROBLEM.getCode());
                        // 问题单展示关联的事件记录
                        RelationLog relationIncidentLog = new RelationLog();
                        relationIncidentLog.setStatus(incidentFieldMap.get(STATUS).toString());
                        relationIncidentLog.setStartDate(DateUtils.formatDate((Date) incidentFieldMap.get(CREATED_TIME), YYYY_MM_DD_HH_MM_SS));
                        relationIncidentLog.setCurrentHandlerId(ObjectUtil.isNotEmpty(incidentFieldMap.get(RedundancyFieldEnum.extra5.name)) ? incidentFieldMap.get(RedundancyFieldEnum.extra5.name).toString() : null);
                        relationIncidentLog.setEndDate(Arrays.asList(EventStatusEnum.CLOSED.getInfo()).contains(incidentFieldMap.get(STATUS).toString()) ? DateUtils.formatDate((Date) incidentFieldMap.get(UPDATED_TIME), YYYY_MM_DD_HH_MM_SS) : null);
                        relationIncidentLog.setRequestNo(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                        relationIncidentLog.setRelationNo(bizNo);
                        relationIncidentLog.setRelationType("相关");
                        relationIncidentLog.setRequestSummary(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString() + ":" + incidentFieldMap.get("event_title").toString());
                        relationIncidentLog.setRequestType(RelationRequestType.EVENT.getCode());
                        Arrays.asList(relationIncidentLog, relationProblemLog).forEach(relationLog -> {
                            try {
                                relationLogService.save(relationLog);
                            } catch (DuplicateKeyException e) {
                                throw new BusinessException(String.format("编号：%s已关联过编号：%s的单子， 请重新选择关联！", relationLog.getRequestNo(), relationLog.getRelationNo()));
                            }
                        });
                    }
                }
                if ((fromCode.equals(WorkOrderInformation.incident.getCode())) && code.equals(WorkOrderInformation.change.getCode())) {
                    dynamicTableName(fromCode);
                    Map<String, Object> incidentFieldMap;
                    RelationLog relationProblemLog = new RelationLog();
                    if (fromCode.equals(WorkOrderInformation.incident.getCode())) {
                        incidentFieldMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                                .select(STATUS, "extra5", RedundancyFieldEnum.extra1.name, "event_title", CREATED_TIME, UPDATED_TIME)
                                .eq("id", id)).get(0);
                        // 事件单展示关联的变更单记录
                        relationProblemLog.setStatus(ChangeStatusEnum.preApproval.getName());
                        relationProblemLog.setStartDate(DateUtils.getTime());
                        relationProblemLog.setCurrentHandlerId(CustomerBizInterceptor.currentUserThread.get().getUserId());
                        relationProblemLog.setEndDate(null);
                        relationProblemLog.setRequestNo(bizNo);
                        relationProblemLog.setRelationNo(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                        relationProblemLog.setRelationType("相关");
                        relationProblemLog.setRequestSummary(customerFormContent.getFields().get(RedundancyFieldEnum.extra1.name).toString() + ":" + customerFormContent.getFields().get("title").toString());
                        relationProblemLog.setRequestType(RelationRequestType.CHANGE.getCode());
                        // 变更单展示关联的事件记录
                        RelationLog relationIncidentLog = new RelationLog();
                        relationIncidentLog.setStatus(incidentFieldMap.get(STATUS).toString());
                        relationIncidentLog.setStartDate(DateUtils.formatDate((Date) incidentFieldMap.get(CREATED_TIME), YYYY_MM_DD_HH_MM_SS));
                        relationIncidentLog.setCurrentHandlerId(incidentFieldMap.get(RedundancyFieldEnum.extra5.name).toString());
                        relationIncidentLog.setEndDate(null);
                        relationIncidentLog.setRequestNo(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                        relationIncidentLog.setRelationNo(bizNo);
                        relationIncidentLog.setRelationType("相关");
                        relationIncidentLog.setRequestSummary(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString() + ":" + incidentFieldMap.get("event_title").toString());
                        relationIncidentLog.setRequestType(RelationRequestType.EVENT.getCode());
                        Arrays.asList(relationIncidentLog, relationProblemLog).forEach(relationLog -> {
                            try {
                                relationLogService.save(relationLog);
                            } catch (DuplicateKeyException e) {
                                throw new BusinessException(String.format("编号：%s已关联过编号：%s的单子， 请重新选择关联！", relationLog.getRequestNo(), relationLog.getRelationNo()));
                            }
                        });
                    }
                }
                if ((fromCode.equals(WorkOrderInformation.chm_task.getCode())) && code.equals(WorkOrderInformation.incident.getCode())) {
                    dynamicTableName(fromCode);
                    Map<String, Object> incidentFieldMap;
                    RelationLog relationProblemLog = new RelationLog();
                    incidentFieldMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                            .select(STATUS, "extra5", RedundancyFieldEnum.extra1.name, "title", CREATED_TIME, "instance_id")
                            .eq("id", id)).get(0);
                    // 事件单
                    relationProblemLog.setStatus(WAIT_SUBMIT.getInfo());
                    relationProblemLog.setStartDate(DateUtils.getTime());
                    relationProblemLog.setCurrentHandlerId(CustomerBizInterceptor.currentUserThread.get().getUserId());
                    relationProblemLog.setEndDate(null);
                    relationProblemLog.setRequestNo(bizNo);
                    relationProblemLog.setRelationNo(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                    relationProblemLog.setRelationType("相关");
                    relationProblemLog.setRequestSummary(customerFormContent.getFields().get(RedundancyFieldEnum.extra1.name).toString() + ":" + customerFormContent.getFields().get("event_title").toString());
                    relationProblemLog.setRequestType(RelationRequestType.EVENT.getCode());
                    // 硬件
                    RelationLog relationIncidentLog = new RelationLog();
                    relationIncidentLog.setStatus(incidentFieldMap.get(STATUS).toString());
                    relationIncidentLog.setStartDate(DateUtils.formatDate((Date) incidentFieldMap.get(CREATED_TIME), YYYY_MM_DD_HH_MM_SS));
                    relationIncidentLog.setCurrentHandlerId((String) incidentFieldMap.get(CustomerBizInterceptor.currentUserThread.get().getUserId()));
                    relationIncidentLog.setEndDate(null);
                    relationIncidentLog.setRequestNo(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                    relationIncidentLog.setRelationNo(bizNo);
                    relationIncidentLog.setRelationType("相关");
                    relationIncidentLog.setRequestSummary(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString() + ":" + incidentFieldMap.get("title").toString());
                    relationIncidentLog.setRequestType(RelationRequestType.CHM.getCode());
                    Arrays.asList(relationIncidentLog, relationProblemLog).forEach(relationLog -> {
                        try {
                            relationLogService.save(relationLog);
                        } catch (DuplicateKeyException e) {
                            throw new BusinessException(String.format("编号：%s已关联过编号：%s的单子， 请重新选择关联！", relationLog.getRequestNo(), relationLog.getRelationNo()));
                        }
                    });
                    OperationDetails details = OperationDetails.builder().operationType("硬件报障").bizNo(incidentFieldMap.get(RedundancyFieldEnum.extra1.name).toString()).description(CustomerBizInterceptor.currentUserThread.get().getPname() + "填写事件单，单号：" + bizNo).build();
                    operationDetailsService.save(details);
                    DesignBizChm designBizChm=new DesignBizChm();
                    designBizChm.setId(Long.valueOf(id));
                    designBizChm.setStatus("已转事件单");
                    designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
                    CustomerFormContent customerFormContent1=new CustomerFormContent();
                    customerFormContent1.setCode(WorkOrderInformation.chm_task.getCode());
                    Map<String,Object> mapFiles=new HashMap<>();
                    mapFiles.put("status","已转事件单");
                    mapFiles.put("id",id);
                    DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                            .eq(DesignBizJsonData.COL_BIZ_ID, id)
                            .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", WorkOrderInformation.chm_task.getCode())));
                    String dataJson = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), mapFiles);
                    customerFormContent1.setDataJson(dataJson);
                    customerFormContent1.setFields(mapFiles);
                    Integer bizId1= customerFormService.dataOperation(WorkOrderInformation.chm_task.getCode(),customerFormContent1);
                    String instanceId = incidentFieldMap.get("instance_id").toString();
                    Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
                    Map<String, Object> map = new HashMap<>();
                    map.put("reCode", "3");
                    map.put("dealUser", CustomerBizInterceptor.currentUserThread.get().getpId());
                    taskService.complete(task.getId(), map);
                }
            }
            // 事件单创建环节不需要保存这个信息
            if (!code.equals(WorkOrderInformation.incident.getCode()) && !code.equals(WorkOrderInformation.change.getCode()) && !code.equals(WorkOrderInformation.changeTask.getCode())) {
                //移动端发起硬件报障不添加
                if (code.equals(WorkOrderInformation.chm_task.getCode())) {
                    String source = customerFormContent.getFields().get("source") == null ? "" : customerFormContent.getFields().get("source").toString();
                    if (!"1".equals(source)) {
                        OperationDetails details = OperationDetails.builder().operationType(formName).bizNo(bizNo).oldValue("待提交").newValue("待提交").description(CustomerBizInterceptor.currentUserThread.get().getPname() + "填写申请信息~").build();
                        operationDetailsService.save(details);
                    }
                } else {
                    OperationDetails details = OperationDetails.builder().operationType(formName).bizNo(bizNo).oldValue("待提交").newValue("待提交").description(CustomerBizInterceptor.currentUserThread.get().getPname() + "填写申请信息~").build();
                    operationDetailsService.save(details);
                }

            }
            if (code.equals(WorkOrderInformation.incident.getCode())) {
                IncidentSubEvent incidentSubEvent = new IncidentSubEvent();
                incidentSubEvent.setSubmitTime(DateUtils.getTime());
                incidentSubEvent.setEventNo(bizNo);
                incidentSubEventService.insertIncidentSubEvent(incidentSubEvent);
            }
        }
        MybatisPlusConfig.customerTableName.remove();

        return bizId.intValue();
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Integer dataOperation(String code, CustomerFormContent customerFormContent) {
        return dataOperation(null, null, code, customerFormContent);
    }

    @Override
    public AjaxResult deleteById(String code, Integer id) {
        dynamicTableName(code);
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", id).set("status", CANCEL.getInfo()).set("stage", stageMap.get(CANCEL.getInfo())).set("close_time", DateUtils.getDate()).set("current_deal_id", null));
            // 取消添加工作详情
            List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("id", id));
            if (CollectionUtils.isEmpty(problemList)) {
                throw new BusinessException("表单数据不存在!");
            }
//            String userId=CustomerBizInterceptor.currentUserThread.get().getUserId();
//            OgPerson op = iOgPersonService.selectOgPersonById(userId);
            String bizNo = problemList.get(0).get(RedundancyFieldEnum.extra1.name).toString();
            OperationDetails operationDetails = OperationDetails.builder()
                    .operationType(WorkOrderInformation.getName(code))
                    .bizNo(bizNo)
                    .description(String.format("%s取消了编号%s的数据", CustomerBizInterceptor.currentUserThread.get().getPname(), bizNo))
                    .build();
            operationDetailsService.save(operationDetails);
            MybatisPlusConfig.customerTableName.remove();
            return AjaxResult.success();
        } else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
            customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", id).set("status", ProblemTaskStatus.CANCEL.getInfo()).set("stage", stageMap.get(ProblemTaskStatus.CANCEL.getInfo())).set("close_time", DateUtils.getDate()).set("current_handler_id", null));
            // 取消添加工作详情
            List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("id", id));
            if (CollectionUtils.isEmpty(problemList)) {
                throw new BusinessException("表单数据不存在!");
            }
//            String userId=CustomerBizInterceptor.currentUserThread.get().getUserId();
//            OgPerson op = iOgPersonService.selectOgPersonById(userId);
            String bizNo = problemList.get(0).get(RedundancyFieldEnum.extra1.name).toString();
            OperationDetails operationDetails = OperationDetails.builder()
                    .operationType(WorkOrderInformation.getName(code))
                    .bizNo(bizNo)
                    .description(String.format("%s取消了编号%s的数据", CustomerBizInterceptor.currentUserThread.get().getPname(), bizNo))
                    .build();
            operationDetailsService.save(operationDetails);
            MybatisPlusConfig.customerTableName.remove();
            return AjaxResult.success();
        } else {
            int resultItem = customerFormMapper.deleteById(id);
            MybatisPlusConfig.customerTableName.remove();
            return AjaxResult.success("删除成功->受影响的条目：" + resultItem);
        }
    }

    @Override
    public AjaxResult getParaValue(String paraName) {
        PubPara pubPara = pubParaMapper.checkParaNameUnique(paraName);
        List<PubParaValue> pubParaValues = pubParaValueMapper.selectPubParaValueById(pubPara.getParaId());
        return AjaxResult.success(pubParaValues);
    }

    @Override
    public Map<String, Object> getFormInfo(String code, Integer id, String processId) {
        List<String> formDataJson = new ArrayList<>();
        List<String> formKeys = new ArrayList<>();
        List<Map<String, Object>> jsonList = new ArrayList<>();

        formKeys.add(code);
        if (StringUtils.isNotEmpty(processId)) {
            List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processId).finished().orderByHistoricTaskInstanceStartTime().asc().list();
            //过滤任务节点没有配置表单key的元素
            historicTaskInstanceList.stream().filter(a -> {
                return StringUtils.isNotEmpty(a.getFormKey());
            }).forEach(a -> formKeys.add(a.getFormKey()));

            List<String> distinctFormKey = formKeys.stream().distinct().collect(Collectors.toList());
            distinctFormKey.stream().forEach(a -> {
                dynamicTableName(a);
                Integer formBusinessKey = customerFormMapper.selectOneByProcessId(processId);
                DesignBizJsonData designBizJsonData1 = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, formBusinessKey).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
                Map<String, Object> jsonMap = new HashMap<>();
                String jsonData = designBizJsonData1.getJsonData();
                jsonData = dealFromInfo(code, jsonData, id);
                jsonMap.put("formJson", jsonData);
                jsonMap.put("isCurrentTaskNode", true);
                jsonList.add(jsonMap);
                MybatisPlusConfig.customerTableName.remove();
            });
        } else {
            dynamicTableName(code);
            DesignBizJsonData designBizJsonData1 = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, id).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
            Map<String, Object> jsonMap = new HashMap<>();
            String jsonData = designBizJsonData1.getJsonData();
            jsonData = dealFromInfo(code, jsonData, id);
            jsonMap.put("formJson", jsonData);
            jsonMap.put("isCurrentTaskNode", true);
            jsonList.add(jsonMap);
            MybatisPlusConfig.customerTableName.remove();
        }
        dynamicTableName(code);
        List<Map<String, Object>> statusList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("status").eq("id", id));
        Map<String, String> formStatus = new HashMap<>();
        formStatus.put("lable", "工单状态");
        formStatus.put("index", "lastIndex");
        formStatus.put("value", Optional.ofNullable(statusList).isPresent() ? statusList.get(0).get("status").toString() : "");

        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
        //获取业务表编号
        List<Map<String, Object>> bizNos = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("id", id));
        Map<String, String> bizNoMap = new HashMap<>();
        bizNoMap.put("lable", formName + "编号");
        bizNoMap.put("index", "firstIndex");
        bizNoMap.put("value", Optional.ofNullable(bizNos).isPresent() ? bizNos.get(0).get(RedundancyFieldEnum.extra1.name).toString() : "");

        List<Map<String, String>> appenJsondata = new ArrayList<>();
        appenJsondata.add(formStatus);
        appenJsondata.add(bizNoMap);
        MybatisPlusConfig.customerTableName.remove();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("jsonData", jsonList);
        resultMap.put("appenJsondata", appenJsondata);
        return resultMap;
    }

    // 事件单审批查询接口转换二线处理部门、二线处理人员等字段
    public String dealFromInfo(String code, String jsonData, Integer id) {
        if (code.equals(WorkOrderInformation.incident.getCode())) {
            List<Map<String, Object>> secondMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("second_deal_org", "second_deal_person").eq("id", id));
            if (!CollectionUtils.isEmpty(secondMap) && StringUtils.isNotEmpty(secondMap.get(0))) {
                if (StringUtils.isNotEmpty(secondMap.get(0).get(EventFieldConstants.SECOND_DEAL_ORG))) {
                    OgOrg org = iSysDeptService.selectDeptByCode(String.valueOf(secondMap.get(0).get(EventFieldConstants.SECOND_DEAL_ORG)));
                    if (org != null) {
                        Map<String, Object> incident = new HashMap<>();
                        incident.put(EventFieldConstants.SECOND_DEAL_ORG, org.getOrgName());
                        jsonData = VueDataJsonUtil.analysisDataJson(jsonData, incident);
                    }
                }
                if (StringUtils.isNotEmpty(secondMap.get(0).get(EventFieldConstants.SECOND_DEAL_PERSON))) {
                    OgPerson person = ogPersonService.selectOgPersonById(String.valueOf(secondMap.get(0).get(EventFieldConstants.SECOND_DEAL_PERSON)));
                    if (person != null) {
                        Map<String, Object> incident = new HashMap<>();
                        incident.put(EventFieldConstants.SECOND_DEAL_PERSON, person.getpName());
                        jsonData = VueDataJsonUtil.analysisDataJson(jsonData, incident);
                    }
                }
            }
        }
        if (code.equals(WorkOrderInformation.problem.getCode()) && checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())) {
            jsonData = VueDataJsonUtil.setDisable(jsonData, false);
        }
        return jsonData;
    }

    /**
     * 构造详情页面按钮信息
     *
     * @param code      业务主表
     * @param id        业务ID
     * @param processId 流程实例ID
     * @param resultMap 详情返回结果
     */
    private void buildFormInfoButton(String code, Integer id, String processId, Map<String, Object> resultMap, List<Map<String, Object>> statusList) {
        //如果流程实例为空  则说明改流程信息未进入流程
        List<FormInfoButtonVo> formInfoButtonVos = new ArrayList<>();

        if (StringUtils.isEmpty(processId)) {
            if (!CANCEL.getInfo().equals(statusList.get(0).get("status"))) {
                formInfoButtonVos.add(FormInfoButtonVo.builder().buttonName(CustomerFlowConstants.addButtonName)
                        .buttonUrlPath(CustomerFlowConstants.addButtonPath)
                        .build());
                formInfoButtonVos.add(FormInfoButtonVo.builder().buttonName(CustomerFlowConstants.submitApplyButtonName)
                        .buttonUrlPath(CustomerFlowConstants.suspendFlowButtonPath)
                        .build());
            }
        } else {

        }
        formInfoButtonVos.add(FormInfoButtonVo.builder()
                .buttonName(CustomerFlowConstants.closeButtonName)
                .buttonUrlPath(CustomerFlowConstants.closeButtonPath)
                .build());
    }


    @Override
    public AjaxResult getFormJson(String code) {
        Map<String, Object> map = new HashMap<>();
        // 获取design_form_version表中的主键ID
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        //根据表单版本ID获取表单json数据
        Map<String, Object> formJsonInfo = customerFormMapper.getFormJsonInfo(currentTableInfo.get("id"));

        if (WorkOrderInformation.incident.getCode().equals(code)) {
            String json = (String) formJsonInfo.get("json");
            // 新建页面隐藏事件单解决环节标签
            json = VueDataJsonUtil.showLabel(json, EventFieldConstants.EVENT_SOLVE_LABEL_ID, false);
            // 新建环节默认填充当前登录人、机构等信息
            OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
            Map<String, Object> reportMap = new HashMap<>();
            reportMap.put("report_org", ogUser.getOrgname());
            reportMap.put("report_person", ogUser.getPname());
            reportMap.put("report_phone", ogUser.getMobilPhone());
            json = VueDataJsonUtil.analysisDataJson(json, reportMap);
            formJsonInfo.put("json", json);
        }else if (WorkOrderInformation.workerOrderRules.getCode().equals(code)||WorkOrderInformation.workerOrder.getCode().equals(code)){
            String json = (String) formJsonInfo.get("json");
            //规则工单模块新建环节默认填充当前登录人所在部门
            OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
            Map<String, Object> reportMap = new HashMap<>();
            reportMap.put("worker_order_dept",ogUser.getOrgname());
            json = VueDataJsonUtil.analysisDataJson(json, reportMap);
            formJsonInfo.put("json", json);
        }	else if ("spv_apply".equals(code)) {
            String json = (String) formJsonInfo.get("json");
            // 新建页面隐藏事件单解决环节标签
            json = VueDataJsonUtil.showLabel(json, EventFieldConstants.EVENT_SOLVE_LABEL_ID, false);
            OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
            Map<String, Object> reportMap = new HashMap<>();
            reportMap.put("apply_depart", ogUser.getOrgname());
            reportMap.put("apply_user", ogUser.getPname());
            json = VueDataJsonUtil.analysisDataJson(json, reportMap);
            formJsonInfo.put("json", json);
        }

        //获取当前表单所有已配置的步骤条
        List<FormStepStatus> formStepStatusList = formStepStatusService.list(Wrappers.<FormStepStatus>query().select(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")).groupBy(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).orderByAsc(FormStepStatus.COL_SORT));

        map.put("formStepStatusList", formStepStatusList);
        map.put("formJsonInfo", formJsonInfo);
        return AjaxResult.success(map);
    }

    private final String problemDesc =
            "1、问题现象及解决要求：问题现象需详细描述事件发生情况、趋势分析或相关潜在风险的具体情况包括事件再次发生的风险。解决 \n" +
                    "      要求需明确 \n" +
                    "      问题解决的目标。 \n" +
                    "2、影响范围：描述影响信息科技系统运维服务的具体系统以及可能对业务造成的影响。 \n" +
                    "3、故障处理情况：针对从突发事件发起的问题，简要描述事件临时解决方案描述。\n" +
                    "4、问题处理的相关团队及人员：由问题发起人初步确认问题解决对应的相关团队。\n" +
                    "5、初步分析结果（可选）：由问题发起人提交初步确认的分析结果。";

    @Override
    public AjaxResult incidentCreate(String fromCode, String code, String id) {
        Map<String, Object> map = new HashMap<>();
        dynamicTableName(fromCode);
        // 获取design_form_version表中的主键ID
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        //根据表单版本ID获取表单json数据
        Map<String, Object> formJsonInfo = customerFormMapper.getFormJsonInfo(currentTableInfo.get("id"));

        List<Map<String, Object>> problemList = new ArrayList<>();
        //事件单发起 问题单变更单
        if (fromCode.equals(WorkOrderInformation.incident.getCode())) {
            problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(
                    RedundancyFieldEnum.extra1.name, EventFieldConstants.SYSTEM_NAME,
                    EventFieldConstants.EVENT_TITLE, EventFieldConstants.EVENT_INFO, EventFieldConstants.SOLVE_PLAN,
                    EventFieldConstants.EVENT_CATEGORY, EventFieldConstants.EVENT_SUBCLASS, EventFieldConstants.EVENT_ENTRY,
                    EventFieldConstants.EVENT_SUBENTRY1, EventFieldConstants.EVENT_SUBENTRY2).eq(ID, id));
        }
        //问题单发起变更
        if (fromCode.equals(WorkOrderInformation.problem.getCode())) {
            problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name,
                    "problem_title", "problem_description").eq(ID, id));
        }
        // 问题任务发起变更
        if (fromCode.equals(WorkOrderInformation.problem_task.getCode())) {
            problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name,
                    "problem_desc", "problem_content").eq(ID, id));
        }
        String json = formJsonInfo.get("json").toString();
        //事件单
        if (fromCode.equals(WorkOrderInformation.incident.getCode()) && WorkOrderInformation.change.getCode().equals(code)) {
            //变更标题
            String tittle = problemList.get(0).get(EventFieldConstants.EVENT_TITLE).toString();
            //变更描述
            String desc = problemList.get(0).get(EventFieldConstants.EVENT_INFO).toString();//事件描述
            //变更依据
            String incidentNo = problemList.get(0).get(RedundancyFieldEnum.extra1.name).toString();
            Map<String, Object> mp = new HashMap<>();
            mp.put("title", tittle);
            mp.put("description", desc);
            mp.put("basis", incidentNo);
            mp.put("basisType", "1");
            //json = VueDataJsonUtil.setJsonValue(json, mp);
            return changeService.init(fromCode, id, CustomerBizInterceptor.currentUserThread.get().getUserId(), mp, false);
        }
        //事件单发起 问题单
        if (fromCode.equals(WorkOrderInformation.incident.getCode()) && WorkOrderInformation.problem.getCode().equals(code)) {
            // 查询发起人信息
            String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();
            OgPerson op = iOgPersonService.selectOgPersonById(userId);
            List<OgGroup> listGroup = iSysWorkService.selectGroupByUserId(userId);
            Boolean isAdmin = false;
            for (OgGroup ogp : listGroup) {
                if (Arrays.asList(CustomerFlowConstants.PROBLEM_ADMIN,
                        CustomerFlowConstants.TINGJING_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.CHENDU_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.NANJIN_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.SUZHOU_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.NINGBO_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.BEIJIN_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.SHENZHENG_BRANCH_PROBLEM_ADMIN,
                        CustomerFlowConstants.HANZHOU_BRANCH_PROBLEM_ADMIN
                ).contains(ogp.getGroupId())) {
                    isAdmin = true;
                    break;
                }
            }
            List<Map<String, String>> personList = iOgPersonService.selectOgPersons(null);
            // 将问题单发起人放置到json中
            String s = VueDataJsonUtil.analysisDataJsonSelect(json, personList, userId);
            // 如果当前登录人非管理员则问题发起人禁用
            if (isAdmin) {
                s = VueDataJsonUtil.setDisable(s, false);
            }

            QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dept_id",op.getOrgId());
            List<ChangeDeptPersonEntity> changeDeptPersonEntityList = iChangePersonService.list(queryWrapper.orderByDesc("create_date"));
            // 问题单发起部室
            Map<String, Object> oriDeptMap = new HashMap<>();
            Map<String, Object> oriDMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(changeDeptPersonEntityList)) {
                String deptPersonId = changeDeptPersonEntityList.get(0).getDeptPerson();
                OgPerson opdept = iOgPersonService.selectOgPersonById(deptPersonId);
                if (opdept != null) {
                    // 设置问题发起部室
                    JSONArray json1 = JSON.parseArray(s);
                    JSONObject o = (JSONObject) json1.get(0);
                    oriDMap.put("label", changeDeptPersonEntityList.get(0).getOrgName());
                    oriDMap.put("value", changeDeptPersonEntityList.get(0).getOrgId());
                    List<Map<String, Object>> list = Lists.newArrayList();
                    list.add(oriDMap);
                    VueDataJsonUtil.analysisDataJsonForProblemName(o, list, ORI_DEP_ID);
                    s = json1.toString();

                    // 设置问题发起部室
                    JSONArray json2 = JSON.parseArray(s);
                    JSONObject o2 = (JSONObject) json2.get(0);
                    oriDeptMap.put("label", opdept.getpName());
                    oriDeptMap.put("value", opdept.getpId());
                    List<Map<String, Object>> list2 = Lists.newArrayList();
                    list2.add(oriDeptMap);
                    VueDataJsonUtil.analysisDataJsonForProblemName(o2, list2, ORI_DEP_MANAGER_ID);
                    s = json2.toString();
                }
            }
            Map<String, Object> mp = new HashMap<>();
            mp.put("problem_source", "1");//来源
            mp.put("temp_solutions", problemList.get(0).get(EventFieldConstants.SOLVE_PLAN));//解决方案
            mp.put("problem_title", problemList.get(0).get(EventFieldConstants.EVENT_TITLE));//事件标题
            mp.put("problem_description", problemList.get(0).get(EventFieldConstants.EVENT_INFO) + "\n" + problemDesc);//事件描述
            Object systemName = problemList.get(0).get(EventFieldConstants.SYSTEM_NAME);
            List<String> systemList = Lists.newArrayList();
            if (ObjectUtil.isNotEmpty(systemName)) {
                systemList.add(systemName.toString());
            }
            mp.put("system_id", systemList);//应用系统
            mp.put("problem_category", problemList.get(0).get(EventFieldConstants.EVENT_CATEGORY));//类别
            mp.put("problem_subclz", problemList.get(0).get(EventFieldConstants.EVENT_SUBCLASS));//子类
            mp.put("problem_entry", problemList.get(0).get(EventFieldConstants.EVENT_ENTRY));//条目
            mp.put("problem_subentry1", problemList.get(0).get(EventFieldConstants.EVENT_SUBENTRY1));//条目
            mp.put("problem_subentry2", problemList.get(0).get(EventFieldConstants.EVENT_SUBENTRY2));//条目
            json = VueDataJsonUtil.setJsonValue(s, mp);
        }
        if (fromCode.equals(WorkOrderInformation.problem.getCode()) && code.equals(WorkOrderInformation.change.getCode())) {
            //变更标题
            String tittle = problemList.get(0).get("problem_title").toString();
            //变更描述
            String desc = problemList.get(0).get("problem_description").toString();//事件描述
            //变更依据
            String incidentNo = problemList.get(0).get(RedundancyFieldEnum.extra1.name).toString();
            Map<String, Object> mp = new HashMap<>();
            mp.put("title", tittle);
            mp.put("description", desc);
            mp.put("basis", incidentNo);
            mp.put("basisType", "2");
            //json = VueDataJsonUtil.setJsonValue(json, mp);
            return changeService.init(fromCode, id, CustomerBizInterceptor.currentUserThread.get().getUserId(), mp, false);
        }
        if (fromCode.equals(WorkOrderInformation.problem_task.getCode()) && code.equals(WorkOrderInformation.change.getCode())) {
            //变更标题
            String tittle = problemList.get(0).get("problem_desc").toString(); // 任务说明
            //变更描述
            String desc = problemList.get(0).get("problem_content").toString();//任务内容
            //变更依据
            String incidentNo = problemList.get(0).get(RedundancyFieldEnum.extra1.name).toString();
            Map<String, Object> mp = new HashMap<>();
            mp.put("title", tittle);
            mp.put("description", desc);
            mp.put("basis", incidentNo);
            mp.put("basisType", "2");
            //json = VueDataJsonUtil.setJsonValue(json, mp);
            return changeService.init(fromCode, id, CustomerBizInterceptor.currentUserThread.get().getUserId(), mp, false);
        }
        if (fromCode.equals(WorkOrderInformation.chm_task.getCode()) && code.equals(WorkOrderInformation.incident.getCode())) {
            dynamicTableName(WorkOrderInformation.chm_task.getCode());
            QueryWrapper<DesignBizChm> wrapper = Wrappers.<DesignBizChm>query()//
                    .select("*")
                    .eq("id", id);
            DesignBizChm designBizChm = designBizChmMapper.selectOne(wrapper);
            String title = designBizChm.getTitle();
            String fault_Description = designBizChm.getFaultDescription();
            Map<String, Object> m = new HashMap<>();
            m.put("event_title", title);
            m.put("event_info", fault_Description);
            String reportDement=designBizChm.getReportDepartment();
            ChmBasedata chmBasedata=iChmBasedataService.selectChmBasedataById(Long.valueOf(reportDement));
            m.put("report_org",chmBasedata.getOrgName());
            m.put("report_person",designBizChm.getContact());
            m.put("report_phone",designBizChm.getContactDetails());
            json = VueDataJsonUtil.setJsonValue(json, m);
        }
        formJsonInfo.put("json", json);
        //获取当前表单所有已配置的步骤条
        List<FormStepStatus> formStepStatusList = formStepStatusService.list(Wrappers.<FormStepStatus>query().select(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")).groupBy(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).orderByAsc(FormStepStatus.COL_SORT));
        map.put("code", code);
        map.put("formStepStatusList", formStepStatusList);
        map.put("formJsonInfo", formJsonInfo);
        map.put("fromCode", fromCode);
        map.put("id", id);
        return AjaxResult.success(map);
    }
//    @Override
//    public  AjaxResult problemCreate(String code, String id){
//        Map<String, Object> map = new HashMap<>();
//        dynamicTableName(WorkOrderInformation.problem.getCode());
//        // 获取design_form_version表中的主键ID
//        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
//        //根据表单版本ID获取表单json数据
//        Map<String, Object> formJsonInfo = customerFormMapper.getFormJsonInfo(currentTableInfo.get("id"));
//
//        List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name,
//                "problem_title","problem_description").eq(ID, id));
//        String json=formJsonInfo.get("json").toString();
//        //变更标题
//        String tittle=problemList.get(0).get("problem_title").toString();
//        //变更描述
//        String desc=problemList.get(0).get("problem_description").toString();//事件描述
//        //变更依据
//        String incidentNo=problemList.get(0).get(RedundancyFieldEnum.extra1.name).toString();
//        Map<String,Object> mp=new HashMap<>();
//        mp.put("title",tittle);
//        mp.put("description",desc);
//        mp.put("basis",incidentNo);
//        mp.put("basisType","2");
//        json=VueDataJsonUtil.setJsonValue(json,mp);
//        formJsonInfo.put("json",json);
//        //获取当前表单所有已配置的步骤条
//        List<FormStepStatus> formStepStatusList = formStepStatusService.list(Wrappers.<FormStepStatus>query().select(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")).groupBy(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).orderByAsc(FormStepStatus.COL_SORT));
//        map.put("code",code);
//        map.put("formStepStatusList", formStepStatusList);
//        map.put("formJsonInfo", formJsonInfo);
//        return AjaxResult.success(map);
//    }

    /**
     * 添加校验
     *
     * @param variables
     */
    private void addCheckPeopleAndParam(String code, Map<String, Object> variables, String businessKey) {
        List<String> codeList = Arrays.asList("tinyWeb_server", "tinyWeb_fault_solveA", "tinyWeb_db_recoverA");
        if (codeList.contains(code)) {
            List<Map<String, Object>> bizNos = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra6.name).eq("id", businessKey));
            if (CollectionUtils.isEmpty(bizNos)) {
                throw new BusinessException("tinyWeb必须包含审批人<check_people>表单");
            }
            //第一节点的审批人
            variables.put("frist_approver", bizNos.get(0).get("check_people"));
        }

        // “问题来源”是事件管理或重大事件，在提交问题时，要求必须有关联的事件
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name, PROBLEM_SOURCE).eq(ID, businessKey));
            if (CollectionUtils.isEmpty(problemList)) {
                throw new BusinessException("问题单不存在!");
            }
            // 校验问题单来源 人工发起的问题单，如果来源是事件管理或重大事件则必须关联到相关的事件单，否则不能提交 0-人工页面发起
            if (Arrays.asList(EVENT_MANAGE, MAJOR_EVENT).contains(problemList.get(0).get(PROBLEM_SOURCE).toString())) {
                // 查询关联的事件单 返回特殊码500跳转到关联事件单列表页面进行关联
                List<RelationLog> relationLogs = relationLogMapper.selectRelationLogList(RelationLog.builder().relationNo(problemList.get(0).get(RedundancyFieldEnum.extra1.name).toString()).requestType(RelationRequestType.EVENT.getCode()).build());
                if (CollectionUtils.isEmpty(relationLogs)) {
                    throw new BusinessException("300", "请查询相关事件并关联!");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult processSubmit(String code, String businessKey, String
            version, Map<String, Object> variables) {
        log.info("into processSubmit.Transactional.code:"+code+",businessKey:"+businessKey);
        version = String.valueOf(customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null).get("version"));
        DesignBizJsonData currentNodeFormInfo = null;
        String loginUser = "";
        if (code.equals(WorkOrderInformation.chm_task.getCode())) {
            //硬件报障接口获取发起人
            loginUser = StringUtils.isEmpty(variables.get("userNo")) ? CustomerBizInterceptor.currentUserThread.get().getUserId() : variables.get("userNo").toString();
            variables.put("dealGroup", CustomerFlowConstants.CHM_IT_GROUP);
        } else {
            loginUser = CustomerBizInterceptor.currentUserThread.get().getUserId();
        }
        if (WorkOrderInformation.problem.getCode().equals(code)) {
            currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                    .eq(DesignBizJsonData.COL_BIZ_ID, businessKey)
                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));

            String js = currentNodeFormInfo.getJsonData();
            String userId = VueDataJsonUtil.resultValue(JSONArray.parseArray(js).getJSONObject(0), "originator_id");
//            if (!loginUser.equals(userId)) {
//                throw new BusinessException("问题单仅限创建人提交！");
//            }
        }
        if (WorkOrderInformation.problem_task.getCode().equals(code)) {
            //问题单子任务
            currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                    .eq(DesignBizJsonData.COL_BIZ_ID, businessKey)
                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));

            String js = currentNodeFormInfo.getJsonData();
            String userId = VueDataJsonUtil.resultValue(JSONArray.parseArray(js).getJSONObject(0), "assistant_id");
            String createdBy = VueDataJsonUtil.resultValue(JSONArray.parseArray(js).getJSONObject(0), "created_by");
            variables.put("dealUser", userId);
            variables.put("createUser", createdBy);
            version = String.valueOf(customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null).get("version"));
        }
        if (WorkOrderInformation.problem.getCode().equals(code)) {
            version = String.valueOf(customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null).get("version"));
        }
        
        if ("spv_apply".equals(code) && !variables.containsKey("reCode")) {
        	variables.put("reCode", "0");
        }
        
        identityService.setAuthenticatedUserId(loginUser);
        /*if (code.equals(WorkOrderInformation.incident.getCode())) {
            // IT服务台分派角色
            variables.put("receptionId", CustomerFlowConstants.ASSIGN_ROLE);
        }*/
        dynamicTableName(code);
        //发起人选择审批人提交 问题单来源校验
        addCheckPeopleAndParam(code, variables, businessKey);
        // 问题单提交设置流程流转分支和变量
        this.setVariables(code, businessKey, variables);
        //启动流程
        log.info("start processSubmit.Transactional.code:"+code+",businessKey:"+businessKey);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(code, String.format("%s_%s_%s", code, version, businessKey), variables);
        //把流程实例ID设置到业务表中
        // dynamicTableName(code);
        if (MybatisPlusConfig.customerTableName.get() == null) {
            dynamicTableName(code);
        }
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        customerFormMapper.setInstanceId(instance.getId(), businessKey);
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        List<Map<String, Object>> bizNos = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("id", businessKey));
        if (CollectionUtils.isEmpty(bizNos)) {
            throw new BusinessException("业务数据不存在!");
        }
        log.info("start processSubmit.Transactional.code:"+code+",businessKey:"+businessKey+",bizNos:"+bizNos);
        if (Optional.ofNullable(task).isPresent()) {
            FormStatusActivityNode formStatusActivityNode = formStatusActivityNodeService.getOne(Wrappers.<FormStatusActivityNode>query().eq(FormStatusActivityNode.COL_ACTIVITY_NODE_ID, task.getTaskDefinitionKey()).eq(FieldActivityNode.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
            if (!Optional.ofNullable(formStatusActivityNode).isPresent()) {
                throw new BusinessException("此工单没有在" + task.getName() + "节点配置状态~~");
            }
            String formStatus = formStatusActivityNode.getBizStatusName();
            String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
            List<Map<String, Object>> records = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq(INSTANCE_ID, instance.getId()));
            if (!code.equals(WorkOrderInformation.incident.getCode()) &&
                    !code.equals(WorkOrderInformation.change.getCode()) &&
                    !code.equals(WorkOrderInformation.changeTask.getCode())) {
                //移动端发起硬件报障跳过
                if (code.equals(WorkOrderInformation.chm_task.getCode())) {
                    String source = records.get(0).get("source") == null ? "" : records.get(0).get("source").toString();
                    if (!"1".equals(source)) {
                        //构造操作详情
                        OperationDetails details = OperationDetails.builder().operationType(formName).bizNo(String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)))
                                .description(CustomerBizInterceptor.currentUserThread.get().getPname() + "发起了流程申请 将状态从：待提交更改为：" + formStatus)
                                .oldValue("待提交")
                                .newValue(formStatus)
                                .build();
                        operationDetailsService.save(details);
                    }
                } else {
                    //构造操作详情
                    OperationDetails details = OperationDetails.builder().operationType(formName).bizNo(String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)))
                            .description(CustomerBizInterceptor.currentUserThread.get().getPname() + "发起了流程申请 将状态从：待提交更改为：" + formStatus)
                            .oldValue("待提交")
                            .newValue(formStatus)
                            .build();
                    operationDetailsService.save(details);
                }
            }
            if (WorkOrderInformation.problem.getCode().equals(code)) {

                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(ID, businessKey).set(STATUS, formStatus).set(STAGE, stageMap.get(formStatus)).set(SUBMIT_TIME, DateUtils.dateTimeNow(YYYY_MM_DD)));
                Map<String, Object> map = new HashMap<>();
                map.put(STAGE, stageMap.get(formStatus));
                map.put(SUBMIT_TIME, DateUtils.dateTimeNow(YYYY_MM_DD));
                String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
                designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, businessKey).set("json_data", jsonData));
            } else if (WorkOrderInformation.problem_task.getCode().equals(code)) {
                String userId = variables.get("dealUser").toString();
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", businessKey).set("status", formStatus).set("stage", formStatus).set("current_handler_id", userId));
                Map<String, Object> map = new HashMap<>();
                map.put("stage", formStatus);
                map.put("current_handler_id", userId);
                String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
                designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, businessKey).set("json_data", jsonData));
                // }  else if (WorkOrderInformation.changeTask.getCode().equals(code)) {
                //  customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", businessKey).set("status", formStatus).set("sequence", businessKey));
            } else if (WorkOrderInformation.incident.getCode().equals(code)) {
                currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                        .eq(DesignBizJsonData.COL_BIZ_ID, businessKey)
                        .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));

                Map<String, Object> map = new HashMap<>();
                map.put("stage", formStatus);

                // 提交时插入数据到事件耗时详情表
                String dealPerson = "";
                String nextNodeName = "";
                if (StringUtils.isNotEmpty(variables.get("dealReceive"))) {
                    dealPerson = variables.get("dealReceive").toString();
                    nextNodeName = "处理人接单";
                } else if (StringUtils.isNotEmpty(variables.get("receptionReceive"))) {
                    dealPerson = variables.get("receptionReceive").toString();
                    nextNodeName = "服务台接单";
                }

                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", businessKey).set("status", formStatus).set("extra5", dealPerson));

                String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
                designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, businessKey).set("json_data", jsonData));
                // 调用事件单发送邮件方法
                Map<String, Object> params = new HashMap<>();
                params.put("businessKey", businessKey);
                eventForeignService.sendEmailMessage(dealPerson, params);

                eventConsumeDetailsService.saveEventConsumeDetails(String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)), CustomerBizInterceptor.currentUserThread.get().getUserId(), dealPerson, "开始", nextNodeName, DateUtils.getTime());

                operationDetailsService.saveOperationDetails(String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)), EventOperationType.CREATE_AND_ASSIGN.getInfo(), EventOperationType.CREATE_AND_ASSIGN.getInfo(), DateUtils.getNowDate());

            } else {
                dynamicTableName(code);
                String taskInitiator = String.valueOf(variables.get("taskInitiator"));
                if(StringUtils.isNotEmpty(taskInitiator)) {
                    customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", businessKey).set("status", formStatus).set("extra5", taskInitiator));
                }else {
                    customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", businessKey).set("status", formStatus));
                }
//                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", businessKey).set("status", formStatus));
            }
            // 更新关联日志表
            relationLogMapper.update(null, Wrappers.<RelationLog>update().eq("request_no", bizNos.get(0).get(RedundancyFieldEnum.extra1.getName()).toString()).set("status", formStatus));
        } else {
            String status = "已关闭";
            if (WorkOrderInformation.problem.getCode().equals(code)) {
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(ID, businessKey).set(STATUS, CLOSE.getInfo()).set(STAGE, stageMap.get(CLOSE.getInfo())));
                status = CLOSE.getInfo();
            } else {
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("id", businessKey).set("status", "已关闭"));
            }
            // 更新关联日志表
            relationLogMapper.update(null, Wrappers.<RelationLog>update().eq("request_no", bizNos.get(0).get(RedundancyFieldEnum.extra1.getName()).toString()).set("status", status).set("end_date", DateUtils.dateTimeNow()));
        }
        MybatisPlusConfig.customerTableName.remove();

        insertFlowLog("", instance.getProcessDefinitionId(), instance.getProcessInstanceId(), code, bizNos.get(0).get(RedundancyFieldEnum.extra1.getName()).toString(), "启动", "启动流程", "");
        log.info("start processSubmit.Transactional.insertIncidentSubEvent.code:"+code+",businessKey:"+businessKey+",bizNos:"+bizNos);
        if (code.equals(WorkOrderInformation.incident.getCode())) {
            IncidentSubEvent incidentSubEvent = new IncidentSubEvent();
            incidentSubEvent.setSubmitTime(DateUtils.getTime());
            incidentSubEvent.setEventNo((String) bizNos.get(0).get(RedundancyFieldEnum.extra1.getName()));
            incidentSubEventService.insertIncidentSubEvent(incidentSubEvent);
        }
        log.info("processSubmit-code:"+code);
        return AjaxResult.success();
    }

    /**
     * 提交设置条件
     *
     * @param code
     * @param businessKey
     * @param variables
     */
    private void setVariables(String code, String businessKey, Map<String, Object> variables) {
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(FOR_ERROR, ORIGINATOR_ID, SOLVER_ID).eq(ID, businessKey));
            if (CollectionUtils.isEmpty(problemList)) {
                throw new BusinessException("问题单表数据不存在!");
            }
            Map<String, Object> problemMap = problemList.get(0);
            // 已知错误 是
            if (ONE.equals(problemMap.get(FOR_ERROR).toString())) {
                variables.put(PROBLEM_FLOW_BRANCH_KEY, PROBLEM_FLOW_BRANCH_POSITIVE1);
                variables.put(SOLVER_ID, problemMap.get(SOLVER_ID));
            } else {
                // 是否已知错误 否
                // 查询问题发起人所在机构 1-分行 0-总行
                OgOrg ogOrg = iSysDeptService.selectDeptById(ogPersonService.selectOgPersonById(problemMap.get(ORIGINATOR_ID).toString()).getOrgId());
                String orgId = changePersonService.selectDept(ogOrg.getOrgId());
                if (StringUtils.isBlank(orgId)) {
                    throw new BusinessException("问题发起人所在机构不正确,非总行或分行人员!");
                }
                OgGroup ogGroup = new OgGroup();
                // 設置當前登錄人所在的機構id todo  后续会根因sysid和orgid查询
                ogGroup.setOrgId(orgId);
                ogGroup.setMemo("problem_admin_not_update");// 問題管理員組
                List<OgGroup> ogGroups = iSysWorkService.selectOgGroupList(ogGroup);
                if (CollectionUtils.isEmpty(ogGroups)) {
                    throw new BusinessException("管理员组不存在!");
                }

                variables.put(PROBLEM_FLOW_BRANCH_KEY, PROBLEM_FLOW_BRANCH_ZERO);
                variables.put(MANAGER_GROUP_ID, ogGroups.get(0).getGroupId());
            }
        } else if (code.equals(WorkOrderInformation.chm_task.getCode())) {
            //硬件
            variables.put("dealGroup", CustomerFlowConstants.CHM_IT_GROUP);//a67fc4417a60421594b2f0ea88a9716b
            variables.put("dealUser", CustomerBizInterceptor.currentUserThread.get().getUserId());
        }else {
            //常规服务目录，设置默认处理人
            variables.put("taskInitiator", CustomerBizInterceptor.currentUserThread.get().getUserId());
            variables.put("currentDealUser", CustomerBizInterceptor.currentUserThread.get().getUserId());
        }
    }


    @Override
    @CustomerButton
    public AjaxResult approvalPopUp(String code, String processId, Integer id, String type, String version) {
        String loginUser = CustomerBizInterceptor.currentUserThread.get().getUserId();
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
        if (WorkOrderInformation.problem_task.getCode().equals(code)
                && Arrays.asList(ProblemTaskStatus.CANCEL.getInfo(), ProblemTaskStatus.CLOSE.getInfo()).contains(status)
                && ObjectUtil.isEmpty(type)) {
            type = "3";
        }
        // 根据流程id查询task任务不存在则标识流程已结束或已取消，此时查询已办内容
        List<Task> taskList = new ArrayList<>();
        if(StringUtils.isEmpty(processId)) {
            type = "1";
        } else {
            taskList = taskService.createTaskQuery().processInstanceId(processId).list();
            if (code.equals(WorkOrderInformation.incident.getCode()) && CollectionUtils.isEmpty(taskList)) {
                type = "3";
            }
        }
        if ("1".equals(type)) {
            result = this.getFormInfo(code, id, processId);
            if (StringUtils.isEmpty(processId) && (status.equals("待提交") || status.equals(EventStatusEnum.CREATED.getInfo()))) {
                //构造没有走到流程中信息
                Map<String, Object> saveButton = new HashMap<>();
                saveButton.put("buttonName", CustomerFlowConstants.addButtonName);
                saveButton.put("buttonUrlPath", CustomerFlowConstants.addButtonPath);
                actionButtonList.add(saveButton);
                Map<String, Object> submitButton = new HashMap<>();
                submitButton.put("buttonName", CustomerFlowConstants.submitApplyButtonName);
                submitButton.put("buttonUrlPath", CustomerFlowConstants.submitApplyButtonPath);
                actionButtonList.add(submitButton);
                Map<String, Object> deleteButton = new HashMap<>();
                deleteButton.put("buttonName", code.equals(WorkOrderInformation.chm_task.getCode()) ? "删除" : CustomerFlowConstants.cancelButtonName);
                deleteButton.put("buttonUrlPath", CustomerFlowConstants.deleteButtonPath);
                if (code.equals(WorkOrderInformation.problem.getCode()) || code.equals(WorkOrderInformation.problem_task.getCode())) {
                    deleteButton.put("popJson", CANCEL_POP_UP);
                }
                if (code.equals(WorkOrderInformation.problem_task.getCode())) {
                    deleteButton.put("popJson", PROBLEM_TASK_CANCEL);
                }
                actionButtonList.add(deleteButton);
//                if(code.equals(WorkOrderInformation.chm_task.getCode())){
//                    Map<String, Object> caleButton = new HashMap<>();
//                    caleButton.put("buttonName", "取消");
//                    caleButton.put("buttonUrlPath","/customerForm/list/{code}");
//                    actionButtonList.add(caleButton);
//                }
                if (code.equals(WorkOrderInformation.problem.getCode())) {
                    Map<String, Object> relationMap = new HashMap<>();
                    relationMap.put("buttonName", "关联关系");
                    relationMap.put("buttonUrlPath", "/relationList");
                    actionButtonList.add(relationMap);
                }
                result.put("actionButtonList", actionButtonList);
                result.put("approveButtonList", null);
                return AjaxResult.success(result);
            } else {
                //TODO 暂不考虑挂起激活
                Map<String, Object> showHistoryPng = new HashMap<>();
                showHistoryPng.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                showHistoryPng.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                actionButtonList.add(showHistoryPng);
                //硬件报障取消逻辑
                if (code.equals(WorkOrderInformation.chm_task.getCode()) && !status.equals("取消") && !status.equals("已关闭")) {
                    //任务发起人 添加取消功能
                    dynamicTableName(code);
                    List<Map<String, Object>> problemTaskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("created_by", STATUS).eq(ID, id));
                    if (StringUtils.equals(loginUser, problemTaskList.get(0).get("created_by").toString())) {
                        buildChmCancelButton(actionButtonList);
                    }
                }
                //事件单撤销逻辑
                if (code.equals(WorkOrderInformation.incident.getCode()) && !status.equals("取消") && !status.equals("已关闭")) {
                    //任务发起人 添加取消功能
                    dynamicTableName(code);
                    List<Map<String, Object>> problemTaskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("created_by", STATUS).eq(ID, id));
                    if (StringUtils.equals(loginUser, problemTaskList.get(0).get("created_by").toString())) {
                        buildInstCancelButton(actionButtonList);
                    }
                }
                result.put("actionButtonList", actionButtonList);
                result.put("approveButtonList", null);
                return AjaxResult.success(result);
            }

        } else if ("3".equals(type)) {
            result = this.getFormInfo(code, id, processId);
            Map<String, Object> showHistoryPng = new HashMap<>();
            if (StringUtils.isNotEmpty(processId)) {
                showHistoryPng.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                showHistoryPng.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                actionButtonList.add(showHistoryPng);
            }
            //事件单已办列表控制这个人是否有催单按钮
            if (WorkOrderInformation.incident.getCode().equals(code)) {
                //查询pub_flow_log表中IT服务台分派环节的处理人
                PubFlowLog pubFlowLog = new PubFlowLog();
                pubFlowLog.setBizId(baseBizData.get(0).get("extra1").toString());
                pubFlowLog.setLogType(code);
                pubFlowLog.setTaskName("IT服务台分派");
                List<PubFlowLog> pubFlowLogs = iPubFlowLogService.selectPubFlowLogList(pubFlowLog);
                List<String> userIds = pubFlowLogs.stream().map(PubFlowLog::getPerformerId).collect(Collectors.toList());
                if (userIds.contains(CustomerBizInterceptor.currentUserThread.get().getUserId())) {
                    Map<String, Object> reminder = new HashMap<>();
                    reminder.put("buttonName", "催单");
                    reminder.put("buttonUrlPath", "/incident/reminder");
                    actionButtonList.add(reminder);
                }
                //发起问题单 发起变更单
                Map<String, Object> creatProblem = new HashMap<>();
                creatProblem.put("buttonName", "发起问题单");
                creatProblem.put("buttonUrlPath", "/incidentCreate/{event}/{problem}/{formId}");
                actionButtonList.add(creatProblem);
                Map<String, Object> creatChange = new HashMap<>();
                creatChange.put("buttonName", "发起变更单");
                creatChange.put("buttonUrlPath", "/incidentCreate/{event}/{change}/{formId}");
                actionButtonList.add(creatChange);
                if (sysRoleService.judgeHasRole(EventFieldConstants.EVENT_ADMIN_ROLE_ID, CustomerBizInterceptor.currentUserThread.get().getUserId())) {
                    Map<String, Object> saveButton = new HashMap<>();
                    saveButton.put("buttonName", CustomerFlowConstants.addButtonName);
                    saveButton.put("buttonUrlPath", CustomerFlowConstants.addButtonPath);
                    actionButtonList.add(saveButton);
                }
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
        //问题单，问题任务单管理员取消
        if (code.equals(WorkOrderInformation.problem.getCode()) || code.equals(WorkOrderInformation.problem_task.getCode())) {
            if (checkAdmin(loginUser)) {
                //问题单管理员 取消权限
                Boolean isCancle = false;
                for (Map<String, Object> mp : actionButtonList) {
                    String name = mp.get("buttonName").toString();
                    if (CustomerFlowConstants.cancelButtonName.equals(name)) {
                        isCancle = true;
                    }
                }
                for (Map<String, Object> map : approveButtonList) {
                    String name = map.get("name").toString();
                    if (CustomerFlowConstants.cancelButtonName.equals(name)) {
                        isCancle = true;
                    }
                }
                if (!isCancle) {
                    buildCancelButton(actionButtonList, code);
                }
                // 是问题单且当前状态是已解决总经理室审批中
                if (code.equals(WorkOrderInformation.problem.getCode()) && status.equals(GENERAL_MANAGER_AUDIT.getInfo())) {
                    this.buildDeleteManagersButton(actionButtonList, processId);
                }
            }
            if (code.equals(WorkOrderInformation.problem.getCode())) {
                Map<String, Object> relationMap = new HashMap<>();
                relationMap.put("buttonName", "关联关系");
                relationMap.put("buttonUrlPath", "/relationList");
                actionButtonList.add(relationMap);
            }
            // 添加问题任务发起人取消问题任务的逻辑
            if (code.equals(WorkOrderInformation.problem_task.getCode())) {
                dynamicTableName(code);
                List<Map<String, Object>> problemTaskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(ORIGINATOR_ID, STATUS).eq(ID, id));
                // 关闭和取消的任务不能再取消
                if (!(CollectionUtils.isEmpty(problemTaskList)
                        || Arrays.asList(ProblemTaskStatus.CANCEL, ProblemTaskStatus.CLOSE).contains(problemTaskList.get(0).get(STATUS).toString()))) {
                    // 判断当前登录人是否为问题任务发起人,如果是且登录人不是管理员则添加取消按钮
                    if (StringUtils.equals(loginUser, problemTaskList.get(0).get(ORIGINATOR_ID).toString())
                            && !checkAdmin(loginUser)) {
                        buildCancelButton(actionButtonList, code);
                    }
                }
                // 如果当前登录人没有待办,且不是管理员 则是问题任务的发起人 此时页面按钮只有保存和取消 修改的字段只有问题任务协查人
                List<Task> list = taskService.createTaskQuery().processInstanceId(processId).processDefinitionKey(code).taskCandidateGroupIn(getGroupsList(CustomerBizInterceptor.currentUserThread.get().getUserId())).list();
                List<Task> list1 = taskService.createTaskQuery().processInstanceId(processId).processDefinitionKey(code).taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId()).list();
                List<Task> allTaskList = new ArrayList<>();
                allTaskList.addAll(list);
                allTaskList.addAll(list1);
                if (CollectionUtils.isEmpty(allTaskList) && StringUtils.equals(loginUser, problemTaskList.get(0).get(ORIGINATOR_ID).toString())
                        && !checkAdmin(loginUser)) {
                    List<Map<String, Object>> aButtonList = new ArrayList<>();
                    buildCancelButton(aButtonList, code);
                    //构造没有走到流程中信息
                    Map<String, Object> saveButton = new HashMap<>();
                    saveButton.put("buttonName", CustomerFlowConstants.addButtonName);
                    saveButton.put("buttonUrlPath", CustomerFlowConstants.addButtonPath);
                    saveButton.put("onlySaveFlag", "1");
                    aButtonList.add(saveButton);
                    result.put("actionButtonList", null);
                    result.put("approveButtonList", null);
                    result.put("actionButtonList", aButtonList);
                }
            }
        }
        //事件单撤销逻辑
        if (code.equals(WorkOrderInformation.incident.getCode()) && loginUser.equals(CustomerFlowConstants.ADMIN_USER_ID)) {
            buildInstCancelButton(actionButtonList);
        }
        // 调查中,根因已明制定解决方案,解决中三个节点任务页签追加相关操作
        Map<String, Object> taskFlagMap = new HashMap<>();
        if (code.equals(WorkOrderInformation.problem.getCode())
                && Arrays.asList("sid-71E52AA3-1A7F-4BBB-85C9-274F1A578C20", "sid-7BC887AD-3D4C-460B-A7C7-497FE787E9F7", "sid-F946EF76-B2F7-4D9E-A0DA-B552B8C9EAA0").contains(currentTaskNode.getTaskDefinitionKey())) {
            //获取问题单编号
            dynamicTableName(code);
            List<Map<String, Object>> bizNos = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq(INSTANCE_ID, processId));
            taskFlagMap.put("taskFlag", true);
            result.put("problem", taskFlagMap);
            result.put("bizNo", bizNos.get(0).get(RedundancyFieldEnum.extra1.name).toString());
        } else {
            taskFlagMap.put("taskFlag", false);
            result.put("problem", taskFlagMap);
        }
        // 事件单抢单特殊标识type=0
        if(code.equals(WorkOrderInformation.incident.getCode()) && "0".equals(type)) {
            // 抢单按钮之前先删除其他的流程相关操作按钮
            approveButtonList.clear();
            actionButtonList.clear();
            // 构造抢单按钮
            Map<String, Object> rolTask = new HashMap<>();
            rolTask.put("buttonName", "抢单");
            rolTask.put("buttonUrlPath", "/customerForm/rob/task");
            actionButtonList.add(rolTask);
            // 查看流程图
            Map<String, Object> showHistoryPng = new HashMap<>();
            showHistoryPng.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
            showHistoryPng.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
            actionButtonList.add(showHistoryPng);
        }
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(result);
    }

    // 构造取消总经理室按钮
    private void buildDeleteManagersButton(List<Map<String, Object>> actionButtonList, String processId) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processId).processDefinitionKey(WorkOrderInformation.problem.getCode()).list();
        if (CollectionUtils.isEmpty(taskList)) {
            return;
        }
        List<Map<String, Object>> approvedGenerals = Lists.newArrayList();
        for (Task task : taskList) {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(task.getAssignee());
            if (ogPerson == null) {
                throw new BusinessException(String.format("根据总经理室id:{%s}查询人员不存在!", task.getAssignee()));
            }
            Map<String, Object> mapPeople = new HashMap<>();
            mapPeople.put("value", ogPerson.getpId());
            mapPeople.put("label", ogPerson.getpName());
            approvedGenerals.add(mapPeople);
        }
        // 人员id转换成名称
        String generalPopJson = VueDataJsonUtil.analysisDataJsonForProblemGeneral(GENERAL_MANAGE_POP_UP, approvedGenerals);
        // 解析弹窗模板 替换现在总经理室有待办的人员
        Map<String, Object> buttonActionResult = new HashMap<>();
        List<Map<String, Object>> buttonActionList = new ArrayList<>();
        Map<String, Object> buttonJsonMap = new HashMap<>();
        buttonJsonMap.put("prop", "approved_opinion");
        buttonJsonMap.put("val", "");
        buttonActionList.add(buttonJsonMap);
        buttonActionResult.put("popJson", generalPopJson);
        buttonActionResult.put("buttonActionList", buttonActionList);
        Map<String, Object> cancelButton = new HashMap<>();
        cancelButton.put("buttonName", CustomerFlowConstants.deleteGenralManagersButtonName);
        cancelButton.put("buttonUrlPath", CustomerFlowConstants.deleteGeneralManagersButtonPath);
        cancelButton.put("buttonAction", buttonActionResult);
        actionButtonList.add(cancelButton);
    }

    // 构造取消按钮
    private void buildCancelButton(List<Map<String, Object>> actionButtonList, String code) {
        Map<String, Object> buttonActionResult = new HashMap<>();
        List<Map<String, Object>> buttonActionList = new ArrayList<>();
        Map<String, Object> buttonJsonMap = new HashMap<>();
        buttonJsonMap.put("prop", "approved_opinion");
        buttonJsonMap.put("val", "");
        buttonActionList.add(buttonJsonMap);
        buttonActionResult.put("popJson", code.equals(WorkOrderInformation.problem.getCode()) ? CANCEL_POP_UP : CANCEL_POP_UP_TASK);
        buttonActionResult.put("buttonActionList", buttonActionList);
        Map<String, Object> cancelButton = new HashMap<>();
        cancelButton.put("buttonName", CustomerFlowConstants.cancelButtonName);
        cancelButton.put("buttonUrlPath", CustomerFlowConstants.cancelFlowButtonPath);
        cancelButton.put("buttonAction", buttonActionResult);
        actionButtonList.add(cancelButton);
    }

    // 构造硬件保障取消按钮
    private void buildChmCancelButton(List<Map<String, Object>> actionButtonList) {
        Map<String, Object> buttonActionResult = new HashMap<>();
        List<Map<String, Object>> buttonActionList = new ArrayList<>();
        Map<String, Object> buttonJsonMap = new HashMap<>();
        buttonJsonMap.put("prop", "approved_opinion");
        buttonJsonMap.put("val", "");
        buttonActionList.add(buttonJsonMap);
        buttonActionResult.put("popJson", CHM_CALE_JSOM);
        buttonActionResult.put("buttonActionList", buttonActionList);
        Map<String, Object> cancelButton = new HashMap<>();
        cancelButton.put("buttonName", CustomerFlowConstants.cancelButtonName);
        cancelButton.put("buttonUrlPath", CustomerFlowConstants.cancelFlowButtonPath);
        cancelButton.put("buttonAction", buttonActionResult);
        actionButtonList.add(cancelButton);
    }

    // 构造硬件保障取消按钮
    private void buildInstCancelButton(List<Map<String, Object>> actionButtonList) {
        Map<String, Object> buttonActionResult = new HashMap<>();
        List<Map<String, Object>> buttonActionList = new ArrayList<>();
        Map<String, Object> buttonJsonMap = new HashMap<>();
        buttonJsonMap.put("prop", "approved_opinion");
        buttonJsonMap.put("val", "");
        buttonActionList.add(buttonJsonMap);
        buttonActionResult.put("popJson", CHM_CALE_JSOM);
        buttonActionResult.put("buttonActionList", buttonActionList);
        Map<String, Object> cancelButton = new HashMap<>();
        cancelButton.put("buttonName", "撤销");
        cancelButton.put("buttonUrlPath", CustomerFlowConstants.cancelFlowButtonPath);
        cancelButton.put("buttonAction", buttonActionResult);
        actionButtonList.add(cancelButton);
    }

    /**
     * 构造弹窗表单信息集合
     *
     * @param code                流程定义key
     * @param processId           流程实例ID
     * @param formDataJson        表单json集合
     * @param formKeys            流程走到当前任务节点所有的表单key集合
     * @param result              返回集合
     * @param currentTaskNode     当前任务节点activity对象
     * @param currentTaskFormInfo 当前任务表单信息
     */
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
                formStepStatusMap.put("currenFormStep", formStepStatus.getStepName());

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
                if (checkAdmin(loginUser) && (code.equals(WorkOrderInformation.problem.getCode())
                        || code.equals(WorkOrderInformation.problem_task.getCode()))) {
                    //问题单管理员编辑
                    if (code.equals(WorkOrderInformation.problem.getCode())) {
                        List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(STATUS, FOR_ERROR).eq(INSTANCE_ID, processId));
                        if (CollectionUtils.isEmpty(problemList)) {
                            throw new BusinessException("业务表数据不存在!");
                        }
                        if (!ObjectUtil.equal(problemList.get(0).get(STATUS).toString(), WAIT_SUBMIT.getInfo())) {
                            fieldActivityNodes.forEach(fieldActivityNode -> {
                                fieldActivityNode.setFieldStatus("1");
                            });
                        }
                        // 如果为否，设置相关字段隐藏
                        if (ZERO.equals(problemList.get(0).get(FOR_ERROR).toString()) && ObjectUtil.equal(problemList.get(0).get(STATUS).toString(), WAIT_SUBMIT.getInfo())) {
                            fieldActivityNodes.forEach(fieldActivityNode -> {
                                if (Arrays.asList("auditor_id", "cause_clz1", "cause_clz2", "solver_id",
                                        "cause_summary", "solver_dep_id").contains(fieldActivityNode.getFieldName())) {
                                    fieldActivityNode.setFieldStatus("2");
                                }
                            });
                        }
                    } else {
                        fieldActivityNodes.forEach(fieldActivityNode -> {
                            if (Arrays.asList("ori_dep_id", "originator_id", "current_handler_id").contains(fieldActivityNode.getFieldName())) {
                                fieldActivityNode.setFieldStatus("0");
                            } else {
                                fieldActivityNode.setFieldStatus("1");
                            }
                        });
                    }
                } else if (!checkAdmin(loginUser) && code.equals(WorkOrderInformation.problem.getCode())) {
                    List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(STATUS, FOR_ERROR).eq(INSTANCE_ID, processId));
                    if (CollectionUtils.isEmpty(problemList)) {
                        throw new BusinessException("业务表数据不存在!");
                    }
                    if (ObjectUtil.equal(problemList.get(0).get(STATUS).toString(), WAIT_SUBMIT.getInfo())) {
                        fieldActivityNodes.forEach(fieldActivityNode -> {
                            if ("originator_id".equals(fieldActivityNode.getFieldName())) {
                                fieldActivityNode.setFieldStatus("0");
                            }
                            if (ZERO.equals(problemList.get(0).get(FOR_ERROR).toString())) {
                                if (Arrays.asList("auditor_id", "cause_clz1", "cause_clz2", "solver_id",
                                        "cause_summary", "solver_dep_id").contains(fieldActivityNode.getFieldName())) {
                                    fieldActivityNode.setFieldStatus("2");
                                }
                            }
                        });
                    }
                }
                //事件单管理员编辑功能
                if (code.equals(WorkOrderInformation.incident.getCode())) {
                    if (sysRoleService.judgeHasRole(EventFieldConstants.EVENT_ADMIN_ROLE_ID, CustomerBizInterceptor.currentUserThread.get().getUserId())) {
                        fieldActivityNodes.forEach(fieldActivityNode -> {
                            fieldActivityNode.setFieldStatus("1");
                        });
                    }
                    // 事件单翻译特殊字段
                    // 翻译页面上的特殊字段，比如二线处理人员、二线处理部门
                    /*List<Map<String, Object>> secondDealMapList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(EventFieldConstants.SECOND_DEAL_PERSON, EventFieldConstants.SECOND_DEAL_ORG).eq("instance_id", processId));
                    if(!CollectionUtils.isEmpty(secondDealMapList)) {
                        IDCodeConvertChineseUtil.convertIdToName(code, secondDealMapList);
                        resultMap.put("formJson", VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), secondDealMapList.get(0)));
                    }*/
                }
                // 问题单状态是待提交时可进行编辑,将字段信息设置为1
//                if (ObjectUtil.equal(code, WorkOrderInformation.problem.getCode())) {
//                    // 根据业务id查询当前数据的状态
//                    List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(STATUS, FOR_ERROR).eq(INSTANCE_ID, processId));
//                    if (CollectionUtils.isEmpty(problemList)) {
//                        throw new BusinessException("业务表数据不存在!");
//                    }

//                    if (ObjectUtil.equal(problemList.get(0).get(STATUS).toString(), WAIT_SUBMIT.getInfo())) {
//                        if (checkAdmin(loginUser)) {
//                            if (ForError.ONE.equals(problemList.get(0).get(FOR_ERROR).toString())) {
//                                fieldActivityNodes.forEach(fieldActivityNode -> {
//                                    if (!Arrays.asList("manager_id", "current_deal_id", "solution_summary", "resolution_completion").contains(fieldActivityNode.getFieldName())) {
//                                        if (Arrays.asList("plan_complete_time", "system_id", "problem_category","problem_subclz","problem_entry","problem_subentry1","problem_subentry2", "cause_clz2")
//                                                .contains(fieldActivityNode.getFieldName())) {
//                                            fieldActivityNode.setFieldStatus("1");
//                                        } else if (Arrays.asList("problem_created_time",
//                                                "submit_time", "problem_update_time", "submit_solution_time", "plan_complete_time_modify_num", "problem_reopen_num", "solver_clear_time",
//                                                "org_id", "solve_time", "solver_last_updated", "observe_time", "close_time", "cancel_note", "cancel_reason", "close_type", "stage","solution_modify_num", "ori_dep_manager_id").contains(fieldActivityNode.getFieldName())) {
//                                            fieldActivityNode.setFieldStatus("0");
//                                        } else {
//                                            fieldActivityNode.setFieldStatus("3");
//                                        }
//                                    } else {
//                                        fieldActivityNode.setFieldStatus("2");
//                                    }
//                                });
//                            } else {
//                                fieldActivityNodes.forEach(fieldActivityNode -> {
//                                    if (!Arrays.asList("manager_id", "current_deal_id", "auditor_id", "cause_clz1", "cause_clz2", "solver_id", "cause_summary", "solution_summary", "resolution_completion", "solver_dep_id").contains(fieldActivityNode.getFieldName())) {
//                                        if (Arrays.asList("plan_complete_time", "system_id", "problem_category","problem_subclz","problem_entry","problem_subentry1","problem_subentry2", "cause_clz2")
//                                                .contains(fieldActivityNode.getFieldName())) {
//                                            fieldActivityNode.setFieldStatus("1");
//                                        } else if (Arrays.asList("problem_created_time", "ori_dep_id",
//                                                "submit_time", "problem_update_time", "submit_solution_time", "plan_complete_time_modify_num", "problem_reopen_num", "solver_clear_time",
//                                                "org_id", "solve_time", "solver_last_updated", "observe_time", "close_time", "cancel_note", "cancel_reason", "close_type", "stage","solution_modify_num", "ori_dep_manager_id")
//                                                .contains(fieldActivityNode.getFieldName())){
//                                            fieldActivityNode.setFieldStatus("0");
//                                        }else{
//                                            fieldActivityNode.setFieldStatus("3");
//                                        }
//                                    } else {
//                                        fieldActivityNode.setFieldStatus("2");
//                                    }
//                                });
//                            }
//                        } else {
//                            if (ForError.ONE.equals(problemList.get(0).get(FOR_ERROR).toString())) {
//                                fieldActivityNodes.forEach(fieldActivityNode -> {
//                                    if (!Arrays.asList("manager_id", "current_deal_id", "solution_summary", "resolution_completion").contains(fieldActivityNode.getFieldName())) {
//                                        if (Arrays.asList("plan_complete_time", "system_id", "problem_category","problem_subclz","problem_entry","problem_subentry1","problem_subentry2", "cause_clz2")
//                                                .contains(fieldActivityNode.getFieldName())) {
//                                            fieldActivityNode.setFieldStatus("1");
//                                        } else if (Arrays.asList("problem_created_time",
//                                                "submit_time", "problem_update_time", "submit_solution_time", "plan_complete_time_modify_num", "problem_reopen_num", "solver_clear_time",
//                                                "org_id", "solve_time", "solver_last_updated", "observe_time", "close_time", "cancel_note", "cancel_reason", "close_type", "stage","solution_modify_num", "ori_dep_manager_id").contains(fieldActivityNode.getFieldName())) {
//                                            fieldActivityNode.setFieldStatus("0");
//                                        } else {
//                                            fieldActivityNode.setFieldStatus("3");
//                                        }
//                                    } else {
//                                        fieldActivityNode.setFieldStatus("2");
//                                    }
//                                    if ("originator_id".equals(fieldActivityNode.getFieldName())){
//                                        fieldActivityNode.setFieldStatus("0");
//                                    }
//                                });
//                            } else {
//                                fieldActivityNodes.forEach(fieldActivityNode -> {
//                                    if (!Arrays.asList("manager_id", "current_deal_id", "auditor_id", "cause_clz1", "cause_clz2", "solver_id", "cause_summary", "solution_summary", "resolution_completion", "solver_dep_id").contains(fieldActivityNode.getFieldName())) {
//                                        if (Arrays.asList("plan_complete_time", "system_id", "problem_category","problem_subclz","problem_entry","problem_subentry1","problem_subentry2", "cause_clz2")
//                                                .contains(fieldActivityNode.getFieldName())) {
//                                            fieldActivityNode.setFieldStatus("1");
//                                        } else if (Arrays.asList("problem_created_time", "ori_dep_id",
//                                                "submit_time", "problem_update_time", "submit_solution_time", "plan_complete_time_modify_num", "problem_reopen_num", "solver_clear_time",
//                                                "org_id", "solve_time", "solver_last_updated", "observe_time", "close_time", "cancel_note", "cancel_reason", "close_type", "stage","solution_modify_num", "ori_dep_manager_id")
//                                                .contains(fieldActivityNode.getFieldName())){
//                                            fieldActivityNode.setFieldStatus("0");
//                                        }else{
//                                            fieldActivityNode.setFieldStatus("3");
//                                        }
//                                    } else {
//                                        fieldActivityNode.setFieldStatus("2");
//                                    }
//                                    if ("originator_id".equals(fieldActivityNode.getFieldName())){
//                                        fieldActivityNode.setFieldStatus("0");
//                                    }
//                                });
//                            }
//                        }
//                    }
//                }
                // 问题任务单中前置问题任务编号 可选择
                if (code.equals(WorkOrderInformation.problem_task.getCode())) {
                    fieldActivityNodes.forEach(fieldActivityNode -> {
                        if (fieldActivityNode.getFieldName().equals("pre_task_num")) {
                            fieldActivityNode.setFieldStatus("1");
                        }
                    });
                    dynamicTableName(code);
                    List<Map<String, Object>> problemTaskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(ORIGINATOR_ID, STATUS).eq(ID, currentNodeFormInfo.getBizId()));
                    if (!CollectionUtils.isEmpty(problemTaskList)) {
                        // 判断当前登录人是否为问题任务发起人,如果是则添加取消按钮
                        if (StringUtils.equals(loginUser, problemTaskList.get(0).get(ORIGINATOR_ID).toString())) {
                            //问题任务发起人可编辑协查人
                            fieldActivityNodes.forEach(fieldActivityNode -> {
                                if (fieldActivityNode.getFieldName().equals("assistant_id") || fieldActivityNode.getFieldName().equals("assistant_dept_id")) {
                                    fieldActivityNode.setFieldStatus("1");
                                }
                            });
                        }
                    }
                }
                resultMap.put("fieldActivityStatus", fieldActivityNodes);
            } else {
                //根据表单版本ID获取表单json数据
                Map<String, Object> formJsonInfo = customerFormMapper.getFormJsonInfo(currentTableInfo.get("id"));
                resultMap.put("formId", null);
                resultMap.put("formJson", formJsonInfo.get("json"));
                resultMap.put("formCode", a);
                resultMap.put("fieldActivityStatus", null);

                // 事件单翻译特殊字段
                /*if(code.equals(WorkOrderInformation.incident.getCode())) {
                    // 翻译页面上的特殊字段，比如二线处理人员、二线处理部门
                    List<Map<String, Object>> secondDealMapList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(EventFieldConstants.SECOND_DEAL_PERSON, EventFieldConstants.SECOND_DEAL_ORG).eq("instance_id", processId));
                    if(!CollectionUtils.isEmpty(secondDealMapList)) {
                        IDCodeConvertChineseUtil.convertIdToName(code, secondDealMapList);
                        resultMap.put("formJson", VueDataJsonUtil.analysisDataJson((String) formJsonInfo.get("json"), secondDealMapList.get(0)));
                    }
                }*/
            }
            resultMap.put("goFlag", true);
            formDataJson.add(resultMap);
            MybatisPlusConfig.customerTableName.remove();
        });
        result.put("jsonData", formDataJson);
    }

    /**
     * 构造按钮信息集合
     *
     * @param code              业务表表名
     * @param processId         流程实例ID
     * @param approveButtonList 审批按钮信息集合
     * @param actionButtonList  动作按钮信息集合
     * @param result            返回
     * @param currentTaskNode   当前任务节点
     */
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
                if ("二线部门".equals(name) || "业务部门".equals(name)) {
                    name = "解决";
                }
                //如果动态配置候选人或代理人则把下一节点动态设置的EL表达式给前端  前端进行字符分割  在审批的时间会传入EL表达式中配置的key
                //目前暂时考虑退回到申请人的业务场景  查出来改单子的申请人信息 将值返回给前端  在B节点审批不通过退回给申请人时  前端就能拿到相应的数据进行参数封装传递后端
                //将流条件和动态候选人/代理人作为参数调用complete审批接口  {"key": "value"}
                if (StringUtils.isNotEmpty(assignee)) {
                    map.put("agentExpression", assignee);
                    map.put("agentUserId", applyUserId);
                    map.put("expression", null);
                    map.put("name", name);
                    // TODO 调用方法重新设置流程处理人或工作组
                    setAgentExpression(code, map, currentTaskNode, applyUserId);
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
                    // 问题单特殊逻辑
                    if (code.equals(WorkOrderInformation.problem.getCode())) {
                        // 以下指定节点前端按钮隐藏
                        if (Arrays.asList("sid-9ECBAF85-7FF2-40AD-87DD-9A90B66E050F", "sid-D460AC1E-2E85-4B93-95F6-3C587CAA0E2D", "sid-AD38715F-CC15-49B0-8481-473E1A180F76").contains(gatewayFlow.getId())) {
                            continue;
                        }
                        // 以下节点发起子任务处理
                        if (Arrays.asList("发起子任务").contains(gatewayFlow.getName())) {
                            // 获取design_form_version表中的主键ID
                            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, WorkOrderInformation.problem_task.getCode()), null);
                            //根据表单版本ID获取表单json数据
                            Map<String, Object> formJsonInfo = customerFormMapper.getFormJsonInfo(currentTableInfo.get("id"));

                            //获取当前表单所有已配置的步骤条
                            List<FormStepStatus> formStepStatusList = formStepStatusService.list(Wrappers.<FormStepStatus>query().select(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")).groupBy(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).orderByAsc(FormStepStatus.COL_SORT));
                            //获取业务表编号
                            List<Map<String, Object>> bizNos = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("instance_id", processId));
                            map.put("formStepStatusList", formStepStatusList);
                            map.put("formJsonInfo", formJsonInfo);
                            map.put("isComplete", false);
                            String codeNew = "发起子任务".equals(gatewayFlow.getName())
                                    ? WorkOrderInformation.problem_task.getCode()
                                    : WorkOrderInformation.change.getCode();
                            map.put("code", codeNew);
                            map.put("name", gatewayFlow.getName());
                            Map<String, String> fieldMap = new HashMap<>();
                            // 子任务需要带的字段
                            if ("发起子任务".equals(gatewayFlow.getName())) {
                                fieldMap.put(SOLVER_ID, ORIGINATOR_ID);
                                fieldMap.put(SOLVER_DEP_ID, ORI_DEP_ID);
                                map.put("mapField", fieldMap);
                            }
                            // 变更流程需要带的字段 todo
//                            if ("变更流程".equals(gatewayFlow.getName())) {
//                                fieldMap.put("", "");
//                                map.put("mapField", fieldMap);
//                            }
                            Map<String, String> mapFixedField = new HashMap<>();
                            mapFixedField.put(PROBLEM_NO, Optional.ofNullable(bizNos).isPresent() ? bizNos.get(0).get(RedundancyFieldEnum.extra1.name).toString() : "");
                            map.put("mapFixedField", mapFixedField);
                            approveButtonList.add(map);
                            continue;
                        }

                        // 不同按钮参数必填校验字段
                        // 取消
//                        if (Arrays.asList("sid-F85EDB8A-79A6-4132-A38A-2E5FA596FCD0", "sid-17F76FA6-4DE0-4585-9E54-5B53FFF35F15").contains(gatewayFlow.getId())) {
//                            Map<String, String> cancelMap0 = new HashMap<>();
//                            cancelMap0.put("问题取消原因", CANCEL_REASON);
//                            cancelMap0.put("问题取消说明", CANCEL_NOTE);
//                            map.put("validFields", cancelMap0);
//                        }
                        // 合规分派
//                        if (Arrays.asList("sid-C6B696E4-1A2E-4D2C-AFD3-E6B8406706A7").contains(gatewayFlow.getId())) {
//                            Map<String, String> cancelMap0 = new HashMap<>();
//                            cancelMap0.put("问题牵头部室", SOLVER_DEP_ID);
//                            cancelMap0.put("问题审核人", AUDITOR_ID);
//                            map.put("validFields", cancelMap0);
//                        }
                        // 技术分派
//                        if (Arrays.asList("sid-B39BD3F9-1481-42F3-92CA-209F34984A4A").contains(gatewayFlow.getId())) {
//                            Map<String, String> cancelMap0 = new HashMap<>();
//                            cancelMap0.put("牵头人", SOLVER_ID);
//                            cancelMap0.put("计划完成时间", PLAN_COMPLETE_TIME);
//                            map.put("validFields", cancelMap0);
//                        }
                        // 提交根因
//                        if (Arrays.asList("sid-742D58C5-AB4D-44EF-80A3-4D8483F6D6CF").contains(gatewayFlow.getId())) {
//                            Map<String, String> cancelMap0 = new HashMap<>();
////                            cancelMap0.put("根因明确时间", SOLVER_CLEAR_TIME);
//                            cancelMap0.put("根因分类一", CAUSE_CLZ1);
////                            cancelMap0.put("根因分类二", CAUSE_CLZ2);
//                            cancelMap0.put("根因分析汇总", CAUSE_SUMMARY);
//                            cancelMap0.put("临时解决方案", TEMP_SOLUTIONS);
//                            map.put("validFields", cancelMap0);
//                        }
                        // 提交解决方案
//                        if (Arrays.asList("sid-5F5AC698-140F-415D-AD01-B5207AF681C8").contains(gatewayFlow.getId())) {
//                            Map<String, String> cancelMap0 = new HashMap<>();
//                            cancelMap0.put("解决方案汇总", SOLUTION_SUMMARY);
//                            cancelMap0.put("计划完成时间", PLAN_COMPLETE_TIME);
//                            cancelMap0.put("临时解决方案", TEMP_SOLUTIONS);
//                            map.put("validFields", cancelMap0);
//                        }
                        // 已解决
//                        if (Arrays.asList("sid-3DAB5845-E027-4E25-B751-F4FB7FC6D9DF").contains(gatewayFlow.getId())) {
//                            Map<String, String> cancelMap0 = new HashMap<>();
//                            cancelMap0.put("解决完成情况", RESOLUTION_COMPLETION);
//                            map.put("validFields", cancelMap0);
//                        }
                        // 观察
//                        if (Arrays.asList("sid-0A8494F7-4F65-4E82-8D32-E3B800D1BB6D").contains(gatewayFlow.getId())) {
//                            Map<String, String> cancelMap0 = new HashMap<>();
//                            cancelMap0.put("观察期限", OBSERVE_TIME);
//                            map.put("validFields", cancelMap0);
//                        }
                        // 观察结束
//                        if (Arrays.asList("sid-90DFC745-8E8B-4E9C-BB2D-B664E6EB79CD").contains(gatewayFlow.getId())) {
//                            Map<String, String> cancelMap0 = new HashMap<>();
//                            cancelMap0.put("观察说明", OBSERVE_NOTE);
//                            map.put("validFields", cancelMap0);
//                        }
                        // 关闭
//                        if (Arrays.asList("sid-D93A7D16-FBAD-4DFF-B942-622E111B3CBC").contains(gatewayFlow.getId())) {
//                            Map<String, String> cancelMap0 = new HashMap<>();
//                            cancelMap0.put("类别", PROBLEM_CATEGORY);
//                            cancelMap0.put("子类", PROBLEM_SUBCLZ);
//                            cancelMap0.put("条目", PROBLEM_ENTRY);
//                            cancelMap0.put("关闭分类", CLOSE_TYPE);
//                            map.put("validFields", cancelMap0);
//                        }
                    }
                    if (Arrays.asList("sid-B39BD3F9-1481-42F3-92CA-209F34984A4A").contains(gatewayFlow.getId())) {
                        //
                        List<Map<String, Object>> solverDepId = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(SOLVER_DEP_ID, SOLVER_ID).eq("instance_id", processId));
                        if (CollectionUtils.isEmpty(solverDepId)) {
                            throw new BusinessException("查询数据为空!");
                        }
                        map.put(SOLVER_DEP_ID, solverDepId.get(0).get(SOLVER_DEP_ID));
                        map.put(SOLVER_ID, solverDepId.get(0).get(SOLVER_ID));
                    }
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
                                map.put("isGroup", true);
                            }
                        }

                        if (StringUtils.isNotEmpty(assignee)) {
                            map.put("agentExpression", assignee);
                            map.put("agentUserId", applyUserId);
                            map.put("expression", gatewayFlow.getConditionExpression());//流条件
                            map.put("name", gatewayFlow.getName());
                            // TODO 调用方法重新设置流程处理人或工作组
                            setAgentExpression(code, map, currentTaskNode, applyUserId);
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
                    // TODO 特殊处理逻辑如果是事件单 处理人接单的"退回服务台"按钮和处理环节的"关闭"按钮隐藏
                    if (code.equals(WorkOrderInformation.incident.getCode())) {
                        if ("三方退回".equals(map.get("name")) || "退回服务台".equals(map.get("name"))
                                || "关闭".equals(map.get("name"))) {
                            continue;
                        }
                        if ("转派二线".equals(map.get("name")) || "转派业务".equals(map.get("name"))) {
                            OgPerson person = ogPersonService.selectOgPersonById(CustomerBizInterceptor.currentUserThread.get().getUserId());
                            boolean orgFlag = deptService.judgeCurrentOrgIfAll(person.getOrgId());
                            if (!orgFlag) {
                                continue;
                            }
                        }
                        if ("退回补全信息".equals(map.get("name")) || ("处理人接单".equals(currentTaskNode.getName())) && "退回".equals(map.get("name"))) {
                            // 如果是监控事件，退回按钮隐藏
                            boolean flag = eventForeignService.judgeMonitorCreateIncidentByInstanceId(processId);
                            if(flag) {
                                continue;
                            }
                        }
                    }
                    if (code.equals(WorkOrderInformation.chm_task.getCode()) && "发起事件单".equals(map.get("name"))) {
                        continue;
                    }
//                    if(code.equals(WorkOrderInformation.chm_task.getCode())){
//                        if("服务台转派".equals(map.get("name"))||"转派".equals(map.get("name"))){
//                            Map<String, Object> buttonActionResult = new HashMap<>();
//                            List<Map<String, Object>> buttonActionList = new ArrayList<>();
//                            Map<String, Object> buttonJsonMap = new HashMap<>();
//                            buttonJsonMap.put("prop", "approved_opinion");
//                            buttonJsonMap.put("val", "");
//                            buttonActionList.add(buttonJsonMap);
//                            buttonActionResult.put("popJson", CHM_GROUP_PERSON);
//                            buttonActionResult.put("buttonActionList", buttonActionList);
//                            Map<String, Object> cancelButton = new HashMap<>();
//                            cancelButton.put("buttonName", CustomerFlowConstants.cancelButtonName);
//                            cancelButton.put("buttonUrlPath", CustomerFlowConstants.cancelFlowButtonPath);
//                            cancelButton.put("buttonAction", buttonActionResult);
//                            actionButtonList.add(cancelButton);
//                        }
//
//                    }
                    approveButtonList.add(map);
                }
                // 问题解决中节点添加变更计划完成时间按钮逻辑
                if ("sid-3538356E-3A66-4FB4-A903-B4A5A04EAC79".equals(((Gateway) targetFlowElement).getId())) {


                    Map<String, Object> buttonActionResult = new HashMap<>();
                    List<Map<String, Object>> buttonActionList = new ArrayList<>();
                    Map<String, Object> buttonJsonMap = new HashMap<>();
                    buttonJsonMap.put("prop", "approved_opinion");
                    buttonJsonMap.put("val", "");
                    buttonActionList.add(buttonJsonMap);
                    buttonActionResult.put("popJson", PLAN_COMPLETE_TIME_JSON);
                    buttonActionResult.put("buttonActionList", buttonActionList);
                    Map<String, Object> planCompleteTimeButton = new HashMap<>();
                    planCompleteTimeButton.put("name", "变更计划完成时间");
                    planCompleteTimeButton.put("buttonAction", buttonActionResult);
                    planCompleteTimeButton.put("isInsertOrUpdate", true);
                    approveButtonList.add(planCompleteTimeButton);
                }
            }
            if (targetFlowElement instanceof EndEvent) {
                Map<String, Object> map = new HashMap<>();
                map.put("agentExpression", null);
                map.put("agentUserId", null);
                map.put("expression", null);
                if(code.equals(WorkOrderInformation.chm_task.getCode())&&"应用支持".equals(currentTaskNode.getName())){
                    map.put("name","关闭");
                }else {
                    map.put("name", currentTaskNode.getName());
                }
                approveButtonList.add(map);
            }
        }
        String taskName = currentTaskNode.getName();
        if (code.equals(WorkOrderInformation.incident.getCode())) {
            //sid-BD14D6B4-6798-4A83-BF23-CA30913913A6  一线
            //sid-B4F65A25-1C1D-403B-A049-EA2E83CEDBDB  二线
            if (taskName.equals("处理") || taskName.equals("确认关闭") || "二线部门".equals(taskName) || "业务部门".equals(taskName)) {
                //发起问题单 发起变更单
                Map<String, Object> creatProblem = new HashMap<>();
                creatProblem.put("buttonName", "发起问题单");
                creatProblem.put("buttonUrlPath", "/incidentCreate/{event}/{problem}/{formId}");
                actionButtonList.add(creatProblem);
                Map<String, Object> creatChange = new HashMap<>();
                creatChange.put("buttonName", "发起变更单");
                creatChange.put("buttonUrlPath", "/incidentCreate/{event}/{change}/{formId}");
                actionButtonList.add(creatChange);
            } else if ("IT服务台分派".equals(taskName)) {
                // IT服务台分派
                Map<String, Object> reminder = new HashMap<>();
                reminder.put("buttonName", "催单");
                reminder.put("buttonUrlPath", "/incident/reminder");
                actionButtonList.add(reminder);
            }
            if ("处理".equals(taskName)) {
                // TODO 判断当前登录处理人具有事件挂起的角色
                if (sysRoleService.judgeHasRole(EventFieldConstants.EVENT_SUSPEND_ROLE_ID, CustomerBizInterceptor.currentUserThread.get().getUserId())) {
                    // 处理
                    Map<String, Object> suspend = new HashMap<>();
                    suspend.put("buttonName", "挂起");
                    suspend.put("buttonUrlPath", "/incident/suspend");
                    actionButtonList.add(suspend);
                }
            }
        }
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            if (taskName.equals("调查原因") || taskName.equals("根因已明,制定解决方案") || taskName.equals("问题解决中")) {
                Map<String, Object> creatProblem = new HashMap<>();
                creatProblem.put("buttonName", "发起变更单");
                creatProblem.put("buttonUrlPath", "/incidentCreate/{problem}/{change}/{formId}");
                actionButtonList.add(creatProblem);
            }
        }
        // 问题任务添加发起变更单按钮
        if (code.equals(WorkOrderInformation.problem_task.getCode())) {
            if (taskName.equals("处理")) {
                Map<String, Object> creatProblem = new HashMap<>();
                creatProblem.put("buttonName", "发起变更单");
                creatProblem.put("buttonUrlPath", "/incidentCreate/{problem_task}/{change}/{formId}");
                actionButtonList.add(creatProblem);
            }
        }
        Map<String, Object> showHistoryPng = new HashMap<>();
        showHistoryPng.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
        showHistoryPng.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);

        Map<String, Object> saveButton = new HashMap<>();
        saveButton.put("buttonName", CustomerFlowConstants.addButtonName);
        saveButton.put("buttonUrlPath", CustomerFlowConstants.addButtonPath);
        if (code.equals(WorkOrderInformation.problem_task.getCode()) && checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())) {
            saveButton.put("onlySaveFlag", "1");
        }
        actionButtonList.add(saveButton);
        actionButtonList.add(showHistoryPng);
        // 硬件报障发起事件单
        if (code.equals(WorkOrderInformation.chm_task.getCode())) {
            if (taskName.equals("分派")) {
                Map<String, Object> creatProblem = new HashMap<>();
                creatProblem.put("buttonName", "发起事件单");
                creatProblem.put("buttonUrlPath", "/incidentCreate/{chm}/{event}/{formId}");
                actionButtonList.add(creatProblem);
            }else {
                actionButtonList.remove(saveButton);
            }
        }
        result.put("approveButtonList", approveButtonList);
        result.put("actionButtonList", actionButtonList);
        result.put("formCodes", CustomerFlowConstants.DICTIONARY);

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
            if (ObjectUtil.isNotEmpty(map.get(SOLVER_DEP_ID))) {
                Map<String, Object> popSourceMap = new HashMap<>();
                popSourceMap.put("prop", SOLVER_ID);
                Map<String, Object> source = new HashMap<>();
                source.put("url", "/system/commonTree/listOgPerson");
                List<Map<String, Object>> infoList = new ArrayList<>();
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("params", "Id");
                infoMap.put("source", "Write");
                infoMap.put("val", map.get(SOLVER_DEP_ID));
                infoList.add(infoMap);
                source.put("info", infoList);
                popSourceMap.put("dataSource", source);
                popSourceMap.put("val", ObjectUtil.isEmpty(map.get(SOLVER_ID)) ? "" : map.get(SOLVER_ID));
                map.remove(SOLVER_DEP_ID);
                map.remove(SOLVER_ID);
                list.add(popSourceMap);
            }
        } else {
            return;
        }
        if (ObjectUtil.isNotEmpty(obj.get("fully_qualified_name"))) {
            String classFullyQualifiedName = obj.get("fully_qualified_name").toString();
            String beanName = obj.get("bean_name").toString();
            String methodName = obj.get("method_name").toString();
            String paramValue = obj.get("param_value").toString();
            try {
                Class<?> serviceInmpl = Class.forName(classFullyQualifiedName);
                Method declaredMethod = serviceInmpl.getDeclaredMethod(methodName, String.class);
                Object invoke = declaredMethod.invoke(SpringUtils.getBean(beanName), paramValue);
                Map<String, Object> buttonActionMap = new HashMap<>();
                buttonActionMap.put("prop", "approved_person");
                buttonActionMap.put("val", "");
                buttonActionMap.put("dataSource", invoke);
                list.add(buttonActionMap);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("反射调用方法失败");
            }

        }
        buttonActionResult.put("buttonActionList", list);
        map.put("buttonAction", buttonActionResult);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult complete(String code, String taskId, String instanceId, String
            statusStr, CustomerVo customerVo) {
        Task t = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(t == null) {
            throw new BusinessException("流程任务已处理或不存在，请刷新列表并检查!");
        }
        String statusOri = "";
        if (Arrays.asList(WorkOrderInformation.problem.getCode(), WorkOrderInformation.problem_task.getCode()).contains(code)) {
            dynamicTableName(code);
            List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(STATUS).eq(INSTANCE_ID, instanceId));
            if (CollectionUtils.isEmpty(problemList)) {
                throw new BusinessException("问题单表数据不存在!");
            }
            statusOri = problemList.get(0).get(STATUS).toString();
        }
        String currentHandlerId = code.equals(WorkOrderInformation.problem.getCode()) ? customerVo.getCustomerFormContent().getFields().get(CURRENT_DEAL_ID).toString() : code.equals(WorkOrderInformation.problem_task.getCode()) ? customerVo.getCustomerFormContent().getFields().get("current_handler_id").toString() : "";
        Map<String, Object> resultMap = new HashMap<>();
        // 发起人和牵头人是同一人或者发起部室经理和审核人是同一人时流分支设置
        handleFlowNode(code, instanceId, customerVo, statusStr, taskId);
        boolean showFlag = handleIncident(code, instanceId, t, customerVo, statusStr);
        this.insertOrUpdate(code, customerVo.getCustomerFormContent());
        //获取当前任务相关信息  插入pubFlowLog表
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).taskId(taskId).singleResult();
        dynamicTableName(code);
        //获取原有列表数据
        List<Map<String, Object>> records = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq(INSTANCE_ID, instanceId));
        Integer bizId = customerFormMapper.selectOneByProcessId(instanceId);
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        //单子类型
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
        taskService.addComment(taskId, instanceId, statusStr);
        // 被委派人处理完成任务
        // p.s. 被委托的流程需要先 resolved 这个任务再提交。
        // 所以在 complete 之前需要先 resolved
        // resolveTask() 要在 claim() 之前，不然 act_hi_taskinst 表的 assignee 字段会为 null
        // resolveTask() 要在 claim() 之前，不然 act_hi_taskinst 表的 assignee 字段会为 null
        taskService.resolveTask(taskId, customerVo.getVariables());
        // 只有签收任务，act_hi_taskinst 表的 assignee 字段才不为 null
        if (Arrays.asList(WorkOrderInformation.problem.getCode()).contains(code) && checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())) {
            // 管理员替别人走流程时导致的已办数据查询错误,所以设置为当前处理人而非当前登录人
            if (statusOri.equals(ADMIN_CONFIRMING.getInfo()) || statusOri.equals(COMPLIANCE_AUDIT.getInfo())) {
                taskService.claim(taskId, CustomerBizInterceptor.currentUserThread.get().getUserId());
            } else {
                taskService.claim(taskId, currentHandlerId);
            }
        } else if (Arrays.asList(WorkOrderInformation.problem_task.getCode()).contains(code) && checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())) {
            taskService.claim(taskId, currentHandlerId);
        } else {
            taskService.claim(taskId, CustomerBizInterceptor.currentUserThread.get().getUserId());
        }

        // 设置总经理室审核人员
        if (code.equals(WorkOrderInformation.problem.getCode()) && customerVo.getVariables().keySet().contains("generalManagerId")) {
            Map<String, Object> variables = customerVo.getVariables();
            variables.put(GENERAL_MANAGER_IDS, variables.get("generalManagerId"));
            variables.remove("generalManagerId");
            taskService.complete(taskId, variables);
        } else if (code.equals(WorkOrderInformation.chm_task.getCode())) {
            dynamicTableName(code);
            Map<String, Object> variables = customerVo.getVariables();
            Map<String, Object> fileds = customerVo.getCustomerFormContent().getFields();
            if (variables.containsKey("loginUser")) {
                variables.put("loginUser", CustomerBizInterceptor.currentUserThread.get().getUserId());
                designBizChmMapper.update(null, Wrappers.<DesignBizChm>update().eq("id", bizId).set("deal_notes", statusStr).set("current_handler_id",CustomerBizInterceptor.currentUserThread.get().getPname()));
            }
            if (variables.containsKey("dealGroup")) {
                variables.put("dealGroup", CustomerFlowConstants.CHM_IT_GROUP);
                designBizChmMapper.update(null, Wrappers.<DesignBizChm>update().eq("id", bizId).set("deal_notes", statusStr).set("current_handler_id","IT服务台"));
            }
            //assigned_person
            if (variables.containsKey("dealUser")) {
                variables.put("dealUser", fileds.get("assigned_person"));
                String userId=fileds.get("assigned_person").toString();
                OgPerson op=ogPersonService.selectOgPersonById(userId);
                designBizChmMapper.update(null, Wrappers.<DesignBizChm>update().eq("id", bizId).set("deal_notes", statusStr).set("current_handler_id",op.getpName()));
            }
            taskService.complete(taskId, customerVo.getVariables());

            DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                    .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
            Map<String, Object> map = new HashMap<>();
            map.put("deal_notes", statusStr);
            String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
            designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
        }
        else {
            taskService.complete(taskId, customerVo.getVariables());
        }

        //判断当前流程实例的任务数是否为1 如果为1则说明只剩一个任务 走完任务进入到下一节点
        //完成当前节点的审批后获取 此流程实例的任务
        String formStatus = "";
        List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).list();
        if (!CollectionUtils.isEmpty(list) && Optional.ofNullable(list.get(0)).isPresent()) {
            FormStatusActivityNode formStatusActivityNode = formStatusActivityNodeService.getOne(Wrappers.<FormStatusActivityNode>query().eq(FormStatusActivityNode.COL_ACTIVITY_NODE_ID, list.get(0).getTaskDefinitionKey()).eq(FormStatusActivityNode.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
            //A节点审批完之后进入B节点   B节点的状态
            formStatus = Optional.ofNullable(formStatusActivityNode).map(FormStatusActivityNode::getBizStatusName).orElse("");
            //获取单子原来的状态
            String oldStatus = String.valueOf(records.get(0).get("status"));
            // 事件单单独保存操作记录详情
            if (!code.equals(WorkOrderInformation.incident.getCode()) && !code.equals(WorkOrderInformation.change.getCode()) && !code.equals(WorkOrderInformation.changeTask.getCode())) {
                //构造操作记录详情
                OperationDetails details = OperationDetails.builder().operationType(formName).bizNo(String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)))
                        .oldValue(oldStatus).newValue(formStatus)
                        .description(CustomerBizInterceptor.currentUserThread.get().getPname() + "将状态从：" + oldStatus + "变更为：" + formStatus)
                        .build();
                //保存操作记录详情
                operationDetailsService.save(details);
            }
            if (code.equals(WorkOrderInformation.problem.getCode())) {
                // 提交总经理要记录到操作详情
                if (formStatus.equals(GENERAL_MANAGER_AUDIT.getInfo()) && customerVo.getVariables().keySet().contains("generalManagerIds")) {
                    List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).processDefinitionKey(code).list();
                    if (!CollectionUtils.isEmpty(taskList)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Task task : taskList) {
                            OgPerson ogPerson = ogPersonService.selectOgPersonById(task.getAssignee());
                            stringBuilder.append(ogPerson.getpName()).append(",");
                        }
                        String managerNames = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
                        //构造操作记录详情
                        OperationDetails detail = OperationDetails.builder().operationType(formName).bizNo(String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)))
                                .description(String.format("%s提交%s进行审核", CustomerBizInterceptor.currentUserThread.get().getPname(), managerNames))
                                .build();
                        //保存操作记录详情
                        operationDetailsService.save(detail);
                    }
                }
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set("status", formStatus).set("stage", stageMap.get(formStatus)));
                DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                        .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                        .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
                Map<String, Object> map = new HashMap<>();
                map.put("stage", stageMap.get(formStatus));
                String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
                designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
            } else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set("status", formStatus).set("stage", formStatus).set("current_handler_id", customerVo.getVariables().get("dealUser") == null ? customerVo.getVariables().get("createUser") : customerVo.getVariables().get("dealUser")));
                DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                        .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                        .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
                Map<String, Object> map = new HashMap<>();
                map.put("stage", formStatus);
                map.put("current_handler_id", customerVo.getVariables().get("dealUser") == null ? customerVo.getVariables().get("createUser") : customerVo.getVariables().get("dealUser"));
                String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
                designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
            } else if (code.equals(WorkOrderInformation.incident.getCode())) {
                dynamicTableName(code);
                Map<String, Object> map = new HashMap<>();
                map.put("stage", formStatus);
                DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                        .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                        .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
                if (customerVo.getVariables().containsKey("dealId") && StringUtils.isNotEmpty(customerVo.getVariables().get("dealId"))) {
                    map.put("assigned_person", customerVo.getVariables().get("dealId"));
                }
                String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
                // 事件单只有处理解决环节才能显示"事件解决"标签
                jsonData = VueDataJsonUtil.showLabel(jsonData, EventFieldConstants.EVENT_SOLVE_LABEL_ID, showFlag);
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set("status", formStatus).set("updated_time", new Date()));
                designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
            } else {
                dynamicTableName(code);
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set("status", formStatus));
            }
            // 更新关联日志表
            relationLogMapper.update(null, Wrappers.<RelationLog>update().eq("request_no", String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name()))).set("status", formStatus));
        } else {
            String status = "已关闭";
            if (code.equals(WorkOrderInformation.problem.getCode()) && statusStr.equals("取消")) {
                //获取单子原来的状态
                String oldStatus = String.valueOf(records.get(0).get(STATUS));
                //构造操作详情记录
                OperationDetails details = OperationDetails.builder().operationType(formName).bizNo(String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)))
                        .oldValue(oldStatus).newValue(CANCEL.getInfo())
                        .description(String.format("%s将状态从：" + oldStatus + "变更为：%s", CustomerBizInterceptor.currentUserThread.get().getPname(), CANCEL.getInfo()))
                        .build();
                operationDetailsService.save(details);
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set(STATUS, CANCEL.getInfo()).set(STAGE, stageMap.get(CANCEL.getInfo())).set(CLOSE_TIME, DateUtils.getTime()).set(CURRENT_DEAL_ID, null));
                DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                        .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                        .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
                Map<String, Object> map = new HashMap<>();
                map.put(STAGE, CLOSE.getInfo());
                map.put(CLOSE_TIME, DateUtils.dateTimeNow(YYYY_MM_DD));
                map.put(CURRENT_HANDLER_ID, null);
                String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
                designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
                status = CANCEL.getInfo();
            } else {
                //获取单子原来的状态
                String oldStatus = String.valueOf(records.get(0).get("status"));
                if (code.equals(WorkOrderInformation.problem.getCode())) {
                    customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set(STATUS, CLOSE.getInfo()).set(STAGE, stageMap.get(CLOSE.getInfo())).set(CLOSE_TIME, DateUtils.getDate()).set(CURRENT_HANDLER_ID, null));
                    DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                            .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                            .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
                    Map<String, Object> map = new HashMap<>();
                    map.put(STAGE, CLOSE.getInfo());
                    map.put(CLOSE_TIME, DateUtils.dateTimeNow(YYYY_MM_DD));
                    map.put(CURRENT_HANDLER_ID, null);
                    String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
                    designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
                    status = CLOSE.getInfo();
                } else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
                    customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set(STATUS, CLOSE.getInfo()).set(STAGE, stageMap.get(CLOSE.getInfo())).set(CLOSE_TIME, DateUtils.getDate()).set("current_handler_id", null));
                    DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                            .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                            .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
                    Map<String, Object> map = new HashMap<>();
                    map.put(STAGE, CLOSE.getInfo());
                    map.put(CLOSE_TIME, DateUtils.dateTimeNow(YYYY_MM_DD));
                    map.put("current_handler_id", null);
                    String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
                    designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
                    status = CLOSE.getInfo();
                } else if (!code.equals(WorkOrderInformation.change.getCode()) && !code.equals(WorkOrderInformation.changeTask.getCode())) {
                    dynamicTableName(code);
                    customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set("status", "已关闭").set("updated_time", new Date()));
                }
                if (!code.equals(WorkOrderInformation.incident.getCode()) && !code.equals(WorkOrderInformation.change.getCode()) && !code.equals(WorkOrderInformation.changeTask.getCode())) {
                    //构造操作详情记录
                    OperationDetails details = OperationDetails.builder().operationType(formName).bizNo(String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)))
                            .oldValue(oldStatus).newValue(status)
                            .description(String.format("%s将状态从：" + oldStatus + "变更为：%s", CustomerBizInterceptor.currentUserThread.get().getPname(), StringUtils.isNotEmpty(formStatus) ? formStatus : "已关闭"))
                            .build();
                    operationDetailsService.save(details);
                }
            }
            // 更新关联日志表
            relationLogMapper.update(null, Wrappers.<RelationLog>update().eq("request_no", String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name()))).set("status", status).set("end_date", DateUtils.dateTimeNow()));
        }
        //判断审批完后页面是否继续审批
        buildContinueResult(resultMap, list, instanceId, records);
        insertFlowLog(historicTaskInstance.getId(), historicTaskInstance.getProcessDefinitionId(), instanceId, code,
                String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.getName())), statusStr,
                historicTaskInstance.getName(), CustomerBizInterceptor.currentUserThread.get().getOrgId());
        // 事件单额外特殊处理逻辑
        if (code.equals(WorkOrderInformation.incident.getCode())) {
            try {
                // 事件单状态变更需要执行发送客服接口逻辑
                eventForeignService.sendMsgTokeFu(instanceId, formStatus, statusStr);
                // 事件单发送邮件
                String currentUserId = (String) customerVo.getCustomerFormContent().getFields().get("extra5");
                eventForeignService.sendEmailMessage(currentUserId, records.get(0));
            } catch (Exception e) {
                // ******** 此处调用失败不能影响正常的流程执行返回  需要特别注意 ********
                e.printStackTrace();
                log.info("事件单调用客服系统失败，失败原因：" + e.getMessage());
            }
        }
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(resultMap);
    }

    private void buildContinueResult(Map<String, Object> resultMap, List<Task> list, String instanceId, List<Map<String, Object>> records) {
        List<Task> personalTask = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                .processInstanceId(instanceId).list();
        List<String> groupsList = this.getGroupsList(CustomerBizInterceptor.currentUserThread.get().getUserId());
        List<Task> groupTask = taskService.createTaskQuery().taskCandidateGroupIn(groupsList).processInstanceId(instanceId).list();
        personalTask.addAll(groupTask);
        if (CollectionUtil.isNotEmpty(list) && CollectionUtil.isNotEmpty(personalTask)) {
            //A->B  审批完到B节点 判断B节点是否为多实例 如果为多实例查找属于当前登录人的那个多实例
            FlowElement flowElement = repositoryService.getBpmnModel(list.get(0).getProcessDefinitionId()).getFlowElement(list.get(0).getTaskDefinitionKey());
            if (flowElement instanceof UserTask) {
                UserTask currentNode = (UserTask) flowElement;
                if (currentNode.getBehavior() instanceof ParallelMultiInstanceBehavior) {
                    Task task = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                            .processInstanceId(instanceId)
                            .singleResult();
                    records.get(0).put("taskId", task.getId());
                } else {
                    records.get(0).put("taskId", list.get(0).getId());
                }
            }
            resultMap.put("records", records);
            resultMap.put("isContinuePopFlag", true);
        } else {
            resultMap.put("isContinuePopFlag", false);
        }
    }

    /**
     * 问题单特殊逻辑处理
     *
     * @param code
     * @param instanceId
     * @param customerVo
     */
    private void handleFlowNode(String code, String instanceId, CustomerVo customerVo, String
            statusStr, String taskId) {
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            CustomerFormContent customerFormContent = customerVo.getCustomerFormContent();
            dynamicTableName(code);
            List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(FOR_ERROR, RedundancyFieldEnum.extra1.name, STATUS, AUDITOR_ID, SOLVER_ID, ORIGINATOR_ID, ORI_DEP_MANAGER_ID, FOR_ERROR, PLAN_COMPLETE_TIME, SOLUTION_SUMMARY, PLAN_COMPLETE_TIME_MODIFY_NUM, SOLUTION_MODIFY_NUM).eq(INSTANCE_ID, instanceId));
            if (CollectionUtils.isEmpty(problemList)) {
                throw new BusinessException("问题单表数据不存在!");
            }
            Map<String, Object> problemMap = problemList.get(0);
            Map<String, Object> map = new HashMap<>();
            OgPerson ogPerson = ogPersonService.selectOgPersonById(CustomerBizInterceptor.currentUserThread.get().getUserId());
            // 问题单管理员组id
            String manageGroupId1 = (String) runtimeService.getVariable(instanceId, MANAGER_GROUP_ID);
            String queryManageGroupId = null;
            if (ObjectUtil.isEmpty(manageGroupId1)) {
                // 查询问题发起人所在机构 1-分行 0-总行
                OgOrg ogOrg = iSysDeptService.selectDeptById(ogPersonService.selectOgPersonById(problemMap.get(ORIGINATOR_ID).toString()).getOrgId());
                String orgId = changePersonService.selectDept(ogOrg.getOrgId());
                if (StringUtils.isBlank(orgId)) {
                    throw new BusinessException("问题发起人所在机构不正确,非总行或分行人员!");
                }
                OgGroup ogGroup = new OgGroup();
                // 設置當前登錄人所在的機構id todo  后续会根因sysid和orgid查询
                ogGroup.setOrgId(orgId);
                ogGroup.setMemo("problem_admin_not_update");// 問題管理員組
                List<OgGroup> ogGroups = iSysWorkService.selectOgGroupList(ogGroup);
                if (CollectionUtils.isEmpty(ogGroups)) {
                    throw new BusinessException("管理员组不存在!");
                }
                queryManageGroupId = ogGroups.get(0).getGroupId(); // 总行
            }
            String manageGroupId = StringUtils.isNotEmpty(manageGroupId1)
                    ? manageGroupId1
                    : queryManageGroupId;
            List<OgGroupPerson> ogGroupPeopleList = ogGroupPersonService.selectOgGroupPersonById(manageGroupId);
            if (CollectionUtils.isEmpty(ogGroupPeopleList)) {
                throw new BusinessException("管理员组没有配置人员!");
            }
            String queryUserId;
            // 合规审核设置管理员
            if (COMPLIANCE_AUDIT.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 设置下一节点审批人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    customerVo.getVariables().put(AUDITOR_ID, customerVo.getCustomerFormContent().getFields().get(AUDITOR_ID));
                    queryUserId = customerVo.getCustomerFormContent().getFields().get(AUDITOR_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                }
                // 设置退回到发起人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_ZERO)) {
                    customerVo.getVariables().put(ORIGINATOR_ID, customerFormContent.getFields().get(ORIGINATOR_ID));
                    queryUserId = customerFormContent.getFields().get(ORIGINATOR_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                }
//                customerFormContent.getFields().put(MANAGER_ID, CustomerBizInterceptor.currentUserThread.get().getUserId());
//                Map<String, Object> manMap = new HashMap<>();
//                manMap.put(ogPerson.getpName(), ogPerson.getpId());
//                manMap.put("default", ogPerson.getpId());
//                map.put(MANAGER_ID, manMap);
            }

            // 技术审核设置el表达式审批人
            if (TECHNOLOGY_AUDIT.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 设置下一节点审批人 牵头人 退回技术审核不用设置 流程图中配置了管理员组
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    customerVo.getVariables().put(SOLVER_ID, customerVo.getCustomerFormContent().getFields().get(SOLVER_ID));
                    queryUserId = customerVo.getCustomerFormContent().getFields().get(SOLVER_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                } else {
                    customerVo.getVariables().remove(MANAGER_GROUP_ID);
                    // 退回到合规审核
                    // 设置当前处理人为管理人员拼接id
                    this.setCurIdIsManagers(customerFormContent, map, ogGroupPeopleList);
                }
            }

            // 接收问题设置el表达式审批人
            if (ASSIGNED.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 设置下一节点审批人 还是牵头人本人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    customerVo.getVariables().put(SOLVER_ID, customerFormContent.getFields().get(SOLVER_ID));
                    queryUserId = customerFormContent.getFields().get(SOLVER_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                }
                // 退回到技术审核节点 审核人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_ZERO)) {
                    customerVo.getVariables().put(AUDITOR_ID, customerFormContent.getFields().get(AUDITOR_ID));
                    queryUserId = customerFormContent.getFields().get(AUDITOR_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                }
            }

            // 调查原因设置el表达式审批人
            if (UNDER_INVESTIGATION.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 设置下一节点审批人 还是牵头人本人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    customerVo.getVariables().put(SOLVER_ID, customerFormContent.getFields().get(SOLVER_ID));
                    queryUserId = customerFormContent.getFields().get(SOLVER_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);

                    // 设置根因明确时间和是否已知错误为是
                    customerFormContent.getFields().put(FOR_ERROR_FIELD, ONE);
                    customerFormContent.getFields().put(SOLVER_CLEAR_TIME, DateUtils.getDate());
                    Map<String, Object> mapDate = new HashMap<>();
                    mapDate.put(FOR_ERROR_FIELD, ONE);
                    mapDate.put(SOLVER_CLEAR_TIME, DateUtils.getDate());
                    customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                }
            }

            // 根因已明,制定解决方案设置el表达式审批人 退回到合规审核 不需要设置
            if (SOLUTION_PENDING.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 设置下一节点审批人 审核人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    customerVo.getVariables().put(AUDITOR_ID, customerFormContent.getFields().get(AUDITOR_ID));
                    queryUserId = customerFormContent.getFields().get(AUDITOR_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                    // 设置提交解决方案时间
                    customerFormContent.getFields().put(SUBMIT_SOLUTION_TIME, DateUtils.getDate());
                    Map<String, Object> mapDate = new HashMap<>();
                    mapDate.put(SUBMIT_SOLUTION_TIME, DateUtils.getDate());
                    customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                } else if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_ZERO)) {
                    if (ObjectUtil.isEmpty(manageGroupId1)) {
                        customerVo.getVariables().put(MANAGER_GROUP_ID, manageGroupId);
                    } else {
                        customerVo.getVariables().remove(MANAGER_GROUP_ID);
                    }
                    // 退回到合规审核，设置当前处理人为管理员组
                    this.setCurIdIsManagers(customerFormContent, map, ogGroupPeopleList);
                }
            }

            // 解决方案审核设置el表达式审批人
            if (SOLUTION_AUDIT.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 设置下一节点审批人 审核人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)
                        || StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_ZERO)) {
                    customerVo.getVariables().put(SOLVER_ID, customerFormContent.getFields().get(SOLVER_ID));
                    queryUserId = customerFormContent.getFields().get(SOLVER_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                }
            }

            // 问题解决中设置el表达式审批人
            if (SOLVING.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 设置下一节点审批人 审核人
                if (Arrays.asList(PROBLEM_FLOW_BRANCH_POSITIVE1, PROBLEM_FLOW_BRANCH_POSITIVE4).contains(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString())) {
                    customerVo.getVariables().put(AUDITOR_ID, customerFormContent.getFields().get(AUDITOR_ID));
                    queryUserId = customerFormContent.getFields().get(AUDITOR_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                }
            }

            // 发起人和牵头是同一人 发起部室经理和审核人是同一人设置流程分支直接到管理员
            if (problemMap.get(STATUS).toString().equals(AUDITOR_CONFIRMING.getInfo()) && customerFormContent.getFields().get(ORIGINATOR_ID).toString().equals(customerFormContent.getFields().get(SOLVER_ID).toString())
                    || problemMap.get(STATUS).toString().equals(ORIGINATOR_CONFIRMING.getInfo()) && customerFormContent.getFields().get(AUDITOR_ID).toString().equals(customerFormContent.getFields().get(ORI_DEP_MANAGER_ID).toString())) {
                customerVo.getVariables().put(PROBLEM_FLOW_BRANCH_KEY, PROBLEM_FLOW_BRANCH_POSITIVE2);
            }

            // 已解决审核人确认设置el表达式审批人 分支0(确认未解决) 和 2(发起人和牵头人为同一人):下一节点为管理员
            if (AUDITOR_CONFIRMING.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 确认已解决设置下一节点审批人 发起人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    customerVo.getVariables().put(ORIGINATOR_ID, customerFormContent.getFields().get(ORIGINATOR_ID));
                    queryUserId = customerFormContent.getFields().get(ORIGINATOR_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                } else {
                    customerVo.getVariables().remove(ORIGINATOR_ID);
                    if (ObjectUtil.isEmpty(manageGroupId1)) {
                        customerVo.getVariables().put(MANAGER_GROUP_ID, manageGroupId);
                    } else {
                        customerVo.getVariables().remove(MANAGER_GROUP_ID);
                    }
                    // 未解决到管理员
                    this.setCurIdIsManagers(customerFormContent, map, ogGroupPeopleList);
                }
            }

            // 已解决发起人确认设置el表达式审批人 分支0(确认未解决) 和 2(发起人和牵头人为同一人):下一节点为管理员不需要手动设置,在流程图中配置了管理员组
            if (ORIGINATOR_CONFIRMING.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 确认已解决设置下一节点审批人 发起人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    customerVo.getVariables().put(ORI_DEP_MANAGER_ID, customerFormContent.getFields().get(ORI_DEP_MANAGER_ID));
                    queryUserId = customerFormContent.getFields().get(ORI_DEP_MANAGER_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                } else {
                    customerVo.getVariables().remove(ORI_DEP_MANAGER_ID);
                    if (ObjectUtil.isEmpty(manageGroupId1)) {
                        customerVo.getVariables().put(MANAGER_GROUP_ID, manageGroupId);
                    } else {
                        customerVo.getVariables().remove(MANAGER_GROUP_ID);
                    }
                    // 未解决到管理员
                    this.setCurIdIsManagers(customerFormContent, map, ogGroupPeopleList);
                }
            }
            // 已解决总经理室确认中设置当前处理人
            if (Arrays.asList(GENERAL_MANAGER_AUDIT.getInfo()).contains(problemMap.get(STATUS).toString())) {
                if (ObjectUtil.isEmpty(manageGroupId1)) {
                    customerVo.getVariables().put(MANAGER_GROUP_ID, manageGroupId);
                } else {
                    customerVo.getVariables().remove(MANAGER_GROUP_ID);
                }
                // 如果是最后一位总经理审批那么当前处理人设置为下一节点管理员 如果还有其他的总经理待审批，那么设置当前处理人其他未审批的总经理中的一人
                // 当前处理人都为管理员
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(instanceId)
                        .list();
                if (taskList.size() == 1) {
                    this.setCurIdIsManagers(customerFormContent, map, ogGroupPeopleList);
                } else {
                    List<String> ids = taskList.stream().filter(task -> !StringUtils.equals(task.getId(), taskId)).map(task -> task.getAssignee()).collect(Collectors.toList());
                    List<String> nameList = ids.stream().map(id -> ogPersonService.selectOgPersonById(id).getpName()).collect(Collectors.toList());
                    String nameStr = String.join(",", nameList);
                    String idStr = String.join(",", ids);
                    Map<String, Object> curMap = new HashMap<>();
                    curMap.put(nameStr, idStr);
                    curMap.put("default", idStr);
                    map.put(CURRENT_HANDLER_ID, curMap);
                    customerFormContent.getFields().put(CURRENT_HANDLER_ID, idStr);
                    customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), map));
                }
            }
            // 观察中,已解决发起部室经理确认中都到管理员
            if (Arrays.asList(ORIGINATE_DEPART_MANAGER_CONFIRMING.getInfo(), OBSERVING.getInfo()).contains(problemMap.get(STATUS).toString())) {
                if (ObjectUtil.isEmpty(manageGroupId1)) {
                    customerVo.getVariables().put(MANAGER_GROUP_ID, manageGroupId);
                } else {
                    customerVo.getVariables().remove(MANAGER_GROUP_ID);
                }
                // 当前处理人都为管理员
                this.setCurIdIsManagers(customerFormContent, map, ogGroupPeopleList);
            }

            // 已解决管理员确认设置el表达式审批人
            if (ADMIN_CONFIRMING.getInfo().equals(problemMap.get(STATUS).toString())) {
                // 确认已解决设置下一节点审批人 发起人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE2)) {
                    customerVo.getVariables().put(ORIGINATOR_ID, customerFormContent.getFields().get(ORIGINATOR_ID));
                    queryUserId = customerFormContent.getFields().get(ORIGINATOR_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                }
                if (Arrays.asList(PROBLEM_FLOW_BRANCH_POSITIVE3, PROBLEM_FLOW_BRANCH_POSITIVE4, PROBLEM_FLOW_BRANCH_POSITIVE5).contains(
                        customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString())) {
                    int reopenNum = Integer.valueOf(customerFormContent.getFields().get(PROBLEM_REOPEN_NUM).toString());
                    Map<String, Object> mapDate = new HashMap<>();
                    mapDate.put(PROBLEM_REOPEN_NUM, reopenNum + 1);
                    customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                    customerVo.getVariables().put(PROBLEM_REOPEN_NUM, reopenNum + 1);
                    customerVo.getVariables().put(SOLVER_ID, customerFormContent.getFields().get(SOLVER_ID));
                    queryUserId = customerFormContent.getFields().get(SOLVER_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                }
                // 管理员确认退回到根因分析时要设置是否已知错误为否
                if (Arrays.asList(PROBLEM_FLOW_BRANCH_POSITIVE3).contains(
                        customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString())) {
                    customerFormContent.getFields().put(FOR_ERROR_FIELD, ZERO);
                    Map<String, Object> mapDate = new HashMap<>();
                    mapDate.put(FOR_ERROR_FIELD, ForError.ZERO);
                    customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                }
            }

//            if (GENERAL_MANAGER_AUDIT.getInfo().equals(problemMap.get(STATUS).toString())) {
//                customerVo.getVariables().put(MANAGER_GROUP_ID, manageGroupId);
//            }
            // 合规审核退回到问题发起人(草稿节点)再次提交时流条件设置
            if (problemMap.get(STATUS).toString().equals(WAIT_SUBMIT.getInfo()) && !ObjectUtil.equal(statusStr, "取消")) {
                if (ForError.ZERO.equals(customerVo.getCustomerFormContent().getFields().get(FOR_ERROR).toString())) {
                    customerVo.getVariables().put(PROBLEM_FLOW_BRANCH_KEY, PROBLEM_FLOW_BRANCH_ZERO);
                    customerVo.getVariables().put(MANAGER_GROUP_ID, manageGroupId);
                } else {
                    customerVo.getVariables().put(PROBLEM_FLOW_BRANCH_KEY, PROBLEM_FLOW_BRANCH_POSITIVE1);
                    customerVo.getVariables().put(SOLVER_ID, customerVo.getCustomerFormContent().getFields().get(SOLVER_ID).toString());
                    queryUserId = customerVo.getCustomerFormContent().getFields().get(SOLVER_ID).toString();
                    this.setCurDealPerson(customerFormContent, map, queryUserId);
                }
            }

//            // 设置当前处理人
//            if (ObjectUtil.isEmpty(queryUserId)) {
//                setCurDealPerson(customerFormContent, map, queryUserId);
//            } else {
//
//            }

            if (ADMIN_CONFIRMING.getInfo().equals(problemMap.get(STATUS).toString())) {
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    // 设置当前处理人为总经理室人员拼接id
                    List<String> ids = (List<String>) (customerVo.getVariables().get("generalManagerId"));
                    List<String> nameList = ids.stream().map(id -> iOgPersonService.selectOgPersonById(id).getpName()).collect(Collectors.toList());
                    String nameStr = String.join(",", nameList);
                    String idStr = String.join(",", ids);
                    Map<String, Object> curMap = new HashMap<>();
                    curMap.put(nameStr, idStr);
                    curMap.put("default", idStr);
                    map.put(CURRENT_HANDLER_ID, curMap);
                    customerFormContent.getFields().put(CURRENT_HANDLER_ID, idStr);
                    customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), map));
                }
            }

//            // 合规审核,修改计划完成时间
//            if (COMPLIANCE_AUDIT.getInfo().equals(problemMap.get(STATUS).toString())
//                    && PROBLEM_FLOW_BRANCH_POSITIVE1.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString())) {
//                // 计划完成时间修改次数
//                int planCompleteTimeModifyNum = 0;
//                // 计划完成时间变更次数
//                if (ObjectUtil.isEmpty(problemMap.get(PLAN_COMPLETE_TIME)) || customerVo.getCustomerFormContent().getFields().get(PLAN_COMPLETE_TIME).toString().equals(problemMap.get(PLAN_COMPLETE_TIME).toString())) {
//                    customerVo.getCustomerFormContent().getFields().put(PLAN_COMPLETE_TIME_MODIFY_NUM, planCompleteTimeModifyNum);
//                } else {
//                    planCompleteTimeModifyNum = Integer.valueOf(ObjectUtil.isEmpty(problemMap.get(PLAN_COMPLETE_TIME_MODIFY_NUM))
//                            ? "0"
//                            : problemMap.get(PLAN_COMPLETE_TIME_MODIFY_NUM).toString()) + 1;
//                    customerVo.getCustomerFormContent().getFields().put(PLAN_COMPLETE_TIME_MODIFY_NUM, planCompleteTimeModifyNum);
//                }
//                Map<String, Object> mapNum = new HashMap<>();
//                mapNum.put(PLAN_COMPLETE_TIME_MODIFY_NUM, planCompleteTimeModifyNum);
//                customerVo.getCustomerFormContent().setDataJson(VueDataJsonUtil.analysisDataJson(customerVo.getCustomerFormContent().getDataJson(), mapNum));
//            }
//
//            // 技术审核节点修改计划完成时间
//            if (TECHNOLOGY_AUDIT.getInfo().equals(problemMap.get(STATUS).toString())
//                    && PROBLEM_FLOW_BRANCH_POSITIVE1.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString())) {
//                // 计划完成时间修改次数
//                int planCompleteTimeModifyNum = Integer.valueOf(ObjectUtil.isEmpty(problemMap.get(PLAN_COMPLETE_TIME_MODIFY_NUM))
//                        ? "0"
//                        : problemMap.get(PLAN_COMPLETE_TIME_MODIFY_NUM).toString());
//                // 计划完成时间变更次数
//                if (ObjectUtil.isEmpty(problemMap.get(PLAN_COMPLETE_TIME)) || customerVo.getCustomerFormContent().getFields().get(PLAN_COMPLETE_TIME).toString().equals(problemMap.get(PLAN_COMPLETE_TIME).toString())) {
//                    customerVo.getCustomerFormContent().getFields().put(PLAN_COMPLETE_TIME_MODIFY_NUM, planCompleteTimeModifyNum);
//                } else {
//                    customerVo.getCustomerFormContent().getFields().put(PLAN_COMPLETE_TIME_MODIFY_NUM, planCompleteTimeModifyNum + 1);
//                    Map<String, Object> mapNum = new HashMap<>();
//                    mapNum.put(PLAN_COMPLETE_TIME_MODIFY_NUM, planCompleteTimeModifyNum + 1);
//                    customerVo.getCustomerFormContent().setDataJson(VueDataJsonUtil.analysisDataJson(customerVo.getCustomerFormContent().getDataJson(), mapNum));
//                }
//            }
//
//            // 提交解决方案时,统计解决方案修改次数及计划完成时间修改次数
//            if (SOLUTION_PENDING.getInfo().equals(problemMap.get(STATUS).toString())
//                    && PROBLEM_FLOW_BRANCH_POSITIVE1.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString())) {
//                Map<String, Object> mapNum = new HashMap<>();
//                // 解决方案修改次数
//                int solutionModifyNum = 0;
//                // 计划完成时间修改次数
//                int planCompleteTimeModifyNum = Integer.valueOf(ObjectUtil.isEmpty(problemMap.get(PLAN_COMPLETE_TIME_MODIFY_NUM))
//                        ? "0"
//                        : problemMap.get(PLAN_COMPLETE_TIME_MODIFY_NUM).toString());
//                // 计划完成时间变更次数
//                if (ObjectUtil.isEmpty(problemMap.get(PLAN_COMPLETE_TIME)) || customerVo.getCustomerFormContent().getFields().get(PLAN_COMPLETE_TIME).toString().equals(problemMap.get(PLAN_COMPLETE_TIME).toString())) {
//                    customerVo.getCustomerFormContent().getFields().put(PLAN_COMPLETE_TIME_MODIFY_NUM, planCompleteTimeModifyNum);
//                } else {
//                    customerVo.getCustomerFormContent().getFields().put(PLAN_COMPLETE_TIME_MODIFY_NUM, planCompleteTimeModifyNum + 1);
//                    mapNum.put(PLAN_COMPLETE_TIME_MODIFY_NUM, planCompleteTimeModifyNum + 1);
//                    customerVo.getCustomerFormContent().setDataJson(VueDataJsonUtil.analysisDataJson(customerVo.getCustomerFormContent().getDataJson(), mapNum));
//                }
//                // 解决方案变更次数
//                if (ObjectUtil.isEmpty(problemMap.get(SOLUTION_SUMMARY))) {
//                    customerVo.getCustomerFormContent().getFields().put(SOLUTION_MODIFY_NUM, solutionModifyNum);
//                } else {
//                    solutionModifyNum = Integer.valueOf(ObjectUtil.isEmpty(problemMap.get(SOLUTION_MODIFY_NUM))
//                            ? "0"
//                            : problemMap.get(SOLUTION_MODIFY_NUM).toString()) + 1;
//                    customerVo.getCustomerFormContent().getFields().put(SOLUTION_MODIFY_NUM, solutionModifyNum);
//                }
//                mapNum.put(SOLUTION_MODIFY_NUM, solutionModifyNum);
//                customerVo.getCustomerFormContent().setDataJson(VueDataJsonUtil.analysisDataJson(customerVo.getCustomerFormContent().getDataJson(), mapNum));
//            }

            // 2.接收问题,调查原因,根因已明制定解决方案,问题解决中节点设置牵头人上次更新时间 3.已解决设置解决时间
            if (Arrays.asList(ASSIGNED.getInfo(), UNDER_INVESTIGATION.getInfo(), SOLUTION_PENDING.getInfo(), SOLVING.getInfo()).contains(problemMap.get(STATUS).toString())
                    && StringUtils.equals(PROBLEM_FLOW_BRANCH_POSITIVE1, customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString())) {
                customerVo.getCustomerFormContent().getFields().put(SOLVER_LAST_UPDATED, DateUtils.dateTimeNow(YYYY_MM_DD));
                Map<String, Object> mapDate = new HashMap<>();
                mapDate.put(SOLVER_LAST_UPDATED, DateUtils.dateTimeNow(YYYY_MM_DD));
                if (Arrays.asList(SOLVING.getInfo()).contains(problemMap.get(STATUS).toString())) {
                    customerVo.getCustomerFormContent().getFields().put(SOLVE_TIME, DateUtils.dateTimeNow(YYYY_MM_DD));
                    mapDate.put(SOLVE_TIME, DateUtils.dateTimeNow(YYYY_MM_DD));
                }
                customerVo.getCustomerFormContent().setDataJson(VueDataJsonUtil.analysisDataJson(customerVo.getCustomerFormContent().getDataJson(), mapDate));
            }

            // 已解决 查询当前问题单关联的任务单和变更单
            if (SOLVING.getInfo().equals(problemMap.get(STATUS).toString())
                    && PROBLEM_FLOW_BRANCH_POSITIVE1.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString())) {
                // 根据问题单编号查询是否有关联的子任务和变更单
                dynamicTableName(WorkOrderInformation.problem_task.getCode());
                List<Map<String, Object>> problemTaskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name, STATUS, PROBLEM_NO).eq(PROBLEM_NO, problemMap.get(RedundancyFieldEnum.extra1.name).toString()));
                if (!CollectionUtils.isEmpty(problemTaskList)) {
                    List<Map<String, Object>> taskList = problemTaskList.stream()
                            .filter(taskMap -> !Arrays.asList(CLOSE.getInfo(), CANCEL.getInfo())
                                    .contains(taskMap.get(STATUS).toString()))
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(taskList)) {
                        throw new BusinessException(String.format("问题单%s关联的任务单未关闭!", problemMap.get(RedundancyFieldEnum.extra1.name).toString()));
                    }
                }
                // 查询问题单是否关联变更单及关联的变更是否已关闭
//                dynamicTableName(WorkOrderInformation.change.getCode());
//                List<Map<String, Object>> changeList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name, STATUS, PROBLEM_NO).eq(PROBLEM_NO, problemMap.get(RedundancyFieldEnum.extra1.name).toString()));
                // 查询关联变更单关系表
                List<RelationLog> relationList = relationLogMapper.selectList(new QueryWrapper<RelationLog>()
                        .eq("relation_no", problemMap.get(RedundancyFieldEnum.extra1.name).toString())
                        .eq("request_type", RelationRequestType.CHANGE.getCode()));
                if (!CollectionUtils.isEmpty(relationList)) {
                    List<String> requestNoList = relationList.stream().filter(relationLog -> !Arrays.asList(ChangeStatusEnum.cancel.getName(), ChangeStatusEnum.closed.getName()).contains(relationLog.getStatus())).map(relationLog -> {
                        return relationLog.getRequestNo();
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(requestNoList)) {
                        throw new BusinessException(String.format("该问题单%s关联的变更单%s未关闭!", problemMap.get(RedundancyFieldEnum.extra1.name).toString(), requestNoList.toString()));
                    }
                } else {
                    // 没有关联的单子,如果有说明理由则可以正常流转到下一节点,如果没有说明理由则抛异常要求关联或说明未关联理由 todo 后面需要放开
                    if (StringUtils.isEmpty(customerVo.getNotRelatedReason())) {
                        throw new BusinessException("请关联相关变更单或填写不关联变更单理由!");
                    }
                }
            }
        } else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
            CustomerFormContent customerFormContent = customerVo.getCustomerFormContent();
            dynamicTableName(code);
            List<Map<String, Object>> problemTaskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name, STATUS, "originator_id", "assistant_id").eq(INSTANCE_ID, instanceId));
            if (CollectionUtils.isEmpty(problemTaskList)) {
                throw new BusinessException("问题任务单表数据不存在!");
            }
            Map<String, Object> problemTaskMap = problemTaskList.get(0);
            String queryUserId = customerVo.getCustomerFormContent().getFields().get("assistant_id").toString();
            if (ProblemTaskStatus.ASSIGNED.getInfo().equals(problemTaskMap.get(STATUS).toString())) {
                // 设置下一节点审批人为协查人
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    customerVo.getVariables().put("dealUser", customerVo.getCustomerFormContent().getFields().get("assistant_id"));
                } else {
                    // 退回到发起人
                    customerVo.getVariables().put("createUser", customerVo.getCustomerFormContent().getFields().get("originator_id"));
                    queryUserId = customerVo.getCustomerFormContent().getFields().get("originator_id").toString();
                }
                customerFormContent.getFields().put("assistant_update_time", DateUtils.getDate());
                Map<String, Object> mapDate = new HashMap<>();
                mapDate.put("assistant_update_time", DateUtils.getDate());
                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
            }
            // 处理环节
            if (ProblemTaskStatus.SOLVING.getInfo().equals(problemTaskMap.get(STATUS).toString())) {
                // 根据问题任务编号查询关联的变更是否已关闭
                List<RelationLog> relationList = relationLogMapper.selectList(new QueryWrapper<RelationLog>()
                        .eq("relation_no", problemTaskMap.get(RedundancyFieldEnum.extra1.name).toString())
                        .eq("request_type", RelationRequestType.CHANGE.getCode()));
                if (!CollectionUtils.isEmpty(relationList)) {
                    List<String> requestNoList = relationList.stream().filter(relationLog -> !Arrays.asList(ChangeStatusEnum.cancel.getName(), ChangeStatusEnum.closed.getName()).contains(relationLog.getStatus())).map(relationLog -> {
                        return relationLog.getRequestNo();
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(requestNoList)) {
                        throw new BusinessException(String.format("问题任务单%s关联的变更单%s未关闭!", problemTaskMap.get(RedundancyFieldEnum.extra1.name).toString(), requestNoList.toString()));
                    }
                }
                customerVo.getVariables().put("createUser", customerVo.getCustomerFormContent().getFields().get("originator_id"));
                queryUserId = customerVo.getCustomerFormContent().getFields().get("originator_id").toString();
                // 设置申请关闭时间
                customerFormContent.getFields().put("apply_close_time", DateUtils.getDate());
                customerFormContent.getFields().put("assistant_update_time", DateUtils.getDate());
                Map<String, Object> mapDate = new HashMap<>();
                mapDate.put("apply_close_time", DateUtils.getDate());
                mapDate.put("assistant_update_time", DateUtils.getDate());
                customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
            }
            // 申请关闭中
            if (ProblemTaskStatus.CLOSING.getInfo().equals(problemTaskMap.get(STATUS).toString())) {
                if (StringUtils.equals(customerVo.getVariables().get(PROBLEM_FLOW_BRANCH_KEY).toString(), PROBLEM_FLOW_BRANCH_POSITIVE1)) {
                    customerVo.getVariables().put("dealUser", customerVo.getCustomerFormContent().getFields().get("assistant_id"));
                    int reopenNum = 0;
                    customerFormContent.getFields().put("reopen_num", reopenNum + 1);
                    Map<String, Object> mapDate = new HashMap<>();
                    mapDate.put("reopen_num", reopenNum + 1);
                    customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJson(customerFormContent.getDataJson(), mapDate));
                }
            }
            // 接单拒绝后待提交再次提交
            if (ProblemTaskStatus.WAIT_SUBMIT.getInfo().equals(problemTaskMap.get(STATUS).toString())) {
                customerVo.getVariables().put("dealUser", customerVo.getCustomerFormContent().getFields().get("assistant_id"));
            }
            Map<String, Object> map = new HashMap<>();
            OgPerson curHandlerPerson = ogPersonService.selectOgPersonById(queryUserId);
            customerFormContent.getFields().put("current_handler_id", queryUserId);
            Map<String, Object> curMap = new HashMap<>();
            curMap.put(curHandlerPerson.getpName(), queryUserId);
            curMap.put("default", queryUserId);
            map.put("current_handler_id", curMap);
            String js = VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), map);
            customerFormContent.setDataJson(js);
        }
    }

    private void setCurIdIsManagers(CustomerFormContent customerFormContent, Map<String, Object> map, List<OgGroupPerson> ogGroupPeopleList) {
        List<String> nameList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPname()).collect(Collectors.toList());
        List<String> idList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPid()).collect(Collectors.toList());
        String nameStr = String.join(",", nameList);
        String idStr = String.join(",", idList);
        Map<String, Object> curMap = new HashMap<>();
        curMap.put(nameStr, idStr);
        curMap.put("default", idStr);
        map.put(CURRENT_HANDLER_ID, curMap);
        customerFormContent.getFields().put(CURRENT_HANDLER_ID, idStr);
        customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), map));
    }

    private void setCurDealPerson(CustomerFormContent customerFormContent, Map<String, Object> map, String queryUserId) {
        OgPerson curHandlerPerson = ogPersonService.selectOgPersonById(queryUserId);
        customerFormContent.getFields().put(CURRENT_HANDLER_ID, queryUserId);
        Map<String, Object> curMap = new HashMap<>();
        curMap.put(curHandlerPerson.getpName(), queryUserId);
        curMap.put("default", queryUserId);
        map.put(CURRENT_HANDLER_ID, curMap);
        customerFormContent.setDataJson(VueDataJsonUtil.analysisDataJsonSelect(customerFormContent.getDataJson(), map));
    }

    /**
     * 事件单分派环节一线处理组和处理人员
     *
     * @param code
     * @param instanceId
     * @param customerVo
     */
    public synchronized boolean handleIncident(String code, String instanceId, Task task, CustomerVo customerVo, String statusStr) {
        // json中的事件解决页签影藏标识（默认显示）
        boolean eventSolveShowFlag = false;
        if (code.equals(WorkOrderInformation.incident.getCode())) {
            boolean lastFlag = false;
            if(task == null) {
                task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
            }
            CustomerFormContent customerFormContent = customerVo.getCustomerFormContent();
            dynamicTableName(code);
            List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq(INSTANCE_ID, instanceId));
            if (CollectionUtils.isEmpty(list)) {
                throw new BusinessException("事件单数据为空！");
            }
            Map<String, Object> incidentMap = list.get(0);
            String assignedGroupDB = (String) incidentMap.get(EventFieldConstants.ASSIGNED_GROUP);
            String assignedPersonDB = (String) incidentMap.get(EventFieldConstants.ASSIGNED_PERSON);

            String secondDealPersonDB = (String) incidentMap.get(EventFieldConstants.SECOND_DEAL_PERSON);

            String createdBy = (String) incidentMap.get(EventFieldConstants.CREATED_BY);
            Map<String, Object> fields = customerFormContent.getFields();

            //String assignedGroup = (String) fields.get(EventFieldConstants.ASSIGNED_GROUP);// 一线受派组
            String assignedPerson = (String) fields.get(EventFieldConstants.ASSIGNED_PERSON);// 一线受派人
            String secondDealOrg = (String) fields.get(EventFieldConstants.SECOND_DEAL_ORG);// 二线部门
            String secondDealPerson = (String) fields.get(EventFieldConstants.SECOND_DEAL_PERSON);// 二线处理人员

            Map<String, Object> variables = customerVo.getVariables();
            String reCode = (String) variables.get("reCode");
            // 事件单当前处理人
            String incidentHandlerUserId = "";
            // 当前节点名称
            String currentTaskName = task.getName();
            // 下一节点名称
            String nextTaskName = "";

            final Date nowDate = DateUtils.getNowDate();
            String backCompletionFlag = "N";
            String eventNo = incidentMap.get("extra1").toString();
            incidentSubEventService.updateIncidentSubEvent(eventNo, backCompletionFlag);
            switch (task.getName()) {
                case "处理人接单":
                    if ("-1".equals(reCode)) {
                        // TODO 三方接口退回到服务台，此处分配的人员需要找到对应的当天服务台所在组的值班人
                        String receptionId = this.selectServiceByGroupName("IT服务台");
                        variables.put("receptionReceive", receptionId);
                        incidentHandlerUserId = receptionId;
                        nextTaskName = "服务台接单";
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), EventOperationType.BACK_EVENT.getInfo(), nowDate);
                    } else if ("0".equals(reCode)) {
                        // 判断是否接口发起
                        boolean flag = eventForeignService.judgeInterfaceCreateBack(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)));
                        if (flag) {
                            // 接口发起的需要退回到服务台  服务台接单环节
                            variables.put("reCode", "-1");
                            String receptionId = this.selectServiceByGroupName("IT服务台");
                            variables.put("receptionReceive", receptionId);
                            incidentHandlerUserId = receptionId;
                            nextTaskName = "服务台接单";
                            backCompletionFlag = "-2";
                        } else {
                            // 退回发起人   发起人接单环节
                            variables.put("startUserReceive", createdBy);
                            incidentHandlerUserId = createdBy;
                            nextTaskName = "发起人接单";
                            backCompletionFlag = "-1";
                        }
                        incidentSubEventService.updateIncidentSubEvent(eventNo, backCompletionFlag);
                        // 保存操作记录详情
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.BACK_EVENT.getInfo(), statusStr, nowDate);
                    } else if ("1".equals(reCode)) {
                        // 接单
                        variables.put("dealId", assignedPersonDB);
                        incidentHandlerUserId = assignedPersonDB;
                        eventSolveShowFlag = true;
                        nextTaskName = "处理";
                        // 保存操作记录详情
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), EventOperationType.RECEIVE_EVENT.getInfo(), nowDate);
                    }
                    break;
                case "服务台接单":
                    // IT服务台分派环节
                    String receptionId = this.selectServiceByGroupName("IT服务台");
                    variables.put("receptionId", receptionId);
                    incidentHandlerUserId = receptionId;

                    // 起始环节到IT服务台，分派人信息为空，此时操作类型是创建并分派，如果是处理｜确认关闭环节退回到服务台则操作类型是退回事件单
                    if (StringUtils.isEmpty(assignedPersonDB)) {
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), EventOperationType.RECEIVE_EVENT.getInfo(), nowDate);
                    } else {
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), StringUtils.isNotEmpty(statusStr) ? statusStr : EventOperationType.RECEIVE_EVENT.getInfo(), nowDate);
                    }
                    nextTaskName = "IT服务台分派";

                    break;
                case "IT服务台分派":
                    // 服务台接单后分派到具体的人
                    variables.put("dealReceive", assignedPerson);
                    incidentHandlerUserId = assignedPerson;
                    // 保存操作记录详情
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.ASSIGN_EVENT.getInfo(), EventOperationType.ASSIGN_EVENT.getInfo(), nowDate);
                    nextTaskName = "处理人接单";
                    break;
                case "退回补全":
                    variables.put("dealReceive", assignedPerson);
                    incidentHandlerUserId = assignedPerson;
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.ASSIGN_EVENT.getInfo(), EventOperationType.ASSIGN_EVENT.getInfo(), nowDate);
                    nextTaskName = "处理人接单";
                    break;
                case "发起人接单":
                    // 退回补全发起人接单后直接分配到创建人
                    variables.put("startUserId", createdBy);
                    incidentHandlerUserId = createdBy;
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), StringUtils.isNotEmpty(statusStr) ? statusStr : EventOperationType.RECEIVE_EVENT.getInfo(), nowDate);
                    nextTaskName = "退回补全";
                    break;
                case "处理":
                    if ("0".equals(reCode)) {
                        // TODO 处理环节退回到服务台，此处分配的人员需要找到对应的当天服务台所在组的值班人
                        String receptionId1 = this.selectServiceByGroupName("IT服务台");
                        variables.put("receptionReceive", receptionId1);
                        incidentHandlerUserId = receptionId1;
                        nextTaskName = "IT服务台分派";
                        // 处理环节退回到服务台
                        backCompletionFlag = "0";
                        incidentSubEventService.updateIncidentSubEvent(eventNo, backCompletionFlag);
                    } else if ("1".equals(reCode)) {
                        // 转派二线接单
                        variables.put("secondReceive", secondDealPerson);
                        incidentHandlerUserId = secondDealPerson;
                        nextTaskName = "二线接单";
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.ASSIGN_EVENT.getInfo(), statusStr, nowDate);
                    } else if ("2".equals(reCode)) {
                        // 转派业务接单
                        variables.put("businessReceive", secondDealPerson);
                        incidentHandlerUserId = secondDealPerson;
                        nextTaskName = "业务接单";
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.ASSIGN_EVENT.getInfo(), statusStr, nowDate);
                    } else if ("-1".equals(reCode)) {
                        // 判断是否接口发起
                        boolean flag = eventForeignService.judgeInterfaceCreateBack(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)));
                        if (flag) {
                            // TODO 处理环节退回到服务台，此处分配的人员需要找到对应的当天服务台所在组的值班人
                            String receptionId1 = this.selectServiceByGroupName("IT服务台");
                            variables.put("receptionReceive", receptionId1);
                            variables.put("reCode", "0");
                            incidentHandlerUserId = receptionId1;
                            nextTaskName = "服务台接单";
                            // 处理环节退回到服务台
                            backCompletionFlag = "1";
                        } else {
                            // 退回发起人接单
                            variables.put("startUserReceive", createdBy);
                            incidentHandlerUserId = createdBy;
                            // 处理环节退回补全
                            backCompletionFlag = "2";
                            nextTaskName = "发起人接单";
                        }
                        incidentSubEventService.updateIncidentSubEvent(eventNo, backCompletionFlag);

                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.BACK_EVENT.getInfo(), statusStr, nowDate);
                    } else if ("3".equals(reCode)) {
                        // 关闭
                        eventSolveShowFlag = true;

                        String desc = StringUtils.isNotEmpty(customerVo.getDescMsg()) ? customerVo.getDescMsg() : (String) incidentMap.get(EventFieldConstants.SOLVE_PLAN);

                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.CLOSE_EVENT.getInfo(), desc, nowDate);

                        IncidentSubEvent incidentSubEvent = new IncidentSubEvent();
                        incidentSubEvent.setEventNo(eventNo);
                        incidentSubEvent.setSolveTime(DateUtils.getTime());
                        incidentSubEvent.setCloseTime(DateUtils.getTime());
                        incidentSubEventService.updateIncidentSubEventByNo(incidentSubEvent);

                        boolean flag = eventForeignService.judgeMonitorCreateIncident(eventNo);
                        if (flag) {
                            nextTaskName = "结束";
                            lastFlag = true;
                            try {
                                String solution = (String) fields.get(EventFieldConstants.SOLVE_PLAN);
                                OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
                                Map<String, Object> params = new HashMap<>();
                                params.put("itsmId", eventNo);
                                params.put("solution", solution);
                                params.put("handler", ogUser.getPname() + "@" + ogUser.getUsername());
                                params.put("closeTime", new Date().getTime());
                                Map<String, Object> result = eventForeignService.closeAlertByItsmId(params);
                                log.info("------执行通知告警关闭接口调用结果 result:{}-----", result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if ("4".equals(reCode)) {
                        eventSolveShowFlag = true;
                        String description = "";
                        // 解决按钮，更新解决时间
                        // 判断是否监控告警发起的事件单
                        boolean flag = eventForeignService.judgeMonitorCreateIncident(eventNo);
                        IncidentSubEvent incidentSubEvent = new IncidentSubEvent();
                        incidentSubEvent.setEventNo(eventNo);
                        if (flag) {
                            // 监控发起流程走向关闭，不需要发起人确认  此时需要调用监控告警的关闭通知接口
                            incidentSubEvent.setSolveTime(DateUtils.getTime());
                            incidentSubEvent.setCloseTime(DateUtils.getTime());
                            nextTaskName = "结束";
                            lastFlag = true;
                            variables.put("reCode", "3");
                            try {
                                description = customerVo.getDescMsg();
                                if(StringUtils.isEmpty(description)) {
                                    description = (String) fields.get(EventFieldConstants.SOLVE_PLAN);
                                }
                                String solution = (String) fields.get(EventFieldConstants.SOLVE_PLAN);
                                OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
                                Map<String, Object> params = new HashMap<>();
                                params.put("itsmId", eventNo);
                                params.put("solution", solution);
                                params.put("handler", ogUser.getPname() + "@" + ogUser.getUsername());
                                params.put("closeTime", new Date().getTime());
                                Map<String, Object> result = eventForeignService.closeAlertByItsmId(params);
                                log.info("------执行通知告警关闭接口调用结果 result:{}-----", result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            description = (String) fields.get(EventFieldConstants.SOLVE_PLAN);
                            incidentSubEvent.setSolveTime(DateUtils.getTime());
                            // 点击解决按钮到确认关闭环节由发起人关闭
                            variables.put("closeId", createdBy);
                            incidentHandlerUserId = createdBy;
                            nextTaskName = "确认关闭";
                        }
                        incidentSubEventService.updateIncidentSubEventByNo(incidentSubEvent);
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.SOLVE_EVENT.getInfo(), description, nowDate);
                    } else if ("5".equals(reCode)) {
                        // 转派其他一线接单
                        if(StringUtils.isEmpty(assignedPerson)) {
                            // 如果当前处理人为空，则取值当前操作人
                            assignedPerson = CustomerBizInterceptor.currentUserThread.get().getUserId();
                        }
                        variables.put("forwardOneLineReceive", assignedPerson);
                        incidentHandlerUserId = assignedPerson;
                        nextTaskName = "转派一线接单";
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.ASSIGN_EVENT.getInfo(), statusStr, nowDate);
                    }
                    break;
                case "转派一线接单":
                    // 接单后赋值一线分派人
                    variables.put("dealId", assignedPerson);
                    incidentHandlerUserId = assignedPerson;
                    eventSolveShowFlag = true;
                    nextTaskName = "处理";
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), statusStr, nowDate);

                    break;
                case "二线接单":
                    // 二线部门接单
                    variables.put("preSolutionId", secondDealPerson);
                    incidentHandlerUserId = secondDealPerson;
                    eventSolveShowFlag = true;
                    nextTaskName = "二线部门";
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), EventOperationType.RECEIVE_EVENT.getInfo(), nowDate);

                    break;
                case "业务接单":
                    // 业务接单
                    variables.put("businessDept", secondDealPerson);
                    incidentHandlerUserId = secondDealPerson;
                    eventSolveShowFlag = true;
                    nextTaskName = "业务部门";
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), EventOperationType.RECEIVE_EVENT.getInfo(), nowDate);

                    break;
                case "二线部门":
                    String description = "";
                    if ("0".equals(reCode)) {
                        // 退回一线取转派一线接单
                        variables.put("forwardOneLineReceive", assignedPersonDB);
                        incidentHandlerUserId = assignedPersonDB;
                        description = statusStr;
                    } else if ("1".equals(reCode)) {
                        // 二线部门环节解决到一线确认接单环节
                        variables.put("secondDeptConfirm", assignedPersonDB);
                        incidentHandlerUserId = assignedPersonDB;
                        description = (String) fields.get(EventFieldConstants.SECOND_SOLVE_PLAN);
                    }
                    eventSolveShowFlag = true;
                    nextTaskName = "二线部门转一线接单";
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.ASSIGN_EVENT.getInfo(), description, nowDate);

                    break;
                case "业务部门":
                    String description1 = "";
                    if ("0".equals(reCode)) {
                        // 退回一线取转派一线接单
                        variables.put("forwardOneLineReceive", assignedPersonDB);
                        incidentHandlerUserId = assignedPersonDB;
                        description1 = statusStr;
                    } else if ("1".equals(reCode)) {
                        // 业务部门环节解决到一线确认接单环节
                        variables.put("businessDeptConfirm", assignedPersonDB);
                        incidentHandlerUserId = assignedPersonDB;
                        description1 = (String) fields.get(EventFieldConstants.SECOND_SOLVE_PLAN);
                    }
                    eventSolveShowFlag = true;
                    nextTaskName = "业务部门转一线接单";
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.ASSIGN_EVENT.getInfo(), description1, nowDate);

                    break;
                case "二线部门转一线接单":
                    // 一线解决接单
                    variables.put("solutionId", assignedPersonDB);
                    incidentHandlerUserId = assignedPersonDB;
                    eventSolveShowFlag = true;
                    nextTaskName = "处理";
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), EventOperationType.RECEIVE_EVENT.getInfo(), nowDate);

                    break;
                case "业务部门转一线接单":
                    // 一线解决接单
                    variables.put("solutionId", assignedPersonDB);
                    incidentHandlerUserId = assignedPersonDB;
                    eventSolveShowFlag = true;
                    nextTaskName = "处理";
                    operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.RECEIVE_EVENT.getInfo(), EventOperationType.RECEIVE_EVENT.getInfo(), nowDate);

                    break;
                case "确认关闭":
                    if ("-1".equals(reCode)) {
                        // TODO 确认关闭环节退回到服务台，此处分配的人员需要找到对应的当天服务台所在组的值班人
                        // 确认关闭环节退回到服务台
                        backCompletionFlag = "3";
                        incidentSubEventService.updateIncidentSubEvent(eventNo, backCompletionFlag);

                        String receptionId2 = this.selectServiceByGroupName("IT服务台");
                        variables.put("receptionId", receptionId2);
                        incidentHandlerUserId = receptionId2;
                        nextTaskName = "IT服务台分派";
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.BACK_EVENT.getInfo(), StringUtils.isNotEmpty(statusStr) ? statusStr : EventOperationType.BACK_EVENT.getInfo(), nowDate);

                    } else if ("0".equals(reCode)) {
                        // 关闭到流程结束节点，故不做任何设置
                        IncidentSubEvent incidentSubEvent = new IncidentSubEvent();
                        incidentSubEvent.setCloseTime(DateUtils.getTime());
                        incidentSubEvent.setEventNo(eventNo);
                        incidentSubEventService.updateIncidentSubEventByNo(incidentSubEvent);
                        nextTaskName = "结束";
                        lastFlag = true;
                        //incidentHandlerUserId = createdBy;
                        operationDetailsService.saveOperationDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), EventOperationType.CLOSE_EVENT.getInfo(), EventOperationType.CLOSE_EVENT.getInfo(), nowDate);

                    }
                    eventSolveShowFlag = true;
                    break;
            }
            eventConsumeDetailsService.saveEventConsumeDetails(String.valueOf(incidentMap.get(RedundancyFieldEnum.extra1.name)), CustomerBizInterceptor.currentUserThread.get().getUserId(), incidentHandlerUserId, currentTaskName, nextTaskName, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate));
            fields.put("extra5", lastFlag ? null : incidentHandlerUserId);
            log.info("-----当前处理环节 currentTaskName:{},下一处理环节 nextTaskName:{},当前处理人 incidentHandlerUserId:{}-----", currentTaskName, nextTaskName, incidentHandlerUserId);
        }
        return eventSolveShowFlag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult cancelApply(String instanceId) {
        //获取该流程的当前任务环节对象
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).orderByTaskCreateTime().desc().list().get(0);
        String code = StrUtil.subBefore(historicTaskInstance.getProcessDefinitionId(), ":", false);
        dynamicTableName(code);
        Integer bizId = customerFormMapper.selectOneByProcessId(instanceId);
        String newStatus = "已取消";
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(INSTANCE_ID, instanceId).set(STATUS, CANCEL.getInfo()).set(STAGE, stageMap.get(CANCEL.getInfo())).set("close_time", DateUtils.getDate()).set(PROBLEM_UPDATE_TIME, DateUtils.getDate()).set("updated_time", DateUtils.getNowDate()).set("current_deal_id", null));
            DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                    .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
            Map<String, Object> map = new HashMap<>();
            map.put(STAGE, CLOSE.getInfo());
            map.put(CLOSE_TIME, DateUtils.dateTimeNow(YYYY_MM_DD));
            map.put(PROBLEM_UPDATE_TIME, DateUtils.getDate());
            map.put(CURRENT_HANDLER_ID, null);
            String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
            designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
            newStatus = CANCEL.getInfo();
        } else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
            customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(INSTANCE_ID, instanceId).set(STATUS, CANCEL.getInfo()).set(STAGE, stageMap.get(CANCEL.getInfo())).set("close_time", DateUtils.getDate()).set("task_updated_time", DateUtils.getDate()).set("updated_time", DateUtils.getNowDate()).set("current_handler_id", null));
            DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                    .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
            Map<String, Object> map = new HashMap<>();
            map.put(STAGE, CLOSE.getInfo());
            map.put(CLOSE_TIME, DateUtils.dateTimeNow(YYYY_MM_DD));
            map.put("current_handler_id", null);
            map.put("task_updated_time", DateUtils.dateTimeNow(YYYY_MM_DD));
            String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), map);
            designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
            newStatus = CANCEL.getInfo();
        } else {
            customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set("status", "取消"));
        }
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
        List<Map<String, Object>> records = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq(INSTANCE_ID, instanceId));
        MybatisPlusConfig.customerTableName.remove();
        //获取当前任务节点的状态
        if(WorkOrderInformation.change.getCode().equals(code)||WorkOrderInformation.changeTask.getCode().equals(code)){
            processService.cancelApply(instanceId, "该任务已被：" + CustomerBizInterceptor.currentUserThread.get().getPname() + "撤销！");
            insertFlowLog(historicTaskInstance.getId(), historicTaskInstance.getProcessDefinitionId(), instanceId, code,
                    String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)), CustomerBizInterceptor.currentUserThread.get().getPname() + "撤销了该任务",
                    "撤销流程", CustomerBizInterceptor.currentUserThread.get().getOrgId());
            dynamicTableName(code);
            if(WorkOrderInformation.change.getCode().equals(code)){
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set("changeStatus", "取消"));
                ChangeUtil.ADPM_POOL.submit(instanceId,()->{
                    base.updateAdpmChange(instanceId);
                });
            }else{
                customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set("taskStatus", "取消"));
                ChangeUtil.ADPM_POOL.submit(instanceId,()->{
                    base.updateAdpmTask(instanceId);
                });
            }
            MybatisPlusConfig.customerTableName.remove();
            return AjaxResult.success();
        }
        FormStatusActivityNode formStatusActivityNode = formStatusActivityNodeService.getOne(Wrappers.<FormStatusActivityNode>query().eq(FormStatusActivityNode.COL_ACTIVITY_NODE_ID, taskService.createTaskQuery().processInstanceId(instanceId).list().get(0).getTaskDefinitionKey()).eq(FormStatusActivityNode.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
        OperationDetails details = OperationDetails.builder().newValue(newStatus).oldValue(formStatusActivityNode.getBizStatusName()).operationType(formName).bizNo(String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)))
                .description(CustomerBizInterceptor.currentUserThread.get().getPname() + "已撤销了该流程~").build();
        // 事件单需要保存该操作类型
        if (code.equals(code.equals(WorkOrderInformation.incident.getCode()))) {
            details.setOperationType(EventOperationType.CLOSE_EVENT.getInfo());
        }
        operationDetailsService.save(details);
        processService.cancelApply(instanceId, "该任务已被：" + CustomerBizInterceptor.currentUserThread.get().getPname() + "撤销！");
        insertFlowLog(historicTaskInstance.getId(), historicTaskInstance.getProcessDefinitionId(), instanceId, code,
                String.valueOf(records.get(0).get(RedundancyFieldEnum.extra1.name)), CustomerBizInterceptor.currentUserThread.get().getPname() + "撤销了该任务",
                "撤销流程", CustomerBizInterceptor.currentUserThread.get().getOrgId());

        return AjaxResult.success();
    }


    @Override
    public AjaxResult suspendOrActiveApply(String code, String instanceId, String bizNo) {
        boolean suspended = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult().isSuspended();
        if (suspended) {
            runtimeService.activateProcessInstanceById(instanceId);
            return AjaxResult.success("激活成功");
        } else {
            runtimeService.suspendProcessInstanceById(instanceId);
            return AjaxResult.success("挂起成功");

        }

    }


    @Override
    public AjaxResult selectHistoryList(String code, String instanceId) {
        if (StringUtils.isEmpty(instanceId)) {
            return AjaxResult.success();
        }
        List<HistoricActivity> activityList = new ArrayList<>();
        List<HistoricActivityInstance> historyList = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId)
                .activityType("userTask")
                .orderByHistoricActivityInstanceStartTime()
                .desc()
                .list();
        historyList.forEach(instance -> {
            HistoricActivity activity = new HistoricActivity();
            BeanUtils.copyProperties(instance, activity);
            String taskId = instance.getTaskId();
            List<Comment> comment = taskService.getTaskComments(taskId, "comment");
            if (!CollectionUtils.isEmpty(comment)) {
                activity.setComment(comment.get(0).getFullMessage());
            }
            activity.setAssigneeName(instance.getAssignee());
            OgUser ogUser = ogUserService.selectOgUserByUserId(instance.getAssignee());
            if (ogUser != null) {
                activity.setAssigneeName(ogUser.getPname());
            }
            activityList.add(activity);
        });
        //把初始申请人信息添加至历史审批记录中
        HistoricActivity startActivity = new HistoricActivity();
        //获取申请人信息记录
        dynamicTableName(code);
        String applyUserId = customerFormMapper.selectApplyUser(instanceId);
        OgUser applyUser = ogUserService.selectOgUserByUserId(applyUserId);
        //根据code获取当前页面名称
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        String codeName = customerFormMapper.getFormName(currentTableInfo.get("id"));
        startActivity.setAssigneeName(applyUser.getPname());
        startActivity.setActivityName(codeName + "填写基本信息");
        startActivity.setComment(codeName + "填写基本信息");
        startActivity.setProcessInstanceId(instanceId);
        //获取流程实例第一个节点的开始时间作为流程实例启动时间
        startActivity.setStartTime(activityList.get(activityList.size() - 1).getStartTime());
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        if (Optional.ofNullable(historicProcessInstance.getEndTime()).isPresent()) {
            startActivity.setEndTime(historicProcessInstance.getEndTime());
            startActivity.setDurationInMillis(startActivity.getEndTime().getTime() - startActivity.getStartTime().getTime());
        }
        activityList.add(startActivity);
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(activityList);
    }


    @Override
    public String activityXmlResource(String code) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(code)
                .orderByProcessDefinitionVersion()
                .desc()
                .list()
                .get(0);
        RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        BpmnXMLConverter converter = new BpmnXMLConverter();
        byte[] bytes1 = converter.convertToXML(bpmnModel);
        String xmlStr = new String(bytes1);
        return xmlStr;
    }


    @Override
    public void createHistoryImage(String instanceId, HttpServletResponse response) {
        getActivitiProccessImage(instanceId, response);
    }


    @Override
    public AjaxResult addCaddCandidateUser(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return AjaxResult.success("委派成功");
    }

    @Override
    public AjaxResult problemTaskList(String problemNo, Page page) {
        //   获取表单版本ID、表单版本号
//        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix,WorkOrderInformation.problem_task.getCode() ),null);
//        Map<String, Object> resultMap = new HashMap<>();
//        dynamicTableName(WorkOrderInformation.problem_task.getCode());
//        //code processId version id  type
//        QueryWrapper<CustomerFormContent> queryWrapper = Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name,
//                PROBLEM_CONTENT, CURRENT_TASK_HANDLER_ID, STATUS,"id","").eq(PROBLEM_NO, problemNo);
//        Page page1 = customerFormMapper.selectMapsPage(page, queryWrapper);
//        resultMap.put("pageListInfo", page1);
//        MybatisPlusConfig.customerTableName.remove();
//        return AjaxResult.success(page1);
        String code = WorkOrderInformation.problem_task.getCode();
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
        //获取表名的中文名
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));

        //构造查询对象的ID集合
        Map<String, Object> resultMap = new HashMap<>();
        dynamicTableName(code);
        //QueryWrapper queryWrapper1 = buildQueryWrapper(formFieldInfos, list , customerFormListDTO.getConCondition(), ids, "1", formName);
        StringBuilder key = new StringBuilder("id,instance_id,created_by");
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("name", "status");
        statusMap.put("description", "工单状态");
        statusMap.put("display", "1");
        formFieldInfos.add(0, statusMap);
        Map<String, String> bizNo = new HashMap<>();
        bizNo.put("name", RedundancyFieldEnum.extra1.name);
        bizNo.put("description", formName + "编号");
        bizNo.put("display", "1");
        formFieldInfos.add(0, bizNo);
        formFieldInfos.forEach(a -> {
            key.append("," + a.get("name"));
        });
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(key.toString());
        queryWrapper.eq("problem_no", problemNo);
        queryWrapper.orderByDesc("created_time");
        Page page1 = customerFormMapper.selectMapsPage(page, queryWrapper);
        buildResultRecodes(WorkOrderInformation.problem_task.getCode(), page1, String.valueOf(currentTableInfo.get("version")));
        //构造返回结果
        //buildResultList(code, "1", page1,String.valueOf(currentTableInfo.get("version")) );
        resultMap.put("pageListInfo", page1);
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(page1);

    }

    @Override
    public AjaxResult getOperationDetails(String bizNo, Page page) {
        Page page1 = operationDetailsService.page(page, Wrappers.<OperationDetails>query().eq(OperationDetails.COL_BIZ_NO, bizNo).orderByDesc(OperationDetails.COL_ID));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", page1.getTotal());
        List<Map<String, Object>> colLists = new ArrayList<>();

        Map<String, Object> colMap1 = new HashMap<>();
        Map<String, Object> colMap2 = new HashMap<>();
        Map<String, Object> colMap3 = new HashMap<>();
        Map<String, Object> colMap4 = new HashMap<>();
        Map<String, Object> colMap5 = new HashMap<>();
        colMap1.put("label", "操作类型");
        colMap1.put("val", "operationType");
        colMap2.put("label", "表单编号");
        colMap2.put("val", "bizNo");
        colMap3.put("label", "描述");
        colMap3.put("val", "description");
        colMap4.put("label", "提交人");
        colMap4.put("val", "createdName");
        colMap5.put("label", "提交时间");
        colMap5.put("val", "createdTime");
        colLists.add(colMap1);
        colLists.add(colMap2);
        colLists.add(colMap3);
        colLists.add(colMap4);
        colLists.add(colMap5);
        resultMap.put("col", colLists);
        resultMap.put("pageNum", page.getCurrent());
        resultMap.put("pageSize", page.getSize());
        resultMap.put("list", page1.getRecords());

        return AjaxResult.success(resultMap);
    }


    @Override
    public AjaxResult insertOperationDetails(String code, OperationDetails operationDetails) {
        if(operationDetails.getDescription()==null||"".equals(operationDetails.getDescription().trim())){
            return AjaxResult.warn("请填入内容");
        }
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
        operationDetails.setOperationType(formName);
        if(WorkOrderInformation.change.getCode().equals(code)||WorkOrderInformation.changeTask.getCode().equals(code)){
            String userId = CustomerBizInterceptor.currentUserThread.get().getpId();
            operationDetails.setCreatedTime(new Date());
            operationDetails.setCreatedBy(userId);
            OgUser user = ogUserService.selectOgUserByUserId(userId);
            OgPerson ogPerson = ogPersonService.selectOgPersonById(userId);
            operationDetails.setCreatedName(ogPerson.getpName()+"("+user.getUsername()+")");
            operationDetailsService.saveOperationDetailsforChange(operationDetails);
            return AjaxResult.success();
        }
        // 事件单单独保存详情信息的分类
        if (code.equals(WorkOrderInformation.incident.getCode())) {
            operationDetails.setOperationType(EventOperationType.GENERAL_INFORMATION.getInfo());
        }
        operationDetailsService.save(operationDetails);
        return AjaxResult.success();
    }

    public void getActivitiProccessImage(String pProcessInstanceId, HttpServletResponse response) {
        //logger.info("[开始]-获取流程图图像");
        try {
            //  获取历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(pProcessInstanceId).singleResult();

            if (historicProcessInstance == null) {
                throw new BusinessException("获取流程实例ID[" + pProcessInstanceId + "]对应的历史流程实例失败！");
            } else {

                // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
                List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(pProcessInstanceId).orderByHistoricActivityInstanceId().asc().list();

                // 已执行的节点ID集合
                List<String> executedActivityIdList = new ArrayList<String>();
                int index = 1;
                //logger.info("获取已经执行的节点ID");
                for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                    executedActivityIdList.add(activityInstance.getActivityId());
                    index++;
                }

                BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

                // 已执行的线集合
                List<String> flowIds = new ArrayList<String>();
                // 获取流程走过的线 (getHighLightedFlows是下面的方法)
                flowIds = getHighLightedFlows(bpmnModel, historicActivityInstanceList);

                Set<String> currIds = runtimeService.createExecutionQuery().processInstanceId(pProcessInstanceId).list()
                        .stream().map(e -> e.getActivityId()).collect(Collectors.toSet());

                ICustomProcessDiagramGenerator diagramGenerator = (ICustomProcessDiagramGenerator) processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
                InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,
                        flowIds, "宋体", "宋体", "宋体", null, 1.0, new Color[]{WorkflowConstants.COLOR_NORMAL, WorkflowConstants.COLOR_CURRENT}, currIds);

                response.setContentType("image/png");
                OutputStream os = response.getOutputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                imageStream.close();
            }
            //logger.info("[完成]-获取流程图图像");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CustomerBusinessEnum.ACTIVITY_IMAGE_EXCEPTION.getCode(), CustomerBusinessEnum.ACTIVITY_IMAGE_EXCEPTION.getDesc());
        }
    }

    private List<String> getHighLightedFlows(BpmnModel
                                                     bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId

        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 对历史流程节点进行遍历
            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());


            List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// 用以保存后续开始时间相同的节点
            FlowNode sameActivityImpl1 = null;

            HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// 第一个节点
            HistoricActivityInstance activityImp2_;

            for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
                activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点

                if (activityImpl_.getActivityType().equals("userTask") && activityImp2_.getActivityType().equals("userTask") &&
                        df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))) //都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
                {

                } else {
                    sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());//找到紧跟在后面的一个节点
                    break;
                }

            }
            sameStartTimeNodes.add(sameActivityImpl1); // 将后面第一个节点放在时间相同节点的集合里
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

                if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))) {// 如果第一个节点和第二个节点开始时间相同保存
                    FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {// 有不相同跳出循环
                    break;
                }
            }
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows(); // 取出节点的所有出去的线

            for (SequenceFlow pvmTransition : pvmTransitions) {// 对所有的线进行遍历
                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(pvmTransition.getTargetRef());// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }

        }
        return highFlows;

    }


    /**
     * 构造查询条件
     *
     * @param fieldInfos
     * @param searchDTOList
     * @param conCondition
     * @return
     */
    private QueryWrapper buildQueryWrapper
    (String code, List<Map<String, String>> fieldInfos, List<CustomerFormSearchDTO> searchDTOList, String
            conCondition, List<String> ids, String type, String formName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        StringBuilder key = new StringBuilder("id,instance_id");
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("name", "status");
        statusMap.put("description", "工单状态");
        statusMap.put("display", "1");
        statusMap.put("exportable", "0");
        statusMap.put("editable", "0");
        statusMap.put("record_data_change", "0");
        fieldInfos.add(0, statusMap);
        Map<String, String> bizNo = new HashMap<>();
        bizNo.put("name", RedundancyFieldEnum.extra1.name);
        bizNo.put("description", formName + "编号");
        bizNo.put("display", "1");
        bizNo.put("exportable", "0");
        bizNo.put("editable", "0");
        bizNo.put("record_data_change", "0");
        fieldInfos.add(1, bizNo);
        if (code.equals(WorkOrderInformation.incident.getCode())) {
            // 事件单新加创建时间
            Map<String, String> createMap = new HashMap<>();
            createMap.put("name", "created_time");
            createMap.put("description", "创建时间");
            createMap.put("display", "1");
            createMap.put("exportable", "0");
            createMap.put("editable", "0");
            createMap.put("record_data_change", "0");
            fieldInfos.add(2, createMap);

            Map<String, String> currentDealMap = new HashMap<>();
            currentDealMap.put("name", "extra5");
            currentDealMap.put("description", "事件当前处理人");
            currentDealMap.put("display", "1");
            currentDealMap.put("exportable", "0");
            currentDealMap.put("editable", "0");
            currentDealMap.put("record_data_change", "0");
            fieldInfos.add(3, currentDealMap);
        }

        fieldInfos.forEach(a -> {
            key.append("," + a.get("name"));
        });
        queryWrapper.select(key.toString());
        if (searchDTOList != null && searchDTOList.size() > 0) {
            build(queryWrapper, searchDTOList.get(0));
        }
        if (!StringUtils.isBlank(conCondition) && searchDTOList.size() > 1) {
            if ("or".equals(conCondition)) {
                queryWrapper.or();
                build(queryWrapper, searchDTOList.get(1));
            } else {
                build(queryWrapper, searchDTOList.get(1));
            }
        }
        if (("1").equals(type)) {
            queryWrapper.eq("created_by", CustomerBizInterceptor.currentUserThread.get().getUserId());
        }
        if (CollectionUtil.isNotEmpty(ids) && (!("1").equals(type))) {
            if (code.equals(WorkOrderInformation.incident.getCode())) {
                queryWrapper.in("extra5", CustomerBizInterceptor.currentUserThread.get().getUserId());
            } else {
                queryWrapper.in("id", ids);
            }
            if(code.equals(WorkOrderInformation.chm_task.getCode())){
                queryWrapper.ne("status","已关闭");
            }
        }
        if (CollectionUtil.isEmpty(ids) && (!("1").equals(type))) {
            queryWrapper.ne("instance_id", null);
        }
        queryWrapper.orderByDesc("id");
        return queryWrapper;
    }


    /**
     * 构造动态表名
     *
     * @param code 表名
     */
    private void dynamicTableName(String code) {
        MybatisPlusConfig.customerTableName.set(String.format("%s_%s", bizTablePrefix, code));
    }


    public QueryWrapper build(QueryWrapper queryWrapper, CustomerFormSearchDTO customerFormSearchDTO) {
        switch (customerFormSearchDTO.getSearchCondition()) {
            case "eq":
                queryWrapper.eq(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "ne":
                queryWrapper.ne(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
            case "like":
                queryWrapper.like(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "gt":
                queryWrapper.gt(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "ge":
                queryWrapper.ge(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "lt":
                queryWrapper.lt(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "le":
                queryWrapper.le(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
        }
        return queryWrapper;
    }

    /**
     * 构建查询集合IDS ----代办 、 已办
     *
     * @param code
     * @param currentTableInfo
     * @param type             1--查看自己所有的  2--查看自己代办的  3--查看自己已办的
     */


    private List<String> buildQueryIds(String code, Map<String, Long> currentTableInfo, String type) {
        List<String> businessIds = new ArrayList<>();
        String version = String.valueOf(currentTableInfo.get("version"));
        if ("1".equals(type)) {
            //TODO  这里需要添加版本控制维度   逻辑和代办已办一样  先查询activity中对应的版本控制
            return businessIds;
        } else if ("2".equals(type)) {
            String businessKeyContent = String.format("%s_%s", code, version);
            List<Task> allTodoTaskList = new ArrayList<>();
            // 组任务查询需要获取组的list集合id
            List<String> groupsList = this.getGroupsList(CustomerBizInterceptor.currentUserThread.get().getUserId());
            allTodoTaskList = taskService.createTaskQuery().taskCandidateGroupIn(groupsList).processDefinitionKey(code).list();
            //机构代办任务查询
            OgPerson ogPerson = ogPersonService.selectOgPersonById(CustomerBizInterceptor.currentUserThread.get().getUserId());
            List<Task> orgTaks = taskService.createTaskQuery().taskCandidateGroup(ogPerson.getOrgId()).processDefinitionKey(code).list();
            allTodoTaskList.addAll(orgTaks);
            List<Task> todoTasks = processService.findTodoTasks(CustomerBizInterceptor.currentUserThread.get().getUserId(), code);
            allTodoTaskList.addAll(todoTasks);
            //获取代办所有的流程实例ID集合
            Set<String> collect = allTodoTaskList.stream().map(Task::getProcessInstanceId).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(collect)) {
                return businessIds;
            }
            runtimeService.createProcessInstanceQuery().processInstanceIds(collect).list()
                    .stream()
                    .filter(a -> a.getBusinessKey().contains(businessKeyContent))
                    .forEach(a -> businessIds.add(StrUtil.subAfter(a.getBusinessKey(), "_", true)));

//            allTodoTaskList.forEach(a -> {
//                TaskEntityImpl taskImpl = (TaskEntityImpl) a;
//                String businessKey = runtimeService.createProcessInstanceQuery().processInstanceId(taskImpl.getProcessInstanceId()).singleResult().getBusinessKey();
//                if (businessKey.contains(String.format("%s_%s", code, version))) {
//                    businessIds.add(StrUtil.subAfter(businessKey, "_", true));
//                }
//            });
            return businessIds;
        } else if ("3".equals(type)) {
            //未结束流程
            List<HistoricTaskInstance> unfinishedList = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(CustomerBizInterceptor.currentUserThread.get().getUserId()).unfinished().processDefinitionKey(code)
                    .list();
            //已结束流程
            List<HistoricTaskInstance> finishedList = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(CustomerBizInterceptor.currentUserThread.get().getUserId()).finished().processDefinitionKey(code)
                    .list();

            unfinishedList.addAll(finishedList);
            String businessKeyContent = String.format("%s_%s", code, version);
            //获取当前登录人所有已办的流程实例ID
            Set<String> collect = unfinishedList.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(collect)) {
                return businessIds;
            }
            historyService.createHistoricProcessInstanceQuery().processInstanceIds(collect).list()
                    .stream()
                    .filter(a -> a.getBusinessKey().contains(businessKeyContent))
                    .forEach(a -> businessIds.add(StrUtil.subAfter(a.getBusinessKey(), "_", true)));
            return businessIds;
        }
        return businessIds;
    }

    /**
     * 根据人员id获取机构id｜角色id集合｜工作组集合id
     *
     * @param userId
     * @return
     */
    public List<String> getGroupsList(String userId) {
        List<String> reList = new ArrayList<>();
        // 获取工作组
        List<OgGroup> ogGroups = sysWorkService.selectGroupByUserId(userId);
        // 查询角色
        List<OgRole> ogRoles = sysRoleService.selectRolesByUserId(userId);
        // 机构
        OgPerson ogPerson = ogPersonService.selectOgPersonEvoById(userId);
        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(ogPerson)) {
            reList.add(ogPerson.getOrgId());
        }
        if (!CollectionUtils.isEmpty(ogGroups)) {
            List<String> groupIdList = ogGroups.stream().map(group -> {
                return group.getGroupId();
            }).collect(Collectors.toList());
            reList.addAll(groupIdList);
        }
        if (!CollectionUtils.isEmpty(ogRoles)) {
            List<String> rIdList = ogRoles.stream().map(role -> {
                return role.getRid();
            }).collect(Collectors.toList());
            reList.addAll(rIdList);
        }
        return reList;
    }

    private void buildResultRecodes(String code, Page page1, String version) {
        List<Map<String, Object>> records = (List<Map<String, Object>>) page1.getRecords();
        // 人员,部门,基础数据转中文展示
        IDCodeConvertChineseUtil.convertIdToName(code, records);
        String loginUser = CustomerBizInterceptor.currentUserThread.get().getUserId();
        records.forEach(record -> {
            record.put("code", code);
            record.put("version", version);
            String creatUserId = record.get("created_by").toString();
            String assistantId = record.get("assistant_id").toString();
            String status = record.get("status").toString();
            record.put("processId", record.get("instance_id"));
            if (status.equals("待提交")) {
                record.put("type", 1);
            }
            if (status.equals("已指派") || status.equals("处理中")) {
                if (loginUser.equals(assistantId)) {
                    record.put("type", 2);
                } else {
                    record.put("type", 3);
                }
            }
            if (status.equals("申请关闭中")) {

                if (loginUser.equals(creatUserId)) {
                    record.put("type", 2);
                } else {
                    record.put("type", 3);
                }
            }
            if (status.equals("已关闭")) {
                record.put("type", 3);
            }
            if (ObjectUtil.isNotEmpty(record.get("instance_id"))) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(record.get("instance_id").toString())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    TaskEntityImpl task = (TaskEntityImpl) taskList.get(0);
                    record.put("taskId", task.getId());
                }
            }
        });

    }

    private void buildResultList(String code, String type, Page page1) {
        List<Map<String, Object>> records = (List<Map<String, Object>>) page1.getRecords();
        // 人员,部门,基础数据转中文展示
        IDCodeConvertChineseUtil.convertIdToName(code, records);
        IDCodeConvertChineseUtil.convertEnumToName(code, records);
        if ("1".equals(type)) {
            records.forEach(a -> {
                List<Map<String, Object>> buttonList = new ArrayList<>();
                if (ObjectUtil.isNotEmpty(a.get("instance_id"))) {
                    List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                            .processInstanceId(a.get("instance_id").toString())
//                        .singleResult();
                            .list();    // 例如请假会签，会同时拥有多个任务
                    if (!CollectionUtils.isEmpty(taskList)) {
                        TaskEntityImpl task = (TaskEntityImpl) taskList.get(0);
                        a.put("taskId", task.getId());
                        if (task.getSuspensionState() == CustomerFlowConstants.PROCESS_INSTANCE_SUSPEND) {
                            Map<String, Object> buttonMap = new HashMap<>();
                            a.put("taskName", "已挂起");
                            buttonMap.put("buttonName", CustomerFlowConstants.activityButtonFlowName);
                            buttonMap.put("buttonUrlPath", CustomerFlowConstants.activityButtonFlowPath);
                            buttonList.add(buttonMap);
                            if (!code.equals(WorkOrderInformation.problem.getCode())) {
                                Map<String, Object> cancelButton = new HashMap<>();
                                cancelButton.put("buttonName", CustomerFlowConstants.cancelFlowButtonName);
                                cancelButton.put("buttonUrlPath", CustomerFlowConstants.cancelFlowButtonPath);
                                buttonList.add(cancelButton);
                            }
                            Map<String, Object> baseDataButton = new HashMap<>();
                            baseDataButton.put("buttonName", CustomerFlowConstants.baseDataButtonName);
                            baseDataButton.put("buttonUrlPath", CustomerFlowConstants.baseDataButtonPath);


                            buttonList.add(baseDataButton);

                            Map<String, Object> approveHistoryButton = new HashMap<>();
                            approveHistoryButton.put("buttonName", CustomerFlowConstants.approveHistoryButtonName);
                            approveHistoryButton.put("buttonUrlPath", CustomerFlowConstants.approveHistoryButtonPath);
                            buttonList.add(approveHistoryButton);


                            Map<String, Object> approvalImageButton = new HashMap<>();
                            approvalImageButton.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                            approvalImageButton.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                            buttonList.add(approvalImageButton);

                            a.put("buttonInfo", buttonList);
                        } else {
                            a.put("taskName", task.getName());
                            Map<String, Object> buttonMap = new HashMap<>();
                            buttonMap.put("buttonName", CustomerFlowConstants.suspendFlowButtonName);
                            buttonMap.put("buttonUrlPath", CustomerFlowConstants.suspendFlowButtonPath);
                            buttonList.add(buttonMap);
                            if (!code.equals(WorkOrderInformation.problem.getCode())) {
                                Map<String, Object> cancelButton = new HashMap<>();
                                cancelButton.put("buttonName", CustomerFlowConstants.cancelFlowButtonName);
                                cancelButton.put("buttonUrlPath", CustomerFlowConstants.cancelFlowButtonPath);
                                buttonList.add(cancelButton);
                            }
                            Map<String, Object> baseDataButton = new HashMap<>();
                            baseDataButton.put("buttonName", CustomerFlowConstants.baseDataButtonName);
                            baseDataButton.put("buttonUrlPath", CustomerFlowConstants.baseDataButtonPath);
                            buttonList.add(baseDataButton);
                            Map<String, Object> approveHistoryButton = new HashMap<>();
                            approveHistoryButton.put("buttonName", CustomerFlowConstants.approveHistoryButtonName);
                            approveHistoryButton.put("buttonUrlPath", CustomerFlowConstants.approveHistoryButtonPath);
                            buttonList.add(approveHistoryButton);

                            Map<String, Object> approvalImageButton = new HashMap<>();
                            approvalImageButton.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                            approvalImageButton.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                            buttonList.add(approvalImageButton);
                            a.put("buttonInfo", buttonList);
                        }
                    } else {
                        // 已办结或者已撤销
                        a.put("taskName", "已结束");
                        Map<String, Object> baseDataButton = new HashMap<>();
                        baseDataButton.put("buttonName", CustomerFlowConstants.baseDataButtonName);
                        baseDataButton.put("buttonUrlPath", CustomerFlowConstants.baseDataButtonPath);
                        buttonList.add(baseDataButton);
                        Map<String, Object> approveHistoryButton = new HashMap<>();
                        approveHistoryButton.put("buttonName", CustomerFlowConstants.approveHistoryButtonName);
                        approveHistoryButton.put("buttonUrlPath", CustomerFlowConstants.approveHistoryButtonPath);
                        buttonList.add(approveHistoryButton);

                        Map<String, Object> approvalImageButton = new HashMap<>();
                        approvalImageButton.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                        approvalImageButton.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                        buttonList.add(approvalImageButton);
                        a.put("buttonInfo", buttonList);
                    }
                } else {
                    a.put("taskName", "未启动");
                    if (Arrays.asList(WorkOrderInformation.problem.getCode(), WorkOrderInformation.problem_task.getCode()).contains(code)) {
                        if (Arrays.asList(CANCEL.getInfo(), CLOSE.getInfo()).contains(a.get(STATUS))) {
                            Map<String, Object> buttonMap = new HashMap<>();
                            buttonMap.put("buttonName", CustomerFlowConstants.baseDataButtonName);
                            buttonMap.put("buttonUrlPath", CustomerFlowConstants.baseDataButtonPath);
                            buttonList.add(buttonMap);
                            a.put("buttonInfo", buttonList);
                        } else {
                            Map<String, Object> editButton = new HashMap<>();
                            editButton.put("buttonName", CustomerFlowConstants.editButtonName);
                            editButton.put("buttonUrlPath", CustomerFlowConstants.editButtonPath);
                            buttonList.add(editButton);
                            Map<String, Object> deleteButton = new HashMap<>();
                            deleteButton.put("buttonName", CustomerFlowConstants.cancelButtonName);
                            deleteButton.put("buttonUrlPath", CustomerFlowConstants.deleteButtonPath);
                            buttonList.add(deleteButton);
                            a.put("buttonInfo", buttonList);
                        }
                    } else {
                        Map<String, Object> editButton = new HashMap<>();
                        editButton.put("buttonName", CustomerFlowConstants.editButtonName);
                        editButton.put("buttonUrlPath", CustomerFlowConstants.editButtonPath);
                        buttonList.add(editButton);
                        Map<String, Object> deleteButton = new HashMap<>();
                        deleteButton.put("buttonName", CustomerFlowConstants.deleteButtonName);
                        deleteButton.put("buttonUrlPath", CustomerFlowConstants.deleteButtonPath);
                        buttonList.add(deleteButton);
                        a.put("buttonInfo", buttonList);
                    }
                }
            });
            page1.setRecords(records);
        } else if ("2".equals(type)) {
            //TODO  这里待定优化  暂时思路为获取当前代办节点实例 判断是否为多实例  如果为多实例则查找其中一个为本人的多实例

//            List<String> processId=new ArrayList<>();
//            records.forEach(a->processId.add(String.valueOf(a.get("instance_id"))));
//
//            List<Task> myTodoTask = taskService.createTaskQuery().processInstanceIdIn(processId).list();
//
//            //获取流程模型的definitionId
//            String processDefinitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processId.get(0)).singleResult().getProcessDefinitionId();
//
//            ArrayList<Task> collect = myTodoTask.stream().collect(
//                    Collectors.collectingAndThen(
//                            Collectors.toCollection(
//                                    () -> new TreeSet<>(Comparator.comparing(Task::getProcessInstanceId))
//                            )
//                            , ArrayList::new)
//            );
//            List<Task> realTodoTaskList=new ArrayList<>();
//            collect.forEach(a->{
//                FlowElement flowElement = repositoryService.getBpmnModel(a.getProcessDefinitionId()).getFlowElement(a.getTaskDefinitionKey());
//                if (flowElement instanceof UserTask){
//                    UserTask currentNode = (UserTask) flowElement;
//                    if (currentNode.getBehavior() instanceof ParallelMultiInstanceBehavior){
//                        realTodoTaskList.add(taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
//                                .processInstanceId(a.getProcessInstanceId()).singleResult());
//                    }else {
//                        realTodoTaskList.add(a);
//                    }
//                }
//            });
//
//            records.forEach(a->{
//                List<Map<String, Object>> buttonList = new ArrayList<>();
//                for (Task s :realTodoTaskList) {
//                    TaskEntityImpl task = (TaskEntityImpl) s;
//                    if (a.get("instance_id").equals(s.getProcessInstanceId())){
//                        a.put("taskId",task.getId());
//                        if (task.getSuspensionState() == CustomerFlowConstants.PROCESS_INSTANCE_SUSPEND) {
//                            a.put("taskName", "已挂起");
//                            Map<String, Object> baseDataButton = new HashMap<>();
//                            baseDataButton.put("buttonName", CustomerFlowConstants.baseDataButtonName);
//                            baseDataButton.put("buttonUrlPath", CustomerFlowConstants.baseDataButtonPath);
//                            buttonList.add(baseDataButton);
//                            Map<String, Object> approveHistoryButton = new HashMap<>();
//                            approveHistoryButton.put("buttonName", CustomerFlowConstants.approveHistoryButtonName);
//                            approveHistoryButton.put("buttonUrlPath", CustomerFlowConstants.approveHistoryButtonPath);
//                            buttonList.add(approveHistoryButton);
//
//                            Map<String, Object> approvalImageButton = new HashMap<>();
//                            approvalImageButton.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
//                            approvalImageButton.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
//                            buttonList.add(approvalImageButton);
//                            a.put("buttonInfo", buttonList);
//                        } else {
//                            a.put("taskName", task.getName());
//                            Map<String, Object> buttonMap = new HashMap<>();
//                            buttonMap.put("buttonName", task.getName());
//                            buttonMap.put("buttonUrlPath", CustomerFlowConstants.popButtonPath);
//                            buttonList.add(buttonMap);
//                            Map<String, Object> baseDataButton = new HashMap<>();
//                            baseDataButton.put("buttonName", CustomerFlowConstants.baseDataButtonName);
//                            baseDataButton.put("buttonUrlPath", CustomerFlowConstants.baseDataButtonPath);
//                            buttonList.add(baseDataButton);
//                            Map<String, Object> approveHistoryButton = new HashMap<>();
//                            approveHistoryButton.put("buttonName", CustomerFlowConstants.approveHistoryButtonName);
//                            approveHistoryButton.put("buttonUrlPath", CustomerFlowConstants.approveHistoryButtonPath);
//                            buttonList.add(approveHistoryButton);
//
//                            Map<String, Object> approvalImageButton = new HashMap<>();
//                            approvalImageButton.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
//                            approvalImageButton.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
//                            buttonList.add(approvalImageButton);
//                            a.put("buttonInfo", buttonList);
//                        }
//                    }
//                }
//            });
//            page1.setRecords(records);


            records.forEach(a -> {
                List<Map<String, Object>> buttonList = new ArrayList<>();
//                List<HistoricTaskInstance> unfinished = historyService.createHistoricTaskInstanceQuery()
//                        .taskAssignee(CustomerBizInterceptor.currentUserThread.get().getUserId())
//                        .processInstanceId(String.valueOf(a.get("instance_id"))).unfinished().list();
                List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                        .processInstanceId(String.valueOf(a.get("instance_id")))
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (CollectionUtils.isEmpty(taskList)) {
                    taskList = taskService.createTaskQuery().processInstanceId(String.valueOf(a.get("instance_id"))).list();
                }
                //TaskEntityImpl task1 = (TaskEntityImpl) unfinished.get(0);
                if (!CollectionUtils.isEmpty(taskList)) {
                    TaskEntityImpl task = (TaskEntityImpl) taskList.get(0);
                    a.put("taskId", task.getId());
                    if (task.getSuspensionState() == CustomerFlowConstants.PROCESS_INSTANCE_SUSPEND) {
                        a.put("taskName", "已挂起");
                        Map<String, Object> baseDataButton = new HashMap<>();
                        baseDataButton.put("buttonName", CustomerFlowConstants.baseDataButtonName);
                        baseDataButton.put("buttonUrlPath", CustomerFlowConstants.baseDataButtonPath);
                        buttonList.add(baseDataButton);
                        Map<String, Object> approveHistoryButton = new HashMap<>();
                        approveHistoryButton.put("buttonName", CustomerFlowConstants.approveHistoryButtonName);
                        approveHistoryButton.put("buttonUrlPath", CustomerFlowConstants.approveHistoryButtonPath);
                        buttonList.add(approveHistoryButton);

                        Map<String, Object> approvalImageButton = new HashMap<>();
                        approvalImageButton.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                        approvalImageButton.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                        buttonList.add(approvalImageButton);
                        a.put("buttonInfo", buttonList);
                    } else {
                        a.put("taskName", task.getName());
                        Map<String, Object> buttonMap = new HashMap<>();
                        buttonMap.put("buttonName", task.getName());
                        buttonMap.put("buttonUrlPath", CustomerFlowConstants.popButtonPath);
                        buttonList.add(buttonMap);
                        Map<String, Object> baseDataButton = new HashMap<>();
                        baseDataButton.put("buttonName", CustomerFlowConstants.baseDataButtonName);
                        baseDataButton.put("buttonUrlPath", CustomerFlowConstants.baseDataButtonPath);
                        buttonList.add(baseDataButton);
                        Map<String, Object> approveHistoryButton = new HashMap<>();
                        approveHistoryButton.put("buttonName", CustomerFlowConstants.approveHistoryButtonName);
                        approveHistoryButton.put("buttonUrlPath", CustomerFlowConstants.approveHistoryButtonPath);
                        buttonList.add(approveHistoryButton);

                        Map<String, Object> approvalImageButton = new HashMap<>();
                        approvalImageButton.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                        approvalImageButton.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                        buttonList.add(approvalImageButton);
                        if (code.equals(WorkOrderInformation.problem.getCode())) {
                            Map<String, Object> editButton = new HashMap<>();
                            editButton.put("buttonName", CustomerFlowConstants.editButtonName);
                            editButton.put("buttonUrlPath", CustomerFlowConstants.editButtonPath);
                        }
                        a.put("buttonInfo", buttonList);
                    }
                }
            });
            page1.setRecords(records);
        } else if ("3".equals(type)) {
            records.forEach(a -> {
                List<Map<String, Object>> buttonList = new ArrayList<>();
                Map<String, Object> baseDataButton = new HashMap<>();
                baseDataButton.put("buttonName", CustomerFlowConstants.baseDataButtonName);
                baseDataButton.put("buttonUrlPath", CustomerFlowConstants.baseDataButtonPath);
                buttonList.add(baseDataButton);
                Map<String, Object> approveHistoryButton = new HashMap<>();
                approveHistoryButton.put("buttonName", CustomerFlowConstants.approveHistoryButtonName);
                approveHistoryButton.put("buttonUrlPath", CustomerFlowConstants.approveHistoryButtonPath);
                buttonList.add(approveHistoryButton);

                Map<String, Object> approvalImageButton = new HashMap<>();
                approvalImageButton.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
                approvalImageButton.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
                buttonList.add(approvalImageButton);
                a.put("buttonInfo", buttonList);
                List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(String.valueOf(a.get("instance_id"))).taskAssignee(CustomerBizInterceptor.currentUserThread.get().getUserId()).list();
                historicTaskInstances.forEach(b -> {
                    a.put("taskId", b.getId());
                    a.put("taskName", b.getName());
                    a.put("doneTime", b.getEndTime());
                });
            });
            page1.setRecords(records);
        }
        if (code.equals(WorkOrderInformation.chm_task.getCode())) {
            records.forEach(re -> {
                if (!StringUtils.isEmpty(re.get("contact"))) {
                    OgPerson op = iOgPersonService.selectOgPersonById(re.get("contact").toString());
                    re.put("contact", op == null ? re.get("contact") : op.getpName());
                }
                if (!StringUtils.isEmpty(re.get("assigned_person"))) {
                    re.put("assigned_person", iOgPersonService.selectOgPersonById(re.get("assigned_person").toString()).getpName());
                }
                if (!StringUtils.isEmpty(re.get("assigned_person_cs"))) {
                    re.put("assigned_person_cs", iOgPersonService.selectOgPersonById(re.get("assigned_person_cs").toString()).getpName());
                }
                if (!StringUtils.isEmpty(re.get("current_handler_id"))) {
                    re.put("current_handler_id", iOgPersonService.selectOgPersonById(re.get("current_handler_id").toString()).getpName());
                }
                if (!StringUtils.isEmpty(re.get("assigned_group"))) {
                    //iSysWorkService
                    CommonTree commonTree = iCommonTreeService.selectCommonTreeById(Long.valueOf(re.get("assigned_group").toString()));
                    re.put("assigned_group", commonTree == null ? re.get("assigned_group") : commonTree.getName());
                }
                if (!StringUtils.isEmpty(re.get("assigned_group_cs"))) {
                    re.put("assigned_group_cs", iSysWorkService.selectOgGroupById(re.get("assigned_group_cs").toString()).getGrpName());
                }
                // OgOrg sysDept = deptService.selectDeptById(deptId);
                if (!StringUtils.isEmpty(re.get("report_department"))) {
                    re.put("report_department", re.get("report_department").toString());
                }
                if(StringUtils.isNotEmpty(re.get("created_by"))){
                    OgPerson op = iOgPersonService.selectOgPersonById(re.get("created_by").toString());
                    re.put("created_by",op.getpName());
                }
                if(StringUtils.isNotEmpty(re.get("solution"))){
                    if("0".equals(re.get("solution").toString())){
                        re.put("solution","现场");
                    }
                    if("1".equals(re.get("solution").toString())){
                        re.put("solution","远程");
                    }
                }
                if(StringUtils.isNotEmpty(re.get("fault_type"))){
                    re.put("fault_type",ChmfaultTypenum.getInfo(re.get("fault_type").toString()));
                }
            });
        }
        if (WorkOrderInformation.incident.getCode().equals(code)) {
            records.forEach(a -> {
                a.put("remindeFlag", "否");
                a.put("remindeTime", "");
                a.put("suspendFlag", "否");
                a.put("suspendTime", "");
                List<OperationDetails> reminderOperationDetails = operationDetailsService.list(Wrappers.<OperationDetails>query()
                        .eq(OperationDetails.COL_NEW_VALUE, "催单")
                        .eq(OperationDetails.COL_BIZ_NO, a.get(RedundancyFieldEnum.extra1.getName()))
                        .orderByDesc(OperationDetails.COL_ID));
                if (CollectionUtil.isNotEmpty(reminderOperationDetails)) {
                    a.put("remindeFlag", "是");
                    a.put("remindeTime", reminderOperationDetails.get(0).getCreatedTime());
                }
                List<OperationDetails> suspendOperationDetails = operationDetailsService.list(Wrappers.<OperationDetails>query()
                        .eq(OperationDetails.COL_NEW_VALUE, "挂起")
                        .eq(OperationDetails.COL_BIZ_NO, a.get(RedundancyFieldEnum.extra1.getName()))
                        .orderByDesc(OperationDetails.COL_ID));
                if (CollectionUtil.isNotEmpty(suspendOperationDetails)) {
                    a.put("suspendFlag", "是");
                    a.put("suspendTime", suspendOperationDetails.get(0).getCreatedTime());
                }
                page1.setRecords(records);
            });
        }
    }

    // 人员 部门 基础数据id转名称
    private void convertIdToName(String code, List<Map<String, Object>> records) {
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            records.forEach(problem -> {
                if (ObjectUtil.isNotEmpty(problem.get(ORIGINATOR_ID))) {
                    problem.put(ORIGINATOR_ID, ogPersonService.selectOgPersonById(problem.get(ORIGINATOR_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(SOLVER_ID))) {
                    problem.put(SOLVER_ID, ogPersonService.selectOgPersonById(problem.get(SOLVER_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(AUDITOR_ID))) {
                    problem.put(AUDITOR_ID, ogPersonService.selectOgPersonById(problem.get(AUDITOR_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(ORI_DEP_MANAGER_ID))) {
                    problem.put(ORI_DEP_MANAGER_ID, ogPersonService.selectOgPersonById(problem.get(ORI_DEP_MANAGER_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(MANAGER_ID))) {
                    problem.put(MANAGER_ID, ogPersonService.selectOgPersonById(problem.get(MANAGER_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(CURRENT_HANDLER_ID))) {
                    problem.put(CURRENT_HANDLER_ID, ogPersonService.selectOgPersonById(problem.get(CURRENT_HANDLER_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(SOLVER_DEP_ID))) {
                    problem.put(SOLVER_DEP_ID, iSysDeptService.selectDeptById(problem.get(SOLVER_DEP_ID).toString()).getOrgName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(INTERRUPT_FLAG))) {
                    problem.put(INTERRUPT_FLAG, StringUtils.equals(InterruptFlag.ONE, problem.get(INTERRUPT_FLAG).toString())
                            ? InterruptFlag.CH_ZERO
                            : InterruptFlag.CH_ONE);
                }
                if (ObjectUtil.isNotEmpty(problem.get(CAUSE_CLZ1))) {
                    problem.put(CAUSE_CLZ1, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(CAUSE_CLZ1).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(CAUSE_CLZ2))) {
                    problem.put(CAUSE_CLZ2, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(CAUSE_CLZ2).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_CATEGORY))) {
                    problem.put(PROBLEM_CATEGORY, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_CATEGORY).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_SUBCLZ))) {
                    problem.put(PROBLEM_SUBCLZ, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_SUBCLZ).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_ENTRY))) {
                    problem.put(PROBLEM_ENTRY, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_ENTRY).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_SUBENTRY1))) {
                    problem.put(PROBLEM_SUBENTRY1, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_SUBENTRY1).toString()).getTypeName());
                }
            });
        } else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
            records.forEach(task -> {
                if (StringUtils.isNotEmpty(task.get(CURRENT_HANDLER_ID_TASK))) {
                    OgPerson person = ogPersonService.selectOgPersonById(task.get(CURRENT_HANDLER_ID_TASK).toString());
                    if (person != null)
                        task.put(CURRENT_HANDLER_ID_TASK, person.getpName());
                }
                if (StringUtils.isNotEmpty(task.get(ORIGINATOR_ID))) {
                    OgPerson person = ogPersonService.selectOgPersonById(task.get(ORIGINATOR_ID).toString());
                    if (person != null)
                        task.put(ORIGINATOR_ID, person.getpName());
                }
                //assistant_id
                if (StringUtils.isNotEmpty(task.get("assistant_id"))) {
                    OgPerson person = ogPersonService.selectOgPersonById(task.get("assistant_id").toString());
                    if (person != null)
                        task.put("assistant_id", person.getpName());
                }
                if (ObjectUtil.isNotEmpty(task.get(PROBLEM_CATEGORY))) {
                    task.put(PROBLEM_CATEGORY, ogTypeinfoService.selectOgTypeInfoByTypeNo(task.get(PROBLEM_CATEGORY).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(task.get(PROBLEM_SUBCLZ))) {
                    task.put(PROBLEM_SUBCLZ, ogTypeinfoService.selectOgTypeInfoByTypeNo(task.get(PROBLEM_SUBCLZ).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(task.get(PROBLEM_ENTRY))) {
                    task.put(PROBLEM_ENTRY, ogTypeinfoService.selectOgTypeInfoByTypeNo(task.get(PROBLEM_ENTRY).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(task.get(PROBLEM_SUBENTRY1))) {
                    task.put(PROBLEM_SUBENTRY1, ogTypeinfoService.selectOgTypeInfoByTypeNo(task.get(PROBLEM_SUBENTRY1).toString()).getTypeName());
                }
            });
        } else if (code.equals(WorkOrderInformation.incident.getCode())) {
            records.forEach(event -> {
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.REPORT_ORG))) {
                    OgOrg reportOrg = iSysDeptService.selectDeptByCode(event.get(EventFieldConstants.REPORT_ORG).toString());
                    if (reportOrg != null) {
                        event.put(EventFieldConstants.REPORT_ORG, reportOrg.getOrgName());
                    }
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.REPORT_PERSON))) {
                    OgPerson person = ogPersonService.selectOgPersonById(event.get(EventFieldConstants.REPORT_PERSON).toString());
                    if (person != null)
                        event.put(EventFieldConstants.REPORT_PERSON, person.getpName());
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.ASSIGNED_GROUP))) {
                    OgGroup group = sysWorkService.selectOgGroupById(event.get(EventFieldConstants.ASSIGNED_GROUP).toString());
                    if (group != null)
                        event.put(EventFieldConstants.ASSIGNED_GROUP, group.getGrpName());
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.ASSIGNED_PERSON))) {
                    OgPerson person = ogPersonService.selectOgPersonById(event.get(EventFieldConstants.ASSIGNED_PERSON).toString());
                    if (person != null)
                        event.put(EventFieldConstants.ASSIGNED_PERSON, person.getpName());
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.SIDE_FLAG))) {
                    List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName("side_flag");
                    event.put(EventFieldConstants.SIDE_FLAG, EventFieldConstants.convertParaList(list, event.get(EventFieldConstants.SIDE_FLAG).toString()));
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.FINANCE_FLAG))) {
                    event.put(EventFieldConstants.FINANCE_FLAG, EventFieldConstants.convertFinanceFlag(event.get(EventFieldConstants.FINANCE_FLAG).toString()));
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.URGENT_FLAG))) {
                    event.put(EventFieldConstants.URGENT_FLAG, EventFieldConstants.convertFinanceFlag(event.get(EventFieldConstants.URGENT_FLAG).toString()));
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.ORG_FLAG))) {
                    List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName("org_flag");
                    event.put(EventFieldConstants.ORG_FLAG, EventFieldConstants.convertParaList(list, event.get(EventFieldConstants.ORG_FLAG).toString()));
                }
            });
        } else if (code.equals(WorkOrderInformation.TINYWEB_DB_RECOVER.getCode())) {
            //tinyWeb 数据库恢复 列表 部分字段ID转译
            records.forEach(dbRecover -> {
                if (StringUtils.isNotEmpty(dbRecover.get(TinywebConstants.APP_SYSTEM))) {
                    OgSys ogSys = applicationManagerService.selectOgSysBySysId(dbRecover.get(TinywebConstants.APP_SYSTEM).toString());
                    dbRecover.put(TinywebConstants.APP_SYSTEM, ogSys.getCaption());
                }
                if (StringUtils.isNotEmpty(dbRecover.get(TinywebConstants.CHECK_PEOPLE))) {
                    OgPerson person = ogPersonService.selectOgPersonEvoById(dbRecover.get(TinywebConstants.CHECK_PEOPLE).toString());
                    dbRecover.put(TinywebConstants.CHECK_PEOPLE, person.getpName());
                }
                if (StringUtils.isNotEmpty(dbRecover.get(TinywebConstants.DESENSITIZATION_TYPE))) {

                    List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName(TinywebConstants.DESENSITIZATION_TYPE);
                    for (PubParaValue pubParaValue : list) {
                        if (pubParaValue.getValue().equals(dbRecover.get(TinywebConstants.DESENSITIZATION_TYPE))) {
                            dbRecover.put(TinywebConstants.DESENSITIZATION_TYPE, pubParaValue.getValueDetail());
                            break;
                        }
                    }
                }
            });
        } else if (code.equals(WorkOrderInformation.TINYWEB_FAULT_SOLVE.getCode())) {
            //tinyWeb 故障解决 列表 部分字段ID转译
            records.forEach(faultSolve -> {
                if (StringUtils.isNotEmpty(faultSolve.get(TinywebConstants.APP_SYSTEM))) {
                    OgSys ogSys = applicationManagerService.selectOgSysBySysId(faultSolve.get(TinywebConstants.APP_SYSTEM).toString());
                    faultSolve.put(TinywebConstants.APP_SYSTEM, ogSys.getCaption());
                }
                if (StringUtils.isNotEmpty(faultSolve.get(TinywebConstants.CHECK_PEOPLE))) {
                    OgPerson person = ogPersonService.selectOgPersonEvoById(faultSolve.get(TinywebConstants.CHECK_PEOPLE).toString());
                    faultSolve.put(TinywebConstants.CHECK_PEOPLE, person.getpName());
                }
                if (StringUtils.isNotEmpty(faultSolve.get(TinywebConstants.MALFUNCTION_TYPE))) {

                    List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName(TinywebConstants.FAULT_TYPE);
                    for (PubParaValue pubParaValue : list) {
                        String type = (String) faultSolve.get(TinywebConstants.MALFUNCTION_TYPE);
                        if (pubParaValue.getValue().equals(type)) {
                            faultSolve.put(TinywebConstants.MALFUNCTION_TYPE, pubParaValue.getValueDetail());
                            break;
                        }
                    }
                }
            });
        } else if (code.equals(WorkOrderInformation.TINYWEB_SERVER.getCode())) {
            //tinyWeb 服务请求 列表 部分字段ID转译
            records.forEach(server -> {
                if (StringUtils.isNotEmpty(server.get(TinywebConstants.APP_SYSTEM))) {
                    OgSys ogSys = applicationManagerService.selectOgSysBySysId(server.get(TinywebConstants.APP_SYSTEM).toString());
                    server.put(TinywebConstants.APP_SYSTEM, ogSys.getCaption());
                }
                if (StringUtils.isNotEmpty(server.get(TinywebConstants.CHECK_PEOPLE))) {
                    OgPerson person = ogPersonService.selectOgPersonEvoById(server.get(TinywebConstants.CHECK_PEOPLE).toString());
                    server.put(TinywebConstants.CHECK_PEOPLE, person.getpName());
                }
            });
        } else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
            records.forEach(cell -> {
                if (StringUtils.isNotEmpty(cell.get("current_handler_id"))) {
                    OgPerson person = ogPersonService.selectOgPersonEvoById(cell.get("current_handler_id").toString());
                    cell.put("current_handler_id", person.getpName());
                }
                if (StringUtils.isNotEmpty(cell.get("originator_id"))) {
                    OgPerson person = ogPersonService.selectOgPersonEvoById(cell.get("originator_id").toString());
                    cell.put("originator_id", person.getpName());
                }
            });
        } else if (WorkOrderInformation.change.getCode().equals(code)) {
            records.forEach(p -> {
                Object basisType = p.get("basisType");
                Object type = p.get("type");
                Object reason = p.get("reason");
                Object urgentReason = p.get("urgentReason");
                Object changeLevel = p.get("changeLevel");
                Object currentRiskLevel = p.get("currentRiskLevel");
                if (StringUtils.isNotEmpty(basisType)) {
                    switch (basisType.toString()) {
                        case "1":
                            p.put("basisType", "事件");
                            break;
                        case "2":
                            p.put("basisType", "问题");
                            break;
                        case "3":
                            p.put("basisType", "服务请求");
                            break;
                        case "4":
                            p.put("basisType", "需求");
                            break;
                    }
                } else if (StringUtils.isNotEmpty(type)) {
                    switch (type.toString()) {
                        case "1":
                            p.put("type", "普通变更");
                            break;
                        case "2":
                            p.put("type", "紧急变更");
                            break;
                        case "3":
                            p.put("type", "评审变更");
                            break;
                    }
                } else if (StringUtils.isNotEmpty(reason)) {
                    switch (reason.toString()) {
                        case "1":
                            p.put("reason", "新增需求");
                            break;
                        case "2":
                            p.put("reason", "故障诊断");
                            break;
                        case "3":
                            p.put("reason", "系统缺陷");
                            break;
                        case "4":
                            p.put("reason", "失败变更");
                            break;
                        case "5":
                            p.put("reason", "项目上线");
                            break;
                        case "6":
                            p.put("reason", "系统下线");
                            break;
                        case "7":
                            p.put("reason", "性能调优");
                            break;
                        case "8":
                            p.put("reason", "演练切换");
                            break;
                    }
                } else if (StringUtils.isNotEmpty(urgentReason)) {
                    switch (urgentReason.toString()) {
                        case "1":
                            p.put("urgentReason", "行政要求-外部单位");
                            break;
                        case "2":
                            p.put("urgentReason", "行政要求-业务需求");
                            break;
                        case "3":
                            p.put("urgentReason", "行政要求-科技需求");
                            break;
                        case "4":
                            p.put("urgentReason", "生产修复-已发故障");
                            break;
                        case "5":
                            p.put("urgentReason", "生产修复-潜在问题");
                            break;
                        case "6":
                            p.put("basisType", "生产修复-硬件抢修");
                            break;
                    }
                } else if (StringUtils.isNotEmpty(changeLevel)) {
                    switch (changeLevel.toString()) {
                        case "1":
                            p.put("changeLevel", "一级");
                            break;
                        case "2":
                            p.put("changeLevel", "二级");
                            break;
                        case "3":
                            p.put("changeLevel", "三级");
                            break;
                        case "4":
                            p.put("changeLevel", "四级");
                            break;
                    }
                } else if (StringUtils.isNotEmpty(currentRiskLevel)) {
                    switch (currentRiskLevel.toString()) {
                        case "1":
                            p.put("currentRiskLevel", "低");
                            break;
                        case "2":
                            p.put("currentRiskLevel", "中");
                            break;
                        case "3":
                            p.put("currentRiskLevel", "高");
                            break;
                        case "4":
                            p.put("currentRiskLevel", "重大");
                            break;
                    }
                }
            });
        } else if (WorkOrderInformation.changeTask.getCode().equals(code)) {
            records.forEach(p -> {
                Object type = p.get("type");
                Object preApproval = p.get("preApproval");
                Object assignedGroup = p.get("assignedGroup");
                Object assignee = p.get("assignee");
                Object referApp = p.get("referApp");
                //Object deployWay = p.get("deployWay");
                Object triggerMode = p.get("triggerMode");
                Object checkMan = p.get("checkMan");
                Object implResultCheck = p.get("implResultCheck");
                if (StringUtils.isNotEmpty(type)) {
                    String typeStr = type.toString();
                    ChangeTaskScene scene = changeTaskSceneService.getChangeTaskSceneByCode(typeStr);
                    p.put("type", scene.getName());
                } else if (StringUtils.isNotEmpty(assignedGroup)) {
                    String assignedGroupStr = assignedGroup.toString();
                    OgOrg ogOrg = iSysDeptService.selectDeptById(assignedGroupStr);
                    p.put("assignedGroup", ogOrg.getOrgName());
                } else if (StringUtils.isNotEmpty(referApp)) {
                    String referAppStr = referApp.toString();
                    OgSys ogSys = applicationManagerService.selectOgSysBySysId(referAppStr);
                    p.put("referApp", ogSys.getCaption());
                } else if (StringUtils.isNotEmpty(assignee)) {
                    String assigneeStr = assignee.toString();
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(assigneeStr);
                    p.put("assignee", ogPerson.getpName());
                } else if (StringUtils.isNotEmpty(checkMan)) {
                    String checkManStr = checkMan.toString();
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(checkManStr);
                    p.put("checkMan", ogPerson.getpName());
                } else if (StringUtils.isNotEmpty(preApproval)) {
                    String preApprovalStr = preApproval.toString();
                    OgPerson ogPerson = ogPersonService.selectOgPersonById(preApprovalStr);
                    p.put("preApproval", ogPerson.getpName());
                } else if (StringUtils.isNotEmpty(triggerMode)) {
                    switch (triggerMode.toString()) {
                        case "1":
                            p.put("triggerMode", "手动触发");
                            break;
                        case "2":
                            p.put("triggerMode", "自动触发");
                            break;
                    }
                } else if (StringUtils.isNotEmpty(implResultCheck)) {
                    switch (implResultCheck.toString()) {
                        case "1":
                            p.put("implResultCheck", "成功");
                            break;
                        case "2":
                            p.put("implResultCheck", "失败");
                            break;
                        case "3":
                            p.put("implResultCheck", "缺陷");
                            break;
                        case "4":
                            p.put("implResultCheck", "取消");
                            break;
                    }
                }
            });
        }
    }


    @Override
    public AjaxResult getStartProcessCondition(String code, String businessKey) {
        dynamicTableName(code);
        //获取数据对象  可以参考下面的代码去查想要的字段信息
        List<Map<String, Object>> list;
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("id", "for_error").eq("id", businessKey));
        } else if (code.equals(WorkOrderInformation.incident.getCode())) {
            list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("id", EventFieldConstants.ASSIGNED_GROUP, EventFieldConstants.ASSIGNED_PERSON).eq("id", businessKey));
        } else {
            list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("id").eq("id", businessKey));
        }

        Map<String, Object> resultMap = new HashMap<>();
        //获取该业务流程最新部署后的processDefinition对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(code).orderByProcessDefinitionVersion().desc().list().get(0);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        List<FlowElement> flowElements = (List<FlowElement>) bpmnModel.getMainProcess().getFlowElements();
        Optional<FlowElement> startFlowElement = flowElements.stream().filter(a -> {
            return a instanceof StartEvent ? true : false;
        }).findFirst();
        FlowNode startFlowNode = (FlowNode) startFlowElement.get();
        List<SequenceFlow> outgoingFlows = startFlowNode.getOutgoingFlows();
        outgoingFlows.stream().forEach(a -> {
            if (a.getTargetFlowElement() instanceof UserTask) {
                if (code.equals(WorkOrderInformation.incident.getCode())) {
                    resultMap.put("startNextUserExpression", "${receptionId==" + this.selectServiceByGroupName("IT服务台") + "}");
                    resultMap.put("startExpression", null);
                    resultMap.put("isSelected", "0");
                    resultMap.put("selectField", null);

                }
            } else if (a.getTargetFlowElement() instanceof Gateway) {
                if (code.equals(WorkOrderInformation.problem.getCode())) {
                    // list 待查询for_error字段
                    if (list.get(0).get(FOR_ERROR).equals(ZERO)) {
                        resultMap.put("startExpression", "${reCode==0}");
                        resultMap.put("isSelected", "0");
                        resultMap.put("startNextUserExpression", null);
                        resultMap.put("selectField", null);

                    } else {
                        resultMap.put("startExpression", "${reCode==1}");
                        resultMap.put("startNextUserExpression", "${solver_id}");
                        resultMap.put("isSelected", "1");
                        resultMap.put("selectField", "solver_id");
                    }
                } else if (code.equals(WorkOrderInformation.incident.getCode())) {
                    if (StringUtils.isNotEmpty(list.get(0).get(EventFieldConstants.ASSIGNED_PERSON))) {
                        // 起始环节分派到人
                        resultMap.put("startNextUserExpression", "${dealReceive==" + list.get(0).get(EventFieldConstants.ASSIGNED_PERSON) + "}");
                        resultMap.put("startExpression", "${reCode==2}");
                        resultMap.put("isSelected", "0");
                        resultMap.put("selectField", EventFieldConstants.ASSIGNED_PERSON);
                    } else {
                        // 起始环节到下一节点服务台分派
                        resultMap.put("startExpression", "${reCode==0}");
                        // 默认给一个事件单服务台角色  角色id=9301
                        resultMap.put("startNextUserExpression", "${receptionId==" + this.selectServiceByGroupName("IT服务台") + "}");
                        resultMap.put("isSelected", "0");
                        resultMap.put("selectField", null);
                    }
                }
            }
        });
//        Optional<SequenceFlow> targetFlowElement = outgoingFlows.stream().filter(a -> {
//            return a.getTargetFlowElement() instanceof Gateway ? true : false;
//        }).findFirst();
//        //构造查询开始流程时需要填写的条件 返回给前端  这里只考虑了开始流程后第一个节点为互斥网关的情况 其他网关没考虑
//        if (targetFlowElement.isPresent()) {
//            if (code.equals(WorkOrderInformation.problem.getCode())) {
//                // list 待查询for_error字段
//                if (list.get(0).get(FOR_ERROR).equals(ForError.ZERO)) {
//                    resultMap.put("startExpression", "${reCode==0}");
//                } else {
//                    resultMap.put("startExpression", "${reCode==1}");
//                    resultMap.put("startNextUserExpression", "${solver_id}");
//                    resultMap.put("isSelected", "1");
//                    resultMap.put("selectField", "solver_id");
//                }
//            } else if (code.equals(WorkOrderInformation.incident.getCode())) {
//                // 事件单开始节点之后的流条件和流程处理人｜组  页面发起的默认给一个手工分派
//                resultMap.put("startExpression", "${reCode==0}");
//                // 默认给一个事件单服务台 服务台是个虚拟的机构
//                resultMap.put("startNextUserExpression", "${receptionId == "+CustomerFlowConstants.ASSIGN_ROLE+"}");
//                resultMap.put("isSelected", "0");
//            } else {
//                //startExpression 为配置的EL表达式  示例${reCode==0}  自己的业务逻辑自己处理这里的表达式的值
//                List<SequenceFlow> gateWayOutFlow = ((Gateway) targetFlowElement.get().getTargetFlowElement()).getOutgoingFlows();
//                gateWayOutFlow.forEach(a -> {
//                    if (true) {
//                        //startExpression 为配置的EL表达式  示例${reCode==0}  自己的业务逻辑自己处理这里的表达式的值
//                        resultMap.put("startExpression", a.getConditionExpression());
//                    }
//                });
//            }
//        }
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(resultMap);
    }


    /**
     * 插入流程日志
     *
     * @param taskId               当前任务ID
     * @param processDefinitionId  流程定义ID
     * @param processInstancedId   流程实例ID
     * @param processDefinitionKey 流程key标识
     * @param businessKey          业务key  构成 code_version_业务主键id
     * @param comment              当前描述
     * @param taskName             当前任务名称
     * @param groupId              用户组ID
     */
    public void insertFlowLog(String taskId, String processDefinitionId, String processInstancedId, String
            processDefinitionKey, String businessKey, String comment, String taskName, String groupId) {
        long start = System.currentTimeMillis();
        log.debug("---------插入流程记录开始，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey);
        int count = iPubFlowLogService.selectPubFlowLogByBusinessKey(businessKey);
        //流程记录
        PubFlowLog pubFlowLog = new PubFlowLog();
        //日志id
        pubFlowLog.setLogId(IdUtils.randomUUID());
        //流程类型
        pubFlowLog.setLogType(StrUtil.subBefore(processDefinitionKey, ":", false));
        //businessKey
        pubFlowLog.setBizId(businessKey);

        //下一步任务
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessKey)
                .list();
        StringBuffer users = new StringBuffer();
        StringBuffer usersPhone = new StringBuffer();
        StringBuffer nextTaskId = new StringBuffer();
        StringBuffer nextTaskName = new StringBuffer();
        //操作名称
        String deal = getFlowTransitions(taskId, processDefinitionId, processInstancedId);
        if (taskName.equals("撤销流程")) {
            deal = "关闭流程";
        }
        if (taskName.equals("撤回")) {
            deal = "撤回";
        }

        if (taskList.size() < 1) {
            nextTaskName.append("任务结束:");
        } else {
            for (Task task : taskList) {
                nextTaskId.append(task.getId() + ":");
                nextTaskName.append(task.getName() + ":");
                //查询任务候选人
                List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
                for (IdentityLink id : identityLinkList) {
                    String userId = id.getUserId();
                    if (com.ruoyi.common.utils.StringUtils.isNotEmpty(userId)) {
                        OgPerson ogPerson = iOgPersonService.selectOgPersonEvoById(userId);
                        if (ogPerson != null) {
                            users.append(ogPerson.getpId() + ",");
                            if (com.ruoyi.common.utils.StringUtils.isEmpty(ogPerson.getPhone())) {
                                usersPhone.append(ogPerson.getpName() + "-" + ogPerson.getMobilPhone() + ",");
                            } else {
                                usersPhone.append(ogPerson.getpName() + "-" + ogPerson.getPhone() + ",");
                            }
                        }
                    } else {
                        //组 角色 任务
                        String group = id.getGroupId();
                        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(group) && group.contains("_")) {
                            group = group.substring(0, group.indexOf("_"));
                        }
                        //组 5
                        OgGroup OgGroup = iSysWorkService.selectOgGroupById(group);
                        //角色 21
                        OgRole ogRole = iSysRoleService.selectRoleById(group);
                        //岗位 2
                        //机构
                        OgOrg OgOrg = iSysDeptService.selectDeptById(group);

                        String ogGp = OgGroup == null ? "" : OgGroup.getGroupId();
                        String ogRl = ogRole == null ? "" : ogRole.getRid();
                        String org = OgOrg == null ? "" : OgOrg.getOrgId();

                        users.append(ogGp + ogRl + org + ",");
                    }
                }
                if (!com.ruoyi.common.utils.StringUtils.isEmpty(users)) {
                    users = new StringBuffer(users.substring(0, users.length() - 1));
                    users.append(":");
                }
                if (!com.ruoyi.common.utils.StringUtils.isEmpty(usersPhone)) {
                    usersPhone = new StringBuffer(usersPhone.substring(0, usersPhone.length() - 1));
                    usersPhone.append(":");
                }
            }
        }
        //任务名
        pubFlowLog.setTaskName(taskName);
        OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
        //下一步任务id
        pubFlowLog.setNextTaskId(nextTaskId.toString());
        //下一步任务名
        String nextTName = nextTaskName.toString();
        nextTName = nextTName.substring(0, nextTName.length() - 1);
        pubFlowLog.setNextTaskName(nextTName);
        //下一步处理人
        String failusers = "";
        if (users.indexOf(":") > 0) {
            failusers = users.toString().substring(0, users.length() - 1);
        } else {
            failusers = users.toString();
        }
        pubFlowLog.setNextPerformerDesc(failusers);
        //下一步处理人电话
        if (usersPhone.length() > 3000) {
            String up = usersPhone.toString().substring(0, 3000);
            pubFlowLog.setNextPerformerTel(up + "...");
        } else {
            pubFlowLog.setNextPerformerTel(usersPhone.toString());
        }
        //备注
        pubFlowLog.setPerformDesc(comment);
        //操作人Id
        pubFlowLog.setPerformerId(ogUser.getpId());
        //操作人名称
        pubFlowLog.setPerformerName(ogUser.getUsername());
        //操作名称
        pubFlowLog.setPerformName(deal);
        //操作时间
        pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());

        pubFlowLog.setPerformerOrgId(ogUser.getOrgId());
        //用户机构名称
        pubFlowLog.setPerformerOrgName(ogUser.getOrgname());
        //用户组名
        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(groupId)) {
            OgGroup OgGroup = iSysWorkService.selectOgGroupById(groupId);
            if (OgGroup != null) {
                pubFlowLog.setPerformerGroupId(OgGroup.getGroupId());
                //用户组名
                pubFlowLog.setPerformerGroupName(OgGroup.getGrpName());
            }
        }
        //17  用户电话
        if (com.ruoyi.common.utils.StringUtils.isEmpty(ogUser.getPhonenumber())) {
            pubFlowLog.setPerformerTel("");
        } else {
            pubFlowLog.setPerformerTel(ogUser.getPhonenumber());
        }
        //处理用时间
        //pubFlowLog.setSysResidenceTime(claiTime);
        //状态
        pubFlowLog.setCurrentState("");
        pubFlowLog.setSerialNum(String.valueOf(count + 1));
        pubFlowLog.setLogId(IdUtils.fastSimpleUUID());
        int rows = iPubFlowLogService.insertPubFlowLog(pubFlowLog);
        long end = System.currentTimeMillis();
        if (rows > 0) {
            log.debug("---------插入流程记录结束，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey + ",总耗时:" + (end - start) + "毫秒");
        } else {
            log.debug("---------插入流程记录失败，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey + ",总耗时:" + (end - start) + "毫秒");
        }


    }


    /**
     * 获取操作名称  根据流程历史中最近时间的两个节点 获取流程线对象 并获取线上的名称
     *
     * @param processDefinitionId
     * @param processInstancedId
     * @return
     */
    public String getFlowTransitions(String taskId, String processDefinitionId, String processInstancedId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24小时制
        //不传taskid  是任务发起情况
        if (com.ruoyi.common.utils.StringUtils.isEmpty(taskId)) {
            return "启动任务";
        }
        // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery().processInstanceId(processInstancedId).orderByHistoricActivityInstanceStartTime().asc().list();
        // 判空防止程序取集合报错
        if (CollectionUtils.isEmpty(historicActivityInstances)) {
            return "无";
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        String deal = "";// 用以保存高亮的线name
        //已办理
        HistoricActivityInstance historicActivityInstance1 = null;
        HistoricActivityInstance historicActivityInstance2 = null;
        HistoricActivityInstance historicActivityInstance3 = null;
        for (int i = 0; i < historicActivityInstances.size(); i++) {
            if (com.ruoyi.common.utils.StringUtils.isNotEmpty(historicActivityInstances.get(i).getTaskId()) && historicActivityInstances.get(i).getTaskId().equals(taskId)) {
                //通过taskId 匹配已完成任务
                historicActivityInstance1 = historicActivityInstances.get(i);
                //已完成任务时间=未完成任务开始时间
                if (historicActivityInstance1.getEndTime() == null) {
                    //多人任务，任务未结束
                    return "通过";
                } else {
                    for (HistoricActivityInstance hi : historicActivityInstances) {
                        Long endTime = historicActivityInstance1.getEndTime().getTime();
                        Long hiStartTime = hi.getStartTime().getTime();
                        Long a1 = hiStartTime - endTime;
                        //时间误差 0到2秒钟
                        if (0 <= a1 && a1 <= 2000) {
                            if (hi.getActivityType().equals("userTask") || hi.getActivityType().equals("endEvent")) {
                                historicActivityInstance3 = hi;
                            }
                            if (hi.getActivityType().equals("exclusiveGateway") || hi.getActivityType().equals("parallelGateway")) {
                                if (historicActivityInstance2 == null) {
                                    historicActivityInstance2 = hi;
                                } else {
                                    historicActivityInstance3 = hi;
                                }
                            }
                        }

                    }
                }
                break;
            }
        }
        //下一部 什么都没有  强制关闭任务
        if (historicActivityInstance2 == null && historicActivityInstance3 == null) {
            return "任务结束";
        }
        //下一步  没有任务 并行任务情况待 改造
        if (historicActivityInstance2 == null && historicActivityInstance3 != null) {

            historicActivityInstance2 = historicActivityInstance3;
        } else if (historicActivityInstance2 != null && historicActivityInstance3 != null) {
            //下一步有任务 有异步网关
            historicActivityInstance1 = historicActivityInstance2;
            historicActivityInstance2 = historicActivityInstance3;
        }
        //前一个节点
        FlowNode activityImpl1 = (FlowNode) bpmnModel.getMainProcess()
                .getFlowElement(historicActivityInstance1.getActivityId());

        //最后一个节点
        FlowNode activityImpl2 = (FlowNode) bpmnModel.getMainProcess()
                .getFlowElement(historicActivityInstance2.getActivityId());//


        List<SequenceFlow> pvmTransitions1 = activityImpl1.getOutgoingFlows(); // 取出节点的所有进去
        List<SequenceFlow> pvmTransitions2 = activityImpl2.getIncomingFlows(); // 取出节点的所有进去


        for (SequenceFlow pvmTransition : pvmTransitions1) {// 对所有的线进行遍历
            for (SequenceFlow pn : pvmTransitions2) {
                if (pvmTransition.getId().equals(pn.getId())) {
                    deal = pn.getName() == null ? "" : pn.getName();
                }
            }
        }
        return com.ruoyi.common.utils.StringUtils.isEmpty(deal) ? "通过" : deal;
    }

    /**
     * 设置流程节点的审批人或处理组
     *
     * @param code
     * @param map
     * @param task
     */
    private void setAgentExpression(String code, Map<String, Object> map, Task task, String applyUserId) {
        if (code.equals(WorkOrderInformation.incident.getCode())) {
            //创建人
//            variables.put("startUserId",CustomerBizInterceptor.currentUserThread.get().getUserId());
//            //关闭人
//            variables.put("closeId",CustomerBizInterceptor.currentUserThread.get().getUserId());
//            //一线处理角色
//            variables.put("front_line_role",CustomerFlowConstants.ASSIGN_ROLE);
//            //二线处理角色
//            variables.put("sssecondary_line_role",CustomerFlowConstants.ASSIGN_ROLE);
//            //业务处理 business_role
//            variables.put("business_role",CustomerFlowConstants.ASSIGN_ROLE);
//            //外联单位处理
//            variables.put("outreach_role",CustomerFlowConstants.ASSIGN_ROLE);
            ///dealId

            if (!ObjectUtils.isEmpty(map.get("agentExpression"))) {
                String agentExpression = map.get("agentExpression").toString();
                String name = map.get("name").toString();
                // 服务台分派
                if (agentExpression.contains("receptionId")) {
                    map.put("agentUserId", this.selectServiceByGroupName("IT服务台"));
                }
                // 一线处理环节
                if (agentExpression.contains("dealId")) {
                    map.put("agentUserId", CustomerBizInterceptor.currentUserThread.get().getUserId());
                }
                // 转二线解决 TODO 需要通过页面弹框选择
                if (agentExpression.contains("preSolutionId")) {
                    map.put("agentUserId", CustomerFlowConstants.ADMIN_USER_ID);
                }
                // 转业务部门 TODO 需要通过页面弹框选择
                if (agentExpression.contains("businessSolution")) {
                    map.put("agentUserId", CustomerFlowConstants.ADMIN_USER_ID);
                }
                // 转外联单位 TODO 需要通过页面弹框选择
                if (agentExpression.contains("outLineSolution")) {
                    map.put("agentUserId", CustomerFlowConstants.ADMIN_USER_ID);
                }
                // 一线解决  TODO 此处应该要查到处理环节处理人信息，赋值到一线解决复核环节
                if (agentExpression.contains("solutionId")) {
                    /*PubFlowLog pubFlowLog = selectPubFlowLogByCondition(task, WorkOrderInformation.incident.getCode(), "一线处理");
                    map.put("agentUserId", pubFlowLog != null ? pubFlowLog.getPerformerId() : CustomerFlowConstants.ADMIN_USER_ID);*/
                }
                // 退回补全 取创建人的id
                if (agentExpression.contains("startUserId")) {
                    map.put("agentUserId", applyUserId);
                }
                // 关闭环节  TODO 暂时取值创建人的id
                if (agentExpression.contains("closeId")) {
                    // 关闭环节取创建人id
                    map.put("agentUserId", applyUserId);
                }
            }
        }
        if (code.equals(WorkOrderInformation.TINYWEB_DB_RECOVER.getCode())) {
            switch (task.getName()) {
                case "已完成":
                    //查日志表 筛选出执行人
                    String instanceId = task.getProcessInstanceId();
                    Integer bizId = customerFormMapper.selectOneByProcessId(instanceId);
                    Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
                    String version = String.format("%s_%s_%s", code, currentTableInfo.get("version"), bizId);
                    PubFlowLog pubFlowLog = pubFlowLogMapper.selectPubFlowLogByCondition(WorkOrderInformation.TINYWEB_DB_RECOVER.getCode(), version, "执行新建单");
                    if (pubFlowLog != null) {
                        //退回最近执行人
                        map.put("agentUserId", pubFlowLog.getPerformerId());
                    }
                    break;
            }
        }
        
        if (!ObjectUtils.isEmpty(map.get("agentExpression"))) {
            String agentExpression = map.get("agentExpression").toString();
            if("${currentDealUser}".equals(agentExpression)) {
            	 map.put("currentDealUser", CustomerBizInterceptor.currentUserThread.get().getUserId());
            }else {
            	if(Boolean.parseBoolean(String.valueOf(map.get("isGroup")))){
            		map.put("currentDealUser", this.selectPersonByGroupId(agentExpression.substring(1,agentExpression.length())));
            	}else {
            		map.put("currentDealUser", map.get("agentUserId"));
            	}
            }
        }
    }

    /**
     * 判断用户是否为管理员
     */
    public Boolean checkAdmin(String userId) {
        Boolean isAdmin = false;
        List<String> allIds = getGroupsList(userId);
        if (allIds.contains(CustomerFlowConstants.PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.TINGJING_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.CHENDU_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.NANJIN_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.SUZHOU_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.NINGBO_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.BEIJIN_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.SHENZHENG_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.HANZHOU_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.ADMIN_USER_ID)
        ) {
            isAdmin = true;
        }
        return isAdmin;
    }

    public PubFlowLog selectPubFlowLogByCondition(Task task, String code, String taskName) {
        //查日志表 筛选出执行人
        String instanceId = task.getProcessInstanceId();
        Integer bizId = customerFormMapper.selectOneByProcessId(instanceId);
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        String version = String.format("%s_%s_%s", code, currentTableInfo.get("version"), bizId);
        PubFlowLog pubFlowLog = pubFlowLogMapper.selectPubFlowLogByCondition(code, version, taskName);
        return pubFlowLog;
    }

    /**
     * 查询服务台值班组人员
     *
     * @param groupName
     * @return
     */
    public String selectServiceByGroupName(String groupName) {
        Map<String, Object> map = eventForeignService.selectDutyPersonByGroupName(groupName);
        return (String) map.get("userId");
    }

    @Override
    public AjaxResult updateFromList(CustomerFormContent customerFormContent) {
        String code = String.valueOf(customerFormContent.getCode());
        Long id = customerFormContent.getId();
        dynamicTableName(code);
        int resultItem = 0;
        Long bizId = ObjectUtil.isEmpty(customerFormContent.getId()) ? null : Long.valueOf(String.valueOf(customerFormContent.getId()));
        DesignBizJsonData designBizJsonData = null;
        customerFormContent.getFields().remove("buttonInfo");
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        //根据表单版本ID获取当前表单当前版本的字段
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        //获取表名的中文名
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
        //过滤需要记录数值变更的字段
        List<Map<String, String>> recordDataChanged = formFieldInfos.stream().filter(map -> String.valueOf(map.get("record_data_changed")).equals("1")).collect(Collectors.toList());
        String selectKey = "";
        if (code.equals(WorkOrderInformation.changeTask.getCode())) {
            selectKey = "implStartDate,implEndDate," + RedundancyFieldEnum.extra1.name;
        }
        //获取原有字段值
//                Map<String, Object> oldValue = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(selectKey).eq("id", bizId)).get(0);
//                List<OperationDetails> operationDetails = new ArrayList<>();
//                //在新的key-value中筛选出需要记录变更的字段值
//                for (Map.Entry<String, Object> oldEntry : oldValue.entrySet()) {
//                    //不处理编号
//                    if (oldEntry.getKey().equals(RedundancyFieldEnum.extra1.name)) {
//                        continue;
//                    }
//                    if (customerFormContent.getFields().get(oldEntry.getKey()) != null &&
//                            !customerFormContent.getFields().get(oldEntry.getKey()).equals(oldEntry.getValue())) {
//                        OperationDetails op = OperationDetails.builder()
//                                .operationType(formName)
//                                .bizNo(oldValue.get(RedundancyFieldEnum.extra1.name).toString())
//                                .oldValue(String.valueOf(oldEntry.getValue()))
//                                .newValue(String.valueOf(customerFormContent.getFields().get(oldEntry.getKey())))
//                                .description(CustomerBizInterceptor.currentUserThread.get().getPname() + "将字段：" + recordDataChanged.stream().filter(a -> a.get("name").equals(oldEntry.getKey())).findFirst().get().get("description") + "的值从：" + oldEntry.getValue() + " 变更为：" + customerFormContent.getFields().get(oldEntry.getKey()))
//                                .build();
//                        operationDetails.add(op);
//                    }
//                }
//                //记录操作记录
//                operationDetailsService.saveBatch(operationDetails);
        //走修改 把通用字段中的修改时间及修改人添加至字段中
        customerFormContent.getFields().put("updated_time", DateUtils.getNowDate());
        customerFormContent.getFields().put("updated_by", CustomerBizInterceptor.currentUserThread.get().getUserId());
        resultItem = customerFormMapper.updateById(customerFormContent);
        designBizJsonData = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, id).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
        Map<String, Object> map = new HashMap<>();
        map.put("implStartDate", customerFormContent.getFields().get("implStartDate"));
        map.put("implEndDate", customerFormContent.getFields().get("implEndDate"));
        String jsonData = VueDataJsonUtil.analysisDataJson(designBizJsonData.getJsonData(), map);
        designBizJsonDataService.update(designBizJsonData, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_ID, bizId).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(bizId.intValue());
    }

    /**
     * 主页列表
     *
     * @param code
     * @param type
     * @param page
     * @param customerFormListDTO
     * @return
     */
    @Override
    public AjaxResult listChangeShifts(String code, String type, Page page, CustomerFormListDTO customerFormListDTO) {
        //   获取表单版本ID、表单版本号
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        if (ObjectUtil.isEmpty(currentTableInfo)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getDesc());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = sdf.format(FormUtil.getStartTime());
        String endTime = sdf.format(FormUtil.getEndTime());
//        CustomerFormSearchDTO customerFormSearchDTO = new CustomerFormSearchDTO();
//        customerFormSearchDTO.setSearchKey("planStartDate");
//        customerFormSearchDTO.setSearchValue(startTime);
//        customerFormSearchDTO.setSearchCondition("gt");
//        CustomerFormSearchDTO customerFormSearchDTOe = new CustomerFormSearchDTO();
//        customerFormSearchDTOe.setSearchKey("planStartDate");
//        customerFormSearchDTOe.setSearchValue(endTime);
//        customerFormSearchDTOe.setSearchCondition("lt");
//        List<CustomerFormSearchDTO> searchDTOList = new ArrayList<>();
//        searchDTOList.add(customerFormSearchDTO);
//        searchDTOList.add(customerFormSearchDTOe);
//        customerFormListDTO.setSearchDTOList(searchDTOList);
//        List searchList = customerFormListDTO.getSearchDTOList().stream().filter(p -> (StringUtils.isNotEmpty(p.getSearchCondition()))).collect(Collectors.toList());
//        //根据表单版本ID获取当前表单当前版本的字段
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
//        if (CollectionUtil.isEmpty(formFieldInfos)) {
//            throw new BusinessException(CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getDesc());
//        }
        //获取表名的中文名
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
        //构造查询对象的ID集合
        Map<String, Object> resultMap = new HashMap<>();
        List<String> ids = buildQueryIds(code, currentTableInfo, type);
        Page page1 = new Page();
        if (!CollectionUtils.isEmpty(ids)) {
            String version = String.valueOf(currentTableInfo.get("version"));
            dynamicTableName(code);
            QueryWrapper queryWrapper = Wrappers.<CustomerFormContent>query()
                    .select("*").in("ID", ids).gt("planStartDate", startTime).lt("planStartDate", endTime);
            page1 = customerFormMapper.selectMapsPage(page, queryWrapper);
            //构造返回结果
            buildResultList(code, type, page1);
            //查询当前表单的所有版本
        }
        resultMap.put("pageListInfo", page1);
        resultMap.put("fieldInfo", formFieldInfos);
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(resultMap);
    }

    @Override
    public AjaxResult listChangeSequence() {
        /**
         * 准备中：preApprovalPassed，waitImpl
         * 实施中：impling
         * 已完成：reviewed，closed
         */
        /**
         * 准备中：waitImpl
         * 实施中:impling
         * 已完成：closed
         * 延迟: delay
         * 0 完成 1 延迟 2插队
         */
        String code = WorkOrderInformation.changeTask.getCode();
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        List<Map<String, Object>> allList = new ArrayList<>();
        Set<String> roleIds=iSysRoleService.selectAllRoleKeys(CustomerBizInterceptor.currentUserThread.get().getUserId());
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String,Object>> topList=new ArrayList<>();
        Map<String, Object> reMap = new HashMap<>();
        if(roleIds.contains(CustomerFlowConstants.PBPERSON_ROLE)){
            //登录人为 排班发布人 查询所有
            List<String> allUser=iImplRecordService.selectAllEffectUser();
            if(!CollectionUtils.isEmpty(allUser)){
                for(String userName:allUser){
                    list = iImplRecordService.getChangeTaskListByStatusAndCurrentUserIdAll(userName);
                    if(!CollectionUtils.isEmpty(list)){
                        iImplRecordService.addReMap(topList,allList,list,reMap,currentTableInfo.get("version"),userName);
                    }
                }
            }
            reMap.put("role",CustomerFlowConstants.PBPERSON_ROLE);
            return AjaxResult.success(reMap);
        }else {
            String userName=CustomerBizInterceptor.currentUserThread.get().getPname();
            list = iImplRecordService.getChangeTaskListByStatusAndCurrentUserIdAll(userName.replace(" ",""));
            iImplRecordService.addReMap(topList,allList,list,reMap,currentTableInfo.get("version"),userName.replace(" ",""));
            if(roleIds.contains(CustomerFlowConstants.PB_EFFECTUSER)){
                reMap.put("role",CustomerFlowConstants.PB_EFFECTUSER);
            }else {
                reMap.put("role",CustomerFlowConstants.PB_TOURIST);
            }
            return AjaxResult.success(reMap);
        }
    }

    public AjaxResult changeSequence(String id, String userId, String upOrdown) {
        List<Map<String, Object>> list = iImplRecordService.getChangeTaskListByStatusAndCurrentUserIdAll(userId);
        int j = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("id").toString().equals(id)) {
                j = i;
                break;
            }
        }
        Map<String, Object> map1 = list.get(j);
        String sequene1 = map1.get("sequence")==null?map1.get("id").toString():map1.get("sequence").toString();
        String id2 = "";
        String sequene2 = "";
        if ("1".equals(upOrdown)) {
            //延迟
            Map<String, Object> map2 = list.get(j + 1);
            id2 = map2.get("id").toString();
            sequene2 = map2.get("sequence").toString();
            ImplRecord implRecord = new ImplRecord();
            implRecord.setId(Long.valueOf(sequene1));
            implRecord.setTaskStatus("延迟");
            iImplRecordService.updateImplRecord(implRecord);
        }
        if ("2".equals(upOrdown) && j > 0) {
            //插队
            Map<String, Object> map2 = list.get(j - 1);
            id2 = map2.get("id").toString();
            sequene2 = map2.get("sequence")==null?map2.get("id").toString():map2.get("sequence").toString();
        }
        if ("2".equals(upOrdown) && j == 0) {
            return AjaxResult.success("无需插入！");
        }
        dynamicTableName(WorkOrderInformation.changeTask.getCode());
        //当前任务序号重置
        CustomerFormContent customerFormContent = new CustomerFormContent();
        customerFormContent.setId(Long.valueOf(id));
        customerFormContent.setCode(WorkOrderInformation.changeTask.getCode());
        Map<String, Object> fileMap = new HashMap<>();
        fileMap.put("sequence", sequene2);
        fileMap.put("id", id);
        customerFormContent.setFields(fileMap);
        customerFormMapper.updateById(customerFormContent);
        //后续任务序号重置
        customerFormContent.setId(Long.valueOf(id2));
        fileMap.put("sequence", sequene1);
        fileMap.put("id", id2);
        customerFormContent.setFields(fileMap);
        customerFormMapper.updateById(customerFormContent);
        return AjaxResult.success("修改成功！");
    }


    @Override
    public AjaxResult reminder(String code, OperationDetails operationDetails) {
        String today = DateUtil.today();
        OperationDetails operationDetails1 = operationDetailsService.getOne(Wrappers.<OperationDetails>query()
                .eq(OperationDetails.COL_BIZ_NO, operationDetails.getBizNo())
                .eq(OperationDetails.COL_NEW_VALUE, "催单")
                .like(OperationDetails.COL_CREATED_TIME, today)
                .orderByDesc(OperationDetails.COL_ID)
                .last("limit 1"));
        if (ObjectUtil.isEmpty(operationDetails1)) {
            //String formName = GeneralQueryUtil.getFormName(GeneralQueryUtil.getCurrentTableInfo(code, null).get("id"));
            operationDetails.setOperationType(EventOperationType.GENERAL_INFORMATION.getInfo());
            operationDetails.setOldValue("分派环节");
            operationDetails.setNewValue("催单");
            operationDetails.setDescription(CustomerBizInterceptor.currentUserThread.get().getPname() + "在分派环节发起了" + "催单操作~");
            operationDetailsService.save(operationDetails);

            IncidentSubEvent incidentSubEvent = new IncidentSubEvent();
            incidentSubEvent.setUrgeFlag("1");// 催单标识
            incidentSubEvent.setUrgeTime(DateUtils.getTime());// 催单时间
            incidentSubEvent.setEventNo(operationDetails.getBizNo());
            incidentSubEventService.updateIncidentSubEventByNo(incidentSubEvent);

            return AjaxResult.success("催单成功！");
        } else {
            return AjaxResult.error(operationDetails.getBizNo() + "今天已被催单，无法再次催单！");
        }
    }

    @Override
    public AjaxResult getRelationLogList(String formNo, String requestType, Page page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("*");
        queryWrapper.eq("relation_no", formNo);
        if (StringUtils.isNotEmpty(requestType)) {
            String substring = formNo.substring(0, 2);
            if ("IM".equals(substring)) {
                queryWrapper.in("request_type", Arrays.asList("1", "2"));
            } else {
                queryWrapper.eq("request_type", requestType);
            }
        }
        queryWrapper.orderByDesc("id");
        Page page1 = relationLogMapper.selectMapsPage(page, queryWrapper);
        List<Map<String, Object>> relationList = page1.getRecords();
        for (Map<String, Object> relation : relationList) {
            relation.put("code", RelationRequestType.getTableName(relation.get("request_type").toString()));
            relation.put("request_type", RelationRequestType.getName(relation.get("request_type").toString()));
            relation.put("t_identify", "relation_detail");
        }
        // 问题单展示关联的变更单时也要把问题任务单创建的变更单也展示出来
        // 根据问题单编号查询该问题单创建的问题任务单编号
        if ("IMP".equals(formNo.substring(0, 3))) {
            // 根据问题单编号查询是否有关联的子任务
            dynamicTableName(WorkOrderInformation.problem_task.getCode());
            List<Map<String, Object>> problemTaskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name, STATUS, PROBLEM_NO).eq(PROBLEM_NO, formNo));
            MybatisPlusConfig.customerTableName.remove();
            if (!CollectionUtils.isEmpty(problemTaskList)) {
                List<String> noList = problemTaskList.stream().map(task -> task.get(RedundancyFieldEnum.extra1.name).toString()).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(noList)) {
                    // 查询问题任务单关联的变更单
                    List<Map<String, Object>> relationTasks = noList.stream().map(no -> {
                        QueryWrapper query = new QueryWrapper();
                        query.select("*");
                        query.eq("relation_no", no);
                        if (StringUtils.isNotEmpty(requestType)) {
                            query.eq("request_type", requestType);
                        }
                        query.orderByDesc("id");
                        Page pageTask = relationLogMapper.selectMapsPage(page, query);
                        List<Map<String, Object>> relations = pageTask.getRecords();
                        for (Map<String, Object> relation : relations) {
                            relation.put("code", RelationRequestType.getTableName(relation.get("request_type").toString()));
                            relation.put("request_type", RelationRequestType.getName(relation.get("request_type").toString()));
                            relation.put("t_identify", "relation_detail");
                        }
                        return relations;
                    }).collect(Collectors.toList()).stream().flatMap(Collection::stream).collect(Collectors.toList());
                    relationList.addAll(relationTasks);
                }
            }
        }
        // 添加表头展示字段
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", page1.getTotal());
        List<Map<String, Object>> colLists = new ArrayList<>();

//        Map<String, Object> colMap1 = new HashMap<>();
        Map<String, Object> colMap2 = new HashMap<>();
        Map<String, Object> colMap3 = new HashMap<>();
        Map<String, Object> colMap4 = new HashMap<>();
        Map<String, Object> colMap5 = new HashMap<>();
        Map<String, Object> colMap6 = new HashMap<>();
        Map<String, Object> colMap7 = new HashMap<>();
//        colMap1.put("label", "关系类型");
//        colMap1.put("val", "relation_type");
        colMap2.put("label", "请求类型");
        colMap2.put("val", "request_type");
        colMap3.put("label", "请求编号");
        colMap3.put("val", "request_no");
        colMap4.put("label", "请求摘要");
        colMap4.put("val", "request_summary");
        colMap5.put("label", "状态");
        colMap5.put("val", "status");
        colMap6.put("label", "开始日期");
        colMap6.put("val", "start_date");
        colMap7.put("label", "结束日期");
        colMap7.put("val", "end_date");
        colLists.add(colMap2);
        colLists.add(colMap3);
        colLists.add(colMap4);
        colLists.add(colMap5);
        colLists.add(colMap6);
        colLists.add(colMap7);
        resultMap.put("col", colLists);
        resultMap.put("pageNum", page.getCurrent());
        resultMap.put("pageSize", page.getSize());
        resultMap.put("list", relationList);
        return AjaxResult.success(resultMap);
    }

    @Override
    public AjaxResult queryFormDetailByBizNo(String bizNo, String code) {
        dynamicTableName(code);
        List<Map<String, Object>> resultList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(INSTANCE_ID, ID).eq("extra1", bizNo));
        if (CollectionUtils.isEmpty(resultList)) {
            return AjaxResult.success("查询数据为空!");
        }
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(resultList.get(0));
    }

    @Override
    public AjaxResult getProblemTaskFormInfo(String code, String problemNo) {
        Map<String, Object> map = new HashMap<>();
        // 获取design_form_version表中的主键ID
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        //根据表单版本ID获取表单json数据
        Map<String, Object> formJsonInfo = customerFormMapper.getFormJsonInfo(currentTableInfo.get("id"));
        //获取当前表单所有已配置的步骤条
        List<FormStepStatus> formStepStatusList = formStepStatusService.list(Wrappers.<FormStepStatus>query().select(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")).groupBy(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).orderByAsc(FormStepStatus.COL_SORT));

        map.put("formStepStatusList", formStepStatusList);
        map.put("formJsonInfo", formJsonInfo);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getNoById(String id, String code) {
        dynamicTableName(code);
        List<Map<String, Object>> bizNo = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("id", id));
        if (CollectionUtils.isEmpty(bizNo)) {
            throw new BusinessException("查询表数据不存在!");
        }
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(bizNo.get(0).get(RedundancyFieldEnum.extra1.name));
    }

    // 删除总经理室人员
    @Override
    public AjaxResult deleteGeneralManager(String instanceId, String managerIds) {
        List<String> managerList = Arrays.asList(managerIds.split(","));
        List<Task> taskList = taskService.createTaskQuery()
                .processInstanceId(instanceId)
                .list();
        dynamicTableName(WorkOrderInformation.problem.getCode());
        List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(ORG_ID, RedundancyFieldEnum.extra1.name, ID).eq(INSTANCE_ID, instanceId));
        if (CollectionUtils.isEmpty(problemList)) {
            throw new BusinessException("问题单表数据不存在!");
        }
        if (ObjectUtil.equal(managerList.size(), taskList.size())) {
            Map<String, Object> variables = new HashMap<>();
            // 需要区分总分行的管理员
            OgGroup ogGroup = new OgGroup();
            // 设置当前登录人所在机构id
            ogGroup.setOrgId(problemList.get(0).get(ORG_ID).toString());
            ogGroup.setMemo("problem_admin_not_update");// 问题管理员組
            List<OgGroup> ogGroups = iSysWorkService.selectOgGroupList(ogGroup);
            if (CollectionUtils.isEmpty(ogGroups)) {
                throw new BusinessException("该组不存在!");
            }
            String groupid = ogGroups.get(0).getGroupId();
            List<OgGroupPerson> ogGroupPeopleList = ogGroupPersonService.selectOgGroupPersonById(groupid);
            if (CollectionUtils.isEmpty(ogGroupPeopleList)) {
                throw new BusinessException("管理员组没有配置人员!");
            }

            variables.put("manager_group", managerIds);
            if (managerList.size() == 1) {
                Task task = taskList.get(0);
                //  完成当前任务
                taskService.complete(task.getId(), variables);
            } else {
                // 取消后还需要继续到下一节点
                for (int i = 0; i < managerList.size(); i++) {
                    if (i != (managerList.size() - 1)) {
                        for (Task task : taskList) {
                            if (task.getAssignee().equals(managerList.get(i))) {
                                ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
                                ManagementService managementService = processEngine.getManagementService();
                                managementService.executeCommand(new DeleteMultiInstanceCmd(task.getExecutionId(), true, task.getId()));
                            }
                        }
                    } else {
                        taskService.complete(taskList.get(managerList.size() - 1).getId(), variables);
                    }
                }
            }
            // 设置当前处理人为管理员
            List<String> nameList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPname()).collect(Collectors.toList());
            List<String> idList = ogGroupPeopleList.stream().map(ogGroupPerson -> ogGroupPerson.getPid()).collect(Collectors.toList());
            String nameStr = String.join(",", nameList);
            String idStr = String.join(",", idList);
            Map<String, Object> curMap = new HashMap<>();
            curMap.put(nameStr, idStr);
            curMap.put("default", idStr);
            Map<String, Object> map = new HashMap<>();
            map.put(CURRENT_HANDLER_ID, curMap);
            DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                    .eq(DesignBizJsonData.COL_BIZ_ID, problemList.get(0).get(ID))
                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, WorkOrderInformation.problem.getCode())));
            String json = VueDataJsonUtil.analysisDataJsonSelect(currentNodeFormInfo.getJsonData(), map);
            Map<String, Object> map1 = new HashMap<>();
            map1.put(STAGE, stageMap.get(ADMIN_CONFIRMING.getInfo()));
            map1.put(PROBLEM_UPDATE_TIME, DateUtils.getDate());
            String jsonData = VueDataJsonUtil.analysisDataJson(json, map1);
            designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, WorkOrderInformation.problem.getCode())).eq(DesignBizJsonData.COL_BIZ_ID, problemList.get(0).get(ID).toString()).set("json_data", jsonData));
//             更新表的状态
            customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set("status", ADMIN_CONFIRMING.getInfo()).set("stage", stageMap.get(ADMIN_CONFIRMING.getInfo())).set(CURRENT_HANDLER_ID, idStr).set(PROBLEM_UPDATE_TIME, DateUtils.getDate()).set(UPDATED_TIME, DateUtils.getTime()));
            // 更新关联日志表
            relationLogMapper.update(null, Wrappers.<RelationLog>update().eq("request_no", problemList.get(0).get(RedundancyFieldEnum.extra1.name).toString()).set("status", ADMIN_CONFIRMING.getInfo()).set("end_date", null));
        } else {
            for (String id : managerList) {
                List<Task> tasks = taskList.stream().filter(task -> com.ruoyi.common.utils.StringUtils.equals(task.getAssignee(), id)).collect(Collectors.toList());
                Task task = tasks.get(0);
                ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
                ManagementService managementService = processEngine.getManagementService();
                managementService.executeCommand(new DeleteMultiInstanceCmd(task.getExecutionId(), true, task.getId()));
            }
            List<String> idList = taskList.stream().filter(task -> !managerList.contains(task.getAssignee())).map(task -> task.getAssignee()).collect(Collectors.toList());
            List<String> nameList = idList.stream().map(id -> ogPersonService.selectOgPersonById(id).getpName()).collect(Collectors.toList());
            String idStr = String.join(",", idList);
            String nameStr = String.join(",", nameList);
            // 设置当前处理人为管理员
            DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                    .eq(DesignBizJsonData.COL_BIZ_ID, problemList.get(0).get(ID))
                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, WorkOrderInformation.problem.getCode())));
            Map<String, Object> curMap = new HashMap<>();
            curMap.put(nameStr, idStr);
            curMap.put("default", idStr);
            Map<String, Object> map = new HashMap<>();
            map.put(CURRENT_HANDLER_ID, curMap);
            String jsonData = VueDataJsonUtil.analysisDataJsonSelect(currentNodeFormInfo.getJsonData(), map);
            designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, WorkOrderInformation.problem.getCode())).eq(DesignBizJsonData.COL_BIZ_ID, problemList.get(0).get(ID).toString()).set("json_data", jsonData));
//             更新表的状态
            customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq("instance_id", instanceId).set(CURRENT_HANDLER_ID, idStr).set(PROBLEM_UPDATE_TIME, DateUtils.getDate()).set(UPDATED_TIME, DateUtils.getTime()));
        }
        // 查询总经理室人员
        StringBuilder stringBuilder = new StringBuilder();
        for (String id : managerList) {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(id);
            stringBuilder.append(ogPerson.getpName()).append(",");
        }
        String managerNames = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        // 添加工作详情记录
        String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        OgPerson op = iOgPersonService.selectOgPersonById(userId);
        OperationDetails operationDetails = OperationDetails.builder()
                .operationType(WorkOrderInformation.problem.getDesc())
                .bizNo(problemList.get(0).get(RedundancyFieldEnum.extra1.name).toString())
                .description(String.format("%s取消了总经理:%s的审核", op.getpName(), managerNames))
                .build();
        operationDetailsService.save(operationDetails);
        MybatisPlusConfig.customerTableName.remove();

        return AjaxResult.success("取消总经理室人员审核成功!");
    }

    @Override
    public AjaxResult getRelationLogList(String formNo, String requestType) {
        QueryWrapper<RelationLog> queryWrapper = new QueryWrapper<RelationLog>()
                .eq("relation_no", formNo);
        if (StringUtils.isNotEmpty(requestType)) {
            queryWrapper.eq("request_type", requestType);
        }

        List<RelationLog> relationList = relationLogMapper.selectList(queryWrapper);
        relationList.forEach(relationLog -> relationLog.setRequestType(RelationRequestType.getName(relationLog.getRequestNo())));
        return AjaxResult.success(relationList);
    }

    @Override
    public AjaxResult saveRelationLogList(String formId, String formCode, String relationFormId, String relationFormCode) {
        List<RelationLog> relationLogList = new ArrayList<>();
        if (formCode.equals(WorkOrderInformation.problem.getCode())) {
            dynamicTableName(formCode);
            List<Map<String, Object>> problemFieldMapList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                    .select(STATUS, CURRENT_HANDLER_ID, RedundancyFieldEnum.extra1.name, PROBLEM_TITLE, CREATED_TIME, CLOSE_TIME, UPDATED_TIME)
                    .eq("id", formId));
            if (CollectionUtils.isEmpty(problemFieldMapList)) {
                throw new BusinessException(String.format("该业务表%s数据%s不存在!", formCode, formId));
            }
            Map<String, Object> problemFieldMap = problemFieldMapList.get(0);
            Map<String, Object> relationFieldMap = new HashMap<>();
            if (relationFormCode.equals(WorkOrderInformation.change.getCode())) {
                dynamicTableName(relationFormCode);
                List<Map<String, Object>> maps = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                        .select(STATUS, "title", RedundancyFieldEnum.extra1.name, ChangeFieldEnum.APPROVAL.getName(), "created_time", "updated_time")
                        .eq("id", relationFormId));
                if (CollectionUtils.isEmpty(maps)) {
                    throw new BusinessException(String.format("该业务表%s数据%s不存在!", relationFormCode, relationFormId));
                }
                relationFieldMap = maps.get(0);
                // 问题单关系页签展示关联的变更单记录
                RelationLog relationChangeLog = new RelationLog();
                relationChangeLog.setStatus(relationFieldMap.get(STATUS).toString());
                relationChangeLog.setStartDate(DateUtils.formatDate((Date) relationFieldMap.get("created_time"), YYYY_MM_DD_HH_MM_SS));
                relationChangeLog.setCurrentHandlerId(ObjectUtil.isNotEmpty(relationFieldMap.get(ChangeFieldEnum.APPROVAL.getName())) ? relationFieldMap.get(ChangeFieldEnum.APPROVAL.getName()).toString() : null);
                relationChangeLog.setEndDate(Arrays.asList(ChangeStatusEnum.cancel.getName(), ChangeStatusEnum.closed.getName()).contains(relationFieldMap.get(STATUS).toString()) ? DateUtils.formatDate((Date) relationFieldMap.get("updated_time"), YYYY_MM_DD_HH_MM_SS) : null);
                relationChangeLog.setRequestNo(relationFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                relationChangeLog.setRelationNo(problemFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                relationChangeLog.setRelationType("相关");
                relationChangeLog.setRequestSummary(relationFieldMap.get(RedundancyFieldEnum.extra1.name).toString() + ":" + relationFieldMap.get("title").toString());
                relationChangeLog.setRequestType(RelationRequestType.CHANGE.getCode());
                relationLogList.add(relationChangeLog);
            }
            if (relationFormCode.equals(WorkOrderInformation.incident.getCode())) {
                dynamicTableName(relationFormCode);
                List<Map<String, Object>> maps = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                        .select(STATUS, EventFieldConstants.EVENT_TITLE, RedundancyFieldEnum.extra1.name, "extra5", "created_time", "updated_time")
                        .eq("id", relationFormId));
                if (CollectionUtils.isEmpty(maps)) {
                    throw new BusinessException(String.format("该业务表%s数据%s不存在!", relationFormCode, relationFormId));
                }
                relationFieldMap = maps.get(0);
                // 问题单关系页签展示关联的事件单记录
                RelationLog relationIncidentLog = new RelationLog();
                relationIncidentLog.setStatus(relationFieldMap.get(STATUS).toString());
                relationIncidentLog.setStartDate(DateUtils.formatDate((Date) relationFieldMap.get("created_time"), YYYY_MM_DD_HH_MM_SS));
                relationIncidentLog.setCurrentHandlerId(ObjectUtil.isNotEmpty(relationFieldMap.get("extra5")) ? String.valueOf(relationFieldMap.get("extra5")) : null);
                relationIncidentLog.setEndDate(Arrays.asList(EventStatusEnum.CLOSED.getInfo()).contains(relationFieldMap.get(STATUS).toString()) ? DateUtils.formatDate((Date) relationFieldMap.get("updated_time"), YYYY_MM_DD_HH_MM_SS) : null);
                relationIncidentLog.setRequestNo(relationFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                relationIncidentLog.setRelationNo(problemFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
                relationIncidentLog.setRelationType("相关");
                relationIncidentLog.setRequestSummary(relationFieldMap.get(RedundancyFieldEnum.extra1.name).toString() + ":" + relationFieldMap.get(EventFieldConstants.EVENT_TITLE).toString());
                relationIncidentLog.setRequestType(RelationRequestType.EVENT.getCode());
                relationLogList.add(relationIncidentLog);
            }
            // 其它单子关系页签展示关联的问题单记录
            RelationLog relationProblemLog = new RelationLog();
            relationProblemLog.setStatus(problemFieldMap.get(STATUS).toString());
            relationProblemLog.setStartDate(DateUtils.formatDate((Date) problemFieldMap.get(CREATED_TIME), YYYY_MM_DD_HH_MM_SS));
            relationProblemLog.setCurrentHandlerId(problemFieldMap.get(CURRENT_HANDLER_ID).toString());
            relationProblemLog.setEndDate(Arrays.asList(ProblemStatus.CANCEL.getInfo(), CLOSE.getInfo()).contains(problemFieldMap.get(STATUS).toString()) ? DateUtils.formatDate((Date) problemFieldMap.get("updated_time"), YYYY_MM_DD_HH_MM_SS) : null);
            relationProblemLog.setRequestNo(problemFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
            relationProblemLog.setRelationNo(relationFieldMap.get(RedundancyFieldEnum.extra1.name).toString());
            relationProblemLog.setRelationType("相关");
            relationProblemLog.setRequestSummary(problemFieldMap.get(RedundancyFieldEnum.extra1.name).toString() + ":" + problemFieldMap.get(PROBLEM_TITLE).toString());
            relationProblemLog.setRequestType(RelationRequestType.PROBLEM.getCode());
            relationLogList.add(relationProblemLog);
        }
        Collections.reverse(relationLogList);
        relationLogList.forEach(relationLog -> {
            try {
                relationLogMapper.insertRelationLog(relationLog);
            } catch (DuplicateKeyException e) {
                throw new BusinessException(String.format("该编号：%s已关联过编号：%s的单子， 请重新选择关联！", relationLog.getRequestNo(), relationLog.getRelationNo()));
            }
        });
        // 移除表
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success("关联成功！");
    }

    @Override
    public AjaxResult suspend(String code, String instanceId, String bizNo) {
        OperationDetails suspendDetails = operationDetailsService.getOne(Wrappers.<OperationDetails>query()
                .eq(OperationDetails.COL_BIZ_NO, bizNo)
                .eq(OperationDetails.COL_NEW_VALUE, "挂起"));
        if (ObjectUtil.isEmpty(suspendDetails)) {
            String taskName = taskService.createTaskQuery().processInstanceId(instanceId).list().get(0).getName();
            OperationDetails operationDetails = OperationDetails.builder().operationType(GeneralQueryUtil.getFormName(GeneralQueryUtil.getCurrentTableInfo(code, null).get("id")))
                    .oldValue("处理环节")
                    .bizNo(bizNo)
                    .newValue("挂起").description(CustomerBizInterceptor.currentUserThread.get().getPname() + "在" + taskName + "节点进行了挂起操作").build();
            if (code.equals(WorkOrderInformation.incident.getCode())) {
                operationDetails.setOperationType(EventOperationType.GENERAL_INFORMATION.getInfo());
            }
            operationDetailsService.save(operationDetails);

            // 记录事件单副表相关字段
            IncidentSubEvent incidentSubEvent = new IncidentSubEvent();
            incidentSubEvent.setEventNo(bizNo);
            incidentSubEvent.setSuspendFlag("1");// 挂起标识
            incidentSubEvent.setSuspendTime(DateUtils.getTime());
            incidentSubEventService.updateIncidentSubEventByNo(incidentSubEvent);

            return AjaxResult.success("挂起成功");
        } else {
            return AjaxResult.error(bizNo + "已被" + suspendDetails.getCreatedName() + "挂起，无法再次挂起~");
        }

    }

    /**
     * 从pub_flow_log中查询节点处理人
     *
     * @param taskName
     * @param code
     * @param bizNo
     * @return
     */
    public String selectTaskNameDealId(String taskName, String code, String bizNo) {
        String dealId = "";
        PubFlowLog pubFlowLog = new PubFlowLog();
        pubFlowLog.setLogType(code);
        pubFlowLog.setBizId(bizNo);
        pubFlowLog.setTaskName(taskName);
        List<PubFlowLog> pubFlowLogs = iPubFlowLogService.selectPubFlowLogList(pubFlowLog);
        if (!CollectionUtils.isEmpty(pubFlowLogs)) {
            dealId = pubFlowLogs.get(0).getPerformerId();
        }
        return dealId;
    }
    
    @Autowired
    private ISysWorkService workService;
    public List selectPersonByGroupId(String groupId) {
        OgGroupPerson person = new OgGroupPerson();
        person.setGroupId(groupId);
        List<OgGroupPerson> list = workService.selectOgGroupPersonList(person);
        List<String> l = new ArrayList();
        if(!list.isEmpty()) {
            for (OgGroupPerson ogGroupPerson : list) {
                l.add(ogGroupPerson.getPid());
            }
        }
        return l;
    }
}


