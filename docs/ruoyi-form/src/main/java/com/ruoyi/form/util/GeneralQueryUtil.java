package com.ruoyi.form.util;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.form.domain.ButtonConfigInfo;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.DesignBizJsonData;
import com.ruoyi.form.domain.FormStepStatus;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.service.ButtonConfigInfoService;
import com.ruoyi.form.service.DesignBizJsonDataService;
import com.ruoyi.form.service.FormStepStatusService;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysWorkService;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName GeneralQueryUtil
 * @Description 通用查询
 * @Author JiaQi Zhang
 * @Date 2022/10/2 8:45
 */
@Component
@RequiredArgsConstructor
public class GeneralQueryUtil {
    private final ISysWorkService sysWorkService;
    private final ISysRoleService sysRoleService;
    private final IOgPersonService ogPersonService;
    private final FormStepStatusService formStepStatusService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final ButtonConfigInfoService  buttonConfigInfoService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final CustomerFormMapper customerFormMapper;
    private final DesignBizJsonDataService designBizJsonDataService;
    private static String bizTablePrefix="design_biz";

    public static GeneralQueryUtil generalQueryUtil;



    @PostConstruct
    public void init(){
        generalQueryUtil=this;
    }

    /**
     * 获取当前登录人的所有有关数据ID  如：所在工作组ID  所在角色ID  所在机构ID
     * @param pid  当前登录人ID
     * @return
     */
    public static List<String> getAllBaseDataByPid(String pid){
        List<String> reList = new ArrayList<>();
        // 获取工作组
        List<OgGroup> ogGroups = generalQueryUtil.sysWorkService.selectGroupByUserId(pid);
        // 查询角色
        List<OgRole> ogRoles = generalQueryUtil.sysRoleService.selectRolesByUserId(pid);
        // 机构
        OgPerson ogPerson = generalQueryUtil.ogPersonService.selectOgPersonEvoById(pid);
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

    /**
     * 构造步骤条
     * @param currentTableInfo 当前生效表单的数据
     * @param status 当前数据状态
     * @return
     */
    public static Map<String,Object> buildFormStep(Map<String, Long> currentTableInfo,String status){

        List<FormStepStatus> formStepStatusList = generalQueryUtil.formStepStatusService.list(Wrappers.<FormStepStatus>query().select(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")).groupBy(FormStepStatus.COL_STEP_NAME, FormStepStatus.COL_SORT).orderByAsc(FormStepStatus.COL_SORT));

        FormStepStatus formStepStatus = generalQueryUtil.formStepStatusService.getOne(Wrappers.<FormStepStatus>query().eq(FormStepStatus.COL_BIZ_STATUS_NAME, status).eq(FormStepStatus.COL_FORM_VERSION_ID, currentTableInfo.get("id")));
        Map<String, Object> formStepStatusMap = new HashMap<>();
        formStepStatusMap.put("formStepStatusList", formStepStatusList);
        formStepStatusMap.put("currenFormStep", formStepStatus.getStepName());
        return formStepStatusMap;
    }

    public static List<ButtonConfigInfo> buildButtonInfo(String processId){

        Task currentTaskNode = generalQueryUtil.taskService.createTaskQuery().processInstanceId(processId).list().get(0);
        //根据activity的ID查询按钮配置表信息
        return generalQueryUtil.buttonConfigInfoService.list(Wrappers.<ButtonConfigInfo>query().eq(ButtonConfigInfo.COL_BIZ_PROCESS_ACTIVITY_NODE_ID, currentTaskNode.getTaskDefinitionKey()));
    }


    public static ButtonConfigInfo buildButtonInfo(String buttonId,String code){
        return generalQueryUtil.buttonConfigInfoService.getById(buttonId);
    }

    /**
     * 构造审批对象参数
     * @param buttonConfigId
     * @param approveObject
     */
    public static Map<String,Object> buildCompleteVariables(String buttonConfigId, String approveObject,List<Map<String, Object>> oldData) {
        Map<String,Object> variables=new HashMap<>();
        ButtonConfigInfo buttonConfigInfo = generalQueryUtil.buttonConfigInfoService.getById(buttonConfigId);
        if (buttonConfigInfo.getConditionEnable()==1){
            String[] split = buttonConfigInfo.getConditionExpression().split("==");
            variables.put(split[0],split[1]);
        }
        if (buttonConfigInfo.getDynamicApprovePersonal()==1){
            if (!StringUtils.isEmpty(approveObject)){
                variables.put(buttonConfigInfo.getApprovePersonalExpression(),approveObject);
            }
        }else {
            variables.put(buttonConfigInfo.getApprovePersonalExpression(),approveObject);
        }
        return variables;
    }

    /**
     * 获取表单JSON
     * @param code
     * @param processId
     * @param id
     * @return
     */
    public static List<Map<String,Object>> getFormJsonData(String code,String processId,Integer id){
        List<String> formKeys = new ArrayList<>();
        List<Map<String,Object>> jsonList=new ArrayList<>();
        formKeys.add(code);
        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(processId)) {
            List<HistoricTaskInstance> historicTaskInstanceList = generalQueryUtil.historyService.createHistoricTaskInstanceQuery().processInstanceId(processId).finished().orderByHistoricTaskInstanceStartTime().asc().list();
            //过滤任务节点没有配置表单key的元素
            historicTaskInstanceList.stream().filter(a -> {
                return com.ruoyi.common.utils.StringUtils.isNotEmpty(a.getFormKey());
            }).forEach(a -> formKeys.add(a.getFormKey()));

            List<String> distinctFormKey = formKeys.stream().distinct().collect(Collectors.toList());
            distinctFormKey.stream().forEach(a -> {
                dynamicTableName(a);
                Integer formBusinessKey = generalQueryUtil.customerFormMapper.selectOneByProcessId(processId);
                DesignBizJsonData designBizJsonData1 = generalQueryUtil.designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, formBusinessKey).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
                Map<String,Object> jsonMap=new HashMap<>();
                jsonMap.put("formJson",designBizJsonData1.getJsonData());
                jsonMap.put("isCurrentTaskNode",true);
                jsonList.add(jsonMap);
                MybatisPlusConfig.customerTableName.remove();
            });
        } else {
            dynamicTableName(code);
            DesignBizJsonData designBizJsonData1 = generalQueryUtil.designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, id).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
            Map<String,Object> jsonMap=new HashMap<>();
            jsonMap.put("formJson",designBizJsonData1.getJsonData());
            jsonMap.put("isCurrentTaskNode",true);
            jsonList.add(jsonMap);
            MybatisPlusConfig.customerTableName.remove();
        }
        return jsonList;
    }


