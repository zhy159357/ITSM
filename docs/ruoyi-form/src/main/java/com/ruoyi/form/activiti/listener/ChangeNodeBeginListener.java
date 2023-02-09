package com.ruoyi.form.activiti.listener;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.ChangeUtil;
import com.ruoyi.form.enums.*;
import com.ruoyi.system.domain.OgDeputyCfg;
import com.ruoyi.system.service.IOgDeputyCfgService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component("ChangeNodeBeginListener")
public class ChangeNodeBeginListener extends Base implements TaskListener, Serializable {

    @Autowired
    TaskService taskService;
    @Autowired
    IOgDeputyCfgService ogDeputyCfgService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String businessKey = delegateTask.getVariable(BUSINESS_KEY).toString();
        String taskId = delegateTask.getTaskDefinitionKey();
        //在节点开始时从数据库里把候选人传入
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateTimeFormatter.format(now);
        if (ChangeDefineKeyEnum.changeMangerPrepared.getName().equals(taskId)) {
            //先查一下是否是评审变更
            String type = getChangeColumnValueBySingleCondition(ChangeFieldEnum.TYPE, ChangeFieldEnum.ID, businessKey);
            Integer typeFlag = Integer.parseInt(type);
            if (typeFlag == 3) {
                //记录下当前日期
                updateChangeSingle(ChangeFieldEnum.ID, businessKey, ChangeFieldEnum.APPROVAL_SUBMIT_DATE, nowDate);
            }
        }else if (ChangeDefineKeyEnum.submit.getName().equals(taskId)) {
            //清空风险评估项
            clearChangeRisk(businessKey);
        }
        if(!ChangeDefineKeyEnum.adminApproval.getName().equals(taskId)){
            String approval = getChangeColumnValueBySingleCondition(ChangeFieldEnum.APPROVAL, ChangeFieldEnum.ID, businessKey);
            delegateTask.addCandidateUser(approval);
            OgDeputyCfg ogDeputyCfg = new OgDeputyCfg();
            ogDeputyCfg.setDirector(approval);
            ogDeputyCfg.setStatus("0");
            ogDeputyCfg.setCfgType(2L);
            List<OgDeputyCfg> list = ogDeputyCfgService.selectOgDeputyCfgList(ogDeputyCfg);
            if(!list.isEmpty()){
                OgDeputyCfg old = list.get(0);
                String startTime = old.getStartTime();
                String endTime = old.getEndTime();
                LocalDateTime start = LocalDateTime.parse(startTime,dateTimeFormatter);
                LocalDateTime end = LocalDateTime.parse(endTime,dateTimeFormatter);
                if(start.isBefore(now)&&end.isAfter(now)){
                    String sec = old.getSecondaryId();
                    delegateTask.addCandidateUser(sec);
                    List<String> users = new ArrayList<>();
                    users.add(approval);
                    users.add(sec);
                    updateAdminCurrentProcessor(users,businessKey);
                }
            }
        }
    }
}

