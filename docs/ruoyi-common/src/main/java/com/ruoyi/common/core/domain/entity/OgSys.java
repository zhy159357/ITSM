package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.Domain;
import com.ruoyi.common.core.domain.BaseEntity;

import java.sql.Time;
import java.util.List;

public class OgSys implements Domain {
    private String sysId;
    private String adpmId;//对接adpm系统的sysid
    private String orgId;// 机构

    private boolean flag = false;//是否选中标识

    private String code;// 编码

    private String caption;// 系统名称

    private String memo;// 备注

    private String invalidationMark;// 无效标识

    private String isExamination;// 是否参与考核

    private String dept;// 所属处室

    private String pid;// 系统负责人

    private String businessId;// 所属业务部门

    private String isKeySys;// 是否重要系统

    private String sysType;// 系统类型

    private String orgName;// 所属机构名称

    private String businessName;// 业务部门名称

    private String createBy;

    private String updateBy;

    private String adder;// 添加人

    private String addTime;// 添加时间

    private String moder;// 人名

    private String updateTime;// 修改时间

    private String XtId;//关联到应急事件单=应用系统

    private OgOrg ogOrg;// 关联到机构

    private OgOrg ogBusiness;// 关联到业务部门

    private OgPerson ogPerson;//人员信息

    private String platform_number;//系统编码

    private String upgradeTime;//基线

    /*是否分页参数，1-是，2否*/
    private String isPage;

    private String systemStage;//所处阶段(是否转运维)

    private String FrontlineDate;//转一线日期

    private String SecondlineDate;//转二线日期

    /**是否外联系统渠道标识*/
    private String isOutChannel;

    /**作为备用字段*/
    private String remark;
    private String redlineTime;


    @TableField(exist = false)
    private List<String> sysids;

    public void setSysids(List<String> sysids) {
        this.sysids = sysids;
    }

    public List<String> getSysids() {
        return sysids;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSystemStage() {
        return systemStage;
    }

    public void setSystemStage(String systemStage) {
        this.systemStage = systemStage;
    }

    public String getFrontlineDate() {
        return FrontlineDate;
    }

    public void setFrontlineDate(String frontlineDate) {
        FrontlineDate = frontlineDate;
    }

    public String getSecondlineDate() {
        return SecondlineDate;
    }

    public void setSecondlineDate(String secondlineDate) {
        SecondlineDate = secondlineDate;
    }

    public String getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(String upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public String getPlatform_number() {
        return platform_number;
    }

    public void setPlatform_number(String platform_number) {
        this.platform_number = platform_number;
    }

    public OgPerson getOgPerson() {
        return ogPerson;
    }

    public void setOgPerson(OgPerson ogPerson) {
        this.ogPerson = ogPerson;
    }

    public String getAdpmId() {
        return adpmId;
    }

    public void setAdpmId(String adpmId) {
        this.adpmId = adpmId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getIsExamination() {
        return isExamination;
    }

    public void setIsExamination(String isExamination) {
        this.isExamination = isExamination;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getIsKeySys() {
        return isKeySys;
    }

    public void setIsKeySys(String isKeySys) {
        this.isKeySys = isKeySys;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public OgOrg getOgOrg() {
        return ogOrg;
    }

    public void setOgOrg(OgOrg ogOrg) {
        this.ogOrg = ogOrg;
    }

    public OgOrg getOgBusiness() {
        return ogBusiness;
    }

    public void setOgBusiness(OgOrg ogBusiness) {
        this.ogBusiness = ogBusiness;
    }

    public String getXtId() {
        return XtId;
    }

    public void setXtId(String xtId) {
        XtId = xtId;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getIsPage() {
        return isPage;
    }

    public void setIsPage(String isPage) {
        this.isPage = isPage;
    }

    public String getIsOutChannel() {
        return isOutChannel;
    }

    public void setIsOutChannel(String isOutChannel) {
        this.isOutChannel = isOutChannel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRedlineTime() {
        return redlineTime;
    }

    public void setRedlineTime(String redlineTime) {
        this.redlineTime = redlineTime;
    }
}
