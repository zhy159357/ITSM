package com.ruoyi.common.enums;

/**
 * 监听器业务分类
 */
public enum ListenerBusinessType {
    /**
     * 事件单
     */
    EVENT_BILL("01", "事件单"),
    /**
     * 问题单
     */
    PROBLEM_BILL("02", "问题单"),
    /**
     * 变更单
     */
    CHANGE_BILL("03", "变更单"),

    /**
     * 下线管理
     */
    OFFLINE_MANAGE("04", "下线管理"),

    /**
     * 服务目录
     */
    SERVICE_DIRECTORY("05", "服务目录");

    private final String businessCode;
    private final String businessName;

    ListenerBusinessType(String businessCode, String businessName)
    {
        this.businessCode = businessCode;
        this.businessName = businessName;
    }

    public String getBusinessCode()
    {
        return this.businessCode;
    }

    public String getBusinessName()
    {
        return this.businessName;
    }
}
