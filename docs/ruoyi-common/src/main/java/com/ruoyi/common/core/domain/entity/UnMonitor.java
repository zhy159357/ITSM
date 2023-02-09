package com.ruoyi.common.core.domain.entity;

import java.util.Map;

/**
 * 统一监控对接 输入参数
 */
public class UnMonitor {

    private String vapToken;

    private String clientId;

    private Map<String,String> expand;

    public String getVapToken() {
        return vapToken;
    }

    public void setVapToken(String vapToken) {
        this.vapToken = vapToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Map<String, String> getExpand() {
        return expand;
    }

    public void setExpand(Map<String, String> expand) {
        this.expand = expand;
    }
}
