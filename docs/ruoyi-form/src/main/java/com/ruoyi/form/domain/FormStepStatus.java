package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "FormStepStatus")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "form_step_status")
public class FormStepStatus {
    /**
     * 步骤和工单状态关联表
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "步骤和工单状态关联表")
    private Long id;

    /**
     * 步骤名称
     */
    @TableField(value = "step_name")
    @ApiModelProperty(value = "步骤名称",required = true)
    private String stepName;

    /**
     * 业务状态名称
     */
    @TableField(value = "biz_status_name")
    @ApiModelProperty(value = "业务状态名称",required = true)
    private String bizStatusName;

    /**
     * 表单id
     */
    @TableField(value = "form_version_id")
    @ApiModelProperty(value = "表单id",hidden = true)
    private Long formVersionId;

    /**
     * 排序
     */
    @TableField(value = "sort")
    @ApiModelProperty(value = "排序",required = true)
    private Integer sort;

    public static final String COL_ID = "id";

    public static final String COL_STEP_NAME = "step_name";

    public static final String COL_BIZ_STATUS_NAME = "biz_status_name";

    public static final String COL_FORM_VERSION_ID = "form_version_id";

    public static final String COL_SORT = "sort";
}