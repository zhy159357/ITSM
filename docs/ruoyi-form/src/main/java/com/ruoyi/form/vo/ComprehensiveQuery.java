package com.ruoyi.form.vo;

import com.ruoyi.form.domain.CustomerFormSearchDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ComprehensiveQuery
 * @Description 综合查询接参对象
 * @Author JiaQi Zhang
 * @Date 2022/9/20 13:49
 */
@Data
@ApiModel(description = "综合查询接参对象")
public class ComprehensiveQuery {

    @ApiModelProperty(value = "业务单子类型",required = true)
    private String code;

    @ApiModelProperty(value = "搜索条件对象集合",dataType = "CustomerFormSearchDTO",allowableValues = "true",required = true)
    private List<CustomerFormSearchDTO> searchDTOList;

}
