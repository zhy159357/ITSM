package com.ruoyi.form.enums;

public enum ChangeTaskFieldEnum {
    /*TABLE_NAME("design_biz_changeTask"),*/
    UPDATED_TIME("updated_time"),
    REFER_INFRASTRUCTURE("refer_infrastructure"),
    REMEDY_REASON("remedyReason"),
    WANT_IMPL_TIME("wantImplTime"),
    PLAN_DES("planDescription"),
    CHANGE_TASK_VERIFICATION("changeTaskVerification"),
    TASK_CONTENT("taskContent"),
    BACKUP_STEP("backupStep"),
    OPERATE_STEP("operateStep"),
    VERIFI_STEP("verificationStep"),
    RETURN_STEP("returnStep"),
    INF_RANGE("infRange"),
    INSTANCE_ID("instance_id"),
    APPROVAL("approval"),
    STATUS("status"),
    ID("id"),
    EXTRA1("extra1"),
    CHANGE_NO("changeNo"),
    TASK_STATUS("taskStatus"),
    CHANGE_ID("changeId"),
    REMEDY_FLAG("remedyFlag"),
    REVIEWED("reviewed"),
    TASKDEPT("taskDept"),
    ASSIGNEE("assignee"),
    ASSIGNEE_GROUP("assignedGroup"),
    PRE_APPROVAL("preApproval"),
    CHECK_MAN("checkMan"),
    BRANCH_ID("branchId"),
    CURRENT_PROCESSOR("currentProcessor"),
    PRE_APPROVAL_BACK_FLAG("preApprovalBackFlag"),
    BACK_TO_APPROVAL_FLAG("backToApprovalFlag"),
    LOCK_FLAG("lockFlag"),
    TYPE("type"),
    MERGE_FLAG("mergeFlag"),
    MERGE_TASK_NO("mergeTaskNo"),
    REFER_SYS("referSys"),
    DEPLOY_WAY("deployWay"),
    PLAN_START_DATE("planStartDate"),
    PLAN_OVER_DATE("planOverDate"),
    IMPL_START_DATE("implStartDate"),
    IMPL_END_DATE("implEndDate"),
    IMPL_RECEIVE_DATE("implReceivedDate"),
    IMPL_OVER_DATE("implOverDate"),
    IMPL_RESULT("implResultCheck"),
    CHANGE_TASK_NO("changeTaskNo"),
    PRE_TASK_FLAG("preTaskFlag"),
    PRE_TASK_NO("preTaskNo"),
    IMPL_MAN("impl_man"),
    SCHEDULE_MAN("scheduleMan"),
    SCHEDULE_DATE("scheduleDate"),
    AUTO_TASK_FLAG("autoTaskFlag"),
    UPLOAD_FILE("enclosureUploadFile"),
    REFER_CENTER_FLAG("referBusinessCenterFlag"),
    BACK_TO_START("back_to_start"),
    LOCK("adpm_lock"),
    REFER_FLAG("refer_flag");
    private String name;

    ChangeTaskFieldEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
