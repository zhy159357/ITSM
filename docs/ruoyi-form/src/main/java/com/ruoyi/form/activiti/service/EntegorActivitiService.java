package com.ruoyi.form.activiti.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.mapper.CustomerFormMapper;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Service("EntegorActivitiApi")
public class EntegorActivitiService extends Base implements JavaDelegate, Serializable {
	@Autowired
    private CustomerFormMapper customerFormMapper;//SpringUtils.getBean(CustomerFormMapper.class);
    @Override
    public void execute(DelegateExecution delegateExecution) {
//        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
    	 DelegateExecution parent=delegateExecution.getParent();
         String businessKeys= (String) parent.getProcessInstanceBusinessKey();
         String[] lens=businessKeys.split("_");
         String businessKey=businessKeys.split("_")[lens.length-1];
         Map<String, Object> incidentFieldMap = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query()
                 .select(RedundancyFieldEnum.extra1.name)
                 .eq("id", businessKey)).get(0);
        String des = "";
        delegateExecution.setVariable(RECODE, 0);
        des = "进入实施中";
//        updateStatus(ChangeFieldEnum.ID, businessKey, "0","it_entegor_data");
//        addChangeSysOperateDetail(businessKey, des, "sys");
    }
}
