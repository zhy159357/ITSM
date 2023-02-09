package com.ruoyi.form.enums;



/**
 * 问题单阶段常量枚举
 */
public enum ProblemStage {
    WAIT_SUBMIT("1", "未提交"),
    PROCESSING("2", "处理中"),
    SOLUTION("3", "已解决"),
    CLOSED("4", "已关闭");

    private String code;
    private String info;

    ProblemStage(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 根据编号获取名称
     *
     * @param code:编号
     **/
    public static String getName(String code) {
        ProblemStage[] statges = values();
        for (ProblemStage stage : statges) {
            if (code.equals(stage.getCode())) {
                return stage.getInfo();
            }
        }
        return null;
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
