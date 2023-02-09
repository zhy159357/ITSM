package com.ruoyi.form.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.DesignBizChange;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignBizChangeMapper;
import com.ruoyi.form.service.DesignBizChangeService;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.IDCodeConvertChineseUtil;
import com.ruoyi.form.vo.ChangeRetrievalVo;
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
public class DesignBizChangeServiceImpl extends ServiceImpl<DesignBizChangeMapper, DesignBizChange> implements DesignBizChangeService{

    private final TaskService taskService;
    private final CustomerFormMapper customerFormMapper;


    @Override
    public AjaxResult changeRetrieval(Page page, ChangeRetrievalVo changeRetrievalVo) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.change.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.verificationNull(formFieldInfos,CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        QueryWrapper<DesignBizChange> queryWrapper = getDesignBizChangeQueryWrapper(changeRetrievalVo, formFieldInfos);
        Page resultMapPage = this.pageMaps(page, queryWrapper);
        List<Map<String,Object>> records = (List<Map<String, Object>>)resultMapPage.getRecords();
        records.forEach(a->{
            if (ObjectUtil.isEmpty(a.get("approval"))){
                a.put("popType","3");
                a.put("taskId","");
            }else {
                if (a.get("approval").toString().contains(CustomerBizInterceptor.currentUserThread.get().getUserId())){
                    String instance_id = a.get("instance_id").toString();
                    //通过实例ID查询当前任务ID
                    List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                            .processInstanceId(instance_id)
                            .list();    // 例如请假会签，会同时拥有多个任务

                    if (CollectionUtils.isEmpty(taskList)) {
                        taskList = taskService.createTaskQuery().processInstanceId(instance_id).list();
                    }
                    if (CollectionUtils.isEmpty(taskList)){
                        log.error("change query is error! activity-task-not-exits! biz dada instanceId is "+a.get("instance_id"));
                        a.put("taskId","");
                        a.put("popType","3");
                        return;
                    }
                    a.put("taskId",((TaskEntityImpl)taskList.get(0)).getId());
                    a.put("popType","2");
                }else {
                    a.put("popType","3");
                    a.put("taskId","");
                }
            }
        });
        IDCodeConvertChineseUtil.convertIdToName(WorkOrderInformation.change.getCode(),records);
        IDCodeConvertChineseUtil.convertEnumToName(WorkOrderInformation.change.getCode(),records);
        resultMapPage.setRecords(records);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("fieldInfo",formFieldInfos);
        resultMap.put("pageListInfo",resultMapPage);
        return AjaxResult.success(resultMap);
    }

    private QueryWrapper<DesignBizChange> getDesignBizChangeQueryWrapper(ChangeRetrievalVo changeRetrievalVo, List<Map<String, String>> formFieldInfos) {
        QueryWrapper<DesignBizChange> queryWrapper=new QueryWrapper<DesignBizChange>(changeRetrievalVo.getDesignBizChange());
        //发起时间
        queryWrapper.gt(StringUtils.isNotEmpty(changeRetrievalVo.getApplyEarlyData()),DesignBizChange.COL_CREATED_TIME, changeRetrievalVo.getApplyEarlyData())
                .lt(StringUtils.isNotEmpty(changeRetrievalVo.getApplyLateData()),DesignBizChange.COL_CREATED_TIME, changeRetrievalVo.getApplyLateData())
                //计划开始时间
                .gt(StringUtils.isNotEmpty(changeRetrievalVo.getPlanStartEarlyDate()),DesignBizChange.COL_PLANSTARTDATE, changeRetrievalVo.getPlanStartEarlyDate())
                .lt(StringUtils.isNotEmpty(changeRetrievalVo.getPlanStartLateDate()),DesignBizChange.COL_PLANSTARTDATE, changeRetrievalVo.getPlanStartLateDate())
                //计划结束时间
                .gt(StringUtils.isNotEmpty(changeRetrievalVo.getPlanEndEarlyDate()),DesignBizChange.COL_PLANCOMPLETEDATE, changeRetrievalVo.getPlanEndEarlyDate())
                .lt(StringUtils.isNotEmpty(changeRetrievalVo.getPlanEndLateDate()),DesignBizChange.COL_PLANCOMPLETEDATE, changeRetrievalVo.getPlanEndLateDate())
                //实际实施时间
                .gt(StringUtils.isNotEmpty(changeRetrievalVo.getImplementEarlyDate()),DesignBizChange.COL_IMPLEMENTSTARTDATE, changeRetrievalVo.getImplementEarlyDate())
                .lt(StringUtils.isNotEmpty(changeRetrievalVo.getImplementLateDate()),DesignBizChange.COL_IMPLEMENTSTARTDATE, changeRetrievalVo.getImplementLateDate())
                //实际完成时间
                .gt(StringUtils.isNotEmpty(changeRetrievalVo.getImplementOverEarlyDate()),DesignBizChange.COL_IMPLEMENTOVERDATE, changeRetrievalVo.getImplementOverEarlyDate())
                .lt(StringUtils.isNotEmpty(changeRetrievalVo.getImplementOverLateDate()),DesignBizChange.COL_IMPLEMENTOVERDATE, changeRetrievalVo.getImplementOverLateDate());
        CustomerStrategyUtil.extractedSelectSql(queryWrapper,"变更", formFieldInfos);
        return queryWrapper;
    }

    @Override
    public void changeRetrieval(ChangeRetrievalVo changeRetrievalVo, HttpServletResponse httpServletResponse) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.change.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.verificationExportIdsNull(formFieldInfos,CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        QueryWrapper<DesignBizChange> queryWrapper = getDesignBizChangeQueryWrapper(changeRetrievalVo, formFieldInfos);
        switch (changeRetrievalVo.getExcelParams().getType()){
            case "2":{
                CustomerStrategyUtil.verificationExportIdsNull(changeRetrievalVo.getExcelParams().getIds(),CustomerBusinessEnum.EXPORT_IDS_ARRAY_EXCEPTION);
                queryWrapper.in("id",changeRetrievalVo.getExcelParams().getIds());
                break;
            }
        }
        List<Map<String, Object>> list = this.listMaps(queryWrapper);
        CustomerStrategyUtil.exportExcel(list,formFieldInfos,"变更",changeRetrievalVo.getExcelParams(),httpServletResponse);
    }
}
