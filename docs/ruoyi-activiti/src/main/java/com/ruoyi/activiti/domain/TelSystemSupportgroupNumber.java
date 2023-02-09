package com.ruoyi.activiti.domain;

public class TelSystemSupportgroupNumber {

    //主键
    private String id;
    //公司名称
    private String orgid;
    //系统名称
    private String sysid;
    //对外支持组编号
    private String supportGroupCode;
    //对外支持组名称
    private String supportGroupName;
    //事件单组编号
    private String sjGroupCode;
    //事件单组名称
    private String sjGroupName;
    //创建人
    private String createPerson;
    //创建时间
    private String createTime;
    //修改人
    private String updatePerson;
    //修改时间
    private String updateTime;

    //机构名称
    private String orgname;
    //系统名称
    private String caption;
    //人员名称
    private String pName;

    //标识
    private String showFlag;

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getSupportGroupCode() {
        return supportGroupCode;
    }

    public void setSupportGroupCode(String supportGroupCode) {
        this.supportGroupCode = supportGroupCode;
    }

    public String getSupportGroupName() {
        return supportGroupName;
    }

    public void setSupportGroupName(String supportGroupName) {
        this.supportGroupName = supportGroupName;
    }

    public String getSjGroupCode() {
        return sjGroupCode;
    }

    public void setSjGroupCode(String sjGroupCode) {
        this.sjGroupCode = sjGroupCode;
    }

    public String getSjGroupName() {
        return sjGroupName;
    }

    public void setSjGroupName(String sjGroupName) {
        this.sjGroupName = sjGroupName;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
