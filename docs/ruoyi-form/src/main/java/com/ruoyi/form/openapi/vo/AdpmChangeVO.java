package com.ruoyi.form.openapi.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdpmChangeVO {
    private String CurrentUserCode;
    private String appProcessId;
    private String title;
    private String description;
    private String verification;
    private String type;
    private String urgentReason;
    private String reason;
    private String appVersion;
    private String basisType;
    private List<AdpmBasisVO> basis;
    private List<AdpmChangeTaskVO> taskContent;
}
