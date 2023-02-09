package com.ruoyi.system.http.entegorserver.entity;

/**
 * @author 14735
 */
public class EntegorConstances {

    /**
     * 1-作业调度 2-信息采集 3-变更管理 4-灾备切换 5-定时任务 6-应急操作
     */
    public static final String MODUL_TYPE_1 = "1";
    public static final String MODUL_TYPE_2 = "2";
    public static final String MODUL_TYPE_3 = "3";
    public static final String MODUL_TYPE_4 = "4";
    public static final String MODUL_TYPE_5 = "5";
    public static final String MODUL_TYPE_6 = "6";

    /**
     * tasktype为指令类型，1-提交  2-审批 4-执行、
     */
    public static final String TASK_TYPE_1 = "1";
    public static final String TASK_TYPE_2 = "2";
    public static final String TASK_TYPE_4 = "4";

    /**
     * 启动类型  0-直接启动   1-定时启动（按照startTime的时间）
     */
    public static final String START_MODE_0 = "0";
    public static final String START_MODE_1 = "1";
}
