package com.ruoyi.form.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ChangeTaskFieldParamMap {
    mergeFlag("or_not"),
    preTaskFlag("or_not"),
    referBusinessCenterFlag("or_not"),
    implResultCheck("change_task_impl_result"),
    deployWay("deploy_way"),
    fortressMachineFlag("or_not");
    private String param;
    public static List<String> fieldList;

    static {
        ChangeTaskFieldParamMap[] ChangeTaskFieldParamMapArray = ChangeTaskFieldParamMap.values();
        fieldList = Arrays.stream(ChangeTaskFieldParamMapArray).map(Enum::toString).collect(Collectors.toList());
    }
    ChangeTaskFieldParamMap(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}
