package com.ruoyi.activiti.bmp.service;

import com.ruoyi.activiti.bmp.entity.BmpEntity;

import java.util.List;
import java.util.Map;

/**
 * 流程相关service类
 */
public interface IBmpService {
    /**
     * 流程启动方法（使用流程定义key和版本获取流程定义id，启动流程，如没有传参版本号，则默认启动最后一个版本）
     * @param bmpEntity
     */
    void startProcess(BmpEntity bmpEntity);

    /**
     * 流程审批方法封装 单人任务|多人任务
     * @param bmpEntity 流程审批所需要的参数
     */
    void complete(BmpEntity bmpEntity);

    /**
     * 挂起流程
     * @param processInstanceId 流程实例id
     */
    void suspendProcessInstance(String processInstanceId);

    /**
     * 激活流程
     * @param processInstanceId 流程实例id
     */
    void activateProcessInstance(String processInstanceId);

    /**
     * 查询待办（包含个人任务和组组任务）
     * @param bmpEntity
     * 参数包含userId|processDefinitionKey|description
     * @return
     */
    List<BmpEntity> getUserTask(BmpEntity bmpEntity);

    /**
     * 查询待办用户任务总数
     * @param userId
     * @param processDefinitionKey
     * @return
     */
    Long getUserTaskCount(String userId, String processDefinitionKey);

    /**
     * 检查业务流程是否存在
     * @param businessKey
     * @return true-表示已存在，false-表示不存在
     */
    boolean isExitProcess(String businessKey);

    /**
     * 撤销流程
     * @param id
     */
    void cancelProcess(String id);

    /**
     * 关闭流程
     *
     * @param id
     */
    void closeProcess(String id, String closeReason);
}
