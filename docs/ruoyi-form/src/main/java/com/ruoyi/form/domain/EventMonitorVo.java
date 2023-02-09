package com.ruoyi.form.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventMonitorVo {

    private String imCode;// IM事件编号
    private String eventPriority;// 事件单等级

    private String selfHealingState;// 自愈状态结果
}
