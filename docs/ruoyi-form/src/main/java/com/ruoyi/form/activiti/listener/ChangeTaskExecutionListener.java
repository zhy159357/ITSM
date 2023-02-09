package com.ruoyi.form.activiti.listener;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.enums.ChangeTaskDefineKeyEnum;
import com.ruoyi.form.enums.ChangeTaskFieldEnum;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service("ChangeTaskExecutionListener")
public class ChangeTaskExecutionListener extends Base implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
        String taskDefinitionKey = delegateExecution.getCurrentActivityId();
        Object recodeObj = delegateExecution.getVariable(RECODE);
        String approval = "";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateTimeFormatter.format(now);
        updateChangeTaskSingle(ChangeTaskFieldEnum.ID,businessKey,ChangeTaskFieldEnum.UPDATED_TIME,nowDate);
        if (ChangeTaskDefineKeyEnum.waitApproval.getName().equals(taskDefinitionKey)) {
            //传入预审人
            approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.PRE_APPROVAL, businessKey);
            updateChangeTaskApproval(ChangeTaskFieldEnum.ID, businessKey, approval);
        } else if (ChangeTaskDefineKeyEnum.changeManagerApproval.getName().equals(taskDefinitionKey)) {
            Integer recode = Integer.parseInt(recodeObj.toString());
            if(recode == 0){
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.ASSIGNEE, businessKey);
            } else if (recode == 1) {
                //传入默认实施人（预审人）
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.PRE_APPROVAL, businessKey);
                //同步预审人为实施人
                updateChangeTaskSingle(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskFieldEnum.IMPL_MAN, approval);
            } else if (recode == 2) {
                //传入任务预审人
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.PRE_APPROVAL, businessKey);
            }
            updateChangeTaskApproval(ChangeTaskFieldEnum.ID, businessKey, approval);
        }
    }
}
