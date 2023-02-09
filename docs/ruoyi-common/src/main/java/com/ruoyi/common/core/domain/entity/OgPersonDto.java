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
@TableName(value = "og_person")
public class OgPersonDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "pid", type = IdType.ID_WORKER_STR)
    private String pid;
    @TableField(value = "orgid")
    private String orgid;
    @TableField(value = "pname")
    private String pname;
    @TableField(value = "sex")
    private String sex;
    @TableField(value = "edu")
    private String edu;
    @TableField(value = "invalidation_mark")
    private String invalidationMark;
    @TableField(value = "partake_org_id")
    private String partakeOrgId;
    @TableField(value = "position")
    private String position;
    @TableField(value = "birthday")
    private String birthday;
    @TableField(value = "birthplace")
    private String birthplace;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "mobil_phone")
    private String mobilPhone;

    @TableField(value = "email")
    private String email;
    @TableField(value = "address")
    private String address;
    @TableField(value = "adder")
    private String adder;

    @TableField(value = "update_time")
    private String updateTime;

    @TableField(value = "moder")
    private String moder;
    @TableField(value = "memo")
    private String memo;

    @TableField(exist = false)
    private String addAndUpdate;
    
    @TableField(value = "pid_crc")
    private long pid_crc;
}

