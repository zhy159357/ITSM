package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据变更短信通知关联对象 og_r_pyw_rywcz
 * 
 * @author ruoyi
 * @date 2021-06-29
 */
public class OgRPywRywcz extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ywid;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String czName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String czPhone;

    public void setYwid(String ywid) 
    {
        this.ywid = ywid;
    }

    public String getYwid() 
    {
        return ywid;
    }
    public void setCzName(String czName) 
    {
        this.czName = czName;
    }

    public String getCzName() 
    {
        return czName;
    }
    public void setCzPhone(String czPhone) 
    {
        this.czPhone = czPhone;
    }

    public String getCzPhone() 
    {
        return czPhone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ywid", getYwid())
            .append("czName", getCzName())
            .append("czPhone", getCzPhone())
            .toString();
    }
}
