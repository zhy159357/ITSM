package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value = "button_config_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "button_config_info")
public class ButtonConfigInfo {
    public static final String COL_DYNAMICA_APPROVE_PERSONAL = "dynamica_approve_personal";
    /**
     * 按钮配置表主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "按钮配置表主键ID")
    private Integer id;

    /**
     * 按钮名称
     */
    @TableField(value = "button_name")
    @ApiModelProperty(value = "按钮名称")
    private String buttonName;

    /**
     * 按钮类型【1：审批类  2：动作类  3：跳转类】
     */
    @TableField(value = "button_type")
    @ApiModelProperty(value = "按钮类型【1：审批类  2：动作类  3：跳转类】")
    private Integer buttonType;

    /**
     * 是否需要审批弹框【0：不需要  1：需要】
     */
    @TableField(value = "approve_enable")
    @ApiModelProperty(value = "是否需要审批弹框【0：不需要  1：需要】")
    private Integer approveEnable;

    /**
     * 审批弹框模板
     */
    @TableField(value = "approve_template")
    @ApiModelProperty(value = "审批弹框模板")
    private Integer approveTemplate;

    /**
     * 按钮动作路径
     */
    @TableField(value = "button_action_url")
    @ApiModelProperty(value = "按钮动作路径")
    private String buttonActionUrl;

    /**
     * 所属业务流程
     */
    @TableField(value = "biz_process_code")
    @ApiModelProperty(value = "所属业务流程")
    private String bizProcessCode;

    /**
     * 所属业务流程中的节点ID（activity-id）
     */
    @TableField(value = "biz_process_activity_node_id")
    @ApiModelProperty(value = "所属业务流程中的节点ID（activity-id）")
    private String bizProcessActivityNodeId;

    /**
     * 是否存在流条件【0：不存在  1：存在】
     */
    @TableField(value = "condition_enable")
    @ApiModelProperty(value = "是否存在流条件【0：不存在  1：存在】")
    private Integer conditionEnable;

    /**
     * 流表达式
     */
    @TableField(value = "condition_expression")
    @ApiModelProperty(value = "流表达式")
    private String conditionExpression;

    /**
     * 是否动态选择审批人【0：否  1：是】
     */
    @TableField(value = "dynamic_approve_personal")
    @ApiModelProperty(value = "是否动态选择审批人【0：否  1：是】")
    private Integer dynamicApprovePersonal;

    /**
     * 动态审批人表达式
     */
    @TableField(value = "approve_personal_expression")
    @ApiModelProperty(value = "动态审批人表达式")
    private String approvePersonalExpression;

    /**
     * 动态审批人表达式
     */
    @TableField(value = "process_complete_service_impl")
    @ApiModelProperty(value = "流程审批实现类的Bean")
    private String processCompleteServiceImpl;

    /**
     * 创建人
     */
    @TableField(value = "created_by",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人",hidden = true)
    private String createdBy;

    /**
     * 修改人
     */
    @TableField(value = "updated_by",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改人",hidden = true)
    private String updatedBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间",hidden = true)
    private Date createdTime;

    /**
     * 修改时间
     */
    @TableField(value = "updated_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间",hidden = true)
    private Date updatedTime;

    public static final String COL_ID = "id";

    public static final String COL_BUTTON_NAME = "button_name";

    public static final String COL_BUTTON_TYPE = "button_type";

    public static final String COL_APPROVE_ENABLE = "approve_enable";

    public static final String COL_APPROVE_TEMPLATE = "approve_template";

    public static final String COL_BUTTON_ACTION_URL = "button_action_url";

    public static final String COL_BIZ_PROCESS_CODE = "biz_process_code";

    public static final String COL_BIZ_PROCESS_ACTIVITY_NODE_ID = "biz_process_activity_node_id";

    public static final String COL_CONDITION_ENABLE = "condition_enable";

    public static final String COL_CONDITION_EXPRESSION = "condition_expression";

    public static final String COL_DYNAMIC_APPROVE_PERSONAL = "dynamic_approve_personal";

    public static final String COL_APPROVE_PERSONAL_EXPRESSION = "approve_personal_expression";

    public static final String PROCESS_COMPLETE_SERVICE_IMPL = "process_complete_service_impl";

    public static final String COL_CREATED_BY = "created_by";

    public static final String COL_UPDATED_BY = "updated_by";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_UPDATED_TIME = "updated_time";
}