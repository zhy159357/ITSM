package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 参数列别对象 duty_typeinfo
 * @author dayong_sun
 * @date 2020-01-19
 */
public class DutyTypeinfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String typeinfoId;

    /** pid */
    private String pid;

    /** pid */
    private String pname;

    /** 类别编号 */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String typeNo;

    /** 类别名称 */
    private String typeName;

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

    private String updateTime;

    private String dutyNumber;

    private String typeRows;

    private String typeColumns;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    private String parentTypeName;// 上级类别页面展示用

    private String parentName;// 上级类别页面展示用

    public String getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(String typeNo) {
        this.typeNo = typeNo;
    }

    public void setTypeinfoId(String typeinfoId)
    {
        this.typeinfoId = typeinfoId;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName() 
    {
        return typeName;
    }

    public String getTypeinfoId() {
        return typeinfoId;
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

    public void setSerial(Long serial)
    {
        this.serial = serial;
    }

    public Long getSerial() 
    {
        return serial;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getParentId() 
    {
        return parentId;
    }

    public String getParentTypeName() {
        return parentTypeName;
    }

    public void setParentTypeName(String parentTypeName) {
        this.parentTypeName = parentTypeName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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
}
