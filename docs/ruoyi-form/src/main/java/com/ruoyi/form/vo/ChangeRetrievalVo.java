package com.ruoyi.form.vo;

import com.ruoyi.form.domain.DesignBizChange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ChangeRetrievalVo
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/11/9 9:27
 */
@ApiModel("变更单搜索条件")
@Data
public class ChangeRetrievalVo {

    @ApiModelProperty(value = "发起开始时间")
    private String applyEarlyData;
    @ApiModelProperty(value = "发起结束时间")
    private String applyLateData;
    @ApiModelProperty(value = "计划开始开始时间")
    private String planStartEarlyDate;
    @ApiModelProperty(value = "计划开始结束时间")
    private String planStartLateDate;
    @ApiModelProperty(value = "计划结束开始时间")
    private String planEndEarlyDate;
    @ApiModelProperty(value = "计划结束结束时间")
    private String planEndLateDate;
    @ApiModelProperty(value = "实际实施开始时间")
    private String implementEarlyDate;
    @ApiModelProperty(value = "实际实施结束时间")
    private String implementLateDate;
    @ApiModelProperty(value = "实际完成开始时间")
    private String implementOverEarlyDate;
    @ApiModelProperty(value = "实际完成结束时间")
    private String implementOverLateDate;
    @ApiModelProperty(value = "变更单业务主表")
    private DesignBizChange designBizChange;
    @ApiModelProperty(value = "变更单导出对象")
    private ExportExcelParams excelParams;


}
