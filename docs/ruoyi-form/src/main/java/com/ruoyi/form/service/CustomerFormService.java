package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.CustomerFormListDTO;
import com.ruoyi.form.domain.CustomerVo;
import com.ruoyi.form.domain.OperationDetails;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface CustomerFormService {
    AjaxResult list(String code, String type,Page page, CustomerFormListDTO customerFormListDTO);

    AjaxResult insertOrUpdate(String code, CustomerFormContent customerFormContent);

    AjaxResult insertOrUpdate(String fromCode, String id, String code, CustomerFormContent customerFormContent);

    Integer dataOperation(String code, CustomerFormContent customerFormContent);

    Integer dataOperation(String fromCode, String id, String code, CustomerFormContent customerFormContent);

    AjaxResult deleteById(String code, Integer id);

    AjaxResult getParaValue(String paraName);

    Map<String, Object> getFormInfo(String code,Integer id,String processId);


    AjaxResult getFormJson(String code);

    AjaxResult processSubmit(String code, String businessKey,String version,Map<String,Object> variables);

    AjaxResult approvalPopUp(String code,String processId,Integer id,String type,String version);

    AjaxResult complete(String code, String taskId, String instanceId, String statusStr, CustomerVo customerVo);

    AjaxResult cancelApply(String instanceId);

    AjaxResult suspendOrActiveApply(String code,String instanceId,String bizNo);

    AjaxResult selectHistoryList(String code,String instanceId);

    AjaxResult getStartProcessCondition(String code,String businessKey);

    String activityXmlResource(String code);

    void createHistoryImage(String instanceId, HttpServletResponse response);

    AjaxResult addCaddCandidateUser(String taskId,String userId);

    AjaxResult problemTaskList(String problemNo, Page page);

    AjaxResult getOperationDetails(String bizNo,Page page);

    AjaxResult insertOperationDetails(String code, OperationDetails operationDetails);

    AjaxResult incidentCreate(String fromCode,String code, String id);

    AjaxResult updateFromList(CustomerFormContent customerFormContent);

    AjaxResult listChangeShifts(String code, String type, Page page, CustomerFormListDTO customerFormListDTO);


    AjaxResult listChangeSequence();

    AjaxResult changeSequence(String id,String userId, String upOrdown);

    AjaxResult reminder(String code, OperationDetails operationDetails);

    AjaxResult suspend(String code, String instanceId, String bizNo);

    AjaxResult saveRelationLogList(String formId, String formType, String relationFormId, String relationFormType);

    AjaxResult getRelationLogList(String formNo, String requestType, Page page);
    AjaxResult getRelationLogList(String formNo, String requestType);

    AjaxResult deleteGeneralManager(String taskId, String managerIds);

    AjaxResult getNoById(String id, String code);

    AjaxResult getProblemTaskFormInfo(String code, String problemNo);

    AjaxResult queryFormDetailByBizNo(String bizNo, String code);
}
