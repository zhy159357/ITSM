package com.ruoyi.activiti.bmp.listener.execution;

import com.ruoyi.common.annotation.ActivitiListener;
import com.ruoyi.common.enums.ListenerBusinessType;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("ActivitiListenerTest")
@ActivitiListener(listenerName = "测试监听器", listenerBusinessType = ListenerBusinessType.EVENT_BILL)
public class ActivitiListenerTest implements ExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(ActivitiListenerTest.class);

    @Override
    public void notify(DelegateExecution delegateExecution) {
        log.info("-----------工作流监听开始-------------");
        DelegateExecution parent = delegateExecution.getParent();
        String businessKey = parent.getProcessInstanceBusinessKey();
        Map<String, Object> variable = parent.getVariables();
        FlowElement currentFlowElement = delegateExecution.getCurrentFlowElement();
        // 任务名称
        String taskName = currentFlowElement.getName();

        log.info("获取工作流相关参数 businessKey:{},taskName:{}", businessKey, taskName);
        log.info("获取工作流相关参数 variable:{}", variable);
    }
}
