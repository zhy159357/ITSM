package com.ruoyi.form.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author chw
 * @since 2022-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TwWorkNode implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;

    /**
     * 工单id
     */
    private String  workOrderId;

    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 分类
     */
    private String classify;

    /**
     * 主机名
     */
    private String hostName;

    /**
     * 设备型号
     */
    private String equipmentType;

    /**
     * 主机ip
     */
    private String ip;

    /**
     * 目前生产生产环境cpu
     */
    private String productionCpu;

    /**
     * 目前生产环境内存
     */
    private String productionMemory;

    /**
     * 申请cpu大小
     */
    private String applyCpu;

    /**
     * 申请内存大小
     */
    private String applyMemory;

    /**
     * 操作系统及版本
     */
    private String osVersion;

    /**
     * 数据库及版本
     */
    private String dbVersion;

    /**
     * 中间件及版本
     */
    private String middlewareVersion;

    /**
     * 特殊参数需求
     */
    private String specialParameters;

    /**
     * 特殊软件需求
     */
    private String specialApp;
    /**
     * 分卷名称
     */
    private String osVolumeName;
    /**
     * 分卷大小
     */
    private String osVolumeSize;
    /**
     * 磁盘大小
     */
    private String diskSize;
    /**
     * 设备id 多个逗号隔开
     */
    private String equipmentIds;
    /**
     * 台数
     */
    private Integer number;

    /**
     * 子级
     */
    @TableField(exist = false)
    private List<TwUserDb> children;

    /**
     * 统计所有台数
     */
    @TableField(exist = false)
    private Integer allNumber;

    @TableField(exist = false)
    private String dbType;

}
