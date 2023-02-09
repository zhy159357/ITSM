package com.ruoyi.form.enums;


public enum ChangeTaskDeptEnum {
    appSupport("应用支持部"),
    system("系统设备部"),
    netWork("网络通讯部"),
    baseManager("基础管理部"),
    operation("运维平台部"),
    production("生产管理部"),
    monitor("运行监控部");
    private final String name;

    ChangeTaskDeptEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
