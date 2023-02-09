package com.ruoyi.form.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.CustomerVo;
import com.ruoyi.form.domain.RiskAccessRecord;
import com.ruoyi.form.openapi.vo.ECompleteVO;

import java.util.List;
import java.util.Map;

public interface IChangeService {

    String getChangeVersion();

    String getChangeTaskVersion();

    void updateChangeIdByChangeNoANDTaskId(String id, String changeNo);

    AjaxResult init(String formCode, String id, String currentUserId, Map<String, Object> fieldMap, Boolean pageFlag);

    AjaxResult initChangeTask(Map<String, Object> params);

    AjaxResult initByInterface(String userId, Map<String, Object> changeParam, List<Map<String, Object>> taskParamList);

    AjaxResult setAdminFreeApproval(String flag, String comment, String changeId);

    AjaxResult initTaskByPage(String changNo, String taskDept);

    AjaxResult initAndSubmitChangeTask(String changeNo, String taskDept);

    AjaxResult addBasis(String changeNo, String basis);

    AjaxResult saveTaskAndStart(CustomerFormContent customerFormContent);

    CustomerFormContent initCustomerFormContent(String tableName);

    List<Map<String, Object>> getSceneData(String tableCode, String changeTaskNo);

    List<Map<String, Object>> getAllFieldValueBySingleCondition(String tableCode, String key, String value);

    Page<Map<String, Object>> getAllFieldValuePageBySingleCondition(String tableCode, String key, String value, Page<Map<String, Object>> page);

    AjaxResult delARowBySingleCondition(String tableCode, String key, String value);

    int getCountBySingleCondition(String tableCode, String key, String value);

    AjaxResult getChangeTotalPage();

    //OgPerson getOrgLeaderByOrgId(String orgId);

    AjaxResult backCompletion(String taskId, String instanceId, CustomerVo customerVo);

    // AjaxResult remedyTask(String taskId, String instanceId, CustomerVo customerVo);

    AjaxResult changeTaskList(String changeNo);

    String getCurrentVersion(String tableName);

    AjaxResult changeTaskListToTable(String changeNo);

    AjaxResult getAppAccessData(String changeNo, String prefix);

    AjaxResult viewTaskPage(String changeTaskId);

    AjaxResult viewChangePage(String changeNo);

    AjaxResult setCandidate();

    AjaxResult getAdminApprovalRecord(String changeNo);

    AjaxResult getTotalList(Integer type, String date, Integer pageNum, Integer pageSize);

    AjaxResult viewTaskSceneTablePageAndData(String code, String changeTaskNo);

    AjaxResult cancelChangeProcess(String id);

    AjaxResult getAllMergeTaskInfoList(String changeId);

    AjaxResult getAllMergeTaskInfoListByTaskNo(String changeTaskNo);

    AjaxResult cancelChangeTaskProcess(String changeTaskNo);

    AjaxResult transferTask(String taskId, String changeTaskNo, CustomerFormContent customerFormContent);

    AjaxResult robTask(String changeTaskNo);

    AjaxResult chargePlanTime(String changeTaskNo, String planStartDate);

    AjaxResult setBackToPreApprovalFlag(String changeTaskNo, Integer flag);

    AjaxResult setBackToStart(String changeTaskNo, Integer flag);

    AjaxResult autoStartDevTask(String changeTaskNo);

    AjaxResult autoStartDevTaskNew(String changeTaskNo);

    AjaxResult getAutoStatusNew(String changeTaskNo);

    AjaxResult getAutoStatus(String changeTaskNo);

    AjaxResult adjustRiskLevel(RiskAccessRecord riskAccessRecord);

    AjaxResult addOrUpdateSceneData(String code, Map<String, Object> fields);

    public AjaxResult addARowSceneData(String code, Map<String, Object> fields);

    AjaxResult delSceneDataById(String code, String id);

    AjaxResult syncSceneDataToAuto(String code, String changeTaskNo);

    AjaxResult completeByPhone(ECompleteVO eCompleteVO);

    AjaxResult processData(String changeNo);

    AjaxResult getButtonList(String currentId, String changeNo);

    AjaxResult tarnsFerTaskWaitImpl(String taskId, String changeTaskNo, CustomerFormContent customerFormContent, String transferMan);

    AjaxResult getSysDealList(String changeNo);

    AjaxResult getStartDeptMangerData(String changeNo);

    AjaxResult updateChangeInfoToAdpm(Map<String, Object> map);

    AjaxResult updateChangeTaskInfoToAdpm(Map<String, Object> map);

    List<Map<String, Object>> getFormField(String tableCode);
}
