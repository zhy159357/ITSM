package com.ruoyi.form.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @program: ruoyi
 * @description: 环境单历史数据
 * @author: ma zy
 * @date: 2022-12-05 10:50
 **/
@Data
@TableName(value = "tw_history")
@EqualsAndHashCode(callSuper = false)
public class TwHistoryEntity implements Serializable {
    private String id;
    @Excel(name = "系统名称")
    private String sysName;
    @Excel(name = "部门负责人")
    private String deptLeader;
    private String personLeader;
    @Excel(name = "数据库类型")
    private String dbType;
    @Excel(name = "端口")
    private String port;
    @Excel(name = "实例名称")
    private String spaceName;//实例名称
    private String updateTime;
    private String rank;//级别
    @Excel(name = "主机Ip")
    private String hostIp;//主机ip
    private String twType;//环境类型
    @Excel(name = "Paso名称")
    private String pasoName;//Paso名称
    private String status;
    private String nodeType;
}
