package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventOperationType {

    CREATE_AND_ASSIGN("CREATE_AND_ASSIGN", "创建并分派事件单"),
    RECEIVE_EVENT("RECEIVE_EVENT", "接受事件单"),
    BACK_EVENT("BACK_EVENT", "退回事件单"),
    SOLVE_EVENT("SOLVE_EVENT", "解决事件单"),
    ASSIGN_EVENT("ASSIGN_EVENT", "转派事件单"),
    GENERAL_INFORMATION("GENERAL_INFORMATION", "常规信息"),
    CLOSE_EVENT("CLOSE_EVENT", "关闭事件单"),
    APPLY_COMPLETE_INFORMATION("APPLY_COMPLETE_INFORMATION", "申请补全信息"),
    COMPLETE_INFORMATION("COMPLETE_INFORMATION", "补全信息"),
    ROB_TASK("ROB_TASK", "抢单");

    private String code;
    private String info;
}
