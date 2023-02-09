package com.ruoyi.activiti.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * activiti 通用
 * zx
 */
public interface ActivitiCommService {
    /**
     * 开启流程
     *
     * @param
     * @return
     */
    public Map<String, Object> startProcess(String businessKey, String processDefinitionKey, Map<String, Object> variables);


    /**
     * 开启流程 (例行计划申请延期)
     *
     * @param
     * @return
     */
    public Map<String, Object> startProcessLxbg(String businessKey, String processDefinitionKey, Map<String, Object> variables);


    /**
     * user待办查询
     */
    public List<Map<String, Object>> userTask(String processDefinitionKey, String description);
    /**
     * 用户查询待办任务详细  分页查询 提升查询效率
     * userId 要查询的用户的登录名
     * processDefinitionKey 流程模板id 区分业务
     * 出参结构见：processList()方法
     */
    public Map<String, Object> userTaskPage(String processDefinitionKey, String description,int firstResult,int maxResult);
    /**
     * user待办查询
     */
    public List<Map<String, Object>> userTaskUserId(String userId, String processDefinitionKey, String description);

    /**
     * 多用户待办查询
     *
     * @param LoginNameList
     * @return
     */
    public List<Map<String, Object>> userTask(List<String> LoginNameList, String processDefinitionKey);

    /**
     * groups 待办查询
     */
    public List<Map<String, Object>> groupTasks(String processDefinitionKey, String description);

    /**
     * 认领任务
     * zx
     */
    public Map<String, Object> claim(String businessKey, String taskId);

    /**
     * 单个完成任务
     */
    public Map<String, Object> complete(Map<String, Object> variables);

    public List<String> userInfo(String userId);

    /**
     * 批量任务完成
     *
     * @param allTask
     * @param variables
     * @return
     */
    public Map<String, Object> compeleteAll(List<Map<String, Object>> allTask, Map<String, Object> variables);

    /**
     * 已办任务查询
     *
     * @param description
     * @param processDefinitionKey
     * @return
     */
    public List<Map<String, Object>> allComplete(String processDefinitionKey, String description);

    /**
     * 撤销任务
     *
     * @param businessKey  流程id
     * @param deleteReason 撤销原因
     * @return
     */
    public Map<String, Object> deleteProcess(String businessKey, String deleteReason);

    /**
     * 多候选人或多候选组任务完成
     *
     * @param variables userOrGroup 多人：users 多组：groups
     * @return
     */
    public Map<String, Object> usersComplete(Map<String, Object> variables);

    /**
     * 插入协同处理人（调度事件单，并行任务不可用）
     *
     * @param map
     * @return
     */
    public Map<String, Object> insertUser(Map<String, Object> map);

    /**
     * 定时任务完成
     *
     * @param variable
     * @return
     */
    public Map<String, Object> quartzCompelte(Map<String, Object> variable);

    /**
     * 接口创建流程
     *
     * @param businessKey
     * @param processDefinitionKey
     * @param variables
     * @return
     */
    public Map<String, Object> quartzstartProcess(String businessKey, String processDefinitionKey, Map<String, Object> variables);

    /**
     * 通过业务ID 任务描述 查询task对象 确认 任务是否存在
     *
     * @param businesskey 业务Id 必输
     * @param userId 任务描述 必输
     * @return
     */
    public Task getUserTask(String businesskey,String userId);

    /**
     * 通过业务ID 任务描述 查询task对象 确认 任务是否存在
     *
     * @param businessKey 业务Id 必输
     * @param description 任务描述 必输
     * @return
     */
    public Task getTask(String businessKey,String description);

    /**
     * 根据taskId查询任务
     * @param taskId
     * @return
     */
    public Task getTaskByTaskId(String taskId);

    /**
     * 根据流程实例id查询
     *
     * @param processInstanceId  流程实例id
     * @return
     */
    public Task getTaskByProcessInstanceId(String processInstanceId);

    /**
     * 根据businessKey 查看流程是否存在
     *
     * @param businessKey
     * @return
     */
    public List<ProcessInstance> processInfo(String businessKey);

    /**
     * 流程图
     *
     * @param businessKey
     * @return
     */
    InputStream readResource(String businessKey);
    List<Map<String, Object>> processDefinetionKeyTasks(String processKey);
    /**
     * 撤回任务到上一步
     * @param businessKey
     * @param userId 操作人，只有上一步操作人有权撤回
     * @param processDefinitionKey 流程key
     * @throws Exception
     */
    public void revokeTask(String businessKey,String userId,String processDefinitionKey) throws Exception;
    /**
     * 发起人撤回
     * @param businessKey
     * @param userId
     * @param processDefinitionKey 流程key
     * @throws Exception
     */
    public void revokeCreateTask(String businessKey,String userId,String processDefinitionKey)throws Exception;
    /**
     * 用户待办 简略
     * @param userId
     * @param processDefinitionKey
     * @return
     */
    public List<Task> allTasks(String userId,String processDefinitionKey );
    /**
     * 查询用户任务总数
     * @param userId
     * @param processDefinitionKey
     * @return
     */
    public Long userTaskCount(String userId,String processDefinitionKey);

    /**
     * 通过业务id查询流程信息
     *
     * */
    List<Map<String, Object>> processBusinesskeyKeyTask(String businesskey);

}
