package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "itsm_email_message")
public class EmailMessage {
    // 主键
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 业务单号
    @TableField(value = "biz_no")
    private String bizNo;

    // 业务类型；1-事件单；2-问题单；3-变更单
    @TableField(value = "biz_type")
    private String bizType;

    // 邮箱
    @TableField(value = "email")
    private String email;

    // 邮件标题
    @TableField(value = "title")
    private String title;

    // 邮件信息
    @TableField(value = "email_message")
    private String emailMessage;

    // 发送状态；0-未发送；1-已发送
    @TableField(value = "send_status")
    private String sendStatus;

    // 发送时间
    @TableField(value = "send_time")
    private Date sendTime;

    // 邮件业务类型
    public static final String EMAIL_MESSAGE_BIZ_TYPE_EVENT = "1"; //事件
    public static final String EMAIL_MESSAGE_BIZ_TYPE_PROBLEM = "2";// 问题
    public static final String EMAIL_MESSAGE_BIZ_TYPE_PROBLEM_TASK = "3"; // 问题任务
    public static final String EMAIL_MESSAGE_BIZ_TYPE_CHANGE = "4"; // 变更
    public static final String EMAIL_MESSAGE_BIZ_TYPE_CHANGE_TASK = "5"; // 变更任务

    // 邮件状态
    public static final String EMAIL_MESSAGE_STATUS_NO = "0"; // 未发送
    public static final String EMAIL_MESSAGE_STATUS_YES = "1"; // 已发送
}
