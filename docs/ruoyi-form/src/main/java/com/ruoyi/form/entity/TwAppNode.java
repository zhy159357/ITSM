package com.ruoyi.form.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author chw
 * @since 2022-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TwAppNode extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 工单号
     */
    private String workNum;

    /**
     * 应用系统
     */
    private String appSystem;

    /**
     * ip
     */
    private String ip;

    /**
     * 环境类型
     */
    private String envType;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 端口号
     */
    private String port;

    /**
     * 实例名
     */
    private String instanceName;

    /**
     * 用户信息
     */
    private String userMessage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 节点类型： 0：应用节点，1：数据库节点
     */
    private Boolean type;

    /**
     * 业务单ID
     */
    @TableField(exist = false)
    private String bizId;


}
