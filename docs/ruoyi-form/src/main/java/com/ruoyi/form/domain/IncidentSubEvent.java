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

@ApiModel(value = "incident_sub_event")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "incident_sub_event")
public class IncidentSubEvent {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 事件单id，关联主表
     */
    @TableField(value = "event_no")
    @ApiModelProperty(value = "事件单id，关联主表")
    private String eventNo;

    /**
     * 提交时间 格式：yyyy-MM-dd HH:mm:ss
     */
    @TableField(value = "submit_time")
    @ApiModelProperty(value = "提交时间 格式：yyyy-MM-dd HH:mm:ss")
    private String submitTime;

    /**
     * 解决时间 格式：yyyy-MM-dd HH:mm:ss
     */
    @TableField(value = "solve_time")
    @ApiModelProperty(value = "解决时间 格式：yyyy-MM-dd HH:mm:ss")
    private String solveTime;

    /**
     * 解决时长
     */
    @TableField(value = "solve_length")
    @ApiModelProperty(value = "解决时长")
    private Integer solveLength;

    /**
     * 解决人
     */
    @TableField(value = "solve_person")
    @ApiModelProperty(value = "解决人")
    private String solvePerson;

    /**
     * 解决部门
     */
    @TableField(value = "solve_org")
    @ApiModelProperty(value = "解决部门")
    private String solveOrg;

    /**
     * 解决组
     */
    @TableField(value = "solve_group")
    @ApiModelProperty(value = "解决组")
    private String solveGroup;

    /**
     * 关闭时间 格式：yyyy-MM-dd HH:mm:ss
     */
    @TableField(value = "close_time")
    @ApiModelProperty(value = "关闭时间 格式：yyyy-MM-dd HH:mm:ss")
    private String closeTime;

    /**
     * 是否挂起标识 1-是；0-否
     */
    @TableField(value = "suspend_flag")
    @ApiModelProperty(value = "是否挂起标识 1-是；0-否")
    private String suspendFlag;

    /**
     * 挂起时间 格式：yyyy-MM-dd HH:mm:ss
     */
    @TableField(value = "suspend_time")
    @ApiModelProperty(value = "挂起时间 格式：yyyy-MM-dd HH:mm:ss")
    private String suspendTime;

    /**
     * 挂起时长
     */
    @TableField(value = "suspend_length")
    @ApiModelProperty(value = "挂起时长")
    private Integer suspendLength;

    /**
     * 是否催单 1-是；0-否
     */
    @TableField(value = "urge_flag")
    @ApiModelProperty(value = "是否催单 1-是；0-否")
    private String urgeFlag;

    /**
     * 催单时间
     */
    @TableField(value = "urge_time")
    @ApiModelProperty(value = "催单时间")
    private String urgeTime;

    /**
     * 是否客服退回 1-是；0-否
     */
    @TableField(value = "service_back_falg")
    @ApiModelProperty(value = "是否客服退回 1-是；0-否")
    private String serviceBackFalg;

    /**
     * 是否补充信息退回 1-是；0-否
     */
    @TableField(value = "add_msg_back_falg")
    @ApiModelProperty(value = "是否补充信息退回 1-是；0-否")
    private String addMsgBackFalg;

    /**
     * 是否撤单
     */
    @TableField(value = "cancel_falg")
    @ApiModelProperty(value = "是否撤单")
    private String cancelFalg;

    /**
     * 撤单时间 格式：yyyy-MM-dd HH:mm:ss
     */
    @TableField(value = "cancel_time")
    @ApiModelProperty(value = "撤单时间 格式：yyyy-MM-dd HH:mm:ss")
    private String cancelTime;

    /**
     * 退回发起人补全标识 处理环节退回1；解决环节退回2
     */
    @TableField(value = "back_completion_flag")
    @ApiModelProperty(value = "退回发起人补全标识 处理环节退回1；解决环节退回2")
    private String backCompletionFlag;

    public static final String COL_ID = "id";

    public static final String COL_EVENT_NO = "event_no";

    public static final String COL_SUBMIT_TIME = "submit_time";

    public static final String COL_SOLVE_TIME = "solve_time";

    public static final String COL_SOLVE_LENGTH = "solve_length";

    public static final String COL_SOLVE_PERSON = "solve_person";

    public static final String COL_SOLVE_ORG = "solve_org";

    public static final String COL_SOLVE_GROUP = "solve_group";

    public static final String COL_CLOSE_TIME = "close_time";

    public static final String COL_SUSPEND_FLAG = "suspend_flag";

    public static final String COL_SUSPEND_TIME = "suspend_time";

    public static final String COL_SUSPEND_LENGTH = "suspend_length";

    public static final String COL_URGE_FLAG = "urge_flag";

    public static final String COL_URGE_TIME = "urge_time";

    public static final String COL_SERVICE_BACK_FALG = "service_back_falg";

    public static final String COL_ADD_MSG_BACK_FALG = "add_msg_back_falg";

    public static final String COL_CANCEL_FALG = "cancel_falg";

    public static final String COL_CANCEL_TIME = "cancel_time";

    public static final String COL_BACK_COMPLETION_FLAG = "back_completion_flag";
}