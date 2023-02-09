package com.ruoyi.form.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.form.domain.CommonTree;
import com.ruoyi.form.domain.DesignBizProblem;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.ProblemStatus;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignBizProblemMapper;
import com.ruoyi.form.service.DesignBizProblemService;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.form.service.ICommonTreeService;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.IDCodeConvertChineseUtil;
import com.ruoyi.form.vo.ProblemRetrievalVo;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class DesignBizProblemServiceImpl extends ServiceImpl<DesignBizProblemMapper, DesignBizProblem> implements DesignBizProblemService {
    private final TaskService taskService;
    private final CustomerFormMapper customerFormMapper;
    private final RuntimeService runtimeService;
    private final ISysDeptService iSysDeptService;
    private final IOgPersonService ogPersonService;
    private final IChangePersonService changePersonService;
    private final IOgGroupPersonService ogGroupPersonService;
    private final ISysWorkService iSysWorkService;
    private final IPubParaValueService pubParaValueService;
    @Autowired
    private ICommonTreeService commonTreeService;
    @Override
    public AjaxResult problemRetrieval(Page page, ProblemRetrievalVo problemRetrievalVo) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.problem.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.verificationNull(formFieldInfos,CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        QueryWrapper<DesignBizProblem> queryWrapper = getDesignBizProblemQueryWrapper(problemRetrievalVo, formFieldInfos);
        Page resultMapPage = this.pageMaps(page, queryWrapper);
        List<Map<String,Object>> records = (List<Map<String, Object>>)resultMapPage.getRecords();
        records.forEach(a -> {
            if (ObjectUtil.isNotEmpty(a.get("instance_id")) && !Arrays.asList(ProblemStatus.CANCEL.getInfo(), ProblemStatus.CLOSE.getInfo()).contains(a.get(STATUS).toString())) {
                List<Task> taskList;
                // 如果是问题单,且状态是提交总经理室审核
                if (ProblemStatus.GENERAL_MANAGER_AUDIT.getInfo().equals(a.get(STATUS).toString())) {
                    taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                            .processInstanceId(a.get("instance_id").toString())
                            .list();    // 总经理室审核，会同时拥有多个任务(选择了几个人同时审批)
                    // 如果为空证明当前登录人不是总经理室选定人
                    if (CollectionUtils.isEmpty(taskList)) {
                        // 查询总经理室组的待办人员id
                        Map<String, Object> managerMap = runtimeService.getVariables(String.valueOf(a.get("instance_id")));
                        List<String> generalManagerIds = (List<String>) managerMap.get("generalManagerIds");
                        for (String generalManagerId : generalManagerIds) {
                            // 如果有任务,则跳循环
                            taskList = taskService.createTaskQuery().taskCandidateOrAssigned(generalManagerId)
                                    .processInstanceId(a.get("instance_id").toString())
                                    .list();
                            if (!CollectionUtils.isEmpty(taskList)) {
                                break;
                            }
                        }
                    }
                } else {
                    taskList = taskService.createTaskQuery().processInstanceId(String.valueOf(a.get("instance_id"))).list();
                }
                //TaskEntityImpl task1 = (TaskEntityImpl) unfinished.get(0);
                if (!org.springframework.util.CollectionUtils.isEmpty(taskList)) {
                    TaskEntityImpl task = (TaskEntityImpl) taskList.get(0);
                    a.put("taskId", task.getId());
                }
            } else {
                a.put("taskId", null);
            }
        });
//        page.setRecords(records);
//        records.forEach(a -> {
//            if (ObjectUtil.isEmpty(a.get("instance_id"))) {
//                if (CustomerBizInterceptor.currentUserThread.get().getUserId().equals(a.get("created_by"))) {
//                    a.put("popType", "1");
//                    a.put("taskId", "");
//                } else {
//                    a.put("popType", "3");
//                    a.put("taskId", "");
//                }
//            }else {
//                if (ObjectUtil.isEmpty(a.get("current_deal_id"))){
//                    a.put("popType", "3");
//                    a.put("taskId", "");
//                }else {
//                    //查询当前筛选结果的任务ID
//                    if (a.get("current_deal_id").toString().contains(CustomerBizInterceptor.currentUserThread.get().getUserId())){
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
//                            log.error("incident query is error! activity-task-not-exits! biz dada instanceId is instance_id");
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
        IDCodeConvertChineseUtil.convertIdToName(WorkOrderInformation.problem.getCode(),records);
        IDCodeConvertChineseUtil.convertEnumToName(null, records);
        resultMapPage.setRecords(records);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("fieldInfo",formFieldInfos);
        resultMap.put("pageListInfo",resultMapPage);
        return AjaxResult.success(resultMap);
    }

    private QueryWrapper<DesignBizProblem> getDesignBizProblemQueryWrapper(ProblemRetrievalVo problemRetrievalVo, List<Map<String, String>> formFieldInfos) {
        if (StringUtils.isEmpty(problemRetrievalVo.getDesignBizProblem().getOrgId())) {
            OgOrg ogOrg = iSysDeptService.selectDeptById(ogPersonService.selectOgPersonById(CustomerBizInterceptor.currentUserThread.get().getUserId()).getOrgId());
            String orgId = changePersonService.selectDept(ogOrg.getOrgId());
            if (com.ruoyi.common.utils.StringUtils.isBlank(orgId)) {
                throw new BusinessException("当前登录人所在机构不正确,非总行或分行人员!");
            }
            // 查询总行管理员
            List<OgGroupPerson> ogGroupPeopleList = ogGroupPersonService.selectOgGroupPersonById("f3ec04830847402288b5cf45a263c412");
            List<OgGroupPerson> ogGroupPeople = ogGroupPeopleList.stream().filter(ogGroupPerson -> {
                return ogGroupPerson.getPid().equals(CustomerBizInterceptor.currentUserThread.get().getUserId());
            }).collect(Collectors.toList());

            // 不是总行管理员
            if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(ogGroupPeople)) {
                problemRetrievalVo.getDesignBizProblem().setOrgId(orgId);
            }
        }
        // 阶段和状态把code改为中文
        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(problemRetrievalVo.getDesignBizProblem().getStage())) {
            List<PubParaValue> pubParaValues = pubParaValueService.selectPubParaValueByParaName("problem_step");
            if(org.springframework.util.CollectionUtils.isEmpty(pubParaValues)) {
                throw new BusinessException("根据字典key[problem_step]未找到字典项");
            }
            Map<String, String> pubParaMap = pubParaValues.stream().collect(Collectors.toMap(PubParaValue::getValue, PubParaValue::getValueDetail, (key1, key2) -> key1));
            problemRetrievalVo.getDesignBizProblem().setStage(pubParaMap.get( problemRetrievalVo.getDesignBizProblem().getStage()));
        }
        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(problemRetrievalVo.getDesignBizProblem().getStatus())) {
            List<PubParaValue> pubParaValues = pubParaValueService.selectPubParaValueByParaName("problem_status");
            if(org.springframework.util.CollectionUtils.isEmpty(pubParaValues)) {
                throw new BusinessException("根据字典key[problem_status]未找到字典项");
            }
            Map<String, String> pubParaMap = pubParaValues.stream().collect(Collectors.toMap(PubParaValue::getValue, PubParaValue::getValueDetail, (key1, key2) -> key1));
            problemRetrievalVo.getDesignBizProblem().setStatus(pubParaMap.get( problemRetrievalVo.getDesignBizProblem().getStatus()));
        }
        // 问题发起部室
        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(problemRetrievalVo.getDesignBizProblem().getOriDepId())) {
            CommonTree commonTree = commonTreeService.selectCommonTreeById(Long.valueOf(problemRetrievalVo.getDesignBizProblem().getOriDepId()));
            String orgId = commonTree.getOgId();
            problemRetrievalVo.getDesignBizProblem().setOriDepId(orgId);
        }
        // 当前处理人为多人要特殊处理查询逻辑
        String dealId = StringUtils.isNotBlank(problemRetrievalVo.getDesignBizProblem().getCurrentDealId()) ? problemRetrievalVo.getDesignBizProblem().getCurrentDealId() : null;
        problemRetrievalVo.getDesignBizProblem().setCurrentDealId(null);
        // 相关应用系统逻辑处理
        String systemId = StringUtils.isNotBlank(problemRetrievalVo.getDesignBizProblem().getSystemId()) ? problemRetrievalVo.getDesignBizProblem().getSystemId() : null;
        problemRetrievalVo.getDesignBizProblem().setSystemId(null);
        QueryWrapper<DesignBizProblem> queryWrapper = new QueryWrapper<>(problemRetrievalVo.getDesignBizProblem());
        if (StringUtils.isNotBlank(dealId)) {
            queryWrapper.like(DesignBizProblem.COL_CURRENT_DEAL_ID, dealId);
        }
        if (StringUtils.isNotBlank(systemId)) {
            queryWrapper.apply("JSON_CONTAINS(system_id, JSON_ARRAY({0}))", systemId);
        }
        //问题提交时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getSubmitStartTime())) {
            queryWrapper.ge(DesignBizProblem.COL_SUBMIT_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSubmitStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getSubmitEndTime())) {
            queryWrapper.le(DesignBizProblem.COL_SUBMIT_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSubmitEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题计划完成时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getPlanCompleteStartTime())) {
            queryWrapper.ge(DesignBizProblem.COL_PLAN_COMPLETE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getPlanCompleteStartTime(), YYYY_MM_DD), YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getPlanCompleteEndTime())) {
            queryWrapper.le(DesignBizProblem.COL_PLAN_COMPLETE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getPlanCompleteEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题创建时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getCreateStartTime())) {
            queryWrapper.ge(DesignBizProblem.COL_PROBLEM_CREATED_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getCreateStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getCreateEndTime())) {
            queryWrapper.le(DesignBizProblem.COL_PROBLEM_CREATED_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getCreateEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题更新时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getUpdateStartTime())) {
            queryWrapper.ge(DesignBizProblem.COL_PROBLEM_UPDATE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getUpdateStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getUpdateEndTime())) {
            queryWrapper.le(DesignBizProblem.COL_PROBLEM_UPDATE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getUpdateEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题提交解决方案时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getSubmitSolutionStartTime())) {
            queryWrapper.ge(DesignBizProblem.COL_SUBMIT_SOLUTION_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSubmitSolutionStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getSubmitSolutionEndTime())) {
            queryWrapper.le(DesignBizProblem.COL_SUBMIT_SOLUTION_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSubmitSolutionEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题根因已明时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getSolverClearStartTime())) {
            queryWrapper.ge(DesignBizProblem.COL_SOLVER_CLEAR_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSolverClearStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getSolverClearEndTime())) {
            queryWrapper.le(DesignBizProblem.COL_SOLVER_CLEAR_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSolverClearEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题解决时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getSolveStartTime())) {
            queryWrapper.ge(DesignBizProblem.COL_SOLVE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSolveStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getSolveEndTime())) {
            queryWrapper.le(DesignBizProblem.COL_SOLVE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSolveEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题牵头人上次更新时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getSolverLastStartUpdated())) {
            queryWrapper.ge(DesignBizProblem.COL_SOLVER_LAST_UPDATED, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSolverLastStartUpdated(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getSolverLastEndUpdated())) {
            queryWrapper.le(DesignBizProblem.COL_SOLVER_LAST_UPDATED, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getSolverLastEndUpdated(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题关闭时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getCloseStartTime())) {
            queryWrapper.ge(DesignBizProblem.COL_CLOSE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getCloseStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getCloseEndTime())) {
            queryWrapper.le(DesignBizProblem.COL_CLOSE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getCloseEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        //问题观察期限时间
        if (StringUtils.isNotBlank(problemRetrievalVo.getObserveStartTime())) {
            queryWrapper.ge(DesignBizProblem.COL_OBSERVE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getObserveStartTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getObserveEndTime())) {
            queryWrapper.le(DesignBizProblem.COL_OBSERVE_TIME, DateUtils.formatDate(DateUtils.parseDate(problemRetrievalVo.getObserveEndTime(), YYYY_MM_DD),YYYY_MM_DD));
        }
        if (StringUtils.isNotBlank(problemRetrievalVo.getIsClosed())) {
            switch (problemRetrievalVo.getIsClosed()) {
                case "1":
                    queryWrapper.in(STATUS, ProblemStatus.CANCEL.getInfo(), ProblemStatus.CLOSE.getInfo());
                    break;
                case "0":
                    queryWrapper.in(STATUS, ProblemStatus.WAIT_SUBMIT.getInfo(), ProblemStatus.COMPLIANCE_AUDIT.getInfo(), ProblemStatus.TECHNOLOGY_AUDIT.getInfo(), ProblemStatus.ASSIGNED.getInfo(),
                            ProblemStatus.UNDER_INVESTIGATION.getInfo(), ProblemStatus.SOLUTION_PENDING.getInfo(), ProblemStatus.SOLUTION_AUDIT.getInfo(), ProblemStatus.SOLVING.getInfo(),
                            ProblemStatus.AUDITOR_CONFIRMING.getInfo(), ProblemStatus.ADMIN_CONFIRMING.getInfo(), ProblemStatus.ORIGINATOR_CONFIRMING.getInfo(), ProblemStatus.ORIGINATE_DEPART_MANAGER_CONFIRMING.getInfo(),
                            ProblemStatus.GENERAL_MANAGER_AUDIT.getInfo(), ProblemStatus.OBSERVING.getInfo());
                    break;
            }
        }
        CustomerStrategyUtil.extractedSelectSql(queryWrapper,"问题单", formFieldInfos);
        return queryWrapper;
    }


    @Override
    public void problemRetrieval(ProblemRetrievalVo problemRetrievalVo, HttpServletResponse response) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.problem.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.verificationExportIdsNull(formFieldInfos,CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        QueryWrapper<DesignBizProblem> queryWrapper = getDesignBizProblemQueryWrapper(problemRetrievalVo, formFieldInfos);
        switch (problemRetrievalVo.getExcelParams().getType()){
            case "2":{
                CustomerStrategyUtil.verificationExportIdsNull(problemRetrievalVo.getExcelParams().getIds(),CustomerBusinessEnum.EXPORT_IDS_ARRAY_EXCEPTION);
                queryWrapper.in("id",problemRetrievalVo.getExcelParams().getIds());
                break;
            }
        }
        List<Map<String, Object>> list = this.listMaps(queryWrapper);
        CustomerStrategyUtil.exportExcel(list,formFieldInfos,"问题单",problemRetrievalVo.getExcelParams(),response);
    }
}
