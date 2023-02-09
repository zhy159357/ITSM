package com.ruoyi.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventCenterNet {
    Center("Center", "总行事件"),
    Net("Net", "分行事件");

    private String code;
    private String info;
}
