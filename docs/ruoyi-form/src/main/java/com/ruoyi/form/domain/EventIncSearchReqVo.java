package com.ruoyi.form.domain;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventIncSearchReqVo {
    // 起始时间
    private String stratTime;
    // 结束时间
    private String endTime;
    // 柜面事件编号
    private String eventNo;
    // IM事件编号
    private String imCode;
    // 操作员号
    private String transTeller;
    // 员工号
    private String transNo;
    // 交易网点号
    private String transInstno;
    // 事件状态
    private String imStatus;
    // 当前页数
    private Integer pageSize;
    // 每页条数
    private Integer pageNum;

    private Integer pageStart;
    //状态
    private List status;

    private int isTimeFlag;
}
