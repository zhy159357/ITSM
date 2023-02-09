package com.ruoyi.form.enums;

/**
 * 业务类型枚举
 */
public enum BusTypeEnum {

    /**
     * 事件单
     */
    BUS_SJ("01", "事件单"),
    /**
     * 问题单
     */
    BUS_WT("02", "问题单"),
    /**
     * 变更单
     */
    BUS_BG("03", "变更单");

    private String code;
    private String name;

    BusTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
