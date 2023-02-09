package com.ruoyi.form.activiti.service;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.enums.ChangeFieldEnum;
import com.ruoyi.form.enums.ChangeStatusEnum;
import com.ruoyi.system.domain.OgDeputyCfg;
import com.ruoyi.system.service.IOgDeputyCfgService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("ChangeAdminChargeService")
public class ChangeAdminChargeService extends Base implements JavaDelegate, Serializable {

    @Autowired
    IOgDeputyCfgService ogDeputyCfgService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
        List<String> users = getAdminUserList(businessKey);
        String branchFlag = getChangeColumnValueBySingleCondition(ChangeFieldEnum.BRANCH_FLAG, ChangeFieldEnum.ID, businessKey);
        if ("1".equals(branchFlag)) {
            //从参数里获取需要审批的人
            Object o = delegateExecution.getVariable(ADMIN_ORG_LIST);
            List<String> orgList = (List<String>) o;
            orgList = orgList.stream().distinct().collect(Collectors.toList());
            String changeLevel = getChangeColumnValueBySingleCondition(ChangeFieldEnum.CHANGE_LEVLE, ChangeFieldEnum.ID, businessKey);
            users = getHeadListByOrgList(orgList, false);
            if ("1".equals(changeLevel)) {
                //加上分管领导
                users = getHeadListByOrgList(orgList, true);
            }
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<String> userList = new ArrayList<>();
//        Map<String,Object> director = new HashMap<>();
        users.forEach(user -> {
            //把每一个user的代理人放进去
            OgDeputyCfg ogDeputyCfg = new OgDeputyCfg();
            ogDeputyCfg.setDirector(user);
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
                    userList.add(sec);
                    /*director.put(user,sec);
                    director.put(sec,user);*/
                }
            }
        });
        users.addAll(userList);
        users = users.stream().distinct().collect(Collectors.toList());
        //directorMap.put(businessKey,director);
        users = getChangeAdminDisFreeApprovalUserList(businessKey, users);
        if (users.isEmpty()) {
            delegateExecution.setVariable(RECODE, 1);
        }
        delegateExecution.setVariable(USERS, users);
        updateAdminCurrentProcessor(users, businessKey);
        updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.approval);
        String nowDate = dateTimeFormatter.format(now);
        updateChangeSingle(ChangeFieldEnum.ID,businessKey,ChangeFieldEnum.UPDATE_TIME,nowDate);
    }
}
