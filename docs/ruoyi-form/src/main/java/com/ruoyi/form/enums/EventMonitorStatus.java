package com.ruoyi.form.enums;

public enum EventMonitorStatus {
    // dispose  待处理
    // close    已关闭
    // processing  处理中
    // resolved   已解决
    dispose("dispose", "待处理"),
    close("close", "已关闭"),
    processing("processing", "处理中"),
    resolved("resolved", "已解决");

    private String code;

    private String info;

    EventMonitorStatus(String code, String info) {
        this.code = code;
        this.info = info;
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

    public static String getName(String code) {
        EventMonitorStatus[] status = values();
        for (EventMonitorStatus eventMonitorStatus : status) {
            if (code.equals(eventMonitorStatus.getCode())) {
                return eventMonitorStatus.getInfo();
            }
        }
        return null;
    }
}
