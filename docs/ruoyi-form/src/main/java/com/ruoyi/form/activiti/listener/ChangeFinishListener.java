package com.ruoyi.form.activiti.listener;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.ChangeUtil;
import com.ruoyi.form.enums.ChangeFieldEnum;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("ChangeFinishListener")
public class ChangeFinishListener extends Base implements ExecutionListener, Serializable {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        String changeId = delegateExecution.getVariable(BUSINESS_KEY).toString();
        setClosedCodeAndStatus(changeId);
        addChangeSysOperateDetail(changeId,"所有任务单都关闭了，变更单状态从已完成变成已关闭","sys");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateTimeFormatter.format(now);
        updateChangeSingle(ChangeFieldEnum.ID,changeId,ChangeFieldEnum.CHANGE_CLOSED_TIME,nowDate);
        updateChangeSingle(ChangeFieldEnum.ID,changeId,ChangeFieldEnum.UPDATE_TIME,nowDate);
        String instanceId = delegateExecution.getProcessInstanceId();
        ChangeUtil.ADPM_POOL.submit(instanceId,()->{
            updateAdpmChange(instanceId);
        });
        ChangeUtil.ADPM_POOL.shutDown(instanceId);
    }
}
