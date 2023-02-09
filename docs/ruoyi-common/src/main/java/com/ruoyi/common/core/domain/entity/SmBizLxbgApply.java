package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;

import java.io.Serializable;

/**
 * 例行变更计划申请延期单
 */

public class SmBizLxbgApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 申请id */
    private String id;

    /** 计划单id */
    @Excel(name = "计划单id")
    private String schedulingId;

    /** 创建人id */
    @Excel(name = "创建人id")
    private String createId;

    /** 原因 */
    @Excel(name = "原因")
    private String reason;

    /** 延期时间 */
    @Excel(name = "计划发布时间")
    private String releaseTime;

    /** 处长审核人id */
    @Excel(name = "处长审核人id")
    private String checkId;

    /** 状态 1:待处长审核  2：计划审核人审核  3:关闭   4:处长审核不通过   5.计划人审核不通过 */
    @Excel(name = "状态")
    private String appType;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createTime;

    /**
     * 通过标识  true通过  false不通过
     */
    private String passFlag;

    /** 处长审核意见 */
    @Excel(name = "处长审核意见")
    private String ctorChecktext;

    /** 计划审核人审核意见 */
    @Excel(name = "计划审核人审核意见")
    private String jhChecktext;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(String schedulingId) {
        this.schedulingId = schedulingId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getPassFlag() {
        return passFlag;
    }

    public void setPassFlag(String passFlag) {
        this.passFlag = passFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCtorChecktext() {
        return ctorChecktext;
    }

    public void setCtorChecktext(String ctorChecktext) {
        this.ctorChecktext = ctorChecktext;
    }

    public String getJhChecktext() {
        return jhChecktext;
    }

    public void setJhChecktext(String jhChecktext) {
        this.jhChecktext = jhChecktext;
    }
}
