package com.ruoyi.form.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CustomerFormListDTO
 * @Description 自定义表单列表对象
 * @Author JiaQi Zhang
 * @Date 2022/6/24 8:44
 */
@Data
@ApiModel(description = "自定义表单列表对象")
public class CustomerFormListDTO {

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "搜索条件对象集合",dataType = "CustomerFormSearchDTO",allowableValues = "true")
    private List<CustomerFormSearchDTO> searchDTOList;

    @ApiModelProperty(value = "搜索条件用or或者and连接")
    private String conCondition;

}
