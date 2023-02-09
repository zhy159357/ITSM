package com.ruoyi.common.core.domain.entity;

/**
 * cmdb传递参数
 */
public class Items {

    private String field;

    private String operator;

    private String value;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Items{" +
                "field='" + field + '\'' +
                ", operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
