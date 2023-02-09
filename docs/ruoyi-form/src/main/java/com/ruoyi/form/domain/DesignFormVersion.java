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

/**
    * 表单版本
    */
@ApiModel(value="表单版本")
@Data
@Builder
@TableName(value = "design_form_version")
public class DesignFormVersion {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Long id;

    /**
     * 表单ID
     */
    @TableField(value = "form_id")
    @ApiModelProperty(value="表单ID")
    private Long formId;

    /**
     * 版本号
     */
    @TableField(value = "version")
    @ApiModelProperty(value="版本号")
    private Integer version;

    /**
     * 提交记录
     */
    @TableField(value = "`data`")
    @ApiModelProperty(value="提交记录")
    private String data;

    /**
     * 是否为当前版本
     */
    @TableField(value = "is_current")
    @ApiModelProperty(value="是否为当前版本")
    private Integer isCurrent;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    @ApiModelProperty(value="名称")
    private String name;

    /**
     * 类型
     */
    @TableField(value = "`type`")
    @ApiModelProperty(value="类型")
    private Integer type;

    /**
     * 表名
     */
    @TableField(value = "code")
    @ApiModelProperty(value="表名")
    private String code;

    /**
     * 字符集
     */
    @TableField(value = "charset")
    @ApiModelProperty(value="字符集")
    private String charset;

    /**
     * HTML源码
     */
    @TableField(value = "html")
    @ApiModelProperty(value="HTML源码")
    private String html;

    /**
     * JSON源码
     */
    @TableField(value = "json")
    @ApiModelProperty(value="JSON源码")
    private String json;

    /**
     * 状态
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    @ApiModelProperty(value="创建时间")
    private Date createdTime;

    /**
     * 创建者
     */
    @TableField(value = "created_by")
    @ApiModelProperty(value="创建者")
    private String createdBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    @ApiModelProperty(value="更新时间")
    private Date updatedTime;

    /**
     * 更新者
     */
    @TableField(value = "updated_by")
    @ApiModelProperty(value="更新者")
    private String updatedBy;

    public static final String COL_ID = "id";

    public static final String COL_FORM_ID = "form_id";

    public static final String COL_VERSION = "version";

    public static final String COL_DATA = "data";

    public static final String COL_IS_CURRENT = "is_current";

    public static final String COL_NAME = "name";

    public static final String COL_TYPE = "type";

    public static final String COL_CODE = "code";

    public static final String COL_CHARSET = "charset";

    public static final String COL_HTML = "html";

    public static final String COL_JSON = "json";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_CREATED_BY = "created_by";

    public static final String COL_UPDATED_TIME = "updated_time";

    public static final String COL_UPDATED_BY = "updated_by";
}