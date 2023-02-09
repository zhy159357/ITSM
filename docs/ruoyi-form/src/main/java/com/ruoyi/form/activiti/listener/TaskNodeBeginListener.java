package com.ruoyi.form.activiti.listener;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.ChangeUtil;
import com.ruoyi.form.enums.*;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component("TaskNodeBeginListener")
public class TaskNodeBeginListener extends Base implements TaskListener, Serializable {

    @Autowired
    TaskService taskService;

    @Override
    public void notify(DelegateTask delegateTask) {
        //在节点开始时从数据库里把候选人传入
        String businessKey = delegateTask.getVariable(BUSINESS_KEY).toString();
        Map<String,Object> map = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK,ChangeTaskFieldEnum.ID.getName(),businessKey).get(0);
        String approval = map.get(ChangeTaskFieldEnum.APPROVAL.getName()).toString();
        delegateTask.setAssignee(approval);
    }
}
