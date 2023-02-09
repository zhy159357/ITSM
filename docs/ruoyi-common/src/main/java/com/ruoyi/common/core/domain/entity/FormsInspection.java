package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 报表管理对象 forms_inspection
 *
 * @author ruoyi
 * @date 2022-3-8
 */
@Data
public class FormsInspection {
    /**
     * 主键
     */
    private String id;

    /**
     * 应用系统名称版本
     */
    private String sysName;

    /**
     * 应用系统运维单位
     */
    private String sysCompany;

    /**
     * 巡检人员
     */
    private String inspectors;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 系统负责人
     */
    private String sysManager;

    /**
     * 巡检项最后更新日期
     */
    private String lastDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 登录页面是否正常(0：异常；1：正常)
     */
    private String loginStates;

    /**
     * 登录页面是否正常说明
     */
    private String loginAnnotation;

    /**
     * 数据统计页是否正常(0：异常；1：正常)
     */
    private String dateStates;

    /**
     * 数据统计页是否正常说明
     */
    private String dateAnnotation;

    /**
     * 通讯录管理页面是否正常(0：异常；1：正常)
     */
    private String bookStates;

    /**
     * 通讯录管理页面是否正常说明
     */
    private String bookAnnotation;

    /**
     * 应用管理页面是否正常(0：异常；1：正常)
     */
    private String appStates;

    /**
     * 应用管理页面是否正常说明
     */
    private String appAnnotation;

    /**
     * 全局设置页面是否正常(0：异常；1：正常)
     */
    private String globalStates;

    /**
     * 全局设置页面是否正常说明
     */
    private String globalAnnotation;

    /**
     * 权限管理页面是否正常(0：异常；1：正常)
     */
    private String limitStates;

    /**
     * 权限管理页面是否正常说明
     */
    private String limitAnnotation;

    /**
     * 批量上传页面是否正常(0：异常；1：正常)
     */
    private String uploadStates;

    /**
     * 批量上传页面是否正常说明
     */
    private String uploadAnnotation;

    /**
     * 服务器操作系统是否正常(0：异常；1：正常)
     */
    private String osStates;

    /**
     * 服务器操作系统是否正常说明
     */
    private String osAnnotation;

    /**
     * 服务器网络是否正常(0：异常；1：正常)
     */
    private String networkStates;

    /**
     * 服务器网络是否正常说明
     */
    private String networkAnnotation;

    /**
     * 应用分系统访问是否正常(0：异常；1：正常)
     */
    private String accessStates;

    /**
     * 应用分系统访问是否正常说明
     */
    private String accessAnnotation;

    /**
     * 数据库是否正常(0：异常；1：正常)
     */
    private String databaseStates;

    /**
     * 数据库是否正常说明
     */
    private String databaseAnnotation;

    /**
     * 备注
     */
    private String remark;

//excel报表导出注解
//    @Excel(name = "发生机构", sort = 18)
//    private String orgName;
}
