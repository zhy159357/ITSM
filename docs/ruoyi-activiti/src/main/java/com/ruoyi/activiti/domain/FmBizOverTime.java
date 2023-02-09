package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FmBizOverTime extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 单号
     */
    @Excel(name = "单号", sort = 3)
    private String singelNumber;

    /**
     * 日期
     */
    @Excel(name = "考核年月", sort = 1)
    private String datetime;

    /**
     * 所涉系统ID
     */

    private String systemId;

    @Excel(name = "系统", sort = 2)
    private String sysName;

    /**
     * 所占权重
     */
    @Excel(name = "所占权重", sort = 4)
    private String proportion;

    /**
     * 是否紧急  1是  2否
     */
    @Excel(name = "是否紧急", sort = 5, readConverterExp = "1=是,2=否")
    private String ifjjtype;

    /**
     * $column.columnComment
     */
    private String id;

    public void setSingelNumber(String singelNumber) {
        this.singelNumber = singelNumber;
    }

    public String getSingelNumber() {
        return singelNumber;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getProportion() {
        return proportion;
    }

    public void setIfjjtype(String ifjjtype) {
        this.ifjjtype = ifjjtype;
    }

    public String getIfjjtype() {
        return ifjjtype;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("singelNumber", getSingelNumber())
                .append("datetime", getDatetime())
                .append("systemId", getSystemId())
                .append("proportion", getProportion())
                .append("ifjjtype", getIfjjtype())
                .append("id", getId())
                .toString();
    }
}
