package com.ruoyi.form.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件单状态常量枚举
 */
public enum ProblemStatus {
    WAIT_SUBMIT("1", "待提交"),
    COMPLIANCE_AUDIT("2", "合规审核"),
    TECHNOLOGY_AUDIT("3", "技术审核"),
    ASSIGNED("4", "已指派"),
    UNDER_INVESTIGATION("5", "调查中"),
    SOLUTION_PENDING("6", "根因已明解决方案未定"),
    SOLUTION_AUDIT("7", "解决方案审核中"),
    SOLVING("8", "根因已明解决中"),
    AUDITOR_CONFIRMING("9", "已解决审核人确认中"),
    ADMIN_CONFIRMING("12", "已解决管理员确认中"),
    ORIGINATOR_CONFIRMING("10", "已解决发起人确认中"),
    ORIGINATE_DEPART_MANAGER_CONFIRMING("11", "已解决发起部室经理确认中"),
    GENERAL_MANAGER_AUDIT("13", "已解决总经理室审批中"),
    OBSERVING("14", "观察中"),
    CANCEL("15", "已取消"),
    CLOSE("16", "已关闭");

    private String code;
    private String info;

    ProblemStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 根据编号获取名称
     *
     * @param code:编号
     **/
    public static String getName(String code) {
        ProblemStatus[] status = values();
        for (ProblemStatus problemStatus : status) {
            if (code.equals(problemStatus.getCode())) {
                return problemStatus.getInfo();
            }
        }
        return null;
    }

    /**
     * 未提交（草稿）
     * 处理中（合规审核、技术审核、已指派、调查中、根因已明解决方案未定、解决方案审核中、根因已明解决中），
     * 已解决（包括已解决审核人确认中、已解决管理员确认中、已解决发起人确认中、已解决发起部室经理确认中、已解决总经理室审批中、观察中），
     * 已关闭（包括已取消和已关闭）
     */
    public static Map<String, String> stageMap = new HashMap<>();
    static {
        stageMap.put(WAIT_SUBMIT.getInfo(), ProblemStage.WAIT_SUBMIT.getInfo());
        stageMap.put(COMPLIANCE_AUDIT.getInfo(), ProblemStage.PROCESSING.getInfo());
        stageMap.put(TECHNOLOGY_AUDIT.getInfo(), ProblemStage.PROCESSING.getInfo());
        stageMap.put(ASSIGNED.getInfo(), ProblemStage.PROCESSING.getInfo());
        stageMap.put(UNDER_INVESTIGATION.getInfo(), ProblemStage.PROCESSING.getInfo());
        stageMap.put(SOLUTION_PENDING.getInfo(), ProblemStage.PROCESSING.getInfo());
        stageMap.put(SOLUTION_AUDIT.getInfo(), ProblemStage.PROCESSING.getInfo());
        stageMap.put(SOLVING.getInfo(), ProblemStage.PROCESSING.getInfo());
        stageMap.put(AUDITOR_CONFIRMING.getInfo(), ProblemStage.SOLUTION.getInfo());
        stageMap.put(ADMIN_CONFIRMING.getInfo(), ProblemStage.SOLUTION.getInfo());
        stageMap.put(ORIGINATOR_CONFIRMING.getInfo(), ProblemStage.SOLUTION.getInfo());
        stageMap.put(ORIGINATE_DEPART_MANAGER_CONFIRMING.getInfo(), ProblemStage.SOLUTION.getInfo());
        stageMap.put(GENERAL_MANAGER_AUDIT.getInfo(), ProblemStage.SOLUTION.getInfo());
        stageMap.put(OBSERVING.getInfo(), ProblemStage.SOLUTION.getInfo());
        stageMap.put(CANCEL.getInfo(), ProblemStage.CLOSED.getInfo());
        stageMap.put(CLOSE.getInfo(), ProblemStage.CLOSED.getInfo());

    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
