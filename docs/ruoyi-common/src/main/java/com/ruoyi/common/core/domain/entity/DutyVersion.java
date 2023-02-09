package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 版本对象 duty_version
 * @author dayong_sun
 * @date 2020-01-19
 */
public class DutyVersion implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String versionId;

    /** 值班信息id */
    private String schedulingId;

    /** 版本编号 */
    private String versionNo;

    /** 版本名称 */
    private String versionName;

    /** 值班日期 */
    private String dutyDate;

    /** 创建时间 */
    private String addTime;

    /** 修改时间 */
    private String updateTime;

    /** 备注 */
    private String remark;

    /** id */
    private String versionTypeinfoId;

    /** TypeinfoId */
    private String TypeinfoId;

    /** pId */
    private String pid;

    /** pname */
    private String pname;

    /** 类别编号 */
    private String typeNo;

    /** 类别名称 */
    private String typeName;

    /** 父类名称 */
    private String parentName;

    /** 类别描述 */
    private String typeDescription;

    /** 值班手机 */
    private String mobilephone;

    /** 主值类别 */
    private String leader;

    /** 无效标志 */
    private String invalidationMark;

    /** 序列号 */
    private Long serial;

    /** 父类型 */
    private String parentId;

    private String dutyNumber;

    private String typeRows;

    private String typeColumns;

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVersionTypeinfoId() {
        return versionTypeinfoId;
    }

    public void setVersionTypeinfoId(String versionTypeinfoId) {
        this.versionTypeinfoId = versionTypeinfoId;
    }

    public String getTypeinfoId() {
        return TypeinfoId;
    }

    public void setTypeinfoId(String typeinfoId) {
        TypeinfoId = typeinfoId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(String typeNo) {
        this.typeNo = typeNo;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public Long getSerial() {
        return serial;
    }

    public void setSerial(Long serial) {
        this.serial = serial;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDutyNumber() {
        return dutyNumber;
    }

    public void setDutyNumber(String dutyNumber) {
        this.dutyNumber = dutyNumber;
    }

    public String getTypeRows() {
        return typeRows;
    }

    public void setTypeRows(String typeRows) {
        this.typeRows = typeRows;
    }

    public String getTypeColumns() {
        return typeColumns;
    }

    public void setTypeColumns(String typeColumns) {
        this.typeColumns = typeColumns;
    }

    public String getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(String schedulingId) {
        this.schedulingId = schedulingId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
