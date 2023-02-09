package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 监控值班对象 og_person_duty
 * 
 * @author ruoyi
 * @date 2021-06-25
 */
public class OgPersonDuty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 值班表id */
    private String dutyId;

    /** 人员id */
    @Excel(name = "人员id")
    private String pid;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 工作组表id */
    @Excel(name = "工作组表id")
    private String groupId;

    /** 人员-监控类型关联表id */
    @Excel(name = "人员-监控类型关联表id")
    private String monitorTypeAccountId;

    /** 值班组类型 01-工作组  02-监控类型 */
    private String dutyType;

    /** 值班组名称（工作组名称｜监控类型名称） */
    private String dutyName;

    /** 添加值班组时间 */
    private Date operateTime;
    /**联系方式**/
    private String tel;
    /**签到状态**/
    private String status;
    /**签到时间**/
    private String statusTime;

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setDutyId(String dutyId)
    {
        this.dutyId = dutyId;
    }

    public String getDutyId() 
    {
        return dutyId;
    }
    public void setPid(String pid) 
    {
        this.pid = pid;
    }

    public String getPid() 
    {
        return pid;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setGroupId(String groupId) 
    {
        this.groupId = groupId;
    }

    public String getGroupId() 
    {
        return groupId;
    }
    public void setMonitorTypeAccountId(String monitorTypeAccountId) 
    {
        this.monitorTypeAccountId = monitorTypeAccountId;
    }

    public String getMonitorTypeAccountId() 
    {
        return monitorTypeAccountId;
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dutyId", getDutyId())
            .append("pid", getPid())
            .append("userId", getUserId())
            .append("groupId", getGroupId())
            .append("monitorTypeAccountId", getMonitorTypeAccountId())
            .append("dutyType", getDutyType())
            .append("dutyName", getDutyName())
            .append("operateTime", getOperateTime())
            .toString();
    }
}
