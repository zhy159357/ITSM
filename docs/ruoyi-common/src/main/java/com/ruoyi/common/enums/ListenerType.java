package com.ruoyi.common.enums;

/**
 * 监听器分类  执行监听和任务监听
 */
public enum ListenerType {

    // 执行监听器
    EXECUTION_LISTENER("0", "execution"),

    // 任务监听器
    TASK_LISTENER("1", "task");

    private final String code;
    private final String name;

    ListenerType(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getName()
    {
        return this.name;
    }
}
