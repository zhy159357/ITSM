package com.ruoyi.form.enums;

public enum ChangeRoleEnum {
    generalManager("总经理"),
    changeManger("变更经理"),
    branchGeneralManager("分行总经理"),
    branchChangeManager("分行变更经理");
    private String name;

    ChangeRoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
