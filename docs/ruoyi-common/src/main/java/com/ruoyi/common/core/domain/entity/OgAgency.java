package com.ruoyi.common.core.domain.entity;

/**
 * OgAgency
 *
 * @author dongdong_Liu
 * @date 2021-04-08
 */
public class OgAgency {

    private String orgid;//机构ID

    private String orgcode;//机构编号

    private String orgname;//机构名称

    private OgAgency parent;//上级机构

    private String orglv;//机构等级

    private String levelCode;//等级编号

    private String branchMark;//总部标记

    private String inoutsideMark;//内外标记

    private String invalidationMark;//状态

    private String adder;//添加人

    private String addtime;//添加时间

    private String moder;//修改人

    private String updateTime;//更新时间

    private String memo;//备注

    private String leaf;//是否叶子节点

    private String type;// 类型 00省 01 02信息局

    private String sort;// 类别 1核心类2支撑类

    private String operaterNum;// 运维总人数

    private String khnum;//厂商序号

    private String isCenter;//是否全国中心机构

    private String pCode;//所属省机构code

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public OgAgency getParent() {
        return parent;
    }

    public void setParent(OgAgency parent) {
        this.parent = parent;
    }

    public String getOrglv() {
        return orglv;
    }

    public void setOrglv(String orglv) {
        this.orglv = orglv;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getBranchMark() {
        return branchMark;
    }

    public void setBranchMark(String branchMark) {
        this.branchMark = branchMark;
    }

    public String getInoutsideMark() {
        return inoutsideMark;
    }

    public void setInoutsideMark(String inoutsideMark) {
        this.inoutsideMark = inoutsideMark;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getAdder() {
        return adder;
    }

    public void setAdder(String adder) {
        this.adder = adder;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getModer() {
        return moder;
    }

    public void setModer(String moder) {
        this.moder = moder;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getLeaf() {
        return leaf;
    }

    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOperaterNum() {
        return operaterNum;
    }

    public void setOperaterNum(String operaterNum) {
        this.operaterNum = operaterNum;
    }

    public String getKhnum() {
        return khnum;
    }

    public void setKhnum(String khnum) {
        this.khnum = khnum;
    }

    public String getIsCenter() {
        return isCenter;
    }

    public void setIsCenter(String isCenter) {
        this.isCenter = isCenter;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }
}
