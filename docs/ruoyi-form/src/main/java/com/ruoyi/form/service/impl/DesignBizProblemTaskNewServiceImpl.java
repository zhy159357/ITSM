package com.ruoyi.form.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.DesignBizProblemTaskNew;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.ProblemStatus;
import com.ruoyi.form.enums.ProblemTaskStatus;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignBizProblemTaskNewMapper;
import com.ruoyi.form.service.DesignBizProblemTaskNewService;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.IDCodeConvertChineseUtil;
import com.ruoyi.form.vo.ProblemTaskRetrievalVo;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DateUtils.YYYY_MM_DD;
import static com.ruoyi.form.constants.ProblemConstant.STATUS;

@Service
@RequiredArgsConstructor
public class DesignBizProblemTaskNewServiceImpl extends ServiceImpl<DesignBizProblemTaskNewMapper, DesignBizProblemTaskNew> implements DesignBizProblemTaskNewService{

    private final TaskService taskService;
    private final CustomerFormMapper customerFormMapper;
    private final RuntimeService runtimeService;
    private final ISysDeptService iSysDeptService;
    private final IOgPersonService ogPersonService;
    private final IChangePersonService changePersonService;
    private final IOgGroupPersonService ogGroupPersonService;
    private final IPubParaValueService pubParaValueService;

    @Override
    public AjaxResult problemTaskRetrieval(Page page, ProblemTaskRetrievalVo problemTaskRetrievalVo) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.problem_task.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.verificationNull(formFieldInfos,CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        QueryWrapper<DesignBizProblemTaskNew> queryWrapper = getDesignBizProblemTaskNewQueryWrapper(problemTaskRetrievalVo, formFieldInfos);
        Page resultMapPage = this.pageMaps(page, queryWrapper);
        List<Map<String,Object>> records = (List<Map<String, Object>>)resultMapPage.getRecords();
        records.forEach(a -> {
            if (ObjectUtil.isNotEmpty(a.get("instance_id")) && !Arrays.asList(ProblemStatus.CANCEL.getInfo(), ProblemStatus.CLOSE.getInfo()).contains(a.get(STATUS).toString())) {
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(String.valueOf(a.get("instance_id"))).list();
                //TaskEntityImpl task1 = (TaskEntityImpl) unfinished.get(0);
                if (!org.springframework.util.CollectionUtils.isEmpty(taskList)) {
                    TaskEntityImpl task = (TaskEntityImpl) taskList.get(0);
                    a.put("taskId", task.getId());
                }
            } else {
                a.put("taskId", null);
            }
        });
//        records.forEach(a->{
//            if (ObjectUtil.isEmpty(a.get("instance_id"))) {
//                if (CustomerBizInterceptor.currentUserThread.get().getUserId().equals(a.get("created_by"))) {
//                    a.put("popType", "1");
//                    a.put("taskId", "");
//                } else {
//                    a.put("popType", "3");
//                    a.put("taskId", "");
//                }
//            }else {
//                if (ObjectUtil.isEmpty(a.get("current_handler_id"))){
//                    a.put("popType", "3");
//                    a.put("taskId", "");
//                }else {
//                    //查询当前筛选结果的任务ID
//                    if (a.get("current_handler_id").toString().contains(CustomerBizInterceptor.currentUserThread.get().getUserId())){
//                        String instance_id = a.get("instance_id").toString();
//                        //通过实例ID查询当前任务ID
//                        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
//                                .processInstanceId(instance_id)
//                                .list();    // 例如请假会签，会同时拥有多个任务
//
//                        if (CollectionUtils.isEmpty(taskList)) {
//                            taskList = taskService.createTaskQuery().processInstanceId(instance_id).list();
//                        }
//                        if (CollectionUtils.isEmpty(taskList)){
//                            log.error("problem_task_new query is error! activity-task-not-exits! biz dada instanceId is instance_id");
//                            a.put("taskId","");
//                            a.put("popType","3");
//                            return;
//                        }
//                        a.put("taskId",((TaskEntityImpl)taskList.get(0)).getId());
//                        a.put("popType","2");
//                    }else {
//                        a.put("popType", "3");
//                        a.put("taskId", "");
//                    }
//                }
//            }
//        });
        IDCodeConvertChineseUtil.convertIdToName(WorkOrderInformation.problem_task.getCode(),records);
        IDCodeConvertChineseUtil.convertEnumToName(null, records);
        resultMapPage.setRecords(records);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("fieldInfo",formFieldInfos);
        resultMap.put("pageListInfo",resultMapPage);
        return AjaxResult.success(resultMap);
    }

