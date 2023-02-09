package com.ruoyi.form.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ChangeFieldParamMap {
    type("itsm_change_type"),
    basisType("change_basis_type"),
    reason("change_reason"),
    urgentReason("itsm_urgent_change_reason"),
    currentRiskLevel("change_risk_level"),
    changeLevel("change_level"),
    supplyProcess("or_not"),
    incident_relation_flag("or_not");
    private String param;
    public static List<String> fieldList;

    static {
        ChangeFieldParamMap[] changeFieldParamMapArray = ChangeFieldParamMap.values();
        fieldList = Arrays.stream(changeFieldParamMapArray).map(Enum::toString).collect(Collectors.toList());
    }

    ChangeFieldParamMap(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}
