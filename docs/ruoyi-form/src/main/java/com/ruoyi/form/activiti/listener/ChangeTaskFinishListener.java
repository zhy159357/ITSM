package com.ruoyi.form.activiti.listener;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.ChangeUtil;
import com.ruoyi.form.enums.*;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component("ChangeTaskFinishListener")
public class ChangeTaskFinishListener extends Base implements ExecutionListener, Serializable {

    @Autowired
    TaskService taskService;


    @Override
    public void notify(DelegateExecution delegateExecution) {
        //结束（每个任务单先改变自己的状态，然后去查看自己是否时最后一个已关闭的任务单，如果是的话就去审批变更单的指定节点）
        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
        String changId = getChangeIdByTaskId(businessKey);
        updateChangeTaskStatus(ChangeTaskFieldEnum.ID , businessKey, ChangeTaskStatusEnum.closed);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = dateTimeFormatter.format(now);
        Map<String,Object> updateParam = new HashMap<>();
        updateParam.put(ChangeTaskFieldEnum.IMPL_OVER_DATE.getName(), nowStr);
        updateParam.put(ChangeTaskFieldEnum.UPDATED_TIME.getName(), nowStr);
        Map<String, String> query = new HashMap<>();
        query.put(ChangeTaskFieldEnum.ID.getName(), businessKey);
        update(ChangeTableNameEnum.CHANGE_TASK, updateParam, query);
        //是补救单的话，修改原单的状态为已关闭
        String remedyFlagStr = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.REMEDY_FLAG,ChangeTaskFieldEnum.ID,businessKey);
        Integer remedyFlag = Integer.parseInt(remedyFlagStr);
        if(remedyFlag==1){
            closeRemedyOldTask(businessKey,nowStr);
        }
        //结束所有并包单子的流程并更新实施结果
        updateMergeTask(businessKey);
        updateChangeTaskApproval(ChangeTaskFieldEnum.ID,businessKey,"");
        addChangeTaskSysOperateDetail(businessKey,"任务单关闭了","sys");
        if (taskAllPassed(changId, ChangeTaskStatusEnum.closed)) {
            String instanceId = getChangeInstanceIdByChangeId(changId);
            activeWaitImplChange(instanceId);
            updateChangeStatus(ChangeFieldEnum.ID, changId, ChangeStatusEnum.closed);
        }
        String instanceId = delegateExecution.getProcessInstanceId();
        ChangeUtil.ADPM_POOL.submit(instanceId,()->{
            updateAdpmTask(instanceId);
        });
        ChangeUtil.ADPM_POOL.shutDown(instanceId);
    }
}
