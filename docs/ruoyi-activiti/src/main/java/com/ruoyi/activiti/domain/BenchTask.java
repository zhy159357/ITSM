package com.ruoyi.activiti.domain;

import java.io.Serializable;
/**
 * 工作台代办任务
 *
 * @author LiuLiang
 */
public class BenchTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private String taskId;//任务id
    private String bizId;//业务id
    private String number;//单号
    private String taskName;//任务名称
    private String title;//标题
    private String prePerformTime;//上一步操作时间
    private String type;//任务类型（必填）,用于区分特殊待办，例如公告、信息制度
    private String currentState;//任务状态（非必填），用于区分特殊待办（公告、信息制度），打开待办执行任务时，打开的页面
    private String flag;//标记（非必填），用于公告待处理状态的任务，阅件、执行件打开不同页面
    private String description;//任务描述

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrePerformTime() {
        return prePerformTime;
    }

    public void setPrePerformTime(String prePerformTime) {
        this.prePerformTime = prePerformTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
