package com.ruoyi.form.vo;

import com.ruoyi.form.domain.DesignBizIncident;
import com.ruoyi.form.domain.IncidentSubEvent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName IncidentRetrievalVo
 * @Description 事件单检索页面接参对象
 * @Author JiaQi Zhang
 * @Date 2022/11/2 14:04
 */

@Data
@ApiModel("事件单检索页面接参对象")
public class IncidentRetrievalVo {

    @ApiModelProperty("事件单业务主表")
    private DesignBizIncident designBizIncident;
    @ApiModelProperty("事件单业务从表")
    private IncidentSubEvent incidentSubEvent;

    @ApiModelProperty("分派人")
    private String todoPersonal;
    @ApiModelProperty("分派组")
    private String todoGroup;

    @ApiModelProperty("经手人")
    private String brokerage;
    @ApiModelProperty("开单人")
    private String createdBy;
    @ApiModelProperty("开单组")
    private String createdGroupBy;
    @ApiModelProperty("提交开始时间")
    private String submitStartTime;
    @ApiModelProperty("提交结束时间")
    private String submitEndTime;
    @ApiModelProperty("解决开始时间")
    private String solveStartTime;
    @ApiModelProperty("解决结束时间")
    private String solveEndTime;
    @ApiModelProperty("关闭开始时间")
    private String closeStartTime;
    @ApiModelProperty("关闭结束时间")
    private String closeEndTime;

    @ApiModelProperty(value = "导出对象")
    private ExportExcelParams excelParams;

}
