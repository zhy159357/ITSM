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

@ApiModel(value = "FormStatusActivityNode")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "form_status_activity_node")
public class FormStatusActivityNode {
    /**
     * 表单状态id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "表单状态id")
    private Long id;

    /**
     * 状态名称
     */
    @TableField(value = "biz_status_name")
    @ApiModelProperty(value = "状态名称",required = true)
    private String bizStatusName;

    /**
     * 流程节点ID
     */
    @TableField(value = "activity_node_id")
    @ApiModelProperty(value = "流程节点ID",required = true)
    private String activityNodeId;

    /**
     * 表单版本id
     */
    @TableField(value = "form_version_id")
    @ApiModelProperty(value = "表单版本id",hidden = true)
    private Long formVersionId;

    /**
     * 排序
     */
    @TableField(value = "sort")
    @ApiModelProperty(value = "排序",required = true)
    private Integer sort;

    public static final String COL_ID = "id";

    public static final String COL_BIZ_STATUS_NAME = "biz_status_name";

    public static final String COL_ACTIVITY_NODE_ID = "activity_node_id";

    public static final String COL_FORM_VERSION_ID = "form_version_id";

    public static final String COL_SORT = "sort";
}