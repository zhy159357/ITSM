package com.ruoyi.form.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ExportExcelParams
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/9/26 15:16
 */
@Data
public class ExportExcelParams {
    @ApiModelProperty(value = "导出模块CODE")
    private String code;
    @ApiModelProperty(value = "导出类型：1-全部  2-导出选中")
    private String type;
    @ApiModelProperty(value = "选中的ID集合")
    private List<String> ids;
}
