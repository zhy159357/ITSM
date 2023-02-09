package com.ruoyi.form.vo;

import com.ruoyi.form.domain.DesignBizChangetask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ChangeTaskRetrievalVo
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/11/10 9:25
 */
@Data
@ApiModel("问题任务单检索")
public class ChangeTaskRetrievalVo {
    @ApiModelProperty(value = "创建开始时间")
    private String createdStartTime;
    @ApiModelProperty(value = "创建结束时间")
    private String createdEndTime;

    @ApiModelProperty(value = "实际开始开始时间")
    private String implStartTime;
    @ApiModelProperty(value = "实际开始结束时间")
    private String implEndTime;

    @ApiModelProperty(value = "实际结束开始时间")
    private String implOverStartTime;
    @ApiModelProperty(value = "实际结束结束时间")
    private String implOverEndTime;

    @ApiModelProperty(value = "计划开始时间早于")
    private String planStartEarlyTime;
    @ApiModelProperty(value = "计划开始时间晚于")
    private String planStartLateTime;

    @ApiModelProperty(value = "计划结束时间早于")
    private String planEndEarlyTime;
    @ApiModelProperty(value = "计划结束时间晚于")
    private String planEndLateTime;

    @ApiModelProperty(value = "关闭时间早于")
    private String closeEarlyTime;
    @ApiModelProperty(value = "关闭时间晚于")
    private String closeLateTime;

    @ApiModelProperty(value = "变更任务单检索")
    private DesignBizChangetask designBizChangetask;

    @ApiModelProperty(value = "变更任务导出对象")
    private ExportExcelParams excelParams;
}
