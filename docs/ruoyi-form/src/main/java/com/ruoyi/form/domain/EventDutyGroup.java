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

@ApiModel(value = "event_duty_group")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "event_duty_group")
public class EventDutyGroup {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 值班组类型
     */
    @TableField(value = "duty_group_type")
    @ApiModelProperty(value = "值班组类型 1-部门机构；2-工作组；3-角色")
    private String dutyGroupType;

    /**
     * 值班组名称
     */
    @TableField(value = "duty_group_name")
    @ApiModelProperty(value = "值班组名称")
    private String dutyGroupName;

    /**
     * 值班人id
     */
    @TableField(value = "duty_person_id")
    @ApiModelProperty(value = "值班人id")
    private String dutyPersonId;

    /**
     * 值班人名称
     */
    @TableField(value = "duty_person_name")
    @ApiModelProperty(value = "值班人名称")
    private String dutyPersonName;

    /**
     * 值班人电话
     */
    @TableField(value = "duty_person_phone")
    @ApiModelProperty(value = "值班人电话")
    private String dutyPersonPhone;

    /**
     * 值班人邮箱
     */
    @TableField(value = "duty_person_email")
    @ApiModelProperty(value = "值班人邮箱")
    private String dutyPersonEmail;

    /**
     * 值班开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "值班开始时间")
    private String startTime;

    /**
     * 值班结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "值班结束时间")
    private String endTime;

    public static final String COL_ID = "id";

    public static final String DUTY_GROUP_TYPE = "duty_group_type";

    public static final String DUTY_GROUP_NAME = "duty_group_name";

    public static final String DUTY_PERSON_ID = "duty_person_id";

    public static final String DUTY_PERSON_NAME = "duty_person_name";

    public static final String DUTY_PERSON_PHONE = "duty_person_phone";

    public static final String DUTY_PERSON_EMAIL = "duty_person_email";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";
}