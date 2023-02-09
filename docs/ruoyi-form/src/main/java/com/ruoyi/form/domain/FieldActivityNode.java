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

@ApiModel(value="FieldActivityNode",description = "表单字段流程关联对象")
@Data
@TableName(value = "field_activity_node")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldActivityNode {
    /**
     * 表单字段关联流程id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="表单字段关联流程id")
    private Long id;

    /**
     * 表字段
     */
    @TableField(value = "field_name")
    @ApiModelProperty(value="表字段")
    private String fieldName;

    /**
     * 表字段描述
     */
    @TableField(value = "field_desc")
    @ApiModelProperty(value="表字段描述")
    private String fieldDesc;

    /**
     * 字段状态：0-只读，1可编辑
     */
    @TableField(value = "field_status")
    @ApiModelProperty(value="字段状态：0-只读，1-可编辑，2-隐藏，3-必填")
    private String fieldStatus;

    /**
     * 流程节点id-关联xml中的sid
     */
    @TableField(value = "activity_node_id")
    @ApiModelProperty(value="流程节点id-关联xml中的sid")
    private String activityNodeId;

    /**
     * 表单版本ID
     */
    @TableField(value = "form_version_id")
    @ApiModelProperty(value="表单版本ID")
    private Long formVersionId;

    public static final String COL_ID = "id";

    public static final String COL_FIELD_NAME = "field_name";

    public static final String COL_FIELD_DESC = "field_desc";

    public static final String COL_FIELD_STATUS = "field_status";

    public static final String COL_ACTIVITY_NODE_ID = "activity_node_id";

    public static final String COL_FORM_VERSION_ID = "form_version_id";
}