    private QueryWrapper<DesignBizProblemTaskNew> getDesignBizProblemTaskNewQueryWrapper(ProblemTaskRetrievalVo problemTaskRetrievalVo, List<Map<String, String>> formFieldInfos) {
        // 目前问题任务单表字段(org_id)里没有区分记录是总行或分行
//        if (org.apache.commons.lang3.StringUtils.isEmpty(problemTaskRetrievalVo.getDesignBizProblemTaskNew().getSystemId())) {
//            OgOrg ogOrg = iSysDeptService.selectDeptById(ogPersonService.selectOgPersonById(CustomerBizInterceptor.currentUserThread.get().getUserId()).getOrgId());
//            String orgId = changePersonService.selectDept(ogOrg.getOrgId());
//            if (com.ruoyi.common.utils.StringUtils.isBlank(orgId)) {
//                throw new BusinessException("问题发起人所在机构不正确,非总行或分行人员!");
//            }
//            // 查询总行管理员
//            List<OgGroupPerson> ogGroupPeopleList = ogGroupPersonService.selectOgGroupPersonById("f3ec04830847402288b5cf45a263c412");
//            List<OgGroupPerson> ogGroupPeople = ogGroupPeopleList.stream().filter(ogGroupPerson -> {
//                return ogGroupPerson.getPid().equals(CustomerBizInterceptor.currentUserThread.get().getUserId());
//            }).collect(Collectors.toList());
//
//            // 不是总行管理员
//            if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(ogGroupPeople)) {
////            problemTaskRetrievalVo.getDesignBizProblemTaskNew().setOrgId(orgId);
//            }
//        }
        // 阶段和状态把code改为中文
        if (StringUtils.isNotEmpty(problemTaskRetrievalVo.getDesignBizProblemTaskNew().getStage())) {
            List<PubParaValue> pubParaValues = pubParaValueService.selectPubParaValueByParaName("problem_task_step");
            if(CollectionUtils.isEmpty(pubParaValues)) {
                throw new BusinessException("根据字典key[problem_task_step]未找到字典项");
            }
            Map<String, String> pubParaMap = pubParaValues.stream().collect(Collectors.toMap(PubParaValue::getValue, PubParaValue::getValueDetail, (key1, key2) -> key1));
            problemTaskRetrievalVo.getDesignBizProblemTaskNew().setStage(pubParaMap.get( problemTaskRetrievalVo.getDesignBizProblemTaskNew().getStage()));
        }
        if (StringUtils.isNotEmpty(problemTaskRetrievalVo.getDesignBizProblemTaskNew().getStatus())) {
            List<PubParaValue> pubParaValues = pubParaValueService.selectPubParaValueByParaName("problem_task_status");
            if(CollectionUtils.isEmpty(pubParaValues)) {
                throw new BusinessException("根据字典key[problem_task_status]未找到字典项");
            }
            Map<String, String> pubParaMap = pubParaValues.stream().collect(Collectors.toMap(PubParaValue::getValue, PubParaValue::getValueDetail, (key1, key2) -> key1));
            problemTaskRetrievalVo.getDesignBizProblemTaskNew().setStatus(pubParaMap.get( problemTaskRetrievalVo.getDesignBizProblemTaskNew().getStatus()));
        }
        // 相关应用系统逻辑处理
        String systemId = org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getDesignBizProblemTaskNew().getSystemId()) ? problemTaskRetrievalVo.getDesignBizProblemTaskNew().getSystemId() : null;
        problemTaskRetrievalVo.getDesignBizProblemTaskNew().setSystemId(null);
        QueryWrapper<DesignBizProblemTaskNew> queryWrapper = new QueryWrapper<>(problemTaskRetrievalVo.getDesignBizProblemTaskNew());
        if (org.apache.commons.lang3.StringUtils.isNotBlank(systemId)) {
            queryWrapper.apply("JSON_CONTAINS(system_id, JSON_ARRAY({0}))", systemId);
        }

