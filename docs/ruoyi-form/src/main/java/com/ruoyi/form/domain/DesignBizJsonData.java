package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@ApiModel(value="design_biz_json_data")
@Data
@Builder
@TableName(value = "design_biz_json_data")
public class DesignBizJsonData {
    /**
     * 自定义表单内容表主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="自定义表单内容表主键ID")
    private Long id;

    /**
     * 自定义表单内容json
     */
    @TableField(value = "json_data")
    @ApiModelProperty(value="自定义表单内容json")
    private String jsonData;

    /**
     * 自定义表单业务表ID 关联表design_biz_xxx
     */
    @TableField(value = "biz_id")
    @ApiModelProperty(value="自定义表单业务表ID 关联表design_biz_xxx")
    private Long bizId;

    /**
     * 业务表名称  design_biz_xxx
     */
    @TableField(value = "biz_table")
    @ApiModelProperty(value="业务表名称  design_biz_xxx")
    private String bizTable;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    @ApiModelProperty(value="创建时间")
    private Date createdTime;

    /**
     * 创建人
     */
    @TableField(value = "created_by")
    @ApiModelProperty(value="创建人")
    private String createdBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    @ApiModelProperty(value="更新时间")
    private Date updatedTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by")
    @ApiModelProperty(value="更新人")
    private String updatedBy;

    public static final String COL_ID = "id";

    public static final String COL_JSON_DATA = "json_data";

    public static final String COL_BIZ_ID = "biz_id";

    public static final String COL_BIZ_TABLE = "biz_table";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_CREATED_BY = "created_by";

    public static final String COL_UPDATED_TIME = "updated_time";

    public static final String COL_UPDATED_BY = "updated_by";
}