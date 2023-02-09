package com.ruoyi.form.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件单状态常量枚举
 */
public enum ProblemTaskStatus {
    WAIT_SUBMIT("1", "待提交"),
    ASSIGNED("2", "已指派"),
    SOLVING("3", "处理中"),
    CLOSING("4", "申请关闭中"),
    CANCEL("5", "已取消"),
    CLOSE("6", "已关闭");


    private String code;
    private String info;

    ProblemTaskStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 根据编号获取名称
     *
     * @param code:编号
     **/
    public static String getName(String code) {
        ProblemTaskStatus[] status = values();
        for (ProblemTaskStatus problemStatus : status) {
            if (code.equals(problemStatus.getCode())) {
                return problemStatus.getInfo();
            }
        }
        return null;
    }

    /**
     * 未提交（待提交）
     * 处理中（合规审核、技术审核、已指派、调查中、根因已明解决方案未定、解决方案审核中、根因已明解决中），
     * 已解决（包括已解决审核人确认中、已解决管理员确认中、已解决发起人确认中、已解决发起部室经理确认中、已解决总经理室审批中、观察中），
     * 已关闭（包括已取消和已关闭）
     */
    public static Map<String, String> stageMap = new HashMap<>();
    static {
        stageMap.put(WAIT_SUBMIT.getInfo(), ProblemStage.WAIT_SUBMIT.getInfo());
        stageMap.put(ASSIGNED.getInfo(), ProblemStage.PROCESSING.getInfo());
        stageMap.put(SOLVING.getInfo(), ProblemStage.PROCESSING.getInfo());
        stageMap.put(CLOSING.getInfo(), ProblemStage.SOLUTION.getInfo());
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
