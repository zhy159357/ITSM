package com.ruoyi.form.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.system.service.ISysWorkService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.form.domain.DesignBizIncident;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignBizIncidentMapper;
import com.ruoyi.form.service.DesignBizIncidentService;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.GeneralQueryUtil;
import com.ruoyi.form.util.IDCodeConvertChineseUtil;
import com.ruoyi.form.vo.IncidentRetrievalVo;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DesignBizIncidentServiceImpl extends ServiceImpl<DesignBizIncidentMapper, DesignBizIncident> implements DesignBizIncidentService {
    private static final Logger log = LoggerFactory.getLogger(DesignBizIncidentServiceImpl.class);
    private final ISysWorkService sysWorkService;
    private final DesignBizIncidentMapper designBizIncidentMapper;
    private final TaskService taskService;
    private final CustomerFormMapper customerFormMapper;

    @Override
    public AjaxResult incidentRetrieval(Page page, IncidentRetrievalVo retrievalVo) {
    	log.info("...incidentRetrieval...");
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.incident.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.extractedSelectSql("??????",formFieldInfos);
        CustomerStrategyUtil.verificationNull(formFieldInfos, CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        // ?????????????????????????????????????????????
        if (StringUtils.isEmpty(retrievalVo.getSubmitStartTime()) || StringUtils.isEmpty(retrievalVo.getSubmitEndTime())) {
            retrievalVo.setSubmitStartTime(DateUtils.formatDate(DateUtils.addDays(new Date(), -3), DateUtils.YYYY_MM_DD_HH_MM_SS));
            retrievalVo.setSubmitEndTime(DateUtils.formatDate(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        }
        log.info("...incidentRetrieval...????????????...");
        Map<String, String> todoPersonal = new HashMap<>();
        todoPersonal.put("name", "extra5");
        todoPersonal.put("description", "???????????????");
        todoPersonal.put("display", "1");
        todoPersonal.put("exportable", "1");
        formFieldInfos.add(2,todoPersonal);
        Map<String, String> todoPersonalgroup = new HashMap<>();
        todoPersonalgroup.put("name", "orgname");
        todoPersonalgroup.put("description", "???????????????");
        todoPersonalgroup.put("display", "1");
        todoPersonalgroup.put("exportable", "1");
        formFieldInfos.add(3,todoPersonalgroup);
        log.info("...incidentRetrieval...????????????...");
       List state= retrievalVo.getDesignBizIncident().getEventState();
       List<String> trancState=new ArrayList<String>();
       	if(null!=state && state.size()>0) {
       		for (int i = 0; i < state.size(); i++) {
				if(Integer.parseInt(String.valueOf(state.get(i)))==1) {
					trancState.add("??????");
				}else if(Integer.parseInt(String.valueOf(state.get(i)))==2) {
					trancState.add("?????????");
				}else if(Integer.parseInt(String.valueOf(state.get(i)))==3) {
					trancState.add("?????????");
				}else if(Integer.parseInt(String.valueOf(state.get(i)))==4) {
					trancState.add("?????????");
				}else if(Integer.parseInt(String.valueOf(state.get(i)))==5) {
					trancState.add("?????????");
				}
			}
       		retrievalVo.getDesignBizIncident().setEventState(trancState);
       	}
       	/*if(!StringUtils.isEmpty(retrievalVo.getTodoGroup())) {
            OgGroup ogGroup = sysWorkService.selectOgGroupById(retrievalVo.getTodoGroup());
            if(ogGroup != null) {
                retrievalVo.setTodoGroup(ogGroup.getOrgId());
            }
        }*/
        IPage<Map<String, Object>> page1 = designBizIncidentMapper.incidentPageRetrieval(page, retrievalVo);
        log.info("...incidentRetrieval...????????????...");
        List<String> allBaseDataByPid = GeneralQueryUtil.getAllBaseDataByPid(userId);
        log.info("...incidentRetrieval...??????????????????id??????...");
        List<Map<String, Object>> records = page1.getRecords();
        records.forEach(a -> {
            //????????????????????????????????????????????????????????????????????????ID
            if (userId.equals(a.get("currentUserId")) || allBaseDataByPid.contains(a.get("currentUserId"))) {
                String instance_id = String.valueOf(a.get("instance_id"));
                //????????????ID??????????????????ID
                List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                        .processInstanceId(instance_id)
                        .list();    // ????????????????????????????????????????????????

                if (CollectionUtils.isEmpty(taskList)) {
                    taskList = taskService.createTaskQuery().processInstanceId(instance_id).list();
                }
                if (CollectionUtils.isEmpty(taskList)) {
                    log.error("incident query is error! activity-task-not-exits! biz dada instanceId is "+a.get("instance_id"));
                    a.put("taskId", "");
                    a.put("popType", "3");
                    return;
                }
                a.put("taskId", ((TaskEntityImpl) taskList.get(0)).getId());
                a.put("popType", "2");
            } else if (userId.equals(a.get("createdBy")) && StringUtils.isEmpty(a.get("instance_id"))) {
                a.put("taskId", "");
                a.put("popType", "1");
            } else {
                a.put("taskId", "");
                a.put("popType", "3");
            }
        });
        log.info("...incidentRetrieval...??????forEach...num:" +String.valueOf(null!=records?records.size():"---"));
//        IDCodeConvertChineseUtil.convertIdToName(WorkOrderInformation.incident.getCode(), records);
        IDCodeConvertChineseUtil.convertEnumToName(WorkOrderInformation.incident.getCode(),records);
        page1.setRecords(records);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("fieldInfo", formFieldInfos);
        resultMap.put("pageListInfo", page1);
        return AjaxResult.success(resultMap);
    }

    @Override
    public void incidentRetrieval(IncidentRetrievalVo retrievalVo, HttpServletResponse response) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.incident.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        Map<String, String> todoPersonal = new HashMap<>();
        todoPersonal.put("name", "extra5");
        todoPersonal.put("description", "???????????????");
        todoPersonal.put("exportable", "1");
        formFieldInfos.add(2,todoPersonal);
        Map<String, String> todoPersonalgroup = new HashMap<>();
        todoPersonalgroup.put("name", "orgname");
        todoPersonalgroup.put("description", "???????????????");
        todoPersonalgroup.put("exportable", "1");
        formFieldInfos.add(3,todoPersonalgroup);
        CustomerStrategyUtil.verificationExportIdsNull(formFieldInfos, CustomerBusinessEnum.FORM_FIELD_EXCEPTION);
        // ?????????????????????????????????????????????
        if (StringUtils.isEmpty(retrievalVo.getSubmitStartTime()) || StringUtils.isEmpty(retrievalVo.getSubmitEndTime())) {
            retrievalVo.setSubmitStartTime(DateUtils.formatDate(DateUtils.addDays(new Date(), -3), DateUtils.YYYY_MM_DD_HH_MM_SS));
            retrievalVo.setSubmitEndTime(DateUtils.formatDate(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        }
        log.info("...export.incidentRetrieval...????????????...");
        List state= retrievalVo.getDesignBizIncident().getEventState();
        List<String> trancState=new ArrayList<String>();
        	if(null!=state && state.size()>0) {
        		for (int i = 0; i < state.size(); i++) {
 				if(Integer.parseInt(String.valueOf(state.get(i)))==1) {
 					trancState.add("??????");
 				}else if(Integer.parseInt(String.valueOf(state.get(i)))==2) {
 					trancState.add("?????????");
 				}else if(Integer.parseInt(String.valueOf(state.get(i)))==3) {
 					trancState.add("?????????");
 				}else if(Integer.parseInt(String.valueOf(state.get(i)))==4) {
 					trancState.add("?????????");
 				}else if(Integer.parseInt(String.valueOf(state.get(i)))==5) {
 					trancState.add("?????????");
 				}
 			}
        		retrievalVo.getDesignBizIncident().setEventState(trancState);
        	}
        List<Map<String, Object>> list = designBizIncidentMapper.incidentRetrieval(retrievalVo);
        log.info("...export.incidentRetrieval...????????????...");
        switch (retrievalVo.getExcelParams().getType()) {
            case "2": {
                CustomerStrategyUtil.verificationExportIdsNull(retrievalVo.getExcelParams().getIds(), CustomerBusinessEnum.EXPORT_IDS_ARRAY_EXCEPTION);
                list = list.stream().filter(a -> retrievalVo.getExcelParams().getIds().contains(String.valueOf(a.get("id")))).collect(Collectors.toList());
                break;
            }
        }
        buildIncidentSubEventFieldInfos(formFieldInfos);
        log.info("...export.buildIncidentSubEventFieldInfos...????????????...");
        CustomerStrategyUtil.exportExcelSet(list, formFieldInfos, "?????????", retrievalVo.getExcelParams(), response);
    }


    /**
     * ?????????????????????????????????????????????????????????  ?????????????????????????????????????????? ????????????????????????????????????????????????
     *
     * @param formFieldInfos
     * @return
     */
    private static void buildIncidentSubEventFieldInfos(List<Map<String, String>> formFieldInfos) {
        Map<String, String> incidentSubEvent1 = new HashMap<>();
        incidentSubEvent1.put("name", "submit_time");
        incidentSubEvent1.put("description", "????????????");
        incidentSubEvent1.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent1);
        Map<String, String> incidentSubEvent2 = new HashMap<>();
        incidentSubEvent2.put("name", "solve_time");
        incidentSubEvent2.put("description", "????????????");
        incidentSubEvent2.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent2);
        Map<String, String> incidentSubEvent3 = new HashMap<>();
        incidentSubEvent3.put("name", "solve_person");
        incidentSubEvent3.put("description", "?????????");
        incidentSubEvent3.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent3);
        Map<String, String> incidentSubEvent4 = new HashMap<>();
        incidentSubEvent4.put("name", "solve_org");
        incidentSubEvent4.put("description", "????????????");
        incidentSubEvent4.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent4);
        Map<String, String> incidentSubEvent5 = new HashMap<>();
        incidentSubEvent5.put("name", "solve_group");
        incidentSubEvent5.put("description", "?????????");
        incidentSubEvent5.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent5);
        Map<String, String> incidentSubEvent6 = new HashMap<>();
        incidentSubEvent6.put("name", "close_time");
        incidentSubEvent6.put("description", "????????????");
        incidentSubEvent6.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent6);
        Map<String, String> incidentSubEvent7 = new HashMap<>();
        incidentSubEvent7.put("name", "suspend_flag");
        incidentSubEvent7.put("description", "????????????");
        incidentSubEvent7.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent7);
        Map<String, String> incidentSubEvent8 = new HashMap<>();
        incidentSubEvent8.put("name", "suspend_time");
        incidentSubEvent8.put("description", "????????????");
        incidentSubEvent8.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent8);
        Map<String, String> incidentSubEvent9 = new HashMap<>();
        incidentSubEvent9.put("name", "urge_flag");
        incidentSubEvent9.put("description", "????????????");
        incidentSubEvent9.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent9);
        Map<String, String> incidentSubEvent10 = new HashMap<>();
        incidentSubEvent10.put("name", "service_back_falg");
        incidentSubEvent10.put("description", "??????????????????");
        incidentSubEvent10.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent10);
        Map<String, String> incidentSubEvent11 = new HashMap<>();
        incidentSubEvent11.put("name", "add_msg_back_falg");
        incidentSubEvent11.put("description", "????????????????????????");
        incidentSubEvent11.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent11);
        Map<String, String> incidentSubEvent12 = new HashMap<>();
        incidentSubEvent12.put("name", "cancel_falg");
        incidentSubEvent12.put("description", "????????????");
        incidentSubEvent12.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent12);
        Map<String, String> incidentSubEvent13 = new HashMap<>();
        incidentSubEvent13.put("name", "cancel_time");
        incidentSubEvent13.put("description", "????????????");
        incidentSubEvent13.put("exportable", "1");
        formFieldInfos.add(incidentSubEvent13);
    }
}
