package com.ruoyi.form.activiti.service;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.enums.ChangeTaskFieldEnum;
import com.ruoyi.form.enums.ChangeTaskStatusEnum;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service("ChangeTaskManagerService")
public class ChangeTaskManagerService extends Base implements JavaDelegate, Serializable {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
        String changeId = getChangeIdByTaskId(businessKey);
        updateChangeTaskStatus(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskStatusEnum.preApprovalPassed);
        //更新下任务单的预审退回标识为否
        updateChangeTaskSingle(ChangeTaskFieldEnum.ID,businessKey,ChangeTaskFieldEnum.BACK_TO_APPROVAL_FLAG,"0");
        //更新任务的BACK_TO_START标识为0
        updateChangeTaskSingle(ChangeTaskFieldEnum.ID,businessKey,ChangeTaskFieldEnum.BACK_TO_START,"0");
        if (taskAllPassed(changeId, ChangeTaskStatusEnum.preApprovalPassed)) {
            String instanceId = getChangeInstanceIdByChangeId(changeId);
            activeChangeSecPartByInstanceId(instanceId);
            //同步任务单的最早时间
            updateChangePlanTime(changeId);
        }
    }
}
