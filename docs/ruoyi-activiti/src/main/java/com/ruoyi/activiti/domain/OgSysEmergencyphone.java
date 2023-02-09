package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 og_sys_emergencyphone
 *
 * @author ruoyi
 * @date 2021-07-28
 */
public class OgSysEmergencyphone extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 系统ID */
    private String sysId;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String sysName;

    /** 人员id */
    private String pid;

    /** 人员姓名 */
    @Excel(name = "姓名")
    private String pname;

    /** 固话 */
    private String phone;

    /** 移动电话 */
    @Excel(name = "手机号码")
    private String mobilPhone;

    /** 创建时间 */
    private String addTime;

    /** 更新时间 */
    private String editTime;

    /** ID */
    private String emId;

    /** 有效标志 */
    private String invalipationMark;

    /** 部门 */
    @Excel(name = "部门")
    private String orgName;

    /** 岗位 */
    @Excel(name = "职务（岗位）")
    private String roleName;

    /** 更新人 */
    private String updatePerson;

    public void setSysId(String sysId)
    {
        this.sysId = sysId;
    }

    public String getSysId()
    {
        return sysId;
    }
    public void setSysName(String sysName)
    {
        this.sysName = sysName;
    }

    public String getSysName()
    {
        return sysName;
    }
    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getPid()
    {
        return pid;
    }
    public void setPname(String pname)
    {
        this.pname = pname;
    }

    public String getPname()
    {
        return pname;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setMobilPhone(String mobilPhone)
    {
        this.mobilPhone = mobilPhone;
    }

    public String getMobilPhone()
    {
        return mobilPhone;
    }
    public void setAddTime(String addTime)
    {
        this.addTime = addTime;
    }

    public String getAddTime()
    {
        return addTime;
    }
    public void setEmId(String emId)
    {
        this.emId = emId;
    }

    public String getEmId()
    {
        return emId;
    }
    public void setInvalipationMark(String invalipationMark)
    {
        this.invalipationMark = invalipationMark;
    }

    public String getInvalipationMark()
    {
        return invalipationMark;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("sysId", getSysId())
                .append("sysName", getSysName())
                .append("pid", getPid())
                .append("pname", getPname())
                .append("phone", getPhone())
                .append("mobilPhone", getMobilPhone())
                .append("addTime", getAddTime())
                .append("updateTime", getUpdateTime())
                .append("emId", getEmId())
                .append("invalipationMark", getInvalipationMark())
                .toString();
    }
}
