package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value = "operation_details")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "operation_details")
public class OperationDetails {
    /**
     * 操作记录主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "操作记录主键ID",hidden = true)
    private Long id;

    /**
     * 操作类型 --业务单类型
     */
    @TableField(value = "operation_type")
    @ApiModelProperty(value = "操作类型 --业务单类型",hidden = true)
    private String operationType;

    /**
     * 业务编号
     */
    @TableField(value = "biz_no")
    @ApiModelProperty(value = "业务编号",required = true)
    private String bizNo;

    /**
     * 旧值
     */
    @TableField(value = "old_value")
    @ApiModelProperty(value = "旧值")
    private String oldValue;

    /**
     * 新值
     */
    @TableField(value = "new_value")
    @ApiModelProperty(value = "新值")
    private String newValue;

    /**
     * 操作详情记录
     */
    @TableField(value = "description")
    @ApiModelProperty(value = "操作详情记录")
    private String description;

    /**
     * 创建人ID
     */
    @TableField(value = "created_by",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人ID",hidden = true)
    private String createdBy;

    /**
     * 创建人名称
     */
    @TableField(value = "created_name",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人名称",hidden = true)
    private String createdName;

    /**
     * 创建时间
     */
    @TableField(value = "created_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间",hidden = true)
    private Date createdTime;

    /**
     * 级别
     */
    @TableField(value = "highWarn",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "级别",hidden = true)
    private  String highWarn;


    public static final String COL_ID = "id";

    public static final String COL_OPERATION_TYPE = "operation_type";

    public static final String COL_BIZ_NO = "biz_no";

    public static final String COL_OLD_VALUE = "old_value";

    public static final String COL_NEW_VALUE = "new_value";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_CREATED_BY = "created_by";

    public static final String COL_CREATED_NAME = "created_name";

    public static final String COL_CREATED_TIME = "created_time";
    public static final String HIGH_WARN ="high_warn";
}