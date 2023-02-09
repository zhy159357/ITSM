package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 代理人配置对象 og_deputy_cfg
 * 
 * @author admin
 * @date 2022-12-12
 */
public class OgDeputyCfg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 更新时间 */
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updatedBy;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createdBy;

    /** 结束时间 */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    /** 开始时间 */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 代理人 */
    @Excel(name = "代理人")
    private String secondary;

    /** 负责人 */
    @Excel(name = "负责人")
    private String director;

    /** 配置来源类型 */
    @Excel(name = "配置来源类型")
    private Long cfgType;

    /** ID */
    @Excel(name = "ID")
    private Long id;

    /** 代理人 */
    private String secondaryId;

    /** 负责人 */
    private String directorId;
    
    /** 代理人 */
    private String secondaryName;

    /** 负责人 */
    private String directorName;
    
    public String getSecondaryName() {
		return secondaryName;
	}

	public void setSecondaryName(String secondaryName) {
		this.secondaryName = secondaryName;
	}

	public String getDirectorName() {
		return directorName;
	}

	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}

	public String getSecondaryId() {
		return secondaryId;
	}

	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}

	public String getDirectorId() {
		return directorId;
	}

	public void setDirectorId(String directorId) {
		this.directorId = directorId;
	}

	public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }
    public void setUpdatedBy(String updatedBy) 
    {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy() 
    {
        return updatedBy;
    }
    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }
    public void setCreatedBy(String createdBy) 
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() 
    {
        return createdBy;
    }
    public void setEndTime(String endTime) 
    {
        this.endTime = endTime;
    }

    public String getEndTime() 
    {
        return endTime;
    }
    public void setStartTime(String startTime) 
    {
        this.startTime = startTime;
    }

    public String getStartTime() 
    {
        return startTime;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setSecondary(String secondary) 
    {
        this.secondary = secondary;
    }

    public String getSecondary() 
    {
        return secondary;
    }
    public void setDirector(String director) 
    {
        this.director = director;
    }

    public String getDirector() 
    {
        return director;
    }
    public void setCfgType(Long cfgType) 
    {
        this.cfgType = cfgType;
    }

    public Long getCfgType() 
    {
        return cfgType;
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
            .append("updatedTime", getUpdatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("createdTime", getCreatedTime())
            .append("createdBy", getCreatedBy())
            .append("endTime", getEndTime())
            .append("startTime", getStartTime())
            .append("status", getStatus())
            .append("secondary", getSecondary())
            .append("director", getDirector())
            .append("cfgType", getCfgType())
            .append("id", getId())
            .toString();
    }
}
