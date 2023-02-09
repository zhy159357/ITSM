package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【新建工作表】对象 itsm_work_status
 *
 * @author ruoyi
 * @date 2021-06-25
 */
public class ItsmWorkStatus extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private String workId;

    /** 登录人id */
    @Excel(name = "登录人id")
    private String pid;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 工作状态 */
    @Excel(name = "工作状态")
    private String workStatus;

    /** 操作时间 */
    @Excel(name = "操作时间")
    private String operateTime;

    /**
     * 标签
     */
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setWorkId(String workId)
    {
        this.workId = workId;
    }

    public String getWorkId()
    {
        return workId;
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
    public void setWorkStatus(String workStatus)
    {
        this.workStatus = workStatus;
    }

    public String getWorkStatus()
    {
        return workStatus;
    }
    public void setOperateTime(String operateTime)
    {
        this.operateTime = operateTime;
    }

    public String getOperateTime()
    {
        return operateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("workId", getWorkId())
                .append("pid", getPid())
                .append("userId", getUserId())
                .append("workStatus", getWorkStatus())
                .append("operateTime", getOperateTime())
                .toString();
    }
}
