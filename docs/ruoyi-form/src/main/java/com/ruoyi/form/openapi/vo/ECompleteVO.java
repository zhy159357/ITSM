package com.ruoyi.form.openapi.vo;

import lombok.Data;

import java.util.List;

@Data
public class ECompleteVO {
    private String changeNo;
    private String userCode;
    private String recode;
    private String taskId;
    private String instanceId;
    private String content;
    private List<String> adminOrgList;
}
