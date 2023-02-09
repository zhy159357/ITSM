package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.DesignBizIncident;
import com.ruoyi.form.domain.DesignBizJsonData;
import com.ruoyi.form.domain.OperationDetails;
import com.ruoyi.form.entity.CompleteParamDto;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.EventOperationType;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignBizIncidentMapper;
import com.ruoyi.form.service.*;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.IDCodeConvertChineseUtil;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IncidentServiceImpl implements CustomerStrategyService, IncidentService {

    private final String tableName = "design_biz_incident";

    @Autowired
    private CustomerFormMapper customerFormMapper;

    @Autowired
    IOgGroupPersonService ogGroupPersonService;

    @Autowired
    private TaskService taskService;

    @Autowired
    DesignBizJsonDataService designBizJsonDataService;

    @Autowired
    private OperationDetailsService operationDetailsService;

    @Resource
    DesignBizIncidentMapper designBizIncidentMapper;

    @Resource
    IOgPersonService ogPersonService;


    @Override
    public AjaxResult customerDetailsPage(String code, String processId, Integer id, String type, String version) {
        return null;
    }

    @Override
    public AjaxResult customerDetailsPage(List<Map<String, Object>> formJsonData, List<Map<String, String>> formJsonAppendInfo, String code, Integer id, String processId) {
        if (StringUtils.isEmpty(processId)) {
            return AjaxResult.error("事件单流程不存在，不能进行抢单！");
        }
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        if (null == task) {
            return AjaxResult.error("事件单流程不存在，不能进行抢单！");
        }
        OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
        Map<String, Object> resultMap = new HashMap<>();
        String groupId = customerFormMapper.selectIncidentGroupById(Long.valueOf(id));
        if (StringUtils.isNotEmpty(groupId)) {
            List<OgGroupPerson> personList = ogGroupPersonService.selectOgGroupPersonById(groupId);
            if (!CollectionUtils.isEmpty(personList)) {
                List<String> collect = personList.stream().map(person -> {
                    return person.getPid();
                }).collect(Collectors.toList());
                if (collect.contains(ogUser.getpId())
                        && !ogUser.getpId().equals(task.getAssignee())
                        && "一线处理".equals(task.getName())) {
                    Map<String, Object> button = new HashMap<>();
                    button.put("buttonName", "抢单");
                    button.put("buttonUrlPath", "/customerForm/rob/task");
                    List<Map<String, Object>> buttonInfoList = new ArrayList<>();
                    buttonInfoList.add(button);
                    resultMap.put("actionButtonList", buttonInfoList);
                }
            }
        }
        resultMap.put("jsonData", formJsonData);
        resultMap.put("appenJsondata", formJsonAppendInfo);
        return AjaxResult.success(resultMap);
    }

    @Override
    public AjaxResult processComplete(CompleteParamDto completeParamDto) {
        return null;
    }


    @Override
    public AjaxResult robTask(String imCode) {
        Map<String, Object> map = customerFormMapper.selectIncidentMapByCode(tableName, imCode);
        String instanceId = (String) map.get("instanceId");
        Long bizId = (Long) map.get("id");
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        if(task == null) {
            return AjaxResult.error("根据事件单号:" + imCode + "，查询流程任务不存在或已处理！");
        }
        String assignee = CustomerBizInterceptor.currentUserThread.get().getUserId();
        taskService.setAssignee(task.getId(), assignee);

        DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                .eq(DesignBizJsonData.COL_BIZ_TABLE, tableName));
        Map<String, Object> json = new HashMap<>();
        json.put("assigned_person", assignee);
        String jsonData = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), json);
        designBizJsonDataService.update(null, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_TABLE, tableName).eq(DesignBizJsonData.COL_BIZ_ID, bizId).set("json_data", jsonData));
        // 更新事件单当前处理人
        DesignBizIncident designBizIncident = new DesignBizIncident();
        designBizIncident.setId(bizId);
        designBizIncident.setExtra5(assignee);
        designBizIncident.setAssignedPerson(assignee);
        designBizIncidentMapper.updateById(designBizIncident);
        // 保存工作详情记录
        this.saveOperationDetails(imCode);
        return AjaxResult.success();
    }

    private void saveOperationDetails(String bizNo) {
        OperationDetails operationDetails = OperationDetails.builder().operationType(EventOperationType.ROB_TASK.getInfo())
                .bizNo(bizNo).description(CustomerBizInterceptor.currentUserThread.get().getPname() + "进行了抢单操作").build();
        operationDetailsService.save(operationDetails);
    }

    /**
     * 事件控制台
     *
     * @return 返回结果
     */
    @Override
    public AjaxResult console(Page page) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.incident.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.extractedSelectSql("事件", formFieldInfos);
        CustomerStrategyUtil.verificationNull(formFieldInfos, CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        Map<String, String> todoPersonal = new HashMap<>();
        todoPersonal.put("name", "extra5");
        todoPersonal.put("description", "当前处理人");
        todoPersonal.put("display", "1");
        todoPersonal.put("exportable", "1");
        formFieldInfos.add(2, todoPersonal);
        // 组内除了自己以外其他人的处理任务标识(组内其他人任务需要抢单)
        boolean groupOthersFlag = true;
        String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        //工作组
        //List<OgGroup> ogGroups = sysWorkMapper.selectGroupByUserId(userId);
        IPage<Map<String, Object>> mapIPage = designBizIncidentMapper.todoGroupConsole(page, userId);

        for (Map<String, Object> a : mapIPage.getRecords()) {//如果当前筛选出的数据为当前登录人的代办则查找任务ID
            String instance_id = String.valueOf(a.get("instance_id"));
            //通过实例ID查询当前任务ID
            List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                    .processInstanceId(instance_id)
                    .list();    // 例如请假会签，会同时拥有多个任务

            if (CollectionUtils.isEmpty(taskList)) {
                taskList = taskService.createTaskQuery().processInstanceId(instance_id).list();
            }
            if (CollectionUtils.isEmpty(taskList)) {
                log.error("incident query is error! activity-task-not-exits! biz dada instanceId is instance_id");
                a.put("taskId", "");
                a.put("popType", "3");
                continue;
            }
            a.put("taskId", taskList.get(0).getId());
            a.put("popType", "2");
            if(userId.equals(a.get("extra5"))) {
                groupOthersFlag = false;
            }
            // 事件单其组内的其他任务 设置popType=0标识
            if(groupOthersFlag) {
                a.put("popType", "0");
            }
        }
        IDCodeConvertChineseUtil.convertIdToName(WorkOrderInformation.incident.getCode(), (List<Map<String, Object>>) mapIPage.getRecords());
        IDCodeConvertChineseUtil.convertEnumToName(WorkOrderInformation.incident.getCode(), (List<Map<String, Object>>) mapIPage.getRecords());
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("fieldInfo",formFieldInfos);
        resultMap.put("pageListInfo",mapIPage);
        return AjaxResult.success(resultMap);
    }

    @Override
    public AjaxResult summaryConsoleDesk(Page page, String searchType) {
        return null;
    }
}
