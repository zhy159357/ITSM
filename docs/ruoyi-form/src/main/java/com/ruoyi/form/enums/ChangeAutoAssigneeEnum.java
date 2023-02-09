package com.ruoyi.form.enums;

public enum ChangeAutoAssigneeEnum {
    CHANGE_1("change1"),
    CHANGE_2("change2");
    private String name;

    ChangeAutoAssigneeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
