package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * og_org
 *
 * @author zhang_chao
 * @date 2020-12-09
 */
public class OgOrg extends BaseEntity
{
    /**机构id*/
    private String orgId;
    /**父机构id*/
    private String parentId;
    /**机构编号*/
    private String orgCode;
    /**机构名称*/
    private String orgName;
    /**机构等级*/
    private String orgLv;
    /**添加人*/
    private String adder;
    /**添加时间*/
    private String addtime;
    /**修改人*/
    private String moder;
    /**修改时间*/
    private String modtime;
    /**修改时间*/
    private String updatetime;
    /**无效标志*/
    private String invalidationMark;
    /**备注*/
    private String memo;
    /**分布标记*/
    private String branchMark;
    /**进出标记*/
    private String inoutsideMark;
    /**等级编号*/
    private String levelCode;
    /**是否叶子节点*/
    private String leaf;
    private String type;
    private String sort;
    private String operaternum;
    /**考核厂商序号*/
    private String khnum;
    /**父部门名称*/
    private String parentName;

    private String orgLeader;
    //是否选中标识
    private boolean flag = false;

    public String getOrgLeader() {
        return orgLeader;
    }

    public void setOrgLeader(String orgLeader) {
        this.orgLeader = orgLeader;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgLv() {
        return orgLv;
    }

    public void setOrgLv(String orgLv) {
        this.orgLv = orgLv;
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

    public String getModtime() {
        return modtime;
    }

    public void setModtime(String modtime) {
        this.modtime = modtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
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

    public String getOperaternum() {
        return operaternum;
    }

    public void setOperaternum(String operaternum) {
        this.operaternum = operaternum;
    }

    public String getKhnum() {
        return khnum;
    }

    public void setKhnum(String khnum) {
        this.khnum = khnum;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}