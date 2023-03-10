package com.ruoyi.activiti.bmp.service.impl;

import com.ruoyi.activiti.bmp.service.IBmpService;
import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.activiti.service.impl.ActivitiCommServiceImpl;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysWorkService;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BmpServiceImpl implements IBmpService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private ISysWorkService sysWorkService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ActivitiCommServiceImpl activitiCommService;

    private static final Logger log = LoggerFactory.getLogger(BmpServiceImpl.class);

    @Override
    public synchronized void startProcess(BmpEntity bmpEntity) {
        if (StringUtils.isEmpty(bmpEntity) && StringUtils.isEmpty(bmpEntity.getProcessDefinitionKey()) || StringUtils.isEmpty(bmpEntity.getBusinessKey())) {
            throw new BusinessException("process start error cause:process start need params is null...");
        }
        String processDefinitionId = this.getProcessDefinitionId(bmpEntity.getProcessDefinitionKey(), bmpEntity.getProcessDefinitionVersion());
        if(StringUtils.isEmpty(processDefinitionId)) {
            log.info("????????????id???????????????????????????......");
            return;
        }
        bmpEntity.setProcessDefinitionId(processDefinitionId);
        Map<String, Object> variables = processParams(bmpEntity, null);
        // ?????????????????????????????????????????????
        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                .processDefinitionId(bmpEntity.getProcessDefinitionId())
                .processDefinitionKey(bmpEntity.getProcessDefinitionKey())
                .businessKey(bmpEntity.getBusinessKey()).variables(variables)
                .start();
        //ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, businessKey, variables);
        bmpEntity.setProcessInstanceId(processInstance.getProcessInstanceId());
        insertFlowLog(bmpEntity, "0");

        bmpEntity.setSuccessFlag(true);
        bmpEntity.setFlowMessage("??????????????????");
        log.info("?????????????????????businessKey:{},userId:{},processInstanceId:{}", bmpEntity.getBusinessKey(),bmpEntity.getUserId(),bmpEntity.getProcessInstanceId());
    }

    @Override
    public synchronized void complete(BmpEntity bmpEntity) {
        if (StringUtils.isEmpty(bmpEntity) || StringUtils.isEmpty(bmpEntity.getTaskId())
                || StringUtils.isEmpty(bmpEntity.getUserId())) {
            throw new BusinessException("process complete error cause:process complete need params is null...");
        }
        String taskId = bmpEntity.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (StringUtils.isEmpty(task)) {
            throw new BusinessException("process complete error cause:process is completed non repeatable...");
        }

        Map<String, Object> variables = processParams(bmpEntity, task);

        //????????????
        taskService.addComment(taskId, bmpEntity.getProcessInstanceId(), bmpEntity.getComment());
        // ?????????????????????????????????
        if (bmpEntity.isUsersFlag()) {
            //?????????????????????
            List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
            //???????????????????????????-????????????????????? ??????-?????????????????????????????????
            if (identityLinkList.size() == 1) {
                taskService.setAssignee(taskId, bmpEntity.getUserId());
            } else {
                //?????????????????? ??????
                taskService.deleteCandidateUser(taskId, bmpEntity.getUserId());
            }
        } else {
            taskService.setAssignee(taskId, bmpEntity.getUserId());
        }
        taskService.complete(taskId, variables);
        insertFlowLog(bmpEntity, "0");

        bmpEntity.setSuccessFlag(true);
        bmpEntity.setFlowMessage("??????????????????");
        log.info("???????????????????????????businessKey:{},taskId:{},taskName:{},userId:{},processInstanceId:{}", bmpEntity.getBusinessKey(),taskId,bmpEntity.getTaskName(),bmpEntity.getUserId(),bmpEntity.getProcessInstanceId());
    }

    @Override
    public void suspendProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    @Override
    public void activateProcessInstance(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Override
    public List<BmpEntity> getUserTask(BmpEntity bmpEntity) {
        List<Task> allTaskList = new ArrayList<>();
        // ???userId??????????????????????????????userId
        if(StringUtils.isEmpty(bmpEntity.getUserId())) {
            bmpEntity.setUserId(ShiroUtils.getUserId());
        }
        // ???????????????????????????
        if(bmpEntity.isGroupFlag()) {
            // ?????????????????????????????????list??????id
            List<String> groupsList = this.getGroupsList(bmpEntity.getUserId());
            if (StringUtils.isNotEmpty(bmpEntity.getDescription())) {
                allTaskList = taskService.createTaskQuery().taskCandidateGroupIn(groupsList).processDefinitionKey(bmpEntity.getProcessDefinitionKey()).taskDescription(bmpEntity.getDescription()).orderByTaskCreateTime().asc().list();
            } else {
                allTaskList = taskService.createTaskQuery().taskCandidateGroupIn(groupsList).processDefinitionKey(bmpEntity.getProcessDefinitionKey()).orderByTaskCreateTime().asc().list();
            }
        }
        allTaskList.addAll(getUserTaskByUserId(bmpEntity));
        return processList(allTaskList, bmpEntity.isGroupFlag());
    }

    private List<Task> getUserTaskByUserId(BmpEntity bmpEntity) {
        List<Task> all = new ArrayList<>();
        List<Task> assigneeList;
        List<Task> userList;
        if (StringUtils.isNotEmpty(bmpEntity.getDescription())) {
            // ???????????????
            assigneeList = taskService.createTaskQuery().taskAssignee(bmpEntity.getUserId()).processDefinitionKey(bmpEntity.getProcessDefinitionKey()).taskDescription(bmpEntity.getDescription()).orderByTaskCreateTime().asc().list();
            // ??????????????????
            userList = taskService.createTaskQuery().taskCandidateUser(bmpEntity.getUserId()).processDefinitionKey(bmpEntity.getProcessDefinitionKey()).taskDescription(bmpEntity.getDescription()).orderByTaskCreateTime().asc().list();
        } else {
            // ???????????????
            assigneeList = taskService.createTaskQuery().taskAssignee(bmpEntity.getUserId()).processDefinitionKey(bmpEntity.getProcessDefinitionKey()).orderByTaskCreateTime().asc().list();
            // ?????????????????????
            userList = taskService.createTaskQuery().taskCandidateUser(bmpEntity.getUserId()).processDefinitionKey(bmpEntity.getProcessDefinitionKey()).orderByTaskCreateTime().asc().list();
        }
        all.addAll(assigneeList);
        all.addAll(userList);
        return all;
    }

    @Override
    public Long getUserTaskCount(String userId, String processDefinitionKey) {
        TaskQuery tq = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey);
        long total = tq.count();
        tq = taskService.createTaskQuery().taskCandidateUser(userId).processDefinitionKey(processDefinitionKey);
        total += tq.count();
        return total;
    }

    @Override
    public boolean isExitProcess(String businessKey) {
        List<ProcessInstance> processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
        return !CollectionUtils.isEmpty(processInstance);
    }

    /**
     * ????????????
     *
     * @param id
     * @param closeReason
     */
    @Override
    public void closeProcess(String id, String closeReason) {
        activitiCommService.deleteProcess(id, "????????????");
    }

    @Override
    public void cancelProcess(String id) {
        activitiCommService.deleteProcess(id, "????????????");
    }

    /**
     * ??????????????????id???????????????key???????????????version???????????????????????????????????????
     */
    public String getProcessDefinitionId(String processDefinitionKey, Integer version) {
        if(StringUtils.isEmpty(processDefinitionKey)) {
            throw new BusinessException("method getProcessDefinitionId error cause:need parameter {processDefinitionKey} is null...");
        }
        ProcessDefinition processDefinition = null;
        if(null == version) {
            processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        } else {
            processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).processDefinitionVersion(version).singleResult();
        }
        String processDefinitionId = "";
        if(StringUtils.isNotEmpty(processDefinition)) {
            processDefinitionId = processDefinition.getId();
        }
        return processDefinitionId;
    }

    /**
     * ??????task???????????????????????????????????????????????????
     * @param bmpEntity
     * @param task
     * @return
     */
    public Map<String, Object> processParams(BmpEntity bmpEntity, Task task) {
        // task?????????????????????????????????
        if (task != null) {
            String processInstancedId = task.getProcessInstanceId();
            String processDefinitionId = task.getProcessDefinitionId();
            String taskName = task.getName();
            String description = task.getDescription();
            bmpEntity.setProcessInstanceId(processInstancedId);
            if(StringUtils.isEmpty(bmpEntity.getProcessDefinitionId())) {
                bmpEntity.setProcessDefinitionId(processDefinitionId);
            }
            bmpEntity.setTaskName(taskName);
            bmpEntity.setDescription(description);
        } else {
            bmpEntity.setComment("??????");
            bmpEntity.setTaskName("????????????");
        }

        // ??????????????????map
        bmpEntity.flowConditionMap();
        // ????????????????????????map
        bmpEntity.flowApprovalMap();
        return bmpEntity.getProcessVariables();
    }

    /**
     * ????????????id????????????id?????????id????????????????????????id
     * @param userId
     * @return
     */
    public List<String> getGroupsList(String userId) {
        List<String> reList = new ArrayList<>();
        // ???????????????
        List<OgGroup> ogGroups = sysWorkService.selectGroupByUserId(userId);
        // ????????????
        List<OgRole> ogRoles = sysRoleService.selectRolesByUserId(userId);
        // ??????
        OgPerson ogPerson = ogPersonService.selectOgPersonEvoById(userId);
        if(StringUtils.isNotEmpty(ogPerson)) {
            reList.add(ogPerson.getOrgId());
        }
        if(!CollectionUtils.isEmpty(ogGroups)) {
            List<String> groupIdList = ogGroups.stream().map(group -> {
                return group.getGroupId();
            }).collect(Collectors.toList());
            reList.addAll(groupIdList);
        }
        if(!CollectionUtils.isEmpty(ogRoles)) {
            List<String> rIdList = ogRoles.stream().map(role -> {
                return role.getRid();
            }).collect(Collectors.toList());
            reList.addAll(rIdList);
        }
        return reList;
    }

    /**
     * ??????task????????????????????????????????????
     * @param taskList
     * @param isGroup
     * @return
     */
    public List<BmpEntity> processList(List<Task> taskList, boolean isGroup) {
        List<BmpEntity> reList = new ArrayList<>();
        ProcessInstanceQuery pi = runtimeService.createProcessInstanceQuery();
        for (Task task : taskList) {
            ProcessInstance pro = pi.processInstanceId(task.getProcessInstanceId()).singleResult();
            if(StringUtils.isEmpty(pro.getBusinessKey()) || StringUtils.isEmpty(task.getId())) {
                continue;
            }
            BmpEntity bmpEntity = new BmpEntity();
            // ??????????????????????????????id  businessKey
            bmpEntity.setBusinessKey(pro.getBusinessKey());
            //??????????????????
            bmpEntity.setStartTime(pro.getStartTime());
            //????????????????????????
            bmpEntity.setCreateTime(task.getCreateTime());
            //????????????
            bmpEntity.setProcessName(pro.getName());
            //??????id
            bmpEntity.setProcessInstanceId(task.getProcessInstanceId());
            //?????????????????????????????????????????????
            bmpEntity.setProcessVariables(pro.getProcessVariables());
            //???????????????
            bmpEntity.setStartUser(pro.getStartUserId());
            //????????????????????????
            bmpEntity.setTaskName(task.getName());
            //taskId ??????????????????id ?????????????????????????????????
            bmpEntity.setTaskId(task.getId());
            //????????????
            bmpEntity.setDescription(task.getDescription());
            //???????????????
            if (isGroup) {
                bmpEntity.setGroupFlag(true);
            } else {
                //1 ???????????????
                bmpEntity.setGroupFlag(false);
            }
            reList.add(bmpEntity);
        }
        return reList;
    }

    public void insertFlowLog(BmpEntity bmpEntity, String claiTime) {
        long start = System.currentTimeMillis();
        log.debug("---------???????????????????????? processDefinitionKey:{}, businessKey:{}", bmpEntity.getProcessDefinitionKey(), bmpEntity.getBusinessKey());
        int count = pubFlowLogService.selectPubFlowLogByBusinessKey(bmpEntity.getBusinessKey());
        //????????????
        PubFlowLog pubFlowLog = new PubFlowLog();
        //??????id
        pubFlowLog.setLogId(IdUtils.randomUUID());
        //??????id
        pubFlowLog.setLogType(bmpEntity.getProcessDefinitionKey());
        //businessKey
        pubFlowLog.setBizId(bmpEntity.getBusinessKey());

        //???????????????
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(bmpEntity.getProcessInstanceId()).list();
        StringBuffer users = new StringBuffer();
        StringBuffer usersPhone = new StringBuffer();
        StringBuffer nextTaskId = new StringBuffer();
        StringBuffer nextTaskName = new StringBuffer();
        //????????????
        String deal = activitiCommService.getFlowTransitions(bmpEntity.getTaskId(), bmpEntity.getProcessDefinitionId(), bmpEntity.getProcessInstanceId());
        if (bmpEntity.getTaskName().equals("????????????")) {
            deal = "????????????";
        }
        if (bmpEntity.getTaskName().equals("??????")) {
            deal = "??????";
        }
        if (taskList.size() < 1) {
            nextTaskName.append("????????????:");
        } else {
            for (Task task : taskList) {
                nextTaskId.append(task.getId() + ":");
                nextTaskName.append(task.getName() + ":");
                //?????????????????????
                List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
                for (IdentityLink id : identityLinkList) {
                    String userId = id.getUserId();
                    if (StringUtils.isNotEmpty(userId)) {
                        OgPerson ogPerson = ogPersonService.selectOgPersonEvoById(userId);
                        users.append(ogPerson.getpId() + ",");
                        if (StringUtils.isEmpty(ogPerson.getPhone())) {
                            usersPhone.append(ogPerson.getpName() + "-" + ogPerson.getMobilPhone() + ",");
                        } else {
                            usersPhone.append(ogPerson.getpName() + "-" + ogPerson.getPhone() + ",");
                        }

                    } else {
                        //??????????????????
                        String group = id.getGroupId();
                        if (StringUtils.isNotEmpty(group) && group.contains("_")) {
                            group = group.substring(0, group.indexOf("_"));
                        }
                        //?????????
                        OgGroup OgGroup = sysWorkService.selectOgGroupById(group);
                        //?????? 21
                        OgRole ogRole = sysRoleService.selectRoleById(group);
                        //??????
                        OgOrg OgOrg = sysDeptService.selectDeptById(group);

                        String ogGp = OgGroup == null ? "" : OgGroup.getGroupId();
                        String ogRl = ogRole == null ? "" : ogRole.getRid();
                        String org = OgOrg == null ? "" : OgOrg.getOrgId();

                        users.append(ogGp + ogRl + org + ",");
                    }
                }
                if (!StringUtils.isEmpty(users)) {
                    users = new StringBuffer(users.substring(0, users.length() - 1));
                    users.append(":");
                }
                if (!StringUtils.isEmpty(usersPhone)) {
                    usersPhone = new StringBuffer(usersPhone.substring(0, usersPhone.length() - 1));
                    usersPhone.append(":");
                }
            }

        }
        //?????????
        pubFlowLog.setTaskName(bmpEntity.getTaskName());
        OgPerson op;
        if (StringUtils.isNotEmpty(bmpEntity.getUserId())) {
            op = ogPersonService.selectOgPersonEvoById(bmpEntity.getUserId());
        } else {
            op = ogPersonService.selectOgPersonEvoById(ShiroUtils.getUserId());
        }
        //???????????????id
        pubFlowLog.setNextTaskId(nextTaskId.toString());
        //??????????????????
        String nextTName = nextTaskName.toString();
        nextTName = nextTName.substring(0, nextTName.length() - 1);
        pubFlowLog.setNextTaskName(nextTName);
        //??????????????????
        String failUsers = "";
        if (users.indexOf(":") > 0) {
            failUsers = users.toString().substring(0, users.length() - 1);
        } else {
            failUsers = users.toString();
        }
        pubFlowLog.setNextPerformerDesc(failUsers);
        //????????????????????????
        if (usersPhone.length() > 3000) {
            String up = usersPhone.toString().substring(0, 3000);
            pubFlowLog.setNextPerformerTel(up + "...");
        } else {
            pubFlowLog.setNextPerformerTel(usersPhone.toString());
        }
        //??????
        pubFlowLog.setPerformDesc(bmpEntity.getComment());
        //?????????Id
        pubFlowLog.setPerformerId(op.getpId());
        //???????????????
        pubFlowLog.setPerformerName(op.getpName());
        //????????????
        pubFlowLog.setPerformName(deal);
        //????????????
        pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());

        pubFlowLog.setPerformerOrgId(op.getOrgId());
        //??????????????????
        pubFlowLog.setPerformerOrgName(op.getOrgname());
        //????????????
        if (StringUtils.isNotEmpty(bmpEntity.getGroupId())) {
            OgGroup OgGroup = sysWorkService.selectOgGroupById(bmpEntity.getGroupId());
            if (OgGroup != null) {
                pubFlowLog.setPerformerGroupId(OgGroup.getGroupId());
                //????????????
                pubFlowLog.setPerformerGroupName(OgGroup.getGrpName());
            }
        }
        //17  ????????????
        if (StringUtils.isEmpty(op.getPhone())) {
            pubFlowLog.setPerformerTel(op.getMobilPhone());
        } else {
            pubFlowLog.setPerformerTel(op.getPhone());
        }
        //???????????????
        pubFlowLog.setSysResidenceTime(claiTime);
        //??????
        pubFlowLog.setCurrentState("");
        pubFlowLog.setSerialNum(String.valueOf(count + 1));
        pubFlowLog.setLogId(IdUtils.fastSimpleUUID());
        pubFlowLogService.insertPubFlowLog(pubFlowLog);
        long end = System.currentTimeMillis();
        log.debug("---------???????????????????????????processDefinitionKey=" + bmpEntity.getProcessDefinitionKey() + ",??????businessKey=" + bmpEntity.getBusinessKey() + ",?????????:" + (end - start) + "??????");
    }
}
