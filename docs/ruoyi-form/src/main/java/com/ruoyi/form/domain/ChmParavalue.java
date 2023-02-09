package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 基础数据对象 chm_paravalue
 * 
 * @author ruoyi
 * @date 2022-10-24
 */
public class ChmParavalue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 父ID */
    @Excel(name = "父ID")
    private Long parentId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** id */
    private Long id;
    private String levels;
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
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
    public Long getId()
    {
        return id;
    }

    public void setLevels(String levels)
    {
        this.levels = levels;
    }
    public String getLevels()
    {
        return levels;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("parentId", getParentId())
            .append("name", getName())
            .append("id", getId())
                .append("levels",getLevels())
            .toString();
    }
}
