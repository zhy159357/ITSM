package com.ruoyi.form.activiti.service;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.enums.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service("ChangeBranchChargeService")
public class ChangeBranchChargeService extends Base implements JavaDelegate, Serializable {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
        String changeLevel = getChangeColumnValueBySingleCondition(ChangeFieldEnum.CHANGE_LEVLE, ChangeFieldEnum.ID, businessKey);
        String des = "";
        if ("1".equals(changeLevel) || "2".equals(changeLevel)) {
            delegateExecution.setVariable(RECODE, 1);
            String manager = getChangeMangerUserId();
            updateChangeApproval(ChangeFieldEnum.ID, businessKey, manager);
            des = "变更流转到总行变更经理" + getUsernameAndPname(manager) + "审批";
            updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.prepared);
        } else {
            delegateExecution.setVariable(RECODE, 0);
            des = "变更进入实施中";
            updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.implement);
        }
        addChangeSysOperateDetail(businessKey, des, "sys");
    }
}
