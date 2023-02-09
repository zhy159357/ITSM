package com.ruoyi.form.openapi.vo;

import lombok.Data;

@Data
public class EChangeVO {
    private String userNo;
    private String type;
    private String startDate;
    private String endDate;
    private Long pageNum;
    private Long pageSize;
}
