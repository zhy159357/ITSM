package com.ruoyi.system.enums;

/**
 * @author jiangfeng
 * 優先級
 * @date 2022-06-21
 */

public enum PriorityParaValueEnum {

    TEST1("textPriority","event_impact_degree", "event_impact_scope", "注释");

    private String code;

    private String degreeCode;

    private String scopeCode;

    private String desc;


    PriorityParaValueEnum(String code, String degreeCode,
                          String scopeCode, String desc) {
        this.code = code;
        this.degreeCode = degreeCode;
        this.scopeCode = scopeCode;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDegreeCode() {
        return degreeCode;
    }

    public String getScopeCode() {
        return scopeCode;
    }

    public String getDesc() {
        return desc;
    }

    public static PriorityParaValueEnum find(String code) {
        for (PriorityParaValueEnum value : PriorityParaValueEnum.values()) {

            if (value.code.equalsIgnoreCase(code)) {

                return value;

            }
        }
        return null;
    }
}
