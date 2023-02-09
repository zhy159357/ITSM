package com.ruoyi.activiti.domain;

import lombok.Data;

import java.util.List;

@Data
public class VerbVo {

    private String severity;//告警等级（0：恢复，1：警告，2：错误，3：紧急）
    private String entityName;//告警对象
    private String name;//告警名称
    private String description;//告警名称
    private String id;//告警ID
    private String source;//告警来源
    private String status;//告警状态（0：未接手，40：已确认，150：处理中，190：已解决，255：已关闭）
    private String owner;//处理人id
    private List properties; //属性
    private String userName;//处理人
}
