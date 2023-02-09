package com.ruoyi.form.domain;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 事件单
    */
@ApiModel(value="事件单")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "design_biz_incident")
public class DesignBizIncident {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 实例ID
     */
    @TableField(value = "instance_id")
    @ApiModelProperty(value="实例ID")
    private String instanceId;

    /**
     * 备用字段1
     */
    @TableField(value = "extra1")
    @ApiModelProperty(value="备用字段1")
    private String extra1;

    /**
     * 备用字段2
     */
    @TableField(value = "extra2")
    @ApiModelProperty(value="备用字段2")
    private String extra2;

    /**
     * 备用字段3
     */
    @TableField(value = "extra3")
    @ApiModelProperty(value="备用字段3")
    private String extra3;

    /**
     * 备用字段4
     */
    @TableField(value = "extra4")
    @ApiModelProperty(value="备用字段4")
    private String extra4;

    /**
     * 备用字段5
     */
    @TableField(value = "extra5", updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value="备用字段5")
    private String extra5;

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

    /**
     * 上报人部门
     */
    @TableField(value = "report_org")
    @ApiModelProperty(value="上报人部门")
    private String reportOrg;

    /**
     * 上报人
     */
    @TableField(value = "report_person")
    @ApiModelProperty(value="上报人")
    private String reportPerson;

    /**
     * 上报人电话
     */
    @TableField(value = "report_phone")
    @ApiModelProperty(value="上报人电话")
    private String reportPhone;

    /**
     * 中心侧/网点侧事件
     */
    @TableField(value = "side_flag")
    @ApiModelProperty(value="中心侧/网点侧事件")
    private String sideFlag;

    /**
     * 财务事件
     */
    @TableField(value = "finance_flag")
    @ApiModelProperty(value="财务事件")
    private Integer financeFlag;

    /**
     * 是否加急
     */
    @TableField(value = "urgent_flag")
    @ApiModelProperty(value="是否加急")
    private Integer urgentFlag;

    /**
     * 总分行事件标记
     */
    @TableField(value = "org_flag")
    @ApiModelProperty(value="总分行事件标记")
    private String orgFlag;

    /**
     * 事件标题
     */
    @TableField(value = "event_title")
    @ApiModelProperty(value="事件标题")
    private String eventTitle;

    /**
     * 事件详述
     */
    @TableField(value = "event_info")
    @ApiModelProperty(value="事件详述")
    private String eventInfo;

    /**
     * 应用系统名称
     */
    @TableField(value = "system_name")
    @ApiModelProperty(value="应用系统名称")
    private String systemName;

    /**
     * 是否涉及投诉
     */
    @TableField(value = "complain_flag")
    @ApiModelProperty(value="是否涉及投诉")
    private Integer complainFlag;

    /**
     * 来源
     */
    @TableField(value = "event_source")
    @ApiModelProperty(value="来源")
    private String eventSource;

    /**
     * 状态
     */
    @TableField(value = "event_status")
    @ApiModelProperty(value="状态")
    private String eventStatus;

    /**
     * 状态理由
     */
    @TableField(value = "event_status_reason")
    @ApiModelProperty(value="状态理由")
    private String eventStatusReason;

    /**
     * 一级
     */
    @TableField(value = "init_first_level")
    @ApiModelProperty(value="一级")
    private String initFirstLevel;

    /**
     * 二级
     */
    @TableField(value = "init_second_level")
    @ApiModelProperty(value="二级")
    private String initSecondLevel;

    /**
     * 三级
     */
    @TableField(value = "init_three_level")
    @ApiModelProperty(value="三级")
    private String initThreeLevel;

    /**
     * 影响程度
     */
    @TableField(value = "inf_level")
    @ApiModelProperty(value="影响程度")
    private String infLevel;

    /**
     * 优先级
     */
    @TableField(value = "event_priority")
    @ApiModelProperty(value="优先级")
    private String eventPriority;

    /**
     * 目标解决日期
     */
    @TableField(value = "target_resolve_date")
    @ApiModelProperty(value="目标解决日期")
    private String targetResolveDate;

