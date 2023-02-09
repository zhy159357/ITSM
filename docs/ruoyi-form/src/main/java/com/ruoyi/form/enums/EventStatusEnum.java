package com.ruoyi.form.enums;

import com.ruoyi.common.utils.StringUtils;

/**
 * 事件单状态常量枚举
 */
public enum EventStatusEnum {
    CREATED("1", "新建"),
    ASSIGNED("2", "已分派"),
    PROCESS("3", "进行中"),
    RESOLVED("4", "已解决"),
    CLOSED("5", "已关闭"),
    COMPLETION("6", "待补全");

    private String code;
    private String info;

    EventStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static String getStatusName(String code) {
        if(StringUtils.isNotEmpty(code)) {
            for (EventStatusEnum statusEnum : values()) {
                if(code.equals(statusEnum.getCode())) {
                    return statusEnum.getInfo();
                }
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
