package com.ruoyi.common.core.domain.entity;

/**
 * cmdb接受参数
 */
public class Conditions {
    private String field;
    private String value;
    public void setField(String field) {
        this.field = field;
    }
    public String getField() {
        return field;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "Conditions{" +
                "field='" + field + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
