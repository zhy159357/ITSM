package com.ruoyi.form.domain;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 屏蔽告警对象 design_biz_shield_warn
 * 
 * @author ruoyi
 * @date 2022-11-22
 */
@Data
public class DesignBizShieldWarn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 所属任务单号 */
    @Excel(name = "所属任务单号")
    private String changeTaskNo;

    /** 类型 */
    @Excel(name = "类型")
    private String shieldType;

    /** 应用PASO */
    @Excel(name = "应用PASO")
    private String pasoCode;

    private String[] passCodeArray;

    /** 网络基础设备 */
    @Excel(name = "网络基础设备")
    private String net;
    private String[] netArray;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddress;
    private String[] ipArray;

    /** 监控指标类型 */
    @Excel(name = "监控指标类型")
    private String indexType;
    private String[] indexTypeArray;

    private String systemCode;
    private String[] systemCodeArray;

    private String objectName;

    private String insName;

    private Integer limit;
    private Integer offset;
}
