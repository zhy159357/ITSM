package com.ruoyi.form.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventIncSearchVo {

    private String eventNo;// 柜面事件编号
    private String evenTile;// 事件标题
    private String imCode;// IM事件编号
    private String systemName;// 应用系统
    private String eventStatus;// 事件状态
    private String reportPerson;// 上报人
}
