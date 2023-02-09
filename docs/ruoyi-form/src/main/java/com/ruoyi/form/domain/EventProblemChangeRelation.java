package com.ruoyi.form.domain;

import lombok.Data;

@Data
public class EventProblemChangeRelation {

    private Long relationId;

    // 事件单编号
    private String imCode;

    // 关系类型
    private String relationType;

    // 请求类型
    private String requestType;

    // 请求转换单号
    private String requestNo;

    // 请求概要
    private String requestGeneral;
}
