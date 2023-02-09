package com.ruoyi.form.activiti.listener;


import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.service.OperationDetailsService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("ChangeNodeCompleteListener")
public class ChangeNodeCompleteListener extends Base implements TaskListener, Serializable {
    public static Map<String, Object> map = new HashMap<>();
    @Autowired
    OperationDetailsService operationDetailsService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String businessKey = delegateTask.getVariable(BUSINESS_KEY).toString();
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String currentAssignee = delegateTask.getAssignee();
        String approval = "";
        if (ChangeDefineKeyEnum.submit.getName().equals(taskDefinitionKey)) {
            activeAllTask(businessKey, ChangeTaskDefineKeyEnum.waitApproval.getName(), null, ChangeTaskStatusEnum.preApprovaling);
            //如果是紧急变更，则额度减一
            delOneOverSize(businessKey);
            Integer recodeInt = 2;
            String branchFlag = getChangeColumnValueBySingleCondition(ChangeFieldEnum.BRANCH_FLAG, ChangeFieldEnum.ID, businessKey);
            if ("1".equals(branchFlag)) {
                recodeInt = 3;
            } else {
                Boolean flag = managerApproval(businessKey);
                //recode == 1-->需要变更经理审批
                if (flag) {
                    recodeInt = 1;
                }
            }
            String recodeStr = String.valueOf(recodeInt);
            updateChangeSingle(ChangeFieldEnum.ID, businessKey, ChangeFieldEnum.PRE_RECODE, recodeStr);
            updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.preApproval);
            addChangeSysOperateDetail(businessKey, "提交了变更单", currentAssignee);
        } else if (ChangeDefineKeyEnum.changeMangerPrepared.getName().equals(taskDefinitionKey)) {
            Integer recode = Integer.valueOf(delegateTask.getVariable(RECODE).toString());
            if (recode == 1) {
                //把变更经理传入
                approval = getChangeMangerUserId();
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.managerApproval);
            } else if (recode == 2) {
                approval = getChangeTableStarterUserId(businessKey);
                String des = "变更退回给" + getUsernameAndPname(approval);
                addChangeSysOperateDetail(businessKey, des, currentAssignee);
            }
        } else if (ChangeDefineKeyEnum.completion.getName().equals(taskDefinitionKey)) {
            //把变更经理传入
            approval = getChangeMangerUserId();
            addChangeSysOperateDetail(businessKey, "变更提交给变更经理" + getUsernameAndPname(approval) + "审批", currentAssignee);
        } else if (ChangeDefineKeyEnum.changeMangerApproval.getName().equals(taskDefinitionKey)) {
            //如果不是通过的话，那就返回给发起人
            Integer recode = Integer.valueOf(delegateTask.getVariable(RECODE).toString());
            if (recode == 0) {
                //传入发起人
                approval = getChangeTableStarterUserId(businessKey);
                addOneOverSize(businessKey);
                //审批所有需要退回发起的任务单
                Map<String, Object> param = new HashMap<>();
                Map<String, String> query = new HashMap<>();
                param.put("*", "");
                query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), businessKey);
                /* query.put(ChangeTaskFieldEnum.STATUS.getName(), ChangeTaskStatusEnum.preApprovalPassed.getName());*/
                query.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), "0");
                query.put(ChangeTaskFieldEnum.BACK_TO_START.getName(), "1");
                List<Map<String, Object>> list = selectListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
                Map<String, Object> variables = new HashMap<>();
                variables.put(RECODE, 0);
                activeTaskList(list, ChangeTaskDefineKeyEnum.changeManagerApproval.getName(), variables, ChangeTaskStatusEnum.registered);
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.unSubmit);
                String des = "变更退回给" + getUsernameAndPname(approval);
                addChangeSysOperateDetail(businessKey, des, currentAssignee);
            } else if (recode == 1) {
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.approval);
                //更新下所有任务单的预审退回标识和退回发起的标识为否
                Map<String, Object> param = new HashMap<>();
                Map<String, String> query = new HashMap<>();
                param.put(ChangeTaskFieldEnum.BACK_TO_START.getName(), "0");
                param.put(ChangeTaskFieldEnum.BACK_TO_APPROVAL_FLAG.getName(), "0");
                query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), businessKey);
                query.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), "0");
                update(ChangeTableNameEnum.CHANGE_TASK, param, query);
                String des = "变更单进入变更审批";
                addChangeSysOperateDetail(businessKey, des, "sys");
            } else if (recode == 2) {
                approval = getChangeTableStarterUserId(businessKey);
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.prepared);
                String des = "变更退回给" + getUsernameAndPname(approval);
                addChangeSysOperateDetail(businessKey, des, currentAssignee);
            } else if (recode == 3) {
                //获取所有需要退回预审并且是变更经理审批的任务，审批掉
                Map<String, Object> param = new HashMap<>();
                Map<String, String> query = new HashMap<>();
                param.put("*", "");
                query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), businessKey);
                query.put(ChangeTaskFieldEnum.STATUS.getName(), ChangeTaskStatusEnum.preApprovalPassed.getName());
                query.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), "0");
                query.put(ChangeTaskFieldEnum.BACK_TO_APPROVAL_FLAG.getName(), "1");
                List<Map<String, Object>> list = selectListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
                Map<String, Object> variables = new HashMap<>();
                variables.put(RECODE, 2);
                activeTaskList(list, ChangeTaskDefineKeyEnum.changeManagerApproval.getName(), variables, ChangeTaskStatusEnum.preApprovaling);
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.preApproval);
                String des = "变更被退回预审";
                addChangeSysOperateDetail(businessKey, des, currentAssignee);
            } else if (recode == 4) {
                approval = getChangeMangerUserId();
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.prepared);
            }
        } else if (ChangeDefineKeyEnum.branchManager.getName().equals(taskDefinitionKey)) {
            Integer recode = Integer.valueOf(delegateTask.getVariable(RECODE).toString());
            String des = "";
            if (recode == 0) {
                //退回发起人，并审批对应的任务单节点
                approval = getChangeTableStarterUserId(businessKey);
                Map<String, Object> variables = new HashMap<>();
                variables.put(RECODE, 0);
                activeAllTask(businessKey, ChangeTaskDefineKeyEnum.changeManagerApproval.getName(), variables, ChangeTaskStatusEnum.registered);
                addOneOverSize(businessKey);
                des = "变更被退回到" + getUsernameAndPname(approval);
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.unSubmit);
            } else if (recode == 1) {
                approval = getBranchGeneralMangerUserId(getChangeTableStarterOrgId(businessKey));
                des = "变更单流转到" + getUsernameAndPname(approval) + "审批";
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.branchGeneralApproval);
            }
            addChangeSysOperateDetail(businessKey, des, currentAssignee);
        } else if (ChangeDefineKeyEnum.branchGeneralManager.getName().equals(taskDefinitionKey)) {
            Integer recode = Integer.valueOf(delegateTask.getVariable(RECODE).toString());
            if (recode == 0) {
                approval = getChangeTableStarterUserId(businessKey);
                addOneOverSize(businessKey);
                Map<String, Object> variables = new HashMap<>();
                variables.put(RECODE, recode);
                activeAllTask(businessKey, ChangeTaskDefineKeyEnum.changeManagerApproval.getName(), variables, ChangeTaskStatusEnum.registered);
                addChangeSysOperateDetail(businessKey, "变更被退回到" + getUsernameAndPname(approval), currentAssignee);
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.unSubmit);
            }
        } else if (ChangeDefineKeyEnum.preparedApproval.getName().equals(taskDefinitionKey)) {
            approval = getChangeMangerUserId();
            updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.managerApproval);
            addChangeSysOperateDetail(businessKey, "变更流转到变更经理" + getUsernameAndPname(approval) + "审批", currentAssignee);
        } else if (ChangeDefineKeyEnum.changeMangerApprovalWhenBranch.getName().equals(taskDefinitionKey)) {
            Integer recode = Integer.valueOf(delegateTask.getVariable(RECODE).toString());
            if (recode == 0) {
                approval = getBranchGeneralMangerUserId(getChangeTableStarterOrgId(businessKey));
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.branchGeneralApproval);
                addChangeSysOperateDetail(businessKey, "变更被退回到分行总经理" + getUsernameAndPname(approval), currentAssignee);
            } else if (recode == 2) {
                approval = getChangeMangerUserId();
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.prepared);
            }
        }
        updateChangeApproval(ChangeFieldEnum.ID, businessKey, approval);
    }

    public static void removeMapByKey(String key) {
        map.remove(key);
    }
}