    /**
     * 受派组
     */
    @TableField(value = "assigned_group")
    @ApiModelProperty(value="受派组")
    private String assignedGroup;

    /**
     * 受派人
     */
    @TableField(value = "assigned_person")
    @ApiModelProperty(value="受派人")
    private String assignedPerson;

    /**
     * 二线解决方案
     */
    @TableField(value = "second_solve_plan")
    @ApiModelProperty(value="二线解决方案")
    private String secondSolvePlan;

    /**
     * 解决方案
     */
    @TableField(value = "solve_plan")
    @ApiModelProperty(value="解决方案")
    private String solvePlan;

    /**
     * 事件原因分类
     */
    @TableField(value = "event_reason_category")
    @ApiModelProperty(value="事件原因分类")
    private String eventReasonCategory;

    /**
     * 事件源系统
     */
    @TableField(value = "event_source_system")
    @ApiModelProperty(value="事件源系统")
    private String eventSourceSystem;

    /**
     * 关联系统
     */
    @TableField(value = "relational_system")
    @ApiModelProperty(value="关联系统")
    private String relationalSystem;

    /**
     * 类别
     */
    @TableField(value = "event_category")
    @ApiModelProperty(value="类别")
    private String eventCategory;

    /**
     * 子类
     */
    @TableField(value = "event_subclass")
    @ApiModelProperty(value="子类")
    private String eventSubclass;

    /**
     * 条目
     */
    @TableField(value = "event_entry")
    @ApiModelProperty(value="条目")
    private String eventEntry;

    /**
     * 子条目一
     */
    @TableField(value = "event_subentry1")
    @ApiModelProperty(value="子条目一")
    private String eventSubentry1;

    /**
     * 子条目二
     */
    @TableField(value = "event_subentry2")
    @ApiModelProperty(value="子条目二")
    private String eventSubentry2;

    /**
     * 一级
     */
    @TableField(value = "final_first_level")
    @ApiModelProperty(value="一级")
    private String finalFirstLevel;

    /**
     * 二级
     */
    @TableField(value = "final_second_level")
    @ApiModelProperty(value="二级")
    private String finalSecondLevel;

    /**
     * 三级
     */
    @TableField(value = "final_three_level")
    @ApiModelProperty(value="三级")
    private String finalThreeLevel;

    /**
     * 是否变更引起
     */
    @TableField(value = "change_flag")
    @ApiModelProperty(value="是否变更引起")
    private Integer changeFlag;

    /**
     * 涉及的变更单号
     */
    @TableField(value = "change_no")
    @ApiModelProperty(value="涉及的变更单号")
    private String changeNo;

    /**
     * 影响面
     */
    @TableField(value = "inf_face")
    @ApiModelProperty(value="影响面")
    private String infFace;

    /**
     * 是否影响可用性
     */
    @TableField(value = "inf_use")
    @ApiModelProperty(value="是否影响可用性")
    private Integer infUse;

    /**
     * 受影响的系统
     */
    @TableField(value = "inf_system")
    @ApiModelProperty(value="受影响的系统")
    private String infSystem;

    /**
     * 影响时间范围
     */
    @TableField(value = "inf_time_range")
    @ApiModelProperty(value="影响时间范围")
    private String infTimeRange;

    /**
     * 二线处理部门
     */
    @TableField(value = "second_deal_org")
    @ApiModelProperty(value="二线处理部门")
    private String secondDealOrg;

    /**
     * 二线处理人员
     */
    @TableField(value = "second_deal_person")
    @ApiModelProperty(value="二线处理人员")
    private String secondDealPerson;

    /**
     * 二线解决方案有效
     */
    @TableField(value = "solution_valid_flag")
    @ApiModelProperty(value="二线解决方案有效")
    private Integer solutionValidFlag;

    /**
     * 关闭代码
     */
    @TableField(value = "close_code")
    @ApiModelProperty(value="关闭代码")
    private String closeCode;

    /**
     * 事件级别
     */
    @TableField(value = "event_level")
    @ApiModelProperty(value="事件级别")
    private String eventLevel;

