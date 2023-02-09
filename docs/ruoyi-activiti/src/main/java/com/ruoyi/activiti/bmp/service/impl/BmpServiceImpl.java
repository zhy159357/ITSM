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
            log.info("流程定义id为空，启动流程失败......");
            return;
        }
        bmpEntity.setProcessDefinitionId(processDefinitionId);
        Map<String, Object> variables = processParams(bmpEntity, null);
        // 此处使用流程实例构造器启动流程
        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                .processDefinitionId(bmpEntity.getProcessDefinitionId())
                .processDefinitionKey(bmpEntity.getProcessDefinitionKey())
                .businessKey(bmpEntity.getBusinessKey()).variables(variables)
                .start();
        //ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, businessKey, variables);
        bmpEntity.setProcessInstanceId(processInstance.getProcessInstanceId());
        insertFlowLog(bmpEntity, "0");

        bmpEntity.setSuccessFlag(true);
        bmpEntity.setFlowMessage("流程启动成功");
        log.info("流程启动成功，businessKey:{},userId:{},processInstanceId:{}", bmpEntity.getBusinessKey(),bmpEntity.getUserId(),bmpEntity.getProcessInstanceId());
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

        //加入批注
        taskService.addComment(taskId, bmpEntity.getProcessInstanceId(), bmpEntity.getComment());
        // 区分多人任务和单人任务
        if (bmpEntity.isUsersFlag()) {
            //查询任务候选人
            List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
            //没有候选人或者驳回-节点任务即完成 否则-等待其他候选人完成任务
            if (identityLinkList.size() == 1) {
                taskService.setAssignee(taskId, bmpEntity.getUserId());
            } else {
                //候选人中删除 当前
                taskService.deleteCandidateUser(taskId, bmpEntity.getUserId());
            }
        } else {
            taskService.setAssignee(taskId, bmpEntity.getUserId());
        }
        taskService.complete(taskId, variables);
        insertFlowLog(bmpEntity, "0");

        bmpEntity.setSuccessFlag(true);
        bmpEntity.setFlowMessage("流程审批成功");
        log.info("流程执行流转成功，businessKey:{},taskId:{},taskName:{},userId:{},processInstanceId:{}", bmpEntity.getBusinessKey(),taskId,bmpEntity.getTaskName(),bmpEntity.getUserId(),bmpEntity.getProcessInstanceId());
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
        // 若userId为空，则取当前登录人userId
        if(StringUtils.isEmpty(bmpEntity.getUserId())) {
            bmpEntity.setUserId(ShiroUtils.getUserId());
        }
        // 区分是否查询组任务
        if(bmpEntity.isGroupFlag()) {
            // 组任务查询需要获取组的list集合id
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
            // 指定办理人
            assigneeList = taskService.createTaskQuery().taskAssignee(bmpEntity.getUserId()).processDefinitionKey(bmpEntity.getProcessDefinitionKey()).taskDescription(bmpEntity.getDescription()).orderByTaskCreateTime().asc().list();
            // 动态指定办理
            userList = taskService.createTaskQuery().taskCandidateUser(bmpEntity.getUserId()).processDefinitionKey(bmpEntity.getProcessDefinitionKey()).taskDescription(bmpEntity.getDescription()).orderByTaskCreateTime().asc().list();
        } else {
            // 指定办理人
            assigneeList = taskService.createTaskQuery().taskAssignee(bmpEntity.getUserId()).processDefinitionKey(bmpEntity.getProcessDefinitionKey()).orderByTaskCreateTime().asc().list();
            // 动态指定办理人
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
     * 关闭流程
     *
     * @param id
     * @param closeReason
     */
    @Override
    public void closeProcess(String id, String closeReason) {
        activitiCommService.deleteProcess(id, "关闭流程");
    }

    @Override
    public void cancelProcess(String id) {
        activitiCommService.deleteProcess(id, "撤销流程");
    }

    /**
     * 查询流程定义id的方法，若key为空报错，version为空默认查询最新的一个版本
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
     * 获取task相关信息及组装流程流转所需要的参数
     * @param bmpEntity
     * @param task
     * @return
     */
    public Map<String, Object> processParams(BmpEntity bmpEntity, Task task) {
        // task为空表明是启动流程环节
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
            bmpEntity.setComment("启动");
            bmpEntity.setTaskName("启动流程");
        }

        // 组装分支条件map
        bmpEntity.flowConditionMap();
        // 组装下一步处理人map
        bmpEntity.flowApprovalMap();
        return bmpEntity.getProcessVariables();
    }

    /**
     * 根据人员id获取机构id｜角色id集合｜工作组集合id
     * @param userId
     * @return
     */
    public List<String> getGroupsList(String userId) {
        List<String> reList = new ArrayList<>();
        // 获取工作组
        List<OgGroup> ogGroups = sysWorkService.selectGroupByUserId(userId);
        // 查询角色
        List<OgRole> ogRoles = sysRoleService.selectRolesByUserId(userId);
        // 机构
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
     * 根据task信息组装待办查询返回数据
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
            // 流程对应的业务表主键id  businessKey
            bmpEntity.setBusinessKey(pro.getBusinessKey());
            //流程开启时间
            bmpEntity.setStartTime(pro.getStartTime());
            //添加流程操作时间
            bmpEntity.setCreateTime(task.getCreateTime());
            //流程名称
            bmpEntity.setProcessName(pro.getName());
            //流程id
            bmpEntity.setProcessInstanceId(task.getProcessInstanceId());
            //流程变量，自定义放入的所有变量
            bmpEntity.setProcessVariables(pro.getProcessVariables());
            //流程发起人
            bmpEntity.setStartUser(pro.getStartUserId());
            //当前任务节点名称
            bmpEntity.setTaskName(task.getName());
            //taskId 当前节点任务id 认领任务完成任务必输项
            bmpEntity.setTaskId(task.getId());
            //任务描述
            bmpEntity.setDescription(task.getDescription());
            //是否组任务
            if (isGroup) {
                bmpEntity.setGroupFlag(true);
            } else {
                //1 不需要认领
                bmpEntity.setGroupFlag(false);
            }
            reList.add(bmpEntity);
        }
        return reList;
    }

    public void insertFlowLog(BmpEntity bmpEntity, String claiTime) {
        long start = System.currentTimeMillis();
        log.debug("---------流程记录插入开始 processDefinitionKey:{}, businessKey:{}", bmpEntity.getProcessDefinitionKey(), bmpEntity.getBusinessKey());
        int count = pubFlowLogService.selectPubFlowLogByBusinessKey(bmpEntity.getBusinessKey());
        //流程记录
        PubFlowLog pubFlowLog = new PubFlowLog();
        //日志id
        pubFlowLog.setLogId(IdUtils.randomUUID());
        //流程id
        pubFlowLog.setLogType(bmpEntity.getProcessDefinitionKey());
        //businessKey
        pubFlowLog.setBizId(bmpEntity.getBusinessKey());

        //下一步任务
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(bmpEntity.getProcessInstanceId()).list();
        StringBuffer users = new StringBuffer();
        StringBuffer usersPhone = new StringBuffer();
        StringBuffer nextTaskId = new StringBuffer();
        StringBuffer nextTaskName = new StringBuffer();
        //操作名称
        String deal = activitiCommService.getFlowTransitions(bmpEntity.getTaskId(), bmpEntity.getProcessDefinitionId(), bmpEntity.getProcessInstanceId());
        if (bmpEntity.getTaskName().equals("撤销流程")) {
            deal = "关闭流程";
        }
        if (bmpEntity.getTaskName().equals("撤回")) {
            deal = "撤回";
        }
        if (taskList.size() < 1) {
            nextTaskName.append("任务结束:");
        } else {
            for (Task task : taskList) {
                nextTaskId.append(task.getId() + ":");
                nextTaskName.append(task.getName() + ":");
                //查询任务候选人
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
                        //组或角色任务
                        String group = id.getGroupId();
                        if (StringUtils.isNotEmpty(group) && group.contains("_")) {
                            group = group.substring(0, group.indexOf("_"));
                        }
                        //查询组
                        OgGroup OgGroup = sysWorkService.selectOgGroupById(group);
                        //角色 21
                        OgRole ogRole = sysRoleService.selectRoleById(group);
                        //机构
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
        //任务名
        pubFlowLog.setTaskName(bmpEntity.getTaskName());
        OgPerson op;
        if (StringUtils.isNotEmpty(bmpEntity.getUserId())) {
            op = ogPersonService.selectOgPersonEvoById(bmpEntity.getUserId());
        } else {
            op = ogPersonService.selectOgPersonEvoById(ShiroUtils.getUserId());
        }
        //下一步任务id
        pubFlowLog.setNextTaskId(nextTaskId.toString());
        //下一步任务名
        String nextTName = nextTaskName.toString();
        nextTName = nextTName.substring(0, nextTName.length() - 1);
        pubFlowLog.setNextTaskName(nextTName);
        //下一步处理人
        String failUsers = "";
        if (users.indexOf(":") > 0) {
            failUsers = users.toString().substring(0, users.length() - 1);
        } else {
            failUsers = users.toString();
        }
        pubFlowLog.setNextPerformerDesc(failUsers);
        //下一步处理人电话
        if (usersPhone.length() > 3000) {
            String up = usersPhone.toString().substring(0, 3000);
            pubFlowLog.setNextPerformerTel(up + "...");
        } else {
            pubFlowLog.setNextPerformerTel(usersPhone.toString());
        }
        //备注
        pubFlowLog.setPerformDesc(bmpEntity.getComment());
        //操作人Id
        pubFlowLog.setPerformerId(op.getpId());
        //操作人名称
        pubFlowLog.setPerformerName(op.getpName());
        //操作名称
        pubFlowLog.setPerformName(deal);
        //操作时间
        pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());

        pubFlowLog.setPerformerOrgId(op.getOrgId());
        //用户机构名称
        pubFlowLog.setPerformerOrgName(op.getOrgname());
        //用户组名
        if (StringUtils.isNotEmpty(bmpEntity.getGroupId())) {
            OgGroup OgGroup = sysWorkService.selectOgGroupById(bmpEntity.getGroupId());
            if (OgGroup != null) {
                pubFlowLog.setPerformerGroupId(OgGroup.getGroupId());
                //用户组名
                pubFlowLog.setPerformerGroupName(OgGroup.getGrpName());
            }
        }
        //17  用户电话
        if (StringUtils.isEmpty(op.getPhone())) {
            pubFlowLog.setPerformerTel(op.getMobilPhone());
        } else {
            pubFlowLog.setPerformerTel(op.getPhone());
        }
        //处理用时间
        pubFlowLog.setSysResidenceTime(claiTime);
        //状态
        pubFlowLog.setCurrentState("");
        pubFlowLog.setSerialNum(String.valueOf(count + 1));
        pubFlowLog.setLogId(IdUtils.fastSimpleUUID());
        pubFlowLogService.insertPubFlowLog(pubFlowLog);
        long end = System.currentTimeMillis();
        log.debug("---------流程记录插入结束，processDefinitionKey=" + bmpEntity.getProcessDefinitionKey() + ",工单businessKey=" + bmpEntity.getBusinessKey() + ",总耗时:" + (end - start) + "毫秒");
    }
}
