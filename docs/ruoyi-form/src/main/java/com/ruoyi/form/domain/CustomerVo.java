package com.ruoyi.form.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName CustomerVo
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/8/19 9:47
 */
@Data
@ApiModel(value = "customerVo")
public class CustomerVo implements Cloneable {
    @ApiModelProperty(name = "customerFormContent",value = "自定义数据对象")
    private CustomerFormContent customerFormContent;
    @ApiModelProperty(name = "variables",value = "审批对象",dataType = "json")
    private Map<String,Object> variables;
    @ApiModelProperty(name = "notRelatedReason",value = "问题单已解决不关联变更理由")
    private String notRelatedReason;

    // 事件单告警批量关闭事件单号，逗号隔开
    private String eventNos;

    // 批量关单详情描述信息
    private String descMsg;

    @Override
    public CustomerVo clone(){
        CustomerVo vo = null;
        try {
            vo = (CustomerVo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return vo;
    }
}
