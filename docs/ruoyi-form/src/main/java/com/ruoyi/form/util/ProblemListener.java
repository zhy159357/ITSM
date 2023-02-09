package com.ruoyi.form.util;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * create by:hecaili
 * Date:2022/8/26 15:48
 */

@Component("problemListener")
public class ProblemListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        String generalManagerIds = String.valueOf(delegateExecution.getVariable("generalManagerIds"));
        if (StringUtils.isNotBlank(generalManagerIds)) {
            delegateExecution.setVariable("generalManagerIds", Arrays.asList(generalManagerIds.split(",")));
        }
    }
}
