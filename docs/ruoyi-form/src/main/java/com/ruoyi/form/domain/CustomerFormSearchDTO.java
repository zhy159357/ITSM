package com.ruoyi.form.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CustomerFormSearchDTO
 * @Description 自定义表单搜索条件
 * @Author JiaQi Zhang
 * @Date 2022/6/21 8:43
 */
@Data
@ApiModel(description = "自定义表单搜索条件对象")
public class CustomerFormSearchDTO {

    @ApiModelProperty("搜索key键，某个表的字段")
    private String searchKey;

    @ApiModelProperty("搜索value值")
    private Object searchValue;

    @ApiModelProperty("搜索条件，大于、等于、小于之类")
    private String searchCondition;

}
