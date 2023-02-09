package com.ruoyi.form.enums;

public enum ChangeFieldEnum {
    /* CHANGE_TABLE_NAME("design_biz_change"),
     CHANGE_TASK_TABLE_NAME("design_biz_changeTask"),*/
    UPDATE_TIME("updated_time"),
    APP_PROCESS_ID("appProcessId"),
    EXTRA2("extra2"),
    APPROVAL_SUBMIT_DATE("approvalSubmitDate"),
    CHANGE_NO("changeNo"),
    INSTANCE_ID("instance_id"),
    CREATOR("created_by"),
    APPROVAL("approval"),
    STATUS("status"),
    ID("id"),
    EXTRA1("extra1"),
    CHANGE_STATUS("changeStatus"),
    TYPE("type"),
    START_DEPT("startDept"),
    CURRENT_RISK_LEVEL("currentRiskLevel"),
    RISK_LEVEL("riskLevel"),
    REFER_SYS("referApp"),
    CURRENT_PROCESSOR("currentProcessor"),
    SUBMIT_DATE("submitDate"),
    REASON("reason"),
    URGENT_REASON("urgentReason"),
    PLAN_START_DATE("planStartDate"),
    PLAN_COMPLETE_DATE("planCompleteDate"),
   /* IMPLEMENT_TAKE_DATE("implementTakeDate"),
    IMPLEMENT_STATEMENT_DATE("implementStatementDate"),*/
    IMPLEMENT_START_DATE("implementStartDate"),
    IMPLEMENT_OVER_DATE("implementOverDate"),
    DATE_OF_SUBMIT_ON_TIME("dateOfSubmitOnTime"),
    ONTIME("ontime"),
    BRANCH_FLAG("branchFlag"),
    CHANGE_LEVLE("changeLevel"),
    COMPLETION("completion"),
    CLOSE_CODE("closedCode"),
    BASIS("basis"),
    PRE_RECODE("pre_recode"),
    CHANGE_FINISH_TIME("changeFinishTime"),
    CHANGE_CLOSED_TIME("changeClosedTime");
    private String name;

    ChangeFieldEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
