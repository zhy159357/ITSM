package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;


/**
 * 账户对象
 *
 * @author ruoyi
 * @date 2020-12-08
 */
@Data
@TableName(value = "og_user")
public class OgUserDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "userId", type = IdType.ID_WORKER_STR)
    private String userId;
    @TableField(value = "pId")
    private String pId;
    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password;
    @TableField(value = "update_time")
    private String updateTime;
    @TableField(value = "invalidation_mark")
    private String invalidationMark;
    @TableField(value = "ip_start")
    private String ipStart;
    @TableField(value = "ip_end")
    private String ipEnd;
    @TableField(value = "sms_code")
    private String smsCode;//短信验证码
    @TableField(value = "last_time")
    private Long lastTime;//验证码发送时
    @TableField(value = "l_count")
    private String lCount;//短信验证码错误次数
    @TableField(value = "p_not_count")
    private String pNotCount;//密码错误次数
    @TableField(value = "lock_time")
    private String lockTime;//密码锁定时间
    @TableField(value = "weak_pwd")
    private String weakPwd;//密码强弱校验  0-弱密码  1-强密码
    @TableField(value = "cust_no")
    private String custNo;
    @TableField(exist = false)
    private String addAndUpdate;
}

