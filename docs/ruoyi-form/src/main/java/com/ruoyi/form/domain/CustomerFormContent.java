package com.ruoyi.form.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel(description = "自定义表单新增/编辑传参对象")
public class CustomerFormContent {
    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    private String  code;
    @ApiModelProperty(value = "带数据的json",required = true)
    private String dataJson;
    @ApiModelProperty(value = "字段值",required = true)
    private Map<String, Object> fields;
    private String onlySaveFlag;
}