        //创建时间
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getCreatedStartTime())) {
            queryWrapper.ge(DesignBizProblemTaskNew.COL_TASK_CREATED_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getCreatedStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getCreatedEndTime())) {
            queryWrapper.le(DesignBizProblemTaskNew.COL_TASK_CREATED_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getCreatedEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题任务更新时间
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getUpdateStartTime())) {
            queryWrapper.ge(DesignBizProblemTaskNew.COL_TASK_UPDATED_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getUpdateStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getUpdateEndTime())) {
            queryWrapper.le(DesignBizProblemTaskNew.COL_TASK_UPDATED_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getUpdateEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        // 计划完成时间
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getPlanCompleteStartTime())) {
            queryWrapper.ge(DesignBizProblemTaskNew.COL_PLAN_COMPLETE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getPlanCompleteStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getPlanCompleteEndTime())) {
            queryWrapper.le(DesignBizProblemTaskNew.COL_PLAN_COMPLETE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getPlanCompleteEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        // 问题任务申请关闭时间
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getApplyCloseStartTime())) {
            queryWrapper.ge(DesignBizProblemTaskNew.COL_APPLY_CLOSE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getApplyCloseStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getApplyCloseEndTime())) {
            queryWrapper.le(DesignBizProblemTaskNew.COL_APPLY_CLOSE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getApplyCloseEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        // 问题任务关闭时间
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getCloseStartTime())) {
            queryWrapper.ge(DesignBizProblemTaskNew.COL_CLOSE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getCloseStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getCloseEndTime())) {
            queryWrapper.le(DesignBizProblemTaskNew.COL_CLOSE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getCloseEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        // 问题协查人更新开始时间
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getAssistantUpdateStartTime())) {
            queryWrapper.ge(DesignBizProblemTaskNew.COL_ASSISTANT_UPDATE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getAssistantUpdateStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getAssistantUpdateEndTime())) {
            queryWrapper.le(DesignBizProblemTaskNew.COL_ASSISTANT_UPDATE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemTaskRetrievalVo.getAssistantUpdateEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(problemTaskRetrievalVo.getIsClosed())) {
            switch (problemTaskRetrievalVo.getIsClosed()) {
                case "1":
                    queryWrapper.in(STATUS, ProblemTaskStatus.CANCEL.getInfo(), ProblemTaskStatus.CLOSE.getInfo());
                    break;
                case "0":
                    queryWrapper.in(STATUS, ProblemTaskStatus.WAIT_SUBMIT.getInfo(), ProblemTaskStatus.ASSIGNED.getInfo(), ProblemTaskStatus.SOLVING.getInfo(), ProblemTaskStatus.CLOSING.getInfo());
                    break;
            }
        }
        CustomerStrategyUtil.extractedSelectSql(queryWrapper,"问题任务", formFieldInfos);
        return queryWrapper;
    }


    @Override
    public void problemTaskRetrieval(ProblemTaskRetrievalVo problemTaskRetrievalVo, HttpServletResponse response) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.problem_task.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.verificationExportIdsNull(formFieldInfos,CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        QueryWrapper<DesignBizProblemTaskNew> queryWrapper = getDesignBizProblemTaskNewQueryWrapper(problemTaskRetrievalVo, formFieldInfos);
        switch (problemTaskRetrievalVo.getExcelParams().getType()){
            case "2":{
                CustomerStrategyUtil.verificationExportIdsNull(problemTaskRetrievalVo.getExcelParams().getIds(),CustomerBusinessEnum.EXPORT_IDS_ARRAY_EXCEPTION);
                queryWrapper.in("id",problemTaskRetrievalVo.getExcelParams().getIds());
                break;
            }
        }
        List<Map<String, Object>> list = this.listMaps(queryWrapper);
        CustomerStrategyUtil.exportExcel(list,formFieldInfos,"问题任务",problemTaskRetrievalVo.getExcelParams(),response);
    }
}
