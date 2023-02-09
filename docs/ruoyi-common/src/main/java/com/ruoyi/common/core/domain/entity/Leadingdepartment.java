package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 牵头部门信息
 */
public class Leadingdepartment extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /** 牵头部门ID */
    private String lpid;
// 部门id
    private String orgId;
// 部门名称
    private String orgName;
    // 父类部门名称
    private String porgName;



    public String getLpid() {
        return lpid;
    }

    public void setLpid(String lpid) {
        this.lpid = lpid;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPorgName() {
        return porgName;
    }

    public void setPorgName(String porgName) {
        this.porgName = porgName;
    }
}
