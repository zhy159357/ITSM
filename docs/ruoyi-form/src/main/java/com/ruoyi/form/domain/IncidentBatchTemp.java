package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "incident_bacth_temp")
public class IncidentBatchTemp {

    // 主键
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 单号
    @TableField(value = "event_no")
    private String eventNo;

    // 批量主单流程实例id
    @TableField(value = "instance_id")
    private String instanceId;

    // 操作人id
    @TableField(value = "user_id")
    private String userId;

    // 批量操作类型；1-接单；2-解决
    @TableField(value = "type")
    private Integer type;

    // 批量操作json参数
    @TableField(value = "customer_json")
    private String customerJson;

    // 页面处理描述信息
    @TableField(value = "status_str")
    private String statusStr;

    // 执行状态  success-执行成功；error-执行失败；running-执行中
    @TableField(value = "execute_status")
    private String executeStatus;

    public static final Integer INCIDENT_BATCH_OPERATOR_TYPE_DEAL = 1; //接单
    public static final Integer INCIDENT_BATCH_OPERATOR_TYPE_SOLVE = 2; //解决

    // 执行状态
    public static final String INCIDENT_BATCH_SUCCESS = "success"; //成功
    public static final String INCIDENT_BATCH_ERROR = "error"; //执行失败
    public static final String INCIDENT_BATCH_RUNNING = "running"; //执行中
    public static final String INCIDENT_BATCH_UNEXECUTED = "unexecuted"; //未执行
}
