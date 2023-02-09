package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.FmBiz;

public class OgPhone {

    @Excel(name = "单号ID")
    private String phoneId;
    @Excel(name = "事件单号")
    private String phoneName;
    @Excel(name = "来电人")
    private String phoneMan;
    @Excel(name = "来电号码")
    private String phoneNumber;
    @Excel(name = "来电机构")
    private String orgId;
    @Excel(name = "应用系统")
    private String xtId;
    @Excel(name = "状态")
    private String phoneState;

    @Excel(name = "创建时间")
    private String startTime;
    private String endTime;

    private String phoneType;
    private String phoneMemo;

    private String markName;

    //后来添加的字段
    private String phoneShift;
    private String phoneFound;
    private String phoneFirm;
    private String phoneAffair;

    private String phoneGroup;
    private String phoneRunout;
    private String runoutTime;
    private String isorNo;
    private String workType;

    //业务事件单的实体类
    private FmBiz fmBiz;

    public FmBiz getFmBiz() {
        return fmBiz;
    }

    public void setFmBiz(FmBiz fmBiz) {
        this.fmBiz = fmBiz;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getPhoneGroup() {
        return phoneGroup;
    }

    public void setPhoneGroup(String phoneGroup) {
        this.phoneGroup = phoneGroup;
    }

    public String getPhoneRunout() {
        return phoneRunout;
    }

    public void setPhoneRunout(String phoneRunout) {
        this.phoneRunout = phoneRunout;
    }

    public String getRunoutTime() {
        return runoutTime;
    }

    public void setRunoutTime(String runoutTime) {
        this.runoutTime = runoutTime;
    }

    public String getIsorNo() {
        return isorNo;
    }

    public void setIsorNo(String isorNo) {
        this.isorNo = isorNo;
    }

    public String getPhoneShift() {
        return phoneShift;
    }

    public void setPhoneShift(String phoneShift) {
        this.phoneShift = phoneShift;
    }

    public String getPhoneFound() {
        return phoneFound;
    }

    public void setPhoneFound(String phoneFound) {
        this.phoneFound = phoneFound;
    }

    public String getPhoneFirm() {
        return phoneFirm;
    }

    public void setPhoneFirm(String phoneFirm) {
        this.phoneFirm = phoneFirm;
    }

    public String getPhoneAffair() {
        return phoneAffair;
    }

    public void setPhoneAffair(String phoneAffair) {
        this.phoneAffair = phoneAffair;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneMemo() {
        return phoneMemo;
    }

    public void setPhoneMemo(String phoneMemo) {
        this.phoneMemo = phoneMemo;
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

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneMan() {
        return phoneMan;
    }

    public void setPhoneMan(String phoneMan) {
        this.phoneMan = phoneMan;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getXtId() {
        return xtId;
    }

    public void setXtId(String xtId) {
        this.xtId = xtId;
    }

    public String getPhoneState() {
        return phoneState;
    }

    public void setPhoneState(String phoneState) {
        this.phoneState = phoneState;
    }

}
