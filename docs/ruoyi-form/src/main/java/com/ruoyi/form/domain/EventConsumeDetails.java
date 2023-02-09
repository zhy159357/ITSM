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

@ApiModel(value = "event_consume_details")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "event_consume_details")
public class EventConsumeDetails {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 事件单号
     */
    @TableField(value = "biz_no")
    @ApiModelProperty(value = "事件单号")
    private String bizNo;

    /**
     * 部门
     */
    @TableField(value = "deal_org")
    @ApiModelProperty(value = "部门")
    private String dealOrg;

    /**
     * 部门名称
     */
    @TableField(value = "deal_org_name")
    @ApiModelProperty(value = "部门名称")
    private String dealOrgName;

    /**
     * 处理人
     */
    @TableField(value = "deal_person")
    @ApiModelProperty(value = "处理人")
    private String dealPerson;

    /**
     * 处理人名称
     */
    @TableField(value = "deal_person_name")
    @ApiModelProperty(value = "处理人名称")
    private String dealPersonName;

    /**
     * 当前节点名称
     */
    @TableField(value = "current_node_name")
    @ApiModelProperty(value = "当前节点名称")
    private String currentNodeName;

    /**
     * 下一节点名称
     */
    @TableField(value = "next_node_name")
    @ApiModelProperty(value = "下一节点名称")
    private String nextNodeName;

    /**
     * 开始处理时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "开始处理时间")
    private String startTime;

    /**
     * 结束处理时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "结束处理时间")
    private String endTime;

    /**
     * 事件耗时类型
     */
    @TableField(value = "consume_type")
    @ApiModelProperty(value = "事件耗时类型")
    private String consumeType;

    /**
     * 事件耗时天
     */
    @TableField(exist = false)
    private long days;

    /**
     * 事件耗时时
     */
    @TableField(exist = false)
    private long hours;

    /**
     * 事件耗时分
     */
    @TableField(exist = false)
    private long minutes;

    /**
     * 事件耗时秒
     */
    @TableField(exist = false)
    private long seconds;


    public static final String COL_ID = "id";

    public static final String BIZ_NO = "biz_no";

    public static final String DEAL_ORG = "deal_org";

    public static final String DEAL_ORG_NAME = "deal_org_name";

    public static final String DEAL_PERSON = "deal_person";

    public static final String DEAL_PERSON_NAME = "deal_person_name";

    public static final String CURRENT_NODE_NAME = "current_node_name";

    public static final String NEXT_NODE_NAME = "next_node_name";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";

    public static final String CONSUME_TYPE = "consume_type";

    // 耗时类型  0-开始（创建） 1-接单（响应时间）  2-处理（处理时间）  3-解决（解决-关闭时间）
    public static final String CONSUME_TYPE_0 = "0";
    public static final String CONSUME_TYPE_1 = "1";
    public static final String CONSUME_TYPE_2 = "2";
    public static final String CONSUME_TYPE_3 = "3";
}