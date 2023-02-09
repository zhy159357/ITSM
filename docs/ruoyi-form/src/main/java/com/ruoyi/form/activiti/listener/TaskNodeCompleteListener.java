package com.ruoyi.form.activiti.listener;

import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.ChangeUtil;
import com.ruoyi.form.domain.ImplRecord;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.service.IChangeService;
import com.ruoyi.form.service.IImplRecordService;
import com.ruoyi.system.service.IOgPersonService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("TaskNodeCompleteListener")
public class TaskNodeCompleteListener extends Base implements TaskListener, Serializable {

    @Autowired
    IChangeService changeService;
    @Autowired
    IOgPersonService ogPersonService;
    @Autowired
    IImplRecordService iImplRecordService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String businessKey = delegateTask.getVariable(BUSINESS_KEY).toString();
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        Object code = delegateTask.getVariable(RECODE);
        String currentAssignee = delegateTask.getAssignee();
        //节点审批后将下一节点的审批人更新到表里
        String approval = "";
        if(ChangeTaskDefineKeyEnum.submitTask.getName().equals(taskDefinitionKey)){
            updateChangeTaskStatus(ChangeTaskFieldEnum.ID,businessKey,ChangeTaskStatusEnum.planReviewed);
            addChangeTaskSysOperateDetail(businessKey,"变更任务的方案审核完成",currentAssignee);
        } else if (ChangeTaskDefineKeyEnum.taskPreApproval.getName().equals(taskDefinitionKey)) {
            Integer recode = Integer.parseInt(code.toString());
            //根据code传入下一节点的审批人
            String des = "";
            if (recode == 0) {
                //传入任务配合人
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.ASSIGNEE, businessKey);
                //更新任务预审退回标识为1，代表预审退回
                updateChangeTaskSingle(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskFieldEnum.PRE_APPROVAL_BACK_FLAG, "1");
                des = "预审不通过，任务被退回到"+getUsernameAndPname(approval);
            } else {
                //预审通过就传入
                //添加一条实施记录
                Map<String, Object> params = new HashMap<>();
                params.put(ChangeTaskFieldEnum.EXTRA1.getName(), "");
                params.put(ChangeTaskFieldEnum.APPROVAL.getName(), "");
                params.put("status", "");
                Map<String, String> query = new HashMap<>();
                query.put(ChangeTaskFieldEnum.ID.toString(), businessKey);
                Map<String, Object> resultMap = selectMap(ChangeTableNameEnum.CHANGE_TASK, params, query);
                String changeTaskNo = resultMap.get(ChangeTaskFieldEnum.EXTRA1.getName()).toString();
                String currentUserId = resultMap.get(ChangeTaskFieldEnum.APPROVAL.getName()).toString();
                //过滤变更任务
                if(changeTaskNo.contains("A")){
                    String status = resultMap.get("status").toString();
                    ImplRecord implRecord = new ImplRecord();
                    implRecord.setChangeTaskNo(changeTaskNo);
                    implRecord.setTaskStatus(status);
                    List<ImplRecord> list = iImplRecordService.selectImplRecordList(implRecord);
                    if (list.isEmpty()) {
                        implRecord.setUserid(currentUserId);
                        iImplRecordService.insertImplRecord(implRecord);
                    } else {
                        ImplRecord record = list.get(0);
                        if (!currentUserId.equals(record.getUserid())) {
                            record.setUserid(currentUserId);
                            iImplRecordService.updateImplRecord(record);
                        }
                    }
                }
                des = "任务预审通过";
            }
            addChangeTaskSysOperateDetail(businessKey,des,currentAssignee);
        } else if (ChangeTaskDefineKeyEnum.edit_task.getName().equals(taskDefinitionKey)) {
            approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.PRE_APPROVAL, businessKey);
            addChangeTaskSysOperateDetail(businessKey,"任务提交给"+getUsernameAndPname(approval)+"审批",currentAssignee);
        } else if ( ChangeTaskDefineKeyEnum.receive.getName().equals(taskDefinitionKey)
        ) {
            //传入实施人
            approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.IMPL_MAN, businessKey);
            //更新任务单实施开始时间
            String changeId = getChangeIdByTaskId(businessKey);
            String startDate = getChangeColumnValueBySingleCondition(ChangeFieldEnum.IMPLEMENT_START_DATE, ChangeFieldEnum.ID, changeId);
            List<Map<String, Object>> list = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.ID.getName(), businessKey);
            Map<String, Object> map = list.get(0);
            Object startTaskDate = map.get(ChangeTaskFieldEnum.IMPL_START_DATE.getName());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (startTaskDate == null || "".equals(startTaskDate.toString().trim())) {
                startTaskDate = dateTimeFormatter.format(now);
                Map<String, Object> updateParam = new HashMap<>();
                updateParam.put(ChangeTaskFieldEnum.IMPL_START_DATE.getName(), startTaskDate);
                Map<String, String> query = new HashMap<>();
                query.put(ChangeTaskFieldEnum.ID.getName(), businessKey);
                update(ChangeTableNameEnum.CHANGE_TASK, updateParam, query);
            }
            if (startDate == null || "".equals(startDate.trim())) {
                startDate = dateTimeFormatter.format(now);
                updateChangeSingle(ChangeFieldEnum.ID, changeId, ChangeFieldEnum.IMPLEMENT_START_DATE, startDate);
            }
            //同步任务单的最早时间
            updateChangePlanTime(changeId);
            addChangeTaskSysOperateDetail(businessKey,"已接单",currentAssignee);
        } else if (ChangeTaskDefineKeyEnum.remedyApproval.getName().equals(taskDefinitionKey)) {
            Integer recode = Integer.parseInt(code.toString());
            if (recode == 1) {
                //传入默认实施人（预审人）
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.PRE_APPROVAL, businessKey);
                //同步预审人为实施人
                updateChangeTaskSingle(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskFieldEnum.IMPL_MAN, approval);
            } else if (recode == 0) {
                //传入受派人
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.ASSIGNEE, businessKey);
                String des ="审批不通过，退回到"+getUsernameAndPname(approval);
                addChangeTaskSysOperateDetail(businessKey,des,currentAssignee);
            }
        } else if (ChangeTaskDefineKeyEnum.implement.getName().equals(taskDefinitionKey)) {
            //传入
            String des = "";
            Integer recode = Integer.parseInt(code.toString());
            if (recode == 1) {
                //复核人
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.CHECK_MAN, businessKey);
                updateChangeTaskSingle(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskFieldEnum.REVIEWED, "1");
                //更新任务单的实施结束时间
                String changeId = getChangeIdByTaskId(businessKey);
                String endTaskDate = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.IMPL_END_DATE, ChangeTaskFieldEnum.ID, businessKey);
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                if (endTaskDate == null || "".equals(endTaskDate.trim())) {
                    endTaskDate = dateTimeFormatter.format(now);
                    Map<String, Object> updateParam = new HashMap<>();
                    updateParam.put(ChangeTaskFieldEnum.IMPL_END_DATE.getName(), endTaskDate);
                    Map<String, String> query = new HashMap<>();
                    query.put(ChangeTaskFieldEnum.ID.getName(), businessKey);
                    update(ChangeTableNameEnum.CHANGE_TASK, updateParam, query);
                }
                String endDate = dateTimeFormatter.format(now);
                updateChangeSingle(ChangeFieldEnum.ID, changeId, ChangeFieldEnum.IMPLEMENT_OVER_DATE, endDate);
                String remedyFlagStr = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.REMEDY_FLAG, ChangeTaskFieldEnum.ID, businessKey);
                Integer remedyFlag = Integer.parseInt(remedyFlagStr);
                if (remedyFlag == 1) {
                    //是补救单的话，修改下原单的复核人和实施完成时间
                    syncRemedyCheckManToOldTask(businessKey,endTaskDate);
                }
                //更新任务单reviewed字段值为1，代表已进入过复核阶段
                //查一下所有的任务单的reviewed字段值是否都是1
                Boolean flag = checkAllTaskReviewedByChangeId(changeId);
                if (flag) {
                    updateChangeStatus(ChangeFieldEnum.ID, changeId, ChangeStatusEnum.completed);
                    //更新变更单的变更完成时间
                    updateChangeSingle(ChangeFieldEnum.ID, changeId, ChangeFieldEnum.CHANGE_FINISH_TIME, endDate);
                    //插入一条记录
                    addChangeSysOperateDetail(changeId, "所有任务都实施完成，变更单状态从变更实施变为已完成","sys");
                    List<Map<String,Object>> changeDataList = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE,ChangeFieldEnum.ID.getName(),changeId);
                    if(!changeDataList.isEmpty()){
                        Map<String,Object> changeData = changeDataList.get(0);
                        Object appProcessId = changeData.get(ChangeFieldEnum.APP_PROCESS_ID.getName());
                        if(appProcessId!=null&&!"".equals(appProcessId.toString().trim())){
                         try {
                             String instanceId = changeData.get(ChangeFieldEnum.INSTANCE_ID.getName()).toString();
                             ChangeUtil.ADPM_POOL.submit(instanceId,()->{
                                 updateAdpmChange(instanceId);
                             });
                         }catch (Exception e){
                             e.printStackTrace();
                         }
                        }
                    }
                }
                des = "实施完成";
            } else if (recode == 2) {
                //新建补救任务单的初始审批人还是实施人,也是受派人
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.ASSIGNEE, businessKey);
                des = "实施缺陷，新建了补救单";
            }
            addChangeTaskSysOperateDetail(businessKey,des,currentAssignee);
            /*"sid-EEDD767C-B4BF-44E1-8B95-9282D337E54C"*/
        } else if (ChangeTaskDefineKeyEnum.impl_check_update.getName().equals(taskDefinitionKey)) {
            approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.CHECK_MAN, businessKey);
            addChangeTaskSysOperateDetail(businessKey,"任务提交给"+getUsernameAndPname(approval)+"审批",currentAssignee);
        } else if (ChangeTaskDefineKeyEnum.review.getName().equals(taskDefinitionKey)) {
            Integer recode = Integer.parseInt(code.toString());
            if (recode == 0) {
                //传入实施人
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.IMPL_MAN, businessKey);
            }
        } else if (ChangeTaskDefineKeyEnum.remedySubmit.getName().equals(taskDefinitionKey)) {
            //补救单的预审人
            approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.PRE_APPROVAL, businessKey);
            addChangeTaskSysOperateDetail(businessKey,"补救单填写完成，提交给"+getUsernameAndPname(approval)+"审批",currentAssignee);
        } else if (ChangeTaskDefineKeyEnum.REMEDY_PRE_APPROVAL.getName().equals(taskDefinitionKey)) {
            String des = "";
            Integer recode = Integer.parseInt(code.toString());
            if (recode == 1) {
                approval = getTaskAssigneeGroupLeaderUserId(businessKey);
                des = "审核通过，提交给"+getUsernameAndPname(approval)+"审批";
            } else if (recode == 2) {
                //退回受派人
                approval = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.ASSIGNEE, businessKey);
                des = "审核不通过，退回给"+getUsernameAndPname(approval);
            }
            addChangeTaskSysOperateDetail(businessKey,des,currentAssignee);
        }
        updateChangeTaskApproval(ChangeTaskFieldEnum.ID, businessKey, approval);
    }
}
