package com.ruoyi.form.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel(description = "itsm 告警信息")
@AllArgsConstructor
public class FormAlert {

    @ApiModelProperty(value = "告警id")
    private Long alertId;

    @ApiModelProperty(value = "告警对象")
    private String object;
    
    @ApiModelProperty(value = "告警内容")
    private String content;
    
    @ApiModelProperty(value = "告警级别（0：恢复，20：通知，30：次要，40：一般，50：严重，60：紧急）")
    private Integer severity;
    private String severityStr;

    @ApiModelProperty(value = "告警时间")
    private Long beginTime;
    private String beginTimeStr;
    
    @ApiModelProperty(value = "告警状态")
    private String status;

    public FormAlert(Long alertId, String object, String content, String severityStr, String beginTimeStr, String status) {
        this.alertId = alertId;
        this.object = object;
        this.content = content;
        this.severityStr = severityStr;
        this.beginTimeStr = beginTimeStr;
        this.status = status;
    }
}
