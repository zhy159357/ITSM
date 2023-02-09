package com.ruoyi.activiti.service.impl;

import java.awt.*;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.ruoyi.activiti.config.ICustomProcessDiagramGenerator;
import com.ruoyi.activiti.config.WorkflowConstants;
import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.domain.CmBizSingleSJVo;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.mapper.DutyAccountMapper;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.CmBizSingleSJService;
import com.ruoyi.activiti.service.EventRunService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.common.core.domain.entity.DutyAccount;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubHolidayService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysWorkService;
import com.ruoyi.system.service.OgPostService;
import com.ruoyi.system.service.impl.SysApplicationManagerServiceImpl;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * zx
 */
@Service
@Transactional
public class ActivitiCommServiceImpl implements ActivitiCommService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IPubFlowLogService iPubFlowLogService;
    @Autowired
    private ISysWorkService iSysWorkService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private OgPostService ogPostService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private IPubHolidayService iPubHolidayService;
    @Autowired
    private PubBizSmsServiceImpl pubBizSmsService;
    @Autowired
    private PubBizPresmsServiceImpl pubBizPresmsService;
    @Autowired
    private VmBizInfoServiceImpl vmBizInfoService;
    @Autowired
    private SysApplicationManagerServiceImpl applicationManagerService;
    @Autowired
    private DutyAccountMapper dutyAccountMapper;
    @Autowired
    private CmBizSingleSJService cmBizSingleSJService;
    @Autowired
    private IFmBizService fmBizService;
    @Autowired
    private EventRunService eventRunService;
    private static final Logger log = LoggerFactory.getLogger(ActivitiCommServiceImpl.class);

    /**
     * 是否定时任务全局变量
     * 主要考虑到定时任务处理流程没有人员信息
     */
    private boolean quartzFlag = false;

    /**
     * 流程启动
     * 页面指定的审批组，审批机构，审批人，互斥网关分流条件，监听事件的类型名等)
     * 返回map  中processInstanceId 为流程启动后得到的流程id
     *
     * @param businessKey          （流程中存储的业务ID 即业务表中主键Id）
     * @param processDefinitionKey （流程模板ID 指定启动模板对应的流程）
     * @param variables            3.variables(Map<String,Obeject> 格式，内容为流程变量，即流程模板中配置的${XXXXX} ,key=XXXXX value=自定义，默认全局变量
     * @return
     */
    @Override
    public Map<String, Object> startProcess(String businessKey, String processDefinitionKey, Map<String, Object> variables) {
        long start = System.currentTimeMillis();
        log.debug("---------流程处理类【startProcess】方法调用开始，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey);
        String userId = "";
        String reCode = "";
        String status = "";
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.得到RunService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //开始流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("processInstanceId", processInstance.getProcessInstanceId());
        if (StringUtils.isNotNull(variables.get("userId"))) {
            userId = variables.get("userId").toString();
        }
        if (StringUtils.isNotNull(variables.get("reCode"))) {
            reCode = variables.get("reCode").toString();
        }
        if (StringUtils.isNotNull(variables.get("status"))) {
            status = variables.get("status").toString();
        }
        // 针对软研提交启动流程设置版本单状态
        if (StringUtils.isNotNull(variables.get("versionStatus"))) {
            status = variables.get("versionStatus").toString();
        }
        //流程记录
        insertFlowLog("", processInstance.getProcessDefinitionId(), processInstance.getProcessInstanceId(), processDefinitionKey, businessKey, "启动", "启动流程", "", userId, "0", reCode, status);
        long end = System.currentTimeMillis();
        log.debug("---------流程处理类【startProcess】方法调用开始，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey + ",总耗时:" + (end -start) + "毫秒");
        return reMap;
    }


    /**
     * 流程启动    例行变更申请延期
     * 页面指定的审批组，审批机构，审批人，互斥网关分流条件，监听事件的类型名等)
     * 返回map  中processInstanceId 为流程启动后得到的流程id
     *
     * @param businessKey          （流程中存储的业务ID 即业务表中主键Id）
     * @param processDefinitionKey （流程模板ID 指定启动模板对应的流程）
     * @param variables            3.variables(Map<String,Obeject> 格式，内容为流程变量，即流程模板中配置的${XXXXX} ,key=XXXXX value=自定义，默认全局变量
     * @return
     */
    @Override
    public synchronized Map<String, Object> startProcessLxbg(String businessKey, String processDefinitionKey, Map<String, Object> variables) {
        String userId = "";
        String reCode = "";
        String status = "";
        String comment = "";
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.得到RunService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //开始流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("processInstanceId", processInstance.getProcessInstanceId());
        if (StringUtils.isNotNull(variables.get("userId"))) {
            userId = variables.get("userId").toString();
        }
        if (StringUtils.isNotNull(variables.get("reCode"))) {
            reCode = variables.get("reCode").toString();
        }
        if (StringUtils.isNotNull(variables.get("status"))) {
            status = variables.get("status").toString();
        }
        if (variables.get("comment") != null) {
            comment = variables.get("comment").toString();
        }
        // 针对软研提交启动流程设置版本单状态
        if (StringUtils.isNotNull(variables.get("versionStatus"))) {
            status = variables.get("versionStatus").toString();
        }
        //流程记录
        insertFlowLog("", processInstance.getProcessDefinitionId(), processInstance.getProcessInstanceId(), processDefinitionKey, businessKey, comment, "延期申请", "", userId, "0", reCode, status);
        return reMap;
    }

    /**
     * 用户查询待办任务详细
     * userId 要查询的用户的登录名
     * processDefinitionKey 流程模板id 区分业务
     * 出参结构见：processList()方法
     */
    @Override
    public List<Map<String, Object>> userTask(String processDefinitionKey, String description) {
        //个人任务查询
        String userId = ShiroUtils.getOgUser().getpId();
        List<Task> usertasks = new ArrayList<>();
        List<Task> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(description)) {
            usertasks = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey).taskDescription(description).orderByTaskCreateTime().asc().list();
            list = taskService.createTaskQuery().taskCandidateUser(userId).processDefinitionKey(processDefinitionKey).taskDescription(description).orderByTaskCreateTime().asc().list();
        } else {
            usertasks = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey).orderByTaskCreateTime().asc().list();
            list = taskService.createTaskQuery().taskCandidateUser(userId).processDefinitionKey(processDefinitionKey).orderByTaskCreateTime().asc().list();
        }
        usertasks.addAll(list);
        return processList(usertasks, false);
    }

    /**
     * 查询用户任务总数
     *
     * @param userId
     * @param processDefinitionKey
     * @return
     */
    @Override
    public Long userTaskCount(String userId, String processDefinitionKey) {
        long total = 0;
        TaskQuery tq = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey);
        total = tq.count();
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateUser(userId).processDefinitionKey(processDefinitionKey);
        total += taskQuery.count();
        return total;
    }

    /**
     * 用户查询待办任务详细  分页查询
     * userId 要查询的用户的登录名
     * processDefinitionKey 流程模板id 区分业务
     * 出参结构见：processList()方法
     */
    @Override
    public Map<String, Object> userTaskPage(String processDefinitionKey, String description, int firstResult, int maxResult) {
        //个人任务查询
        String userId = ShiroUtils.getOgUser().getpId();
        List<Task> usertasks = new ArrayList<>();
        List<Task> list = new ArrayList<>();
        long total = 0;
        if (StringUtils.isNotEmpty(description)) {
            TaskQuery tq = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey).taskDescription(description).orderByTaskCreateTime().desc();
            total = tq.count();
            usertasks = tq.listPage(firstResult, maxResult);
            TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateUser(userId).processDefinitionKey(processDefinitionKey).taskDescription(description).orderByTaskCreateTime().desc();
            total += taskQuery.count();
            list = taskQuery.listPage(firstResult, maxResult);
        } else {
            TaskQuery tq = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey).orderByTaskCreateTime().desc();
            total = tq.count();
            usertasks = tq.listPage(firstResult, maxResult);
            TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateUser(userId).processDefinitionKey(processDefinitionKey).orderByTaskCreateTime().desc();
            total += taskQuery.count();
            list = taskQuery.listPage(firstResult, maxResult);
        }
        usertasks.addAll(list);
        List<Map<String, Object>> reList = processList(usertasks, false);
        Map<String, Object> mp = new HashMap<>();
        mp.put("total", total);
        mp.put("list", reList);
        return mp;
    }

    @Override
    public List<Map<String, Object>> userTaskUserId(String userId, String processDefinitionKey, String description) {
        List<Task> usertasks = new ArrayList<>();
        List<Task> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(description)) {
            //指定办理人
            usertasks = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey).taskDescription(description).list();
            //动态指定办理
            list = taskService.createTaskQuery().taskCandidateUser(userId).processDefinitionKey(processDefinitionKey).taskDescription(description).list();
        } else {
            //指定办理人
            usertasks = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey).list();
            //动态指定办理人
            list = taskService.createTaskQuery().taskCandidateUser(userId).processDefinitionKey(processDefinitionKey).list();
        }
        usertasks.addAll(list);
        return processList(usertasks, false);
    }


    /**
     * 多用户待办任务查询
     * LoginNameList 要查询的用户的登录名list
     * processDefinitionKey 流程模板Id 区分查询哪个业务
     * 出参结构见：processList()方法
     *
     * @param LoginNameList
     * @return
     */
    @Override
    public List<Map<String, Object>> userTask(List<String> LoginNameList, String processDefinitionKey) {
        //多用户任务查询
        List<Task> usertasks = taskService.createTaskQuery().taskAssigneeIds(LoginNameList).processDefinitionKey(processDefinitionKey).list();
        return processList(usertasks, false);
    }

    /**
     * *  组待办任务查询
     * <p>
     * <p>
     * <p>
     * <p>
     * 出参结构见：processList()方法
     *
     * @param processDefinitionKey 流程模板Id 区分查询哪个业务
     * @param description          任务描述可为空
     * @return
     */


    @Override
    public List<Map<String, Object>> groupTasks(String processDefinitionKey, String description) {
        String userId = ShiroUtils.getOgUser().getpId();
        List<String> groups = userInfo(userId);
        //获取ProcessEngine对象d
        ProcessEngine processEngines = ProcessEngines.getDefaultProcessEngine();
        //获取TaskService对象
        TaskService taskServices = processEngines.getTaskService();
        //组（机构）任务查询
        List<Task> groupTasks = new ArrayList<>();
        // List<Task> groupTasks1= taskServices.createTaskQuery().taskAssigneeIds(groups).processDefinitionKey(processDefinitionKey).list();
        if (StringUtils.isNotEmpty(description)) {
            groupTasks = taskServices.createTaskQuery().taskCandidateGroupIn(groups).processDefinitionKey(processDefinitionKey).taskDescription(description).orderByTaskCreateTime().asc().list();
        } else {
            groupTasks = taskServices.createTaskQuery().taskCandidateGroupIn(groups).processDefinitionKey(processDefinitionKey).orderByTaskCreateTime().asc().list();
        }
        return processList(groupTasks, true);
    }

    /**
     * /**
     * 认领任务
     * taskId 任务Id，loginName 用户登录名
     *
     * @param taskId
     * @param businessKey
     * @return
     */
    @Override
    public Map<String, Object> claim(String businessKey, String taskId) {
        Map<String, Object> reMap = new HashMap<>();
        ProcessEngines.getDefaultProcessEngine().getTaskService().claim(taskId, ShiroUtils.getOgUser().getpId());
        Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
        //查询认领人工作组信息
        String groupId = identityLinkList.get(0).getGroupId();
        //OgGroup orGroup = iSysWorkService.selectOgGroupById(groupId);
        String processDefinitionKey = task.getTaskDefinitionKey();
        String claim = String.valueOf(task.getCreateTime().getTime() - new Long(DateUtils.dateTime()));
        String userId = ShiroUtils.getUserId();
        insertFlowLog(taskId, task.getProcessDefinitionId(), task.getProcessInstanceId(), processDefinitionKey, businessKey, "认领", task.getName(), groupId, userId, claim);
        //0  成功
        reMap.put("reCode", 0);
        reMap.put("reInfo", "认领成功！");
        return reMap;
    }

    /**
     * 用户待办 简略
     *
     * @param userId
     * @param processDefinitionKey
     * @return
     */
    @Override
    public List<Task> allTasks(String userId, String processDefinitionKey) {
        List<Task> usertasks = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey).orderByTaskCreateTime().desc().list();
        List<Task> list = taskService.createTaskQuery().taskCandidateUser(userId).processDefinitionKey(processDefinitionKey).orderByTaskCreateTime().desc().list();
        list.addAll(usertasks);
        return list;
    }

    /**
     * 单人任务完成
     *
     * @param
     * @param
     * @param variables 流程变量默认全局流程变量
     *                  comment（审核意见,例->【同意】：审核通过 || 【驳回】：驳回申请）,用于流程历史展示.
     *                  reCode 节点审批结果变量  0：通过 1：驳回 其他值，自定义其他流程走向
     *                  businessKey
     *                  userId
     * @return
     */
    @Override
    public Map<String, Object> complete(Map<String, Object> variables) {
        Map<String, Object> reMap = new HashMap<>();
        //String taskId,String loginName,String instanceId,String comment,String reCode,
        String businessKey = "";
        String comment = "";
        String userId = "";
        String reCode = "";
        String taskId = "";
        String taskName = "";
        String processInstancedId = "";
        String processDefinitionId = "";
        String processDefinitionKey = "";
        String claimTime = "";
        String groupId = "";
        String status = "";
        //问题描述
        String description = "";
        if (variables.get("processDefinitionKey") != null) {
            processDefinitionKey = variables.get("processDefinitionKey").toString();
        }
        if (variables.get("businessKey") != null) {
            businessKey = variables.get("businessKey").toString();
        }
        if (variables.get("userId") != null) {
            userId = variables.get("userId").toString();
        } else {
            userId = ShiroUtils.getUserId();
        }
        if (variables.get("reCode") != null) {
            reCode = variables.get("reCode").toString();
        }
        if (variables.get("status") != null) {
            status = variables.get("status").toString();
        }
        if (variables.get("comment") != null) {
            comment = variables.get("comment").toString();
        }
        if (variables.get("description") != null) {
            description = variables.get("description").toString();
        }
        if (!ObjectUtils.isEmpty(variables.get("groupId"))) {
            groupId = variables.get("groupId").toString();
        }
        long start = System.currentTimeMillis();
        log.debug("---------流程处理类【complete】方法调用开始，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey);
        if (variables.get("taskId") != null) {
            taskId = variables.get("taskId").toString();

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            processInstancedId = task.getProcessInstanceId();
            processDefinitionId = task.getProcessDefinitionId();
//            processDefinitionKey=task.getTaskDefinitionKey();
            taskName = task.getName();
            if ("FmYx".equals(processDefinitionKey)) {
                //运行事件单计算实际时间
                EventRun eventRun = eventRunService.selectEventRunById(businessKey);
                if (StringUtils.isNotEmpty(eventRun.getHandleTime())) {
                    long now = (new Date()).getTime();
                    long createTime = Long.valueOf(eventRun.getHandleTime());
                    long diff = (now - createTime) / 1000;
                    claimTime = String.valueOf(diff);
                }

            } else if ("FmBiz".equals(processDefinitionKey) && variables.get("dealGroupId") != null) {
                claimTime = deleteFmTime(businessKey, userId, DateUtils.handleTimeYYYYMMDDHHMMSS(task.getCreateTime()), DateUtils.dateTimeNow());
            } else {
                long diff = iPubHolidayService.getWorkDayUsedTimes(DateUtils.handleTimeYYYYMMDDHHMMSS(task.getCreateTime()), DateUtils.dateTimeNow());
                claimTime = String.valueOf(diff);
            }
        } else {
            List<Task> task = taskService.createTaskQuery().taskCandidateUser(userId).processInstanceBusinessKey(businessKey).list();
            //未认领任务查询
            if (task.size() < 1) {
                task = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessKey)
                        .list();
            }
            if (StringUtils.isNotEmpty(description)) {
                for (Task tk : task) {
                    if (description.equals(tk.getDescription())) {
                        taskId = tk.getId();
                        processInstancedId = tk.getProcessInstanceId();
                        processDefinitionId = tk.getProcessDefinitionId();
//                        processDefinitionKey=tk.getTaskDefinitionKey();
                        taskName = tk.getName();
                        if ("FmYx".equals(processDefinitionKey)) {
                            //运行事件单计算实际时间
                            EventRun eventRun = eventRunService.selectEventRunById(businessKey);
                            if (StringUtils.isNotEmpty(eventRun.getHandleTime())) {
                                long now = (new Date()).getTime();
                                long createTime = Long.valueOf(eventRun.getHandleTime());
                                long diff = (now - createTime) / 1000;
                                claimTime = String.valueOf(diff);
                            }
                        } else if ("FmBiz".equals(processDefinitionKey) && variables.get("dealGroupId") != null) {
                            claimTime = deleteFmTime(businessKey, userId, DateUtils.handleTimeYYYYMMDDHHMMSS(tk.getCreateTime()), DateUtils.dateTimeNow());
                        } else {
                            long diff = iPubHolidayService.getWorkDayUsedTimes(DateUtils.handleTimeYYYYMMDDHHMMSS(tk.getCreateTime()), DateUtils.dateTimeNow());
                            claimTime = String.valueOf(diff);
                        }

                    }
                }
            } else {
                taskId = task.get(0).getId();
                processInstancedId = task.get(0).getProcessInstanceId();
                processDefinitionId = task.get(0).getProcessDefinitionId();
//                processDefinitionKey=task.get(0).getTaskDefinitionKey();
                taskName = task.get(0).getName();
                if ("FmYx".equals(processDefinitionKey)) {
                    //运行事件单计算实际时间
                    EventRun eventRun = eventRunService.selectEventRunById(businessKey);
                    if (StringUtils.isNotEmpty(eventRun.getHandleTime())) {
                        long now = (new Date()).getTime();
                        long createTime = Long.valueOf(eventRun.getHandleTime());
                        long diff = (now - createTime) / 1000;
                        claimTime = String.valueOf(diff);
                    }
                } else if ("FmBiz".equals(processDefinitionKey) && variables.get("dealGroupId") != null) {
                    claimTime = deleteFmTime(businessKey, userId, DateUtils.handleTimeYYYYMMDDHHMMSS(task.get(0).getCreateTime()), DateUtils.dateTimeNow());
                } else {
                    long diff = iPubHolidayService.getWorkDayUsedTimes(DateUtils.handleTimeYYYYMMDDHHMMSS(task.get(0).getCreateTime()), DateUtils.dateTimeNow());
                    claimTime = String.valueOf(diff);
                }
            }
        }
        //加入批注
        variables.put("reCode", reCode);
        taskService.setAssignee(taskId, userId);
        taskService.addComment(taskId, processInstancedId, comment);
        taskService.complete(taskId, variables);
        //流程日志 待测
        if (variables.get("dealGroupId") != null) {
            //OgGroup orGroup = iSysWorkService.selectOgGroupById(variables.get("dealGroupId").toString());
            insertFlowLog(taskId, processDefinitionId, processInstancedId, processDefinitionKey, businessKey, comment, taskName, variables.get("dealGroupId").toString(), userId, claimTime, reCode, status);
        } else {
            if ("FmBiz".equals(processDefinitionKey)) {
                groupId = "";
            }
            insertFlowLog(taskId, processDefinitionId, processInstancedId, processDefinitionKey, businessKey, comment, taskName, groupId, userId, claimTime, reCode, status);
        }
        reMap.put("reCode", 1);
        reMap.put("reInfo", "成功！");
        long end = System.currentTimeMillis();
        log.debug("---------流程处理类【complete】方法调用开始，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey + ",总耗时:" + (end -start) + "毫秒");
        return reMap;
    }

    /**
     * 处理任务前 查询任务  确认任务是否存在
     *
     * @param businesskey 业务Id 必输
     * @param userId      必输
     * @return
     */
    @Override
    public Task getUserTask(String businesskey, String userId) {
        if (StringUtils.isEmpty(businesskey) || StringUtils.isEmpty(userId)) {
            return null;
        }
        Task tk = processEngine.getTaskService().createTaskQuery().taskAssignee(userId).processInstanceBusinessKey(businesskey).singleResult();
        if (tk == null) {
            tk = processEngine.getTaskService().createTaskQuery().taskCandidateUser(userId).processInstanceBusinessKey(businesskey).singleResult();
        }
        return tk;
    }

    /**
     * 处理任务前 查询任务  确认任务是否存在
     *
     * @param businessKey 业务Id 必输
     * @param description 任务描述 必输
     * @return
     */
    @Override
    public Task getTask(String businessKey, String description) {
        if (StringUtils.isEmpty(businessKey) || StringUtils.isEmpty(description)) {
            return null;
        }
        Task tk = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessKey).taskDescription(description).singleResult();
        return tk;
    }

    /**
     * 根据taskId查询任务
     * @param taskId
     * @return
     */
    @Override
    public Task getTaskByTaskId(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            throw new BusinessException("......taskId is not null !");
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task;
    }

    /**
     * 处理任务前 查询任务  确认任务是否存在
     *
     * @param processInstanceId 流程id
     * @return
     */
    @Override
    public Task getTaskByProcessInstanceId(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)) {
            return null;
        }
        Task tk = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).singleResult();
        return tk;
    }

    /**
     * 多候选人 任务完成
     *
     * @param variables
     * @return
     */
    @Override
    public Map<String, Object> usersComplete(Map<String, Object> variables) {
        Map<String, Object> reMap = new HashMap<>();
        //String taskId,String loginName,String instanceId,String comment,String reCode,
        String businessKey = "";
        String comment = "";
        String userId = "";
        String reCode = "";
        String taskId = "";
        String taskName = "";
        String processDefinitionId = "";
        String processInstancedId = "";
        String processDefinitionKey = "";
        String claimTime = "";
        String groupId = "";
        //问题描述
        String description = "";
        if (variables.get("processDefinitionKey") != null) {
            processDefinitionKey = variables.get("processDefinitionKey").toString();
        }
        if (variables.get("businessKey") != null) {
            businessKey = variables.get("businessKey").toString();
        }
        if (variables.get("userId") != null) {
            userId = variables.get("userId").toString();
        } else {
            userId = ShiroUtils.getOgUser().getpId();
        }
        if (variables.get("reCode") != null) {
            reCode = variables.get("reCode").toString();
        }
        if (variables.get("comment") != null) {
            comment = variables.get("comment").toString();
        }
        if (variables.get("description") != null) {
            description = variables.get("description").toString();
        }
        if (!ObjectUtils.isEmpty(variables.get("groupId"))) {
            groupId = variables.get("groupId").toString();
        }
        long start = System.currentTimeMillis();
        log.debug("---------流程处理类【userComplete】方法调用开始，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey);
        if (variables.get("taskId") != null) {
            taskId = variables.get("taskId").toString();
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            processInstancedId = task.getProcessInstanceId();
            processDefinitionId = task.getProcessDefinitionId();
//            processDefinitionKey=task.getTaskDefinitionKey();
            taskName = task.getName();
            long diff = iPubHolidayService.getWorkDayUsedTimes(DateUtils.handleTimeYYYYMMDDHHMMSS(task.getCreateTime()), DateUtils.dateTimeNow());
            claimTime = String.valueOf(diff);
        } else {
            List<Task> task = taskService.createTaskQuery().taskCandidateUser(userId).processInstanceBusinessKey(businessKey).list();
            //未认领任务查询
            if (task.size() < 1) {
                task = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessKey)
                        .list();
            }
            if (StringUtils.isNotEmpty(description)) {
                for (Task tk : task) {
                    if (description.equals(tk.getDescription())) {
                        taskId = tk.getId();
                        processInstancedId = tk.getProcessInstanceId();
                        processDefinitionId = tk.getProcessDefinitionId();
//                        processDefinitionKey=tk.getTaskDefinitionKey();
                        taskName = tk.getName();
                        long diff = iPubHolidayService.getWorkDayUsedTimes(DateUtils.handleTimeYYYYMMDDHHMMSS(tk.getCreateTime()), DateUtils.dateTimeNow());
                        claimTime = String.valueOf(diff);
                    }
                }
            } else {
                taskId = task.get(0).getId();
                processInstancedId = task.get(0).getProcessInstanceId();
                processDefinitionId = task.get(0).getProcessDefinitionId();
//                processDefinitionKey=task.get(0).getTaskDefinitionKey();
                taskName = task.get(0).getName();
                long diff = iPubHolidayService.getWorkDayUsedTimes(DateUtils.handleTimeYYYYMMDDHHMMSS(task.get(0).getCreateTime()), DateUtils.dateTimeNow());
                claimTime = String.valueOf(diff);
            }
        }
        //加入批注
        taskService.addComment(taskId, processInstancedId, comment);
        //完成任务
        // Set<String> userList=new HashSet<>();
        //获取ProcessEngine对象
        ProcessEngine processEngines = ProcessEngines.getDefaultProcessEngine();
        //查询任务候选人
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
           /*
            if (identityLinkList != null && identityLinkList.size() > 0) {
                for (Iterator iterator = identityLinkList.iterator(); iterator.hasNext(); ) {
                    IdentityLink identityLink = (IdentityLink) iterator.next();
                    if (identityLink.getUserId() != null) {
                        userList.add(identityLink.getUserId());
                    }
                }
            }*/
        //没有 候选人 或者 驳回 节点任务即完成 否则 等待其他候选人完成任务
        if (1 == identityLinkList.size() || "1".equals(reCode)) {
            variables.put("reCode", reCode);
            taskService.setAssignee(taskId, userId);
            taskService.complete(taskId, variables);
            insertFlowLog(taskId, processDefinitionId, processInstancedId, processDefinitionKey, businessKey, comment, taskName, groupId, userId, claimTime, reCode);
            //候选人中删除 当前
            //   processEngines.getTaskService().deleteCandidateUser(taskId, userId);
            reMap.put("reCode", 0);
            reMap.put("reInfo", "流程节点任务完成");
        } else {
            //候选人中删除 当前
            processEngines.getTaskService().deleteCandidateUser(taskId, userId);

            // taskService.setVariables(taskId,variables);
            //候选组中删除当前
            insertFlowLog(taskId, processDefinitionId, processInstancedId, processDefinitionKey, businessKey, comment, taskName, groupId, userId, claimTime, reCode);
            reMap.put("reCode", 0);
            reMap.put("reInfo", "候选人：" + userId + "任务完成");
        }
        long end = System.currentTimeMillis();
        log.debug("---------流程处理类【userComplete】方法调用开始，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey + ",总耗时:" + (end -start) + "毫秒");
        return reMap;
    }

    /**
     * 定时任务完成
     *
     * @param variable
     * @return
     */
    @Override
    public Map<String, Object> quartzCompelte(Map<String, Object> variable) {
        Map<String, Object> reMap = new HashMap<>();
        String userId = "";
        String businessKey = "";
        String comment = "";
        String processDefinitionKey = "";
        if (variable.get("businessKey") != null) {
            businessKey = variable.get("businessKey").toString();
        }
        if (variable.get("comment") != null) {
            comment = variable.get("comment").toString();
        }
        if (variable.get("processDefinitionKey") != null) {
            processDefinitionKey = variable.get("processDefinitionKey").toString();
        }
        if (variable.get("userId") != null) {
            userId = variable.get("userId").toString();
        }
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessKey).list();
        for (Task tk : taskList) {
            taskService.addComment(tk.getId(), tk.getProcessInstanceId(), comment);
            taskService.complete(tk.getId(), variable);
            // 设置定时任务标志为true
            this.quartzFlag = true;
            insertFlowLog(tk.getId(), tk.getProcessDefinitionId(), tk.getProcessInstanceId(), processDefinitionKey, businessKey, comment, tk.getName(), "", userId, "");
            reMap.put("reCode", 0);
            reMap.put("reInfo", "定时完成任务");
        }
        return reMap;
    }

    @Override
    public Map<String, Object> quartzstartProcess(String businessKey, String processDefinitionKey, Map<String, Object> variables) {
        String userId = "";
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.得到RunService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //开始流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("processInstanceId", processInstance.getProcessInstanceId());
        this.quartzFlag = true;
        if (variables.get("createId") != null) {//设置流程处理人
            userId = variables.get("createId").toString();
        }
        //流程记录
        insertFlowLog("", processInstance.getProcessDefinitionId(), processInstance.getProcessInstanceId(), processDefinitionKey, businessKey, "启动", "启动流程", "", userId, "0");
        return reMap;
    }

    /**
     * 批量快速完成任务
     * 多个任务候选人只有一个的情况下
     *
     * @param allTask list元素为 taskId instanceId comment
     *                variables 多个任务使用相同流程变量
     * @return
     */
    public Map<String, Object> compeleteAll(List<Map<String, Object>> allTask, Map<String, Object> variables) {
        Map<String, Object> reMap = new HashMap<>();
        if (allTask.size() > 0) {
            for (Map<String, Object> task : allTask) {
                //加入批注
                taskService.addComment(task.get("taskId").toString(), task.get("instanceId").toString(), task.get("comment").toString());
                taskService.complete(task.get("taskId").toString(), variables);
            }
        }
        reMap.put("reCode", 0);
        reMap.put("reInfo", "批量完成任务成功！");
        return reMap;
    }

    /**
     * 根据businesskey 查看流程是否存在
     *
     * @param businesskey
     * @return
     */
    @Override
    public List<ProcessInstance> processInfo(String businesskey) {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.得到RunService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //开始流程
        List<ProcessInstance> processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businesskey).list();
        return processInstance;
    }

    /**
     * 已办任务查询
     *
     * @param description 任务描述可为空
     *                    processDefinitionKey 流程模板Id 区分业务
     * @return
     */
    @Override
    public List<Map<String, Object>> allComplete(String processDefinitionKey, String description) {
        Map<String, Object> reMap = new HashMap<>();
        List<Map<String, Object>> reList = new ArrayList<>();
        String userId = ShiroUtils.getOgUser().getpId();
        //未结束流程
        List<HistoricTaskInstance> hisList = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId).unfinished().processDefinitionKey(processDefinitionKey)
                .list();
        //已结束流程
        List<HistoricTaskInstance> hisList2 = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId).finished().processDefinitionKey(processDefinitionKey)
                .list();
        hisList.addAll(hisList2);
        // 根据流程的业务ID查询实体并关联
        for (HistoricTaskInstance instance : hisList) {
            String processInstanceId = instance.getProcessInstanceId();
            HistoricProcessInstance hisProcess = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (StringUtils.isNotEmpty(description)) {
                if (description.equals(instance.getName())) {
                    //流程Id
                    reMap.put("processInstanceId", processInstanceId);
                    //业务表主键Id
                    reMap.put("businesskey", hisProcess.getBusinessKey());
                    //流程名成
                    reMap.put("peocessName", hisProcess.getName());
                    //开始时间
                    reMap.put("startTime", hisProcess.getStartTime());
                    //流程变量map
                    reMap.put("variables", hisProcess.getProcessVariables());
                    //发起人
                    reMap.put("startUserId", hisProcess.getStartUserId());
                    //如果流程删除 删除原因
                    reMap.put("deleteReason", hisProcess.getDeleteReason());
                    //完的任务节点名称
                    reMap.put("taskName", instance.getName());
                }
            } else {

                //流程Id
                reMap.put("processInstanceId", processInstanceId);
                //业务表主键Id
                reMap.put("businesskey", hisProcess.getBusinessKey());
                //流程名成
                reMap.put("peocessName", hisProcess.getName());
                //开始时间
                reMap.put("startTime", hisProcess.getStartTime());
                //流程变量map
                reMap.put("variables", hisProcess.getProcessVariables());
                //发起人
                reMap.put("startUserId", hisProcess.getStartUserId());
                //如果流程删除 删除原因
                reMap.put("deleteReason", hisProcess.getDeleteReason());
                //完的任务节点名称
                reMap.put("taskName", instance.getName());
            }
            reList.add(reMap);
        }
        return reList;

    }

    /**
     * 撤销任务
     *
     * @param businessKey  业务Id
     * @param deleteReason 撤销原因
     * @return
     */
    @Override
    public Map<String, Object> deleteProcess(String businessKey, String deleteReason) {
        String userId = ShiroUtils.getUserId();
        Map<String, Object> reMap = new HashMap<>();
        // 执行此方法后未审批的任务 act_ru_task 会被删除，流程历史 act_hi_taskinst 不会被删除，并且流程历史的状态为finished完成
        List<ProcessInstance> ProcessInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessKey)
                .list();
        for (Task tk : taskList) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(tk.getProcessInstanceId()).singleResult();
            runtimeService.deleteProcessInstance(tk.getProcessInstanceId(), deleteReason);
            insertFlowLog(tk.getId(), tk.getProcessDefinitionId(), tk.getProcessInstanceId(), processInstance.getProcessDefinitionKey(), businessKey, deleteReason, deleteReason, "", userId, "");
             break;
        }
        reMap.put("reCode", 0);
        reMap.put("reInfo", "撤销成功");
        return reMap;
    }

    /**
     * 根据processKey查询对应的所有任务数据
     *
     * @param processKey
     * @return
     */
    @Override
    public List<Map<String, Object>> processDefinetionKeyTasks(String processKey) {
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(processKey).list();
        return processList(list, false);
    }

    /**
     * 流程历史
     *
     * @param processInstanceId
     * @return
     */
    public List<Map<String, Object>> processHistory(String processInstanceId) {
        Map<String, Object> reMap = new HashMap<>();
        List<Map<String, Object>> reList = new ArrayList<>();
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();
        List<HistoricActivityInstance> list = query.processInstanceId(processInstanceId).activityType("userTask")
                .finished().orderByHistoricActivityInstanceStartTime().desc().list();
        for (HistoricActivityInstance hi : list) {
            String taskId = hi.getTaskId();
            String deleteReason = hi.getDeleteReason();

            //如果中断
            if (deleteReason != null) {
                reMap.put("taskName", taskService.createTaskQuery().taskName(taskId));
                reMap.put("processInstanceId", processInstanceId);
                reMap.put("taskId", taskId);
                reMap.put("comment", deleteReason);
                reMap.put("assignee", hi.getAssignee());
                reMap.put("startTime", hi.getStartTime());
                reMap.put("endTime", hi.getEndTime());
                reMap.put("workTime", hi.getTime());
                reList.add(reMap);
            } else {
                //多人处理同一个任务 会记录多条任务信息 单人处理任务只有一条信息
                List<Comment> comments = taskService.getTaskComments(taskId);
                for (Comment ct : comments) {
                    reMap.put("taskName", taskService.createTaskQuery().taskName(taskId));
                    reMap.put("processInstanceId", processInstanceId);
                    reMap.put("taskId", taskId);
                    reMap.put("comment", ct.getFullMessage());
                    reMap.put("assignee", ct.getUserId());
                    reMap.put("startTime", hi.getStartTime());
                    reMap.put("endTime", ct.getTime());
                    reMap.put("workTime", hi.getStartTime().getTime() - hi.getStartTime().getTime());
                    reList.add(reMap);
                }
            }
        }
        return reList;
    }

    /**
     * @param businessKey
     * @throws Exception
     */
    public InputStream readResource(String businessKey) {
        InputStream resourceAsStream = null;
        List<HistoricTaskInstance> historicTaskInstanceList = historyService // 历史相关Service
                .createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).list();
        String pProcessInstanceId = "";
        if (historicTaskInstanceList.size() > 0) {
            pProcessInstanceId = historicTaskInstanceList.get(0).getProcessInstanceId();
        }
        String processDefinitionId = "";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(pProcessInstanceId).singleResult();
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(pProcessInstanceId).singleResult();
            processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

        String resourceName = pd.getDiagramResourceName();

        if (resourceName.endsWith(".png") && StringUtils.isEmpty(pProcessInstanceId) == false) {
            resourceAsStream = getActivitiProccessImage(pProcessInstanceId);
            // ProcessDiagramGenerator.generateDiagram(pde, "png",
            // getRuntimeService().getActiveActivityIds(processInstanceId));
        } else {
            // 通过接口读取
            resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);
        }
        return resourceAsStream;
    }

    /**
     * 查看流程进度图形
     *
     * @param pProcessInstanceId
     * @return
     */
    public InputStream getActivitiProccessImage(String pProcessInstanceId) {
        // logger.info("[开始]-获取流程图图像");
        InputStream imageStream = null;
        try {
            // 获取历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(pProcessInstanceId).singleResult();

            if (historicProcessInstance == null) {
                // throw new BusinessException("获取流程实例ID[" + pProcessInstanceId +
                // "]对应的历史流程实例失败！");
            } else {
                // 获取流程定义
                ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

                // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
                List<HistoricActivityInstance> historicActivityInstanceList = historyService
                        .createHistoricActivityInstanceQuery().processInstanceId(pProcessInstanceId)
                        .orderByHistoricActivityInstanceId().asc().list();

                // 已执行的节点ID集合
                List<String> executedActivityIdList = new ArrayList<String>();
                int index = 1;
                // logger.info("获取已经执行的节点ID");
                for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                    executedActivityIdList.add(activityInstance.getActivityId());

                    // logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + "
                    // : " +activityInstance.getActivityName());
                    index++;
                }

                BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

                // 已执行的线集合
                List<String> flowIds = new ArrayList<String>();
                // 获取流程走过的线 (getHighLightedFlows是下面的方法)
                flowIds = getHighLightedFlows(bpmnModel, processDefinition, historicActivityInstanceList);

//                // 获取流程图图像字符流
                //ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
//                //配置字体
//                InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds,"宋体","微软雅黑","黑体",null,2.0);

                Set<String> currIds = runtimeService.createExecutionQuery().processInstanceId(pProcessInstanceId).list()
                        .stream().map(e -> e.getActivityId()).collect(Collectors.toSet());

                ICustomProcessDiagramGenerator diagramGenerator = (ICustomProcessDiagramGenerator) processEngine
                        .getProcessEngineConfiguration().getProcessDiagramGenerator();
                imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,
                        flowIds, "宋体", "宋体", "宋体", null, 1.0,
                        new Color[]{WorkflowConstants.COLOR_NORMAL, WorkflowConstants.COLOR_CURRENT}, currIds);
            }
            // logger.info("[完成]-获取流程图图像");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return imageStream;
    }

    private List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity,
                                             List<HistoricActivityInstance> historicActivityInstances) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24小时制
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId

        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 对历史流程节点进行遍历
            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess()
                    .getFlowElement(historicActivityInstances.get(i).getActivityId());

            List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// 用以保存后续开始时间相同的节点
            FlowNode sameActivityImpl1 = null;

            HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// 第一个节点
            HistoricActivityInstance activityImp2_;

            for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
                activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点

                if (activityImpl_.getActivityType().equals("userTask")
                        && activityImp2_.getActivityType().equals("userTask")
                        && df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))) // 都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
                {

                } else {
                    sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess()
                            .getFlowElement(historicActivityInstances.get(k).getActivityId());// 找到紧跟在后面的一个节点
                    break;
                }

            }
            sameStartTimeNodes.add(sameActivityImpl1); // 将后面第一个节点放在时间相同节点的集合里
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

                if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))) {// 如果第一个节点和第二个节点开始时间相同保存
                    FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess()
                            .getFlowElement(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {// 有不相同跳出循环
                    break;
                }
            }
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows(); // 取出节点的所有出去的线

            for (SequenceFlow pvmTransition : pvmTransitions) {// 对所有的线进行遍历
                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess()
                        .getFlowElement(pvmTransition.getTargetRef());// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }

        }
        return highFlows;

    }

    /**
     * 任务参数封装
     *
     * @param taskList
     * @return
     */
    public List<Map<String, Object>> processList(List<Task> taskList, boolean isGroup) {
        List<Map<String, Object>> reList = new ArrayList<>();

        ProcessInstanceQuery pi = runtimeService.createProcessInstanceQuery();
        String processInstanceId = "";
        String businesskey = "";
        for (Task task : taskList) {
            Map<String, Object> reMap = new HashMap<>();
            //4.使用流程实例对象获取BusinessKey
            processInstanceId = task.getProcessInstanceId();
            ProcessInstance pro = pi.processInstanceId(processInstanceId).singleResult();
            businesskey = pro.getBusinessKey();
            //流程对应的业务表主键id
            reMap.put("businesskey", businesskey);
            //流程开启时间
            reMap.put("startTime", pro.getStartTime());
            //添加流程操作时间
            reMap.put("createTime", task.getCreateTime());
            //流程名称
            reMap.put("processName", pro.getName());
            //流程id
            reMap.put("processInstanceId", processInstanceId);
            //流程变量Variables Map<String, Object> 流程发起到上一步，自定义放入的所有变量
            reMap.put("variables", pro.getProcessVariables());
            //流程发起人
            reMap.put("startUser", pro.getStartUserId());
            //当前任务节点名称
            reMap.put("taskName", task.getName());
            //taskId 当前节点任务id 认领任务完成任务必输项
            reMap.put("taskId", task.getId());
            //任务描述
            reMap.put("description", task.getDescription());
            //是否认领
            if (isGroup) {
                //0 需要认领
                reMap.put("isClaim", 0);
            } else {
                //1 不需要认领
                reMap.put("isClaim", 1);
            }
            reList.add(reMap);
        }
        return reList;
    }

    /**
     * 插入流程日志
     *
     * @param processDefinitionKey 流程id
     * @param businessKey          业务id
     * @param comment              批注
     * @param taskName             当前任务名称
     * @param groupId              组ID
     * @param currentUserId        当前流程记录操作人
     * @param others               流程记录所需的额外参数
     * @return
     */
    public void insertFlowLog(String taskId, String processDefinitionId, String processInstancedId, String processDefinitionKey, String businessKey, String comment, String taskName, String groupId, String currentUserId, String claiTime, String... others) {
        long start = System.currentTimeMillis();
        log.debug("---------插入流程记录开始，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey);
        int count = iPubFlowLogService.selectPubFlowLogByBusinessKey(businessKey);
        //流程记录
        PubFlowLog pubFlowLog = new PubFlowLog();
        //日志id
        pubFlowLog.setLogId(IdUtils.randomUUID());
        //流程id
        pubFlowLog.setLogType(processDefinitionKey);
        //businessKey
        pubFlowLog.setBizId(businessKey);

        //下一步任务
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessKey)
                .list();
        StringBuffer users = new StringBuffer();
        StringBuffer usersPhone = new StringBuffer();
        StringBuffer nextTaskId = new StringBuffer();
        StringBuffer nextTaskName = new StringBuffer();
        //操作名称
        String deal = getFlowTransitions(taskId, processDefinitionId, processInstancedId);
        if (taskName.equals("撤销流程")) {
            deal = "关闭流程";
        }
        if (taskName.equals("撤回")) {
            deal = "撤回";
        }
        // reCode=1时排除纯技术路线（版本状态-待技术审核-6）和发布审批走紧急审批路线（版本状态-待紧急审批-13），这两种情况不属于任务结束
        boolean vmBnFlag = false;
        String reCode = "0";
        if ("VmBn".equals(processDefinitionKey) && others != null && others.length != 0) {
            String status = "";
            int length = others.length;
            if (length == 1) {
                reCode = others[0];
            } else if (length == 2) {
                reCode = others[0];
                status = others[1];
            }
            if ("1".equals(reCode)) {
                if (!VersionStatusConstants.VERSION_STATUS_13.equals(status) && !VersionStatusConstants.VERSION_STATUS_6.equals(status)) {
                    vmBnFlag = true;
                }
            }
        }
        if (taskList.size() < 1 || vmBnFlag) {
            nextTaskName.append("任务结束:");
        } else {
            int num = 0;
            for (Task task : taskList) {
                nextTaskId.append(task.getId() + ":");
                nextTaskName.append(task.getName() + ":");
                //查询任务候选人
                List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
                for (IdentityLink id : identityLinkList) {
                    String userId = id.getUserId();
                    if (StringUtils.isNotEmpty(userId)) {
                        OgPerson ogPerson = iOgPersonService.selectOgPersonEvoById(userId);
                        // OgGroup OgGroup = iSysWorkService.selectOgGroupById(ogPerson.getGroupId());
                        users.append(ogPerson.getpId() + ",");
                        if (StringUtils.isEmpty(ogPerson.getPhone())) {
                            usersPhone.append(ogPerson.getpName() + "-" + ogPerson.getMobilPhone() + ",");
                        } else {
                            usersPhone.append(ogPerson.getpName() + "-" + ogPerson.getPhone() + ",");
                        }

                        // 调用发短信方法
                        log.debug("------业务流程key[businessKey]=" + businessKey + ",下一流程环节人员名称[pName]=" + ogPerson.getpName() + ",手机号[phone]=" + ogPerson.getMobilPhone() + ",当前操作人[FlowUser]=" + currentUserId + "-------");
                        this.sendMsg(taskName, task, processDefinitionKey, ogPerson, businessKey, currentUserId, reCode, num);
                        num++;
                    } else {
                        //组 角色 任务
                        String group = id.getGroupId();
                        if (StringUtils.isNotEmpty(group) && group.contains("_")) {
                            group = group.substring(0, group.indexOf("_"));
                        }
                        //组 5
                        OgGroup OgGroup = iSysWorkService.selectOgGroupById(group);
                        //角色 21
                        OgRole ogRole = iSysRoleService.selectRoleById(group);
                        //岗位 2
//                        OgPost OgPost = (OgPost) ogPostService.selectPostById(group);
                        //机构 null
                        OgOrg OgOrg = iSysDeptService.selectDeptById(group);

                        String ogGp = OgGroup == null ? "" : OgGroup.getGroupId();
                        String ogRl = ogRole == null ? "" : ogRole.getRid();
//                        String ogPt = OgPost == null ? "" : OgPost.getPostName();
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

            //流程类型
//            pubFlowLog.setLogType(taskList.get(0).getProcessDefinitionId());
        }
        //任务名
        pubFlowLog.setTaskName(taskName);
        OgPerson op = new OgPerson();
        // 如果是定时任务完成流程没有人员信息，默认为空new一个OgPerson对象（防止后边的代码出错）
        if (quartzFlag) {
            if (StringUtils.isNotEmpty(currentUserId)) {
                op = iOgPersonService.selectOgPersonEvoById(currentUserId);
            }
            quartzFlag = false;
        } else {
            if (StringUtils.isNotEmpty(currentUserId)) {
                op = iOgPersonService.selectOgPersonEvoById(currentUserId);
            } else {
                op = iOgPersonService.selectOgPersonEvoById(ShiroUtils.getUserId());
            }
        }
        //下一步任务id
        pubFlowLog.setNextTaskId(nextTaskId.toString());
        //下一步任务名
        String nextTName = nextTaskName.toString();
        nextTName = nextTName.substring(0, nextTName.length() - 1);
        pubFlowLog.setNextTaskName(nextTName);
        //下一步处理人
        String failusers = "";
        if (users.indexOf(":") > 0) {
            failusers = users.toString().substring(0, users.length() - 1);
        } else {
            failusers = users.toString();
        }
        pubFlowLog.setNextPerformerDesc(failusers);
        //下一步处理人电话
        if (usersPhone.length() > 3000) {
            String up = usersPhone.toString().substring(0, 3000);
            pubFlowLog.setNextPerformerTel(up + "...");
        } else {
            pubFlowLog.setNextPerformerTel(usersPhone.toString());
        }
        //备注
        pubFlowLog.setPerformDesc(comment);
        //操作人Id
        pubFlowLog.setPerformerId(op.getpId());
        //操作人名称
        pubFlowLog.setPerformerName(op.getpName());
        //操作名称
        pubFlowLog.setPerformName(deal);
        //操作时间
        pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());
        //用户机构id OgPerson ogPerson=iOgPersonService.selectOgPersonEvoById(userId);

        pubFlowLog.setPerformerOrgId(op.getOrgId());
        //用户机构名称
        pubFlowLog.setPerformerOrgName(op.getOrgname());
        //用户组名
        if (StringUtils.isNotEmpty(groupId)) {
            OgGroup OgGroup = iSysWorkService.selectOgGroupById(groupId);
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
        //运行值班人
        if ("FmYx".equals(processDefinitionKey)) {
            if (op.getpId() != null) {
                //值班账号
                DutyAccount dutyAccount= dutyAccountMapper.selectDutyAccountByPid(op.getpId());
                if(dutyAccount!=null){
                    pubFlowLog.setDutyId(dutyAccount.getAccountPid());
                    OgPerson ogPerson= iOgPersonService.selectOgPersonById(dutyAccount.getAccountPid());
                    if(ogPerson!=null){
                        pubFlowLog.setDutyAccount(ogPerson.getpName());
                    }
                }
            }
        }
        pubFlowLog.setSerialNum(String.valueOf(count + 1));
        pubFlowLog.setLogId(IdUtils.fastSimpleUUID());
        int rows = iPubFlowLogService.insertPubFlowLog(pubFlowLog);
        long end = System.currentTimeMillis();
        if(rows > 0){
            log.debug("---------插入流程记录结束，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey + ",总耗时:" + (end -start) + "毫秒");
        } else {
            log.debug("---------插入流程记录失败，processDefinitionKey=" + processDefinitionKey + ",工单id=" + businessKey + ",总耗时:" + (end -start) + "毫秒");
        }
    }

    /**
     * @param userId
     * @return
     */
    public List<String> userInfo(String userId) {
        List<String> reList = new ArrayList<>();
        //组 5
        List<OgGroup> userGroupList = iSysWorkService.selectGroupByUserId(userId);
        //角色 21
        List<OgRole> ogRoles = iSysRoleService.selectRolesByUserId(userId);
        OgPost post = new OgPost();
        post.setUserId(userId);
        //岗位 2
        List<OgPost> ogPosts = ogPostService.selectAllPostByUserId(userId, "");
        //机构 null
        OgPerson ogPerson = iOgPersonService.selectOgPersonEvoById(userId);
        reList.add(ogPerson.getOrgId());
        if (userGroupList != null && userGroupList.size() > 0) {
            for (OgGroup gp : userGroupList) {
                reList.add(gp.getGroupId());
            }
        }
        if (ogRoles != null && ogRoles.size() > 0) {
            for (OgRole sr : ogRoles) {
                if ("0301".equals(sr.getRid())) {
                    String pCode = iSysDeptService.getpCode(userId);
                    reList.add("0301_" + pCode);
                }
                reList.add(sr.getRid());
            }
        }
        if (ogPosts != null && ogPosts.size() > 0) {
            for (OgPost op : ogPosts) {
                reList.add(op.getPostId());
            }
        }
        return reList;
    }

    /**
     * 插入协同处理人（调度事件单，并行任务不可用）
     *
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> insertUser(Map<String, Object> map) {
        Map<String, Object> reMap = new HashMap<>();
        String userId = "";
        if (map.get("userId") != null) {
            userId = map.get("userId").toString();
        } else {
            userId = ShiroUtils.getUserId();
        }
        String businessKey = map.get("businessKey").toString();
        String comment = map.get("comment").toString();
        Task task = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessKey)
                .singleResult();
        ProcessEngine processEngines = ProcessEngines.getDefaultProcessEngine();
        String variable = taskService.getVariable(task.getId(), "dealId") == null ? "" : taskService.getVariable(task.getId(), "dealId").toString();
        String[] xtIdsS = variable.split(",");

        String newxtIds = map.get("dealId").toString();
        String[] newxtidsS = newxtIds.split(",");
        StringBuffer rextIds = new StringBuffer();
        if (xtIdsS.length > newxtidsS.length) {
            //删除协同处理人
            List<String> list = new LinkedList<String>();
            for (String str : xtIdsS) { // 处理第一个数组,list里面的值为1,2,3,4
                if (!list.contains(str)) {
                    list.add(str);
                }
            }
            for (String str : newxtidsS) { // 如果第二个数组存在和第一个数组相同的值，就删除
                rextIds.append(str);
                if (list.contains(str)) {
                    list.remove(str);
                }
            }
            String[] result = {}; // 创建空数组
            list.toArray(result); // List to Array
            for (String id : list) {

                processEngines.getTaskService().deleteCandidateUser(task.getId(), id);
            }
            taskService.setVariable(task.getId(), "dealId", newxtIds);
        } else {
            Set<String> idsSet = new HashSet<>();
            if (StringUtils.isNotEmpty(variable)) {

                for (String s : xtIdsS) {
                    idsSet.add(s);
                }
            }


            for (String s : newxtidsS) {
                if (!idsSet.contains(s)) {
                    processEngines.getTaskService().addCandidateUser(task.getId(), s);
                    idsSet.add(s);
                }

            }

            for (String ss : idsSet) {
                rextIds.append(ss + ",");
            }
        }
        if (!StringUtils.isEmpty(rextIds)) {
            String idss = rextIds.substring(0, rextIds.length() - 1);
            taskService.setVariable(task.getId(), "dealId", idss);
            reMap.put("dealId", idss);
        }

        reMap.put("comment", comment);
        reMap.put("businessKey", businessKey);
        reMap.put("reCode", 2);
        insertFlowLog(task.getId(), task.getProcessDefinitionId(), task.getProcessInstanceId(), map.get("activiti").toString(), businessKey, comment, map.get("taskName").toString(), "", userId, "");
        return reMap;
    }

    /**
     * 获取操作名称  根据流程历史中最近时间的两个节点 获取流程线对象 并获取线上的名称
     *
     * @param processDefinitionId
     * @param processInstancedId
     * @return
     */
    public String getFlowTransitions(String taskId, String processDefinitionId, String processInstancedId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24小时制
        //不传taskid  是任务发起情况
        if (StringUtils.isEmpty(taskId)) {
            return "启动任务";
        }
        // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery().processInstanceId(processInstancedId).orderByHistoricActivityInstanceStartTime().asc().list();
        // 判空防止程序取集合报错
        if (CollectionUtils.isEmpty(historicActivityInstances)) {
            return "无";
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        String deal = "";// 用以保存高亮的线name
        //已办理
        HistoricActivityInstance historicActivityInstance1 = null;
        HistoricActivityInstance historicActivityInstance2 = null;
        HistoricActivityInstance historicActivityInstance3 = null;
        for (int i = 0; i < historicActivityInstances.size(); i++) {
            if (StringUtils.isNotEmpty(historicActivityInstances.get(i).getTaskId()) && historicActivityInstances.get(i).getTaskId().equals(taskId)) {
                //通过taskId 匹配已完成任务
                historicActivityInstance1 = historicActivityInstances.get(i);
                //已完成任务时间=未完成任务开始时间
                if (historicActivityInstance1.getEndTime() == null) {
                    //多人任务，任务未结束
                    return "通过";
                } else {
                    for (HistoricActivityInstance hi : historicActivityInstances) {
                        Long endTime = historicActivityInstance1.getEndTime().getTime();
                        Long hiStartTime = hi.getStartTime().getTime();
                        Long a1 = hiStartTime - endTime;
                        //时间误差 0到2秒钟
                        if (0 <= a1 && a1 <= 2000) {
                            if (hi.getActivityType().equals("userTask") || hi.getActivityType().equals("endEvent")) {
                                historicActivityInstance3 = hi;
                            }
                            if (hi.getActivityType().equals("exclusiveGateway") || hi.getActivityType().equals("parallelGateway")) {
                                if (historicActivityInstance2 == null) {
                                    historicActivityInstance2 = hi;
                                } else {
                                    historicActivityInstance3 = hi;
                                }
                            }
                        }

                    }
                }
                break;
            }
        }
        //下一部 什么都没有  强制关闭任务
        if (historicActivityInstance2 == null && historicActivityInstance3 == null) {
            return "强制结束任务";
        }
        //下一步  没有任务 并行任务情况待 改造
        if (historicActivityInstance2 == null && historicActivityInstance3 != null) {

            historicActivityInstance2 = historicActivityInstance3;
        } else if (historicActivityInstance2 != null && historicActivityInstance3 != null) {
            //下一步有任务 有异步网关
            historicActivityInstance1 = historicActivityInstance2;
            historicActivityInstance2 = historicActivityInstance3;
        }
        //前一个节点
        FlowNode activityImpl1 = (FlowNode) bpmnModel.getMainProcess()
                .getFlowElement(historicActivityInstance1.getActivityId());

        //最后一个节点
        FlowNode activityImpl2 = (FlowNode) bpmnModel.getMainProcess()
                .getFlowElement(historicActivityInstance2.getActivityId());//


        List<SequenceFlow> pvmTransitions1 = activityImpl1.getOutgoingFlows(); // 取出节点的所有进去
        List<SequenceFlow> pvmTransitions2 = activityImpl2.getIncomingFlows(); // 取出节点的所有进去


        for (SequenceFlow pvmTransition : pvmTransitions1) {// 对所有的线进行遍历
            for (SequenceFlow pn : pvmTransitions2) {
                if (pvmTransition.getId().equals(pn.getId())) {
                    deal = pn.getName() == null ? "" : pn.getName();
                }
            }
        }
        //如果是接口自动执行的任务
        if (this.quartzFlag == true) {
            Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstancedId).singleResult();
            String deleteReason = historicActivityInstance1.getDeleteReason();
            //如果流程结束
            if (Objects.isNull(execution) && StringUtils.isNotEmpty(deleteReason)) {
                //操作名称 结束流程
                deal = "结束流程";
            }
        }
        return StringUtils.isEmpty(deal) ? "通过" : deal;
    }

    /**
     * 向各个节点发送短信（安全审核节点和流程作废以及发布成功情况写在VersionPublicProcessListener（版本发布流程监听类））
     *
     * @param taskName
     * @param nextTask
     * @param processDefinitionKey
     * @param smsPerson
     * @param businessKey
     * @param num                  使用num控制当前环节是业务审核，下一节点是业务主管存在多个的情况），向项目经理发送重复短信的问题
     *                             控制当前环节是技术主管，下一节点可能是业务审核或业务主管（存在多个的情况），向项目经理重复发短信的问题
     *                             控制当前环节是业务主管，（下一节点可能是技术审核｜技术主管）｜当前节点候选人可能还是业务主管（存在多个的情况），向项目经理重复发短信的问题
     *                             num = 0 控制只能发送一次，下一次循环出去后num++，判断不会在进入
     */
    public void sendMsg(String taskName, Task nextTask, String processDefinitionKey, OgPerson smsPerson, String businessKey, String currentUserId, String reCode, int num) {
        // 版本发布
        if ("VmBn".equals(processDefinitionKey)) {
            if ("0".equals(reCode)) {
                OgPerson person = iOgPersonService.selectOgPersonEvoById(currentUserId);
                VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(businessKey);
                log.debug("-----------版本单号:" + vmBizInfo.getVersionInfoNo() + "，进入版本发布发送短信判断部分，当前环节名称:" + taskName + ",下一环节名称:" + nextTask.getName());
                log.debug("-----------版本单号:" + vmBizInfo.getVersionInfoNo() + "，进入版本发布发送短信判断部分，当前操作人:" + person.getpName() + ",流程节点人员名称:" + smsPerson.getpName() + ",流程节点人员手机号:" + smsPerson.getMobilPhone());
                // 版本创建人(项目经理)
                OgPerson producerPerson = iOgPersonService.selectOgPersonEvoById(vmBizInfo.getVersionProducerId());
                if ("业务审核".equals(taskName) && "业务主管审核".equals(nextTask.getName())) {
                    // 向业务主管发送上行短信
                    this.sendMsgUp(nextTask.getId(), businessKey, currentUserId, smsPerson, "VmBn");
                    if (num == 0) {
                        String[] strings = Convert.convertStrArrToSet(vmBizInfo.getBusinessApprovalId());
                        List<OgPerson> businessApprovalPersons = iOgPersonService.selectPersonListByPIds(strings);
                        String busApprovalName = "";
                        for (OgPerson busApproval : businessApprovalPersons) {
                            busApprovalName += busApproval.getpName() + ",";
                        }
                        busApprovalName = StringUtils.removeLastChar(busApprovalName);
                        // 向项目经理发送短信
                        String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本业务审核已全部通过,下一步业务主管审批,审批人：" + busApprovalName + "。";
                        this.sendMsgNoUp(producerPerson, msg, currentUserId);
                    }
                } else if ("业务主管审核".equals(taskName) && !"版本审核".equals(nextTask.getName())) {
                    if (num == 0) {
                        String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本业务主管：" + person.getpName() + ",已审批通过。";
                        // 向项目经理发送短信
                        this.sendMsgNoUp(producerPerson, msg, currentUserId);
                    }
                } else if ("业务主管审核".equals(taskName) && "版本审核".equals(nextTask.getName())) {
                    String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本业务主管：" + person.getpName() + ",已审批通过,下一步版本审核,审核人：" + smsPerson.getpName() + "。";
                    // 向项目经理发送短信
                    this.sendMsgNoUp(producerPerson, msg, currentUserId);
                    // 向版本审核发送短信
                    this.sendMsgNoUp(smsPerson, msg, currentUserId);
                } else if ("技术审核".equals(taskName) && "技术主管审核".equals(nextTask.getName())) {
                    String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本技术审核人：" + person.getpName() + ",已审核通过,下一步技术主管审批,审批人：" + smsPerson.getpName() + "。";
                    // 向项目经理发送短信
                    this.sendMsgNoUp(producerPerson, msg, currentUserId);
                    // 向技术主管发送上行短信
                    this.sendMsgUp(nextTask.getId(), businessKey, currentUserId, smsPerson, "VmBn");
                } else if ("技术主管审核".equals(taskName) && !"版本审核".equals(nextTask.getName())) {
                    if (num == 0) {
                        String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本技术主管：" + person.getpName() + "已审批通过。";
                        // 向项目经理发送短信
                        this.sendMsgNoUp(producerPerson, msg, currentUserId);
                    }
                } else if ("技术主管审核".equals(taskName) && "版本审核".equals(nextTask.getName())) {
                    String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本技术主管：" + person.getpName() + ",已审批通过,下一步版本审核,审核人：" + smsPerson.getpName() + "。";
                    // 向项目经理发送短信
                    this.sendMsgNoUp(producerPerson, msg, currentUserId);
                    // 向版本审核发送短信
                    this.sendMsgNoUp(smsPerson, msg, currentUserId);
                } else if ("版本审核".equals(taskName)) {
                    String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本审核：" + person.getpName() + ",已审批通过,下一步运维审核,审核人：" + smsPerson.getpName() + "。";
                    // 向项目经理发送短信
                    this.sendMsgNoUp(producerPerson, msg, currentUserId);
                    // 向运维审核发送上行短信
                    this.sendMsgUp(nextTask.getId(), businessKey, currentUserId, smsPerson, "VmBn");
                } else if ("运维审核".equals(taskName)) {
                    String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本运维审核：" + person.getpName() + ",已审批通过,下一步发布审核,审核人：" + smsPerson.getpName() + "。";
                    // 向项目经理发送短信
                    this.sendMsgNoUp(producerPerson, msg, currentUserId);
                    // 向发布审核发送上行短信
                    this.sendMsgUp(nextTask.getId(), businessKey, currentUserId, smsPerson, "VmBn");
                } else if ("发布审批".equals(taskName)) {
                    /**
                     * 发布审批reCode=0的情况包含发布成功和作废都写在VersionPublicProcessListener类中
                     */
                } else if ("紧急发布审批".equals(taskName)) {
                    /**
                     * 紧急审批reCode=0的情况包含发布成功和作废都写在VersionPublicProcessListener类中
                     */
                }
            } else if ("发布审批".equals(taskName) && "1".equals(reCode)) {
                // 版本创建人(项目经理)
                VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(businessKey);
                OgPerson producerPerson = iOgPersonService.selectOgPersonEvoById(vmBizInfo.getVersionProducerId());
                OgPerson person = iOgPersonService.selectOgPersonEvoById(currentUserId);
                String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本发布审核：" + person.getpName() + ",已审批通过,下一步紧急审批,审核人：" + smsPerson.getpName() + "。";
                // 向项目经理发送短信
                this.sendMsgNoUp(producerPerson, msg, currentUserId);
                // 发布审批选择紧急审批时reCode=1，此时需要向紧急审批发送上行短信
                this.sendMsgUp(nextTask.getId(), businessKey, currentUserId, smsPerson, "VmBn");
            }
        }
        if ("CmZy".equals(processDefinitionKey)) {
            if ("审批变更单".equals(taskName) && "分管领导审批".equals(nextTask.getName())) {
                this.sendMsgUp(nextTask.getId(), businessKey, currentUserId, smsPerson, "CmZy");
            }
        }
        if ("BGQQ".equals(processDefinitionKey)) {
            if ("审批".equals(taskName) && "分管领导审批".equals(nextTask.getName())) {
                this.sendMsgUp(nextTask.getId(), businessKey, currentUserId, smsPerson, "BGQQ");
            }
        }
    }

    /**
     * 发送短信方法(不包含上行)
     *
     * @param person
     * @param msg
     */
    public void sendMsgNoUp(OgPerson person, String msg, String currentUserId) {
        PubBizPresms pubBizPresms = new PubBizPresms();
        pubBizPresms.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresms.setTelephone(person.getMobilPhone());
        pubBizPresms.setName(person.getpName());
        pubBizPresms.setSmsText(msg);
        pubBizPresms.setCreaterId(currentUserId);
        pubBizPresms.setInvalidationMark("1");
        pubBizPresms.setSmsState("0");
        pubBizPresms.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        pubBizPresms.setInspectObject("050100");// 检查对象
        pubBizPresms.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        //pubBizPresms.setDealStatus("0");
        pubBizPresmsService.insertPubBizPresms(pubBizPresms);
        pubBizSmsService.findPreSmsAndSend(pubBizPresms);
    }

    /**
     * 发送短信方法(包含上行)
     *
     * @param taskId
     * @param bizId
     * @param userId
     * @param smsPerson
     * @param processDefinitionKey
     */
    public void sendMsgUp(String taskId, String bizId, String userId, OgPerson smsPerson, String processDefinitionKey) {
        OgPerson person = iOgPersonService.selectOgPersonEvoById(userId);
        pubBizSmsService.sendMessage(taskId, bizId, person, smsPerson, processDefinitionKey);
    }

    /**
     * 撤销任务  原有任务全部结束
     *
     * @param businessKey
     * @param userId
     * @param processDefinitionKey 流程key
     * @throws Exception
     */
    @Override
    public void revokeCreateTask(String businessKey, String userId, String processDefinitionKey) throws Exception {
        List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
        //撤销刚刚启动的任务
        runtimeService.deleteProcessInstance(taskList.get(0).getProcessInstanceId(), "发起人撤回！");
        historyService.deleteHistoricProcessInstance(taskList.get(0).getProcessInstanceId());
        insertFlowLog(taskList.get(0).getId(), taskList.get(0).getProcessDefinitionId(), taskList.get(0).getProcessInstanceId(), processDefinitionKey, businessKey, "撤回任务", "撤回", "", "", "");
        return;
    }


    /**
     * 中间任务撤回到上一步
     *
     * @param businessKey
     * @param userId               操作人，只有上一步操作人有权撤回
     * @param processDefinitionKey 流程key
     * @throws Exception
     */
    @Override
    public void revokeTask(String businessKey, String userId, String processDefinitionKey) throws Exception {

        List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
        if (CollectionUtils.isEmpty(taskList)) {
            throw new BusinessException("流程未启动或已执行完成，无法撤回");
        }
        String excutionId = taskList.get(0).getExecutionId();
        String processDefinitionId = taskList.get(0).getProcessDefinitionId();
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                .executionId(excutionId).finished().list();
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(businessKey).finished()
                .orderByHistoricTaskInstanceStartTime()
                .desc()
                .list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        for (HistoricTaskInstance hti : htiList) {
            if (userId.equals(hti.getAssignee())) {
                myTask = hti;
                myTaskId = hti.getId();
            }
        }
        if (StringUtils.isEmpty(myTaskId)) {
            Map<String, Object> variables = runtimeService.getVariables(excutionId);
            if (variables.get("createId") != null && userId.equals(variables.get("createId"))) {
                //撤销刚刚启动的任务
                runtimeService.deleteProcessInstance(taskList.get(0).getProcessInstanceId(), "发起人撤回！");
                historyService.deleteHistoricProcessInstance(taskList.get(0).getProcessInstanceId());
                insertFlowLog(taskList.get(0).getId(), taskList.get(0).getProcessDefinitionId(), taskList.get(0).getProcessInstanceId(), processDefinitionKey, businessKey, "撤回任务", "撤回", "", "", "");
                return;
            }
        }

        if (StringUtils.isNotEmpty(userId) && null == myTaskId) {
            throw new BusinessException("该任务非当前用户提交，无法撤回");
        }
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        //变量
        String myActivityId = null;
        for (HistoricActivityInstance hai : haiList) {
            if (myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        for (Task task : taskList) {
            FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);
            Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
            String activityId = execution.getActivityId();
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);
            //记录原活动方向
            List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
            oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

            //清理活动方向
            flowNode.getOutgoingFlows().clear();
            //建立新方向
            List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
            SequenceFlow newSequenceFlow = new SequenceFlow();
            newSequenceFlow.setId("newSequenceFlowId");
            newSequenceFlow.setSourceFlowElement(flowNode);
            newSequenceFlow.setTargetFlowElement(myFlowNode);
            newSequenceFlowList.add(newSequenceFlow);
            flowNode.setOutgoingFlows(newSequenceFlowList);

            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), task.getProcessInstanceId(), "撤回");
            //完成任务
            taskService.complete(task.getId());
            //恢复原方向
            flowNode.setOutgoingFlows(oriSequenceFlows);
        }
        insertFlowLog(taskList.get(0).getId(), processDefinitionId, myTask.getProcessInstanceId(), processDefinitionKey, businessKey, "撤回任务", "撤回", "", userId, "");
    }

    /**
     * 业务事件单去除转数据变更和业务处理用时
     *
     * @param businessKey
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    public String deleteFmTime(String businessKey, String userId, String startTime, String endTime) {
        String claimTime = "";

        List<OgPost> ogPosts = ogPostService.selectAllPostByUserId(userId, "");
        if (!ogPosts.isEmpty()) {
            for (OgPost op : ogPosts) {
                if ("0013".equals(op.getPostId())) {//如果是业务人员处理处理时间整个为0
                    claimTime = "0";
                    break;
                }
            }
        }
        if (!"0".equals(claimTime)) {
            FmBiz fb = fmBizService.selectFmBizById(businessKey);
            if (fb != null) {
                int aa = 0;
                String sysType = applicationManagerService.selectOgSysBySysId(fb.getSysid()).getSysType();
                Long l = iPubHolidayService.getWorkDaySysTimes(sysType, startTime, endTime);
                //查询处理环节是否存在转数据变更单start
                CmBizSingleSJVo cbs = new CmBizSingleSJVo();
                cbs.setFmNo(fb.getFaultNo());
                Map<String, Object> reMap = new HashMap<>();
                reMap.put("startTime", startTime);
                reMap.put("endTime", endTime);
                cbs.setParams(reMap);
                List<CmBizSingleSJVo> list = cmBizSingleSJService.fmAndCmList(cbs);
                //查询处理环节是否存在转数据变更单end
                if (!list.isEmpty()) {//存在的话去除转数据变更单时间
                    for (CmBizSingleSJVo csVo : list) {
                        List<PubFlowLog> list2 = iPubFlowLogService.selectPubFlowLogAsc(csVo.getChangeSingleId());
                        if (!list2.isEmpty()) {
                            for (PubFlowLog pubFlowLog : list2) {
                                if (StringUtils.isNotEmpty(pubFlowLog.getSysResidenceTime())) {
                                    aa += Integer.parseInt(pubFlowLog.getSysResidenceTime());
                                }

                            }
                            claimTime = String.valueOf(l - aa);
                        } else {
                            claimTime = String.valueOf(l);
                        }
                    }
                } else {//不存在直接拿计算的处理用时
                    claimTime = String.valueOf(l);
                }
            } else {
                claimTime = "0";
            }
        }
        return claimTime;
    }

    @Override
    /**
     * 通过业务id查询流程信息
     *
     * */
    public List<Map<String, Object>> processBusinesskeyKeyTask(String businesskey) {
        List<Task> list = taskService.createTaskQuery().processInstanceBusinessKey(businesskey).list();
        return processList(list, false);
    }

}
