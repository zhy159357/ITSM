package com.ruoyi.form.enums;

public enum ChangeStatusEnum {
    unSubmit("待提交"),
    preApproval("变更预审"),
    prepared("变更准备"),
    managerApproval("变更经理审批"),
    branchManagerApproval("分行变更经理审批"),
    branchGeneralApproval("分行总经理审批"),
    approval("变更审批"),
    implement("变更实施中"),
    completed("已完成"),
    cancel("取消"),
    closed("已关闭");
    private String name;
    ChangeStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
