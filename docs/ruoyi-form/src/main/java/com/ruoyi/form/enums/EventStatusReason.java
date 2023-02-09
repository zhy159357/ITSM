package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventStatusReason {

    /**
     * 状态理由
     * Assigned_HelpDesk: 分派到服务台
     * Assigned_OneLine：分派到一线
     * Assigned_TwoLine：分派到二线
     * Progress_OneLine：一线处理中
     * Progress_TwoLine：二线处理中
     * Wait_ThreeOther：等待第三方厂商
     * Progress_HelpDesk：服务台处理中
     * Check_HelpDesk：发起人确认中
     */
    Assigned_HelpDesk("Assigned_HelpDesk", "分派到服务台"),
    Assigned_OneLine("Assigned_OneLine", "分派到一线"),
    Assigned_TwoLine("Assigned_TwoLine", "分派到二线"),
    Progress_OneLine("Progress_OneLine", "一线处理中"),
    Progress_TwoLine("Progress_TwoLine", "二线处理中"),
    Wait_ThreeOther("Wait_ThreeOther", "等待第三方厂商"),
    Progress_HelpDesk("Progress_HelpDesk", "服务台处理中"),
    Check_HelpDesk("Check_HelpDesk", "发起人确认中");

    private String code;
    private String info;
}
