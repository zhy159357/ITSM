package com.ruoyi.form.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.DesignBizChangetask;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignBizChangetaskMapper;
import com.ruoyi.form.service.DesignBizChangetaskService;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.IDCodeConvertChineseUtil;
import com.ruoyi.form.vo.ChangeTaskRetrievalVo;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DesignBizChangetaskServiceImpl extends ServiceImpl<DesignBizChangetaskMapper, DesignBizChangetask> implements DesignBizChangetaskService{

    private final TaskService taskService;
    private final CustomerFormMapper customerFormMapper;


    @Override
    public AjaxResult changeTaskRetrieval(Page page, ChangeTaskRetrievalVo changeTaskRetrievalVo) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.changeTask.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.verificationNull(formFieldInfos,CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        QueryWrapper<DesignBizChangetask> queryWrapper = getDesignBizChangetaskQueryWrapper(changeTaskRetrievalVo, formFieldInfos);
        Page resultMapPage = this.pageMaps(page, queryWrapper);
        List<Map<String,Object>> records = (List<Map<String, Object>>)resultMapPage.getRecords();
        records.forEach(a->{
            if (ObjectUtil.isEmpty(a.get("approval"))){
                a.put("popType","3");
                a.put("taskId","");
            }else {
                if (a.get("approval").toString().contains(CustomerBizInterceptor.currentUserThread.get().getUserId())){
                    Object instanceIdObj = a.get("instance_id");
                    if(instanceIdObj!=null){
                        String instance_id = instanceIdObj.toString();
                        //通过实例ID查询当前任务ID
                        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                                .processInstanceId(instance_id)
                                .list();    // 例如请假会签，会同时拥有多个任务

                        if (CollectionUtils.isEmpty(taskList)) {
                            taskList = taskService.createTaskQuery().processInstanceId(instance_id).list();
                        }
                        if (CollectionUtils.isEmpty(taskList)){
                            log.error("changeTask query is error! activity-task-not-exits! biz dada instanceId is instance_id");
                            a.put("taskId","");
                            a.put("popType","3");
                            return;
                        }
                        a.put("taskId",((TaskEntityImpl)taskList.get(0)).getId());
                        a.put("popType","2");
                    }
                }else {
                    a.put("popType","3");
                    a.put("taskId","");
                }
            }
        });
        IDCodeConvertChineseUtil.convertIdToName(WorkOrderInformation.changeTask.getCode(),records);
        IDCodeConvertChineseUtil.convertEnumToName(WorkOrderInformation.changeTask.getCode(),records);
        resultMapPage.setRecords(records);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("fieldInfo",formFieldInfos);
        resultMap.put("pageListInfo",resultMapPage);
        return AjaxResult.success(resultMap);
    }

    private QueryWrapper<DesignBizChangetask> getDesignBizChangetaskQueryWrapper(ChangeTaskRetrievalVo changeTaskRetrievalVo, List<Map<String, String>> formFieldInfos) {
        QueryWrapper<DesignBizChangetask> queryWrapper=new QueryWrapper<>(changeTaskRetrievalVo.getDesignBizChangetask());
        //创建时间
        queryWrapper.gt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getCreatedStartTime()),DesignBizChangetask.COL_CREATED_TIME, changeTaskRetrievalVo.getCreatedStartTime())
                .lt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getCreatedEndTime()),DesignBizChangetask.COL_CREATED_TIME, changeTaskRetrievalVo.getCreatedEndTime())
                //实际开始时间
                .gt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getImplStartTime()),DesignBizChangetask.COL_IMPLSTARTDATE, changeTaskRetrievalVo.getImplStartTime())
                .lt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getImplEndTime()),DesignBizChangetask.COL_IMPLSTARTDATE, changeTaskRetrievalVo.getImplEndTime())
                //实际结束时间
                .gt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getImplOverStartTime()),DesignBizChangetask.COL_IMPLENDDATE, changeTaskRetrievalVo.getImplOverStartTime())
                .lt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getImplOverEndTime()),DesignBizChangetask.COL_IMPLENDDATE, changeTaskRetrievalVo.getImplOverEndTime())
                //计划开始时间
                .gt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getPlanStartEarlyTime()),DesignBizChangetask.COL_PLANSTARTDATE, changeTaskRetrievalVo.getPlanStartEarlyTime())
                .lt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getPlanStartLateTime()),DesignBizChangetask.COL_PLANSTARTDATE, changeTaskRetrievalVo.getPlanStartLateTime())
                //计划结束时间
                .gt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getPlanEndEarlyTime()),DesignBizChangetask.COL_PLANOVERDATE, changeTaskRetrievalVo.getPlanEndEarlyTime())
                .lt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getPlanEndLateTime()),DesignBizChangetask.COL_PLANOVERDATE, changeTaskRetrievalVo.getPlanEndLateTime())
                //关闭时间
                .gt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getCloseEarlyTime()),DesignBizChangetask.COL_IMPLOVERDATE, changeTaskRetrievalVo.getCloseEarlyTime())
                .lt(StringUtils.isNotEmpty(changeTaskRetrievalVo.getCloseLateTime()),DesignBizChangetask.COL_IMPLOVERDATE, changeTaskRetrievalVo.getCloseLateTime());
        CustomerStrategyUtil.extractedSelectSql(queryWrapper,"变更任务", formFieldInfos);
        return queryWrapper;
    }

    @Override
    public void changeTaskRetrieval(ChangeTaskRetrievalVo changeRetrievalVo, HttpServletResponse response) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.changeTask.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.verificationExportIdsNull(formFieldInfos,CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        QueryWrapper<DesignBizChangetask> queryWrapper = getDesignBizChangetaskQueryWrapper(changeRetrievalVo, formFieldInfos);
        switch (changeRetrievalVo.getExcelParams().getType()){
            case "2":{
                CustomerStrategyUtil.verificationExportIdsNull(changeRetrievalVo.getExcelParams().getIds(),CustomerBusinessEnum.EXPORT_IDS_ARRAY_EXCEPTION);
                queryWrapper.in("id",changeRetrievalVo.getExcelParams().getIds());
                break;
            }
        }
        List<Map<String, Object>> list = this.listMaps(queryWrapper);
        CustomerStrategyUtil.exportExcel(list,formFieldInfos,"变更任务",changeRetrievalVo.getExcelParams(),response);
    }
}
