package com.ruoyi.form.vo;

import com.ruoyi.form.domain.DesignBizProblemTaskNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ProblemTaskRetrievalVo
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/11/10 8:53
 */
@ApiModel("问题任务搜索条件")
@Data
public class ProblemTaskRetrievalVo {
    @ApiModelProperty(value = "创建开始时间")
    private String createdStartTime;
    @ApiModelProperty(value = "创建结束时间")
    private String createdEndTime;
    @ApiModelProperty(value = "问题任务更新开始时间")
    private String updateStartTime;
    @ApiModelProperty(value = "问题任务更新结束时间")
    private String updateEndTime;
    @ApiModelProperty(value = "问题任务提交开始时间")
    private String submitStartTime;
    @ApiModelProperty(value = "问题任务提交结束时间")
    private String submitEndTime;
    @ApiModelProperty(value = "问题任务计划完成开始时间")
    private String planCompleteStartTime;
    @ApiModelProperty(value = "问题任务计划完成结束时间")
    private String planCompleteEndTime;

    @ApiModelProperty(value = "问题任务申请关闭开始时间")
    private String applyCloseStartTime;
    @ApiModelProperty(value = "问题任务申请关闭结束时间")
    private String applyCloseEndTime;

    @ApiModelProperty(value = "问题任务关闭开始时间")
    private String closeStartTime;
    @ApiModelProperty(value = "问题任务关闭结束时间")
    private String closeEndTime;

    @ApiModelProperty(value = "问题协查人更新开始时间")
    private String assistantUpdateStartTime;
    @ApiModelProperty(value = "问题协查人更新结束时间")
    private String assistantUpdateEndTime;

    @ApiModelProperty(value = "问题任务是否关闭")
    private String isClosed;

    @ApiModelProperty(value = "问题任务单业务主表")
    private DesignBizProblemTaskNew designBizProblemTaskNew;

    @ApiModelProperty(value = "问题任务检索导出")
    private ExportExcelParams excelParams;


}
