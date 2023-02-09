package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 厂商考核对象 task_assess_system
 * 
 * @author ruoyi
 * @date 2022-01-20
 */
public class TaskAssessSystem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 满意度 */
    @Excel(name = "满意度")
    private String manyidu;

    /** 任务类别 */
    @Excel(name = "任务类别")
    private String taskType;

    /** id */
    private String id;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 用户id */
    private String userId;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String sysName;

    /** 系统id */
    private String sysId;

    /** 签到 */
    @Excel(name = "签到")
    private String qianDao;

    /** 已完成 */
    @Excel(name = "已完成")
    private String end;

    /** 未完成 */
    @Excel(name = "未完成")
    private String unEnd;

    /** 超时 */
    @Excel(name = "超时")
    private String timeOut;

    public void setManyidu(String manyidu) 
    {
        this.manyidu = manyidu;
    }

    public String getManyidu() 
    {
        return manyidu;
    }
    public void setTaskType(String taskType) 
    {
        this.taskType = taskType;
    }

    public String getTaskType() 
    {
        return taskType;
    }
    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setSysName(String sysName) 
    {
        this.sysName = sysName;
    }

    public String getSysName() 
    {
        return sysName;
    }
    public void setSysId(String sysId) 
    {
        this.sysId = sysId;
    }

    public String getSysId() 
    {
        return sysId;
    }
    public void setQianDao(String qianDao) 
    {
        this.qianDao = qianDao;
    }

    public String getQianDao() 
    {
        return qianDao;
    }
    public void setEnd(String end) 
    {
        this.end = end;
    }

    public String getEnd() 
    {
        return end;
    }
    public void setUnEnd(String unEnd) 
    {
        this.unEnd = unEnd;
    }

    public String getUnEnd() 
    {
        return unEnd;
    }
    public void setTimeOut(String timeOut) 
    {
        this.timeOut = timeOut;
    }

    public String getTimeOut() 
    {
        return timeOut;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("manyidu", getManyidu())
            .append("taskType", getTaskType())
            .append("id", getId())
            .append("userName", getUserName())
            .append("userId", getUserId())
            .append("sysName", getSysName())
            .append("sysId", getSysId())
            .append("qianDao", getQianDao())
            .append("end", getEnd())
            .append("unEnd", getUnEnd())
            .append("timeOut", getTimeOut())
            .toString();
    }
}
