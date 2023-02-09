package com.ruoyi.form.activiti.service;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.enums.*;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service("TaskSourceApi")
public class TaskSourceApi extends Base implements JavaDelegate, Serializable {
	
	@Autowired
    private TaskService taskService;
    @Override
    public void execute(DelegateExecution delegateExecution) {
    	 Task task = taskService.createTaskQuery().processInstanceId(delegateExecution.getProcessInstanceId()).singleResult();
        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
        String des = "";
        delegateExecution.setVariable(RECODE, 0);
        des = "进入实施中";
        updateStatus(ChangeFieldEnum.ID, businessKey, "0","it_entegor_data");
        addChangeSysOperateDetail(businessKey, des, "sys");
    }
}
