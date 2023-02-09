package com.ruoyi.form.enums;

public enum ChangeBasisTypeEnum {
    EVENT("1"),
    PROBLEM("2"),
    SERVER("3"),
    REQUIREMENT("4");
    private String value;

    ChangeBasisTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
