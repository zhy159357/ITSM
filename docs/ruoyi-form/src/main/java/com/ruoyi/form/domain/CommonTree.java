package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * commonTree对象 common_tree
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public class CommonTree extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 类型0机构1组2其他 */
    @Excel(name = "类型0机构1组2其他")
    private String type;

    /** 使用工单类型1问题，变更2事件3其他 */
    @Excel(name = "使用工单类型1问题，变更2事件3其他")
    private String belongTo;

    /** 父级ID */
    @Excel(name = "父级ID")
    private String parentId;

    /** 机构和组ID */
    @Excel(name = "机构和组ID")
    private String ogId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 备注 */
    @Excel(name = "备注")
    private String descs;

    /** 机构标识，区分一线、二线、业务部门 */
    @Excel(name = "机构标识")
    private String orgFlag;

    /** 主键 */
    private Long id;

    public void setDescs(String descs)
    {
        this.descs = descs;
    }

    public String getDescs()
    {
        return descs;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setBelongTo(String belongTo) 
    {
        this.belongTo = belongTo;
    }

    public String getBelongTo() 
    {
        return belongTo;
    }
    public void setParentId(String parentId) 
    {
        this.parentId = parentId;
    }

    public String getParentId() 
    {
        return parentId;
    }
    public void setOgId(String ogId) 
    {
        this.ogId = ogId;
    }

    public String getOgId() 
    {
        return ogId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setId(Long id) 
    {
        this.id = id;
    }

    public String getOrgFlag() {
        return orgFlag;
    }

    public void setOrgFlag(String orgFlag) {
        this.orgFlag = orgFlag;
    }

    public Long getId()
    {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("desc", getDescs())
            .append("type", getType())
            .append("belongTo", getBelongTo())
            .append("parentId", getParentId())
            .append("ogId", getOgId())
            .append("name", getName())
            .append("id", getId())
            .toString();
    }
}
