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

@ApiModel(value = "button_action")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "button_action")
public class ButtonAction {
    /**
     * 按钮配置表
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "按钮配置表")
    private Long id;

    /**
     * 描述
     */
    @TableField(value = "`desc`")
    @ApiModelProperty(value = "描述",required = true)
    private String desc;

    /**
     * 类的全限定名
     */
    @TableField(value = "fully_qualified_name")
    @ApiModelProperty(value = "类的全限定名")
    private String fullyQualifiedName;

    /**
     * 类的全限定名
     */
    @TableField(value = "bean_name")
    @ApiModelProperty(value = "方法所在类的Bean名称")
    private String beanName;

    /**
     * 方法名
     */
    @TableField(value = "method_name")
    @ApiModelProperty(value = "方法名")
    private String methodName;

    /**
     * 参数值
     */
    @TableField(value = "param_value")
    @ApiModelProperty(value = "参数值")
    private String paramValue;

    /**
     * 节点ID
     */
    @TableField(value = "activity_node_id")
    @ApiModelProperty(value = "节点ID",required = true)
    private String activityNodeId;

    /**
     * 模板ID 关联approve_template
     */
    @TableField(value = "template_id")
    @ApiModelProperty(value = "模板ID 关联approve_template",required = true)
    private Long templateId;

    public static final String COL_ID = "id";

    public static final String COL_DESC = "desc";

    public static final String COL_FULLY_QUALIFIED_NAME = "fully_qualified_name";

    public static final String COL_BEAN_NAME = "bean_name";

    public static final String COL_METHOD_NAME = "method_name";

    public static final String COL_PARAM_VALUE = "param_value";

    public static final String COL_ACTIVITY_NODE_ID = "activity_node_id";

    public static final String COL_TEMPLATE_ID = "template_id";
}