package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventIncSource {
    /**
     * 报障的来源
     * Phone：电话
     * Monitor：监控
     * Daily：日常检查
     * Service：客服派单
     * Email：邮件报修
     * Capacity：容量管理
     * Emergency：应急演练
     * Other：其他
     * SelfService：网页报障
     * MobileService：移动报障
     * IMService：IM报障
     * TIPService：柜面报障
     * ESTService：E事通
     */
    Phone("Phone", "电话"),
    Monitor("Monitor", "监控"),
    Daily("Daily", "日常检查"),
    Service("Service", "客服派单"),
    Email("Email", "邮件报修"),
    Capacity("Capacity", "容量管理"),
    Emergency("Emergency", "应急演练"),
    Other("Other", "其他"),
    SelfService("SelfService", "网页报障"),
    MobileService("MobileService", "移动报障"),
    IMService("IMService", "IM报障"),
    TIPService("TIPService", "柜面报障");

    private String code;
    private String info;
}