    /**
     * 附件
     */
    @TableField(value = "files")
    @ApiModelProperty(value="附件")
    private String files;

    @TableField(exist = false)
    @ApiModelProperty(value="eventState")
    private List eventState;
    
    public static final String COL_ID = "id";

    public static final String COL_INSTANCE_ID = "instance_id";

    public static final String COL_EXTRA1 = "extra1";

    public static final String COL_EXTRA2 = "extra2";

    public static final String COL_EXTRA3 = "extra3";

    public static final String COL_EXTRA4 = "extra4";

    public static final String COL_EXTRA5 = "extra5";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_CREATED_BY = "created_by";

    public static final String COL_UPDATED_TIME = "updated_time";

    public static final String COL_UPDATED_BY = "updated_by";

    public static final String COL_REPORT_ORG = "report_org";

    public static final String COL_REPORT_PERSON = "report_person";

    public static final String COL_REPORT_PHONE = "report_phone";

    public static final String COL_SIDE_FLAG = "side_flag";

    public static final String COL_FINANCE_FLAG = "finance_flag";

    public static final String COL_URGENT_FLAG = "urgent_flag";

    public static final String COL_ORG_FLAG = "org_flag";

    public static final String COL_EVENT_TITLE = "event_title";

    public static final String COL_EVENT_INFO = "event_info";

    public static final String COL_SYSTEM_NAME = "system_name";

    public static final String COL_COMPLAIN_FLAG = "complain_flag";

    public static final String COL_EVENT_SOURCE = "event_source";

    public static final String COL_EVENT_STATUS = "event_status";

    public static final String COL_EVENT_STATUS_REASON = "event_status_reason";

    public static final String COL_INIT_FIRST_LEVEL = "init_first_level";

    public static final String COL_INIT_SECOND_LEVEL = "init_second_level";

    public static final String COL_INIT_THREE_LEVEL = "init_three_level";

    public static final String COL_INF_LEVEL = "inf_level";

    public static final String COL_EVENT_PRIORITY = "event_priority";

    public static final String COL_TARGET_RESOLVE_DATE = "target_resolve_date";

    public static final String COL_ASSIGNED_GROUP = "assigned_group";

    public static final String COL_ASSIGNED_PERSON = "assigned_person";

    public static final String COL_SECOND_SOLVE_PLAN = "second_solve_plan";

    public static final String COL_SOLVE_PLAN = "solve_plan";

    public static final String COL_EVENT_REASON_CATEGORY = "event_reason_category";

    public static final String COL_EVENT_SOURCE_SYSTEM = "event_source_system";

    public static final String COL_RELATIONAL_SYSTEM = "relational_system";

    public static final String COL_EVENT_CATEGORY = "event_category";

    public static final String COL_EVENT_SUBCLASS = "event_subclass";

    public static final String COL_EVENT_ENTRY = "event_entry";

    public static final String COL_EVENT_SUBENTRY1 = "event_subentry1";

    public static final String COL_EVENT_SUBENTRY2 = "event_subentry2";

    public static final String COL_FINAL_FIRST_LEVEL = "final_first_level";

    public static final String COL_FINAL_SECOND_LEVEL = "final_second_level";

    public static final String COL_FINAL_THREE_LEVEL = "final_three_level";

    public static final String COL_CHANGE_FLAG = "change_flag";

    public static final String COL_CHANGE_NO = "change_no";

    public static final String COL_INF_FACE = "inf_face";

    public static final String COL_INF_USE = "inf_use";

    public static final String COL_INF_SYSTEM = "inf_system";

    public static final String COL_INF_TIME_RANGE = "inf_time_range";

    public static final String COL_SECOND_DEAL_ORG = "second_deal_org";

    public static final String COL_SECOND_DEAL_PERSON = "second_deal_person";

    public static final String COL_SOLUTION_VALID_FLAG = "solution_valid_flag";

    public static final String COL_CLOSE_CODE = "close_code";

    public static final String COL_EVENT_LEVEL = "event_level";

    public static final String COL_FILES = "files";
}