package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 同步用户表 service_user
 *
 * @author dayong_sun
 */
public class SynchronizeUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 应用系统ID */
    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
    private Long id;

    /** 应用系统名称 */
    @Excel(name = "服务名称")
    private String servicename;

    /** 应用系统url */
    @Excel(name = "服务url")
    private String serviceurl;

    /** 应用系统url */
    @Excel(name = "服务参数")
    private String jsonParam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getServiceurl() {
        return serviceurl;
    }

    public void setServiceurl(String serviceurl) {
        this.serviceurl = serviceurl;
    }

    public String getJsonParam() {
        return jsonParam;
    }

    public void setJsonParam(String jsonParam) {
        this.jsonParam = jsonParam;
    }
}
