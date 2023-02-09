package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件优先级
 */
@AllArgsConstructor
@Getter
public enum EventPriority {
    /**
     * Critical：P1
     * High：P2
     * Medium：P3
     * Low：P4
     */
    Critical("Critical", "P1"),
    High("High", "P2"),
    Medium("Daily", "P3"),
    Low("Low", "P4");

    private String code;
    private String info;
}
