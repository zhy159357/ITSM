package com.ruoyi.form.enums;

public enum  ChangeTaskStatusEnum {
    registered("已注册"),
    planReviewed("方案审核完成"),
    preApprovaling("预审中"),
    preApprovalPassed("预审通过"),
    waitImpl("待实施"),
    impling("实施中"),
    canceled("取消"),
    reviewed("复核中"),
    delay("延迟"),
    closed("已关闭");
    private String name;

    ChangeTaskStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
