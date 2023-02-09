package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【版本升级记录】对象 sys_deployment_version
 * 
 * @author ruoyi
 * @date 2021-11-18
 */
public class SysDeploymentVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String versionNo;

    /** $column.columnComment */
    private String versionContent;

    /** $column.columnComment */
    private String sysName;

    /** $column.columnComment */
    private String versionShow;

    /** $column.columnComment */
    private String deploymentTime;

    /** $column.columnComment */
    private String createrId;

    /** $column.columnComment */
    private String dvId;

    public void setVersionNo(String versionNo) 
    {
        this.versionNo = versionNo;
    }

    public String getVersionNo() 
    {
        return versionNo;
    }
    public void setVersionContent(String versionContent) 
    {
        this.versionContent = versionContent;
    }

    public String getVersionContent() 
    {
        return versionContent;
    }
    public void setSysName(String sysName) 
    {
        this.sysName = sysName;
    }

    public String getSysName() 
    {
        return sysName;
    }
    public void setVersionShow(String versionShow) 
    {
        this.versionShow = versionShow;
    }

    public String getVersionShow() 
    {
        return versionShow;
    }
    public void setDeploymentTime(String deploymentTime) 
    {
        this.deploymentTime = deploymentTime;
    }

    public String getDeploymentTime() 
    {
        return deploymentTime;
    }
    public void setCreaterId(String createrId) 
    {
        this.createrId = createrId;
    }

    public String getCreaterId() 
    {
        return createrId;
    }
    public void setDvId(String dvId) 
    {
        this.dvId = dvId;
    }

    public String getDvId() 
    {
        return dvId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("versionNo", getVersionNo())
            .append("versionContent", getVersionContent())
            .append("sysName", getSysName())
            .append("versionShow", getVersionShow())
            .append("deploymentTime", getDeploymentTime())
            .append("createrId", getCreaterId())
            .append("dvId", getDvId())
            .toString();
    }
}
