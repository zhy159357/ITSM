package com.ruoyi.form.vo;

import com.ruoyi.form.domain.DesignBizProblem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ProblemRetrievalVo
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/11/8 10:40
 */
@Data
@ApiModel("问题单检索条件")
public class ProblemRetrievalVo {
    @ApiModelProperty(value = "问题提交开始时间")
    private String submitStartTime;

    @ApiModelProperty(value = "问题提交结束时间")
    private String submitEndTime;

    @ApiModelProperty(value = "计划完成开始时间")
    private String planCompleteStartTime;

    @ApiModelProperty(value = "计划完成结束时间")
    private String planCompleteEndTime;

    @ApiModelProperty(value = "问题创建开始时间")
    private String createStartTime;

    @ApiModelProperty(value = "问题创建结束时间")
    private String createEndTime;

    @ApiModelProperty(value = "问题更新开始时间")
    private String updateStartTime;

    @ApiModelProperty(value = "问题更新结束时间")
    private String updateEndTime;

    @ApiModelProperty(value = "问题提交解决方案开始时间")
    private String submitSolutionStartTime;

    @ApiModelProperty(value = "问题提交解决方案结束时间")
    private String submitSolutionEndTime;

    @ApiModelProperty(value = "问题根因明确开始时间")
    private String solverClearStartTime;

    @ApiModelProperty(value = "问题根因明确结束时间")
    private String solverClearEndTime;

    @ApiModelProperty(value = "问题解决开始时间")
    private String solveStartTime;

    @ApiModelProperty(value = "问题解决结束时间")
    private String solveEndTime;
    @ApiModelProperty(value = "问题牵头人上次更新开始时间")
    private String solverLastStartUpdated;

    @ApiModelProperty(value = "问题牵头人上次更新结束时间")
    private String solverLastEndUpdated;

    @ApiModelProperty(value = "问题关闭开始时间")
    private String closeStartTime;

    @ApiModelProperty(value = "问题关闭结束时间")
    private String closeEndTime;

    @ApiModelProperty(value = "问题观察期限始时间")
    private String observeStartTime;

    @ApiModelProperty(value = "问题观察期限结束时间")
    private String observeEndTime;

    @ApiModelProperty(value = "问题是否关闭")
    private String isClosed;

    @ApiModelProperty(value = "问题单业务主表")
    private DesignBizProblem designBizProblem;

    @ApiModelProperty(value = "导出对象")
    private ExportExcelParams excelParams;
}
