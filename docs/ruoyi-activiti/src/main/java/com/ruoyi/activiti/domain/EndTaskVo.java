package com.ruoyi.activiti.domain;

/**
 * 流程结束公共实体类
 */
public class EndTaskVo {

    /**
     * 任务单的key
     *
     */
    private String businesskey;
    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 任务单号
     */
    private String taskNo;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 标题
     */
    private String  taskTitle;

    /**
     *任务生成时间
     */
    private String taskGeneterTime;


    public String getBusinesskey() {
        return businesskey;
    }

    public void setBusinesskey(String businesskey) {
        this.businesskey = businesskey;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskGeneterTime() {
        return taskGeneterTime;
    }

    public void setTaskGeneterTime(String taskGeneterTime) {
        this.taskGeneterTime = taskGeneterTime;
    }
}
