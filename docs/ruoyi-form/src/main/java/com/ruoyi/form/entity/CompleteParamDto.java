package com.ruoyi.form.entity;

import com.ruoyi.form.domain.CustomerFormContent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CompleteParamDto
 * @Description 审批接口入参
 * @Author JiaQi Zhang
 * @Date 2022/10/3 9:07
 */
@Data
@ApiModel(value = "completeParamDto")
public class CompleteParamDto {
    @ApiModelProperty(value = "业务表编码CODE",required = true)
    private String code;

    @ApiModelProperty(value = "任务ID",required = true)
    private String taskId;

    @ApiModelProperty(value = "流程实例ID",required = true)
    private String instanceId;

    @ApiModelProperty(value = "按钮配置ID",required = true)
    private String buttonConfigId;

    @ApiModelProperty(value = "自定义对象",required = true,dataType = "CustomerFormContent")
    private CustomerFormContent customerFormContent;

    @ApiModelProperty(value = "审批人对象值  即动态选择下一节点审批对象的ID值")
    private String approveObject;

    @ApiModelProperty(value = "审批意见")
    private String approveOpinion;


}
