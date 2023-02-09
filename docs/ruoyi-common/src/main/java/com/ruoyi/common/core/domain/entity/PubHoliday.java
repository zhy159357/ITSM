package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PubHoliday extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /** 节假日ID */
    private String holidayId;

    /** 使用范围（1整个系统 2省中心审核） */
    @Excel(name = "使用范围", readConverterExp = "1=整个系统,2=省中心审核")
    private String scope;

    /** 日期 */
    @Excel(name = "节假日")
    private String day;


    /** 节假日名称*/
    @Excel(name = "节假日名称")
    private String name;

    /** 类别（1休息 2上班） */
    @Excel(name = "类别", readConverterExp = "1=休息,2=上班")
    private String type;


    /** 备注*/
    @Excel(name = "节假日备注")
    private String memo;

    /** 使用机构ID */
    @Excel(name = "使用机构ID", cellType = Excel.ColumnType.NUMERIC)
    private Long agencyId;

    /** 使用机构名称*/
    @Excel(name = "使用机构名称")
    private String agencyName;

    public String getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(String holidayId) {
        this.holidayId = holidayId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("holidayId", getHolidayId())
                .append("scope", getScope())
                .append("day", getDay())
                .append("name", getName())
                .append("type", getType())
                .append("memo", getMemo())
                .append("agencyId", getAgencyId())
                .append("agencyName", getAgencyName())

                .toString();

    }
}
