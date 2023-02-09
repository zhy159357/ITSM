package com.ruoyi.form.enums;

public enum ChangeTableNameEnum {
    CHANGE("design_biz_change"),
    CHANGE_TASK("design_biz_changeTask");
    private String name;

    ChangeTableNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
