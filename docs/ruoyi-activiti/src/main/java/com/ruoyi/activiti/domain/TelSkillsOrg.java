package com.ruoyi.activiti.domain;

public class TelSkillsOrg {

    //主键
    private String id;
    //机构id
    private String ogOrgId;
    //技能组名称
    private String skillsGroupTelname;
    //技能组别名
    private String skillsGroupName;
    //状态
    private String skillsStatus;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;
    //创建人
    private String createPerson;

    //人员
    private String pName;

    //机构名称
    private String orgName;

    //技能组id
    private String skillId;

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    private String levelCode;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOgOrgId() {
        return ogOrgId;
    }

    public void setOgOrgId(String ogOrgId) {
        this.ogOrgId = ogOrgId;
    }

    public String getSkillsGroupTelname() {
        return skillsGroupTelname;
    }

    public void setSkillsGroupTelname(String skillsGroupTelname) {
        this.skillsGroupTelname = skillsGroupTelname;
    }

    public String getSkillsGroupName() {
        return skillsGroupName;
    }

    public void setSkillsGroupName(String skillsGroupName) {
        this.skillsGroupName = skillsGroupName;
    }

    public String getSkillsStatus() {
        return skillsStatus;
    }

    public void setSkillsStatus(String skillsStatus) {
        this.skillsStatus = skillsStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }
}
