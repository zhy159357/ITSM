package com.ruoyi.form.activiti.service;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.listener.ChangeNodeCompleteListener;
import com.ruoyi.form.enums.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service("ChangeLeaderChargeService")
public class ChangeLeaderChargeService extends Base implements JavaDelegate, Serializable {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
        //系统判断是否需要总经理审批
        String branchFlag = getChangeColumnValueBySingleCondition(ChangeFieldEnum.BRANCH_FLAG, ChangeFieldEnum.ID, businessKey);
        Integer recode = 1;
        String des = "";
        String approval = "";
        if ("1".equals(branchFlag)) {
            //如果是分行流程，则不需要总经理
            recode = 0;
            updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.implement);
            des = "变更进入实施中";
        } else {
            boolean flag = generalMangerApproval(businessKey);
            if (!flag) {
                recode = 0;
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.implement);
                des = "变更进入实施中";
            } else {
                //传入总经理
                approval = getGeneralManagerUserId();
                des = "变更流转到总经理" + getUsernameAndPname(approval) + "审批";
            }
        }
        updateChangeApproval(ChangeFieldEnum.ID, businessKey, approval);
        //删除掉并行审批用来判断是否通过的键值对
        addChangeSysOperateDetail(businessKey, des, "sys");
        ChangeNodeCompleteListener.removeMapByKey(businessKey);
        delegateExecution.setVariable(RECODE, recode);
    }
}
