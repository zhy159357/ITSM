package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工作组信息
 */
public class OgGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 工作组ID */
    @Excel(name = "工作组序号", cellType = ColumnType.NUMERIC)
    private String groupId;

    /** 工作组名称 */
    @Excel(name = "工作组名称")
    private String grpName;

    /** 工作组领导 */
    @Excel(name = "工作组领导")
    private String grpLeader;

    /** 无效标志 */
    @Excel(name = "无效标志", cellType = ColumnType.NUMERIC)
    private Long invalidationMark ;

    /** 备注 */
    @Excel(name = "备注")
    private String memo;

    /** 部门id */
    @Excel(name = "部门id", cellType = ColumnType.NUMERIC)
    private String orgId;

    @Excel(name = "计数")
    private String count;

    /** 电话 */
    @Excel(name = "电话")
    private String tel;

    /** 系统 */
    @Excel(name = "系统", cellType = ColumnType.NUMERIC)
    private String sysId;

    /** 白名单 */
    @Excel(name = "白名单")
    private String whiteList;

    private String adder;
    private String addTime;
    private String moder;
    private String modTime;

    //userid
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String orgName;// 机构名称
    private OgOrg ogOrg;// 关联到机构
    private OgSys ogSys;// 所属应用系统
    private OgGroupPerson groupperson;// 所属应用系统

    private String createTimeStr;// 创建时间字符串
    private String updateTimeStr;// 修改时间字符串
    private String groupType;

    private String levelCode;

    private String sName;//应用系统名称

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    //是否选中标识
    private boolean flag = false;


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    public OgGroupPerson getGroupperson() {
        return groupperson;
    }

    public void setGroupperson(OgGroupPerson groupperson) {
        this.groupperson = groupperson;
    }

    public OgGroup(){}

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getGrpLeader() {
        return grpLeader;
    }

    public void setGrpLeader(String grpLeader) {
        this.grpLeader = grpLeader;
    }

    public Long getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(Long invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
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

    public OgSys getOgSys() {
        return ogSys;
    }

    public void setOgSys(OgSys ogSys) {
        this.ogSys = ogSys;
    }

    public String getAdder() {
        return adder;
    }

    public void setAdder(String adder) {
        this.adder = adder;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getModer() {
        return moder;
    }

    public void setModer(String moder) {
        this.moder = moder;
    }

    public String getModTime() {
        return modTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }
}
