package com.ruoyi.common.core.domain.entity;

//技能组接受参数
public class SkillList {

    //机构编号(租户Id)
    private String orgCode;
    //版本号
    private String vers;
    //请求id
    private String reqId;

    private String dataType;
    private String opType;

    private String type;
    private String value;
    private String code;
    private String ifSkillNo;
    private String skillsGroupName;

    private String offset;
    private String limit;
    private String name;

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIfSkillNo() {
        return ifSkillNo;
    }

    public void setIfSkillNo(String ifSkillNo) {
        this.ifSkillNo = ifSkillNo;
    }

    public String getSkillsGroupName() {
        return skillsGroupName;
    }

    public void setSkillsGroupName(String skillsGroupName) {
        this.skillsGroupName = skillsGroupName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getVers() {
        return vers;
    }

    public void setVers(String vers) {
        this.vers = vers;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    @Override
    public String toString() {
        return "SkillList{" +
                "orgCode='" + orgCode + '\'' +
                ", vers='" + vers + '\'' +
                ", reqId='" + reqId + '\'' +
                ", dataType='" + dataType + '\'' +
                ", opType='" + opType + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", code='" + code + '\'' +
                ", ifSkillNo='" + ifSkillNo + '\'' +
                ", skillsGroupName='" + skillsGroupName + '\'' +
                ", offset='" + offset + '\'' +
                ", limit='" + limit + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
