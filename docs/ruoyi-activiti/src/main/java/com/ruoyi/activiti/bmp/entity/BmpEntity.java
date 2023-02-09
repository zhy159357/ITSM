package com.ruoyi.activiti.bmp.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作流程封装参数对象
 */
@Data
public class BmpEntity {
    // 业务工单主键id
    private String businessKey;
    // 流程任务id
    private String taskId;
    // 当前任务节点名称
    private String taskName;
    // 任务节点描述
    private String description;
    // 流程定义key
    private String processDefinitionKey;
    // 流程定义id
    private String processDefinitionId;
    // 流程定义version
    private Integer processDefinitionVersion;
    // 流程实例id
    private String processInstanceId;
    // 流程启动人
    private String startUser;
    // 流程名称
    private String processName;
    // 流程开启时间
    private Date startTime;
    // 流程节点任务操作时间
    private Date createTime;
    // 流程当前处理人id
    private String userId;
    // 流程变量map（启动或流转过程包含分支控制条件和下一步处理人控制条件）
    private Map<String, Object> processVariables;

    // 流程分支条件key，类似流程分支上配置的${reCode==1}这样的el表达式，map的key="reCode"
    private String flowConditionKey;
    // 流程分支条件value，控制流程走向的真实条件
    private String flowConditionValue;

    // 下一步审批人｜工作组｜角色key，类似流程节点上配置的${groupId}这样的el表达式，map的key="groupId"
    private String flowApprovalKey;
    // 下一步审批人｜工作组｜角色value，控制下一步处理人的真实值，传入值可能是(userId,groupId,rid)类型的id
    private String flowApprovalValue;

    // 是否组任务 默认true
    private boolean groupFlag = true;
    // 组任务id
    private String groupId;

    // 是否多人任务 默认false
    private boolean usersFlag = false;

    // 流程启动、流转成功标识
    private boolean successFlag;
    // 流程启动或流转成功｜失败描述信息
    private String flowMessage;

    // 流程审批意见
    private String comment;

    // 组装流条件map集合键值对
    public Map<String, Object> flowConditionMap () {
        if(this.processVariables == null) {
            this.processVariables = new HashMap<>();
        }
        if(StringUtils.isNotEmpty(this.flowConditionKey) && StringUtils.isNotEmpty(this.flowConditionValue)) {
            this.processVariables.put(this.flowConditionKey, this.flowConditionValue);
        }
        return this.processVariables;
    }

    // 组装下一步审批人|组|角色map集合键值对
    public Map<String, Object> flowApprovalMap () {
        if(this.processVariables == null) {
            this.processVariables = new HashMap<>();
        }
        if(StringUtils.isNotEmpty(this.flowApprovalKey) && StringUtils.isNotEmpty(this.flowApprovalValue)) {
            this.processVariables.put(this.flowApprovalKey, this.flowApprovalValue);
        }
        return this.processVariables;
    }
}