    /**
     * 获取追加的表单JSON信息
     * @param code
     * @param id
     * @return
     */
    public static  List<Map<String,String>> getFormJsonAppendInfo(String code,Integer id){
        dynamicTableName(code);
        List<Map<String, Object>> statusList = generalQueryUtil.customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("status").eq("id", id));
        Map<String, String> formStatus = new HashMap<>();
        formStatus.put("lable", "工单状态");
        formStatus.put("index", "lastIndex");
        formStatus.put("value", Optional.ofNullable(statusList).isPresent() ? statusList.get(0).get("status").toString() : "");

        Map<String, Long> currentTableInfo = generalQueryUtil.customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), null);
        String formName = generalQueryUtil.customerFormMapper.getFormName(currentTableInfo.get("id"));
        //获取业务表编号
        List<Map<String, Object>> bizNos = generalQueryUtil.customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(RedundancyFieldEnum.extra1.name).eq("id", id));
        Map<String, String> bizNoMap = new HashMap<>();
        bizNoMap.put("lable", formName + "编号");
        bizNoMap.put("index", "firstIndex");
        bizNoMap.put("value", Optional.ofNullable(bizNos).isPresent() ? bizNos.get(0).get(RedundancyFieldEnum.extra1.name).toString() : "");

        List<Map<String, String>> appenJsondata = new ArrayList<>();
        appenJsondata.add(formStatus);
        appenJsondata.add(bizNoMap);
        MybatisPlusConfig.customerTableName.remove();
        return appenJsondata;
    }


    /**
     * 获取表单版本信息
     * @param code
     * @param version
     * @return
     */
    public static Map<String, Long> getCurrentTableInfo(String code, Integer version){
        Map<String, Long> currentTableInfo = generalQueryUtil.customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), version);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        return currentTableInfo;
    }

    /**
     * 获取表单字段信息
     * @param formVersionId 表单版本ID
     * @return
     */
    public static List<Map<String, String>> getFormFieldInfo(Long formVersionId){
        List<Map<String, String>> formFieldInfos = generalQueryUtil.customerFormMapper.getFormFieldInfo(formVersionId);
        CustomerStrategyUtil.verificationNull(formFieldInfos, CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        return formFieldInfos;
    }

    /**
     * 获取表单名称
     * @param formVersionId 表单版本ID
     * @return
     */
    public static String getFormName(Long formVersionId){
        //获取表名的中文名
        String formName = generalQueryUtil.customerFormMapper.getFormName(formVersionId);
        CustomerStrategyUtil.verificationNull(formName, CustomerBusinessEnum.FORM_NAME_EXCEPTION);
        return formName;
    }



    public static void dynamicTableName(String code) {
        MybatisPlusConfig.customerTableName.set(String.format("%s_%s", bizTablePrefix, code));
    }

    /**
     * 全部
     */
    public static List<Map<String,Object>> allDataInfos(){
        List<Map<String, Object>> allDatas=new ArrayList<>();
        allDatas.addAll(incidentSubmitByCurrentUser());
        allDatas.addAll(incidentTodo());
        allDatas.addAll(incidentTodoByGroup());
        return allDatas;
    }

    /**
     * 由我提交的事件单数据  复用之前的POP接口 如果非自己代办的数据 则判断筛选结果的人是否为单子创建人  如果是则按照我提交的逻辑  如果不是则按照我已办的逻辑  【按钮不一样】
     * @return 返回结果
     */
    public static List<Map<String,Object>> incidentSubmitByCurrentUser(){
        dynamicTableName(WorkOrderInformation.incident.getCode());
        List<Map<String, Object>> submitInfos = generalQueryUtil.customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("id,instance_id,extra1,status,event_title summary,event_priority").eq("created_by", CustomerBizInterceptor.currentUserThread.get().getUserId()).orderByDesc("id"));
        submitInfos.forEach(a->{
            a.put("taskId","");
            a.put("popType","1");
        });
        MybatisPlusConfig.customerTableName.remove();
        return submitInfos;
    }

    /**
     * 我代办的事件单数据
     * @return 返回结果
     */
    public static List<Map<String,Object>> incidentTodo(){
        dynamicTableName(WorkOrderInformation.incident.getCode());
        List<Map<String, Object>> incidentInfos = generalQueryUtil.customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("id,instance_id,extra1,status,event_title summary,event_priority").eq("extra5", CustomerBizInterceptor.currentUserThread.get().getUserId()).ne("instance_id","").orderByDesc("id"));
        incidentInfos.forEach(a->{
            String instanceId = String.valueOf(a.get("instance_id"));
            //通过实例ID查询当前任务ID
            List<Task> taskList = generalQueryUtil.taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                    .processInstanceId(instanceId)
                    .list();    // 例如请假会签，会同时拥有多个任务

            if (CollectionUtils.isEmpty(taskList)) {
                taskList = generalQueryUtil.taskService.createTaskQuery().processInstanceId(instanceId).list();
            }
            a.put("taskId",((TaskEntityImpl)taskList.get(0)).getId());
            a.put("popType","2");
        });
        MybatisPlusConfig.customerTableName.remove();
        return incidentInfos;
    }


    /**
     * 指派给我选定的组
     */

    public static List<Map<String,Object>> incidentTodoByGroup(List<String> groupIds){
        //查询当前登录人所在的工作组
        List<String> currentUsergroupIds = getAllBaseDataByPid(CustomerBizInterceptor.currentUserThread.get().getUserId());

        dynamicTableName(WorkOrderInformation.incident.getCode());
        List<Map<String, Object>> incidentInfos = generalQueryUtil.customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("id,instance_id,extra1,extra5,status,event_title summary,event_priority").in("extra5",groupIds).ne("instance_id","").orderByDesc("id"));
        incidentInfos.forEach(a->{
            if (!currentUsergroupIds.contains(a.get("extra5"))){
                a.put("taskId","");
                a.put("popType","3");
            }else {
                String instanceId = String.valueOf(a.get("instance_id"));
                //通过实例ID查询当前任务ID
                List<Task> taskList = generalQueryUtil.taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                        .processInstanceId(instanceId)
                        .list();    // 例如请假会签，会同时拥有多个任务

                if (CollectionUtils.isEmpty(taskList)) {
                    taskList = generalQueryUtil.taskService.createTaskQuery().processInstanceId(instanceId).list();
                }
                a.put("taskId",((TaskEntityImpl)taskList.get(0)).getId());
                a.put("popType","2");
            }
        });
        MybatisPlusConfig.customerTableName.remove();
        return incidentInfos;
    }


    /**
     * 指派给我所有组的代办
     */

    public static List<Map<String,Object>> incidentTodoByGroup(){
        //查询当前登录人所在的工作组
        List<String> groupIds = getAllBaseDataByPid(CustomerBizInterceptor.currentUserThread.get().getUserId());
        dynamicTableName(WorkOrderInformation.incident.getCode());
        List<Map<String, Object>> incidentInfos = generalQueryUtil.customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("id,instance_id,extra1,status,event_title summary,event_priority").in("extra5",groupIds).ne("instance_id","").orderByDesc("id"));
        incidentInfos.forEach(a->{
            String instanceId = String.valueOf(a.get("instance_id"));
            //通过实例ID查询当前任务ID
            List<Task> taskList = generalQueryUtil.taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                    .processInstanceId(instanceId)
                    .list();    // 例如请假会签，会同时拥有多个任务

            if (CollectionUtils.isEmpty(taskList)) {
                taskList = generalQueryUtil.taskService.createTaskQuery().processInstanceId(instanceId).list();
            }
            a.put("taskId",((TaskEntityImpl)taskList.get(0)).getId());
            a.put("popType","2");
        });
        MybatisPlusConfig.customerTableName.remove();
        return incidentInfos;
    }

}
