package com.ruoyi.form.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.service.StatisticalAnalysisService;
import com.ruoyi.form.util.GeneralQueryUtil;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * @ClassName StatisticalAnalysisServiceImpl
 * @Description 统计分析
 * @Author JiaQi Zhang
 * @Date 2022/10/1 8:53
 */
@Service
@RequiredArgsConstructor
public class StatisticalAnalysisServiceImpl implements StatisticalAnalysisService {

    private final TaskService taskService;
    private final CustomerFormMapper customerFormMapper;

    @Override
    public AjaxResult statisticalAnalysis() {
        Map<String,Object> resultMap=new HashMap<>();
        String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        List<String> allBaseData = GeneralQueryUtil.getAllBaseDataByPid(userId);

        long total=0;

        long problemTotal = buildStatisticalResult(userId, allBaseData, WorkOrderInformation.problem.getCode());
        long problemTaskTotal = buildStatisticalResult(userId, allBaseData, WorkOrderInformation.problem_task.getCode());
        long incidentTotal = buildStatisticalResult(userId, allBaseData, WorkOrderInformation.incident.getCode());
        long changeTotal = buildStatisticalResult(userId, allBaseData, WorkOrderInformation.change.getCode());
        long changeTaskTotal = buildStatisticalResult(userId, allBaseData, WorkOrderInformation.changeTask.getCode());

        total=problemTotal+problemTaskTotal+incidentTotal+changeTotal+changeTaskTotal;

        Map<String,Object> problemMap=new HashMap<>();
        problemMap.put(WorkOrderInformation.problem.getCode(),problemTotal+problemTaskTotal);
        problemMap.put(WorkOrderInformation.problem_task.getCode(),problemTaskTotal);
        resultMap.put(WorkOrderInformation.problem.getCode(),problemMap);
//        problemMap.put(WorkOrderInformation.problem.getCode(),problemTotal);
//        problemMap.put(WorkOrderInformation.problem_task.getCode(),problemTaskTotal);
//        resultMap.put(WorkOrderInformation.problem.getCode(),problemMap);
        Map<String,Object> incidentMap=new HashMap<>();
        incidentMap.put(WorkOrderInformation.incident.getCode(),incidentTotal);
        resultMap.put(WorkOrderInformation.incident.getCode(),incidentMap);

        Map<String,Object> changeMap=new HashMap<>();
        changeMap.put(WorkOrderInformation.change.getCode(),changeTotal+changeTaskTotal);
        changeMap.put(WorkOrderInformation.changeTask.getCode(),changeTaskTotal);
        resultMap.put(WorkOrderInformation.change.getCode(),changeMap);
        
//        changeMap.put(WorkOrderInformation.change.getCode(),changeTotal);
//        changeMap.put(WorkOrderInformation.changeTask.getCode(),changeTaskTotal);
//        resultMap.put(WorkOrderInformation.change.getCode(),changeMap);
        Map<String,Object> totalMap=new HashMap<>();
        totalMap.put("total",total);
        resultMap.put("total",totalMap);
        return AjaxResult.success(resultMap);
    }

    /**
     * 获取某种类型单子的代办总数
     * @param userId
     * @param allBaseData
     * @param code
     * @return
     */
    private long buildStatisticalResult(String userId, List<String> allBaseData, String code) {
        // 事件单查询待办
        if(code.equals(WorkOrderInformation.incident.getCode())) {
            MybatisPlusConfig.customerTableName.set(String.format("%s_%s", "design_biz", code));
            Integer count = customerFormMapper.selectCount(new QueryWrapper<CustomerFormContent>().eq("extra5", CustomerBizInterceptor.currentUserThread.get().getUserId()));
            return count;
        }else  if(code.equals(WorkOrderInformation.problem.getCode())) {
            MybatisPlusConfig.customerTableName.set(String.format("%s_%s", "design_biz", code));
            Integer count = customerFormMapper.selectCount(new QueryWrapper<CustomerFormContent>().like("current_deal_id", CustomerBizInterceptor.currentUserThread.get().getUserId()));
            return count;
        }else if(code.equals(WorkOrderInformation.problem_task.getCode())) {
            MybatisPlusConfig.customerTableName.set(String.format("%s_%s", "design_biz", code));
            Integer count = customerFormMapper.selectCount(new QueryWrapper<CustomerFormContent>().like("current_handler_id", CustomerBizInterceptor.currentUserThread.get().getUserId()));
            return count;
        }else if(code.equals(WorkOrderInformation.change.getCode())) {
            MybatisPlusConfig.customerTableName.set(String.format("%s_%s", "design_biz", code));
            Integer count = customerFormMapper.selectCount(new QueryWrapper<CustomerFormContent>().like("currentProcessor", CustomerBizInterceptor.currentUserThread.get().getUserId()));
            return count;
        }else if(code.equals(WorkOrderInformation.changeTask.getCode())) {
            MybatisPlusConfig.customerTableName.set(String.format("%s_%s", "design_biz", code));
            Integer count = customerFormMapper.selectCount(new QueryWrapper<CustomerFormContent>().like("currentProcessor", CustomerBizInterceptor.currentUserThread.get().getUserId()));
            return count;
        }
//        List<Task> groupTasks = taskService.createTaskQuery().processDefinitionKey(code).taskCandidateGroupIn(allBaseData).list();
        List<Task> personalTasks = taskService.createTaskQuery().processDefinitionKey(code).taskCandidateOrAssigned(userId).list();
//        List<Task> allTasks=new ArrayList<>();
//        allTasks.addAll(groupTasks);
//        allTasks.addAll(personalTasks);
//        long count = allTasks.stream().distinct().collect(Collectors.toList()).stream().count();
        return personalTasks.size();
    }
}
