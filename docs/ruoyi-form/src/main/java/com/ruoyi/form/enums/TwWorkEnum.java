package com.ruoyi.form.enums;

/**
 * 用户状态
 *
 * @author ruoyi
 */
public enum TwWorkEnum {
    /**
     * 暂存
     */
    SAVE(0, "暂存"),
    /**
     * 提交
     */
    SUBMIT(1, "提交"),
    /**
     * 待提交
     */
    TO_BE_SUBMITTED(2, "待提交"),
    /**
     * 初审
     */
    FIRST_TRIAL(3, "初审"),
    /**
     * tinyWeb流程 key
     */
    PROCESS_KEY(4, "FWSQ");

    private final Integer code;


    private final String info;

    TwWorkEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
