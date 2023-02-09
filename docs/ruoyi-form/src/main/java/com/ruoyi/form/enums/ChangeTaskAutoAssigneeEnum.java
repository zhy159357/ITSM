package com.ruoyi.form.enums;

public enum ChangeTaskAutoAssigneeEnum {
    TASK_1("task1"),
    TASK_2("task2"),
    TASK_3("task3"),
    TASK_4("task4"),
    TASK_5("task5");
    private String name;

    ChangeTaskAutoAssigneeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
