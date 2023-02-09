package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * chmData对象 chm_basedata
 * 
 * @author zhangxu
 * @date 2022-11-19
 */
public class ChmBasedata extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long custId;

    /** 外联ID */
    @Excel(name = "外联ID")
    private Long parentId;

    /** 名称 */
    @Excel(name = "名称")
    private String orgName;

    /** id */
    private Long id;

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setCustId(Long custId) 
    {
        this.custId = custId;
    }

    public Long getCustId() 
    {
        return custId;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setOrgName(String orgName) 
    {
        this.orgName = orgName;
    }

    public String getOrgName() 
    {
        return orgName;
    }
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("address", getAddress())
            .append("custId", getCustId())
            .append("parentId", getParentId())
            .append("orgName", getOrgName())
            .append("id", getId())
            .toString();
    }
}
