package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

public class OgGroupPerson extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 工作组ID */
    @Excel(name = "工作组序号", cellType = ColumnType.NUMERIC)
    private String groupId;

    /** 人员ID */
    @Excel(name = "人员序号")
    private String pid;

    private String pname;

    /** 工作组成员id */
    @Excel(name = "工作组成员序号")
    private String gpId;

    /** 工作组位置 */
    @Excel(name = "工作组位置")
    private String gpOsition ;

    /** 工作组顺序 */
    @Excel(name = "工作组顺序")
    private String gpOrder;

    // 关联到用户
    private OgPerson person;

    private String orgId;// 机构id
    private String orgName;// 机构名称
    private OgOrg ogOrg;// 机构

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public OgGroupPerson(){}

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getGpId() {
        return gpId;
    }

    public void setGpId(String gpId) {
        this.gpId = gpId;
    }

    public String getGpOsition() {
        return gpOsition;
    }

    public void setGpOsition(String gpOsition) {
        this.gpOsition = gpOsition;
    }

    public String getGpOrder() {
        return gpOrder;
    }

    public void setGpOrder(String gpOrder) {
        this.gpOrder = gpOrder;
    }

    public OgPerson getPerson() {
        return person;
    }

    public void setPerson(OgPerson person) {
        this.person = person;
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

    public OgOrg getOgOrg() {
        return ogOrg;
    }

    public void setOgOrg(OgOrg ogOrg) {
        this.ogOrg = ogOrg;
    }
}
