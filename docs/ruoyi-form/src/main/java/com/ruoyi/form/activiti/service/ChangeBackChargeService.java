package com.ruoyi.form.activiti.service;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.listener.ChangeNodeCompleteListener;
import com.ruoyi.form.enums.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service("ChangeBackChargeService")
public class ChangeBackChargeService extends Base implements JavaDelegate, Serializable {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        //判断是退回到变更经理还是发起人，并且审批所有任务单的对应节点
        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
        Integer recode;
        ChangeStatusEnum changeStatus = ChangeStatusEnum.prepared;
        String approval = getChangeMangerUserId();
        String msg = "";
        Integer preRecode = getPreRecode(businessKey);
        if (preRecode == 3) {
            recode = 2;
            delegateExecution.setVariable(RECODE, recode);
            msg = "总行变更经理";
        } else {
            Map<String, Object> variables = new HashMap<>();
            if (preRecode == 1) {
                recode = 1;
                variables.put(RECODE, recode);
                delegateExecution.setVariable(RECODE, recode);
                msg = "变更经理";
            } else if (preRecode == 2) {
                recode = 0;
                changeStatus = ChangeStatusEnum.unSubmit;
                //发起人
                approval = getChangeTableStarterUserId(businessKey);
                msg = "发起人";
                addOneOverSize(businessKey);
                variables.put(RECODE, recode);
                delegateExecution.setVariable(RECODE, recode);
                activeAllTask(businessKey, ChangeTaskDefineKeyEnum.changeManagerApproval.getName(), variables, ChangeTaskStatusEnum.registered);
            }
        }
        String des = "变更被退回到" + msg + getUsernameAndPname(approval);
        addChangeSysOperateDetail(businessKey, des, "sys");
        updateChangeStatus(ChangeFieldEnum.ID, businessKey, changeStatus);
        //删除掉并行审批用来判断是否通过的键值对
        ChangeNodeCompleteListener.removeMapByKey(businessKey);
        updateChangeApproval(ChangeFieldEnum.ID, businessKey, approval);
    }
}
