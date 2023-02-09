package com.ruoyi.form.activiti.listener;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.enums.*;
import com.ruoyi.system.domain.OgReviewChangingDateConfig;
import com.ruoyi.system.service.IOgReviewChangingDateConfigService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;

@Service("ChangeExecutionListener")
public class ChangeExecutionListener extends Base implements ExecutionListener {

    @Autowired
    private IOgReviewChangingDateConfigService configService;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String businessKey = delegateExecution.getVariable(BUSINESS_KEY).toString();
        String taskDefinitionKey = delegateExecution.getCurrentActivityId();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateTimeFormatter.format(now);
        updateChangeSingle(ChangeFieldEnum.ID,businessKey,ChangeFieldEnum.UPDATE_TIME,nowDate);
        if (ChangeDefineKeyEnum.waitTaskPreOver.getName().equals(taskDefinitionKey)) {
            String approval = "";
            Integer recode = getPreRecode(businessKey);
            String des = "任务单全部预审通过，变更转入";
            if (recode == 1) {
                approval = getChangeMangerUserId();
                des = des + "变更经理" + getUsernameAndPname(approval) + "审批";
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.prepared);
            } else if (recode == 2) {
                des = des + "变更审批";
            } else if (recode == 3) {
                approval = getBranchChangeMangerUserId(getChangeTableStarterOrgId(businessKey));
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.branchManagerApproval);
                des = des + getUsernameAndPname(approval) + "审批";
            } else if (recode == 4) {
                //退回发起人
                approval = getChangeTableStarterUserId(businessKey);
                des = "变更单退回到发起人" + getUsernameAndPname(approval);
                updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.unSubmit);
            }
            //插入一条操作记录
            addChangeSysOperateDetail(businessKey, des, "sys");
            delegateExecution.setVariable(RECODE, recode);
            updateChangeApproval(ChangeFieldEnum.ID, businessKey, approval);
        } else if (ChangeDefineKeyEnum.waitOver.getName().equals(taskDefinitionKey)) {
            updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.implement);
            Map<String, Object> variables = new HashMap<>();
            variables.put(RECODE, 1);
            activeAllTask(businessKey, ChangeTaskDefineKeyEnum.changeManagerApproval.getName(), variables, ChangeTaskStatusEnum.waitImpl);
            String type = getChangeColumnValueBySingleCondition(ChangeFieldEnum.TYPE, ChangeFieldEnum.ID, businessKey);
            Integer typeFlag = Integer.parseInt(type);
            if (typeFlag == 3) {
                //获取评审日期与提前时间
                OgReviewChangingDateConfig dateConfig = configService.getOne(null);
                Integer hour = Integer.parseInt(dateConfig.getAheadOfHours());
                String weekDay = dateConfig.getDayOfWeek();
                DayOfWeek dayOfWeek = DayOfWeek.valueOf(weekDay);
                LocalDate today = LocalDate.now();
                LocalDate week = today.with(ChronoField.DAY_OF_WEEK, dayOfWeek.getValue());
                LocalDateTime approvalDate = LocalDateTime.of(week, LocalTime.of(0, 0, 0));
                approvalDate = approvalDate.minusHours(hour);
                String onTimeDate = dateTimeFormatter.format(approvalDate);
                updateChangeSingle(ChangeFieldEnum.ID, businessKey, ChangeFieldEnum.DATE_OF_SUBMIT_ON_TIME, onTimeDate);
                String submitDate = getChangeColumnValueBySingleCondition(ChangeFieldEnum.APPROVAL_SUBMIT_DATE, ChangeFieldEnum.ID, businessKey);
                LocalDateTime submitDateTime = LocalDateTime.parse(submitDate, dateTimeFormatter);
                String onTimeFlag = "超时";
                if (!submitDateTime.isAfter(approvalDate)) {
                    //设为按时提交
                    onTimeFlag = "按时";
                }
                updateChangeSingle(ChangeFieldEnum.ID, businessKey, ChangeFieldEnum.ONTIME, onTimeFlag);
            }
//            directorMap.remove(businessKey);
        }
    }
}
