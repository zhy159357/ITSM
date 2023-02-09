package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @author Administrator
 * @title: SysUpgradForm
 * @projectName ruoyi
 * @description: TODO
 * @date 2022/3/910:18
 */
public class SysUpgradForm extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /*主键*/
    private String id;
    /*编号*/
    private String sysNumber;
    /*应用系统名称*/
    private String sysName;
    /*应用系统承建单位*/
    private String sysContractor;
    /*项目负责人*/
    private String itemLeader;
    /*联系方式*/
    private String tel;
    /*申请日期*/
    private String applyDate;
    /*业务部门*/
    private String dept;
    /*当前版本*/
    private String currentVersion;
    /*升级后版本*/
    private String upgradedVersion;
    /*相关系统*/
    private String relatedSystems;
    /*系统升级开始时间*/
    private String startTime;
    /*系统升级完成时间*/
    private String endTime;
    /*升级原因*/
    private String upgradeReason;
    /*升级内容*/
    private String upgradeContent;
    /*备注*/
    private String remarks;
    /*项目负责人签字*/
    private String leaderSign;
    /*项目负责人签字日期*/
    private String leaderSignTime;
    /*相关系统意见*/
    private String sysOpinion;
    /*相关系统意见日期*/
    private String sysOpinionTime;
    /*总集单位审批意见*/
    private String collectiveUnitOpinion;
    /*总集单位审批意见日期*/
    private String collectiveUnitTime;
    /*技术局审批意见*/
    private String bureauUnitOpinion;
    /*创建时间*/
    private String creatTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysNumber() {
        return sysNumber;
    }

    public void setSysNumber(String sysNumber) {
        this.sysNumber = sysNumber;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysContractor() {
        return sysContractor;
    }

    public void setSysContractor(String sysContractor) {
        this.sysContractor = sysContractor;
    }

    public String getItemLeader() {
        return itemLeader;
    }

    public void setItemLeader(String itemLeader) {
        this.itemLeader = itemLeader;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getUpgradedVersion() {
        return upgradedVersion;
    }

    public void setUpgradedVersion(String upgradedVersion) {
        this.upgradedVersion = upgradedVersion;
    }

    public String getRelatedSystems() {
        return relatedSystems;
    }

    public void setRelatedSystems(String relatedSystems) {
        this.relatedSystems = relatedSystems;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUpgradeReason() {
        return upgradeReason;
    }

    public void setUpgradeReason(String upgradeReason) {
        this.upgradeReason = upgradeReason;
    }

    public String getUpgradeContent() {
        return upgradeContent;
    }

    public void setUpgradeContent(String upgradeContent) {
        this.upgradeContent = upgradeContent;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLeaderSign() {
        return leaderSign;
    }

    public void setLeaderSign(String leaderSign) {
        this.leaderSign = leaderSign;
    }

    public String getLeaderSignTime() {
        return leaderSignTime;
    }

    public void setLeaderSignTime(String leaderSignTime) {
        this.leaderSignTime = leaderSignTime;
    }

    public String getSysOpinion() {
        return sysOpinion;
    }

    public void setSysOpinion(String sysOpinion) {
        this.sysOpinion = sysOpinion;
    }

    public String getSysOpinionTime() {
        return sysOpinionTime;
    }

    public void setSysOpinionTime(String sysOpinionTime) {
        this.sysOpinionTime = sysOpinionTime;
    }

    public String getCollectiveUnitOpinion() {
        return collectiveUnitOpinion;
    }

    public void setCollectiveUnitOpinion(String collectiveUnitOpinion) {
        this.collectiveUnitOpinion = collectiveUnitOpinion;
    }

    public String getCollectiveUnitTime() {
        return collectiveUnitTime;
    }

    public void setCollectiveUnitTime(String collectiveUnitTime) {
        this.collectiveUnitTime = collectiveUnitTime;
    }

    public String getBureauUnitOpinion() {
        return bureauUnitOpinion;
    }

    public void setBureauUnitOpinion(String bureauUnitOpinion) {
        this.bureauUnitOpinion = bureauUnitOpinion;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public String toString() {
        return "SysUpgradForm{" +
                "id='" + id + '\'' +
                ", sysNumber='" + sysNumber + '\'' +
                ", sysName='" + sysName + '\'' +
                ", sysContractor='" + sysContractor + '\'' +
                ", itemLeader='" + itemLeader + '\'' +
                ", tel='" + tel + '\'' +
                ", applyDate='" + applyDate + '\'' +
                ", dept='" + dept + '\'' +
                ", currentVersion='" + currentVersion + '\'' +
                ", upgradedVersion='" + upgradedVersion + '\'' +
                ", relatedSystems='" + relatedSystems + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", upgradeReason='" + upgradeReason + '\'' +
                ", upgradeContent='" + upgradeContent + '\'' +
                ", remarks='" + remarks + '\'' +
                ", leaderSign='" + leaderSign + '\'' +
                ", leaderSignTime='" + leaderSignTime + '\'' +
                ", sysOpinion='" + sysOpinion + '\'' +
                ", sysOpinionTime='" + sysOpinionTime + '\'' +
                ", collectiveUnitOpinion='" + collectiveUnitOpinion + '\'' +
                ", collectiveUnitTime='" + collectiveUnitTime + '\'' +
                ", bureauUnitOpinion='" + bureauUnitOpinion + '\'' +
                ", creatTime='" + creatTime + '\'' +
                '}';
    }
}
