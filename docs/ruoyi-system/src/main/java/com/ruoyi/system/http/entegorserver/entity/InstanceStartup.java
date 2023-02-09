package com.ruoyi.system.http.entegorserver.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 实例启动
 */
public class InstanceStartup {
    /**
     * 业务系统名称（Excel上传步骤页的sheet名称）
     */
    @JSONField(name = "IINSTANCENAME")
    private String IINSTANCENAME;
    /**
     * 变更单号(该业务系统下变更时的唯一编号)
     */
    @JSONField(name = "IVERSIONALIAS")
    private String IVERSIONALIAS;

    /**
     * 变更说明
     */
    @JSONField(name = "InstName_input")
    private String InstName_input;
    /**
     * 部署环境 （多个环境用逗号分隔）
     */
    @JSONField(name = "DeploymentEnv")
    private String DeploymentEnv;
    /**
     * 启动用户
     */
    private String startUserName;
    /**
     * 启动类型  0-直接启动   1-定时启动（按照startTime的时间）
     */
    private String startMode;
    /**
     * 定时启动时间
     */
    private String startTime;

    /**
     * 密码
     */
    private String password;

    /**
     * 指令类型
     */
    private String tasktype;

    public String getIINSTANCENAME() {
        return IINSTANCENAME;
    }

    public void setIINSTANCENAME(String IINSTANCENAME) {
        this.IINSTANCENAME = IINSTANCENAME;
    }

    public String getIVERSIONALIAS() {
        return IVERSIONALIAS;
    }

    public void setIVERSIONALIAS(String IVERSIONALIAS) {
        this.IVERSIONALIAS = IVERSIONALIAS;
    }

    public String getInstName_input() {
        return InstName_input;
    }

    public void setInstName_input(String instName_input) {
        this.InstName_input = instName_input;
    }

    public String getDeploymentEnv() {
        return DeploymentEnv;
    }

    public void setDeploymentEnv(String deploymentEnv) {
        this.DeploymentEnv = deploymentEnv;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public String getStartMode() {
        return startMode;
    }

    public void setStartMode(String startMode) {
        this.startMode = startMode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }
}
